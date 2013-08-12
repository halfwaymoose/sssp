package com.halfwaymoose.sssp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;

public class DB extends SQLiteOpenHelper {

	public static final String DB_NAME = "db";
	public static final int DB_VERSION = 1;

	public static final String TBL_GROUP = "groups";
	public static final String TBL_GROUP_ID = "_id";
	public static final String TBL_GROUP_NAME = "name";
	public static final String TBL_GROUP_COLOR = "color";
	public static final String TBL_GROUP_PHONE = "phone";
	public static final String TBL_GROUP_NOTIFY = "notify";
	public static final String TBL_GROUP_MEDIA = "media";
	public static final String TBL_GROUP_ALARM = "alarm";
	public static final String TBL_GROUP_VIB_PHONE = "v_phone";
	public static final String TBL_GROUP_VIB_NOTIFY = "v_notify";
	
	public static final String TBL_TIMES = "times";
	public static final String TBL_TIMES_ID = "_id";
	public static final String TBL_TIMES_GROUP_ID = "g_id";
	public static final String TBL_TIMES_START = "start";
	public static final String TBL_TIMES_LENGTH = "length";
	
	public DB(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String create;
		
		create = "CREATE TABLE " + TBL_GROUP + " (" +
				TBL_GROUP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				TBL_GROUP_NAME + " TEXT, " +
				TBL_GROUP_COLOR + " INTEGER, " +
				TBL_GROUP_PHONE + " INTEGER, " +
				TBL_GROUP_NOTIFY + " INTEGER, " +
				TBL_GROUP_MEDIA + " INTEGER, " + 
				TBL_GROUP_ALARM + " INTEGER, " +
				TBL_GROUP_VIB_PHONE + " BOOLEAN, " +
				TBL_GROUP_VIB_NOTIFY + " BOOLEAN);";
		db.execSQL(create);
		
		create = "CREATE TABLE " + TBL_TIMES + " (" +
				TBL_TIMES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				TBL_TIMES_GROUP_ID + " INTEGER, " +
				TBL_TIMES_START + " INTEGER, " +
				TBL_TIMES_LENGTH + " INTEGER);";
		db.execSQL(create);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String drop;
		
		drop = "DROP TABLE " + TBL_GROUP;
		db.execSQL(drop);
		
		drop = "DROP TABLE " + TBL_TIMES;
		db.execSQL(drop);
		
		onCreate(db);
		
	}
	
	public long insertGroup(String name, int color, 
			int phone, int notify, int media, int alarm, boolean vibPhone, boolean vibNotify) {
		SQLiteDatabase db;
		ContentValues values;
		long ret;
		
		values = new ContentValues();
		values.put(TBL_GROUP_NAME, name);
		values.put(TBL_GROUP_COLOR, color);
		values.put(TBL_GROUP_PHONE, phone);
		values.put(TBL_GROUP_NOTIFY, notify);
		values.put(TBL_GROUP_MEDIA, media);
		values.put(TBL_GROUP_ALARM, alarm);
		values.put(TBL_GROUP_VIB_PHONE, vibPhone);
		values.put(TBL_GROUP_VIB_NOTIFY, vibNotify);
		
		db = getWritableDatabase();
		ret = db.insert(TBL_GROUP, null, values);
		
		db.close();
		return ret;
		
	}
	
	public long insertTime(long groupID, long start, long length) {
		SQLiteDatabase db;
		ContentValues values;
		long ret;
		
		values = new ContentValues();
		values.put(TBL_TIMES_GROUP_ID, groupID);
		values.put(TBL_TIMES_START, start);
		values.put(TBL_TIMES_LENGTH, length);
		
		db = getWritableDatabase();
		ret = db.insert(TBL_TIMES, null, values);
		
		db.close();
		return ret;
		
	}
	
	public long updateGroup(long id, String name, int color, 
			int phone, int notify, int media, int alarm, boolean vibPhone, boolean vibNotify) {
		SQLiteDatabase db;
		ContentValues values;
		long ret;
		
		values = new ContentValues();
		values.put(TBL_GROUP_NAME, name);
		values.put(TBL_GROUP_COLOR, color);
		values.put(TBL_GROUP_PHONE, phone);
		values.put(TBL_GROUP_NOTIFY, notify);
		values.put(TBL_GROUP_MEDIA, media);
		values.put(TBL_GROUP_ALARM, alarm);
		values.put(TBL_GROUP_VIB_PHONE, vibPhone);
		values.put(TBL_GROUP_VIB_NOTIFY, vibNotify);
		
		db = getWritableDatabase();
		ret = db.update(TBL_GROUP, values, TBL_GROUP_ID + "=" + id, null);
		
		db.close();
		return ret;
		
	}
	
	public long updateTime(long id, long groupID, long start, long length) {
		SQLiteDatabase db;
		ContentValues values;
		long ret;
		
		values = new ContentValues();
		values.put(TBL_TIMES_GROUP_ID, groupID);
		values.put(TBL_TIMES_START, start);
		values.put(TBL_TIMES_LENGTH, length);

		db = getWritableDatabase();
		ret = db.update(TBL_TIMES, values, TBL_TIMES_ID + "=" + id, null);
		
		db.close();
		return ret;
		
	}
	
	public long deleteGroup(long id) {
		SQLiteDatabase db;
		long ret;
		
		db = getWritableDatabase();
		ret = db.delete(TBL_GROUP, TBL_GROUP_ID + "=" + id,  null);
		
		db.close();
		return ret;
		
	}
	
	public long deleteTime(long id) {
		SQLiteDatabase db;
		long ret;
		
		db = getWritableDatabase();
		ret = db.delete(TBL_TIMES, TBL_TIMES_ID + "=" + id,  null);
		
		db.close();
		return ret;
		
	}
	
	public Cursor getGroup(long id) {
		SQLiteDatabase db;
		Cursor cursor;
		String selection;
		
		selection = TBL_GROUP_ID + " = " + id;
		
		db = getReadableDatabase();
		cursor = db.query(TBL_GROUP, null, selection, null, null, null, null);
		
		if (cursor.getCount() > 0) {
			
			cursor.moveToFirst();
			
			return cursor;
			
		} else {
			
			cursor.close();
			db.close();
			
			return null;
			
		}
		
	}
	
	public Cursor getGroups() {
		SQLiteDatabase db;
		Cursor cursor;
		
		db = getReadableDatabase();
		cursor = db.query(TBL_GROUP, null, null, null, null, null, null);
		
		if (cursor.getCount() > 0) {
			
			cursor.moveToFirst();
			
			return cursor;
			
		} else {
			
			cursor.close();
			db.close();
			
			return null;
			
		}
		
	}
	
	public Cursor getTime(long id) {
		SQLiteDatabase db;
		Cursor cursor;
		String selection;
		
		selection = TBL_TIMES_ID + " = " + id;
		
		db = getReadableDatabase();
		cursor = db.query(TBL_TIMES, null, selection, null, null, null, null);
		
		if (cursor.getCount() > 0) {
			
			cursor.moveToFirst();
			
			return cursor;
			
		} else {
			
			cursor.close();
			db.close();
			
			return null;
			
		}
		
	}
	
	public Cursor getTimes(long groupID) {
		SQLiteDatabase db;
		Cursor cursor;
		String selection;
		
		selection = TBL_TIMES_GROUP_ID + " = " + groupID;
		
		db = getReadableDatabase();
		cursor = db.query(TBL_TIMES, null, selection, null, null, null, null);
		
		if (cursor.getCount() > 0) {
			
			cursor.moveToFirst();
			
			return cursor;
			
		} else {
			
			cursor.close();
			db.close();
			
			return null;
			
		}
		
	}
	
}

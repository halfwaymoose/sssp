package com.halfwaymoose.sssp.utils;

import java.util.Calendar;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBUtils extends DB {

	public DBUtils(Context context) {
		super(context);
		
	}
	
	public Cursor getCurrentTime(long utc) {
		SQLiteDatabase db;
		Cursor cursor;
		String selection, orderBy, limit;
		Long time;
		
		time = stripDate(utc);
		
		selection = time + " >= " + TBL_TIMES_START + " AND (" + 
				TBL_TIMES_START + " + " + TBL_TIMES_LENGTH + ") >= " + time;
		orderBy = TBL_TIMES_START + " ASC";
		limit = "1";
		
		db = getReadableDatabase();
		cursor = db.query(TBL_TIMES, null, selection, null, null, null, orderBy, limit);
		
		if (cursor.getCount() > 0) {
			
			cursor.moveToFirst();
			return cursor;
			
		} else {
			
			cursor.close();
			db.close();
			return null;
			
		}
		
	}
	
	public Cursor getCurrentGroup(long utc) {
		SQLiteDatabase db;
		Cursor cursor;
		String selection, orderBy, limit;
		String[] columns;
		Long time;
		
		time = stripDate(utc);
		
		columns = new String[] {TBL_TIMES_GROUP_ID};
		selection = time + " >= " + TBL_TIMES_START + " AND (" + 
				TBL_TIMES_START + " + " + TBL_TIMES_LENGTH + ") >= " + time;
		orderBy = TBL_TIMES_START + " ASC";
		limit = "1";
		
		db = getReadableDatabase();
		cursor = db.query(TBL_TIMES, columns, selection, null, null, null, orderBy, limit);
		
		if (cursor.getCount() > 0) {
			long groupID;
			
			cursor.moveToFirst();
			groupID = cursor.getLong(cursor.getColumnIndex(TBL_TIMES_GROUP_ID));
			
			return getGroup(groupID);
			
		} else {
			
			cursor.close();
			db.close();
			return null;
			
		}
		
	}
	
	public Cursor getNextTime(long utc) {
		SQLiteDatabase db;
		Cursor cursor;
		String selection, orderBy, limit;
		Long time;
		
		time = stripDate(utc);
		
		selection = TBL_TIMES_START + " >= " + time;
		orderBy = TBL_TIMES_START + " ASC";
		limit = "1";
		
		db = getReadableDatabase();
		cursor = db.query(TBL_TIMES, null, selection, null, null, null, orderBy, limit);
		
		if (cursor.getCount() > 0) {
			
			cursor.moveToFirst();
			return cursor;
			
		} else {
			
			cursor.close();
			db.close();
			return null;
			
		}
		
	}
	
	public Cursor getNextGroup(long utc) {
		SQLiteDatabase db;
		Cursor cursor;
		String selection, orderBy, limit;
		String[] columns;
		Long time;
		
		time = stripDate(utc);
		
		columns = new String[] {TBL_TIMES_GROUP_ID};
		selection = TBL_TIMES_START + " >= " + time;
		orderBy = TBL_TIMES_START + " ASC";
		limit = "1";
		
		db = getReadableDatabase();
		cursor = db.query(TBL_TIMES, columns, selection, null, null, null, orderBy, limit);
		
		if (cursor.getCount() > 0) {
			long groupID;
			
			cursor.moveToFirst();
			groupID = cursor.getLong(cursor.getColumnIndex(TBL_TIMES_GROUP_ID));
			
			return getGroup(groupID);
			
		} else {
			
			cursor.close();
			db.close();
			return null;
			
		}
		
	}
	
	public long getRemaining(long id, long utc) {
		Cursor cursor;
		long remaining;
	
		cursor = getTime(id);
		
		if (cursor != null) {
			long start, length, endUTC;
			
			start = cursor.getLong(cursor.getColumnIndex(TBL_TIMES_START));
			length = cursor.getLong(cursor.getColumnIndex(TBL_TIMES_LENGTH));
			
			endUTC = toUTC(start) + length;
			remaining = endUTC - utc;
			
			cursor.close();
			return remaining;
			
		} else {
			
			// null
			return -1;
			
		}
	}
	
	public long getTimeTill(long id, long utc) {
		Cursor cursor;
		long delta;
	
		cursor = getTime(id);
		
		if (cursor != null) {
			long start, startUTC;
			
			start = cursor.getLong(cursor.getColumnIndex(TBL_TIMES_START));
			startUTC = toUTC(start);
			
			delta = startUTC - utc;
			
			cursor.close();
			return delta;
			
		} else {
			
			// null
			return -1;
			
		}
	}

	private long stripDate(long utc) {
		Calendar calendar;
		int day, hour, minute, second;
		long delta;
		
		calendar = Calendar.getInstance();
		calendar.setTimeInMillis(utc);
		
		day = calendar.get(Calendar.DAY_OF_WEEK);
		hour = calendar.get(Calendar.HOUR_OF_DAY);
		minute = calendar.get(Calendar.MINUTE);
		second = calendar.get(Calendar.SECOND);

		delta = hour * 60 * 60 * 1000;
		delta = delta + (minute * 60 * 1000);
		delta = delta + (second * 1000);
		delta = delta * day;
		
		return delta;
		
	}
	
	private long toUTC(long time) {
		Calendar calendar;
		int day, hour, minute, second;
		int delta;
		
		calendar = Calendar.getInstance();

		day = calendar.get(Calendar.DAY_OF_WEEK);
		hour = calendar.get(Calendar.HOUR_OF_DAY);
		minute = calendar.get(Calendar.MINUTE);
		second = calendar.get(Calendar.SECOND);

		delta = hour * 60 * 60 * 1000;
		delta = delta + (minute * 60 * 1000);
		delta = delta + (second * 1000);
		delta = delta * day;
		
		calendar.add(Calendar.MILLISECOND, -delta);
		calendar.add(Calendar.MILLISECOND, (int) time);
		
		return calendar.getTimeInMillis();
		
	}
	
}

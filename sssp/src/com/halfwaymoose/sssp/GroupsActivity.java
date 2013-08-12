package com.halfwaymoose.sssp;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.halfwaymoose.sssp.utils.DB;

public class GroupsActivity extends ListActivity implements OnClickListener {

	private Button mButtonAdd;
	private Button mButtonUp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_groups);

		init();

	}

	private void init() {

		mButtonAdd = (Button) findViewById(R.id.activity_groups_btn_add);
		mButtonAdd.setOnClickListener(this);

		mButtonUp = (Button) findViewById(R.id.activity_groups_btn_up);
		mButtonUp.setOnClickListener(this);

		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				selectGroup(id);

			}

		});
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {

				// TODO edit group
				DB db = new DB(GroupsActivity.this);
				db.deleteGroup(id);

				Cursor cursor = db.getGroups();
				loadCursor(cursor);

				return false;
			}
		});
		
	}

	public void loadCursor(Cursor cursor) {

		MyListAdapter adapter;

		adapter = new MyListAdapter(this, cursor);

		setListAdapter(adapter);

	}

	@Override
	protected void onResume() {
		super.onResume();
		DB db;
		Cursor cursor;

		db = new DB(this);
		cursor = db.getGroups();

		loadCursor(cursor);

	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {

		case R.id.activity_groups_btn_add:
			addGroup();
			break;

		case R.id.activity_groups_btn_up:
			finish();
			break;

		}
	}

	private void addGroup() {
		DB db;
		Cursor cursor;

		db = new DB(this);
		db.insertGroup("test", 1, 1, 1, 1, 1, true, true);

		cursor = db.getGroups();

		loadCursor(cursor);

	}
	
	private void selectGroup(long id) {
		Intent intent;
		
		intent = new Intent(this, TimesActivity.class);
		intent.putExtra(DB.TBL_GROUP_ID, id);
		
		startActivity(intent);
		
	}

	class MyListAdapter extends CursorAdapter {

		public MyListAdapter(Context context, Cursor c) {
			super(context, c, false);

		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			TextView textView;
			final Long id;
			String name;

			id = cursor.getLong(cursor.getColumnIndex(DB.TBL_GROUP_ID));
			name = cursor.getString(cursor.getColumnIndex(DB.TBL_GROUP_NAME));

			textView = (TextView) view;
			textView.setText(name);

		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
			TextView textView;

			textView = new TextView(context);
			textView.setTextSize(50);

			return textView;
		}

	}
}

package com.halfwaymoose.sssp;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.halfwaymoose.sssp.GroupsActivity.MyListAdapter;
import com.halfwaymoose.sssp.utils.DB;

public class TimesActivity extends ListActivity implements OnClickListener {

	private Button mButtonAdd;
	private Button mButtonUp;

	private long mGroupID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_times);
		
		Bundle extras = getIntent().getExtras();
		
		if (extras != null && extras.containsKey(DB.TBL_GROUP_ID)) {
			
			mGroupID = extras.getLong(DB.TBL_GROUP_ID);
			
		} else {
			
			// something went wrong...
			finish();
			
		}

		init();

	}

	private void init() {

		mButtonAdd = (Button) findViewById(R.id.activity_times_btn_add);
		mButtonAdd.setOnClickListener(this);

		mButtonUp = (Button) findViewById(R.id.activity_times_btn_up);
		mButtonUp.setOnClickListener(this);

		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {


			}

		});
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {

				// TODO edit group
				DB db = new DB(TimesActivity.this);
				db.deleteTime(id);

				Cursor cursor = db.getTimes(mGroupID);
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
		cursor = db.getTimes(mGroupID);

		loadCursor(cursor);

	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {

		case R.id.activity_times_btn_add:
			addTime();
			break;

		case R.id.activity_times_btn_up:
			finish();
			break;

		}
	}

	private void addTime() {
		DB db;
		Cursor cursor;

		db = new DB(this);
		db.insertTime(mGroupID, 1000, 1000);

		cursor = db.getTimes(mGroupID);

		loadCursor(cursor);

	}

	class MyListAdapter extends CursorAdapter {

		public MyListAdapter(Context context, Cursor c) {
			super(context, c, false);

		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			TextView textView;
			Long id;

			id = cursor.getLong(cursor.getColumnIndex(DB.TBL_TIMES_ID));

			textView = (TextView) view;
			textView.setText(id.toString());

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

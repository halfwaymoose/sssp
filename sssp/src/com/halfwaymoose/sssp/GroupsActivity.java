package com.halfwaymoose.sssp;

import java.util.Properties;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
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
		
		// TODO edit group
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				String details;
				
				details = (String) view.getTag();
				
				deleteGroup(id, details);

				return false;
			}
		});
		
	}

	public void loadCursor() {
		MyListAdapter adapter;
		DB db;
		Cursor cursor;
		
		db = new DB(this);
		cursor = db.getGroups();
		adapter = (MyListAdapter) getListAdapter();
		
		if (adapter != null) {
		
			adapter.changeCursor(cursor);
		} else {
			
			adapter = new MyListAdapter(this, cursor);
			setListAdapter(adapter);	
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	
		loadCursor();
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

	// TODO
	private void addGroup() {
		NewGroupDialog newGroupDialog;
		
		newGroupDialog = new NewGroupDialog(this);
		newGroupDialog.show();
		
	}
	
	private void deleteGroup(final long id, String details) {
		AlertDialog.Builder builder;
		
		builder = new AlertDialog.Builder(this);
		builder.setTitle("Delete Group");
		builder.setMessage("Are you sure you wante to delete " + details);
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				DB db;
				
				db = new DB(GroupsActivity.this);
				db.deleteGroup(id);
				db.deleteTimesByGroup(id);
				
				db.close();
				dialog.dismiss();
				
				loadCursor();
			}
		});	
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				dialog.dismiss();
			}
		});

		builder.create().show();
	}
	
	// TODO startActivityForResult?
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

		// TODO
		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			Button btnColor;
			TextView txtName;
			String name;
			int color;
			
			txtName = (TextView) view.findViewById(R.id.list_item_group_txt_name);
			btnColor = (Button) view.findViewById(R.id.list_item_group_btn_color);
			
			name = cursor.getString(cursor.getColumnIndex(DB.TBL_GROUP_NAME));
			color = cursor.getInt(cursor.getColumnIndex(DB.TBL_GROUP_COLOR));
			
			txtName.setText(name);
			btnColor.setBackgroundColor(color);
			view.setTag(name);
		}

		// TODO
		@Override
		public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
			View view;
			
			view = LayoutInflater.from(context).inflate(R.layout.list_item_group, null, false);
			
			return view;
		}
	}

	class NewGroupDialog extends Dialog {
		
		private Button mBtnOK;
		private Button mBtnCancel;
		private EditText mTxtName;
		private Button mBtnColor;
		private SeekBar mSkColor;
		private TextView mTxtPhone;
		private SeekBar mSkPhone;
		private TextView mTxtNotify;
		private SeekBar mSkNotify;
		private TextView mTxtMedia;
		private SeekBar mSkMedia;
		private TextView mTxtAlarm;
		private SeekBar mSkAlarm;
		private CheckBox mChkPhoneVib;
		private CheckBox mChkNotifyVib;
		
		private int mPhoneMax;
		private int mNotifyMax;
		private int mMediaMax;
		private int mAlarmMax;
		
		public NewGroupDialog(Context context) {
			super(context);
			
		}
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			this.setContentView(R.layout.dialog_newgroup);
			this.setTitle("New Group");
			AudioManager audioManager;
			
			audioManager = (AudioManager) GroupsActivity.this.getSystemService(Context.AUDIO_SERVICE);
			mPhoneMax = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
			mNotifyMax = audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
			mMediaMax = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			mAlarmMax = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
			
			mTxtName = (EditText) this.findViewById(R.id.dialog_newgroup_txt_name);
			
			mBtnColor = (Button) this.findViewById(R.id.dialog_newgroup_btn_color);
			mBtnColor.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
				}
			});
			
			mSkColor = (SeekBar) this.findViewById(R.id.dialog_newgroup_sk_color);
			mSkColor.setMax(Math.abs(Color.BLACK));
			mSkColor.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {

					mBtnColor.setBackgroundColor(-progress);
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					
				}

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					
				}
			});
			mSkColor.setProgress(1);
			mSkColor.setProgress(0);
			
			mTxtPhone = (TextView) this.findViewById(R.id.dialog_newgroup_txt_phone_percent);
			
			// TODO
			mSkPhone = (SeekBar) this.findViewById(R.id.dialog_newgroup_sk_phone);
			mSkPhone.setMax(mPhoneMax);
			mSkPhone.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					
					if (progress > 0) {
						float percent;
						
						percent = ((float) progress / (float) mPhoneMax) * 100;
						mTxtPhone.setText(String.format("%.0f%%", percent));
					} else {
						
						mTxtPhone.setText("0%");
					}
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					
				}

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					
				}
			});
			mSkPhone.setProgress(1);	// force it to change
			mSkPhone.setProgress(0);
			
			mTxtNotify = (TextView) this.findViewById(R.id.dialog_newgroup_txt_notify_percent);
			
			// TODO
			mSkNotify = (SeekBar) this.findViewById(R.id.dialog_newgroup_sk_notify);
			mSkNotify.setMax(mNotifyMax);
			mSkNotify.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					
					if (progress > 0) {
						float percent;
						
						percent = ((float) progress / (float) mNotifyMax) * 100;
						mTxtNotify.setText(String.format("%.0f%%", percent));
					} else {
						
						mTxtNotify.setText("0%");
					}
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					
				}

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					
				}
			});
			mSkNotify.setProgress(1);	// force it to change
			mSkNotify.setProgress(0);

			mTxtMedia = (TextView) this.findViewById(R.id.dialog_newgroup_txt_media_percent);
			
			// TODO
			mSkMedia = (SeekBar) this.findViewById(R.id.dialog_newgroup_sk_media);
			mSkMedia.setMax(mMediaMax);
			mSkMedia.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					
					if (progress > 0) {
						float percent;
						
						percent = ((float) progress / (float) mMediaMax) * 100;
						mTxtMedia.setText(String.format("%.0f%%", percent));
					} else {
						
						mTxtMedia.setText("0%");
					}
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					
				}

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					
				}
			});
			mSkMedia.setProgress(1);	// force it to change
			mSkMedia.setProgress(0);
			
			mTxtAlarm = (TextView) this.findViewById(R.id.dialog_newgroup_txt_alarm_percent);
			
			// TODO
			mSkAlarm = (SeekBar) this.findViewById(R.id.dialog_newgroup_sk_alarm);
			mSkAlarm.setMax(mAlarmMax);
			mSkAlarm.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					
					if (progress > 0) {
						float percent;
						
						percent = ((float) progress / (float) mAlarmMax) * 100;
						mTxtAlarm.setText(String.format("%.0f%%", percent));
					} else {
						
						mTxtAlarm.setText("0%");
					}
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					
				}

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					
				}
			});
			mSkAlarm.setProgress(1);	// force it to change
			mSkAlarm.setProgress(0);
			
			mChkPhoneVib = (CheckBox) this.findViewById(R.id.dialog_newgroup_chk_phone_vib);
			
			mChkNotifyVib = (CheckBox) this.findViewById(R.id.dialog_newgroup_chk_notify_vib);
			
			mBtnOK = (Button) this.findViewById(R.id.dialog_newgroup_btn_ok);
			mBtnOK.setOnClickListener(new View.OnClickListener() {
				
				// TODO
				@Override
				public void onClick(View v) {
					DB db;
					String name;
					int color, phone, notify, media, alarm;
					boolean vibPhone, vibNotify;
					
					color = -mSkColor.getProgress();		// colors are < 0
					
					name = mTxtName.getText().toString();
					
					phone = mSkPhone.getProgress();
					
					notify = mSkNotify.getProgress();
					
					media = mSkNotify.getProgress();
					
					alarm = mSkAlarm.getProgress();
					
					vibPhone = mChkPhoneVib.isChecked();
					
					vibNotify = mChkNotifyVib.isChecked();
					
					db = new DB(GroupsActivity.this);
					db.insertGroup(name, color, phone, notify, media, alarm, vibPhone, vibNotify);
					
					NewGroupDialog.this.dismiss();
					
					loadCursor();
				}
			});
			
			mBtnCancel = (Button) this.findViewById(R.id.dialog_newgroup_btn_cancel);
			mBtnCancel.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					
					NewGroupDialog.this.dismiss();
				}
			});
		}
	}
}

package com.halfwaymoose.sssp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class MainActivity extends Activity implements View.OnClickListener{

	private static final int MENU_GROUPS = 0, MENU_TIMES = 1, MENU_CANCEL = 2;
	
	private TextView mTextDay;
	private TextView mTextDate;
	private TextView mTextTime;
	private Button mButtonGroups;

	private SimpleDateFormat mFormatDay;
	private SimpleDateFormat mFormatDate;
	private SimpleDateFormat mFormatTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		setup();
		
	}

	private void setup() {
		
		mTextDay = (TextView) findViewById(R.id.activity_main_txt_day);
		
		mTextDate = (TextView) findViewById(R.id.activity_main_txt_date);
		
		mTextTime = (TextView) findViewById(R.id.activity_main_txt_time);
		
		mButtonGroups = (Button) findViewById(R.id.activity_main_btn_groups);
		mButtonGroups.setOnClickListener(this);
		
		mFormatDate = new SimpleDateFormat("MM/dd/yy");
		mFormatDay = new SimpleDateFormat("EEEE");
		mFormatTime = new SimpleDateFormat("hh:mm aa");
		
	}
	
	@Override
	public void onClick(View view) {
		
		switch(view.getId()) {
		
		case R.id.activity_main_btn_groups:
			showMenu(view);
			break;
			
		default:
		
		}
		
	}
	
	@SuppressLint("NewApi")
	public void showMenu(View view) {
		PopupMenu popup;
		MenuInflater inflater;
		AlertDialog alertDialog;
		AlertDialog.Builder alertBuilder;
		
		if (Build.VERSION.SDK_INT >= 11) {
		
			popup = new PopupMenu(this, view);
			inflater = popup.getMenuInflater();
			inflater.inflate(R.menu.main, popup.getMenu());
			
			popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					
					switch (item.getItemId()) {
					
					case R.id.action_groups:
						openGroups();
						return true;
						
					case R.id.action_times:
						openTimes();
						return true;
						
					case R.id.action_cancel:
						return false;
					
					default:
						return false;
						
					}

				}
			});
			
			popup.show();
		
		} else {
			
			alertBuilder = new AlertDialog.Builder(this);
			alertBuilder.setTitle("Main Menu");
			
			alertBuilder.setItems(R.array.main_menu_items, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					switch (which) {
					
					case MENU_GROUPS:
						openGroups();
						break;
						
					case MENU_TIMES:
						openTimes();
						break;
						
					case MENU_CANCEL:
						dialog.dismiss();
						break;
						
					default:
						dialog.dismiss();
					
					}
					
				}
			});
			
			alertDialog = alertBuilder.create();
			alertDialog.show();
			
		}
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Calendar calendar;
		Date date;
		String format;
		
		calendar = Calendar.getInstance();
		date = calendar.getTime();
		
		format = mFormatDay.format(date);
		mTextDay.setText(format);
		
		format = mFormatDate.format(date);
		mTextDate.setText(format);
		
		format = mFormatTime.format(date);
		mTextTime.setText(format);
		
	}
	
	private void openGroups() {
		
		startActivity(new Intent(this, GroupsActivity.class));
		
	}
	
	private void openTimes() {

		startActivity(new Intent(this, TimesActivity.class));
		
	}
	
}

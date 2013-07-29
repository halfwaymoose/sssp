package com.halfwaymoose.sssp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class MainActivity extends Activity implements View.OnClickListener{

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
		mFormatDay = new SimpleDateFormat("MM/dd/yy");
		mFormatTime = new SimpleDateFormat("hh:mm aa");
		
	}
	
	@Override
	public void onClick(View view) {
		
		switch(view.getId()) {
		
		case R.id.activity_main_btn_groups:
			
			break;
			
		default:
		
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
	
}

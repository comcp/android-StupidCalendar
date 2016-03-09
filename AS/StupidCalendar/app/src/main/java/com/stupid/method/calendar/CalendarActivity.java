package com.stupid.method.calendar;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

import com.stupid.method.calendar.listener.IXCalendar;

public class CalendarActivity extends FragmentActivity implements
		OnClickListener {
	IXCalendar calendarView;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bton_last:
			calendarView.lastMonthOrWeek();
			break;
		case R.id.bton_next:
			calendarView.nextMonthOrWeek();
			break;
		case R.id.bton_toggle:
			// calendarView.toggle();
			break;
		case R.id.bton_today:
			calendarView.gobackToday();
			break;
		case R.id.bton_otherMonth:
			calendarView.setFilledMonth(!calendarView.isFilledMonth());
		default:
			break;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);
		 init2();
	}

	private void init2() {
		View vv = findViewById(R.id.calendarview);
		if (vv instanceof CalendarViewPager) {
			CalendarViewPager v = (CalendarViewPager) vv;
			v.init(this);
		}
		init();
	}

	private void init() {
		calendarView = (IXCalendar) findViewById(R.id.calendarview);
		findViewById(R.id.bton_last).setOnClickListener(this);
		findViewById(R.id.bton_otherMonth).setOnClickListener(this);
		findViewById(R.id.bton_next).setOnClickListener(this);
		findViewById(R.id.bton_toggle).setOnClickListener(this);
		findViewById(R.id.bton_today).setOnClickListener(this);
	}
}

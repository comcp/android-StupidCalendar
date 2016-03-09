package com.stupid.method.calendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stupid.method.calendar.listener.IXCalendar;
import com.stupid.method.calendar.listener.IXOnLongClickDayListener;
import com.stupid.method.calendar.utils.TimeUtils;

public class CalendarViewPager extends ViewPager implements IXCalendar {
	Calendar calendar = new GregorianCalendar();
	static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
	final int initCurrent = Integer.MAX_VALUE / 2;

	public void gobackToday() {

		setCurrentItem(initCurrent, false);

	}

	public CalendarViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public CalendarViewPager(Context context) {
		this(context, null);

	}

	public void init(FragmentActivity activity) {
		setAdapter(new CalendarPagerAdapter(
				activity.getSupportFragmentManager()));
		setOffscreenPageLimit(2);
		setCurrentItem(initCurrent);

	}

	public class CalendarPagerAdapter extends FragmentPagerAdapter {

		public CalendarPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

			return new F(position - initCurrent);
		}

		@Override
		public int getCount() {

			return Integer.MAX_VALUE;
		}

	}

	public class F extends Fragment {
		int offset;

		public F() {

		}

		public F(int p) {
			offset = p;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			Calendar tmp = (Calendar) calendar.clone();
			tmp.add(Calendar.MONDAY, offset);
			LinearLayout ll = new LinearLayout(inflater.getContext());

			ll.setOrientation(LinearLayout.VERTICAL);

			TextView title = new TextView(inflater.getContext());
			title.setGravity(Gravity.CENTER);
			title.setText(TimeUtils.getTime(tmp.getTimeInMillis(), format));
			ll.addView(title);
			ll.addView(new CalendarView(inflater.getContext(), tmp, true));
			return ll;
		}
	}

	@Override
	public void lastMonthOrWeek() {

		setCurrentItem(getCurrentItem() - 1);
	}

	@Override
	public void nextMonthOrWeek() {
		setCurrentItem(getCurrentItem() + 1);
	}

	@Override
	public boolean isFilledMonth() {

		return false;
	}

	@Override
	public void setFilledMonth(boolean filledMonth) {
	}

	@Override
	public void setOnLongClickDay(IXOnLongClickDayListener onLongClickDay) {
	}

}

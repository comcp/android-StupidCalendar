package com.stupid.method.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

import com.stupid.method.adapter.IXOnItemClickListener;
import com.stupid.method.adapter.IXOnItemLongClickListener;
import com.stupid.method.adapter.XAdapter2;
import com.stupid.method.calendar.bean.Day;
import com.stupid.method.calendar.holder.DayView;
import com.stupid.method.calendar.listener.IXCalendar;
import com.stupid.method.calendar.listener.IXDateExpandData;
import com.stupid.method.calendar.listener.IXOnCalendarCompleteListener;
import com.stupid.method.calendar.listener.IXOnClickDayListener;
import com.stupid.method.calendar.listener.IXOnLongClickDayListener;

public class CalendarView extends GridView implements IXCalendar,
		IXOnItemClickListener, IXOnItemLongClickListener {
	private boolean filledMonth = true;

	public static enum CalendarModel {
		MONTH, TITLE, WEEK, GONE;

	}

	public static class EventType {
		/** 多选 **/
		public final static int multi = 0x01;
		/** 区间 **/
		public final static int range = 0x02;
		/** 单选 **/
		public final static int single = 0x03;

	}

	IXOnCalendarCompleteListener completeListener;

	private static List<Day> titles = new ArrayList<Day>(7);

	private static Date TO_DAY = new Date();
	private IXOnClickDayListener onClickDay;
	private IXOnLongClickDayListener onLongClickDay;
	static {

		titles.add(new Day("日"));
		titles.add(new Day("一"));
		titles.add(new Day("二"));
		titles.add(new Day("三"));
		titles.add(new Day("四"));
		titles.add(new Day("五"));
		titles.add(new Day("六"));

	}

	private static boolean isToday(Date date) {

		if (date.getYear() == TO_DAY.getYear())
			if (date.getMonth() == TO_DAY.getMonth())
				if (date.getDate() == TO_DAY.getDate())
					return true;

		return false;

	}

	XAdapter2<Day> adapter;
	private Calendar calendar = null;

	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar c) {
		if (c == null)
			throw new NullPointerException("你傻逼啊,你设置空的干嘛");

		this.calendar = c;
		initData();
	}

	Class<? extends DayView> cellsClass = DayView.class;
	private IXDateExpandData onExpandData;

	private boolean showTitle = true;

	/** 视图类型 **/
	private CalendarModel viewModel = CalendarModel.MONTH;

	public CalendarView(Context context, Calendar calendar, boolean filledMonth) {
		super(context);
		this.filledMonth = filledMonth;
		this.calendar = calendar;
		init();

	}

	public CalendarView(Context context) {
		this(context, null);

	}

	public void gobackToday() {
		setCalendar(new GregorianCalendar());
	}

	public CalendarView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	public CalendarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private Day getDay(int type, Date date, CalendarModel model) {
		Day day = new Day();
		day.setDayType(type);
		day.setDate(date);
		day.setCalendarStyle(model);
		day.setToday(isToday(date));

		if (onExpandData != null)
			day.setExpandData(onExpandData.onDateExpandData(day));
		return day;
	}

	private List<Day> getMonthDates(Calendar current) {

		List<Day> days = new ArrayList<Day>(42);

		Calendar now = new GregorianCalendar(current.get(Calendar.YEAR),
				current.get(Calendar.MONTH), 1);

		now.setFirstDayOfWeek(Calendar.SUNDAY);

		Calendar next = (Calendar) now.clone();
		next.add(Calendar.MONTH, 1);

		Calendar last = (Calendar) now.clone();

		// 本月第一周,上个月的剩余日期
		int lastday = now.get(Calendar.DAY_OF_WEEK);
		last.add(Calendar.DAY_OF_MONTH, -lastday);
		for (int i = 1; i < lastday; i++) {
			last.add(Calendar.DAY_OF_MONTH, 1);
			days.add(getDay(Day.DAY_TYPE_LASTMONTH, last.getTime(),
					isFilledMonth() ? viewModel : CalendarModel.GONE));
		}
		// 当月日期
		do {
			days.add(getDay(Day.DAY_TYPE_CURRENTMONTH, now.getTime(), viewModel));
			now.add(Calendar.DAY_OF_MONTH, 1);
		} while (now.get(Calendar.MONTH) != next.get(Calendar.MONTH));
		// 下个月日期补全
		if (isFilledMonth())
			for (int i = days.size(); i < 42; i++) {
				days.add(getDay(Day.DAY_TYPE_NEXTMONTH, now.getTime(),
						viewModel));
				now.add(Calendar.DAY_OF_MONTH, 1);
			}
		return days;

	}

	public List<Day> getWeekDates(Calendar targe) {
		List<Day> list = new ArrayList<Day>(7);
		targe.setFirstDayOfWeek(Calendar.SUNDAY);
		Calendar weekFirst = (Calendar) targe.clone();
		weekFirst.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		for (int i = 0; i < 7; i++) {
			list.add(getDay(Day.DAY_TYPE_WEEK, weekFirst.getTime(), viewModel));
			weekFirst.add(Calendar.DAY_OF_MONTH, 1);
		}
		return list;
	}

	private void init() {
		if (calendar == null)
			calendar = new GregorianCalendar();
		setNumColumns(7);
		adapter = new XAdapter2<Day>(getContext(), null, cellsClass);
		adapter.setLongClickItemListener(this);
		adapter.setClickItemListener(this);
		setAdapter(adapter);
		initData();
	}

	private void initData() {
		adapter.clear();
		if (isShowTitle())
			adapter.addAll(titles);

		if (viewModel == CalendarModel.MONTH) {
			initMoon();
		} else if (viewModel == CalendarModel.WEEK) {
			initWeek();
		}
		adapter.notifyDataSetInvalidated();
		if (completeListener != null)
			completeListener.onCalendarComplete(getCalendar());
	}

	private void initMoon() {
		adapter.addAll(getMonthDates(calendar));
	}

	private void initWeek() {
		adapter.addAll(getWeekDates(calendar));
	}

	public boolean isShowTitle() {
		return showTitle;
	}

	/** 上个月或者上个星期 */
	public void lastMonthOrWeek() {
		switch (viewModel) {
		case MONTH:
			calendar.add(Calendar.MONTH, -1);

			break;

		case WEEK:
			calendar.add(Calendar.WEEK_OF_MONTH, -1);
			break;

		}
		initData();

	}

	/** 下个月或者下个星期 */
	public void nextMonthOrWeek() {
		switch (viewModel) {
		case MONTH:
			calendar.add(Calendar.MONTH, 1);

			break;

		case WEEK:
			calendar.add(Calendar.WEEK_OF_MONTH, 1);

			break;
		}
		initData();
	}

	public void setShowTitle(boolean showTitle) {

		if (this.showTitle == showTitle)
			return;

		this.showTitle = showTitle;
		initData();

	}

	public void setStyle(CalendarModel type) {
		viewModel = type;
		initData();

	}

	public void toggle() {
		if (viewModel == CalendarModel.WEEK)
			setStyle(CalendarModel.MONTH);
		else
			setStyle(CalendarModel.WEEK);

	}

	public IXOnClickDayListener getOnClickDay() {
		return onClickDay;
	}

	public void setOnClickDay(IXOnClickDayListener onClickDay) {
		this.onClickDay = onClickDay;
	}

	@Override
	public void onClickItem(View v, int position) {
		if (onClickDay != null)
			onClickDay.onClickDay(v, adapter.get(position));

	}

	@Override
	public boolean onLongClickItem(View v, int position) {
		if (onLongClickDay != null)
			return onLongClickDay.onLongClickDay(v, adapter.get(position));
		return false;
	}

	public IXOnLongClickDayListener getOnLongClickDay() {
		return onLongClickDay;
	}

	public void setOnLongClickDay(IXOnLongClickDayListener onLongClickDay) {
		this.onLongClickDay = onLongClickDay;
	}

	public boolean isFilledMonth() {
		return filledMonth;
	}

	/** 月份补全 **/
	public void setFilledMonth(boolean filledMonth) {
		if (this.filledMonth == filledMonth)
			return;
		this.filledMonth = filledMonth;
		initData();
	}
}

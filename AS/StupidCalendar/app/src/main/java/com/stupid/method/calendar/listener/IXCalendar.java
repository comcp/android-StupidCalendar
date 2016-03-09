package com.stupid.method.calendar.listener;

public interface IXCalendar {
	void lastMonthOrWeek();

	void nextMonthOrWeek();

	void gobackToday();

	boolean isFilledMonth();

	void setFilledMonth(boolean filledMonth);

	void setOnLongClickDay(IXOnLongClickDayListener onLongClickDay);
}

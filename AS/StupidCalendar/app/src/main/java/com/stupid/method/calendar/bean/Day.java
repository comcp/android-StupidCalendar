package com.stupid.method.calendar.bean;

import java.io.Serializable;
import java.util.Date;

import com.stupid.method.calendar.CalendarView.CalendarModel;
import com.stupid.method.calendar.utils.TimeUtils;

public class Day implements Cloneable, Serializable {

	static final Date DATE2 = new Date();
	public static final int DAY_TYPE_CURRENTMONTH = 3;
	public static final int DAY_TYPE_LASTMONTH = 2;
	public static final int DAY_TYPE_NEXTMONTH = 1;
	public static final int DAY_TYPE_TITLE = 5;
	public static final int DAY_TYPE_WEEK = 4;
	private static final long serialVersionUID = 1L;

	private CalendarModel calendarStyle;
	private Date date = DATE2;
	private int dayType = DAY_TYPE_CURRENTMONTH;
	private Object expandData;
	private String title;
	private boolean today;

	public int getWeek() {

		return date.getDay();
	}

	public Day() {

	}

	public Day(int type, Date date) {
		setDayType(type);
		setDate(date);
	}

	public Day(String title) {
		setTitle(title);
		setCalendarStyle(CalendarModel.TITLE);
		setDayType(DAY_TYPE_TITLE);
	}

	@Override
	public Object clone() {

		try {
			Day clone = (Day) super.clone();
			clone.date = (Date) date.clone();
			clone.expandData = expandData;
			clone.title = title;
			clone.dayType = dayType;

			return clone;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError(e);
		}
	}

	public CalendarModel getCalendarStyle() {
		return calendarStyle;
	}

	public Date getDate() {
		return date;
	}

	public int getDayType() {
		return dayType;
	}

	public Object getExpandData() {
		return expandData;
	}

	public String getTitle() {
		return title;
	}

	public boolean isToday() {
		return today;
	}

	public void setCalendarStyle(CalendarModel calendarStyle) {
		this.calendarStyle = calendarStyle;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setDayType(int dayType) {
		this.dayType = dayType;
	}

	public void setExpandData(Object expandData) {
		this.expandData = expandData;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setToday(boolean today) {
		this.today = today;
	}

	@Override
	public String toString() {

		return TimeUtils.getTime(date.getTime(), TimeUtils.DATE_FORMAT_DATE);
	}

}
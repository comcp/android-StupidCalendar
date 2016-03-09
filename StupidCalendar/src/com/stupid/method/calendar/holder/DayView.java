package com.stupid.method.calendar.holder;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stupid.method.adapter.XViewHolder;
import com.stupid.method.calendar.bean.Day;

public class DayView extends XViewHolder<Day> {

	public static class DayStyle {

		public static int backgroud = 0;
		public static int monthTextNext = Color.GRAY;
		public static int monthTextLast = Color.GRAY;
		public static int monthTextCurrent = Color.BLACK;
		public static int weekText = Color.BLACK;
		public static int selected = Color.BLUE;
		public static int today = Color.RED;
		public static int titleTextColor = Color.WHITE;
		public static int titleBackgroud = 0x7F3390E5;
	}

	TextView textView;

	@Override
	public int getLayoutId() {

		return 0;
	}

	@Override
	public View getView() {
		if (super.getView() == null) {
			initView();

		}
		return super.getView();
	}

	protected void initView() {
		ViewGroup root = new RelativeLayout(context) {
//			@Override
//			protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//
//				setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
//						getDefaultSize(0, heightMeasureSpec));
//
//				// Children are just made to fill our space.
//				int childWidthSize = getMeasuredWidth();
//				// 高度和宽度一样
//				heightMeasureSpec = widthMeasureSpec = MeasureSpec
//						.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
//				super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//			}

		};

		// LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
		// LayoutParams.MATCH_PARENT);
		// root.setLayoutParams(params);
		textView = new TextView(context);
		// textView.setLayoutParams(params);
		textView.setGravity(Gravity.CENTER);
		root.addView(textView, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		textView.setOnClickListener(this);
		textView.setOnLongClickListener(this);
		mRoot = root;

	}

	private void month(Day data, int position) {

		switch (data.getDayType()) {
		case Day.DAY_TYPE_LASTMONTH:

			textView.setText(data.getDate().getDate() + "");
			textView.setTextColor(DayStyle.monthTextLast);
			break;
		case Day.DAY_TYPE_NEXTMONTH:

			textView.setText(data.getDate().getDate() + "");
			textView.setTextColor(DayStyle.monthTextNext);

			break;
		case Day.DAY_TYPE_CURRENTMONTH:
			textView.setText(data.getDate().getDate() + "");
			if (data.isToday())
				textView.setTextColor(DayStyle.today);
			else
				textView.setTextColor(DayStyle.monthTextCurrent);
			break;
		default:
			break;
		}

	}

	@Override
	public void onResetView(Day data, int position) {
		textView.setVisibility(View.VISIBLE);
		textView.setBackgroundColor(0);
		textView.setText("");
		switch (data.getCalendarStyle()) {
		case TITLE:
			title(data, position);
			break;
		case MONTH:
			month(data, position);
			break;
		case WEEK:
			week(data, position);
			break;
		case GONE:
			gone(data, position);
			break;

		}
	}

	private void title(Day data, int position) {

		textView.setText(data.getTitle());
		textView.setBackgroundColor(DayStyle.titleBackgroud);
		textView.setTextColor(DayStyle.titleTextColor);

	}

	private void week(Day data, int position) {
		switch (data.getDayType()) {
		case Day.DAY_TYPE_WEEK:
			textView.setText(data.getDate().getDate() + "");
			if (data.isToday())
				textView.setTextColor(DayStyle.today);
			else
				textView.setTextColor(DayStyle.weekText);
			break;

		}

	}

	private void gone(Day data, int position) {
		textView.setVisibility(View.INVISIBLE);

	}

	@Override
	public void onCreate(Context context) {
	}

	 

	
	

}
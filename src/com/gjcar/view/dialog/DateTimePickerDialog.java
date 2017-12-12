package com.gjcar.view.dialog;

import java.util.Calendar;
import java.util.Date;

import com.gjcar.app.R;
import com.gjcar.data.data.Public_Param;
import com.gjcar.utils.TimeHelper;
import com.gjcar.utils.ToastHelper;
import com.gjcar.view.wheelview.NumericWheelAdapter;
import com.gjcar.view.wheelview.OnWheelScrollListener;
import com.gjcar.view.wheelview.WheelView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;


public class DateTimePickerDialog {

	/*选择时间*/
	private static WheelView year;
	private static WheelView month;
	private static WheelView day;
	private static WheelView hour;
	
	/*时间*/
	private static TextView f1_form_takecar_date;
	private static TextView f1_form_takecar_time;
	
	private static TextView f1_form_return_date;
	private static TextView f1_form_return_time;
	
	private static TextView f1_days;
		
	/**
	 * 弹出对话框修改时间
	 */
	public static void showDateDialog(Context context,int take_or_return,String datetime,TextView[] views) {
		
		/* 初始化时间显示空间 */
		if(take_or_return != 3){//3的时候是联动
			f1_form_takecar_date = views[0];
			f1_form_takecar_time = views[1];
			f1_form_return_date = views[2];
			f1_form_return_time = views[3];
			f1_days = views[4];
		}
				
		/* 创建对象 */
		final Dialog dialog = new Dialog(context, R.style.delete_dialog);

		/* 初始化视图 */
		View view = View.inflate(context,R.layout.dialog_f1_takecar_datepicker_wheelview, null);

		initView_DateTime(context,view, dialog, take_or_return,datetime);

		/* 初始化监听器 */
		dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {

				return false;
			}

		});

		/* 初始化监属性 */
		dialog.getWindow().setContentView(view);
		dialog.getWindow().setGravity(Gravity.BOTTOM);
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		dialog.getWindow().setLayout(wm.getDefaultDisplay().getWidth(),LinearLayout.LayoutParams.WRAP_CONTENT);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);

		/* 显示 */
		dialog.show();
	}

	private static void initView_DateTime(final Context context,View view, final Dialog dialog, final int take_or_return, final String datetime) {

		/* 初始化时间 */
		Calendar c = Calendar.getInstance();
		
		if(take_or_return == 1){
			//c.setTime(TimeHelper.getTakeTime_YMD(1));//取车时间为当前时间延后一天
			c.setTime(TimeHelper.getReturnTime_YMD(0,datetime));
		}else{
			
			if(take_or_return == 2){
				c.setTime(TimeHelper.getReturnTime_YMD(0,datetime));//还车时间：点击还车：使用还车时间本身
			}else{
				//还车时间：联动改变：在取车时间基础上加2天
				
				if (TimeHelper.ordaysIsLong88(f1_form_takecar_time.getTag().toString())==2) {
					c.setTime(TimeHelper.getReturnTime_YMD(1,datetime));					
				}else{
					c.setTime(TimeHelper.getReturnTime_YMD(2,datetime));
				}
				
			}
			
		}

		int nowYear = Calendar.getInstance().get(Calendar.YEAR);//这里设为当前年份
		int nowMonth = c.get(Calendar.MONTH) + 1;// 通过Calendar算出的月数要+1
		int nowDay = c.get(Calendar.DAY_OF_MONTH);
		int nowHour = c.get(Calendar.HOUR_OF_DAY);System.out.println("nowHour"+nowHour);
		int nowMinute = c.get(Calendar.MINUTE);

		int curYear = c.get(Calendar.YEAR) - (nowYear - 20);
		int curMonth = nowMonth - 1;
		int curDay = nowDay - 1;
		int curHour = nowHour - 1;
		int curMinute = nowMinute;

		year = (WheelView) view.findViewById(R.id.year);
		year.setAdapter(new NumericWheelAdapter(nowYear - 20, nowYear + 20));// (2016-20,
																				// 2016
																				// +
																				// 20)
		year.setLabel("年");
		year.setCyclic(true);
		year.addScrollingListener(scrollListener);
		year.setCurrentItem(curYear);

		month = (WheelView) view.findViewById(R.id.month);
		month.setAdapter(new NumericWheelAdapter(1, 12, "%02d"));
		month.setLabel("月");
		month.setCyclic(true);
		month.addScrollingListener(scrollListener);
		month.setCurrentItem(curMonth);

		day = (WheelView) view.findViewById(R.id.day);
		initDay(curYear, nowMonth);
		day.setLabel("日");
		day.setCyclic(true);
		day.setCurrentItem(curDay);

		int startTime = 0 ;
		int endTime = 0;
		if(take_or_return == 1){
			startTime = Public_Param.taketime_start;
			endTime = Public_Param.taketime_end;
		}else{
			startTime = Public_Param.returntime_start;
			endTime = Public_Param.returntime_end;
		}
	
		final int x = startTime;
		int y = endTime;
		
		hour = (WheelView) view.findViewById(R.id.hour);
		hour.setAdapter(new NumericWheelAdapter(x, y, "%02d"));
		hour.setLabel("时");
		hour.setCyclic(true);
		hour.addScrollingListener(scrollListener);
		hour.setCurrentItem(curHour-x+1);System.out.println("curHour"+curHour);System.out.println("x"+x);
		
		/*中间标题*/
		TextView title = (TextView) view.findViewById(R.id.takecar_datepicker_takereturn);
		if(take_or_return == 1){title.setText("选择取车时间");}else{title.setText("选择还车时间");}
		
		/* 确定 */
		TextView ok = (TextView) view.findViewById(R.id.takecar_datepicker_ok);
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/* 获取时间 */
				String date = new StringBuilder()
						.append(year.getCurrentItem()
								+ Calendar.getInstance().get(Calendar.YEAR)
								- 20)
						.append("-")
						.append((month.getCurrentItem() + 1) < 10 ? "0"
								+ (month.getCurrentItem() + 1) : (month
								.getCurrentItem() + 1))
						.append("-")
						.append(((day.getCurrentItem() + 1) < 10) ? "0"
								+ (day.getCurrentItem() + 1) : (day
								.getCurrentItem() + 1)).toString();
				String time = new StringBuilder()
						.append((hour.getCurrentItem()+x) < 10 ? "0"
								+ (hour.getCurrentItem()+x) : (hour
								.getCurrentItem()+x))
						.append(":00").toString();
												
				/*设置时间*/
				if(take_or_return == 1){
					Calendar.getInstance().setTime(new Date());
					/*判断时间选择是否正确*/
					if(year.getCurrentItem() < 20){
						ToastHelper.showToastShort(context, "请选择正确的年份");
						return;
					}
					if(year.getCurrentItem()==20 && month.getCurrentItem() < Calendar.getInstance().get(Calendar.MONTH)){
						ToastHelper.showToastShort(context, "取车时间应该迟于当前时间");
						return;
					}
					if(year.getCurrentItem()==20 && month.getCurrentItem() == Calendar.getInstance().get(Calendar.MONTH) && day.getCurrentItem()+1 <  Calendar.getInstance().get(Calendar.DAY_OF_MONTH)){
						ToastHelper.showToastShort(context, "取车时间应该迟于当前时间");System.out.println("item"+day.getCurrentItem());System.out.println("day"+Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
						return;
					}
					if(year.getCurrentItem()==20 && month.getCurrentItem() == Calendar.getInstance().get(Calendar.MONTH) && day.getCurrentItem()+1 ==  Calendar.getInstance().get(Calendar.DAY_OF_MONTH) &&
							hour.getCurrentItem()+x+1-1-2  <  Calendar.getInstance().get(Calendar.HOUR_OF_DAY)){
						ToastHelper.showToastShort(context, "取车时间应该迟于当前时间至少1小时");System.out.println("item"+hour.getCurrentItem());System.out.println("hour"+Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
						return;
					}
					if (TimeHelper.ordaysIsLong88(date+" "+time)==1) {
						ToastHelper.showToastShort(context, "取车时间不得早于当前88天");System.out.println("item"+hour.getCurrentItem());System.out.println("hour"+Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
						return;
					}
					f1_form_takecar_date.setText(TimeHelper.getDateTime_YM(date+" "+time));
					f1_form_takecar_time.setText(TimeHelper.getWeekTime(date+" "+time));
					f1_form_takecar_time.setTag(date+" "+time);
					
					//联动改变还车时间:延后2天
					
					String return_datetime = TimeHelper.getDateTime_YMD_XDays(2, date+" "+time);//默认2天
					
					if (TimeHelper.ordaysIsLong88(f1_form_takecar_time.getTag().toString())==2) {
						return_datetime = TimeHelper.getDateTime_YMD_XDays(1, date+" "+time);				
					}else{
						return_datetime = TimeHelper.getDateTime_YMD_XDays(2, date+" "+time);						
					}
					f1_form_return_date.setText(TimeHelper.getDateTime_YM(return_datetime));
					f1_form_return_time.setText(TimeHelper.getWeekTime(return_datetime));
					f1_form_return_time.setTag(return_datetime);
					
					//弹出选择取车时间
					showDateDialog(context,3,date+" "+time,null);
				}else{
					
					/*判断时间选择是否正确*/
					if(year.getCurrentItem() < 20){
						ToastHelper.showToastShort(context, "请选择正确的年份");
						return;
					}
					
					if(take_or_return == 2){
						
						if (TimeHelper.ordaysIsShort(f1_form_takecar_time.getTag().toString() ,date+" "+time) == 1) {
							ToastHelper.showToastShort(context, "租车时间不得少于1天");System.out.println("item"+hour.getCurrentItem());System.out.println("hour"+Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
							return;
						}else if (TimeHelper.ordaysIsLong89(date+" "+time)==1) {
							ToastHelper.showToastShort(context, "租车时间不得超过当前89天");System.out.println("item"+hour.getCurrentItem());System.out.println("hour"+Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
							return;
						}
						
					}else{
						if (TimeHelper.ordaysIsShort(f1_form_takecar_time.getTag().toString() ,date+" "+time) == 1) {
							ToastHelper.showToastShort(context, "租车时间不得少于1天");System.out.println("item"+hour.getCurrentItem());System.out.println("hour"+Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
							return;
						}else if (TimeHelper.ordaysIsLong89(date+" "+time)==1) {
							ToastHelper.showToastShort(context, "租车时间不得超过当前89天");System.out.println("item"+hour.getCurrentItem());System.out.println("hour"+Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
							return;
						}
					}
					
					f1_form_return_date.setText(TimeHelper.getDateTime_YM(date+" "+time));
					f1_form_return_time.setText(TimeHelper.getWeekTime(date+" "+time));
					f1_form_return_time.setTag(date+" "+time);
					
					f1_days.setText(""+TimeHelper.getOrderDays(f1_form_takecar_time.getTag().toString(), date+" "+time));
					
				}
				
				System.out.println("时间" + time);
				if (null != dialog) {System.out.println("ok");
					dialog.dismiss();System.out.println("fail");
				}
			}
		});
		
		/*取消*/
		TextView cancel = (TextView) view
				.findViewById(R.id.takecar_datepicker_cancle);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if(take_or_return == 3){
					f1_days.setText("2");
				}
				if (null != dialog) {
					dialog.dismiss();
				}
			}
		});

	}

	static OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
		@Override
		public void onScrollingStarted(WheelView wheel) {

		}

		@Override
		public void onScrollingFinished(WheelView wheel) {
			int n_year = year.getCurrentItem();//
			System.out.println("选择" + year.getCurrentItem() + "---"
					+ Calendar.YEAR);
			int n_month = month.getCurrentItem() + 1;//
			// 当月日发生变化时，要改变日
			initDay(n_year,n_month);
			String birthday = new StringBuilder()
					.append(n_year + Calendar.getInstance().get(Calendar.YEAR)
							- 20)
					.append("-")
					.append((month.getCurrentItem() + 1) < 10 ? "0"
							+ (month.getCurrentItem() + 1) : (month
							.getCurrentItem() + 1))
					.append("-")
					.append(((day.getCurrentItem() + 1) < 10) ? "0"
							+ (day.getCurrentItem() + 1) : (day
							.getCurrentItem() + 1)).toString();
			System.out.println("选择日期" + birthday);

		}
	};
	
	/**
	 * 初始化日
	 */
	private static void initDay(int arg1, int arg2) {
		day.setAdapter(new NumericWheelAdapter(1, getDay(arg1, arg2), "%02d"));
	}
	
	/**
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	private static int getDay(int year, int month) {
		System.out.println("month"+month);
		int day = 30;
		boolean flag = false;
		switch (year % 4) {
		case 0:
			flag = true;
			break;
		default:
			flag = false;
			break;
		}
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			day = 31;
			break;
		case 2:
			day = flag ? 29 : 28;
			break;
		default:
			day = 30;
			break;
		}
		return day;
	}

}

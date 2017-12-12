package com.gjcar.data.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.gjcar.data.bean.DayRental;
import com.gjcar.data.data.Public_Param;
import com.gjcar.utils.TimeHelper;

public class RentalDayHelper {

	public ArrayList<DayRental> getList(ArrayList<DayRental> list, String time){
		
		ArrayList<DayRental> data = new ArrayList<DayRental>();
		
		int start = TimeHelper.getDayOfWeek(time) - 1;//当月的1号是星期几
		
		/*开头：3其实是周二*/
		for (int i = 0; i < start; i++) {
			
			DayRental dayRental = new DayRental();
			dayRental.isOk = false;
			
			data.add(dayRental);
		}
		
		/*中间*/
		for (int m = 0; m < list.size(); m++) {
			
			DayRental dayRental = list.get(m);
			if(dayRental != null){
				
				dayRental.id = m+1;
				dayRental.isOk = true;	
				dayRental.isOk_isHasMoney = true;//有钱
				dayRental.isTime = TimeHelper.isTimeOfRental(TimeHelper.get_YMD_Time(Public_Param.order_paramas.takeCarDate), TimeHelper.get_YMD_Time(Public_Param.order_paramas.returnCarDate), dayRental.date);
				
			}else{
				dayRental = new DayRental();
				dayRental.id = m+1;
				dayRental.isOk = true;
				dayRental.isOk_isHasMoney = false;
			}
			
			data.add(dayRental);
		}
		
		/*结尾*/
		for (int n = 0; n < 42-start-list.size(); n++) {
			
			DayRental dayRental = new DayRental();
			dayRental.isOk = false;
			
			data.add(dayRental);
		}
		
		return data;
		
	}
	
	/**
	 * 获取当前月份的1号是星期几
	 */
	public static int getRental_MonthOneDayOfYear(){
		
		/* 获取时间 */
		Calendar cal = Calendar.getInstance();

		/* 获取月 */
		int month = cal.get(Calendar.MONTH)+1; // 得到月

		return month;
	}
	
	/**
	 * 获取月份
	 */
	public static int getRental_MonthOfYear(){
		
		/* 获取时间 */
		Calendar cal = Calendar.getInstance();

		/* 获取月 */
		int month = cal.get(Calendar.MONTH)+1; // 得到月

		return month;
	}
	
	/**
	 * 查询时间
	 */
	public static String getRental_Time(int addMonth){
		
		int month = (getRental_MonthOfYear()-1)+addMonth;//月份要减1
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		Date date = new Date();
		date.setMonth(month);
		String endTime = format.format(date)+"-01";

		return endTime;
	}
	
	/**
	 * 标题
	 */
	public static String getRental_TimeTitle(int addMonth){
		
		int addmonth = (getRental_MonthOfYear()-1)+addMonth;//月份要减1
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		Date date = new Date();
		date.setMonth(addmonth);
	
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		/* 获取月 */
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1; // 得到月
		
		String timeTitle = year+"年"+month+"月";
		
		return timeTitle;
	}
		
}

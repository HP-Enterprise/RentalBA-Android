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
		
		int start = TimeHelper.getDayOfWeek(time) - 1;//���µ�1�������ڼ�
		
		/*��ͷ��3��ʵ���ܶ�*/
		for (int i = 0; i < start; i++) {
			
			DayRental dayRental = new DayRental();
			dayRental.isOk = false;
			
			data.add(dayRental);
		}
		
		/*�м�*/
		for (int m = 0; m < list.size(); m++) {
			
			DayRental dayRental = list.get(m);
			if(dayRental != null){
				
				dayRental.id = m+1;
				dayRental.isOk = true;	
				dayRental.isOk_isHasMoney = true;//��Ǯ
				dayRental.isTime = TimeHelper.isTimeOfRental(TimeHelper.get_YMD_Time(Public_Param.order_paramas.takeCarDate), TimeHelper.get_YMD_Time(Public_Param.order_paramas.returnCarDate), dayRental.date);
				
			}else{
				dayRental = new DayRental();
				dayRental.id = m+1;
				dayRental.isOk = true;
				dayRental.isOk_isHasMoney = false;
			}
			
			data.add(dayRental);
		}
		
		/*��β*/
		for (int n = 0; n < 42-start-list.size(); n++) {
			
			DayRental dayRental = new DayRental();
			dayRental.isOk = false;
			
			data.add(dayRental);
		}
		
		return data;
		
	}
	
	/**
	 * ��ȡ��ǰ�·ݵ�1�������ڼ�
	 */
	public static int getRental_MonthOneDayOfYear(){
		
		/* ��ȡʱ�� */
		Calendar cal = Calendar.getInstance();

		/* ��ȡ�� */
		int month = cal.get(Calendar.MONTH)+1; // �õ���

		return month;
	}
	
	/**
	 * ��ȡ�·�
	 */
	public static int getRental_MonthOfYear(){
		
		/* ��ȡʱ�� */
		Calendar cal = Calendar.getInstance();

		/* ��ȡ�� */
		int month = cal.get(Calendar.MONTH)+1; // �õ���

		return month;
	}
	
	/**
	 * ��ѯʱ��
	 */
	public static String getRental_Time(int addMonth){
		
		int month = (getRental_MonthOfYear()-1)+addMonth;//�·�Ҫ��1
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		Date date = new Date();
		date.setMonth(month);
		String endTime = format.format(date)+"-01";

		return endTime;
	}
	
	/**
	 * ����
	 */
	public static String getRental_TimeTitle(int addMonth){
		
		int addmonth = (getRental_MonthOfYear()-1)+addMonth;//�·�Ҫ��1
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		Date date = new Date();
		date.setMonth(addmonth);
	
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		/* ��ȡ�� */
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1; // �õ���
		
		String timeTitle = year+"��"+month+"��";
		
		return timeTitle;
	}
		
}

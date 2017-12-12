package com.gjcar.view.helper;

import org.apache.http.Header;

import com.alibaba.fastjson.JSONObject;
import com.gjcar.data.data.Public_Api;
import com.gjcar.data.data.Public_Param;
import com.gjcar.utils.HttpHelper;
import com.gjcar.utils.SharedPreferenceHelper;
import com.gjcar.utils.TimeHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

public class ViewInitHelper {

	
	/**
	 * f1:��ʼ��ѡʱ��
	 */
	public static void init_f1_DateTime(TextView[] views,String time){//���ʱ��С��10:00���Ͱ�ʱ��Ĭ��Ϊ10:00

		views[0].setText(TimeHelper.getDateTime_MD_XDays(1));//ѡ������
		views[1].setText(TimeHelper.getDateTime_WT_XDays2(1,time));//ѡ��ʱ�䣺���ұ���ѡ��ʱ���ַ���
		views[2].setText(TimeHelper.getDateTime_MD_XDays(3));
		views[3].setText(TimeHelper.getDateTime_WT_XDays2(3,time));		
		
		views[1].setTag(TimeHelper.getDateTime_YMD(1) + " " + time);
		views[3].setTag(TimeHelper.getDateTime_YMD(3) + " " + time);
				
	}
	
	/**
	 * f1:��ʼ��ѡʱ��
	 */
	public static void init_f1_ChangeDateTime(TextView[] views,String startTime, String endTime){//���ʱ��С��10:00���Ͱ�ʱ��Ĭ��Ϊ10:00
		
		String takeTime = TimeHelper.getStoreHourTime(views[1]);
		String returnTime = TimeHelper.getStoreHourTime(views[3]);
		
		String takeHourtime = TimeHelper.getInitTime_8_20(takeTime, startTime, endTime);
		String returnHourtime = TimeHelper.getInitTime_8_20(returnTime, startTime, endTime);
		
		views[0].setText(TimeHelper.getStoreShowTime(views[1]));
		views[1].setText(TimeHelper.getStoreWeekTime(takeHourtime,views[1]));//ѡ��ʱ�䣺���ұ���ѡ��ʱ���ַ���
		views[2].setText(TimeHelper.getStoreShowTime(views[3]));
		views[3].setText(TimeHelper.getStoreWeekTime(returnHourtime,views[3]));		
		
		views[1].setTag(TimeHelper.getStoreTagTime(takeHourtime,views[1]));
		views[3].setTag(TimeHelper.getStoreTagTime(returnHourtime,views[3]));
		
		System.out.println("ȡ��ʱ��"+views[1].getTag().toString());	
		System.out.println("����ʱ��"+views[3].getTag().toString());	
	}
	
	public static void init_f1_DateTime_ChangeReturn(TextView[] views,String startTime, String endTime){//���С��10��00��Ĭ��10:00
		
		String returnTime = TimeHelper.getStoreHourTime(views[1]);
		String returnHourtime = TimeHelper.getInitTime_8_20(returnTime, startTime, endTime);
		
		views[0].setText(TimeHelper.getStoreShowTime(views[1]));//����
		views[1].setText(TimeHelper.getStoreWeekTime(returnHourtime,views[1]));		
		
		views[1].setTag(TimeHelper.getStoreTagTime(returnHourtime,views[1]));
		System.out.println("�����ŵ�ʱ��"+views[1].getTag().toString());			
	}
	
	public static void init_take(int start, int end){
		
		Public_Param.taketime_start = start;
		Public_Param.taketime_end = end;
	}
	public static void init_return(int start, int end){
		
		Public_Param.returntime_start = start;
		Public_Param.returntime_end = end;
		
	}
	
	public static TextView initText_SP(Activity activity, int id, String key, String spName){
		
		TextView tv = (TextView)activity.findViewById(id);
		
		tv.setText(SharedPreferenceHelper.getString(activity, spName, key));	
				
		return tv;
	}
	
	public static void initText_Extra(Activity activity, TextView[] tvs , String[] extraNames){
	
		for (int j = 0; j < tvs.length; j++) {
			
			if(activity.getIntent().hasExtra(extraNames[j])){
				tvs[j].setText(activity.getIntent().getStringExtra(extraNames[j]));
			}
		}

	}
	
	public static void initTextViews(TextView[] textviews, String[] values){
		
		for (int i = 0; i < textviews.length; i++) {
			textviews[i].setText(values[i]);
		}
	}
	
	/**
	 * {
	    	"status": "true",
	    	"message": {
		        "id": 22,
		        "nickName": "���¾Ž�",
		        "realName": null,
	         }
	    }
	 * */
	public static void initTexts_Service(Context context, final TextView[] tvs, final String[] jsonKeys, String url){
	
		AsyncHttpClient httpClient = new AsyncHttpClient();
		
		HttpHelper.AddCookies(httpClient, context);
		
		httpClient.get(Public_Api.appWebSite + url, null, new AsyncHttpResponseHandler() {

			/* ��������ɹ�  */
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] data) {

				String databack = new String(data);System.out.println("������ɹ�:" + databack);
				
				if(databack == null || databack.equals("") || databack.equals("null")){					
					return;
				}
				
				JSONObject datajobject = JSONObject.parseObject(databack);
				
				boolean status = datajobject.getBoolean("status");
				String message = datajobject.getString("message");

				if(message == null || message.equals("") || message.equals("null")){					
					return;
				}
				
				if(status){
					JSONObject msgObject = JSONObject.parseObject(message);
					
					for (int j = 0; j < tvs.length; j++) {
						System.out.println(""+j+msgObject.getString(jsonKeys[j]));
						if(msgObject.getString(jsonKeys[j]) == null || msgObject.getString(jsonKeys[j]).equals("") || msgObject.getString(jsonKeys[j]).equals("null")){
							tvs[j].setText("");	System.out.println(""+j+"Ϊnull");							
						}else{
							tvs[j].setText(msgObject.getString(jsonKeys[j]));
						}	
					}					
				}
			}
			
			/* 5.��������ʧ��  */
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,Throwable arg3) {

			}
		});	

	} 
	
	
}

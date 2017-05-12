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
	 * f1:初始化选时间
	 */
	public static void init_f1_DateTime(TextView[] views,String time){//如果时间小于10:00，就把时间默认为10:00

		views[0].setText(TimeHelper.getDateTime_MD_XDays(1));//选车日期
		views[1].setText(TimeHelper.getDateTime_WT_XDays2(1,time));//选车时间：并且保存选车时间字符串
		views[2].setText(TimeHelper.getDateTime_MD_XDays(3));
		views[3].setText(TimeHelper.getDateTime_WT_XDays2(3,time));		
		
		views[1].setTag(TimeHelper.getDateTime_YMD(1) + " " + time);
		views[3].setTag(TimeHelper.getDateTime_YMD(3) + " " + time);
				
	}
	
	/**
	 * f1:初始化选时间
	 */
	public static void init_f1_ChangeDateTime(TextView[] views,String startTime, String endTime){//如果时间小于10:00，就把时间默认为10:00
		
		String takeTime = TimeHelper.getStoreHourTime(views[1]);
		String returnTime = TimeHelper.getStoreHourTime(views[3]);
		
		String takeHourtime = TimeHelper.getInitTime_8_20(takeTime, startTime, endTime);
		String returnHourtime = TimeHelper.getInitTime_8_20(returnTime, startTime, endTime);
		
		views[0].setText(TimeHelper.getStoreShowTime(views[1]));
		views[1].setText(TimeHelper.getStoreWeekTime(takeHourtime,views[1]));//选车时间：并且保存选车时间字符串
		views[2].setText(TimeHelper.getStoreShowTime(views[3]));
		views[3].setText(TimeHelper.getStoreWeekTime(returnHourtime,views[3]));		
		
		views[1].setTag(TimeHelper.getStoreTagTime(takeHourtime,views[1]));
		views[3].setTag(TimeHelper.getStoreTagTime(returnHourtime,views[3]));
		
		System.out.println("取车时间"+views[1].getTag().toString());	
		System.out.println("还车时间"+views[3].getTag().toString());	
	}
	
	public static void init_f1_DateTime_ChangeReturn(TextView[] views,String startTime, String endTime){//如何小于10：00，默认10:00
		
		String returnTime = TimeHelper.getStoreHourTime(views[1]);
		String returnHourtime = TimeHelper.getInitTime_8_20(returnTime, startTime, endTime);
		
		views[0].setText(TimeHelper.getStoreShowTime(views[1]));//还车
		views[1].setText(TimeHelper.getStoreWeekTime(returnHourtime,views[1]));		
		
		views[1].setTag(TimeHelper.getStoreTagTime(returnHourtime,views[1]));
		System.out.println("还车门店时间"+views[1].getTag().toString());			
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
		        "nickName": "独孤九剑",
		        "realName": null,
	         }
	    }
	 * */
	public static void initTexts_Service(Context context, final TextView[] tvs, final String[] jsonKeys, String url){
	
		AsyncHttpClient httpClient = new AsyncHttpClient();
		
		HttpHelper.AddCookies(httpClient, context);
		
		httpClient.get(Public_Api.appWebSite + url, null, new AsyncHttpResponseHandler() {

			/* 处理请求成功  */
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] data) {

				String databack = new String(data);System.out.println("请求处理成功:" + databack);
				
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
							tvs[j].setText("");	System.out.println(""+j+"为null");							
						}else{
							tvs[j].setText(msgObject.getString(jsonKeys[j]));
						}	
					}					
				}
			}
			
			/* 5.处理请求失败  */
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,Throwable arg3) {

			}
		});	

	} 
	
	
}

package com.gjcar.data.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gjcar.app.R;
import com.gjcar.data.data.Public_Api;
import com.gjcar.utils.HandlerHelper;
import com.gjcar.utils.HttpHelper;
import com.gjcar.utils.NetworkHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class TicketList_Helper {

	/**
	 * ��ȡ����
	 */
	public <T> void initDataList(String method, Context context, String api, JSONObject jsonObject, RequestParams params, final Handler handler, final int what, int String_Object, final TypeReference<T> type) {
			
		/*��ʼ������*/
		AsyncHttpClient httpClient = new AsyncHttpClient();
		httpClient.setTimeout(8000);
		
		String url = Public_Api.appWebSite + api;
		
		HttpHelper.AddCookies(httpClient, context);
		
		httpClient.get(url, params, new AsyncHttpResponseHandler() {
			
			/* ��������ɹ�  */
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] data) {
				System.out.println("2");
								
				String databack = new String(data);
				
				/*json�����쳣����*/
				if(databack == null || databack.equals("")){System.out.println("�����쳣");
					HandlerHelper.sendString(handler, what, HandlerHelper.Empty);
					return;
				}
				
				System.out.println("������ɹ�:" + databack);
				JSONObject datajobject = JSONObject.parseObject(databack);

				boolean status = datajobject.getBoolean("status");
				String message = datajobject.getString("message");
				System.out.println("22222");  
				
				if(status){
					
					if(message.equals("[]")){
						HandlerHelper.sendString(handler, what, HandlerHelper.Empty);
						return;
					}
					System.out.println("��ʼ����");
					HandlerHelper.sendStringObject(handler, what, HandlerHelper.Ok, JSON.parseObject(message, type));	
					System.out.println("��½����true");
					
				}else{	
					
					HandlerHelper.sendStringData(handler, what, HandlerHelper.Fail, message);
					System.out.println("��½����false");
				}

				
			}
			
			/* 5.��������ʧ��  */
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,Throwable arg3) {System.out.println("ʧ��");

				HandlerHelper.sendString(handler, what, HandlerHelper.DataFail);
			}
		});	
	}
	
}

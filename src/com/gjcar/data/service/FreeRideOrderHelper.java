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
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

public class FreeRideOrderHelper {

	/**
	 * 获取数据
	 */
	public <T> void initData(String method, final Context context, String api, JSONObject jsonObject, RequestParams params, final Handler handler, final int what, int String_Object, final TypeReference<T> type) {
			
		/*初始化参数*/
		final AsyncHttpClient httpClient = new AsyncHttpClient();
		httpClient.setTimeout(30000);
		HttpHelper.AddCookies(httpClient, context);
		
		String url = Public_Api.appWebSite + api;
		
		httpClient.get(url, params, new AsyncHttpResponseHandler() {
			
			/* 处理请求成功  */
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] data) {
				System.out.println("2");
								
				String databack = new String(data);
				
				/*json解析异常处理*/
				if(databack == null || databack.equals("") || databack.equals("||")){System.out.println("解析异常");
					HandlerHelper.sendString(handler, what, HandlerHelper.Empty);
					return;
				}
				
				System.out.println("请求处理成功:" + databack);
				JSONObject datajobject = JSONObject.parseObject(databack);

				boolean status = datajobject.getBoolean("status");
				String message = datajobject.getString("message");				
				System.out.println("22222");  
				
				if(status){
					
					if(message == null || message.equals("") || message.equals("||")){
						HandlerHelper.sendString(handler, what, HandlerHelper.Empty);
						return;
					}
					
					if(message.equals("[]")){
						HandlerHelper.sendString(handler, what, HandlerHelper.Empty);
						return;
					}

					System.out.println("开始解析");
					
					// 登录成功
					String token = getCookie(context,httpClient);
					System.out.println("token-----"+token);
					HandlerHelper.sendStringObject(handler, what, HandlerHelper.Ok, JSON.parseObject(message, type));	
					System.out.println("登陆请求true");
					
				}else{	
					
					HandlerHelper.sendStringData(handler, what, HandlerHelper.Fail, message);
					System.out.println("登陆请求false");
				}
				
			}
			
			/* 5.处理请求失败  */
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,Throwable arg3) {System.out.println("失败");

				HandlerHelper.sendString(handler, what, HandlerHelper.DataFail);
			}
		});	
	}
	/*
	 * 获取cookie中的token
	 */
	public String getCookie(Context context, AsyncHttpClient httpClient) {
		
		 PersistentCookieStore myCookieStore = new PersistentCookieStore(context);  
        
		 List<Cookie> cookies = myCookieStore.getCookies();  
		
         String token = "";
		 
		 for (int i = 0; i < cookies.size(); i++) {
			 
			 Cookie cookie = cookies.get(i);
			 String cookieName = cookie.getName();
			 String cookieValue = cookie.getValue();
			 System.out.println(cookieName);
			 System.out.println(cookieValue);
			 if (!TextUtils.isEmpty(cookieName)&& !TextUtils.isEmpty(cookieValue)) {
				 if(cookieName.equals("token_staff")){
					 token = cookieValue;
					 
				 }
			 }
		 }
		 
		 return token;
	}
}

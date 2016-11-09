package com.gjcar.data.service;

import java.util.ArrayList;

import org.apache.http.Header;

import android.os.Handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gjcar.data.bean.CityShow;
import com.gjcar.data.data.Public_Api;
import com.gjcar.utils.HandlerHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class Fragment1_Helper {

	/** 查询是否有该城市 */
	public void searchCity(final String city, final Handler handler, final int what) {

		/* 向服务器登陆 */
		final AsyncHttpClient httpClient = new AsyncHttpClient();
		httpClient.setTimeout(8000);
	
		RequestParams params = new RequestParams();// 设置请求参数

		String url = Public_Api.appWebSite + Public_Api.api_citylist;// 设置请求的url

		httpClient.get(url, params, new AsyncHttpResponseHandler() {

			/* 处理请求成功 */
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

				String backData = new String(arg2);
				System.out.println("请求处理成功:" + backData);

				JSONObject statusjobject = JSON.parseObject(backData);

				boolean status = statusjobject.getBoolean("status");
				String message = statusjobject.getString("message");

				if (status) {

					ArrayList<CityShow> stu = JSON.parseObject(message,
							new TypeReference<ArrayList<CityShow>>() {
							});

					boolean isFind = false;
					int index = -1;
					
					for (int i = 0; i < stu.size(); i++) {
						System.out.println("城市名" + i + stu.get(i).cityName);
						//注意：武汉没有搜到时
						if (city.substring(0, city.length() - 1).equals(
								stu.get(i).cityName)) {
							System.out.println("找到了******************");
							isFind = true;
							index = i;
							break;
						}
					}

					if (isFind) {
						// 发送验证码成功
						HandlerHelper.sendStringObject(handler, what, HandlerHelper.Ok, stu.get(index));
						System.out.println("发送成功");
					} else {
						HandlerHelper.sendStringObject(handler, what, HandlerHelper.Empty, null);
					}

				} else {

					// 发送验证码失败
					HandlerHelper.sendStringObject(handler, what, HandlerHelper.Fail, null);
				}

			}

			/* 5.处理请求失败 */
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				httpClient.cancelAllRequests(true);
				HandlerHelper.sendStringObject(handler, what, HandlerHelper.DataFail, null);
			}
		});
	}

}

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

	/** ��ѯ�Ƿ��иó��� */
	public void searchCity(final String city, final Handler handler, final int what) {

		/* ���������½ */
		final AsyncHttpClient httpClient = new AsyncHttpClient();
		httpClient.setTimeout(8000);
	
		RequestParams params = new RequestParams();// �����������

		String url = Public_Api.appWebSite + Public_Api.api_citylist;// ���������url

		httpClient.get(url, params, new AsyncHttpResponseHandler() {

			/* ��������ɹ� */
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

				String backData = new String(arg2);
				System.out.println("������ɹ�:" + backData);

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
						System.out.println("������" + i + stu.get(i).cityName);
						//ע�⣺�人û���ѵ�ʱ
						if (city.substring(0, city.length() - 1).equals(
								stu.get(i).cityName)) {
							System.out.println("�ҵ���******************");
							isFind = true;
							index = i;
							break;
						}
					}

					if (isFind) {
						// ������֤��ɹ�
						HandlerHelper.sendStringObject(handler, what, HandlerHelper.Ok, stu.get(index));
						System.out.println("���ͳɹ�");
					} else {
						HandlerHelper.sendStringObject(handler, what, HandlerHelper.Empty, null);
					}

				} else {

					// ������֤��ʧ��
					HandlerHelper.sendStringObject(handler, what, HandlerHelper.Fail, null);
				}

			}

			/* 5.��������ʧ�� */
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				httpClient.cancelAllRequests(true);
				HandlerHelper.sendStringObject(handler, what, HandlerHelper.DataFail, null);
			}
		});
	}

}

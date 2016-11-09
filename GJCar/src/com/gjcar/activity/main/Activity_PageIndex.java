package com.gjcar.activity.main;

import java.util.ArrayList;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gjcar.app.R;
import com.gjcar.data.data.Public_BaiduTJ;
import com.gjcar.data.data.Public_SP;
import com.gjcar.data.service.RightHelper;
import com.gjcar.utils.HandlerHelper;
import com.gjcar.utils.NetworkHelper;
import com.gjcar.utils.SystemUtils;
import com.gjcar.utils.ToastHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class Activity_PageIndex extends Activity{

	/*Handler*/
	private Handler handler;

	private final static int Request_Login = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pageindex);	
		System.out.println("版本aaaaaaaaaaaaaaaaa+"+SystemUtils.getVersion(Activity_PageIndex.this));
		initHandler();
		
		initData();
		
		Public_BaiduTJ.start(this);
		//3333333333333333333333333333333333
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
		Public_BaiduTJ.pageStart(this, Public_BaiduTJ.Activity_PageIndex);	
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Public_BaiduTJ.pageEnd(this, Public_BaiduTJ.Activity_PageIndex);	
	}
	
	private void initHandler() {

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				switch (msg.what) {

					case Request_Login:
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
							/*token没过期*/
							Jump();System.out.println("token登录成功");
						}else{ 
							/*token过期，清空本地文件*/System.out.println("token登录失败");
							SharedPreferences sp = getSharedPreferences(Public_SP.Account, Context.MODE_PRIVATE);
							Editor editor = sp.edit();
							editor.clear();
							editor.commit();
							Jump();
						}
						
						break;
	
					default:
						break;
				}

			}
		};

	}

	/**
	 * 页面跳转
	 */
	private void Jump(){
		
		new Thread(){
			public void run() {
				try {
					Thread.sleep(500);
					Intent intent = new Intent();
					
					intent.setClass(Activity_PageIndex.this, MainActivity.class);
					startActivity(intent);

					finish();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		}.start();
			
	}
	
	private void initData(){
		
		/*判断是否有网络*/
		if(!NetworkHelper.isNetworkAvailable(this)){
			ToastHelper.showNoNetworkToast(Activity_PageIndex.this);
		
			Jump();
			
			return;
		}
		
		/*判断是否过期token*/
		new RightHelper().LoginByToken(Activity_PageIndex.this, handler, Request_Login);
	}
	
	
}

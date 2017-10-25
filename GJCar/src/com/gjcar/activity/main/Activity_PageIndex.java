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
import android.telephony.TelephonyManager;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gjcar.app.R;
import com.gjcar.data.data.Public_BaiduTJ;
import com.gjcar.data.data.Public_Platform;
import com.gjcar.data.data.Public_SP;
import com.gjcar.data.service.RightHelper;
import com.gjcar.utils.HandlerHelper;
import com.gjcar.utils.HttpHelper;
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
	private final static int Request_Submit = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pageindex);	
		
		/*初始化Handler*/
		initHandler();
		
		/*token登录*/
		initData();
		
		/*飓风广告*/
		Request_Submit();
		
		/*百度统计r*/
		Public_BaiduTJ.start(this);
		
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
		
		/*百度统计r*/
		Public_BaiduTJ.pageStart(this, Public_BaiduTJ.Activity_PageIndex);	
		
		/*激光推送*/
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		
		super.onPause();
		Public_BaiduTJ.pageEnd(this, Public_BaiduTJ.Activity_PageIndex);	
		
		JPushInterface.onPause(this);
	}
	
	private void initHandler() {

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				switch (msg.what) {

					/*token登录*/
					case Request_Login:
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
							
							Jump();
						}else{ 
							
							/*token过期，清空本地文件*/
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

	/*** 页面跳转*/
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
	
	/*token登录*/
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
	
	/** 提交信息  */
	private void  Request_Submit(){
		
		/*获取参数*/
		String mediaId = Public_Platform.M_Media3;//腾讯新闻，新浪微博等
		String deviceId = ((TelephonyManager) this.getSystemService(TELEPHONY_SERVICE)).getDeviceId();
		String appid = "1028405";//由飓风平台发放	
		String pkg = "com.gjcar.app";
		System.out.println("deviceId"+deviceId);
		String privateKey = "cb13cc8b69156692";
		int appVersion = SystemUtils.getVersionCode(this);
		String channel = Public_Platform.C_JuFeng;
		
		/*参数*/
		JSONObject jsonObject = new JSONObject(); //
		
		jsonObject.put("mediaId", mediaId);
		jsonObject.put("deviceId", deviceId);
		jsonObject.put("appid", appid);
		jsonObject.put("pkg", pkg);
		
		jsonObject.put("privateKey", privateKey);
		jsonObject.put("appVersion", appVersion);
		jsonObject.put("channel", channel);
		
		/*提交*/
		new HttpHelper().initData(HttpHelper.Method_Post, this, "api/advertise/jf/android/active", jsonObject, null, handler, Request_Submit, 2, null);
		
	}
}

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
		
		/*��ʼ��Handler*/
		initHandler();
		
		/*token��¼*/
		initData();
		
		/*쫷���*/
		Request_Submit();
		
		/*�ٶ�ͳ��r*/
		Public_BaiduTJ.start(this);
		
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
		
		/*�ٶ�ͳ��r*/
		Public_BaiduTJ.pageStart(this, Public_BaiduTJ.Activity_PageIndex);	
		
		/*��������*/
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

					/*token��¼*/
					case Request_Login:
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
							
							Jump();
						}else{ 
							
							/*token���ڣ���ձ����ļ�*/
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

	/*** ҳ����ת*/
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
	
	/*token��¼*/
	private void initData(){
		
		/*�ж��Ƿ�������*/
		if(!NetworkHelper.isNetworkAvailable(this)){
			ToastHelper.showNoNetworkToast(Activity_PageIndex.this);
		
			Jump();
			
			return;
		}
		
		/*�ж��Ƿ����token*/
		new RightHelper().LoginByToken(Activity_PageIndex.this, handler, Request_Login);
	}
	
	/** �ύ��Ϣ  */
	private void  Request_Submit(){
		
		/*��ȡ����*/
		String mediaId = Public_Platform.M_Media3;//��Ѷ���ţ�����΢����
		String deviceId = ((TelephonyManager) this.getSystemService(TELEPHONY_SERVICE)).getDeviceId();
		String appid = "1028405";//��쫷�ƽ̨����	
		String pkg = "com.gjcar.app";
		System.out.println("deviceId"+deviceId);
		String privateKey = "cb13cc8b69156692";
		int appVersion = SystemUtils.getVersionCode(this);
		String channel = Public_Platform.C_JuFeng;
		
		/*����*/
		JSONObject jsonObject = new JSONObject(); //
		
		jsonObject.put("mediaId", mediaId);
		jsonObject.put("deviceId", deviceId);
		jsonObject.put("appid", appid);
		jsonObject.put("pkg", pkg);
		
		jsonObject.put("privateKey", privateKey);
		jsonObject.put("appVersion", appVersion);
		jsonObject.put("channel", channel);
		
		/*�ύ*/
		new HttpHelper().initData(HttpHelper.Method_Post, this, "api/advertise/jf/android/active", jsonObject, null, handler, Request_Submit, 2, null);
		
	}
}

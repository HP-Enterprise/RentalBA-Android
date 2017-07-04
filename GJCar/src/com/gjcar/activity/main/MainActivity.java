package com.gjcar.activity.main;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gjcar.activity.fragment1.Fragment1;
import com.gjcar.app.R;
import com.gjcar.data.bean.ApkInfo;
import com.gjcar.data.bean.CityShow;
import com.gjcar.data.bean.ServiceValueAddShow;
import com.gjcar.data.bean.StoreShows;
import com.gjcar.data.bean.User;
import com.gjcar.data.data.Public_Api;
import com.gjcar.data.data.Public_BaiduTJ;
import com.gjcar.data.data.Public_Param;
import com.gjcar.utils.HandlerHelper;
import com.gjcar.utils.HttpHelper;
import com.gjcar.utils.ImageLoaderHelper;
import com.gjcar.utils.SQL_OpenHelper;
import com.gjcar.utils.SQL_Dao;
import com.gjcar.utils.SQL_TableHelper;
import com.gjcar.utils.SystemUtils;
import com.gjcar.utils.UpdateUtils;
import com.gjcar.utils.UpdateUtils.Update_Notify;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v4.widget.SlidingPaneLayout.PanelSlideListener;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements Update_Notify{
	
	public SlidingPaneLayout slidingPaneLayout;
	
	private Fragment_Content fragment_content;
	private Fragment_Menu fragment_menu;
	
	private DisplayMetrics displayMetrics = new DisplayMetrics();
	private long currenttime = 0;
	
	/*Handler*/
	private Handler handler;
	private final static int Request_Version = 1;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		setContentView(R.layout.activity_main);
		
		initConfig();
		
		initView();
		
		initFragment();
		
		initHandler();
		
		/*版本检测*/
		new HttpHelper().initData(HttpHelper.Method_Get, this, "api/appManage/latest?appType=0", null, null, handler, Request_Version, 1, new TypeReference<ApkInfo>() {});
		
	}
	
	private void initConfig() {
		ImageLoaderHelper.initImageLoader(this);
	}

	private void initView(){
		slidingPaneLayout = (SlidingPaneLayout)findViewById(R.id.slidingpanellayout);
	}
	
	private void initFragment() {
		
		fragment_content = new Fragment_Content();
		fragment_menu = new Fragment_Menu();
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.slidingpane_content, fragment_content);
		transaction.replace(R.id.slidingpane_menu, fragment_menu);

		transaction.commit();
		
		slidingPaneLayout.setPanelSlideListener(new PanelSlideListener() {
			@Override
			public void onPanelSlide(View panel, float slideOffset) {
				
				System.out.println("onPanelSlide");
			}

			@Override
			public void onPanelOpened(View panel) {
				System.out.println("onPanelOpened");
				Public_Param.isOpened = true;
				fragment_menu.flush();
			}

			@Override
			public void onPanelClosed(View panel) {
				System.out.println("onPanelClosed");
				Public_Param.isOpened = false;
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if(keyCode == KeyEvent.KEYCODE_BACK){
			
			if(System.currentTimeMillis() - currenttime > 2000){
				currenttime = System.currentTimeMillis();
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			}else{

				finish();
			}
			
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onResume() {
		System.out.println("MainActivity_onResume---------xxxxxxxxxxxxxxxxxxxxxxx");
		super.onResume();
		
		Public_BaiduTJ.pageStart(this, Public_BaiduTJ.Activity_Main);	
		
		if(Public_Param.loginFrom == Public_Param.loginFrom_NotLogin || Public_Param.loginFrom == Public_Param.loginFrom_LoginOut){
			slidingPaneLayout.closePane();
			Public_Param.loginFrom = 0;
		}
		System.out.println("MainActivity_onResume---over------xxxxxxxxxxxxxxxxxxxxxxx");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Public_BaiduTJ.pageEnd(this, Public_BaiduTJ.Activity_Main);	
	}
	/**
	 * @return the slidingPaneLayout
	 */
	public SlidingPaneLayout getSlidingPaneLayout() {
		return slidingPaneLayout;
	}

	private void initHandler() {

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				switch (msg.what) {

					case Request_Version:
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
							System.out.println("1aaaaaaaaaaaaaaaaa+");
							ApkInfo apkInfo = (ApkInfo)msg.obj;
							System.out.println("2aaaaaaaaaaaaaaaaa+");
							if(apkInfo == null || apkInfo.appVersion == null || apkInfo.appVersion.equals("")){System.out.println("3aaaaaaaaaaaaaaaaa+");
								return;
							}
							
							System.out.println("4aaaaaaaaaaaaaaaaa+");
								System.out.println("版本信息："+apkInfo.appAddress);
								
								
							if(!(apkInfo.appAddress == null || apkInfo.appAddress.equals(""))){
								System.out.println("5aaaaaaaaaaaaaaaaa+");
								String[] str = apkInfo.appVersion.split("-");
								
								Public_Param.Version_Content = apkInfo.updateContent;
								
								System.out.println("6aaaaaaaaaaaaaaaaa+"+str[0]);
								System.out.println("7aaaaaaaaaaaaaaaaa手机版本+"+SystemUtils.getVersion(MainActivity.this));
								if(isUp(str[0], SystemUtils.getVersion(MainActivity.this))){System.out.println("8aaaaaaaaaaaaaaaaa+");
									
									if(str.length == 2){
										System.out.println("10aaaaaaaaaaaaaaaaa+");
										Public_Param.Version_Name = str[0];
										update(Public_Api.appWebSite+apkInfo.appAddress,str[1], apkInfo.forceUpdate.intValue() == 0 ? false : true);System.out.println("下载地址"+Public_Api.appWebSite+apkInfo.appAddress);
										System.out.println("aaaaaaaaaaaaaaaaaaaaa_apksize"+str[1]);
									}
								}
								System.out.println("9aaaaaaaaaaaaaaaaa+");
								
								//update(Public_Api.appWebSite+apkInfo.appAddress);System.out.println("下载地址"+Public_Api.appWebSite+apkInfo.appAddress);
							}

				           	return;
						}
						
						break;
						
					default:
						break;
				}
			}
		};
	}

	
	/** 更新*/
	private void update(String url,String size, boolean isFourceUpdate){
		UpdateUtils update = new UpdateUtils(this, url, size, isFourceUpdate);
		update.setListener(this);
		update.UpdateManager_do();
	}
	
	@Override
	public void update_finish() {	
		
	}

	@Override
	public void closeApp() {
		System.out.println("强制更新推出xxxxxxxxxxxxxx");
		finish();				
	}
	
	public boolean isUp(String v_gjcar, String v_local){
		
		boolean isUp = false;
		System.out.println(""+v_gjcar.substring(1, v_gjcar.length()));
		String[] v_gjcars = v_gjcar.substring(1, v_gjcar.length()).split("\\.");System.out.println(""+v_gjcars.length);
		
		String[] v_locals = v_local.substring(1, v_local.length()).split("\\.");
		
		int g_1 = Integer.parseInt(v_gjcars[0]);
		int g_2 = Integer.parseInt(v_gjcars[1]);
		int g_3 = Integer.parseInt(v_gjcars[2]);
		
		int l_1 = Integer.parseInt(v_locals[0]);
		int l_2 = Integer.parseInt(v_locals[1]);
		int l_3 = Integer.parseInt(v_locals[2]);
		
		if(g_1 > l_1){
			
			isUp = true;
		}else{
			
			if(g_1 == l_1 && g_2 > l_2){
				
				isUp = true;
			}else{
				
				if(g_1 == l_1 && g_2 == l_2 && g_3 > l_3){
					
					isUp = true;
				}
			}
		}
		return isUp;
		
	}
}

package com.gjcar.app.application;


import java.util.LinkedList;
import java.util.List;

import com.baidu.mapapi.SDKInitializer;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.os.Vibrator;

/**
 * ��Application�����аٶȶ�λSDK�Ľӿ�˵����ο������ĵ���http://developer.baidu.com/map/loc_refer/index.html
 *
 * �ٶȶ�λSDK�ٷ���վ��http://developer.baidu.com/map/index.php?title=android-locsdk
 * 
 * ֱ�ӿ���com.baidu.location.service�����Լ��Ĺ����£������ü��ɻ�ȡ��λ�����Ҳ���Ը���demo�������з�װ
 */
public class LocationApplication extends Application {

	private List<Activity> list = new LinkedList<Activity>();
	private static LocationApplication app;
	
	public static LocationApplication getInstance(){
		return app;
	}
	
    @Override
    public void onCreate() {
        super.onCreate();
        
        app = new LocationApplication();
        
        /* ��ʼ����λsdk��������Application�д���*/
        SDKInitializer.initialize(getApplicationContext());  
       
    }
    
    
/**�˳�app******************************************************************/    
	public void addActivity(Activity activity)
	{
		list.add(activity);
	}
	public Activity removeTopActivity(){
		return list.remove(list.size() - 1);
	}
	public Activity getTopActivity(){
		return list.get(list.size() - 1);
	}
	public void exitApp()
	{
		for(int i = list.size() - 1;i >= 0;i--){
			list.get(i).finish();
		}
//		ImageLoader.getInstance().stop();
	}
	
/**�˳�app******************************************************************/ 
}

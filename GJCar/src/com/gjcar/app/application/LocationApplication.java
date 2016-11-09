package com.gjcar.app.application;


import java.util.LinkedList;
import java.util.List;

import com.baidu.mapapi.SDKInitializer;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.os.Vibrator;

/**
 * 主Application，所有百度定位SDK的接口说明请参考线上文档：http://developer.baidu.com/map/loc_refer/index.html
 *
 * 百度定位SDK官方网站：http://developer.baidu.com/map/index.php?title=android-locsdk
 * 
 * 直接拷贝com.baidu.location.service包到自己的工程下，简单配置即可获取定位结果，也可以根据demo内容自行封装
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
        
        /* 初始化定位sdk，建议在Application中创建*/
        SDKInitializer.initialize(getApplicationContext());  
       
    }
    
    
/**退出app******************************************************************/    
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
	
/**退出app******************************************************************/ 
}

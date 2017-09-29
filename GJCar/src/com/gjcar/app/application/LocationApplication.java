package com.gjcar.app.application;


import java.util.LinkedList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

import com.baidu.mapapi.SDKInitializer;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

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
       
        
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        
        initUMShare();
    }
    
    /**
	 * 初始化友盟分享
	 */
	private void initUMShare() {
		
		UMShareAPI.get(this);
		PlatformConfig.setWeixin("wx1c969569e3fce1c1", "3d4945a2fb14e5971353594855418bd1");
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

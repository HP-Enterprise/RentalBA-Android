package com.gjcar.data.data;

import android.content.Context;

import com.baidu.mobstat.StatService;
import com.gjcar.activity.user.more.Activity_Order_Detail;

public class Public_BaiduTJ {

	public final static String Activity_PageIndex = "引导页";//主页面
	public final static String Activity_Main = "主页面";
	public final static String Fragment_Menu = "主页面_个人中心";	
	
	public final static String Activity_User = "我_个人信息";//个人信息
	public final static String Activity_Login = "我_登录";
	public final static String Activity_Register = "我_注册";
	
	public final static String Activity_Order_Ok = "我_订单成功";
	public final static String Activity_Order_List = "我_订单列表";
	public final static String Activity_Order_Detail = "我_订单详情";
	public final static String Activity_Level = "我_等级";
	public final static String Activity_Score = "我_积分";
	public final static String Activity_Score_Change = "我_积分兑换";
	public final static String Activity_Ticket = "我_优惠券";
		
	public final static String Fragment_ShortRentalCar = "短租带驾";//短租代驾
	public final static String Activity_Area = "短租_选择地址";
	public final static String Activity_Map_Area = "短租_地图地址";
	public final static String Activity_City_List = "城市列表";
	public final static String Activity_Car_List = "短租_车型列表";
	public final static String Activity_Service = "短租_选择服务";
	public final static String Activity_Store_Select = "短租_选择门店";
	public final static String Activity_Order_Submit = "短租_确认订单";
	
	public final static String Fragment_LongRentalCar = "长租";//长租
	public final static String Fragment_LongRental_Content = "长租_申请内容";//长租
	
	/**
	 * 开始统计:发送上一次的统计数据
	 */
	public static void start(Context context){
		
		if(context != null){
			StatService.start(context);
		}
		
	}
	
	/**
	 * 开始统计:发送上一次的统计数据
	 */
	public static void pageStart(Context context, String pageName){
		
		if(context != null){
			StatService.onPageStart(context,pageName);
		}
		
	}
	
	/**
	 * 开始统计:发送上一次的统计数据
	 */
	public static void pageEnd(Context context, String pageName){
		
		if(context != null){
			StatService.onPageEnd(context,pageName);
		}
		
	}
	
	/**
	 * 统计bug
	 */
	public static void bug(){
		StatService.setDebugOn(true);
	}
}

package com.gjcar.data.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;

import com.gjcar.data.bean.FreeRide;
import com.gjcar.data.bean.Order;
import com.gjcar.data.bean.OrderParams;
import com.gjcar.data.bean.OtherDrive_Params;
import com.gjcar.data.bean.Point;
import com.gjcar.data.bean.ServiceAmount;
import com.gjcar.data.bean.Transfer_Params;

public class Public_Param {

	/**主页面是否展开了*/
	public static boolean isOpened = false;
	
	public static List<Map<String,Object>> list_area = new ArrayList<Map<String ,Object>>();
	
	/**订单参数*/
	public static OrderParams order_paramas = new OrderParams();
	
	/**订单详情*/
	public static Order order = null;
	
	/**顺风车订单*/
	public static FreeRide freeRide;
	public static Integer freeRideId;
	
	public static OtherDrive_Params other_params = new OtherDrive_Params();
	
	/**接送机参数*/
	public static Transfer_Params transfer_params = new Transfer_Params();
	
	/**登录状态*/	
	public static int loginFrom = 0;//登录页面从哪里来的：
	public static int loginFrom_LoginOut = 1;	
	public static int loginFrom_NotLogin = 2;
	public static int send_toWeb = 1;//默认1，进入2，出来3
	
	/**选择区域*/
	public static List<Activity> list_area_activity = new ArrayList<Activity>();
	
	/**登录注册*/
	public static List<Activity> list_login_activity = new ArrayList<Activity>();
	/**找回密码*/
	public static List<Activity> list_findpwd_activity = new ArrayList<Activity>();
	/**修改密码*/
	public static List<Activity> list_updatepwd_activity = new ArrayList<Activity>();
	/**订单*/
	public static List<Activity> list_order_activity = new ArrayList<Activity>();
	
	/**顺风车订单*/
	public static List<Activity> list_free_order_activity = new ArrayList<Activity>();
	
	/**地址搜索*/
	public static List<Point> points;
	
	/**版本信息*/
	public static String Version_Name = "";
	public static String Version_Content = "";
	
	/** 时间选择*/
	public static int taketime_start = 0;
	public static int taketime_end = 0;
	public static int returntime_start = 0;
	public static int returntime_end = 0;
	
	/**注册直接登录*/
	public static boolean isRegisterOk = false;
	public static String phone = "";
	public static String password = "";
	
	public static boolean isUseActivity = false;
}

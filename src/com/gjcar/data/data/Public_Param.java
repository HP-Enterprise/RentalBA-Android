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

	/**��ҳ���Ƿ�չ����*/
	public static boolean isOpened = false;
	
	public static List<Map<String,Object>> list_area = new ArrayList<Map<String ,Object>>();
	
	/**��������*/
	public static OrderParams order_paramas = new OrderParams();
	
	/**��������*/
	public static Order order = null;
	
	/**˳�糵����*/
	public static FreeRide freeRide;
	public static Integer freeRideId;
	
	public static OtherDrive_Params other_params = new OtherDrive_Params();
	
	/**���ͻ�����*/
	public static Transfer_Params transfer_params = new Transfer_Params();
	
	/**��¼״̬*/	
	public static int loginFrom = 0;//��¼ҳ����������ģ�
	public static int loginFrom_LoginOut = 1;	
	public static int loginFrom_NotLogin = 2;
	public static int send_toWeb = 1;//Ĭ��1������2������3
	
	/**ѡ������*/
	public static List<Activity> list_area_activity = new ArrayList<Activity>();
	
	/**��¼ע��*/
	public static List<Activity> list_login_activity = new ArrayList<Activity>();
	/**�һ�����*/
	public static List<Activity> list_findpwd_activity = new ArrayList<Activity>();
	/**�޸�����*/
	public static List<Activity> list_updatepwd_activity = new ArrayList<Activity>();
	/**����*/
	public static List<Activity> list_order_activity = new ArrayList<Activity>();
	
	/**˳�糵����*/
	public static List<Activity> list_free_order_activity = new ArrayList<Activity>();
	
	/**��ַ����*/
	public static List<Point> points;
	
	/**�汾��Ϣ*/
	public static String Version_Name = "";
	public static String Version_Content = "";
	
	/** ʱ��ѡ��*/
	public static int taketime_start = 0;
	public static int taketime_end = 0;
	public static int returntime_start = 0;
	public static int returntime_end = 0;
	
	/**ע��ֱ�ӵ�¼*/
	public static boolean isRegisterOk = false;
	public static String phone = "";
	public static String password = "";
	
	public static boolean isUseActivity = false;
}

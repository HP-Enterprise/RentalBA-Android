package com.gjcar.data.data;

import android.content.Context;

import com.baidu.mobstat.StatService;
import com.gjcar.activity.user.more.Activity_Order_Detail;

public class Public_BaiduTJ {

	public final static String Activity_PageIndex = "����ҳ";//��ҳ��
	public final static String Activity_Main = "��ҳ��";
	public final static String Fragment_Menu = "��ҳ��_��������";	
	
	public final static String Activity_User = "��_������Ϣ";//������Ϣ
	public final static String Activity_Login = "��_��¼";
	public final static String Activity_Register = "��_ע��";
	
	public final static String Activity_Order_Ok = "��_�����ɹ�";
	public final static String Activity_Order_List = "��_�����б�";
	public final static String Activity_Order_Detail = "��_��������";
	public final static String Activity_Level = "��_�ȼ�";
	public final static String Activity_Score = "��_����";
	public final static String Activity_Score_Change = "��_���ֶһ�";
	public final static String Activity_Ticket = "��_�Ż�ȯ";
		
	public final static String Fragment_ShortRentalCar = "�������";//�������
	public final static String Activity_Area = "����_ѡ���ַ";
	public final static String Activity_Map_Area = "����_��ͼ��ַ";
	public final static String Activity_City_List = "�����б�";
	public final static String Activity_Car_List = "����_�����б�";
	public final static String Activity_Service = "����_ѡ�����";
	public final static String Activity_Store_Select = "����_ѡ���ŵ�";
	public final static String Activity_Order_Submit = "����_ȷ�϶���";
	
	public final static String Fragment_LongRentalCar = "����";//����
	public final static String Fragment_LongRental_Content = "����_��������";//����
	
	/**
	 * ��ʼͳ��:������һ�ε�ͳ������
	 */
	public static void start(Context context){
		
		if(context != null){
			StatService.start(context);
		}
		
	}
	
	/**
	 * ��ʼͳ��:������һ�ε�ͳ������
	 */
	public static void pageStart(Context context, String pageName){
		
		if(context != null){
			StatService.onPageStart(context,pageName);
		}
		
	}
	
	/**
	 * ��ʼͳ��:������һ�ε�ͳ������
	 */
	public static void pageEnd(Context context, String pageName){
		
		if(context != null){
			StatService.onPageEnd(context,pageName);
		}
		
	}
	
	/**
	 * ͳ��bug
	 */
	public static void bug(){
		StatService.setDebugOn(true);
	}
}

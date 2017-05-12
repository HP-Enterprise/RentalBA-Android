package com.gjcar.activity.fragment1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baidu.mobstat.StatService;
import com.gjcar.annotation.ContentView;
import com.gjcar.annotation.ContentWidget;
import com.gjcar.app.R;
import com.gjcar.data.adapter.CarList_Adapter;
import com.gjcar.data.bean.Model____Vendor_Store_Price;
import com.gjcar.data.bean.TradeAreaShow;
import com.gjcar.data.data.Public_BaiduTJ;
import com.gjcar.data.data.Public_Param;
import com.gjcar.data.data.Public_Data;
import com.gjcar.data.data.Public_Platform;
import com.gjcar.data.service.CarList_Helper;
import com.gjcar.framwork.TencentMapHelper;
import com.gjcar.utils.AnnotationViewUtils;
import com.gjcar.utils.HandlerHelper;
import com.gjcar.utils.HttpHelper;
import com.gjcar.utils.IntentHelper;
import com.gjcar.utils.SharedPreferenceHelper;
import com.gjcar.utils.TimeHelper;
import com.gjcar.view.helper.ListViewHelper;
import com.gjcar.view.helper.LoadAnimateHelper;
import com.gjcar.view.helper.TitleBarHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

@ContentView(R.layout.activity_car_list)
public class Activity_Car_List extends Activity{
	
	@ContentWidget(id = R.id.listview) ListView listview;
	@ContentWidget(id = R.id.show_msg) TextView show_msg;
	
	/*Handler*/
	private Handler handler;
	private final static int CarList_Data = 1;
	
	private final static int CarList_Show = 2;
	
	private final static int Show_Msg = 3;
	
	/*数据*/
	private List<Model____Vendor_Store_Price> list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		AnnotationViewUtils.injectObject(this, this);

		Public_Param.list_order_activity.add(this);
	
		/*handler*/
		initHandler();
		
		/*标题*/
		TitleBarHelper.Back(this, "车型列表", 0);
		
		/*加载动画*/
		LoadAnimateHelper.Search_Animate(this, R.id.activity, handler, 0, true,true,1);
		
//		new Thread(){
//			public void run() {
//				
//				try {
//					Thread.sleep(1000);
//					handler.sendEmptyMessage(Show_Msg);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//			};
//		}.start();
			
		/*初始化数据*/
		initData();
	}

	@Override
	protected void onResume() {
		
		super.onResume();
		Public_BaiduTJ.pageStart(this, Public_BaiduTJ.Activity_Car_List);	

	}
	
	@Override
	protected void onPause() {

		super.onPause();
		Public_BaiduTJ.pageEnd(this, Public_BaiduTJ.Activity_Car_List);	
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		
		if(!SharedPreferenceHelper.isLogin(this)){
			return;
		}
		IntentHelper.startActivity(Activity_Car_List.this, Activity_Service.class);
	}
	
	private void initData() {System.out.println("1");
		
		String startDate = TimeHelper.getSearchTime_Mis(Public_Param.order_paramas.takeCarDate);
		String endDate = TimeHelper.getSearchTime_Mis(Public_Param.order_paramas.returnCarDate);
		String latitude = new Double(Public_Param.order_paramas.takeCarLatitude).toString();
		String longitude = new Double(Public_Param.order_paramas.takeCarLongitude).toString();
		String takeCarCityId = Public_Param.order_paramas.takeCarCityId;
		String takeCarStoreId = Public_Param.order_paramas.takeCarStoreId;
		
		//modelId=-1&returnCarCityId=73&returnCarStoreId=2
		
		String returnCarCityId = Public_Param.order_paramas.returnCarCityId;
		String returnCarStoreId = Public_Param.order_paramas.returnCarStoreId;
		
		String userId = new Integer(SharedPreferenceHelper.getUid(this)).toString();System.out.println("userId"+userId);
				
		if(Public_Param.order_paramas.isDoorToDoor.intValue() == 1){
			takeCarStoreId = "-1";
		}
		String api = "api/searchVehicleRentalPack?orderType=priceLow&pageSize=50&brandId=&carGroup=&currentPage=1&modelId=-1&endDate="+endDate+"&latitude="+latitude+"&longitude="+longitude+"&startDate="+startDate+"&takeCarCityId="+takeCarCityId+"&takeCarStoreId="+takeCarStoreId+"&returnCarCityId="+returnCarCityId+"&returnCarStoreId="+returnCarStoreId+"&applicationSide="+Public_Platform.P_Android+"&userId="+userId;
		//String api = "api/searchVehicleRentalPack?brandId=&carGroup=&currentPage=1&endDate=1473508800000&latitude=31.235564&longitude=121.476213&modelId=-1&orderType=priceLow&pageSize=20&returnCarCityId=73&returnCarStoreId=2&startDate=1473292800000&takeCarCityId=73&takeCarStoreId=2";	
		System.out.println("startDate"+startDate);
		System.out.println("endDate"+endDate);
		System.out.println("latitude"+latitude);
		System.out.println("longitude"+longitude);
		System.out.println("takeCarCityId"+takeCarCityId);
		System.out.println("takeCarStoreId"+takeCarStoreId);
		
		new CarList_Helper().initData(HttpHelper.Method_Get, this, api, null, null, handler, CarList_Data, 1, new TypeReference<ArrayList<Model____Vendor_Store_Price>>() {});
	}
	
	private void initHandler() {

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				switch (msg.what) {

					case CarList_Data:
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){

							
							list = (ArrayList<Model____Vendor_Store_Price>)msg.obj;
//				           	System.out.println("size"+list.size());	      
//				           	System.out.println("解析结束"+list.get(0).vehicleModelShow.model );	
				           	handler.sendEmptyMessage(CarList_Show);
				           	return;
						}
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Empty)){
							LoadAnimateHelper.load_empty_animation();
				           	System.out.println("请求失败");	            
						}
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Fail)){
							LoadAnimateHelper.load_fail_animation();
				           	System.out.println("请求失败");	            
						}
						if(HandlerHelper.getString(msg).equals(HandlerHelper.DataFail)){
							LoadAnimateHelper.load_fail_animation();
				           	System.out.println("请求失败");	            
						}
						
						break;
						
					case CarList_Show:
						CarList_Adapter adapter = new CarList_Adapter(Activity_Car_List.this, list);
						listview.setAdapter(adapter);
						LoadAnimateHelper.load_success_animation();
						break;
						
					case Show_Msg:
						LoadAnimateHelper.load_success_animation();
						show_msg.setVisibility(View.VISIBLE);
						break;	
					
					default:
						break;
				}
			}
		};
	}
	
}

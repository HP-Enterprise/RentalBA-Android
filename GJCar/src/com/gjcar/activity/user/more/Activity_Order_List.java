package com.gjcar.activity.user.more;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.gjcar.annotation.ContentView;
import com.gjcar.annotation.ContentWidget;
import com.gjcar.app.R;
import com.gjcar.data.adapter.CarList_Adapter;
import com.gjcar.data.adapter.OrderList_Adapter;
import com.gjcar.data.bean.Model____Vendor_Store_Price;
import com.gjcar.data.bean.Order;
import com.gjcar.data.bean.TradeAreaShow;
import com.gjcar.data.data.Public_BaiduTJ;
import com.gjcar.data.data.Public_Param;
import com.gjcar.data.data.Public_Data;
import com.gjcar.data.service.CarList_Helper;
import com.gjcar.data.service.OrderListHelper;
import com.gjcar.framwork.TencentMapHelper;
import com.gjcar.utils.AnnotationViewUtils;
import com.gjcar.utils.HandlerHelper;
import com.gjcar.utils.HttpHelper;
import com.gjcar.utils.IntentHelper;
import com.gjcar.utils.SharedPreferenceHelper;
import com.gjcar.view.helper.ListViewHelper;
import com.gjcar.view.helper.LoadAnimateHelper;
import com.gjcar.view.helper.TitleBarHelper;
import com.gjcar.view.listview.XListView;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

@ContentView(R.layout.activity_order_list)
public class Activity_Order_List extends Activity{
	
	@ContentWidget(id = R.id.listview) XListView listview;
	private OrderList_Adapter adapter;
	
	/*Handler*/
	private Handler handler;
	
	private final static int Click = 2;
	private final static int Show = 3;
	
	private final static int Request_door = 5;
	private final static int Request_doortodoor = 6;
	private final static int Request_freeride = 7;
	private int Request_Code;
	
	/*数据*/
	private ArrayList<Order> orderlist = new ArrayList<Order>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		AnnotationViewUtils.injectObject(this, this);
		
		/*listview*/
		initXListView();
		
		/*handler*/
		initHandler();
		
		/*标题*/
		TitleBarHelper.Back(this, "订单列表", 0);
		
		/*加载动画*/
		LoadAnimateHelper.Search_Animate(this, R.id.activity, handler, Click, true,true,1);
		
		/*初始化数据*/
		initData();
		
	}


	@Override
	protected void onPause() {

		super.onPause();
		Public_BaiduTJ.pageEnd(this, Public_BaiduTJ.Activity_Order_List);	
	}
	
	@TargetApi(Build.VERSION_CODES.CUPCAKE)
	@SuppressLint("NewApi")
	private void initXListView() {
		listview.setPullLoadEnable(false);
		listview.setPullRefreshEnable(false);
		listview.setFooterDividersEnabled(false);
		listview.setHeaderDividersEnabled(false);
	}

	@Override
	protected void onResume() {
		
		super.onResume();
		Public_BaiduTJ.pageStart(this, Public_BaiduTJ.Activity_Order_List);	
		
		initData();
	}
	
	private void initData() {System.out.println("1");

		String userId = new Integer(SharedPreferenceHelper.getUid(this)).toString();
		
		System.out.println("--------------"+getIntent().getStringExtra("way"));
		if(getIntent().getStringExtra("way").equals("order")){
			
			String api = "api/user/"+userId+"/order?currentPage=1&pageSize=100";
			Request_Code = Request_door;
			new HttpHelper().initData(HttpHelper.Method_Get, this, api, null, null, handler, Request_Code, 1, new TypeReference<ArrayList<Order>>() {});
			
			return;
		}
		if(getIntent().getStringExtra("way").equals("doortodoor")){
			
			String api = "api/door/user/orders?currentPage=1&pageSize=100&userId="+userId;
			Request_Code = Request_doortodoor;
			new CarList_Helper().initData(HttpHelper.Method_Get, this, api, null, null, handler, Request_Code, 1, new TypeReference<ArrayList<Order>>() {});
			
			return;
		}
		
		if(getIntent().getStringExtra("way").equals("freeride")){
			
			String api = "api/user/"+userId+"/freeRideOrder?currentPage=1&pageSize=100";
			Request_Code = Request_freeride;
			new HttpHelper().initData(HttpHelper.Method_Get, this, api, null, null, handler, Request_Code, 1, new TypeReference<ArrayList<Order>>() {});
			
			return;
		}
		
	}
	
	private void initHandler() {

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				switch (msg.what) {

					case Request_door:
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
							
							//orderlist = OrderListHelper.getList((ArrayList<Order>)msg.obj, getIntent().getStringExtra("way").equals("doortodoor"));
				           	
							orderlist = (ArrayList<Order>)msg.obj;
							System.out.println("size"+orderlist.size());	      
				            //	System.out.println("解析结束"+orderlist.get(0).model );	
				           	if(orderlist.size() == 0){
				           		LoadAnimateHelper.load_empty_animation();
				           	}
				           	handler.sendEmptyMessage(Show);
				           	return;
						}
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Empty)){
							LoadAnimateHelper.load_empty_animation();
				           	return;
						}
						LoadAnimateHelper.load_fail_animation();
						System.out.println("请求失败");	 
																								
						break;
					
					case Request_doortodoor:
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
							
							//orderlist = OrderListHelper.getList((ArrayList<Order>)msg.obj, getIntent().getStringExtra("way").equals("doortodoor"));
				           	
							orderlist = (ArrayList<Order>)msg.obj;
							System.out.println("size"+orderlist.size());	      
				            //	System.out.println("解析结束"+orderlist.get(0).model );	
				           	if(orderlist.size() == 0){
				           		LoadAnimateHelper.load_empty_animation();
				           	}
				           	handler.sendEmptyMessage(Show);
				           	return;
						}
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Empty)){
							LoadAnimateHelper.load_empty_animation();
				           	return;
						}
						LoadAnimateHelper.load_fail_animation();
						System.out.println("请求失败");	 
																								
						break;
						
					case Request_freeride:
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
							
//							orderlist = OrderListHelper.getList((ArrayList<Order>)msg.obj, getIntent().getStringExtra("way").equals("doortodoor"));
				           	
							orderlist = (ArrayList<Order>)msg.obj;
							System.out.println("size"+orderlist.size());	      
				            //	System.out.println("解析结束"+orderlist.get(0).model );	
				           	if(orderlist.size() == 0){
				           		LoadAnimateHelper.load_empty_animation();
				           	}
				           	handler.sendEmptyMessage(Show);
				           	return;
						}
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Empty)){
							LoadAnimateHelper.load_empty_animation();
				           	return;
						}
						LoadAnimateHelper.load_fail_animation();
						System.out.println("请求失败");	 
																								
						break;
						
					case Click:
						initData();
						break;
						
					case Show:
						OrderList_Adapter adapter = new OrderList_Adapter(Activity_Order_List.this, orderlist, getIntent().getStringExtra("way").equals("doortodoor"));
						listview.setAdapter(adapter);
						
						LoadAnimateHelper.load_success_animation();
						
						listview.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int position, long arg3) {
								Public_Param.order = orderlist.get(position-1);
								//IntentHelper.startActivity(Activity_Order_List.this, Activity_Order_Detail.class);
								Class<?> cls = null;
								if(getIntent().getStringExtra("way").equals("freeride")){
									cls = Activity_FreeRide_Order_Detail.class;
								}else{								
									cls = Activity_Order_Detail.class;
								}
								Intent intent = new Intent(Activity_Order_List.this, cls);//跳转				
								intent.putExtra("way", getIntent().getStringExtra("way"));
								startActivity(intent);
							}
						});
						break;
						
					default:
						break;
				}
			}
		};
	}
		
}

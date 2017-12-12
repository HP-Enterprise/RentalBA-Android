package com.gjcar.activity.fragment1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.TypeReference;
import com.gjcar.annotation.ContentView;
import com.gjcar.annotation.ContentWidget;
import com.gjcar.app.R;
import com.gjcar.data.adapter.CarList_Adapter;
import com.gjcar.data.adapter.ServiceList_Adapter;
import com.gjcar.data.bean.Details;
import com.gjcar.data.bean.Model____Vendor_Store_Price;
import com.gjcar.data.bean.OrderPrice;
import com.gjcar.data.bean.ServiceAmount;
import com.gjcar.data.bean.TradeAreaShow;
import com.gjcar.data.data.Public_BaiduTJ;
import com.gjcar.data.data.Public_Msg;
import com.gjcar.data.data.Public_Param;
import com.gjcar.data.data.Public_Data;
import com.gjcar.data.service.CarList_Helper;
import com.gjcar.framwork.TencentMapHelper;
import com.gjcar.utils.AnnotationViewUtils;
import com.gjcar.utils.HandlerHelper;
import com.gjcar.utils.HttpHelper;
import com.gjcar.utils.IntentHelper;
import com.gjcar.utils.SharedPreferenceHelper;
import com.gjcar.utils.TimeHelper;
import com.gjcar.utils.ToastHelper;
import com.gjcar.view.helper.ListViewHelper;
import com.gjcar.view.helper.LoadAnimateHelper;
import com.gjcar.view.helper.TitleBarHelper;
import com.gjcar.view.helper.ViewHelper;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

@ContentView(R.layout.activity_service)
public class Activity_Service extends Activity{
	
	@ContentWidget(id = R.id.listview) ListView listview;//
	
	@ContentWidget(id = R.id.preAmount_tv) TextView preAmount_tv;//preAmount_tv
	@ContentWidget(id = R.id.mustAmount_lin) LinearLayout mustAmount_lin;//
	
	@ContentWidget(id = R.id.money_baoxian_detail) TextView money_baoxian_detail;
	@ContentWidget(id = R.id.money_baoxian_count) TextView money_baoxian_count;
	
	@ContentWidget(id = R.id.poundageAmount_detail) TextView poundageAmount_detail;
	@ContentWidget(id = R.id.poundageAmount_all) TextView poundageAmount_all;
	
	@ContentWidget(click = "onClick") TextView payway_online;
	@ContentWidget(click = "onClick") TextView payway_store;
	
	@ContentWidget(id = R.id.tip) TextView tip;//在线支付提示
	
	@ContentWidget(click = "onClick")  Button ok;
	
	/*Handler*/
	private Handler handler;
	private final static int Request_MustData = 1;	
	private final static int Request_ServiceData = 2;
	private final static int ToggleChanged = 3;	
	private final static int Flush = 4;	
	
	private boolean isRequestPriceOk = false;	
	private OrderPrice orderPrice = null;
	
	private ArrayList<ServiceAmount> service_list;	
	private boolean[] checks;
	
	/*其它*/
	private int payWay = 0;//支付方式（0：门店现金 1：门店POS刷卡 2：在线网银 3：在线支付宝）
	private int position_service = 0;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		AnnotationViewUtils.injectObject(this, this);

		Public_Param.list_order_activity.add(this);
		
		initHandler();
		
		/*标题*/
		TitleBarHelper.Back(this, "选择服务", 0);
		
		Request_MustAmount();
		
	}

	@Override
	protected void onRestart() {
		System.out.println("ggg-onRestart");
		System.out.println("ggg-"+Public_Param.isUseActivity+"-"+Public_Param.order_paramas.isSdew.intValue());
		
		new Thread(){
			
			public void run() {
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				handler.sendEmptyMessage(Flush);
			};
		}.start();		
		
		super.onRestart();
	}
	
	@Override
	protected void onResume() {
		
		System.out.println("ggg2");
		super.onResume();
		Public_BaiduTJ.pageStart(this, Public_BaiduTJ.Activity_Service);	

	}
	
	@Override
	protected void onPause() {
		
		System.out.println("ggg3");
		super.onPause();
		Public_BaiduTJ.pageEnd(this, Public_BaiduTJ.Activity_Service);	
	}
	
	public void onClick(View view){

		switch (view.getId()) {
		
			case R.id.ok:
				if(!isRequestPriceOk){
					ToastHelper.showToastShort(Activity_Service.this, "当前网络不可用");
					return;
				}
				
				Public_Param.order_paramas.payWay = payWay;
				
				Public_Param.order_paramas.server_list.clear();
				Public_Param.order_paramas.all_list.clear();

				/*手续费*/
				Public_Param.order_paramas.all_list.add(orderPrice.poundageAmount);
				
				/*可选服务:不计免赔，异地换车*/
				
				Public_Param.order_paramas.isServiceOk = false;
				if(service_list != null && service_list.size() != 0){
					
					for (int i = 0; i < checks.length; i++) {System.out.println("e6");
						System.out.println("名称"+checks[i]);
						if(checks[i]){
							
							Public_Param.order_paramas.server_list.add(service_list.get(i));
							Public_Param.order_paramas.all_list.add(service_list.get(i));
							if(service_list.get(i).chargeName.equals("不计免赔")){
								
								Public_Param.order_paramas.isServiceOk = true;
								
							}
						}
					}
					
				}
				
				/*不计免赔：单独出来*/
				if(service_list != null && service_list.size() != 0){
					for (int i = 0; i < service_list.size(); i++) {
						
						if(service_list.get(i).chargeName.equals("不计免赔")){
							
							Public_Param.order_paramas.serviceAmount = service_list.get(i);
						}
					}
				}
				
				
				/*异店异地*/
				if(orderPrice.doorToDoor != null && orderPrice.doorToDoor.size() != 0){
				
					for (int i = 0; i < orderPrice.doorToDoor.size(); i++) {
						Public_Param.order_paramas.all_list.add(orderPrice.doorToDoor.get(i));
					}
					
				}
//				/*门到门:上门送出，取车上门*/
//				if(Public_Param.order_paramas.isDoorToDoor.intValue() == 1 && orderPrice.doorToDoor != null && orderPrice.doorToDoor.size() != 0){
//					for (int i = 0; i < orderPrice.doorToDoor.size(); i++) {
//						Public_Param.order_paramas.all_list.add(orderPrice.doorToDoor.get(i));
//					}
//					
//				}
				
				IntentHelper.startActivity(Activity_Service.this, Activity_Order_Submit.class);
				break;

			case R.id.payway_online:
				payWay = 3;
				tip.setVisibility(View.VISIBLE);
				ViewHelper.ClickOneFromAll(new TextView[]{payway_online,payway_store},0,R.drawable.service_bg_normal, R.drawable.service_bg_select, R.color.page_text_normal2, R.color.page_text_select);
				break;
			
			case R.id.payway_store:
				payWay = 0;
				tip.setVisibility(View.GONE);
				ViewHelper.ClickOneFromAll(new TextView[]{payway_online,payway_store},1,R.drawable.service_bg_normal, R.drawable.service_bg_select, R.color.page_text_normal2, R.color.page_text_select);				
				break;
					
			default:
				break;
		}
		
	}
	
	private void Request_MustAmount() {
		
		String userId = new Integer(SharedPreferenceHelper.getUid(this)).toString();System.out.println("userId"+userId);
		
		String activityId = Public_Param.order_paramas.activityId.toString();
		String isDoorToDoor = Public_Param.order_paramas.isDoorToDoor.toString();
		
		String startDate = TimeHelper.getSearchTime_Mis(Public_Param.order_paramas.takeCarDate);
		String endDate = TimeHelper.getSearchTime_Mis(Public_Param.order_paramas.returnCarDate);
		String takeCarStoreId = Public_Param.order_paramas.takeCarStoreId;
		String modelId = Public_Param.order_paramas.modelId.toString();
		
		//&returnCityId=73&returnStoreId=2&takeCityId=73
		String returnCityId = Public_Param.order_paramas.returnCarCityId;
		String returnStoreId = Public_Param.order_paramas.returnCarStoreId;
		String takeCityId = Public_Param.order_paramas.takeCarCityId;
		
		String latitude = new Double(Public_Param.order_paramas.takeCarLatitude).toString();
		String longitude = new Double(Public_Param.order_paramas.takeCarLongitude).toString();
		
		String api = "api/searchAmountDetail?userId="+userId+"&activityId="+activityId+"&isDoorToDoor="+isDoorToDoor+"&endDate="+endDate+"&modelId="+modelId+"&startDate="+startDate+"&storeId="+takeCarStoreId+"&returnCityId="+returnCityId+"&returnStoreId="+returnStoreId+"&takeCityId="+takeCityId+"&nonDeducitible="+false;
		
		if(Public_Param.order_paramas.isDoorToDoor.intValue() == 1){System.out.println("门到门");
			api = api+"&latitude="+latitude+"&longitude="+longitude;
			System.out.println(""+api);
		}
		new HttpHelper().initData(HttpHelper.Method_Get, this, api, null, null, handler, Request_MustData, 1, new TypeReference<OrderPrice>() {});		
	}
	
	private void Request_ServiceAmount(){
		
		String startTime = TimeHelper.get_YMD_Time2(Public_Param.order_paramas.takeCarDate);
		String endTime = TimeHelper.get_YMD_Time2(Public_Param.order_paramas.returnCarDate);
		String takeCarStoreId = Public_Param.order_paramas.takeCarStoreId;
		
		String api = "api/charge/service/added-value?s=0,2&endTime="+endTime+"&modelId=&startTime="+startTime+"&storeId="+takeCarStoreId;
		
		new HttpHelper().initData(HttpHelper.Method_Get, this, api, null, null, handler, Request_ServiceData, 1, new TypeReference<ArrayList<ServiceAmount>>() {});
		
	}
	
	private void initHandler() {

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				switch (msg.what) {
					
					case  Flush:

						if(Public_Param.isUseActivity && Public_Param.order_paramas.isSdew.intValue() == 1){
														
							ServiceList_Adapter adapter = new ServiceList_Adapter(Activity_Service.this, service_list, handler, ToggleChanged,orderPrice.daySum.intValue(),Public_Param.order_paramas.isSdew.intValue());
							listview.setAdapter(adapter);
						}else{
							
							checks[service_list.size() > position_service ? position_service : 0] = false;
							
							ServiceList_Adapter adapter = new ServiceList_Adapter(Activity_Service.this, service_list, handler, ToggleChanged,orderPrice.daySum.intValue(),0);
							listview.setAdapter(adapter);
						}
						break;
						
					case Request_MustData:
						
						System.out.println("rrr-1");
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
							isRequestPriceOk = true;System.out.println("rrr-2");
							orderPrice = (OrderPrice)msg.obj;
							init_View_Price(orderPrice);
				           	System.out.println("size"+orderPrice.averagePrice);	  
				           	
				           	if(Public_Param.order_paramas.isHasActivity && Public_Param.order_paramas.activity_payment != null && Public_Param.order_paramas.activity_payment.intValue() != 0){
				           		System.out.println("rrr-x");
				           		initPayWay(Public_Param.order_paramas.activity_payment.intValue());//设置支付方式
				           	}else{
				           		initPayWay(orderPrice.payment.intValue());//设置支付方式
				           		
				           	}
				           	
				            
				    		Request_ServiceAmount();//请求服务费
				           	return;
						}
						//Request_AmountDetail();
						
						break;
						
					case Request_ServiceData:
						
						System.out.println("rrr-3");
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
							service_list = (ArrayList<ServiceAmount>)msg.obj;System.out.println("rrr-4");
							System.out.println("service"+service_list.size());	System.out.println("rrr-5");		
							if(service_list != null && service_list.size() != 0){System.out.println("rrr-6");	
																
//								if(Public_Param.order_paramas.activityHostType.intValue() == 8){
//									mustAmount_lin.setVisibility(View.GONE);         
//								}else{
//									mustAmount_lin.setVisibility(View.VISIBLE);
//								}
								mustAmount_lin.setVisibility(View.VISIBLE);
								
								ServiceList_Adapter adapter = new ServiceList_Adapter(Activity_Service.this, service_list, handler, ToggleChanged,orderPrice.daySum.intValue(),Public_Param.order_paramas.isSdew.intValue());
								listview.setAdapter(adapter);
								System.out.println("rrr-7-a");
								checks = new boolean[service_list.size()];System.out.println("rrr-7-b");
								for (int i = 0; i < service_list.size(); i++) {System.out.println("rrr-7-c");
									
									if(service_list.get(i).chargeName.equals("不计免赔")){System.out.println("rrr-7-d");
										
										position_service = i;
									}
									
									if(service_list.get(i).chargeName.equals("不计免赔") && Public_Param.order_paramas.isSdew.intValue() == 1){
										System.out.println("rrr-7-e");
										checks[i] = true;
									}else{// && Public_Param.order_paramas.activityId.intValue() == 0
										System.out.println("rrr-7-f");
										checks[i] = false;
									}
									
								}
							}
							System.out.println("rrr-7");							
				           	return;
						}
						//Request_AmountDetail();		
						break;	

					case ToggleChanged:
						if(HandlerHelper.getString(msg).equals(Public_Msg.Msg_Checked)){
							int position = Integer.parseInt(msg.getData().getString("data"));
							checks[position] = true;
						}else{
							int position = Integer.parseInt(msg.getData().getString("data"));
							checks[position] = false;
						}
						break;
						
					default:
						break;
				}
			}
			
		};
	}
	
	private void initPayWay(int mypayway){//1.在线；2.门店3.所有
		
		switch (mypayway) {
			case 1:
				payway_store.setVisibility(View.GONE);
				payWay = 3;
				tip.setVisibility(View.VISIBLE);
				ViewHelper.ClickOneFromAll(new TextView[]{payway_online,payway_store},0,R.drawable.service_bg_normal, R.drawable.service_bg_select, R.color.page_text_normal2, R.color.page_text_select);				
				break;
	
			case 2:
				payway_online.setVisibility(View.GONE);	
				payWay = 0;
				tip.setVisibility(View.GONE);
				ViewHelper.ClickOneFromAll(new TextView[]{payway_online,payway_store},1,R.drawable.service_bg_normal, R.drawable.service_bg_select, R.color.page_text_normal2, R.color.page_text_select);				
				break;
							
			case 3:
				//默认保持不变
				break;
				
			default:
				
				break;
		}
		
	}
	
	private void init_View_Price(OrderPrice orderPrice) {
		
		money_baoxian_detail.setText("￥"+orderPrice.basicInsuranceAmount.intValue()+"X"+orderPrice.daySum);
		money_baoxian_count.setText("￥"+orderPrice.basicInsuranceAmount.intValue()*orderPrice.daySum);
		
		poundageAmount_detail.setText("￥"+orderPrice.poundageAmount.details.get(0).price);
		poundageAmount_all.setText("￥"+orderPrice.poundageAmount.details.get(0).price);
		
		preAmount_tv.setText("预授权"+orderPrice.preAuthorization+"元(可退)");
	}
}

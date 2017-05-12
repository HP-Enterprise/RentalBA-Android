package com.gjcar.activity.fragment1;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gjcar.activity.user.ValidationHelper;
import com.gjcar.activity.user.more.Activity_Order_Ok;
import com.gjcar.annotation.ContentView;
import com.gjcar.annotation.ContentWidget;
import com.gjcar.app.R;
import com.gjcar.data.adapter.OrderSubmit_ServiceList_Adapter;
import com.gjcar.data.adapter.TicketList_Adapter;
import com.gjcar.data.bean.CityShow;
import com.gjcar.data.bean.OrderPrice;
import com.gjcar.data.bean.TicketInfo;
import com.gjcar.data.bean.User;
import com.gjcar.data.data.Public_Api;
import com.gjcar.data.data.Public_BaiduTJ;
import com.gjcar.data.data.Public_Param;
import com.gjcar.data.data.Public_Platform;
import com.gjcar.data.data.Public_SP;
import com.gjcar.data.service.OrderSubmit_Helper;
import com.gjcar.utils.AnnotationViewUtils;
import com.gjcar.utils.HandlerHelper;
import com.gjcar.utils.HttpHelper;
import com.gjcar.utils.ImageLoaderHelper;
import com.gjcar.utils.IntentHelper;
import com.gjcar.utils.NetworkHelper;
import com.gjcar.utils.SharedPreferenceHelper;
import com.gjcar.utils.StringHelper;
import com.gjcar.utils.TimeHelper;
import com.gjcar.utils.ToastHelper;
import com.gjcar.view.dialog.SubmitDialog;
import com.gjcar.view.helper.LoadAnimateHelper;
import com.gjcar.view.helper.TitleBarHelper;
import com.gjcar.view.helper.ViewInitHelper;
import com.gjcar.view.listview.MyListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


/*
 * 
 *1.手续费：
2.上门送出，取车上门
3.可选：不计免赔，异地换车 
 * */
@ContentView(R.layout.activity_order_submit)
public class Activity_Order_Submit extends Activity{
	
	/*初始化控件*/
	@ContentWidget(id = R.id.a_picture) ImageView a_picture;
	@ContentWidget(id = R.id.a_model) TextView a_model;
	@ContentWidget(id = R.id.a_note) TextView a_note;
	
	@ContentWidget(id = R.id.b_take_date) TextView b_take_date;//时间
	@ContentWidget(id = R.id.b_take_time) TextView b_take_time;
	@ContentWidget(id = R.id.b_days) TextView b_days;
	@ContentWidget(id = R.id.b_return_date) TextView b_return_date;
	@ContentWidget(id = R.id.b_return_time) TextView b_return_time;
	
	@ContentWidget(id = R.id.c_city) TextView c_city;//城市
	@ContentWidget(id = R.id.c_address) TextView c_address;
	@ContentWidget(id = R.id.c_r_city) TextView c_r_city;
	@ContentWidget(id = R.id.c_r_address) TextView c_r_address;
	
	@ContentWidget(id = R.id.user_lin) LinearLayout user_lin;//个人信息
	@ContentWidget(id = R.id.user_name) EditText user_name;
	@ContentWidget(id = R.id.user_number) EditText user_number;
	
	@ContentWidget(click = "onClick") LinearLayout discount_lin;
	@ContentWidget(id = R.id.discount_activity_lin) LinearLayout discount_activity_lin;
	@ContentWidget(click = "onClick") LinearLayout discount_ticket_lin;
	@ContentWidget(click = "onClick") LinearLayout discount_notuse_lin;
	@ContentWidget(id = R.id.discount_activity_ok) TextView discount_activity_ok;
	@ContentWidget(id = R.id.discount_ticket_ok) TextView discount_ticket_ok;
	@ContentWidget(id = R.id.discount_notuse_ok) TextView discount_notuse_ok;
	@ContentWidget(id = R.id.discount_activity_name) TextView discount_activity_name;
	@ContentWidget(id = R.id.discount_ticket_name) TextView discount_ticket_name;
	
	@ContentWidget(id = R.id.d_service_detail) TextView d_service_detail;//费用
	@ContentWidget(id = R.id.d_service_all) TextView d_service_all;
	@ContentWidget(id = R.id.d_base_detail) TextView d_base_detail;
	@ContentWidget(id = R.id.d_base_all) TextView d_base_all;
	@ContentWidget(id = R.id.d_other_lin) LinearLayout d_other_lin;
	@ContentWidget(id = R.id.d_other_detail) TextView d_other_detail;
	@ContentWidget(id = R.id.d_other_all) TextView d_other_all;
	@ContentWidget(id = R.id.d_pound_detail) TextView d_pound_detail;
	@ContentWidget(id = R.id.d_pound_all) TextView d_pound_all;
	
	@ContentWidget(id = R.id.d_storereduce_lin) LinearLayout d_storereduce_lin;	//到店取车
	@ContentWidget(id = R.id.d_storereduce) TextView d_storereduce;
	@ContentWidget(id = R.id.d_storereduce_all) TextView d_storereduce_all;
	
	@ContentWidget(id = R.id.d_takereturn_lin) LinearLayout d_takereturn_lin;	
	@ContentWidget(id = R.id.d_take_static) TextView d_take_static;
	@ContentWidget(id = R.id.d_take_detail) TextView d_take_detail;
	@ContentWidget(id = R.id.d_take_all) TextView d_take_all;
	@ContentWidget(id = R.id.d_return_detail) TextView d_return_detail;
	@ContentWidget(id = R.id.d_return_all) TextView d_return_all;
	
	@ContentWidget(id = R.id.d_activity_lin) LinearLayout d_activity_lin;	
	@ContentWidget(id = R.id.d_activity_name) TextView d_activity_name;
	@ContentWidget(id = R.id.d_activity_detail) TextView d_activity_detail;
	@ContentWidget(id = R.id.d_activity_all) TextView d_activity_all;
	
	@ContentWidget(id = R.id.d_all_all) TextView d_all_all;
	
	@ContentWidget(click = "onClick") TextView read_ok;
	private boolean isRead = true;
	
	@ContentWidget(click = "onClick") TextView read_prevision;
	
	@ContentWidget(click = "onClick") Button ok;
	@ContentWidget(id = R.id.test) TextView test;
	
	@ContentWidget(id = R.id.listview) MyListView listview;//可选服务
	@ContentWidget(id = R.id.mustAmount_lin) LinearLayout mustAmount_lin;//
	
	/*Handler*/
	private Handler handler;
	private final static int Request_User = 2;
	private final static int Request_Ticket = 3;
	private final static int Click_Ticket = 4;
	private final static int Click_Activity = 5;
	private final static int Request_Black = 6;
	
	private boolean isRequestPriceOk = false;
	private boolean isRequestUserOk = false;
	private boolean isRequestRealNameEmpty = true;
	private boolean isRequestNumberEmpty = true;
	
	private OrderPrice orderPrice = null;
	private User user;
	private ArrayList<TicketInfo> ticket_list;
	
	private final static int OK = 15;
	private final static int Fail = 16;
	private final static int DataFail = 17;
	
	private final static int UpdateOk = 21;//成功
	private final static int UpdateFail = 22;//失败
	private final static int UpdateDataFail = 23;//请求失败
	
	private boolean isOrderSubmit = false;
	
	private String data = "";
	
	private int serviceAllAmount = 0;//可选服务费
	private int activityAmount = 0;//活动或优惠券费用
	
	/*其它*/
	private String orderId = "";
	
	private static int Ticket_List_Position = 0;//1.点击了哪一个优惠券
		
	private static final int Request_Not_Use = 101;//1:不使用优惠2:使用优惠活动3：使用优惠券
	private static final int Request_Use_Activity = 102;//1:不使用优惠2:使用优惠活动3：使用优惠券
	private static final int Request_Use_Ticket = 103;//1:不使用优惠2:使用优惠活动3：使用优惠券
	
	private static int MyFlag = -1;	
	private static final int Not_Use = 1;//1:不使用优惠2:使用优惠活动3：使用优惠券
	private static final int Use_Activity = 2;//1:不使用优惠2:使用优惠活动3：使用优惠券
	private static final int Use_Activity_Cancle_Use_Activity = 3;//1:不使用优惠2:使用优惠活动3：使用优惠券
	private static final int Use_Ticket = 4;//1:不使用优惠2:使用优惠活动3：使用优惠券
	private static final int Use_Ticket_NoTickets = 5;//1:不使用优惠2:使用优惠活动3：使用优惠券
	private static final int Use_Ticket_Cancle_Use_Tickets = 6;//1:不使用优惠2:使用优惠活动3：使用优惠券
	
	private String activityId = "0";
	private String couponNumber = "";
	private String reduce = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		AnnotationViewUtils.injectObject(this, this);

		Public_Param.list_order_activity.add(this);

		/*Handler*/
		initHandler();

		/*标题*/
		TitleBarHelper.Back(this, "确认订单", 0);
		
		/*获取参数*/
		initView();
		
		/*加载用车人信息*/
		new HttpHelper().initData(HttpHelper.Method_Get, this, "api/me", null, null, handler, Request_User, 1, new TypeReference<User>() {});
		
		/*加载费用*/
		if(Public_Param.order_paramas.activityId.intValue() == 0){
			Request_AmountDetail(Request_Not_Use);
		}else{
			Request_AmountDetail(Request_Use_Activity);
		}
		
		/*员工价*/
		if(Public_Param.order_paramas.activityHostType == 8){discount_lin.setVisibility(View.GONE);}
		
	}

	@Override
	protected void onResume() {
		
		super.onResume();
		Public_BaiduTJ.pageStart(this, Public_BaiduTJ.Activity_Order_Submit);	
		
	}
	
	@Override
	protected void onPause() {
		
		super.onPause();
		Public_BaiduTJ.pageEnd(this, Public_BaiduTJ.Activity_Order_Submit);	
	}
	
	private void initView(){System.out.println("订单确认1");
		
		ImageLoader.getInstance().displayImage(Public_Api.appWebSite + Public_Param.order_paramas.picture, a_picture, ImageLoaderHelper.initDisplayImageOptions());
		
		a_model.setText(Public_Param.order_paramas.model);System.out.println("订单确认2");

		String carGroup = "";System.out.println("订单确认3");
		if(Public_Param.order_paramas.carGroup != null){
			carGroup = StringHelper.getCarGroup(Public_Param.order_paramas.carGroup);
		}
		
		String carTrunk = "";System.out.println("订单确认4");
		if(Public_Param.order_paramas.carTrunk != null){
			carTrunk = StringHelper.getCarTrunk(Public_Param.order_paramas.carTrunk);
		}
		
		if(carTrunk.equals("1")){
			carTrunk = "3";
		}
		String seats = Public_Param.order_paramas.seats.toString();System.out.println("订单确认5");
		a_note.setText(carGroup+"/"+carTrunk+"厢/"+seats+"座");System.out.println("订单确认6");
		
		b_take_date.setText(TimeHelper.getDateTime_YM(Public_Param.order_paramas.takeCarDate));System.out.println("订单确认7");
		b_take_time.setText(TimeHelper.getWeekTime(Public_Param.order_paramas.takeCarDate));System.out.println("订单确认8");
		b_return_date.setText(TimeHelper.getDateTime_YM(Public_Param.order_paramas.returnCarDate));System.out.println("订单确认9");
		b_return_time.setText(TimeHelper.getWeekTime(Public_Param.order_paramas.returnCarDate));System.out.println("订单确认10");

		c_city.setText(Public_Param.order_paramas.takeCarCity);System.out.println("订单确认11");
		c_r_city.setText(Public_Param.order_paramas.returnCarCity);System.out.println("11");
		
		if(Public_Param.order_paramas.isDoorToDoor.intValue() == 0){System.out.println("订单确认12");
			c_address.setText(Public_Param.order_paramas.takeCarAddress+"("+Public_Param.order_paramas.takeCarAddress_Store+")");System.out.println("7");
			c_r_address.setText(Public_Param.order_paramas.returnCarAddress+"("+Public_Param.order_paramas.returnCarAddress_Store+")");System.out.println("12");
		}else{System.out.println("订单确认13");
			c_address.setText(Public_Param.order_paramas.takeCarAddress);System.out.println("订单确认14");
			c_r_address.setText(Public_Param.order_paramas.returnCarAddress);System.out.println("订单确认15");
		}
		
	}
	
	private void Request_AmountDetail(int Request_Code) {
		
		isRequestPriceOk = false;
		
		String userId = new Integer(SharedPreferenceHelper.getUid(this)).toString();System.out.println("userId"+userId);
		
		String activityId = Public_Param.order_paramas.activityId.toString();
		
		String isDoorToDoor = Public_Param.order_paramas.isDoorToDoor.toString();
		
		String startDate = TimeHelper.getSearchTime_Mis(Public_Param.order_paramas.takeCarDate);
		String endDate = TimeHelper.getSearchTime_Mis(Public_Param.order_paramas.returnCarDate);
		String takeCarStoreId = Public_Param.order_paramas.takeCarStoreId;
		String modelId = Public_Param.order_paramas.modelId.toString();
		
		String latitude = new Double(Public_Param.order_paramas.takeCarLatitude).toString();
		String longitude = new Double(Public_Param.order_paramas.takeCarLongitude).toString();
		
		//&returnCityId=73&returnStoreId=2&takeCityId=73
		String returnCityId = Public_Param.order_paramas.returnCarCityId;
		String returnStoreId = Public_Param.order_paramas.returnCarStoreId;
		String takeCityId = Public_Param.order_paramas.takeCarCityId;
		
		String api = "api/searchAmountDetail?userId="+userId+"&activityId="+activityId+"&isDoorToDoor="+isDoorToDoor+"&endDate="+endDate+"&modelId="+modelId+"&startDate="+startDate+"&storeId="+takeCarStoreId+"&returnCityId="+returnCityId+"&returnStoreId="+returnStoreId+"&takeCityId="+takeCityId;
		
		if(Public_Param.order_paramas.isDoorToDoor.intValue() == 1){System.out.println("门到门");
			api = api+"&latitude="+latitude+"&longitude="+longitude;
			System.out.println(""+api);
		}
		new HttpHelper().initData(HttpHelper.Method_Get, this, api, null, null, handler, Request_Code, 1, new TypeReference<OrderPrice>() {});		
	}

	public void onClick(View view){

		switch (view.getId()) {
		
			case R.id.discount_activity_lin:
				if(isRequestPriceOk){
						System.out.println("MyFlagxxxxxxxxxxxx"+MyFlag);
					if(MyFlag == Not_Use || MyFlag == Use_Ticket || MyFlag == Use_Ticket_NoTickets || MyFlag == Use_Ticket_Cancle_Use_Tickets){//别的
						
						/*要选中活动栏*/
						MyFlag = Use_Activity_Cancle_Use_Activity;
						init_Actions(Use_Activity_Cancle_Use_Activity);
							
						if(Public_Param.order_paramas.activityShowList != null && Public_Param.order_paramas.activityShowList.size() > 0){
							new OrderSubmit_Helper().initActivityDialog(this, Public_Param.order_paramas.activityShowList,handler, Click_Activity);
						}
					}else{//如果上一次不是使用优惠活动，就无需重新加载费用
							
						if(MyFlag == Use_Activity || MyFlag == Use_Activity_Cancle_Use_Activity){//自己
							
							if(Public_Param.order_paramas.activityShowList != null && Public_Param.order_paramas.activityShowList.size() > 0){
								new OrderSubmit_Helper().initActivityDialog(this, Public_Param.order_paramas.activityShowList,handler, Click_Activity);
							}else{
								//这种情况是不存在的
							}
						}	
					}
				}
				break;
				
			case R.id.discount_ticket_lin:
							
				if(isRequestPriceOk){
	
					if(MyFlag == Use_Activity || MyFlag == Not_Use|| MyFlag == Use_Activity_Cancle_Use_Activity){
	
						Public_Param.order_paramas.activityId = 0;
						Request_AmountDetail(Request_Use_Ticket);
					}else{//如果上一次不是使用优惠活动，就无需重新加载费用
												
						if(ticket_list != null && ticket_list.size() > 0){
							System.out.println("xxxxxxxxxxxxxxxxx弹出错误");
							new OrderSubmit_Helper().initTicketDialog(Activity_Order_Submit.this, ticket_list,handler,Click_Ticket);					
						}else{
							MyFlag = Use_Ticket_NoTickets;
							init_Actions(MyFlag);
						}
						
					}
				}
				break;	
			
				
			case R.id.discount_notuse_lin:

				if(isRequestPriceOk){
					
					if(MyFlag == Use_Activity){
						
						Public_Param.order_paramas.activityId = 0;
						Request_AmountDetail(Request_Not_Use);
					}else{//如果上一次不是使用优惠活动，就无需重新加载费用
						
						MyFlag = Not_Use;
						init_Actions(MyFlag);
					}
				}
				break;
				
			case R.id.read_ok:
				if(isRead){
					read_ok.setBackgroundResource(R.drawable.ordersubmit2_reader_nol);
					isRead = false;
					
				}else{
					read_ok.setBackgroundResource(R.drawable.ordersubmit2_reader_sel);
					isRead = true;
				}
				break;
		
			case R.id.read_prevision:
				IntentHelper.startActivity_StringExtras(Activity_Order_Submit.this, WebActivity.class, new String[]{"title","fragment"}, new String[]{"预定须知","order_read"});
				break;
			
			case R.id.ok:
				//initDialog();
				if(isOrderSubmit){
					return;
				}
				if(!isRead){
					ToastHelper.showToastShort(Activity_Order_Submit.this, "请先阅读条款");
					return;
				}
				
				/*判断是否有网*/
				if(!NetworkHelper.isNetworkAvailable(this)){
					return;
				}
				
				/*判断价格*/
				if(!isRequestPriceOk){//价格没有价值进来
					ToastHelper.showNoNetworkToast(this);
					return;
				}
				
				/*判断是否加载了个人信息*/
				if(!isRequestUserOk){//个人信息没进来
					ToastHelper.showNoNetworkToast(this);
					return;
				}
				
				/*判断填写是否正确*/
				
				if(isRequestRealNameEmpty || isRequestNumberEmpty){
					
					if(isRequestRealNameEmpty){
						
						if(ValidationHelper.isNull(user_name)){ToastHelper.showToastShort(Activity_Order_Submit.this, "姓名必须填写");return;}
						if(!ValidationHelper.IsChineseName(user_name.getText().toString().trim())){ToastHelper.showToastShort(Activity_Order_Submit.this, "姓名填写不正确");return;}
						
					}
					
					if(isRequestNumberEmpty){
						
						if(ValidationHelper.isNull(user_number)){ToastHelper.showToastShort(Activity_Order_Submit.this, "身份证必须填写");return;}
						if(!(user_number.getText().toString().trim().matches("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$")||user_number.getText().toString().trim().matches("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X|x)$"))){ToastHelper.showToastShort(Activity_Order_Submit.this, "身份证格式不正确");return;}	
				
					}
					
					/*弹出提交对话框*/	
					SubmitDialog.showSubmitDialog(this);
					
					new Thread(){
						public void run() {
							Update_RealName();
						};
					}.start();
					
				}else{
					
					/*弹出提交对话框*/	
					SubmitDialog.showSubmitDialog(this);
					
					new HttpHelper().initData(HttpHelper.Method_Get, this, "api/isBlack/"+SharedPreferenceHelper.getUid(this), null, null, handler, Request_Black, 2, null);

				}

				break;

			default:
				break;
		}
		
	}

	private void initHandler() {

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				switch (msg.what) {
				
					case Request_Black:
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
							
							SubmitDialog.closeSubmitDialog();//关闭弹窗	
							ToastHelper.showToastShort(Activity_Order_Submit.this, "系统繁忙");
						}else{
							
							if(HandlerHelper.getString(msg).equals(HandlerHelper.Fail)){
								
								new Thread(){
									public void run() {sendOrderWrite();};
								
								}.start();
								
							}else{
								
								//重新加载
								new Thread(){
									
									public void run() {
										
										try {
											
											Thread.sleep(2000);
											new HttpHelper().initData(HttpHelper.Method_Get, Activity_Order_Submit.this, "api/isBlack/"+SharedPreferenceHelper.getUid(Activity_Order_Submit.this), null, null, handler, Request_Black, 2, null);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
																		
									};
								}.start();
					
							}
						}
						
						break;
				
					case OK:
						test.setText("提示\n"+data);
						isOrderSubmit = false;
						SubmitDialog.closeSubmitDialog();//关闭弹窗
						
						finish();//结束之前的页面
						for (int i = 0; i < Public_Param.list_order_activity.size(); i++) {
							Public_Param.list_order_activity.get(i).finish();
						}							
						Public_Param.list_order_activity.clear();
											
						Intent intent = new Intent(Activity_Order_Submit.this, Activity_Order_Ok.class);//跳转
						
						intent.putExtra("model", Public_Param.order_paramas.model);
						intent.putExtra("days", orderPrice.daySum.toString());
						intent.putExtra("orderId", orderId);						
						intent.putExtra("acount", StringHelper.getMoney(""+(orderPrice.totalPrice.floatValue()+serviceAllAmount-activityAmount)));
						intent.putExtra("payWay", Public_Param.order_paramas.payWay.toString());
						
						if(Public_Param.order_paramas.isDoorToDoor.intValue() == 1){
							intent.putExtra("way", "doortodoor");
						}else{
							intent.putExtra("way", "order");
						}
						
						startActivity(intent);
						break;
						
					case Fail:
						isOrderSubmit = false;
						test.setText("提示失败\n"+data);
						SubmitDialog.closeSubmitDialog();
						ToastHelper.showToastShort(Activity_Order_Submit.this, msg.getData().getString("errorMsg"));
						break;
						
					case DataFail:
						isOrderSubmit = false;
						test.setText("提示失败\n"+data);
						SubmitDialog.closeSubmitDialog();
						ToastHelper.showToastShort(Activity_Order_Submit.this, "订单提交失败,请重新发送");
						break;
						
					case Request_Not_Use:
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
														
							isRequestPriceOk = true;
							orderPrice = (OrderPrice)msg.obj;
							test.setText(data());
							
							/*显示价格*/
							init_View_Price(orderPrice);System.out.println("size"+orderPrice.averagePrice);	 
				           	 
							/*显示活动*/
							MyFlag = Not_Use;
				           	init_Actions(MyFlag);
				           	
				           	return;
						}
						Request_AmountDetail(MyFlag);
						
						break;
						
					case Request_Use_Activity:
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){							
							
							isRequestPriceOk = true;
							orderPrice = (OrderPrice)msg.obj;
							test.setText(data());
							
							/*显示价格*/
							init_View_Price(orderPrice);System.out.println("size"+orderPrice.averagePrice);	 
				           	 
							/*显示活动*/
							MyFlag = Use_Activity;
				           	init_Actions(MyFlag);
				           					           	
				           	return;
						}
						Request_AmountDetail(MyFlag);
						
						break;
						
					case Request_Use_Ticket:
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
														
							isRequestPriceOk = true;
							orderPrice = (OrderPrice)msg.obj;
							test.setText(data());
							
							/*显示价格*/
							init_View_Price(orderPrice);System.out.println("size"+orderPrice.averagePrice);	 
				           	 
							/*显示优惠券:要显示没有使用优惠券*/
							MyFlag = Use_Ticket_Cancle_Use_Tickets;
							init_Actions(Use_Ticket_Cancle_Use_Tickets);
							
							/*显示优惠券*/
				           	load_Tickets();
				           					           	
				           	return;
						}
						Request_AmountDetail(MyFlag);
						
						break;
						
					case Request_User:
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
								
							isRequestUserOk = true;
							user = (User)msg.obj;System.out.println("lvl"+user.lvl.intValue());
							
							if(user.realName == null || user.realName.equals("null") || user.realName.equals("")){
								
								isRequestRealNameEmpty = true;
								user_lin.setVisibility(View.VISIBLE);								
							}else{
								
								isRequestRealNameEmpty = false;
								user_name.setText(user.realName);
								user_name.setEnabled(false);
							}
							
							if(user.credentialNumber == null || user.credentialNumber.equals("null") || user.credentialNumber.equals("")){
															
								isRequestNumberEmpty = true;
								user_lin.setVisibility(View.VISIBLE);							
							}else{
								
								isRequestNumberEmpty = false;
								user_number.setText(user.credentialNumber);
								user_number.setEnabled(false);
							}

							return;
							
						}else{
							
							//重新加载
							new Thread(){
								
								public void run() {
									
									try {
										
										Thread.sleep(2000);
										new HttpHelper().initData(HttpHelper.Method_Get, Activity_Order_Submit.this, "api/me", null, null, handler, Request_User, 1, new TypeReference<User>() {});									
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
																	
								};
							}.start();
							
						}
						
						break;	
						
					case Request_Ticket:
						
						isRequestPriceOk = true;
						
						discount_ticket_lin.setVisibility(View.VISIBLE);//显示优惠券
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
							
							ticket_list = (ArrayList<TicketInfo>)msg.obj;
							if(ticket_list != null && ticket_list.size() > 0){
								System.out.println("xxxxxxxxxxxxxxxxx弹出错误");
								new OrderSubmit_Helper().initTicketDialog(Activity_Order_Submit.this, ticket_list,handler,Click_Ticket);					
							}else{
								MyFlag = Use_Ticket_NoTickets;
								init_Actions(MyFlag);
							}
				           	
						}else{
							MyFlag = Use_Ticket_NoTickets;
							init_Actions(MyFlag);
						}
						break;
						
					case Click_Ticket:
						System.out.println("aaaxxxxxxxxxxxxxxxxx"+msg.getData().getInt("message"));
						System.out.println("aaaxxxxxxxxxxxxxxxxx"+MyFlag);
						
						if(isRequestPriceOk){
							Ticket_List_Position = msg.getData().getInt("message");
							MyFlag = Use_Ticket;
							init_Actions(MyFlag);
						}
				
						break;
					
					case Click_Activity:	
											
						if(isRequestPriceOk){
							Public_Param.order_paramas.activityId = Public_Param.order_paramas.activityShowList.get(msg.getData().getInt("message")).id;							
							Request_AmountDetail(Request_Use_Activity);
						}					
						break;
						
					case UpdateOk:	
						
						new HttpHelper().initData(HttpHelper.Method_Get, Activity_Order_Submit.this, "api/isBlack/"+SharedPreferenceHelper.getUid(Activity_Order_Submit.this), null, null, handler, Request_Black, 2, null);

						break;
						
					case UpdateFail:
						SubmitDialog.closeSubmitDialog();
						ToastHelper.showToastShort(Activity_Order_Submit.this, "下订单失败");//显示发送失败的原因
						break;
	
					case UpdateDataFail:
						SubmitDialog.closeSubmitDialog();
						ToastHelper.showToastShort(Activity_Order_Submit.this, "下订单失败");//显示发送失败的原因			
						break;
						
					default:
						break;
				}
			}
		};
	}
	
	private void init_View_Price(OrderPrice orderPrice){

//		Public_Parameter.order_paramas.rentalAmount =  orderPrice.totalAmount;
//		Public_Parameter.order_store_paramas.rentalAmount =  orderPrice.totalAmount;
		String[] prices = new String[10];
		prices[0] = "均价"+orderPrice.averagePrice+"元/天,共"+orderPrice.daySum+"天";System.out.println("g1*************************");
		prices[1] = "￥"+orderPrice.totalAmount;

		prices[2] = orderPrice.basicInsuranceAmount+"元/天,共"+orderPrice.daySum+"天";System.out.println("g2*************************");
		prices[3] = "￥"+orderPrice.totalBasicInsuranceAmount;
		
		if(orderPrice.totalDelayAmount.intValue() == 0){
			d_other_lin.setVisibility(View.GONE);
			prices[4] = "均价"+orderPrice.delayAmount+"元/时";System.out.println("g3*************************");
			prices[5] = "￥0";
		}else{
			prices[4] = "均价"+orderPrice.delayAmount+"元/时,共"+orderPrice.totalDelayAmount.intValue()/orderPrice.delayAmount+"小时";
			prices[5] = "￥"+orderPrice.totalDelayAmount;System.out.println("g4*************************");
		}
		
		prices[6] = orderPrice.poundageAmount.details.get(0).price+"元/次，共1次";//手续费
		prices[7] = "￥"+orderPrice.poundageAmount.details.get(0).price;	System.out.println("g5*************************");
		
		prices[8] = "￥"+orderPrice.totalPrice.toString();//总费用
				
		prices[9] = orderPrice.daySum.toString();System.out.println("g6*************************");//天数
		
		ViewInitHelper.initTextViews(new TextView[]{d_service_detail,d_service_all,d_base_detail,d_base_all,d_other_detail,d_other_all,d_pound_detail,d_pound_all,d_all_all,b_days}, prices);
		
		/*短租自驾*/
		if(orderPrice.toStoreReduce != null){
			d_storereduce_lin.setVisibility(View.VISIBLE);
			d_storereduce.setText("减免"+orderPrice.toStoreReduce+"元");
			d_storereduce_all.setText("￥-"+orderPrice.toStoreReduce);
		}
		/*门到门费用*/
//		if(Public_Param.order_paramas.isDoorToDoor.intValue() == 1){
////			d_takereturn_lin.setVisibility(View.VISIBLE);
//			System.out.println("g7*************************");
////			String[] takecarPrices = new String[]{"￥"+orderPrice.doorToDoor.get(0).details.get(0).price, "￥"+orderPrice.doorToDoor.get(0).details.get(0).price,
////					"￥"+orderPrice.doorToDoor.get(1).details.get(0).price,"￥"+orderPrice.doorToDoor.get(1).details.get(0).price};
////			System.out.println("g8*************************");
////			ViewInitHelper.initTextViews(new TextView[]{d_take_detail,d_take_all,d_return_detail,d_return_all},takecarPrices);System.out.println("g9*************************");
//		}
		if(orderPrice.doorToDoor != null && orderPrice.doorToDoor.size() == 1){
			d_takereturn_lin.setVisibility(View.VISIBLE);
			System.out.println("g7*************************");
			String[] takecarPrices = new String[]{orderPrice.doorToDoor.get(0).chargeName, "￥"+orderPrice.doorToDoor.get(0).details.get(0).price, "￥"+orderPrice.doorToDoor.get(0).details.get(0).price};
			System.out.println("g8*************************");
			ViewInitHelper.initTextViews(new TextView[]{d_take_static,d_take_detail,d_take_all},takecarPrices);System.out.println("g9*************************");
		}
		/*可选服务*/
		if(Public_Param.order_paramas.server_list.size() != 0){System.out.println("g10*************************");
			System.out.println("size--dd"+Public_Param.order_paramas.server_list.size());
			mustAmount_lin.setVisibility(View.VISIBLE);
			OrderSubmit_ServiceList_Adapter adapter = new OrderSubmit_ServiceList_Adapter(Activity_Order_Submit.this, Public_Param.order_paramas.server_list,orderPrice.daySum.intValue());
			listview.setAdapter(adapter);	System.out.println("g11*************************");
		}
		
		/*可选服务的费用统计*/
		
		serviceAllAmount = 0;
		if(Public_Param.order_paramas.server_list.size() != 0){System.out.println("g12*************************");
			for (int i = 0; i < Public_Param.order_paramas.server_list.size(); i++) {
				if(Public_Param.order_paramas.server_list.get(i).chargeName.equals("不计免赔")){
					if(orderPrice.daySum.intValue() > 7){
						serviceAllAmount = serviceAllAmount + Public_Param.order_paramas.server_list.get(i).details.get(0).price.intValue() * 7;
					}else{
						serviceAllAmount = serviceAllAmount + Public_Param.order_paramas.server_list.get(i).details.get(0).price.intValue() * orderPrice.daySum.intValue();
					}
				}else{
					serviceAllAmount = serviceAllAmount + Public_Param.order_paramas.server_list.get(i).details.get(0).price.intValue();
				}
			}
		}
		System.out.println("g13*************************");
		d_all_all.setText("￥"+new Float(orderPrice.totalPrice.floatValue() + serviceAllAmount).toString());System.out.println("g14*************************");
		System.out.println("增值服务"+serviceAllAmount);
		System.out.println("总价值"+new Float(orderPrice.totalPrice.floatValue() + serviceAllAmount).toString());
	}
	
	/** 显示活动 */
	private void init_Actions(int flag){
		
		/*清空原来的*/
		activityAmount = 0;
		activityId = "0";
		reduce = "";
		couponNumber = "";
		
		switch (flag) {
			case Use_Activity:
				if(orderPrice.activityShows != null){
					
					/*优惠活动*/
					discount_activity_lin.setVisibility(View.VISIBLE);
					discount_activity_name.setText(orderPrice.activityShows.get(0).name);	
					discount_activity_ok.setBackgroundResource(R.drawable.ordersubmit2_reader_sel);
					
					/*优惠券和不使用优惠的处理*/
					discount_ticket_name.setText("");	
					discount_ticket_ok.setBackgroundResource(R.drawable.ordersubmit2_reader_nol);
					discount_notuse_ok.setBackgroundResource(R.drawable.ordersubmit2_reader_nol);
					
					/*费用显示*/
					d_activity_lin.setVisibility(View.VISIBLE);
					d_activity_name.setText("优惠活动");
					d_activity_detail.setText(orderPrice.activityShows.get(0).activityDescription);
					d_activity_all.setText("￥-"+orderPrice.reduce);
					init_Change_Money(0);	
					
					/*优惠活动参数*/
					activityId = orderPrice.activityShows.get(0).id.toString();
					reduce = orderPrice.reduce;
				}
				break;
	
			case Use_Activity_Cancle_Use_Activity:
				
				/*优惠活动*/
				discount_activity_lin.setVisibility(View.VISIBLE);
				discount_activity_name.setText("请选择优惠活动");	
				discount_activity_ok.setBackgroundResource(R.drawable.ordersubmit2_reader_sel);
				
				/*优惠券和不使用优惠的处理*/
				discount_ticket_name.setText("");	
				discount_ticket_ok.setBackgroundResource(R.drawable.ordersubmit2_reader_nol);
				discount_notuse_ok.setBackgroundResource(R.drawable.ordersubmit2_reader_nol);
				
				/*费用显示*/
				d_activity_lin.setVisibility(View.GONE);
				init_Change_Money(0);
				break;
		
			case Use_Ticket:
				
				/*优惠券*/
				discount_ticket_name.setText(ticket_list.get(Ticket_List_Position).title);	
				discount_ticket_ok.setBackgroundResource(R.drawable.ordersubmit2_reader_sel);
				
				/*优惠活动和不使用优惠的处理*/	
				if(Public_Param.order_paramas.isHasActivity){
					discount_activity_lin.setVisibility(View.GONE);
					discount_activity_name.setText("");	
					discount_activity_ok.setBackgroundResource(R.drawable.ordersubmit2_reader_nol);
				}
				discount_notuse_ok.setBackgroundResource(R.drawable.ordersubmit2_reader_nol);
								
				/*费用显示*/
				d_activity_lin.setVisibility(View.VISIBLE);
				d_activity_name.setText("优惠券");
				d_activity_detail.setText(ticket_list.get(Ticket_List_Position).title);
				d_activity_all.setText("￥-"+ticket_list.get(Ticket_List_Position).amount);
				init_Change_Money(ticket_list.get(Ticket_List_Position).amount.intValue());
				
				/*优惠券参数*/
				couponNumber = ticket_list.get(Ticket_List_Position).id.toString();
				
				break;
			
			case Use_Ticket_NoTickets:
				
				/*优惠券*/
				discount_ticket_name.setText("没有可用的优惠券");	
				discount_ticket_ok.setBackgroundResource(R.drawable.ordersubmit2_reader_sel);
				
				/*优惠活动和不使用优惠的处理*/	
				if(Public_Param.order_paramas.isHasActivity){
					discount_activity_lin.setVisibility(View.GONE);
					discount_activity_name.setText("");	
					discount_activity_ok.setBackgroundResource(R.drawable.ordersubmit2_reader_nol);
				}
				discount_notuse_ok.setBackgroundResource(R.drawable.ordersubmit2_reader_nol);
								
				/*费用显示*/
				d_activity_lin.setVisibility(View.GONE);
				init_Change_Money(0);
				
				break;
			
			case Use_Ticket_Cancle_Use_Tickets:
				
				/*优惠券*/
				discount_ticket_name.setText("请选择优惠券");	
				discount_ticket_ok.setBackgroundResource(R.drawable.ordersubmit2_reader_sel);
				
				/*优惠活动和不使用优惠的处理*/	
				if(Public_Param.order_paramas.isHasActivity){
					discount_activity_lin.setVisibility(View.GONE);
					discount_activity_name.setText("");	
					discount_activity_ok.setBackgroundResource(R.drawable.ordersubmit2_reader_nol);
				}
				discount_notuse_ok.setBackgroundResource(R.drawable.ordersubmit2_reader_nol);
								
				/*费用显示*/
				d_activity_lin.setVisibility(View.GONE);
				init_Change_Money(0);
				
				break;	
				
			case Not_Use:
				/*不使用优惠活动*/
				discount_notuse_ok.setBackgroundResource(R.drawable.ordersubmit2_reader_sel);
				
				/*优惠券和优惠活动的处理*/
				discount_ticket_name.setText("");	
				discount_ticket_ok.setBackgroundResource(R.drawable.ordersubmit2_reader_nol);
				if(Public_Param.order_paramas.isHasActivity){
					discount_activity_lin.setVisibility(View.GONE);
					discount_activity_name.setText("");	
					discount_activity_ok.setBackgroundResource(R.drawable.ordersubmit2_reader_nol);
				}
				
				/*费用显示*/
				d_activity_lin.setVisibility(View.GONE);
				init_Change_Money(0);
				break;
				
			default:
				break;
		}
		
	}
	
	/** 显示活动 */
	private void load_Tickets(){
		
		isRequestPriceOk = false;
		
		String uid = new Integer(SharedPreferenceHelper.getUid(this)).toString();
		
		String willBeUseTimeBegin = TimeHelper.getTicketStart_EndTime(Public_Param.order_paramas.takeCarDate);
		String illBeUseTimeEnd = TimeHelper.getTicketStart_EndTime(Public_Param.order_paramas.returnCarDate);
		String consume = new Integer(orderPrice.totalPrice.intValue()+serviceAllAmount).toString();
		String api = "api/me/coupon?consume="+consume+"&currentPage=1&pageSize=100&state=2&uid="+uid+"&willBeUseTimeBegin="+willBeUseTimeBegin+"&willBeUseTimeEnd="+illBeUseTimeEnd+"&source=4";
		new HttpHelper().initData(HttpHelper.Method_Get, this, api, null, null, handler, Request_Ticket, 1, new TypeReference<ArrayList<TicketInfo>>() {});	
	}
	
	private void init_Change_Money(int money){
		
		activityAmount = money;
		d_all_all.setText("￥"+StringHelper.getMoney(new Float(orderPrice.totalPrice.floatValue() + serviceAllAmount - activityAmount).toString()));System.out.println("g14*************************");
		System.out.println("租车费用：￥"+orderPrice.totalPrice.floatValue());
		System.out.println("增值服务：￥"+serviceAllAmount);
		System.out.println("优惠费用：￥-"+activityAmount);
		System.out.println("总价值"+new Float(orderPrice.totalPrice.floatValue() + serviceAllAmount - activityAmount).toString());
		System.out.println("MyFlag:"+MyFlag);
	}
	
	/** 发送城市信息  */
	private void sendOrderWrite() {
		System.out.println("双击点击-------------------");
		if(orderPrice == null){
			ToastHelper.showNoNetworkToast(Activity_Order_Submit.this);
			return;
		}
		isOrderSubmit = true;
		// 创建默认的客户端实例  
		HttpClient httpCLient = new DefaultHttpClient();

		JSONObject jsonObject = new JSONObject(); //**********************注意json发送数据时，要这样
			
		jsonObject.put("averagePrice",orderPrice.averagePrice);System.out.println("averagePrice"+orderPrice.averagePrice);
		jsonObject.put("basicInsuranceAmount", orderPrice.basicInsuranceAmount);System.out.println("basicInsuranceAmount"+orderPrice.basicInsuranceAmount);//基本保险金额		
		jsonObject.put("brandId", Public_Param.order_paramas.brandId);System.out.println("brandId"+Public_Param.order_paramas.brandId);//车型ID			
//		jsonObject.put("couponNumber","");
		jsonObject.put("modelId", Public_Param.order_paramas.modelId);System.out.println("modelId"+Public_Param.order_paramas.modelId);//车模型ID
				
		jsonObject.put("orderState", 1);//订单状态（0：待支付，1：已下单 2：租赁中 3：已还车 4：已完成 5：已取消 6：NoShow）
		
		
		jsonObject.put("orderType", Public_Param.order_paramas.isDoorToDoor.intValue() == 1 ? 2 : 1);//门到门2，短租自驾1
		jsonObject.put("orderValueAddedServiceRelativeShow",new OrderSubmit_Helper().getServiceAmout(orderPrice.daySum.intValue()));System.out.println("orderValueAddedServiceRelativeShow"+new OrderSubmit_Helper().getServiceAmout(orderPrice.daySum.intValue()));
		jsonObject.put("payAmount", StringHelper.getMoney(new Float(orderPrice.totalPrice.floatValue() + serviceAllAmount - activityAmount).toString()));System.out.println("payAmount"+StringHelper.getMoney(new Float(orderPrice.totalPrice.floatValue() + serviceAllAmount - activityAmount).toString()));//订单总金额				
		jsonObject.put("payWay", Public_Param.order_paramas.payWay.intValue());System.out.println("payWay"+Public_Param.order_paramas.payWay.intValue());//支付方式（0：门店现金 1：门店POS刷卡 2：在线网银 3：在线支付宝）
		
		jsonObject.put("poundageAmount", orderPrice.poundageAmount.details.get(0).price);System.out.println("poundageAmount"+orderPrice.poundageAmount.details.get(0).price);//手续费
		jsonObject.put("prepay",new Integer(orderPrice.preAuthorization).intValue());System.out.println("prepay"+orderPrice.preAuthorization);//预授权
		jsonObject.put("reduce",null);
		jsonObject.put("rentalAmount", orderPrice.totalAmount);System.out.println("rentalAmount"+orderPrice.totalAmount);//租车金额
		jsonObject.put("rentalId", orderPrice.rentalIds);System.out.println("rentalId"+orderPrice.rentalIds);
		
		jsonObject.put("returnCarAddress", Public_Param.order_paramas.returnCarAddress);System.out.println("returnCarAddress"+Public_Param.order_paramas.returnCarAddress);
		jsonObject.put("returnCarCity", Public_Param.order_paramas.returnCarCityId);System.out.println("returnCarCity"+Public_Param.order_paramas.returnCarCityId);
		jsonObject.put("returnCarDate",Long.parseLong(TimeHelper.getSearchTime_Mis(Public_Param.order_paramas.returnCarDate)));System.out.println("returnCarDate"+Long.parseLong(TimeHelper.getSearchTime_Mis(Public_Param.order_paramas.returnCarDate)));			
		jsonObject.put("returnCarLatitude",Public_Param.order_paramas.returnCarLatitude);System.out.println("returnCarLatitude"+Public_Param.order_paramas.returnCarLatitude);
		
		jsonObject.put("returnCarLongitude",Public_Param.order_paramas.returnCarLongitude);System.out.println("returnCarLongitude"+Public_Param.order_paramas.returnCarLongitude);
		jsonObject.put("returnCarStoreId",Public_Param.order_paramas.returnCarStoreId);System.out.println("returnCarStoreId"+Public_Param.order_paramas.returnCarStoreId);	
		jsonObject.put("serviceType", Public_Param.order_paramas.isDoorToDoor.intValue() == 1 ? "3":"0");
		jsonObject.put("source",new Integer(Public_Platform.P_Android).intValue());
		jsonObject.put("takeCarAddress",Public_Param.order_paramas.takeCarAddress);System.out.println("takeCarAddress"+Public_Param.order_paramas.takeCarAddress);
		
		jsonObject.put("takeCarCity",Public_Param.order_paramas.takeCarCityId);System.out.println("takeCarCity"+Public_Param.order_paramas.takeCarCityId);
		jsonObject.put("takeCarDate",Long.parseLong(TimeHelper.getSearchTime_Mis(Public_Param.order_paramas.takeCarDate)));System.out.println("takeCarDate"+Long.parseLong(TimeHelper.getSearchTime_Mis(Public_Param.order_paramas.takeCarDate)));
		jsonObject.put("takeCarLatitude",Public_Param.order_paramas.takeCarLatitude);System.out.println("takeCarLatitude"+Public_Param.order_paramas.takeCarLatitude);
		jsonObject.put("takeCarLongitude",Public_Param.order_paramas.takeCarLongitude);System.out.println("takeCarLongitude"+Public_Param.order_paramas.takeCarLongitude);
		jsonObject.put("takeCarStoreId",Public_Param.order_paramas.takeCarStoreId);System.out.println("takeCarStoreId"+Public_Param.order_paramas.takeCarStoreId);
	
		jsonObject.put("tenancyDays",orderPrice.daySum);System.out.println("tenancyDays"+orderPrice.daySum);		
		jsonObject.put("timeoutPrice",orderPrice.delayAmount);System.out.println("timeoutPrice"+orderPrice.delayAmount);
		jsonObject.put("toStoreReduce", orderPrice.toStoreReduce);System.out.println("toStoreReduce"+orderPrice.toStoreReduce);
		jsonObject.put("totalTasicInsuranceAmount",orderPrice.totalBasicInsuranceAmount);System.out.println("totalTasicInsuranceAmount"+orderPrice.totalBasicInsuranceAmount);
		jsonObject.put("totalTimeoutPrice",orderPrice.totalDelayAmount);System.out.println("totalTimeoutPrice"+orderPrice.totalDelayAmount);
	
		jsonObject.put("userId",SharedPreferenceHelper.getUid(this));System.out.println("userId"+SharedPreferenceHelper.getUid(this));
		jsonObject.put("vendorId",1);

		if(MyFlag == Use_Activity){
			jsonObject.put("activityId",activityId);
			jsonObject.put("reduce",reduce);
			jsonObject.put("couponNumber","");
		}else{
			if(MyFlag == Use_Ticket){
				jsonObject.put("couponNumber",couponNumber);
			}else{
				jsonObject.put("couponNumber","");
				jsonObject.put("activityId",0);
			}
		}

		//		"orderValueAddedServiceRelativeShow" : [
//		                                        {
//		                                          "description" : "é€è½¦ä¸Šé—¨",
//		                                          "serviceAmount" : 10,
//		                                          "serviceId" : 3
//		                                        },
//		                                        {
//		                                          "description" : "ä¸Šé—¨å–è½¦",
//		                                          "serviceAmount" : 30,
//		                                          "serviceId" : 4
//		                                        },
//		                                        {
//		                                          "description" : "æ‰‹ç»­è´¹",
//		                                          "serviceAmount" : 30,
//		                                          "serviceId" : 1
//		                                        }
//		                                      ]

		
		StringEntity requestentity = null;
		try {System.out.println("3bbbbbbbbbbbb");
			requestentity = new StringEntity(jsonObject.toString(), "utf-8");System.out.println("4bbbbbbbbbbbb");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}//解决中文乱码问题    
		requestentity.setContentEncoding("UTF-8");
		requestentity.setContentType("application/json");

		System.out.println("5bbbbbbbbbbbb");
		// 创建get请求实例  
//		HttpPost httppost = new HttpPost("http://121.40.157.200:7890/api/user/"+new Integer(SharedPreferenceHelper.getUid(this)).toString() +"/order");//**********************注意请求方法  
		
		String api = "api/user/"+new Integer(SharedPreferenceHelper.getUid(this)).toString()+"/order";
		//String api = "api/user/"+"1"+"/order";
		if(Public_Param.order_paramas.isDoorToDoor.intValue() == 1){
			api = "api/door/user/order";
		}
		HttpPost httppost = new HttpPost(Public_Api.appWebSite+api);//**********************注意请求方法  
		System.out.println("api--------------------"+api);
		httppost.setHeader("Content-Type", "application/json;charset=UTF-8");
		AddCookies(httppost);
		httppost.setEntity(requestentity);
		try {
			System.out.println("6bbbbbbbbbbbb");
			// 客户端执行get请求 返回响应实体  
			HttpResponse response = httpCLient.execute(httppost);System.out.println("1aaaaaa");
			if (response.getStatusLine().getStatusCode() == 200) {//请求成功
				System.out.println("2aaaaaa");
				// 获取响应消息实体  
				HttpEntity responseentity = response.getEntity();
				data = EntityUtils.toString(responseentity);
				
				System.out.println("3aaaaaa"+data);
				//判断响应信息
				org.json.JSONObject datajobject = new org.json.JSONObject(data);
				boolean status = datajobject.getBoolean("status");
				String message = datajobject.getString("message");
				orderId = message;
				
				if (status) {
					System.out.println("33333");
					
					//修改密码成功	
					handler.sendEmptyMessage(OK);

				} else {
					System.out.println("4444");
					//修改密码失败
					//handler.sendEmptyMessage(OK);
					Message msg = new Message();
					msg.what = Fail;
					Bundle bundle = new Bundle();
					bundle.putString("errorMsg", message);
					msg.setData(bundle);
					handler.sendMessage(msg);

				}

			} else {//请求失败
				HttpEntity responseentity = response.getEntity();
				data = EntityUtils.toString(responseentity);//handler.sendEmptyMessage(OK);
				System.out.println("FOUND NOT"+data);
				handler.sendEmptyMessage(DataFail);
				//handler.sendEmptyMessage(sendOrderDataFail);
				System.out.println("1sssssssssss");
			}

		} catch (ClientProtocolException e) { //请求异常  
			e.printStackTrace();
			handler.sendEmptyMessage(DataFail);
			System.out.println("2sssssssssss");
		} catch (IOException e) { //请求异常    
			e.printStackTrace();
			handler.sendEmptyMessage(DataFail);
			System.out.println("3sssssssssss");
		} catch (org.json.JSONException e) {
			e.printStackTrace();
			handler.sendEmptyMessage(DataFail);
			System.out.println("4sssssssssss");
		} finally {
			httpCLient.getConnectionManager().shutdown();
			System.out.println("sssssssssss");
		}
		
	}
	
	private String data(){
		String message = "最新版"+"\n"+
				"averagePrice:"+orderPrice.averagePrice+"\n"+
				"basicInsuranceAmount:"+orderPrice.basicInsuranceAmount+"\n"+			
		"brandId:"+Public_Param.order_paramas.brandId+"\n"+
		"modelId:"+Public_Param.order_paramas.modelId+"\n"+
		"orderState:"+1+"\n"+
			
		"orderType:"+ 1+"\n"+
		"payAmount:"+ orderPrice.totalPrice+"\n"+
		"payWay:"+1+"\n"+
		"poundageAmount:"+0+"\n"+
		"rentalAmount:"+orderPrice.totalAmount+"\n"+	
	
		"rentalId:"+orderPrice.rentalIds+"\n"+
		"returnCarAddress:"+Public_Param.order_paramas.returnCarAddress+"\n"+		
		"returnCarCity:"+Public_Param.order_paramas.returnCarCityId+"\n"+		
		"returnCarDate:"+Long.parseLong(TimeHelper.getSearchTime_Mis(Public_Param.order_paramas.returnCarDate))+"\n"+		
		"returnCarLatitude:"+Public_Param.order_paramas.returnCarLatitude+"\n"+		
	
		"returnCarLongitude:"+Public_Param.order_paramas.returnCarLongitude+"\n"+
		"returnCarStoreId:"+Public_Param.order_paramas.returnCarStoreId+"\n"+		
		"serviceType:"+0+"\n"+		
		"takeCarAddress:"+Public_Param.order_paramas.takeCarAddress+"\n"+		
		"takeCarCity:"+Public_Param.order_paramas.takeCarCity+"\n"+		
	
		"takeCarDate:"+Long.parseLong(TimeHelper.getSearchTime_Mis(Public_Param.order_paramas.takeCarDate))+"\n"+
		"takeCarLatitude:"+Public_Param.order_paramas.takeCarLatitude+"\n"+		
		"takeCarLongitude:"+Public_Param.order_paramas.takeCarLongitude+"\n"+		
		"takeCarStoreId:"+Public_Param.order_paramas.takeCarStoreId+"\n"+		
		"tenancyDays:"+orderPrice.daySum+"\n"+		
	
		"timeoutPrice:"+orderPrice.delayAmount+"\n"+
		"totalTasicInsuranceAmount:"+orderPrice.totalBasicInsuranceAmount+"\n"+		
		"totalTimeoutPrice:"+orderPrice.totalDelayAmount+"\n"+		
		"userId:"+22+"\n"+		
		"vendorId:"+Public_Param.order_paramas.vendorId;	
		
		return message;
	}
	
	/** 发送城市信息  */
	private void Update_RealName() {
		
		// 创建默认的客户端实例  
		HttpClient httpCLient = new DefaultHttpClient();

		JSONObject jsonObject = new JSONObject(); //**********************注意json发送数据时，要这样

		//参数含义https://github.com/HP-Enterprise/Rental653/issues/731		
		jsonObject.put("id", user.id);System.out.println("userId"+user.id);//租车用户ID
		jsonObject.put("realName", user_name.getText().toString());	
		jsonObject.put("credentialNumber", user_number.getText().toString());		
		jsonObject.put("nickName", user.nickName);	
		
		StringEntity requestentity = null;
		try {System.out.println("3bbbbbbbbbbbb");
			requestentity = new StringEntity(jsonObject.toString(), "utf-8");System.out.println("4bbbbbbbbbbbb");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}//解决中文乱码问题    
		requestentity.setContentEncoding("UTF-8");
		requestentity.setContentType("application/json");

		System.out.println("5bbbbbbbbbbbb");
		// 创建get请求实例  
		HttpPut httpput = new HttpPut(Public_Api.appWebSite+"api/me");//**********************注意请求方法  
		
		httpput.setHeader("Content-Type", "application/json;charset=UTF-8");
		AddCookies(httpput);
		
		httpput.setEntity(requestentity);
		try {
			System.out.println("6bbbbbbbbbbbb");
			// 客户端执行get请求 返回响应实体  
			HttpResponse response = httpCLient.execute(httpput);System.out.println("1aaaaaa");
			if (response.getStatusLine().getStatusCode() == 200) {//请求成功
				System.out.println("2aaaaaa");
				// 获取响应消息实体  
				HttpEntity responseentity = response.getEntity();
				String data = EntityUtils.toString(responseentity);
				System.out.println("3aaaaaa"+data);
				//判断响应信息
				org.json.JSONObject datajobject = new org.json.JSONObject(data);
				boolean status = datajobject.getBoolean("status");
				String message = datajobject.getString("message");
				
				if (status) {
					System.out.println("33333");

					//修改密码成功	
					handler.sendEmptyMessage(UpdateOk);

				} else {
					System.out.println("4444");
					//修改密码失败

					Message msg = new Message();
					msg.what = UpdateFail;
					Bundle bundle = new Bundle();
					bundle.putString("errorMsg", message);
					msg.setData(bundle);
					handler.sendMessage(msg);

				}

			} else {//请求失败
				HttpEntity responseentity = response.getEntity();
				String data = EntityUtils.toString(responseentity);
				System.out.println("FOUND NOT"+data);
				handler.sendEmptyMessage(UpdateDataFail);
				System.out.println("1sssssssssss");
			}

		} catch (ClientProtocolException e) { //请求异常  
			e.printStackTrace();
			handler.sendEmptyMessage(UpdateDataFail);
			System.out.println("2sssssssssss");
		} catch (IOException e) { //请求异常    
			e.printStackTrace();
			handler.sendEmptyMessage(UpdateDataFail);
			System.out.println("3sssssssssss");
		} catch (org.json.JSONException e) {
			e.printStackTrace();
			handler.sendEmptyMessage(UpdateDataFail);
			System.out.println("4sssssssssss");
		} finally {
			httpCLient.getConnectionManager().shutdown();
			System.out.println("sssssssssss");
		}
		
	}

	/**
     * 增加Cookie
     * @param request
     */
    public void AddCookies(HttpPut request)
    {
          StringBuilder sb = new StringBuilder();

          String key = "token";
          String val = SharedPreferenceHelper.getString(this, Public_SP.Account, key);
          sb.append(key);
          sb.append("=");
          sb.append(val);
          sb.append(";");

          request.addHeader("cookie", sb.toString());

          System.out.println(""+sb);
    }
	
    /**
     * 增加Cookie
     * @param request
     */
    public void AddCookies(HttpPost request)
    {
          StringBuilder sb = new StringBuilder();

          String key = "token";
          String val = SharedPreferenceHelper.getString(this, Public_SP.Account, key);
          sb.append(key);
          sb.append("=");
          sb.append(val);
          sb.append(";");

          request.addHeader("cookie", sb.toString());

          System.out.println(""+sb);
    }
	
}

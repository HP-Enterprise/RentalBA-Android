package com.gjcar.activity.fragment3;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baidu.mapapi.model.LatLng;
import com.gjcar.activity.fragment1.Activity_Order_Submit;
import com.gjcar.activity.user.more.Activity_Order_Ok;
import com.gjcar.annotation.ContentView;
import com.gjcar.annotation.ContentWidget;
import com.gjcar.app.R;
import com.gjcar.data.adapter.FreeRide_List_Adapter;
import com.gjcar.data.bean.FreeRide;
import com.gjcar.data.bean.Model____Vendor_Store_Price;
import com.gjcar.data.data.Public_Api;
import com.gjcar.data.data.Public_Data;
import com.gjcar.data.data.Public_Param;
import com.gjcar.data.data.Public_Platform;
import com.gjcar.data.service.CarList_Helper;
import com.gjcar.data.service.FreeRideHelper;
import com.gjcar.framwork.BaiduMapHelper;
import com.gjcar.utils.AnnotationViewUtils;
import com.gjcar.utils.HandlerHelper;
import com.gjcar.utils.HttpHelper;
import com.gjcar.utils.ImageLoaderHelper;
import com.gjcar.utils.IntentHelper;
import com.gjcar.utils.NetworkHelper;
import com.gjcar.utils.SharedPreferenceHelper;
import com.gjcar.utils.TimeHelper;
import com.gjcar.utils.ToastHelper;
import com.gjcar.utils.ValidateHelper;
import com.gjcar.view.dialog.SelectDailog;
import com.gjcar.view.dialog.SubmitDialog;
import com.gjcar.view.helper.LoadAnimateHelper;
import com.gjcar.view.helper.TitleBarHelper;
import com.gjcar.view.widget.CustomerScrollview_QQ;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@ContentView(R.layout.activity_freeride_order_submit)
public class Activity_FreeRide_Order_Submit extends Activity{
	
	/*初始化控件*/
	@ContentWidget(id = R.id.a_picture) ImageView a_picture;
	@ContentWidget(id = R.id.a_2city) TextView a_2city;
	@ContentWidget(id = R.id.a_model) TextView a_model;
	@ContentWidget(id = R.id.a_distance) TextView a_distance;
	@ContentWidget(id = R.id.a_note) TextView a_note;
	@ContentWidget(id = R.id.a_price) TextView a_price;
	
	@ContentWidget(id = R.id.b_take_city) TextView b_take_city;
	@ContentWidget(id = R.id.b_take_store) TextView b_take_store;
	@ContentWidget(id = R.id.b_return_city) TextView b_return_city;
	@ContentWidget(click = "onClick") LinearLayout b_return_store_lin;
	@ContentWidget(id = R.id.b_return_store) TextView b_return_store;
	
	@ContentWidget(click = "onClick") LinearLayout take_time_lin;
	@ContentWidget(id = R.id.b_take_date) TextView b_take_date;
	@ContentWidget(id = R.id.b_take_time) TextView b_take_time;
	@ContentWidget(id = R.id.b_days) TextView b_days;
	@ContentWidget(id = R.id.b_return_date) TextView b_return_date;
	@ContentWidget(id = R.id.b_return_time) TextView b_return_time;
	
	@ContentWidget(click = "onClick") Button ok;
	
	/*qq*/
	@ContentWidget(id = R.id.content) CustomerScrollview_QQ content;
	
	/*Handler*/
	private Handler handler;
	private final static int Request_Submit = 1;
	private final static int Select_Time = 2;
	private final static int Request_Data = 3;	
	private final static int Request_Black = 6;
	/*其它*/
	private String takeTime = "";
	private String returnTime = "";
	String[] dates = null;
	String[] times = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		AnnotationViewUtils.injectObject(this, this);

		Public_Param.list_free_order_activity.add(this);
		
		/*Handler*/
		initHandler();
		
		/*标题*/
		TitleBarHelper.Back(this, "顺风车预定", 0);
		
		/*加载动画*/
		LoadAnimateHelper.Search_Animate(this, R.id.activity, handler, 0, false,true,1);
		
		/*初始化数据*/
		initData();
		
	}
	
	public void onClick(View view){

		switch (view.getId()) {
	
			case R.id.b_return_store_lin:
				System.out.println("开始搜索");
				String[] stores = new FreeRideHelper().getStores(Public_Param.freeRide.returnCarStoreShows);
				int[] ids_store = new FreeRideHelper().getIds(Public_Param.freeRide.returnCarStoreShows.size());
				SelectDailog.select(this, "", b_return_store, stores, ids_store);
				break;
			
			case R.id.ok:
				
				/*判断是否有网*/
				if(!NetworkHelper.isNetworkAvailable(Activity_FreeRide_Order_Submit.this)){
					return;
				}
			
				/*判断输入是否正确*/
				if(!ValidateHelper.Validate(Activity_FreeRide_Order_Submit.this, new boolean[]{((Integer)b_return_store.getTag()).intValue() == -1},new String[]{"请选择换车门店"})){
					return;
				}

				/*判断时间是否迟于当前时间*/
				if(!TimeHelper.isLate(takeTime)){
					ToastHelper.showToastShort(Activity_FreeRide_Order_Submit.this, "取车时间必须晚于当前时间");
					return;
				}
							
				/*弹出提交对话框*/
				SubmitDialog.showSubmitDialog(Activity_FreeRide_Order_Submit.this);
				
				new HttpHelper().initData(HttpHelper.Method_Get, this, "api/isBlack/"+SharedPreferenceHelper.getUid(this), null, null, handler, Request_Black, 2, null);

				break;
	
			case R.id.take_time_lin:
				
				dates = TimeHelper.getFreeTimes(Public_Param.freeRide.takeCarDateStart, Public_Param.freeRide.takeCarDateEnd);
				//int[] ids = TimeHelper.getFreeTakeTimeIds(Public_Param.freeRide.takeCarDateStart, Public_Param.freeRide.takeCarDateEnd);
				times = new String[]{"08:00","09:00","10:00","11:00","12:00","13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00"};
				SelectDailog.selectFreeRideTime(this, "选择时间", b_take_date, b_take_time, dates, times,handler ,Select_Time);							
				break;

			default:
				break;
		}
		
	}
	
	private void initData() {System.out.println("1");
	
		String api = "api/freeRide/"+Public_Param.freeRideId.toString();			
		System.out.println("startDate"+Public_Param.freeRideId);
		
		new HttpHelper().initData(HttpHelper.Method_Get, this, api, null, null, handler, Request_Data, 1, new TypeReference<FreeRide>() {});
		
	}
	
	private void initView(){
								
		/*车型信息*/
		content.setVisibility(View.VISIBLE);
		
		ImageLoader.getInstance().displayImage(Public_Api.appWebSite + Public_Param.freeRide.vehicleShow.vehicleModelShow.picture, a_picture, ImageLoaderHelper.initDisplayImageOptions());
				
		a_2city.setText(Public_Param.freeRide.takeCarCityShow.cityName+" - "+Public_Param.freeRide.returnCarCityShow.cityName);
		a_model.setText(Public_Param.freeRide.vehicleShow.vehicleModelShow.model);			
		a_distance.setText(Public_Param.freeRide.maxMileage.toString());
		a_note.setText("自理");
		a_price.setText("￥"+Public_Param.freeRide.price.toString());
		
		b_return_store.setTag(-1);
		b_take_city.setText(Public_Param.freeRide.takeCarCityShow.cityName);	
		b_take_store.setText(Public_Param.freeRide.takeCarStoreShow.storeName);	
		b_return_city.setText(Public_Param.freeRide.returnCarCityShow.cityName);	
			
		b_take_date.setTag(-1);
		b_take_date.setText(TimeHelper.getDateTime_YM(Public_Param.freeRide.takeCarDateStart));
		b_take_time.setText(TimeHelper.getWeekTime(Public_Param.freeRide.takeCarDateStart));
		b_return_date.setText(TimeHelper.getDateTime_YM(TimeHelper.getDateTime_YMD_XDays(Public_Param.freeRide.maxRentalDay.intValue(),Public_Param.freeRide.takeCarDateStart)));
		b_return_time.setText(TimeHelper.getWeekTime(TimeHelper.getDateTime_YMD_XDays(Public_Param.freeRide.maxRentalDay.intValue(),Public_Param.freeRide.takeCarDateStart)));
		b_days.setText("限租"+Public_Param.freeRide.maxRentalDay.toString()+"天");
		
		takeTime = Public_Param.freeRide.takeCarDateStart;
		returnTime = TimeHelper.getDateTime_YMD_XDays(Public_Param.freeRide.maxRentalDay.intValue(),Public_Param.freeRide.takeCarDateStart);
	
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
							ToastHelper.showToastShort(Activity_FreeRide_Order_Submit.this, "系统繁忙");
						}else{
							
							if(HandlerHelper.getString(msg).equals(HandlerHelper.Fail)){
								
								Request_Submit();
								
							}else{
								
								//重新加载
								new Thread(){
									
									public void run() {
										
										try {
											
											Thread.sleep(2000);
											new HttpHelper().initData(HttpHelper.Method_Get, Activity_FreeRide_Order_Submit.this, "api/isBlack/"+SharedPreferenceHelper.getUid(Activity_FreeRide_Order_Submit.this), null, null, handler, Request_Black, 2, null);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
																		
									};
								}.start();
							
							}
						}
					
					break;
				
					case Request_Submit:
						SubmitDialog.closeSubmitDialog();
																	
						if(msg.getData().getString("message").equals("ok")){
							System.out.println("f00a");
							finish();//结束之前的页面
							for (int i = 0; i < Public_Param.list_order_activity.size(); i++) {
								Public_Param.list_order_activity.get(i).finish();
							}	System.out.println("f00b");						
							Public_Param.list_order_activity.clear();System.out.println("f1");
							
							Intent intent = new Intent(Activity_FreeRide_Order_Submit.this, Activity_Order_Ok.class);//跳转
							
							intent.putExtra("model", Public_Param.freeRide.vehicleShow.vehicleModelShow.model);
							intent.putExtra("days", Public_Param.freeRide.maxRentalDay.toString());System.out.println("f2");
							intent.putExtra("orderId", msg.getData().getString("data"));						
							intent.putExtra("acount", Public_Param.freeRide.price.toString());System.out.println("f3");
							intent.putExtra("payWay", "3");
							
							intent.putExtra("way", "freeride");System.out.println("f4");
							
							startActivity(intent);

						}else{
							//下单失败
							if(msg.getData().getString("message").equals(HandlerHelper.Fail)){
								
								ToastHelper.showToastShort(Activity_FreeRide_Order_Submit.this, msg.getData().getString("data"));
							}else{
								
								ToastHelper.showToastShort(Activity_FreeRide_Order_Submit.this, "下订单失败");
							}
							
						}
						break;
					
					case Select_Time:System.out.println("时间id"+((String)b_take_date.getTag()).toString());
						takeTime = ((String)b_take_date.getTag()).toString();
						returnTime = TimeHelper.getDateTime_YMD_XDays(Public_Param.freeRide.maxRentalDay.intValue(),takeTime);
						System.out.println("takeTime时间id"+takeTime);System.out.println("returnTime时间id"+returnTime);
						b_return_date.setText(TimeHelper.getDateTime_YM(returnTime));
						b_return_time.setText(TimeHelper.getWeekTime(returnTime));						
						break;
					
					case Request_Data:
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
	
							LoadAnimateHelper.load_success_animation();
							Public_Param.freeRide = (FreeRide)msg.obj;

				            initView();
				           	return;
						}
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Empty)){
							LoadAnimateHelper.load_empty_animation();
				           	System.out.println("请求失败");	            
						}
						if(HandlerHelper.getString(msg).equals(HandlerHelper.DataFail)){
							LoadAnimateHelper.load_fail_animation();
				           	System.out.println("请求失败");	            
						}
						if(HandlerHelper.getString(msg).equals(HandlerHelper.DataFail)){
							LoadAnimateHelper.load_fail_animation();
				           	System.out.println("请求失败");	            
						}
						break;
						
					default:
						break;
				}
			}
		};
	}
	
	/** 提交信息  */
	private void  Request_Submit(){

		/*参数*/
		JSONObject jsonObject = new JSONObject(); //**********************注意json发送数据时，要这样

		System.out.println("开始");
		jsonObject.put("basicInsuranceAmount", 0);//System.out.println("basicInsuranceAmount"+Public_Parameter.order_store_paramas.basicInsuranceAmount);//基本保险金额
		jsonObject.put("brandId", Public_Param.freeRide.vehicleShow.vehicleModelShow.brandId);//System.out.println("brandId"+Public_Parameter.order_store_paramas.brandId);//车型ID			
		jsonObject.put("modelId", Public_Param.freeRide.vehicleShow.modelId);//System.out.println("modelId"+Public_Parameter.order_store_paramas.modelId);//车模型ID
		jsonObject.put("orderState", 1);//订单状态（0：待支付，1：已下单 2：租赁中 3：已还车 4：已完成 5：已取消 6：NoShow）
		System.out.println("开始1");
		jsonObject.put("orderType", 1);//业务类型(1:短租自驾 2:短租带驾 3:接送机)
		jsonObject.put("payAmount", Public_Param.freeRide.price);//System.out.println("payAmount"+Public_Parameter.order_store_paramas.prepayAmount);//订单总金额
		jsonObject.put("payWay", 3);//支付方式（0：门店现金 1：门店POS刷卡 2：在线网银 3：在线支付宝）
		jsonObject.put("poundageAmount", 0);//手续费
		jsonObject.put("rentalAmount", Public_Param.freeRide.price);//System.out.println("rentalAmount"+Public_Parameter.order_store_paramas.avgAmount);//租车金额
		System.out.println("开始2");
		
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(Public_Param.freeRide.id);
		
		jsonObject.put("rentalId", list);System.out.println("Public_Param.freeRide.id:"+Public_Param.freeRide.id);
		jsonObject.put("returnCarAddress", Public_Param.freeRide.returnCarStoreShows.get(((Integer)b_return_store.getTag()).intValue()).storeAddr);System.out.println("storeAddress"+Public_Param.freeRide.returnCarStoreShows.get(((Integer)b_return_store.getTag()).intValue()).storeAddr);
		jsonObject.put("returnCarCity", Public_Param.freeRide.returnCarCity);System.out.println("returnCarCity"+Public_Param.freeRide.returnCarCity);
		jsonObject.put("returnCarDate",Long.parseLong(TimeHelper.getSearchTime_Mis(returnTime)));System.out.println("returnTime2:"+returnTime);
		
		jsonObject.put("returnCarStoreId",Public_Param.freeRide.returnCarStoreShows.get(((Integer)b_return_store.getTag()).intValue()).id);System.out.println("returnCarStoreId"+Public_Param.freeRide.returnCarStoreShows.get(((Integer)b_return_store.getTag()).intValue()).id);		
		jsonObject.put("serviceType",0);//取车还车方式 0仅支持门店取还 1门店取上门还 2 上门取门店还 3上门取上门还
		jsonObject.put("takeCarAddress",Public_Param.freeRide.takeCarStoreShow.storeAddr);
		jsonObject.put("takeCarCity",Public_Param.freeRide.takeCarCity);
		
		jsonObject.put("takeCarDate",Long.parseLong(TimeHelper.getSearchTime_Mis(takeTime)));System.out.println("takeTime:"+takeTime);
		jsonObject.put("takeCarStoreId",Public_Param.freeRide.takeCarStoreId);
		jsonObject.put("tenancyDays",Public_Param.freeRide.maxRentalDay);
		
		jsonObject.put("timeoutPrice",0);
		jsonObject.put("totalTasicInsuranceAmount",0);
		jsonObject.put("totalTimeoutPrice",0);
		jsonObject.put("userId",SharedPreferenceHelper.getUid(this));System.out.println("userId"+SharedPreferenceHelper.getUid(this));
		jsonObject.put("vendorId",Public_Param.freeRide.vendorId);
		
		jsonObject.put("vehicleId",Public_Param.freeRide.vehicleId);
		
		jsonObject.put("source",Public_Platform.P_Android);
		/*提交*/
		new HttpHelper().initData(HttpHelper.Method_Post, this, "api/user/"+SharedPreferenceHelper.getUid(this)+"/freeRideOrder", jsonObject, null, handler, Request_Submit, 2, null);
		
	}	

}

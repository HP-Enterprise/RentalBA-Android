package com.gjcar.activity.user.more;

import java.util.ArrayList;

import com.alibaba.fastjson.TypeReference;
import com.gjcar.activity.fragment1.Activity_Order_Submit;
import com.gjcar.annotation.ContentView;
import com.gjcar.annotation.ContentWidget;
import com.gjcar.app.R;
import com.gjcar.data.adapter.OrderSubmit_ServiceList_Adapter;
import com.gjcar.data.adapter.ServiceAmountList_Adapter;
import com.gjcar.data.bean.Order;
import com.gjcar.data.bean.User;
import com.gjcar.data.data.Public_Api;
import com.gjcar.data.data.Public_Param;
import com.gjcar.utils.AnnotationViewUtils;
import com.gjcar.utils.HandlerHelper;
import com.gjcar.utils.HttpHelper;
import com.gjcar.utils.ImageLoaderHelper;
import com.gjcar.utils.StringHelper;
import com.gjcar.utils.TimeHelper;
import com.gjcar.view.helper.LoadAnimateHelper;
import com.gjcar.view.helper.TitleBarHelper;
import com.gjcar.view.helper.ViewInitHelper;
import com.gjcar.view.listview.MyListView;
import com.gjcar.view.widget.CustomerScrollview_QQ;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@ContentView(R.layout.activity_freeride_order_detail)
public class Activity_FreeRide_Order_Detail extends Activity{
	
	/*初始化控件*/
	@ContentWidget(id = R.id.pay_state) TextView pay_state;//取车证件
	@ContentWidget(id = R.id.pay_card) TextView pay_card;
	@ContentWidget(id = R.id.pay_way) TextView pay_way;
	@ContentWidget(id = R.id.pay_money) TextView pay_money;//待付款：支付金额，确定支付
	@ContentWidget(id = R.id.pay_money_lin) LinearLayout pay_money_lin;
	
	@ContentWidget(id = R.id.a_orderId) TextView a_orderId;
	@ContentWidget(id = R.id.a_picture) ImageView a_picture;
	@ContentWidget(id = R.id.a_model) TextView a_model;
	@ContentWidget(id = R.id.a_note) TextView a_note;
	
	@ContentWidget(id = R.id.b_take_date) TextView b_take_date;
	@ContentWidget(id = R.id.b_take_time) TextView b_take_time;
	@ContentWidget(id = R.id.b_days) TextView b_days;
	@ContentWidget(id = R.id.b_return_date) TextView b_return_date;
	@ContentWidget(id = R.id.b_return_time) TextView b_return_time;
	
	@ContentWidget(id = R.id.c_city) TextView c_city;
	@ContentWidget(id = R.id.c_address) TextView c_address;
	@ContentWidget(id = R.id.c_r_city) TextView c_r_city;
	@ContentWidget(id = R.id.c_r_address) TextView c_r_address;
	
	@ContentWidget(id = R.id.d_service_all) TextView d_service_all;
	
	@ContentWidget(id = R.id.d_all_all) TextView d_all_all;
	
	/*Handler*/
	private Handler handler;
	private final static int Click = 2;
	private final static int Request_User = 6;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		AnnotationViewUtils.injectObject(this, this);

		/*Handler*/
		initHandler();
		
		/*标题*/
		TitleBarHelper.Back(this, "顺风车订单详情", 0);

		/*加载费用*/
		init();
		
		/*加载用车人信息*/
		new HttpHelper().initData(HttpHelper.Method_Get, this, "api/me", null, null, handler, Request_User, 1, new TypeReference<User>() {});

	}

	private void init(){
				
		System.out.println("1");
		if(Public_Param.order.orderState.intValue() == 0){
			pay_money_lin.setVisibility(View.VISIBLE);
		}
		
		pay_state.setText("订单状态:"+StringHelper.getStringType(Public_Param.order.orderState.intValue(), new String[]{"待支付","已下单","租赁中","已还车","已完成","已取消","NoShow"}));System.out.println("2");//0：待支付，1：已下单 2：租赁中 3：已还车 4：已完成 5：已取消 6：NoShow
		if(Public_Param.order.userShow.credentialType !=null){pay_card.setText(StringHelper.getcredentialType(Public_Param.order.userShow.credentialType));}	System.out.println("3");	
		pay_money.setText("￥"+Public_Param.order.payAmount.toString());System.out.println("4");
		
		if(Public_Param.order.payWay.intValue() == 3){//3表示在线支付
			pay_way.setText("在线支付");
		}else{//0表示门店支付
			pay_way.setText("门店支付");
		}
		
		ImageLoader.getInstance().displayImage(Public_Api.appWebSite + Public_Param.order.picture, a_picture, ImageLoaderHelper.initDisplayImageOptions());
		a_orderId.setText(Public_Param.order.orderId.toString());
		a_model.setText(Public_Param.order.model);	System.out.println("5");	
		a_note.setText(Public_Param.order.carGroupstr+"/"+Public_Param.order.carTrunkStr+"/"+Public_Param.order.seatsStr);
		
		b_take_date.setText(TimeHelper.getDateTime_YM(TimeHelper.getTimemis_to_StringTime(Public_Param.order.takeCarDate)));
		b_take_time.setText(TimeHelper.getWeekTime(TimeHelper.getTimemis_to_StringTime(Public_Param.order.takeCarDate)));
		b_days.setText(Public_Param.order.tenancyDays.toString());
		b_return_date.setText(TimeHelper.getDateTime_YM(TimeHelper.getTimemis_to_StringTime(Public_Param.order.returnCarDate)));
		b_return_time.setText(TimeHelper.getWeekTime(TimeHelper.getTimemis_to_StringTime(Public_Param.order.returnCarDate)));
		
		c_city.setText(Public_Param.order.takeCarCity);System.out.println("6");
		c_address.setText(Public_Param.order.takeCarStore.storeName+"("+Public_Param.order.takeCarStore.storeAddr+")");System.out.println("7");
		c_r_city.setText(Public_Param.order.returnCarCity);System.out.println("11");
		c_r_address.setText(Public_Param.order.returnCarStore.storeName+"("+Public_Param.order.returnCarStore.storeAddr+")");System.out.println("12");

		d_service_all.setText("￥"+Public_Param.order.rentalAmount+ "元");System.out.println("8");
		d_all_all.setText("￥"+Public_Param.order.payAmount+"元");System.out.println("9");//总费用

	}
	
	public void onClick(View view){

		switch (view.getId()) {
		
			case R.id.cancle:
				
				finish();
				
				//IntentHelper.startActivity(Activity_Order_Submit.this, Activity_Order_Ok.class);
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

					case Click:
						
						break;	
						
					case Request_User:
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){

							User user = (User)msg.obj;
							if(user.credentialNumber != null && !user.credentialNumber.equals("null")){
								
								pay_card.setText(user.credentialNumber);							
							}
						}
						
						break;
					default:
						break;
				}
			}
		};
	}

}

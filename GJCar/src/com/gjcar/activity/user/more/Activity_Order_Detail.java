package com.gjcar.activity.user.more;


import com.alibaba.fastjson.TypeReference;
import com.gjcar.annotation.ContentView;
import com.gjcar.annotation.ContentWidget;
import com.gjcar.app.R;
import com.gjcar.data.adapter.ServiceAmountList_Adapter;
import com.gjcar.data.bean.Order;
import com.gjcar.data.bean.User;
import com.gjcar.data.data.Public_Api;
import com.gjcar.data.data.Public_BaiduTJ;
import com.gjcar.data.data.Public_Data;
import com.gjcar.data.data.Public_Param;
import com.gjcar.fragwork.alipay.AlipayHelper;
import com.gjcar.utils.AnnotationViewUtils;
import com.gjcar.utils.HandlerHelper;
import com.gjcar.utils.HttpHelper;
import com.gjcar.utils.ImageLoaderHelper;
import com.gjcar.utils.SharedPreferenceHelper;
import com.gjcar.utils.StringHelper;
import com.gjcar.utils.TimeHelper;
import com.gjcar.utils.ToastHelper;
import com.gjcar.view.dialog.SubmitDialog;
import com.gjcar.view.helper.LoadAnimateHelper;
import com.gjcar.view.helper.TitleBarHelper;
import com.gjcar.view.listview.MyListView;
import com.gjcar.view.widget.CustomDialog;
import com.gjcar.view.widget.CustomerScrollview_QQ;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@ContentView(R.layout.activity_order_detail)
public class Activity_Order_Detail extends Activity{
	
	/*��ʼ���ؼ�*/
	@ContentWidget(click = "onClick") Button pay_ok;
	@ContentWidget(id = R.id.pay_state) TextView pay_state;
	@ContentWidget(id = R.id.pay_card) TextView pay_card;
	@ContentWidget(id = R.id.pay_way) TextView pay_way;
	@ContentWidget(id = R.id.pay_money) TextView pay_money;
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
	
	@ContentWidget(id = R.id.d_service_detail) TextView d_service_detail;
	@ContentWidget(id = R.id.d_service_all) TextView d_service_all;
	@ContentWidget(id = R.id.d_base_detail) TextView d_base_detail;
	@ContentWidget(id = R.id.d_base_all) TextView d_base_all;
	@ContentWidget(id = R.id.d_other_lin) LinearLayout d_other_lin;
	@ContentWidget(id = R.id.d_other_detail) TextView d_other_detail;
	@ContentWidget(id = R.id.d_other_all) TextView d_other_all;
	@ContentWidget(id = R.id.d_activity_lin) LinearLayout d_activity_lin;
	@ContentWidget(id = R.id.d_activity_name) TextView d_activity_name;
	@ContentWidget(id = R.id.d_activity_detail) TextView d_activity_detail;
	@ContentWidget(id = R.id.d_activity_all) TextView d_activity_all;
	
	@ContentWidget(id = R.id.d_storereduce_lin) LinearLayout d_storereduce_lin;	//����ȡ��
	@ContentWidget(id = R.id.d_storereduce) TextView d_storereduce;
	@ContentWidget(id = R.id.d_storereduce_all) TextView d_storereduce_all;
	
	@ContentWidget(id = R.id.d_listview_lin) LinearLayout d_listview_lin;
	@ContentWidget(id = R.id.listview) MyListView listview;//��ѡ����
	
	@ContentWidget(id = R.id.d_all_all) TextView d_all_all;
	
	@ContentWidget(click = "onClick") Button cancle;
	@ContentWidget(id = R.id.order_all) CustomerScrollview_QQ order_all;
	
	/*Handler*/
	private Handler handler;
	private final static int Request_Data = 1;
	private final static int Click = 2;
	private final static int Cancle_Order = 3;
	private boolean isRequestPriceOk = false;	
	
	private final static int Request_User = 6;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		AnnotationViewUtils.injectObject(this, this);

		/*Handler*/
		initHandler();
		
		/*����*/
		TitleBarHelper.Back(this, "��������", 0);
		
		/*���ض���*/
		LoadAnimateHelper.Search_Animate(this, R.id.activity, handler, Click, true,true,1);
		
		/*�������*/
		Request_AmountDetail();
		
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
		Public_BaiduTJ.pageStart(this, Public_BaiduTJ.Activity_Order_Detail);	
	}
	
	@Override
	protected void onPause() {

		super.onPause();
		Public_BaiduTJ.pageEnd(this, Public_BaiduTJ.Activity_Order_Detail);	
	}

	/*���󶩵�����*/
	private void Request_AmountDetail() {
			
		/*���ط���*/
		String userId = new Integer(SharedPreferenceHelper.getUid(this)).toString();

		String api = "api/door/user/"+ Public_Param.order.orderId +"/order";
		System.out.println("way:"+getIntent().getStringExtra("way"));
		if(getIntent().getStringExtra("way").equals("order")){System.out.println("orderId:"+Public_Param.order.orderId);
		
			api = "api/user/"+ userId +"/order/"+Public_Param.order.orderId;
		}
		
		new HttpHelper().initData(HttpHelper.Method_Get, this, api, null, null, handler, Request_Data, 1, new TypeReference<Order>() {});		
		
	}

	private void init(){
	
		/*�����ó�����Ϣ*/
		new HttpHelper().initData(HttpHelper.Method_Get, this, "api/me", null, null, handler, Request_User, 1, new TypeReference<User>() {});
		
		order_all.setVisibility(View.VISIBLE);
		
		System.out.println("1");
		if(Public_Param.order.orderState.intValue() == 0){
			pay_money_lin.setVisibility(View.VISIBLE);
		}
		if(!getIntent().getStringExtra("way").equals("order")){
			
			pay_state.setText("����״̬:"+StringHelper.getStringType(Public_Param.order.orderState.intValue(), new String[]{"��֧��","���µ�","���µ�","���µ�","������", "������ ","������ ","�ѻ���","�����","��ȡ��"}));			
		}else{
			pay_state.setText("����״̬:"+StringHelper.getStringType(Public_Param.order.orderState.intValue(), new String[]{"��֧��","���µ� ","������","�ѻ���", "����� ","��ȡ�� ","NoShow"}));			
		}
		
		if(Public_Param.order.payWay.intValue() == 3){//3��ʾ����֧��
			pay_way.setText("����֧��");
		}else{//0��ʾ�ŵ�֧��
			pay_way.setText("�ŵ�֧��");
		}
	
		if(Public_Param.order.orderState.intValue() == 9){
			pay_state.setText("����״̬:"+"��ȡ��");
		}
		
		if(Public_Param.order.userShow.credentialType !=null){pay_card.setText(StringHelper.getcredentialType(Public_Param.order.userShow.credentialType));}	System.out.println("3");	
		pay_money.setText("��"+StringHelper.getMoney(Public_Param.order.payAmount));System.out.println("4");

		
		if(getIntent().getStringExtra("way").equals("doortodoor")){
			
			ImageLoader.getInstance().displayImage(Public_Api.appWebSite + Public_Param.order.vehicleModelShow.picture, a_picture, ImageLoaderHelper.initDisplayImageOptions());
			a_orderId.setText(Public_Param.order.orderId.toString());
			a_model.setText(Public_Param.order.vehicleModelShow.model);System.out.println("modela");
			
			String carGroup = StringHelper.getCarGroup(Public_Param.order.vehicleModelShow.carGroup);
			
			String carTrunk = Public_Param.order.vehicleModelShow.carTrunk+"��";

			String seats = Public_Param.order.vehicleModelShow.seats+"��";

			a_note.setText(carGroup+"/"+carTrunk+"/"+seats);System.out.println("modelb");
			
			c_city.setText(Public_Param.order.takeCarCityName);
			c_r_city.setText(Public_Param.order.returnCarCityName);System.out.println("modelh");

		}else{
			
			ImageLoader.getInstance().displayImage(Public_Api.appWebSite + Public_Param.order.picture, a_picture, ImageLoaderHelper.initDisplayImageOptions());		
			a_orderId.setText(Public_Param.order.orderId.toString());
			a_model.setText(Public_Param.order.model);	System.out.println("5");	
		
			String carGroup = Public_Param.order.carGroupstr;
			if(Public_Param.order.carGroupstr == null){
				carGroup = "";
			}
			
			String carTrunk = Public_Param.order.carTrunkStr;
			if(Public_Param.order.carTrunkStr == null){
				carTrunk = "";
			}

			String seats = Public_Param.order.seatsStr;
			if(Public_Param.order.seatsStr == null){
				seats = "";
			}

			a_note.setText(carGroup+"/"+carTrunk+"/"+seats);

			c_city.setText(Public_Param.order.takeCarCity);
			c_r_city.setText(Public_Param.order.returnCarCity);System.out.println("11");
		}
		

		b_take_date.setText(TimeHelper.getDateTime_YM(TimeHelper.getTimemis_to_StringTime(Public_Param.order.takeCarDate)));System.out.println("b");
		b_take_time.setText(TimeHelper.getWeekTime(TimeHelper.getTimemis_to_StringTime(Public_Param.order.takeCarDate)));System.out.println("c");
		b_days.setText(Public_Param.order.tenancyDays.toString());System.out.println("d");
		b_return_date.setText(TimeHelper.getDateTime_YM(TimeHelper.getTimemis_to_StringTime(Public_Param.order.returnCarDate)));System.out.println("e");
		b_return_time.setText(TimeHelper.getWeekTime(TimeHelper.getTimemis_to_StringTime(Public_Param.order.returnCarDate)));System.out.println("f");

		
		if(getIntent().getStringExtra("way").equals("order")){
			
			c_address.setText(Public_Param.order.takeCarStore.storeName+"("+Public_Param.order.takeCarStore.detailAddress+")");System.out.println("7");
			if(Public_Param.order.returnCarStore != null){
				c_r_address.setText(StringHelper.Null2Empty(Public_Param.order.returnCarStore.storeName)+"("+StringHelper.Null2Empty(Public_Param.order.returnCarStore.detailAddress)+")");System.out.println("12");
			}
		}else{
			c_address.setText(Public_Param.order.takeCarAddress);
			c_r_address.setText(Public_Param.order.returnCarAddress);System.out.println("12");
		}
		
		d_service_detail.setText("����"+Public_Param.order.averagePrice+"Ԫ/��,��"+Public_Param.order.tenancyDays+"��");
		d_service_all.setText("��"+Public_Param.order.rentalAmount);
		d_base_detail.setText(Public_Param.order.basicInsuranceAmount+"Ԫ/��,��"+Public_Param.order.tenancyDays+"��");
		d_base_all.setText("��"+Public_Param.order.basicInsuranceAmount*Public_Param.order.tenancyDays);
		
		if(Public_Param.order.totalTimeoutPrice.intValue() == 0){
			d_other_lin.setVisibility(View.GONE);
			d_other_detail.setText("����"+Public_Param.order.timeoutPrice.toString()+"Ԫ/ʱ");
			d_other_all.setText("��0");
		}else{
			d_other_detail.setText("����"+Public_Param.order.timeoutPrice.toString()+"Ԫ/ʱ,��"+Public_Param.order.totalTimeoutPrice.intValue()/Public_Param.order.timeoutPrice.intValue()+"Сʱ");
			d_other_all.setText("��"+Public_Param.order.totalTimeoutPrice.intValue());
		}
		
		if(Public_Param.order.orderValueAddedServiceRelativeShow != null){//�����ѺͲ�������
			d_listview_lin.setVisibility(View.VISIBLE);
			ServiceAmountList_Adapter adapter = new ServiceAmountList_Adapter(Activity_Order_Detail.this, Public_Param.order.orderValueAddedServiceRelativeShow,Public_Param.order.tenancyDays);
			listview.setAdapter(adapter);	
		}
		
		/*�����Լ�*/
		if(Public_Param.order.toStoreReduce != null){
			d_storereduce_lin.setVisibility(View.VISIBLE);
			d_storereduce.setText("����"+Public_Param.order.toStoreReduce+"Ԫ");
			d_storereduce_all.setText("��-"+Public_Param.order.toStoreReduce);
		}
		
		/*�����Żݻ*/
		if(Public_Param.order.activityId.intValue() != 0){
			d_activity_lin.setVisibility(View.VISIBLE);
			d_activity_name.setText("�Żݻ");
			d_activity_detail.setText(Public_Param.order.activityShow.activityDescription);
			d_activity_all.setText("��-"+StringHelper.getMoney(Public_Param.order.reduce));
		}else{
			
			if(Public_Param.order.couponNumber != null && !Public_Param.order.couponNumber.equals("null") && !Public_Param.order.couponNumber.equals("")){
				d_activity_lin.setVisibility(View.VISIBLE);
				d_activity_name.setText("�Ż�ȯ");
				d_activity_detail.setText(Public_Param.order.couponShowForAdmin.title);
				d_activity_all.setText("��-"+Public_Param.order.couponShowForAdmin.amount);
			}
		}
		
		d_all_all.setText("��"+StringHelper.getMoney(Public_Param.order.payAmount));//�ܷ���

		/*�Ѿ�ȡ������������ͬ������ȡ��*/
		
		if(!getIntent().getStringExtra("way").equals("order")){
		
			if(Public_Param.order.hasContract.intValue() == 1){//�����˺�ͬ
				cancle.setVisibility(View.GONE);
			}else{
				if(Public_Param.order.orderState.intValue() == 0 || Public_Param.order.orderState.intValue() == 1 || Public_Param.order.orderState.intValue() == 2 || Public_Param.order.orderState.intValue() == 3){
					cancle.setVisibility(View.VISIBLE);
				}else{
					cancle.setVisibility(View.GONE);
				}
			}
		}else{
			
			if(Public_Param.order.hasContract.intValue() == 1){//�����˺�ͬ
				cancle.setVisibility(View.GONE);
			}else{
				if(Public_Param.order.orderState.intValue() == 0 || Public_Param.order.orderState.intValue() == 1){
					cancle.setVisibility(View.VISIBLE);
				}else{
					cancle.setVisibility(View.GONE);
				}
			}
			
		}
			
	}
	
	public void onClick(View view){

		switch (view.getId()) {
		
			case R.id.cancle:
				
				final CustomDialog.Builder ibuilder;
				ibuilder = new CustomDialog.Builder(Activity_Order_Detail.this);
				ibuilder.setTitle("��ʾ");
				ibuilder.setMessage("��ȷ��Ҫȡ��������");
				ibuilder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						
						SubmitDialog.showSubmitDialog(Activity_Order_Detail.this);
						
						String api = "api/door/clientcanle/"+Public_Param.order.orderId+"/order";
					
						new HttpHelper().initData(HttpHelper.Method_Delete, Activity_Order_Detail.this, api, null, null, handler, Cancle_Order, 2, null);		
						
						if(getIntent().getStringExtra("way").equals("order")){
							
							api = "api/user/order/"+Public_Param.order.orderId+"/cancelOrder";
							new HttpHelper().initData(HttpHelper.Method_Put, Activity_Order_Detail.this, api, null, null, handler, Cancle_Order, 2, null);		
							
						}
						
					}
				});
				ibuilder.setNegativeButton("ȡ��", null);
				
				ibuilder.create().show();
						
				break;

			case R.id.pay_ok:
				new AlipayHelper().pay(this, handler, "�Ͻ��⳵", getIntent().getStringExtra("model"), Public_Param.order.payAmount.toString(),Public_Param.order.orderId.toString());
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

					case AlipayHelper.Pay_Ok:
						System.out.println("֧���ɹ�");
						ToastHelper.showToastShort(Activity_Order_Detail.this, "֧���ɹ�");
						pay_money_lin.setVisibility(View.GONE);
						pay_state.setText("����״̬:���µ�");
						break;
					
					case Request_Data:
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
							
							LoadAnimateHelper.load_success_animation();	
							
							isRequestPriceOk = true;
							Public_Param.order = (Order)msg.obj;
							
							/*��ȡ����*/
							
							if(Public_Param.order.tenancyDays == null){
								
							}else{
								init(); 
							}
							
							
				           	return;
						}else{
							
							LoadAnimateHelper.load_fail_animation();
						}					
						break;
					
					case Request_User:
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){

							User user = (User)msg.obj;
							if(user.credentialNumber != null && !user.credentialNumber.equals("null")){
								
								pay_card.setText(user.credentialNumber);							
							}
						}
						
						break;	
						
					case Click:
						Request_AmountDetail();						
						break;
						
					case Cancle_Order:
						SubmitDialog.closeSubmitDialog();
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
							ToastHelper.showToastShort(Activity_Order_Detail.this, "ȡ�������ɹ�");
							finish();
							
				           	return;
						}	
						ToastHelper.showToastShort(Activity_Order_Detail.this, "ȡ������ʧ��");
						break;
						
					default:
						break;
				}
			}
		};
	}


}

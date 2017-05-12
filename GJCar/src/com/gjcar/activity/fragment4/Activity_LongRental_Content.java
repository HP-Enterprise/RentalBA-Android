package com.gjcar.activity.fragment4;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gjcar.activity.fragment1.Activity_Car_List;
import com.gjcar.activity.fragment3.Activity_FreeRide_Order_Submit;
import com.gjcar.activity.user.ValidationHelper;
import com.gjcar.annotation.ContentView;
import com.gjcar.annotation.ContentWidget;
import com.gjcar.app.R;
import com.gjcar.data.bean.BrandModelShow;
import com.gjcar.data.bean.CityShow;
import com.gjcar.data.bean.FreeRide;
import com.gjcar.data.bean.VehicleBrandShow;
import com.gjcar.data.data.Public_Param;
import com.gjcar.data.data.Public_Platform;
import com.gjcar.utils.AnnotationViewUtils;
import com.gjcar.utils.HandlerHelper;
import com.gjcar.utils.HttpHelper;
import com.gjcar.utils.IntentHelper;
import com.gjcar.utils.NetworkHelper;
import com.gjcar.utils.SharedPreferenceHelper;
import com.gjcar.utils.TimeHelper;
import com.gjcar.utils.ToastHelper;
import com.gjcar.utils.ValidateHelper;
import com.gjcar.view.dialog.DateTimePickerHelper;
import com.gjcar.view.dialog.SelectDailog;
import com.gjcar.view.dialog.SubmitDialog;
import com.gjcar.view.helper.EditTextHelper;
import com.gjcar.view.helper.TitleBarHelper;
import com.gjcar.view.helper.ViewInitHelper;

@ContentView(R.layout.activity_longrental_content)
public class Activity_LongRental_Content extends Activity{

	/*信息展示*/
	@ContentWidget(id = R.id.take_city) TextView take_city;
	@ContentWidget(id = R.id.take_time) TextView take_time;
	
	@ContentWidget(id = R.id.take_days) TextView take_days;
	@ContentWidget(id = R.id.count) TextView count;
	@ContentWidget(id = R.id.brand) TextView brand;
	@ContentWidget(id = R.id.car) TextView car;
	
	/*信息填写*/
	@ContentWidget(id = R.id.company_delete) ImageView company_delete;
	@ContentWidget(id = R.id.company) EditText company;
	@ContentWidget(id = R.id.person_delete) ImageView person_delete;
	@ContentWidget(id = R.id.person) EditText person;
	@ContentWidget(id = R.id.phone_delete) ImageView phone_delete;
	@ContentWidget(id = R.id.phone) EditText phone;
	@ContentWidget(id = R.id.email_delete) ImageView email_delete;	
	@ContentWidget(id = R.id.email) EditText email;
	
	/*提交*/
	@ContentWidget(click = "onClick") Button submit;
	
	/*Handler*/
	private Handler handler;	
	private final static int Request_Submit = 1;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		AnnotationViewUtils.injectObject(this, this);
		
		/*handler*/
		initHandler();
		
		/*加载标题*/
		TitleBarHelper.Back(this, "申请内容", 0);

		/*初始化*/
		initParms();
		
		/*初始化视图*/
		initView();
		
	}

	private void initView() {
		new EditTextHelper().setEditText_Clear(company, company_delete);
		new EditTextHelper().setEditText_Clear(person, person_delete);
		new EditTextHelper().setEditText_Clear(phone, phone_delete);
		new EditTextHelper().setEditText_Clear(email, email_delete);
	}

	private void initParms() {
				
		ViewInitHelper.initText_Extra(this, new TextView[]{take_city,take_time,take_days,count,brand,car}, new String[]{"take_city","take_time","take_days","count","brand","car"});
	}
	
	public void onClick(View view){
		
		switch (view.getId()) {
		
			case R.id.submit:
				/*判断是否有网*/
				if(!NetworkHelper.isNetworkAvailable(this)){
					return;
				}
			
				/*判断输入是否正确*/
				if(!ValidateHelper.Validate(this, new boolean[]{!ValidationHelper.isNull("", company).equals(""),!ValidationHelper.isNull("", person).equals(""),!ValidationHelper.isNull("", phone).equals(""),
						!ValidationHelper.IsChineseName(person.getText().toString()),!ValidationHelper.format("phone", phone).equals(""),ValidationHelper.isNull("", email).equals("") && !ValidationHelper.format("email", email).equals("")}, 
						new String[]{"企业/个人名称必须填写","联系人必须填写","联系电话必须填写", "联系人填写不正确", "联系电话格式不正确", "邮箱格式不对"})){
					return;
				}
				//"take_city","take_time","take_days","count","brand","car","cityId","brandId","carId"
				System.out.println("cityId"+getIntent().getIntExtra("cityId", 0));
				System.out.println("brandId"+getIntent().getIntExtra("brandId", 0));
				System.out.println("carId"+getIntent().getIntExtra("carId", 0));

				/*弹出提交对话框*/
				SubmitDialog.showSubmitDialog(this);
				
				Request_Submit();
				
				break;
				
			default:
				break;
		}
	}
	
	/** 提交信息  */
	private void  Request_Submit(){

		/*参数*/
		JSONObject jsonObject = new JSONObject(); //**********************注意json发送数据时，要这样
//		carNumber:"999"
//			customerName:"马云"
//			email:"10001@qq.com"
//			phone:"13888888888"
//			rentalTime:"90天-120天"
//			requirement:"阿里巴巴"
//			useCarCityId:"3"
//			useCarDate:"2016-07-07"
//			vehicleBrandId:"6"
//			vehicleModelId:"3"
				//"take_city","take_time","take_days","count","brand","car","cityId","brandId","carId"		
			
		jsonObject.put("carNumber", getIntent().getStringExtra("count"));System.out.println("carNumber"+getIntent().getStringExtra("count"));
		jsonObject.put("customerName", person.getText().toString());	System.out.println("customerName"+person.getText().toString());		
		jsonObject.put("email", email.getText().toString()+"");System.out.println("email"+email.getText().toString());
		jsonObject.put("phone", phone.getText().toString());System.out.println("phone"+phone.getText().toString());
		jsonObject.put("rentalTime", take_days.getText().toString());System.out.println("rentalTime"+take_days.getText().toString());
		
		jsonObject.put("requirement", company.getText().toString());System.out.println("requirement"+company.getText().toString());
		jsonObject.put("useCarCityId", getIntent().getStringExtra("cityId"));System.out.println("useCarCityId"+getIntent().getStringExtra("useCarCityId"));
		jsonObject.put("useCarDate", getIntent().getStringExtra("take_time"));System.out.println("useCarDate"+getIntent().getStringExtra("useCarDate"));
		jsonObject.put("vehicleBrandId", getIntent().getStringExtra("brandId"));System.out.println("vehicleBrandId"+getIntent().getStringExtra("brandId"));
		jsonObject.put("vehicleModelId", getIntent().getStringExtra("carId"));System.out.println("vehicleModelId"+getIntent().getStringExtra("carId"));

		jsonObject.put("source",Public_Platform.P_Android);
		/*提交*/
		new HttpHelper().initData(HttpHelper.Method_Post, this, "api/longRentalAsk", jsonObject, null, handler, Request_Submit, 2, null);
		
	}	
	
	private void initHandler() {

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				switch (msg.what) {
								
					case Request_Submit:
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
							SubmitDialog.closeSubmitDialog();
							finish();
							ToastHelper.showToastShort(Activity_LongRental_Content.this,"申请成功");
				           	return;
						}
						SubmitDialog.closeSubmitDialog();
						ToastHelper.showToastShort(Activity_LongRental_Content.this,"申请失败，请点击重新提交");
						break;
						
					default:
						break;
				}
			}
		};
	}
}

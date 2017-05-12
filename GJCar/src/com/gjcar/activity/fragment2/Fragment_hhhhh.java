package com.gjcar.activity.fragment2;

import com.alibaba.fastjson.JSONObject;
import com.gjcar.activity.fragment1.Activity_Area;
import com.gjcar.activity.fragment1.Activity_City_List;
import com.gjcar.activity.fragment1.Activity_Map_Area;
import com.gjcar.activity.fragment4.Activity_LongRental_Content;
import com.gjcar.activity.user.ValidationHelper;
import com.gjcar.annotation.ContentWidget;
import com.gjcar.app.R;
import com.gjcar.data.bean.CityShow;
import com.gjcar.data.data.Public_Param;
import com.gjcar.data.data.Public_Platform;
import com.gjcar.data.data.Public_SP;
import com.gjcar.utils.AnnotationViewFUtils;
import com.gjcar.utils.HandlerHelper;
import com.gjcar.utils.HttpHelper;
import com.gjcar.utils.IntentHelper;
import com.gjcar.utils.NetworkHelper;
import com.gjcar.utils.SharedPreferenceHelper;
import com.gjcar.utils.SysoutHelper;
import com.gjcar.utils.TimeHelper;
import com.gjcar.utils.ToastHelper;
import com.gjcar.utils.ValidateHelper;
import com.gjcar.view.dialog.DateTimePickerHelper;
import com.gjcar.view.dialog.SelectDailog;
import com.gjcar.view.dialog.SubmitDialog;
import com.gjcar.view.dialog.TimePickerHelper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceActivity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Fragment_hhhhh extends Fragment{

	/*控件*/
	@ContentWidget(id = R.id.image) ImageView image;//上面图片
	
	@ContentWidget(click = "onClick") LinearLayout take_city_lin;//城市
	@ContentWidget(id = R.id.take_city) TextView take_city;	
	@ContentWidget(id = R.id.company) EditText company;//航空公司
	@ContentWidget(id = R.id.airport) EditText airport;
	@ContentWidget(id = R.id.number) EditText number;
	@ContentWidget(click = "onClick") LinearLayout take_date_lin;
	@ContentWidget(id = R.id.take_date) TextView take_date;
	@ContentWidget(click = "onClick") LinearLayout take_time_lin;
	@ContentWidget(id = R.id.take_time) TextView take_time;
	@ContentWidget(click = "onClick") EditText address;
	
	@ContentWidget(click = "onClick") Button ok;
	
	/*Handler*/
	private Handler handler;
	
	/*其它*/
	private final static int RequestCode_Take = 1;
	private final static int RequestCode_Address = 2;
	private final static int Request_Submit = 3;
	
	private int getCarCityId = -1;
	private double take_latitude = 30.279311;
	private double take_longitude = 120.168592;
	private String takeCarAddress = "";//地址
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment2_fffff, null);SysoutHelper.in(SysoutHelper.Fragment_hhhhh, SysoutHelper.Fragment_hhhhh_flag);
		AnnotationViewFUtils.injectObject(this, getActivity(), view);
		
		initHandler();
		
		return view;                                                                                                                                                                                                                                                                                                        
	}
	
	public void onClick(View view){
		
		/*判断网络*/
		if(!NetworkHelper.isNetworkAvailable(getActivity())){return;}
		
		switch (view.getId()) {
		
			case R.id.take_city_lin://"id","cityName","latitude","longitude"				
				IntentHelper.Fragment_startActivityForResult_Extra(getActivity(), this, Activity_City_List.class,RequestCode_Take, new String[]{"loc_cityId","loc_cityName","loc_latitude","loc_longitude"}, new Object[]{SharedPreferenceHelper.getInt(getActivity(), Public_SP.City, "id", -1), SharedPreferenceHelper.getString(getActivity(), Public_SP.City, "cityName"),(double)SharedPreferenceHelper.getFloat(getActivity(), Public_SP.City, "latitude"),(double)SharedPreferenceHelper.getFloat(getActivity(), Public_SP.City, "longitude")}, new int[]{IntentHelper.Type_Int, IntentHelper.Type_String,IntentHelper.Type_Double,IntentHelper.Type_Double});				
				break;
				
			case R.id.take_date_lin:
				
				if(take_date.getText().toString().equals("请输入接机日期")){
					new DateTimePickerHelper().pickTime(getActivity(), take_date, TimeHelper.getNowTime_YMD(), "选择接机日期");
				}else{
					new DateTimePickerHelper().pickTime(getActivity(), take_date, take_date.getText().toString(), "选择接机日期");
				}					
				break;
				
			case R.id.take_time_lin:
				
				if(take_time.getText().toString().equals("请输入接机时间")){
					new TimePickerHelper().pickTime(getActivity(), take_time, TimeHelper.getNowTime_Hm(), "选择接机时间");
				}else{
					new TimePickerHelper().pickTime(getActivity(), take_time, take_time.getText().toString(), "选择接机时间");
				}				
				break;
			
			case R.id.address:
				if(take_city.getText().toString().trim().equals("请选择用车城市")){
				
					ToastHelper.showToastShort(getActivity(), "请先选择城市");
					return;					
				}
				
				IntentHelper.Fragment_startActivityForResult_Extra(getActivity(), this, Activity_Area.class,RequestCode_Address, new String[]{"cityName"}, new Object[]{"武汉"}, new int[]{IntentHelper.Type_String});
				break;
				
			case R.id.ok:
				
				/*判断输入是否正确*/
				if(!ValidateHelper.Validate(getActivity(), new boolean[]{
					take_city.getText().toString().equals("请选择用车城市"),ValidationHelper.isNull(company),ValidationHelper.isNull(airport),ValidationHelper.isNull(number),ValidationHelper.isNull(address),
					take_date.getText().toString().equals("请输入接机日期"),take_time.getText().toString().equals("请输入接机时间"),!TimeHelper.isLate(take_date.getText().toString()+" "+take_time.getText().toString())}, 
						new String[]{"请选择用车城市","航空公司必须填写","机场必须填写","航班号必须填写", "送达地点必须填写",
									  "接机日期必须填写","接机时间必须填写","接机时间必须晚于当前时间"})){
					return;
				}
				System.out.println("验证完毕");
				
				/*参数*/
				Public_Param.transfer_params.city = take_city.getText().toString();
				Public_Param.transfer_params.company = company.getText().toString();
				Public_Param.transfer_params.airport = airport.getText().toString();
				Public_Param.transfer_params.number = number.getText().toString();
				Public_Param.transfer_params.time = take_date.getText().toString()+" "+take_time.getText().toString();
				
				/*提交*/				
				SubmitDialog.showSubmitDialog(getActivity());
				Request_Submit();
				
				break;
				
			default:
				break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(data == null){//按back或返回键退出
			
			return;
		}
		
		switch (requestCode) {
				
			case RequestCode_Take:
				if(!data.hasExtra("cityShow")){
					return;
				}
				CityShow show = (CityShow) data.getExtras().get("cityShow");
				
				getCarCityId = show.id.intValue();
				take_city.setText(show.cityName);
				break;
			
			case RequestCode_Address:
				
				takeCarAddress = ""+data.getCharSequenceExtra("Address");
				take_latitude = data.getDoubleExtra("latitude", 0.0);
				take_longitude = data.getDoubleExtra("longitude", 0.0);
				address.setText(""+data.getCharSequenceExtra("Address"));
				break;
				
			default:
				break;
		}
						
	}

	/** 提交信息  */
	private void  Request_Submit(){

		/*参数*/
		JSONObject jsonObject = new JSONObject();
		
		jsonObject.put("airDescribe", "订单描述内容");
		jsonObject.put("airlineCompany", company.getText().toString());
		jsonObject.put("airportId", 5);
		jsonObject.put("belongUnit", "武汉市英卡科技");
		jsonObject.put("brandId", "1");
		
		jsonObject.put("costDetailShow", "[]");
		jsonObject.put("flightNumber", "MH370");
		jsonObject.put("latitude", "31.224247");
		jsonObject.put("longitude", "121.467873");
		jsonObject.put("modelId", 114);
		
		jsonObject.put("orderType", 5);
		jsonObject.put("passengerName", "张三丰");
		jsonObject.put("passengerPhone", "13397163438");
		jsonObject.put("payAmount", 100);
		jsonObject.put("rentalAmount", 100);
	
		jsonObject.put("source", Public_Platform.P_Android);
		jsonObject.put("takeCarCity", "上海");
		jsonObject.put("takeCarCityId", 73);
		jsonObject.put("takeCarDate", "1480469723000");
		jsonObject.put("tripAddress", "上海滩");

		jsonObject.put("tripDistance", 120);
		jsonObject.put("tripRemark", "行程备注");
		jsonObject.put("tripType", 1);
		jsonObject.put("userId", 0);
		jsonObject.put("vendorId", 1);
		
		SysoutHelper.parma_Json(jsonObject, SysoutHelper.Fragment_hhhhh_flag);
		
		/*提交*/
		new HttpHelper().initData(HttpHelper.Method_Post, getActivity(), "api/airportTripOrder", jsonObject, null, handler, Request_Submit, 2, null);
		
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
							ToastHelper.showToastShort(getActivity(),"申请成功");
				           	return;
						}
						SubmitDialog.closeSubmitDialog();
						ToastHelper.showToastShort(getActivity(),"申请失败，请点击重新提交");
						break;
						
					default:
						break;
				}
			}
		};
	}
		
}

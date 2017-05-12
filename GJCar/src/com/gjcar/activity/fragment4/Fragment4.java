package com.gjcar.activity.fragment4;

import java.util.ArrayList;

import com.alibaba.fastjson.TypeReference;
import com.baidu.mapapi.map.MapView;
import com.gjcar.activity.fragment1.Activity_Car_List;
import com.gjcar.activity.fragment1.Activity_City_List;
import com.gjcar.activity.fragment1.Activity_Map_Area;
import com.gjcar.activity.fragment1.WebActivity;
import com.gjcar.annotation.ContentWidget;
import com.gjcar.app.R;
import com.gjcar.data.bean.BrandModelShow;
import com.gjcar.data.bean.CityShow;
import com.gjcar.data.bean.FreeRide;
import com.gjcar.data.bean.TradeAreaShow;
import com.gjcar.data.bean.VehicleBrandShow;
import com.gjcar.data.bean.VehicleModelShows;
import com.gjcar.data.data.Public_SP;
import com.gjcar.framwork.BaiduMapHelper;
import com.gjcar.utils.AnnotationViewFUtils;
import com.gjcar.utils.HandlerHelper;
import com.gjcar.utils.HttpHelper;
import com.gjcar.utils.IntentHelper;
import com.gjcar.utils.SharedPreferenceHelper;
import com.gjcar.utils.TimeHelper;
import com.gjcar.utils.ToastHelper;
import com.gjcar.utils.ValidateHelper;
import com.gjcar.view.dialog.DateTimePickerDialog;
import com.gjcar.view.dialog.DateTimePickerHelper;
import com.gjcar.view.dialog.SelectDailog;
import com.gjcar.view.helper.LoadAnimateHelper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Fragment4 extends Fragment{
	
	@ContentWidget(click = "onClick") ImageView image;
	
	@ContentWidget(id = R.id.take_city) TextView take_city;
	@ContentWidget(id = R.id.take_time) TextView take_time;
	
	@ContentWidget(id = R.id.take_days) TextView take_days;
	@ContentWidget(id = R.id.count) EditText count;
	@ContentWidget(id = R.id.brand) TextView brand;
	@ContentWidget(id = R.id.car) TextView car;
	
	@ContentWidget(click = "onClick") LinearLayout take_city_lin,take_time_lin, rental_lin, model_lin, car_lin;
	@ContentWidget(click = "onClick") Button submit;
	
	/*Handler*/
	private Handler handler;	
	private final static int Request_Brand = 1;
	private final static int Request_Car = 2;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment4, null);		
		AnnotationViewFUtils.injectObject(this, getActivity(), view);
		
		initView();
		
		initHandler();
				                                                                                                           		
		return view;
	}
	
	private void initView() {
		
		take_city.setTag(0);
		car.setTag(0);
		brand.setTag(0);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {	
		super.onActivityResult(requestCode, resultCode, data);
		
		if(data == null){//按back或返回键退出
			return;
		}
		
		CityShow show = (CityShow) data.getExtras().get("cityShow");
		
		take_city.setText(show.cityName);
		take_city.setTag(show.id.intValue());
	}
	
	public void onClick(View view){
		
		switch (view.getId()) {
		
			case R.id.image:
				
				IntentHelper.startActivity_StringExtras(getActivity(), WebActivity.class, new String[]{"title","fragment","url"}, new String[]{"活动详情","action_detail","http://www.b-car.cn/Pages/8.jsp"});
				break;
				
			case R.id.take_city_lin:
				IntentHelper.Fragment_startActivityForResult_Extra(getActivity(), this, Activity_City_List.class,100, new String[]{"loc_cityId","loc_cityName","loc_latitude","loc_longitude"}, new Object[]{SharedPreferenceHelper.getInt(getActivity(), Public_SP.City, "id", -1), SharedPreferenceHelper.getString(getActivity(), Public_SP.City, "cityName"),(double)SharedPreferenceHelper.getFloat(getActivity(), Public_SP.City, "latitude"),(double)SharedPreferenceHelper.getFloat(getActivity(), Public_SP.City, "longitude")}, new int[]{IntentHelper.Type_Int, IntentHelper.Type_String,IntentHelper.Type_Double,IntentHelper.Type_Double});								
				break;
				
			case R.id.take_time_lin:
				
				if(take_time.getText().toString().equals("请选择时间")){
					new DateTimePickerHelper().pickTime(getActivity(), take_time, TimeHelper.getNowTime_YMD(), "选择时间");
				}else{
					new DateTimePickerHelper().pickTime(getActivity(), take_time, take_time.getText().toString(), "选择时间");
				}	

				break;
	
			case R.id.rental_lin:
				SelectDailog.select(getActivity(), "选择租期", take_days, new String[]{"90天-120天","121天-180天","181天-360天","360天以上"},new int[]{1,1,1,1});
				break;
				
			case R.id.model_lin:
								
				Request_Brand();
				break;
				
			case R.id.car_lin:
				if(((Integer)brand.getTag()).intValue() == 0){
					ToastHelper.showToastShort(getActivity(), "请先选择品牌");
					return;
				}
				Request_Car(brand.getTag().toString());							
				break;
				
			case R.id.submit:

				if(!ValidateHelper.Validate(getActivity(), new boolean[]{((Integer)take_city.getTag()).intValue() == 0, take_time.getText().toString().equals("请选择时间"),take_days.getText().toString().equals("租期"),
						count.getText() == null || count.getText().toString().equals(""),((Integer)brand.getTag()).intValue() == 0,((Integer)car.getTag()).intValue() == 0},
						new String[]{"请选择城市","请选择时间","请选择租期","请输入数量","请选择品牌","请选择车型"})){
					return;
				}
				
				System.out.println("城市"+take_city.getTag());
				System.out.println("时间"+take_time.getText());
				System.out.println(""+count.getText());
				System.out.println(""+take_days.getText());
				System.out.println(""+brand.getTag());
				System.out.println(""+car.getTag());
				
				IntentHelper.startActivity_Extra(getActivity(),  Activity_LongRental_Content.class, 
						new String[]{"take_city","take_time","take_days","count","brand","car","cityId","brandId","carId"}, new Object[]{take_city.getText().toString(),take_time.getText().toString(),take_days.getText().toString(),new Integer(count.getText().toString()).toString(),brand.getText().toString(),car.getText().toString(),
									((Integer)take_city.getTag()).toString(),((Integer)brand.getTag()).toString(),((Integer)car.getTag()).toString()}, 
						new int[]{IntentHelper.Type_String,IntentHelper.Type_String,IntentHelper.Type_String,IntentHelper.Type_String,IntentHelper.Type_String,IntentHelper.Type_String,
									IntentHelper.Type_String,IntentHelper.Type_String,IntentHelper.Type_String});
				break;
				
			default:
				break;
		}
	}
	
	private void Request_Brand(){
		
		System.out.println("1");
		String api = "api/vehicle/brand/enterprise";
		new HttpHelper().initData(HttpHelper.Method_Get, getActivity(), api, null, null, handler, Request_Brand, 1, new TypeReference<ArrayList<VehicleBrandShow>>() {});

	}
	private void Request_Car(String id){
		
		System.out.println("brandId"+id);
		String api = "api/vehicle/model/enterprise?brandId="+id;
		new HttpHelper().initData(HttpHelper.Method_Get, getActivity(), api, null, null, handler, Request_Car, 1, new TypeReference<ArrayList<BrandModelShow>>() {});

	}
	private void initHandler() {

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				switch (msg.what) {

					case Request_Brand:
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){

							System.out.println("车型");	
							car.setText("车型");//清空车子
							car.setTag(0);
							System.out.println("车型t");	
							ArrayList<VehicleBrandShow> list_brand = (ArrayList<VehicleBrandShow>)msg.obj;
				           	System.out.println("size"+list_brand.size());	

				           	String[] brands = new String[list_brand.size()];
				           	for (int i = 0; i < brands.length; i++) {
				           		brands[i] = list_brand.get(i).brand;
							}
				            
				           	int[] ids = new int[list_brand.size()];
				           	for (int i = 0; i < brands.length; i++) {
				           		ids[i] = list_brand.get(i).id.intValue();
							}
				           	
				           	SelectDailog.select(getActivity(), "选择品牌", brand, brands,ids);
				           	
				           	//handler.sendEmptyMessage(CarList_Show);
				           	return;
						}
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Empty)){
							ToastHelper.showToastShort(getActivity(),"抱歉,"+brand.getText().toString()+"没有相关品牌");
							return;
						}
						
						ToastHelper.showToastShort(getActivity(),"加载失败，请点击重新加载");
						break;
	
					case Request_Car:
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){

							ArrayList<BrandModelShow> list_brand = (ArrayList<BrandModelShow>)msg.obj;
				           	System.out.println("size"+list_brand.size());	

				           	String[] cars = new String[list_brand.size()];
				           	for (int i = 0; i < cars.length; i++) {
				           		cars[i] = list_brand.get(i).series;
							}
				           		
				           	int[] ids = new int[list_brand.size()];
				           	for (int i = 0; i < ids.length; i++) {
				           		ids[i] = list_brand.get(i).id.intValue();
							}
				           	
				           	SelectDailog.select(getActivity(), "选择车型", car, cars,ids);
				           	
				           	return;
						}
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Empty)){
							ToastHelper.showToastShort(getActivity(),"抱歉,"+brand.getText().toString()+"没有相关车型");
							return;
						}
						
						ToastHelper.showToastShort(getActivity(),"加载失败，请点击重新加载");
						break;
						
					default:
						break;
				}
			}
		};
	}
}

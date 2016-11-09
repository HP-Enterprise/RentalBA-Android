package com.gjcar.activity.fragment3;

import java.util.ArrayList;

import com.alibaba.fastjson.TypeReference;
import com.baidu.mapapi.map.MapView;
import com.gjcar.activity.fragment1.Activity_City_List;
import com.gjcar.annotation.ContentWidget;
import com.gjcar.app.R;
import com.gjcar.data.adapter.FreeRide_List_Adapter;
import com.gjcar.data.bean.CityShow;
import com.gjcar.data.bean.FreeRide;
import com.gjcar.data.data.Public_Param;
import com.gjcar.data.data.Public_SP;
import com.gjcar.framwork.BaiduMapHelper;
import com.gjcar.utils.AnnotationViewFUtils;
import com.gjcar.utils.HandlerHelper;
import com.gjcar.utils.HttpHelper;
import com.gjcar.utils.IntentHelper;
import com.gjcar.utils.NetworkHelper;
import com.gjcar.utils.SharedPreferenceHelper;
import com.gjcar.utils.ToastHelper;
import com.gjcar.utils.ValidateHelper;
import com.gjcar.view.dialog.DateTimePickerDialog;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Fragment3 extends Fragment{
	
	/*控件*/
	@ContentWidget(id = R.id.image) ImageView image;
	
	@ContentWidget(id = R.id.take_city) TextView take_city;
	@ContentWidget(id = R.id.return_city) TextView return_city;

	@ContentWidget(click = "onClick") LinearLayout take_city_lin,return_city_lin;
	@ContentWidget(click = "onClick") Button submit;
	
	/*其它*/
	private final static int RequestCode_Take = 1;
	private final static int RequestCode_Return = 2;
	private int getCarCityId = -1;
	private String getCarCity = "";
	private int returnCityId = -1;
	
	/*Handler*/
	private Handler handler;
	private final static int Request_City = 1;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment3, null);		
		AnnotationViewFUtils.injectObject(this, getActivity(), view);

		initHandler();
		
		return view;
	}
	
	public void onClick(View view){
		
		switch (view.getId()) {
		
			case R.id.take_city_lin://"id","cityName","latitude","longitude"				
				IntentHelper.Fragment_startActivityForResult_Extra(getActivity(), this, Activity_City_List.class,RequestCode_Take, new String[]{"loc_cityId","loc_cityName","loc_latitude","loc_longitude"}, new Object[]{SharedPreferenceHelper.getInt(getActivity(), Public_SP.City, "id", -1), SharedPreferenceHelper.getString(getActivity(), Public_SP.City, "cityName"),(double)SharedPreferenceHelper.getFloat(getActivity(), Public_SP.City, "latitude"),(double)SharedPreferenceHelper.getFloat(getActivity(), Public_SP.City, "longitude")}, new int[]{IntentHelper.Type_Int, IntentHelper.Type_String,IntentHelper.Type_Double,IntentHelper.Type_Double});				
				break;
				
			case R.id.return_city_lin:
				if(getCarCityId == -1){
					ToastHelper.showToastShort(getActivity(), "请先选择取车城市");
					return;
				}
				
				new HttpHelper().initData(HttpHelper.Method_Get, getActivity(), "api/freeRide/returnCarCity?getCarCityId="+getCarCityId, null, null, handler, Request_City, 1, new TypeReference<ArrayList<CityShow>>() {});
				
				break;
				
			case R.id.submit:

				if(!NetworkHelper.isNetworkAvailable(getActivity())){
					return;
				}
				
				if(!ValidateHelper.Validate(getActivity(), new boolean[]{getCarCityId == -1, returnCityId == -1}, new String[]{"请选择取车城市","请选择还车城市"})){
					return;
				}
				//IntentHelper.startActivity(getActivity(), Activity_FreeRide_List.class);
				IntentHelper.startActivity_Extra(getActivity(), Activity_FreeRide_List.class, new String[]{"getCarCityId","returnCityId"}, new Object[]{getCarCityId,returnCityId}, new int[]{IntentHelper.Type_Int,IntentHelper.Type_Int});
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
				
				if(getCarCityId == show.id.intValue()){
					return;
				}
				
				getCarCity = show.cityName;
				getCarCityId = show.id.intValue();
				take_city.setText(show.cityName);
				
				returnCityId = -1;
				return_city.setText("请选择还车城市");
				break;
			
			case RequestCode_Return:
				if(!data.hasExtra("cityShow")){
					return;
				}
				CityShow show_return = (CityShow) data.getExtras().get("cityShow");
				
				if(returnCityId == show_return.id.intValue()){
					return;
				}		
				
				returnCityId = show_return.id.intValue();
				return_city.setText(show_return.cityName);
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

					case Request_City:
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){

							IntentHelper.Fragment_startActivityForResult_Extra(getActivity(), Fragment3.this, Activity_City_List.class,RequestCode_Return, new String[]{"loc_cityId","loc_cityName","loc_latitude","loc_longitude","cityId"}, new Object[]{SharedPreferenceHelper.getInt(getActivity(), Public_SP.City, "id", -1), SharedPreferenceHelper.getString(getActivity(), Public_SP.City, "cityName"),(double)SharedPreferenceHelper.getFloat(getActivity(), Public_SP.City, "latitude"),(double)SharedPreferenceHelper.getFloat(getActivity(), Public_SP.City, "longitude"),getCarCityId}, new int[]{IntentHelper.Type_Int, IntentHelper.Type_String,IntentHelper.Type_Double,IntentHelper.Type_Double,IntentHelper.Type_Int});				

				           	return;
						}
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Empty)){
							
				            ToastHelper.showToastShort(getActivity(), "对不起，没有从"+getCarCity+"出发的车辆");           
						}else{
							ToastHelper.showToastShort(getActivity(), "对不起，没有从"+getCarCity+"出发的车辆");   
						}						
						
						break;
						
					default:
						break;
				}
			}
		};
	}

}

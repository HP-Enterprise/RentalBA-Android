package com.gjcar.activity.fragment1;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.TypeReference;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mobstat.StatService;
import com.gjcar.annotation.ContentWidget;
import com.gjcar.app.R;
import com.gjcar.data.bean.CityPointBounds;
import com.gjcar.data.bean.CityShow;
import com.gjcar.data.bean.LocationMessage;
import com.gjcar.data.bean.Point;
import com.gjcar.data.bean.StoreShows;
import com.gjcar.data.bean.TradeAreaShow;
import com.gjcar.data.data.Public_Api;
import com.gjcar.data.data.Public_BaiduTJ;
import com.gjcar.data.data.Public_Data;
import com.gjcar.data.data.Public_Msg;
import com.gjcar.data.data.Public_Param;
import com.gjcar.data.data.Public_SP;
import com.gjcar.data.service.Fragment1_Helper;
import com.gjcar.framwork.BaiduMapHelper;
import com.gjcar.utils.AnnotationViewFUtils;
import com.gjcar.utils.HandlerHelper;
import com.gjcar.utils.HttpHelper;
import com.gjcar.utils.IntentHelper;
import com.gjcar.utils.ListenerHelper;
import com.gjcar.utils.NetworkHelper;
import com.gjcar.utils.SharedPreferenceHelper;
import com.gjcar.utils.TimeHelper;
import com.gjcar.utils.ToastHelper;
import com.gjcar.utils.ValidateHelper;
import com.gjcar.view.dialog.DateTimePickerDialog;
import com.gjcar.view.helper.LoadAnimateHelper;
import com.gjcar.view.helper.ViewInitHelper;
import com.gjcar.view.widget.CustomDialog;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public class Fragment1 extends Fragment  {
	
	/*控件*/
	@ContentWidget(click = "onClick") LinearLayout menu_out;/*头像*/
	@ContentWidget(id = R.id.mapView) MapView mapView;/*地图*/
		
	@ContentWidget(id = R.id.take_date) TextView take_date;/*时间选择*/
	@ContentWidget(id = R.id.take_time) TextView take_time;
	@ContentWidget(id = R.id.return_date) TextView return_date;
	@ContentWidget(id = R.id.return_time) TextView return_time;
	@ContentWidget(id = R.id.time_days) TextView time_days;
	
	@ContentWidget(id = R.id.take_city) TextView take_city;/*城市选择*/
	@ContentWidget(id = R.id.return_city) TextView return_city;
	@ContentWidget(id = R.id.take_doortodoor_lin) LinearLayout take_doortodoor_lin;
	@ContentWidget(id = R.id.take_ok) ToggleButton take_ok;	
	@ContentWidget(id = R.id.take_note) TextView take_note;
	@ContentWidget(id = R.id.take_address_name) TextView take_address_name;
	@ContentWidget(id = R.id.return_address_name) TextView return_address_name;
	
	@ContentWidget(click = "onClick") LinearLayout taketime_lin,returntime_lin,take_city_lin,return_city_lin;
	@ContentWidget(click = "onClick") ImageView iv_tomylocation;
	@ContentWidget(click = "onClick") EditText take_address, return_address;
	@ContentWidget(click = "onClick") Button ok;
	
	/*Handler*/
	private Handler handler;
	
	private final static int Location = 1;//定位
	private final static int ReverseGeoCode = 2;//地理编码
	private final static int FindCity = 3;//请求商店
	private final static int MoveListener = 4;//监听地图是否移动	
	private final static int ToggleChanged = 5;//toglgle开关
	private final static int Request_Store = 6;//请求商店
	
	private final static int RequestCode_takecity = 101;//请求取车城市
	private final static int RequestCode_takeaddress = 102;//
	private final static int RequestCode_takestore = 103;//
	private final static int RequestCode_returncity = 104;//
	private final static int RequestCode_returnaddress = 105;//
	private final static int RequestCode_returnstore = 106;//
	private final static int Request_Loc_Points = 107;//
	private final static int Request_Points = 108;//
	
	/*全局变量*/
	private int loc_cityId = -1;//城市信息
	private String loc_cityName = "";
	private LatLng loc_latlng = new LatLng(30.279311, 120.168592);
	private String loc_address;
	
	private int take_cityId = -1;
	private double take_latitude = 30.279311;
	private double take_longitude = 120.168592;
	private String take_cityName = "";
	private String takeCarAddress = "";
	private String takeCarAddress_Store = "";
	private String takeCarStoreId = "-1";
		
	private int return_cityId = -1;
	private double return_latitude = 30.279311;
	private double return_longitude = 120.168592;
	private String return_cityName = "";
	private String returnCarAddress = "";
	private String returnCarAddress_Store = "";
	private String returnCarStoreId = "-1";
	
	private boolean is_first_tomylocation = true;//地图定位
	private boolean is_tomylocation = false;
	
	private ArrayList<StoreShows> stores;//门店信息
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		System.out.println("F1_onResume---------xxxxxxxxxxxxxxxxxxxxxxx");
		View view = inflater.inflate(R.layout.fragment1, null);
		AnnotationViewFUtils.injectObject(this, getActivity(), view);
		NetworkHelper.isNetworkAvailable(getActivity());
		
		StatService.start(getActivity());
		
		/*清空定位信息*/
		SharedPreferenceHelper.clear(getActivity(), Public_SP.City);
		
		/*handler*/
		initHandler();
		
		/*监听器*/
		ListenerHelper.setListener(take_ok, ListenerHelper.Listener_ToggleButton_CheckedChanged, handler, ToggleChanged);
		
		/*时间选择*/
		ViewInitHelper.init_f1_DateTime(new TextView[]{take_date,take_time,return_date,return_time},"10:00");
		ViewInitHelper.init_take(8, 20);
		ViewInitHelper.init_return(8, 20);
		
		/*定位*/
		new BaiduMapHelper().startLocationClient(getActivity(), handler, Location);
		
		/*地图初始化*/
		new BaiduMapHelper().initBaiduMap(mapView);
		
		/*监听地图移动*/
		new BaiduMapHelper().moveListener(mapView.getMap(), handler, MoveListener);
				
		return view;
	}
	
	@Override
	public void onResume() {
		
		super.onResume();
		Public_BaiduTJ.pageStart(getActivity(), Public_BaiduTJ.Fragment_ShortRentalCar);	
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Public_BaiduTJ.pageEnd(getActivity(), Public_BaiduTJ.Fragment_ShortRentalCar);	
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("F1_onActivityResult---------xxxxxxxxxxxxxxxxxxxxxxx");
		if(data == null){//按back或返回键退出
			return;
		}
		
		switch (requestCode) {
		
			case RequestCode_takecity:
				if(!data.hasExtra("cityShow")){
					return;
				}
				CityShow show = (CityShow) data.getExtras().get("cityShow");
				
				if(resultCode == 101){
					show = new CityShow(loc_cityId, loc_cityName,loc_latlng.latitude, loc_latlng.longitude);
				}
					
				if(take_cityId == show.id.intValue()){
					return;
				}
				
				take_city.setText(show.cityName+"");
				return_city.setText(show.cityName+"");
				take_cityId = show.id;
				take_cityName = show.cityName;
				return_cityId = show.id.intValue();
				return_cityName = show.cityName;
				
				if(show.latitude != null){
					
					take_latitude = show.latitude;
					take_longitude = show.longitude;
					new BaiduMapHelper().ShowMap(new LatLng(take_latitude, take_longitude), mapView.getMap());//地图显示取车城市
					return_latitude = show.latitude;
					return_longitude = show.longitude;
				}

				takeCarStoreId = "-1";
				takeCarAddress = "";
				takeCarAddress_Store = "";
				returnCarStoreId = "-1";
				returnCarAddress = "";
				returnCarAddress_Store = "";

				/*搜索门到门服务范围*/
				new HttpHelper().initData(HttpHelper.Method_Get, getActivity(), "api/serviceCity/view?cityId="+show.id.intValue(), null, null, handler, Request_Loc_Points, 1, new TypeReference<CityPointBounds>() {});								
	
//				if(take_ok.isChecked()){
//					
//					/*搜索门到门服务范围*/
//					new HttpHelper().initData(HttpHelper.Method_Get, getActivity(), "api/serviceCity/view?cityId="+take_cityId, null, null, handler, Request_Points, 1, new TypeReference<CityPointBounds>() {});								
//					
//					take_address.setText("请选择地址");
//					return_address.setText("请选择地址");
//					
//				}else{
////					take_time.setText("");
////					return_time.setText("");
//					
//					take_address.setText("请选择门店");
//					return_address.setText("请选择门店");
//					
//					Request_Store();
//				}
				
				break;
			
			case RequestCode_returncity:
				if(!data.hasExtra("cityShow")){
					return;
				}
				CityShow show_return = (CityShow) data.getExtras().get("cityShow");
				
				if(resultCode == 101){
					show = new CityShow(loc_cityId, loc_cityName,loc_latlng.latitude, loc_latlng.longitude);
				}
				
				if(return_cityId == show_return.id.intValue()){
					return;
				}
				
				return_city.setText(show_return.cityName);
				return_cityId = show_return.id;
				return_cityName = show_return.cityName;
				
				return_latitude = show_return.latitude;
				return_longitude = show_return.longitude;
				new BaiduMapHelper().ShowMap(new LatLng(return_latitude, return_longitude), mapView.getMap());//地图显示还城市
				
				returnCarStoreId = "-1";
				returnCarAddress = "";
				returnCarAddress_Store = "";
						
				if(take_ok.isChecked()){
					
					/*搜索门到门服务范围*/
					if(return_cityId != take_cityId){
						new HttpHelper().initData(HttpHelper.Method_Get, getActivity(), "api/serviceCity/view?cityId="+return_cityId, null, null, handler, Request_Points, 1, new TypeReference<CityPointBounds>() {});														
					}
					
					return_address.setText("请选择地址");
					
				}else{
					
//					return_time.setText("");
					return_address.setText("请选择门店");
				}
				
				break;
		
			case RequestCode_takestore://"Id","Name","Address"
				System.out.println(""+data.getCharSequenceExtra("Name"));	
				
				takeCarStoreId = ""+data.getCharSequenceExtra("Id");
				takeCarAddress = ""+data.getCharSequenceExtra("Name");	
				takeCarAddress_Store = ""+data.getCharSequenceExtra("Address");	
				take_address.setText(""+data.getCharSequenceExtra("Name"));
				
				returnCarStoreId = ""+data.getCharSequenceExtra("Id");
				returnCarAddress = ""+data.getCharSequenceExtra("Name");
				returnCarAddress_Store = ""+data.getCharSequenceExtra("Address");	
				return_address.setText(""+data.getCharSequenceExtra("Name"));
								
				return_cityId = take_cityId;
				return_cityName = take_cityName;
				return_city.setText(take_cityName);

				ViewInitHelper.init_f1_ChangeDateTime(new TextView[]{take_date,take_time,return_date,return_time},""+data.getCharSequenceExtra("StartTime"),""+data.getCharSequenceExtra("EndTime"));		
				ViewInitHelper.init_take(TimeHelper.getTime2_Number(""+data.getCharSequenceExtra("StartTime")), TimeHelper.getTime2_Number(""+data.getCharSequenceExtra("EndTime")));
				ViewInitHelper.init_return(TimeHelper.getTime2_Number(""+data.getCharSequenceExtra("StartTime")), TimeHelper.getTime2_Number(""+data.getCharSequenceExtra("EndTime")));
				break;
				
			case RequestCode_returnstore://"Id","Name","Address"
				System.out.println(""+data.getCharSequenceExtra("Name"));	
				returnCarStoreId = ""+data.getCharSequenceExtra("Id");
				return_address.setText(""+data.getCharSequenceExtra("Name"));
				returnCarAddress = ""+data.getCharSequenceExtra("Name");
				returnCarAddress_Store = ""+data.getCharSequenceExtra("Address");
				
				ViewInitHelper.init_f1_DateTime_ChangeReturn(new TextView[]{return_date,return_time},""+data.getCharSequenceExtra("StartTime"),""+data.getCharSequenceExtra("EndTime"));						
				ViewInitHelper.init_return(TimeHelper.getTime2_Number(""+data.getCharSequenceExtra("StartTime")), TimeHelper.getTime2_Number(""+data.getCharSequenceExtra("EndTime")));
				
				break;
				
			case RequestCode_takeaddress://"Address","latitude","longitude"
				System.out.println(""+data.getCharSequenceExtra("Address"));
				takeCarStoreId = "-1";				
				takeCarAddress = ""+data.getCharSequenceExtra("Address");
				take_latitude = data.getDoubleExtra("latitude", 0.0);
				take_longitude = data.getDoubleExtra("longitude", 0.0);
				take_address.setText(""+data.getCharSequenceExtra("Address"));
				System.out.println("精度"+take_latitude);
				System.out.println("纬度"+take_longitude);
//				returnCarStoreId = "-1";			
//				returnCarAddress = ""+data.getCharSequenceExtra("Address");
//				return_latitude = data.getDoubleExtra("latitude", 0.0);
//				return_longitude = data.getDoubleExtra("longitude", 0.0);
//				return_address.setText(""+data.getCharSequenceExtra("Address"));
				
				break;
				
			case RequestCode_returnaddress://"Address","latitude","longitude"
				
				System.out.println(""+data.getCharSequenceExtra("Address"));
				returnCarStoreId = "-1";	
				returnCarAddress = ""+data.getCharSequenceExtra("Address");					
				return_latitude = data.getDoubleExtra("latitude", 0.0);
				return_longitude = data.getDoubleExtra("longitude", 0.0);
				return_address.setText(""+data.getCharSequenceExtra("Address"));				
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

					case Location:
						if(msg.getData().getString("message").equals("ok")){
								
							LocationMessage locMessage= (LocationMessage)msg.obj;
							loc_latlng = new LatLng(locMessage.Latitude, locMessage.Longitude);
							loc_address = locMessage.address;
							new BaiduMapHelper().ShowMap(loc_latlng, mapView.getMap());
							new BaiduMapHelper().ShowLocation(loc_latlng, mapView.getMap());//显示定位
							new BaiduMapHelper().startGeoCoder(loc_latlng, handler, ReverseGeoCode);
						}else{
							//xx定位失败
							ToastHelper.showToastShort(getActivity(), "抱歉,无法定位到当前城市");
							take_ok.setChecked(false);
						}
						break;
	
					case ReverseGeoCode:
						if(msg.getData().getString("message").equals("ok")){
							
							System.out.println("城市名称2："+msg.getData().getString("data"));
							
							//take_ok.setChecked(true);//这个地方有可能是危险代码
							
							/*查询城市id*/
							new Fragment1_Helper().searchCity(msg.getData().getString("data"), handler, FindCity);
						}else{
							//地理编码失败
							System.out.println("地理编码失败");
							take_ok.setChecked(false);
							new BaiduMapHelper().startGeoCoder(loc_latlng, handler, ReverseGeoCode);
							
						}
						break;
						
					case FindCity:
						if(msg.getData().getString("message").equals(HandlerHelper.Ok)){

							loc_cityId = ((CityShow)msg.obj).id;
							loc_cityName = ((CityShow)msg.obj).cityName;
							
							take_cityId = loc_cityId;
							take_latitude = ((CityShow)msg.obj).latitude;
							take_longitude = ((CityShow)msg.obj).longitude;
							take_cityName =  ((CityShow)msg.obj).cityName;
							
							return_cityId = loc_cityId;
							return_latitude = ((CityShow)msg.obj).latitude;
							return_longitude = ((CityShow)msg.obj).longitude;
							return_cityName =  ((CityShow)msg.obj).cityName;
							
							take_city.setText(loc_cityName);
							return_city.setText(loc_cityName);System.out.println("f1---保存定位的城市--开始");
							
							SharedPreferenceHelper.putBean(getActivity(), Public_SP.City, new String[]{"id","cityName","latitude","longitude"}, new Object[]{loc_cityId,loc_cityName,(float)loc_latlng.latitude,(float)loc_latlng.longitude}, new int[]{SharedPreferenceHelper.Type_Int,SharedPreferenceHelper.Type_String,SharedPreferenceHelper.Type_Float,SharedPreferenceHelper.Type_Float});
							
							/*搜索门到门服务范围：默认是不开启的，所以，不需要判断是否选择了门到门*/
							new HttpHelper().initData(HttpHelper.Method_Get, getActivity(), "api/serviceCity/view?cityId="+loc_cityId, null, null, handler, Request_Loc_Points, 1, new TypeReference<CityPointBounds>() {});								

							System.out.println("f1---保存定位的城市--成功");
//							if(take_ok.isChecked()){//用户可能这个时候点击
//								
//								/*搜索门到门服务范围*/
//								new HttpHelper().initData(HttpHelper.Method_Get, getActivity(), "api/serviceCity/view?cityId="+loc_cityId, null, null, handler, Request_Loc_Points, 1, new TypeReference<CityPointBounds>() {});								
//							}else{
//								
//								Request_Store();//加载门店
//							}
							
						}else{
							
							take_ok.setChecked(false);
							
							if(msg.getData().getString("message").equals(HandlerHelper.Empty)){
								
								take_address.setText("当前城市没有租车门店");//没有找到当前城市
								return_address.setText("请选择还车门店");//没有找到当前城市
							}else{
								//new Fragment1_Helper().searchCity(msg.getData().getString("data"), handler, FindCity);//重新找城市
							}
						}
						break;
						
					case Request_Loc_Points://msg.getData().getString("message").equals(HandlerHelper.Ok)
						if(false){
																					
							List<Point> points = ((CityPointBounds)msg.obj).serveScope;
						    
						    /*绘制点*/
						    if(points != null && points.size() > 2){
						    	
						    	take_doortodoor_lin.setVisibility(View.VISIBLE);
								take_ok.setChecked(true);
						    	
						    	new BaiduMapHelper().DrawPolygon(points, mapView.getMap());
						    	
						    	if(new BaiduMapHelper().isPolygon(points, loc_latlng)){//判断定位地址是否在其中
						    			
						    		/*搜索地址信息*/
						    		if(loc_address != null && !loc_address.equals("")){
						    			
										takeCarStoreId = "-1";		System.out.println("定位信息"+loc_address);	
										takeCarAddress = loc_address;System.out.println("loc_address"+loc_address);
										take_latitude = loc_latlng.latitude;System.out.println("loc_latlng.latitude"+loc_latlng.latitude);
										take_longitude = loc_latlng.longitude;	System.out.println("loc_latlng.longitude"+loc_latlng.longitude);					    			
						    			take_address.setText(loc_address);
						    			
						    			returnCarStoreId = "-1";	
										returnCarAddress = loc_address;					
										return_latitude = loc_latlng.latitude;
										return_longitude = loc_latlng.longitude;
										return_address.setText(loc_address);
						    		}
									
						    	}else{
						    		
						    		take_address.setText("请选择取车地址");
						    		return_address.setText("请选择还车地址");
						    	}
						    }else{
						    	
						    	take_time.setText("");
								return_time.setText("");
								
								take_address.setText("请选择门店");
								return_address.setText("请选择门店");
						    	
						    	take_doortodoor_lin.setVisibility(View.GONE);
								take_ok.setChecked(false);
								
								Request_Store();//加载门店

						    }    
							
						}else{
							
							take_time.setText("");
							return_time.setText("");
							
							take_address.setText("请选择门店");
							return_address.setText("请选择门店");
							
							take_doortodoor_lin.setVisibility(View.GONE);
							take_ok.setChecked(false);
							
							Request_Store();//加载门店
						}
						break;	
						
					case Request_Points:
						
						List<Point> points = ((CityPointBounds)msg.obj).serveScope;
					    
					    /*绘制点*/
					    if(points != null && points.size() > 2){
					    	new BaiduMapHelper().DrawPolygon(points, mapView.getMap());					    						    	
					    }
						break;
						
					case MoveListener:
						if(is_first_tomylocation){
							
							System.out.println("v_1********************");				
							is_first_tomylocation = false;
						}else{
							
							if(is_tomylocation){
								iv_tomylocation.setVisibility(View.GONE);
								is_tomylocation = false;
							}else{
								iv_tomylocation.setVisibility(View.VISIBLE);
								is_tomylocation = false;
							}
						}
						break;
						
					case ToggleChanged:
						if(msg.getData().getString("message").equals(Public_Msg.Msg_UnChecked)){
							
							mapView.getMap().clear();
							
							take_note.setText("门店自助取还车服务");
							take_address_name.setText("取车门店");
							return_address_name.setText("还车门店");
							
//							take_time.setText("");
//							return_time.setText("");
							
							if(take_cityId == -1){
								take_address.setText("请选择门店");
								return_address.setText("请选择门店");
								return;
							}
							
							take_address.setText("请选择门店");
							return_address.setText("请选择门店");
							return_city.setText(take_cityName);
							return_cityId = take_cityId;
							return_cityName = return_cityName;
							
							Request_Store();
							
						}else{
							
							take_note.setText("门到门服务");
							take_address_name.setText("送车地点");
							return_address_name.setText("取车地点");
							
							if(take_cityId != -1){								
								/*搜索门到门服务范围*/
								new BaiduMapHelper().ShowMap(new LatLng(take_latitude, take_longitude), mapView.getMap());//地图显示取车城市
								
								new HttpHelper().initData(HttpHelper.Method_Get, getActivity(), "api/serviceCity/view?cityId="+take_cityId, null, null, handler, Request_Points, 1, new TypeReference<CityPointBounds>() {});								
							}
							 
							ViewInitHelper.init_f1_DateTime(new TextView[]{take_date,take_time,return_date,return_time},"10:00");
							ViewInitHelper.init_take(8, 20);
							ViewInitHelper.init_return(8, 20);
							take_address.setText("请选择地址");
							return_address.setText("请选择地址");
						}
						break;
					
					case Request_Store:
						if(msg.getData().getString("message").equals(HandlerHelper.Ok)){
							
							stores = (ArrayList<StoreShows>)msg.obj;
														
							take_address.setText(stores.get(0).storeName);
							return_address.setText(stores.get(0).storeName);
							
							takeCarAddress = stores.get(0).storeName;
							takeCarAddress_Store = stores.get(0).detailAddress;
							takeCarStoreId = stores.get(0).id.toString();
							returnCarAddress = stores.get(0).storeName;
							returnCarAddress_Store = stores.get(0).detailAddress;
							returnCarStoreId = stores.get(0).id.toString();
														
							ViewInitHelper.init_f1_ChangeDateTime(new TextView[]{take_date,take_time,return_date,return_time},stores.get(0).businessHoursStart,stores.get(0).businessHoursEnd);
							ViewInitHelper.init_take(TimeHelper.getTime2_Number(stores.get(0).businessHoursStart), TimeHelper.getTime2_Number(stores.get(0).businessHoursEnd));
							ViewInitHelper.init_return(TimeHelper.getTime2_Number(stores.get(0).businessHoursStart), TimeHelper.getTime2_Number(stores.get(0).businessHoursEnd));
							System.out.println("上班时间--------"+stores.get(0).businessHoursStart);
						}
						break;
						
					default:
						break;
				}

			}
		};

	}
	
	public void onClick(View view){

		if(!NetworkHelper.isNetworkAvailable(getActivity())){
			return;
		}
		
		switch (view.getId()) {
			case R.id.menu_out:
				
				final SlidingPaneLayout slidingPaneLayout = (SlidingPaneLayout)getActivity().findViewById(R.id.slidingpanellayout);
				if(slidingPaneLayout.isOpen()){
					slidingPaneLayout.closePane();
				}else{
					slidingPaneLayout.openPane();
				}			
				break;
			
			case R.id.taketime_lin:
				
				if(!take_ok.isChecked() && (take_time.getText().toString().equals("")||take_time.getText().toString()==null)){
					ToastHelper.showToastShort(getActivity(), "请先选择取车门店");
					return;
				}
				if(!take_ok.isChecked() && (return_time.getText().toString().equals("")||return_time.getText().toString()==null)){
					ToastHelper.showToastShort(getActivity(), "请先选择还车门店");
					return;
				}
				DateTimePickerDialog.showDateDialog(getActivity(), 1, take_time.getTag().toString(), new TextView[]{take_date,take_time,return_date,return_time,time_days});
				break;
				
			case R.id.returntime_lin:
				
				if(!take_ok.isChecked() && (return_time.getText().toString().equals("")||return_time.getText().toString()==null)){
					ToastHelper.showToastShort(getActivity(), "请先选择还车门店");
					return;
				}
				
				DateTimePickerDialog.showDateDialog(getActivity(), 2, return_time.getTag().toString(), new TextView[]{take_date,take_time,return_date,return_time,time_days});				
				break;
	
			case R.id.iv_tomylocation:
				if(loc_latlng != null){
					
					iv_tomylocation.setVisibility(View.GONE);
					is_tomylocation = true;
					new BaiduMapHelper().ShowMap(loc_latlng, mapView.getMap());
				}
				break;
			
			case R.id.take_city_lin:System.out.println("开始");
				IntentHelper.Fragment_startActivityForResult_Extra(getActivity(), this, Activity_City_List.class,RequestCode_takecity, new String[]{"loc_cityId","loc_cityName","loc_latitude","loc_longitude"}, new Object[]{loc_cityId, loc_cityName,loc_latlng.latitude,loc_latlng.longitude}, new int[]{IntentHelper.Type_Int, IntentHelper.Type_String,IntentHelper.Type_Double,IntentHelper.Type_Double});
				break;
			
//			case R.id.return_city_lin:
//				IntentHelper.Fragment_startActivityForResult_Extra(getActivity(), this, Activity_City_List.class,RequestCode_returncity, new String[]{"loc_cityId","loc_cityName","loc_latitude","loc_longitude"}, new Object[]{loc_cityId, loc_cityName,loc_latlng.latitude,loc_latlng.longitude}, new int[]{IntentHelper.Type_Int, IntentHelper.Type_String,IntentHelper.Type_Double,IntentHelper.Type_Double});
//				break;
			
			case R.id.take_address:
				
				if(take_cityId == -1){//当城市没哟加载进来
					
					ToastHelper.showToastShort(getActivity(), "请选择城市");
					return;
				}
				
				if(take_ok.isChecked()){
					
					System.out.println("选地址开始"+take_latitude+"--"+take_longitude+"--"+take_cityName);
					IntentHelper.Fragment_startActivityForResult_Extra(getActivity(), this, Activity_Map_Area.class,RequestCode_takeaddress, new String[]{"cityId","latitude","longitude","cityName"}, new Object[]{take_cityId,take_latitude, take_longitude,take_cityName}, new int[]{IntentHelper.Type_Int,IntentHelper.Type_Double, IntentHelper.Type_Double,IntentHelper.Type_String});					
				}else{
					
					IntentHelper.Fragment_startActivityForResult_Extra(getActivity(), this, Activity_Store_Select.class,RequestCode_takestore, new String[]{"cityId"}, new Object[]{take_cityId}, new int[]{IntentHelper.Type_Int});								
				}
				break;
				
			case R.id.return_address:
				
				if(!take_ok.isChecked()){//没有被选中
					
					return;
				}
				
				if(return_cityId == -1){//当城市没有加载进来
					ToastHelper.showToastShort(getActivity(), "请选择城市");
					return;
				}
				
				if(take_ok.isChecked()){
					
					IntentHelper.Fragment_startActivityForResult_Extra(getActivity(), this, Activity_Map_Area.class,RequestCode_returnaddress, new String[]{"cityId","latitude","longitude","cityName"}, new Object[]{return_cityId,return_latitude, return_longitude,return_cityName}, new int[]{IntentHelper.Type_Int,IntentHelper.Type_Double, IntentHelper.Type_Double,IntentHelper.Type_String});					
				}else{
					
					IntentHelper.Fragment_startActivityForResult_Extra(getActivity(), this, Activity_Store_Select.class,RequestCode_returnstore, new String[]{"cityId"}, new Object[]{return_cityId}, new int[]{IntentHelper.Type_Int});								
				}
				break;
				
			case R.id.ok:
				/*春节判断:*/
				if(TimeHelper.isTimeOfSpring("2017-01-27", "2017-01-30", take_time.getTag().toString()) && (take_cityId != 235 && take_cityId != 234)){
					
					final CustomDialog.Builder ibuilder;
					ibuilder = new CustomDialog.Builder(getActivity());
					ibuilder.setTitle("提示");
					ibuilder.setMessage("新年期间(1月27日至1月30日)暂不接受此时间取还车业务");
					ibuilder.setPositiveButton("确定", null);
					
					ibuilder.create().show();
					
					return;
				}

				if(TimeHelper.isTimeOfSpring("2017-01-27", "2017-01-30", return_time.getTag().toString()) && (take_cityId != 235 && take_cityId != 234)){
					
					final CustomDialog.Builder ibuilder;
					ibuilder = new CustomDialog.Builder(getActivity());
					ibuilder.setTitle("提示");
					ibuilder.setMessage("新年期间(1月27日至1月30日)暂不接受此时间取还车业务");
					ibuilder.setPositiveButton("确定", null);
					
					ibuilder.create().show();
					
					return;
				}
				
				/*设置参数*/
				Public_Param.order_paramas.takeCarDate = take_time.getTag().toString();System.out.println("取车时间"+take_time.getTag().toString());
				Public_Param.order_paramas.returnCarDate = return_time.getTag().toString();System.out.println("换车时间"+return_time.getTag().toString());
				
				Public_Param.order_paramas.takeCarCity = take_cityName;
				Public_Param.order_paramas.takeCarCityId = new Integer(take_cityId).toString();//为cityId
				Public_Param.order_paramas.returnCarCity = return_cityName;
				Public_Param.order_paramas.returnCarCityId = new Integer(return_cityId).toString();
				
				Public_Param.order_paramas.takeCarStoreId = takeCarStoreId;
				Public_Param.order_paramas.returnCarStoreId = returnCarStoreId;
				
				Public_Param.order_paramas.takeCarLatitude = take_latitude;
				Public_Param.order_paramas.takeCarLongitude = take_longitude;
				Public_Param.order_paramas.returnCarLatitude = return_latitude;
				Public_Param.order_paramas.returnCarLongitude = return_longitude;
				
				if(take_ok.isChecked()){
					Public_Param.order_paramas.isDoorToDoor = 1;
					Public_Param.order_paramas.takeCarAddress = takeCarAddress;
					Public_Param.order_paramas.returnCarAddress = returnCarAddress;
					System.out.println("检查1");
					boolean isOk = ValidateHelper.Validate(getActivity(), new boolean[]{take_cityId == -1,return_cityId == -1,takeCarAddress.equals(""),returnCarAddress.equals("")}, new String[]{"请选择取车城市","请选择还车城市","请选择取车地址","请选择还车地址"});
					if(!isOk){System.out.println("检查2");
						return;
					}
				}else{
					Public_Param.order_paramas.isDoorToDoor = 0;System.out.println("检查3");
					Public_Param.order_paramas.takeCarAddress = takeCarAddress;
					Public_Param.order_paramas.returnCarAddress = returnCarAddress;
					Public_Param.order_paramas.takeCarAddress_Store = takeCarAddress_Store;
					Public_Param.order_paramas.returnCarAddress_Store = returnCarAddress_Store;
					
					boolean isOk = ValidateHelper.Validate(getActivity(), new boolean[]{take_cityId == -1,return_cityId == -1,takeCarStoreId.equals("-1"),returnCarStoreId.equals("-1"),take_time.getText().toString().equals("")||take_time.getText().toString()==null,return_time.getText().toString().equals("")||return_time.getText().toString()==null}, new String[]{"请选择取车城市","请选择还车城市","请选择取车门店","请选择还车门店","请选择取车时间","请选择还车时间"});
					if(!isOk){System.out.println("检查4");
						return;
					}
				}
				
				/*跳转*/
				IntentHelper.startActivity(getActivity(), Activity_Car_List.class);			
				break;
				
			default:
				break;
		}
		
	}
	
	private void Request_Store() {System.out.println("f1---加载门店--开始cityId"+take_cityId);
		new HttpHelper().initData(HttpHelper.Method_Get, getActivity(), "api/china/province/city/"+take_cityId+"/store?available=1", null, null, handler, Request_Store, 1, new TypeReference<ArrayList<StoreShows>>() {});
	}
}

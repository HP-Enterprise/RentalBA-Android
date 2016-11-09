package com.gjcar.activity.fragment1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.TypeReference;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.SpatialRelationUtil;
import com.gjcar.annotation.ContentView;
import com.gjcar.annotation.ContentWidget;
import com.gjcar.app.R;
import com.gjcar.data.adapter.MapArea_Adapter;
import com.gjcar.data.bean.CityPointBounds;
import com.gjcar.data.bean.Point;
import com.gjcar.data.bean.StoreShows;
import com.gjcar.data.data.Public_BaiduTJ;
import com.gjcar.data.data.Public_Param;
import com.gjcar.framwork.BaiduMapHelper;
import com.gjcar.framwork.TencentMapHelper;
import com.gjcar.utils.AnnotationViewUtils;
import com.gjcar.utils.HandlerHelper;
import com.gjcar.utils.HttpHelper;
import com.gjcar.utils.IntentHelper;
import com.gjcar.utils.ListenerHelper;
import com.gjcar.utils.ToastHelper;
import com.gjcar.view.dialog.DateTimePickerDialog;
import com.gjcar.view.helper.ListViewHelper;
import com.gjcar.view.helper.LoadAnimateHelper;
import com.gjcar.view.helper.TitleBarHelper;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

@ContentView(R.layout.activity_map_area)
public class Activity_Map_Area extends Activity{

	/*handler*/
	private Handler handler;
	private final static int OK = 1;
	private final static int Search = 2;
	private final static int Move = 3;
	private final static int ItmeClick = 4;
	private final static int Request_Points = 5;
	
	@ContentWidget(click = "onClick")  EditText et_area;	
	@ContentWidget(id = R.id.listview) ListView listview;	
	
	@ContentWidget(id = R.id.mapView) MapView mapView;/*地图*/

	private String cityName = "";
	private List<Map<String,Object>> list_area = new ArrayList<Map<String,Object>>();
	
	private List<Point> points = new ArrayList<Point>();
	private boolean isLoadPointsOk = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		AnnotationViewUtils.injectObject(this, this);	
		
		Public_Param.list_area_activity.add(this);
				
		System.out.println("失败");
		/*handler*/
		initHandler();
		System.out.println("失败1");
		/*加载标题栏*/
		TitleBarHelper.Back_ok(this, "送车地点", handler, OK, 0);
		System.out.println("失败2");
		/*地图初始化*/
		new BaiduMapHelper().initBaiduMap(mapView);

		/*定位到初始位置*///"latitude","longitude","cityName"
		System.out.println("cityName"+cityName);
		System.out.println("latitude"+getIntent().getDoubleExtra("latitude", 34.50000));
		System.out.println("longitude"+getIntent().getDoubleExtra("longitude", 121.43333));
		cityName = getIntent().getStringExtra("cityName");System.out.println(""+cityName);
		new BaiduMapHelper().ShowMap(new LatLng(getIntent().getDoubleExtra("latitude", 34.50000), getIntent().getDoubleExtra("longitude", 121.43333)), mapView.getMap());//地图
		new BaiduMapHelper().ShowLocation(new LatLng(getIntent().getDoubleExtra("latitude", 34.50000), getIntent().getDoubleExtra("longitude", 121.43333)), mapView.getMap());
		
		/*搜索门到门服务范围*/
		new HttpHelper().initData(HttpHelper.Method_Get, this, "api/serviceCity/view?cityId="+getIntent().getIntExtra("cityId", 0), null, null, handler, Request_Points, 1, new TypeReference<CityPointBounds>() {});
		
		/*加载动画*/
		LoadAnimateHelper.Search_Animate(this, R.id.lin_list, null, 0, false, true, 0);
				
		/*监听地图移动*/
		new BaiduMapHelper().moveListener(mapView.getMap(), handler, Move);
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
		Public_BaiduTJ.pageStart(this, Public_BaiduTJ.Activity_Map_Area);	

	}
	
	@Override
	protected void onPause() {

		super.onPause();
		Public_BaiduTJ.pageEnd(this, Public_BaiduTJ.Activity_Map_Area);	
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(data == null){
			return;
		}

		IntentHelper.setResultExtras(this, 0, new String[]{"Address","latitude","longitude"}, new Object[]{""+data.getCharSequenceExtra("Address"),data.getDoubleExtra("latitude", 0.0),data.getDoubleExtra("longitude", 0.0)}, new int[]{IntentHelper.Type_String,IntentHelper.Type_Double,IntentHelper.Type_Double});							
		
	}
	
	public void onClick(View view){

		switch (view.getId()) {
		
			case R.id.et_area:
				if(!isLoadPointsOk){
					
					return;
				}
				
				Public_Param.points = points;
				IntentHelper.startActivityForResult_Extra(Activity_Map_Area.this, Activity_Area.class,100, new String[]{"cityName"}, new Object[]{cityName}, new int[]{IntentHelper.Type_String});
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
				
					case OK:
						if(list_area != null && list_area.size() != 0){
							IntentHelper.setResultExtras(Activity_Map_Area.this, 0, new String[]{"Address","latitude","longitude"}, new Object[]{list_area.get(0).get("title"),list_area.get(0).get("latitude"),list_area.get(0).get("longitude")}, new int[]{IntentHelper.Type_String,IntentHelper.Type_Double,IntentHelper.Type_Double});							
						}
						finish();
						break;
						
					case Search:
						list_area.clear();
						
						if(msg.getData().getString("message").equals(HandlerHelper.NoNet)){
							LoadAnimateHelper.load_noNetwork_animation();
							return;
						}

						if(msg.getData().getString("message").equals("out")){
							LoadAnimateHelper.load_fail_animation();
							ToastHelper.showToastShort(Activity_Map_Area.this, "超出了城市的范围");
							return;
						}
						
						if(msg.getData().getString("message").equals(HandlerHelper.Ok)){
							
							LoadAnimateHelper.load_success_animation();
							list_area = (List<Map<String,Object>>)msg.obj;
							listview.setVisibility(View.VISIBLE);
							listview.setAdapter(new MapArea_Adapter(Activity_Map_Area.this, (List<Map<String,Object>>)msg.obj));
							
							ListenerHelper.setListener(listview, ListenerHelper.Listener_ListView_OnItemClick, handler, ItmeClick);
							return;
						}
						
						LoadAnimateHelper.load_fail_animation();
						listview.setVisibility(View.GONE);
						break;
					
					case Request_Points:
		
						if(msg.getData().getString("message").equals(HandlerHelper.Ok)){
							
							LoadAnimateHelper.load_success_animation();
							points = ((CityPointBounds)msg.obj).serveScope;
						    
						    /*绘制点*/
						    if(points != null && points.size() > 2){
						    	new BaiduMapHelper().DrawPolygon(points, mapView.getMap());
						    	
						    	if(new BaiduMapHelper().isPolygon(points, new LatLng(getIntent().getDoubleExtra("latitude", 121.43333), getIntent().getDoubleExtra("longitude", 34.50000)))){
						    		
						    		/*搜索地址信息*/
									new BaiduMapHelper().ReverseGeoCode(Activity_Map_Area.this,new LatLng(getIntent().getDoubleExtra("latitude", 121.43333), getIntent().getDoubleExtra("longitude", 34.50000)),cityName,handler,Search);						
							
						    	}else{
						    		
						    		LoadAnimateHelper.load_success_animation();
						    		ToastHelper.showToastShort(Activity_Map_Area.this, "请移动地图或输入地址");
						    	}
						    }else{
						    	
						    	/*搜索地址信息*/
								new BaiduMapHelper().ReverseGeoCode(Activity_Map_Area.this,new LatLng(getIntent().getDoubleExtra("latitude", 121.43333), getIntent().getDoubleExtra("longitude", 34.50000)),cityName,handler,Search);						
						
						    }		    
							
						}else{
							
							/*搜索地址信息*/
							new BaiduMapHelper().ReverseGeoCode(Activity_Map_Area.this,new LatLng(getIntent().getDoubleExtra("latitude", 121.43333), getIntent().getDoubleExtra("longitude", 34.50000)),cityName,handler,Search);						
						}
						
						isLoadPointsOk = true;
						
						break;
						
					case Move:
						if(msg.getData().getString("message").equals(HandlerHelper.Ok)){
							
							/*绘制点*/
						    if(points != null && points.size() > 3){
						    	
						    	if(new BaiduMapHelper().isPolygon(points, (LatLng)msg.obj)){
						    		
						    		/*搜索地址信息*/
									LoadAnimateHelper.start_animation();
									listview.setVisibility(View.GONE);
									new BaiduMapHelper().ReverseGeoCode(Activity_Map_Area.this,(LatLng)msg.obj,cityName,handler,Search);							
				
						    	}else{
						    	
						    		ToastHelper.showToastShort(Activity_Map_Area.this, "超出了门到门服务的范围");
						    	}
						    }
											
						}
						break;
					
					case ItmeClick:						
						int position = msg.getData().getInt("message");
						IntentHelper.setResultExtras(Activity_Map_Area.this, 0, new String[]{"Address","latitude","longitude"}, new Object[]{list_area.get(position).get("title"),list_area.get(position).get("latitude"),list_area.get(position).get("longitude")}, new int[]{IntentHelper.Type_String,IntentHelper.Type_Double,IntentHelper.Type_Double});							
						break;
						
					default:
						break;
				}
			}
		};
	}
}

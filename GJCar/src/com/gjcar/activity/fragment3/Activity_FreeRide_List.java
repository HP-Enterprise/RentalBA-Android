package com.gjcar.activity.fragment3;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.TypeReference;
import com.gjcar.annotation.ContentView;
import com.gjcar.annotation.ContentWidget;
import com.gjcar.app.R;
import com.gjcar.data.adapter.FreeRide_List_Adapter;
import com.gjcar.data.bean.FreeRide;
import com.gjcar.data.data.Public_Param;
import com.gjcar.utils.AnnotationViewUtils;
import com.gjcar.utils.HandlerHelper;
import com.gjcar.utils.HttpHelper;
import com.gjcar.utils.IntentHelper;
import com.gjcar.utils.ListenerHelper;
import com.gjcar.view.helper.LoadAnimateHelper;
import com.gjcar.view.helper.TitleBarHelper;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

@ContentView(R.layout.activity_freecar_list)
public class Activity_FreeRide_List extends Activity{
	
	@ContentWidget(id = R.id.listview) ListView listview;
	
	/*Handler*/
	private Handler handler;
	private final static int CarList_Data = 1;	
	private final static int CarList_Show = 2;
	private final static int ClickItem = 3;
	/*数据*/
	private List<FreeRide> list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		AnnotationViewUtils.injectObject(this, this);
		
		Public_Param.list_free_order_activity.clear();
		Public_Param.list_free_order_activity.add(this);
		
		/*handler*/
		initHandler();
		
		/*标题*/
		TitleBarHelper.Back(this, "车型列表", 0);
		
		/*设置监听器*/
		ListenerHelper.setListener(listview, ListenerHelper.Listener_ListView_OnItemClick, handler, ClickItem);
		
		/*加载动画*/
		LoadAnimateHelper.Search_Animate(this, R.id.activity, handler, 0, false,true,1);
		
		/*初始化数据*/
		initData();
		
	}
	
	private void initData() {
		
		String getCarCityId = new Integer(getIntent().getIntExtra("getCarCityId", -1)).toString();System.out.println("cityId"+getCarCityId);
		String getReturnId = new Integer(getIntent().getIntExtra("returnCityId", -1)).toString();
		String api = "api/freeRide?currentPage=1&getCarCityId="+getCarCityId+"&pageSize=5&status=1&returnCarCityId="+getReturnId;

		new HttpHelper().initData(HttpHelper.Method_Get, this, api, null, null, handler, CarList_Data, 1, new TypeReference<ArrayList<FreeRide>>() {});
	}
	
	private void initHandler() {

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				switch (msg.what) {

					case CarList_Data:
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){

							LoadAnimateHelper.load_success_animation();
							list = (ArrayList<FreeRide>)msg.obj;
//				           	System.out.println("size"+list.size());	      
//				           	System.out.println("解析结束"+list.get(0).vehicleModelShow.model );	
				           	handler.sendEmptyMessage(CarList_Show);
				           	return;
						}
						LoadAnimateHelper.load_empty_animation();
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Empty)){
							
				           	System.out.println("请求失败");	            
						}
						if(HandlerHelper.getString(msg).equals(HandlerHelper.DataFail)){
							
				           	System.out.println("请求失败");	            
						}
						
						break;
						
					case CarList_Show:
						FreeRide_List_Adapter adapter = new FreeRide_List_Adapter(Activity_FreeRide_List.this, list);
						listview.setAdapter(adapter);
						break;
					
					case ClickItem:
						Public_Param.freeRideId = list.get(msg.getData().getInt("message")).id;
						IntentHelper.startActivity(Activity_FreeRide_List.this, Activity_FreeRide_Order_Submit.class);
						break;
						
					default:
						break;
				}
			}
		};
	}
	
}

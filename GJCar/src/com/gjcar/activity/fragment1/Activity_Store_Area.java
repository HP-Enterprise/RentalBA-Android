package com.gjcar.activity.fragment1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import com.alibaba.fastjson.TypeReference;
import com.gjcar.annotation.ContentView;
import com.gjcar.annotation.ContentWidget;
import com.gjcar.app.R;
import com.gjcar.data.adapter.Letter_Right_Adapter;
import com.gjcar.data.adapter.MyBaseAdapter;
import com.gjcar.data.bean.CityShow;
import com.gjcar.data.bean.StoreShows;
import com.gjcar.data.bean.TradeAreaShow;
import com.gjcar.data.data.Public_BaiduTJ;
import com.gjcar.data.service.CityHelper;
import com.gjcar.data.service.Store_SelectHelper;
import com.gjcar.framwork.TencentMapHelper;
import com.gjcar.utils.AnnotationViewUtils;
import com.gjcar.utils.HandlerHelper;
import com.gjcar.utils.HttpHelper;
import com.gjcar.utils.IntentHelper;
import com.gjcar.utils.ListenerHelper;
import com.gjcar.view.helper.ListViewHelper;
import com.gjcar.view.helper.LoadAnimateHelper;
import com.gjcar.view.helper.TitleBarHelper;
import com.gjcar.view.helper.ViewHelper;
import com.gjcar.view.listview.QQListAdapter_City;
import com.gjcar.view.listview.QQListView_City;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

@ContentView(R.layout.activity_store_select)
public class Activity_Store_Area extends Activity{

	private Handler handler;
	private final static int Request_Store = 1;
	private final static int Data_Show = 2;
	private final static int ItmeClick = 3;
	private final static int KeyWord = 4;
	private final static int Search = 5;
	
	private List<StoreShows> list_store = new ArrayList<StoreShows>();
	
	@ContentWidget(id = R.id.lin_search) LinearLayout lin_search;
	@ContentWidget(id = R.id.et_area) EditText et_area;
	@ContentWidget(id = R.id.listview_search) ListView listview_search;
	
	@ContentWidget(id = R.id.lin_store) LinearLayout lin_store;
	@ContentWidget(id = R.id.listview_store) ListView listview_store;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		AnnotationViewUtils.injectObject(this, this);
		
		/*初始化Handler*/
		initHandler();
		
		/*加载标题*/
		TitleBarHelper.Back(this, "选择门店", 0);
		
		/*搜索框监听器设置*/
		//new Store_SelectHelper().Search_Listener(et_area, handler, KeyWord);
		
		/*加载动画*/
		LoadAnimateHelper.Search_Animate(this, R.id.activity, null, 0, false, true, 2);

		/*加载数据*/
		Request_Store();
	}

	@Override
	protected void onResume() {
		
		super.onResume();
		Public_BaiduTJ.pageStart(this, Public_BaiduTJ.Activity_Store_Select);	

	}
	
	@Override
	protected void onPause() {

		super.onPause();
		Public_BaiduTJ.pageEnd(this, Public_BaiduTJ.Activity_Store_Select);	
	}
	
	private void Request_Store() {
																		
		new HttpHelper().initData(HttpHelper.Method_Get, this, "api/china/province/city/168/store?available=1", null, null, handler, Request_Store, 1, new TypeReference<ArrayList<StoreShows>>() {});
	}
	/*初始化Handler*/
	private void initHandler() {

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				switch (msg.what) {

					case Request_Store:
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
							
							LoadAnimateHelper.load_success_animation();
							
							list_store = (ArrayList<StoreShows>)msg.obj;
							
				           	HandlerHelper.sendObject(handler, Data_Show, new Store_SelectHelper().getData(list_store, new String[]{"title", "address"}));
				           
				           	return;
						}
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Empty)){
							LoadAnimateHelper.load_empty_animation();
						}
						//Request_Store();//失败继续请求
						
						break;
					
					case Data_Show:
												
						ListViewHelper.SimpleAdapter(Activity_Store_Area.this, listview_store, (List<Map<String,Object>>)msg.obj, R.layout.listview_area_item, new String[]{"title", "address"}, new int[]{R.id.title,R.id.address});						
	
						ListenerHelper.setListener(listview_store, ListenerHelper.Listener_ListView_OnItemClick, handler, ItmeClick);
						
						/*显示搜索框*/
						lin_search.setVisibility(View.GONE);
						break;
					
					case ItmeClick:						
						int position = msg.getData().getInt("message");
						IntentHelper.setResultStringExtras(Activity_Store_Area.this, 0, new String[]{"Id","Name","Address","StartTime","EndTime"}, new String[]{list_store.get(position).id.toString(), list_store.get(position).storeName,list_store.get(position).detailAddress,list_store.get(position).businessHoursStart,list_store.get(position).businessHoursEnd});
						break;
						
					case KeyWord:
						if(msg.getData().getString("message").equals("")){
							LoadAnimateHelper.load_success_animation();
							listview_search.setVisibility(View.GONE);
							lin_store.setVisibility(View.VISIBLE);
							return;
						}
						
						LoadAnimateHelper.start_animation();
						listview_search.setVisibility(View.GONE);
						lin_store.setVisibility(View.GONE);
						TencentMapHelper.suggestion(Activity_Store_Area.this, "武汉", msg.getData().getString("message"), handler, Search);
						break;
					
					case Search:
						if(msg.getData().getString("message").equals(HandlerHelper.NoNet)){
							LoadAnimateHelper.load_noNetwork_animation();
							return;
						}

						if(msg.getData().getString("message").equals(HandlerHelper.Ok)){
							LoadAnimateHelper.load_success_animation();
//							ListViewHelper.flush((List<Map<String,Object>>)msg.obj);
							ListViewHelper.SimpleAdapter(Activity_Store_Area.this, listview_search, (List<Map<String,Object>>)msg.obj, R.layout.listview_area_item, new String[]{"title", "address"}, new int[]{R.id.title,R.id.address});

							return;
						}
						
						LoadAnimateHelper.load_fail_animation();
						
						break;
						
					default:
						break;
				}
			}
		};
	}
	
}

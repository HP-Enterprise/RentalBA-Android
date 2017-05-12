package com.gjcar.activity.fragment1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gjcar.annotation.ContentView;
import com.gjcar.annotation.ContentWidget;
import com.gjcar.app.R;
import com.gjcar.data.data.Public_BaiduTJ;
import com.gjcar.data.data.Public_Param;
import com.gjcar.framwork.TencentMapHelper;
import com.gjcar.utils.AnnotationViewUtils;
import com.gjcar.utils.HandlerHelper;
import com.gjcar.utils.IntentHelper;
import com.gjcar.utils.ListenerHelper;
import com.gjcar.view.helper.ListViewHelper;
import com.gjcar.view.helper.LoadAnimateHelper;
import com.gjcar.view.helper.TitleBarHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;

@ContentView(R.layout.activity_area)
public class Activity_Area extends Activity{

	/*Handler对象*/
	private Handler handler;
	private final static int KeyWord = 1;
	private final static int Search = 2;
	private final static int ItemClick = 3;
	
	/*控件*/
	@ContentWidget(id = R.id.listview) ListView listview;

	private List<Map<String,Object>> list_area = new ArrayList<Map<String,Object>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		AnnotationViewUtils.injectObject(this, this);

		Public_Param.list_area_activity.add(this);
		
		/*handler*/
		initHandler();
		
		/*加载标题*/
		TitleBarHelper.Search_Area(this, getIntent().getStringExtra("cityName"), handler, KeyWord);
		
		/*加载动画*/
		LoadAnimateHelper.Search_Animate(this, R.id.activity, null, 0, false, false, 1);
		
		/*初始化*/
		ListViewHelper.SimpleAdapter(Activity_Area.this, listview, Public_Param.list_area, R.layout.listview_area_item, new String[]{"title", "address"}, new int[]{R.id.title,R.id.address});

		/*设置点击事件*/
		ListenerHelper.setListener(listview, ListenerHelper.Listener_ListView_OnItemClick, handler, ItemClick);
		
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
		Public_BaiduTJ.pageStart(this, Public_BaiduTJ.Activity_Area);	

	}
	
	@Override
	protected void onPause() {

		super.onPause();
		Public_BaiduTJ.pageEnd(this, Public_BaiduTJ.Activity_Area);	
	}
	
	@SuppressLint("HandlerLeak")
	private void initHandler() {

		handler = new Handler() {
			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				switch (msg.what) {
				
					case KeyWord:
						LoadAnimateHelper.start_animation();
						listview.setVisibility(View.GONE);
						TencentMapHelper.suggestion(Activity_Area.this, getIntent().getStringExtra("cityName"), msg.getData().getString("message"), handler, Search);
						break;
						
					case Search:
						if(msg.getData().getString("message").equals(HandlerHelper.NoNet)){
							LoadAnimateHelper.load_noNetwork_animation();
							return;
						}
							
						if(msg.getData().getString("message").equals(HandlerHelper.Ok)){
							LoadAnimateHelper.load_success_animation();
							list_area = (List<Map<String,Object>>)msg.obj;System.out.println("oka");
							ListViewHelper.SimpleAdapter(Activity_Area.this, listview, (List<Map<String,Object>>)msg.obj, R.layout.listview_area_item, new String[]{"title", "address"}, new int[]{R.id.title,R.id.address});
							System.out.println("okb");
							return;
						}
						
						LoadAnimateHelper.load_fail_animation();
						
						break;
						
					case ItemClick:
						int position = msg.getData().getInt("message");
						IntentHelper.setResultExtras(Activity_Area.this, 0, new String[]{"Address","latitude","longitude"}, new Object[]{list_area.get(position).get("title"),list_area.get(position).get("latitude"),list_area.get(position).get("longitude")}, new int[]{IntentHelper.Type_String,IntentHelper.Type_Double,IntentHelper.Type_Double});							
						break;
						
					default:
						break;
				}
			}
		};
	}
}

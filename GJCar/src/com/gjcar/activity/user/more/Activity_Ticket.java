package com.gjcar.activity.user.more;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.gjcar.annotation.ContentView;
import com.gjcar.annotation.ContentWidget;
import com.gjcar.app.R;
import com.gjcar.data.adapter.CarList_Adapter;
import com.gjcar.data.adapter.FreeRide_List_Adapter;
import com.gjcar.data.adapter.TicketList_Adapter;
import com.gjcar.data.bean.FreeRide;
import com.gjcar.data.bean.Model____Vendor_Store_Price;
import com.gjcar.data.bean.TicketInfo;
import com.gjcar.data.bean.TradeAreaShow;
import com.gjcar.data.data.Public_BaiduTJ;
import com.gjcar.data.data.Public_Param;
import com.gjcar.data.data.Public_Data;
import com.gjcar.data.service.CarList_Helper;
import com.gjcar.data.service.TicketList_Helper;
import com.gjcar.framwork.TencentMapHelper;
import com.gjcar.utils.AnnotationViewUtils;
import com.gjcar.utils.HandlerHelper;
import com.gjcar.utils.HttpHelper;
import com.gjcar.utils.IntentHelper;
import com.gjcar.utils.SharedPreferenceHelper;
import com.gjcar.utils.TimeHelper;
import com.gjcar.view.helper.ListViewHelper;
import com.gjcar.view.helper.LoadAnimateHelper;
import com.gjcar.view.helper.PageIndicatorHelper;
import com.gjcar.view.helper.TitleBarHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

@ContentView(R.layout.activity_ticket)
public class Activity_Ticket extends Activity{
	
	@ContentWidget(id = R.id.listview) ListView listview;
	
	@ContentWidget(id = R.id.t_notuse) TextView t_notuse;
	@ContentWidget(id = R.id.t_use) TextView t_use;
	@ContentWidget(id = R.id.t_timeout) TextView t_timeout;

	@ContentWidget(id = R.id.l_notuse) View l_notuse;
	@ContentWidget(id = R.id.l_use) View l_use;
	@ContentWidget(id = R.id.l_timeout) View l_timeout;
	
	/*Handler*/
	private Handler handler;
	private final static int List_Data = 1;
	
	private final static int Page_Indicator = 3;
	
	/*数据*/
	private List<TicketInfo> list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		AnnotationViewUtils.injectObject(this, this);
		
		/*handler*/
		initHandler();
		
		/*标题*/
		TitleBarHelper.Back(this, "我的礼券", 0);
		
		/*加载动画*/
		LoadAnimateHelper.Search_Animate(this, R.id.activity, handler, 0, false,false,2);
		
		/*初始化数据*/
		initData("2");
		
		/*视图*/		
		new PageIndicatorHelper().initIndicator(this, new TextView[]{t_notuse,t_use,t_timeout}, new View[]{l_notuse,l_use,l_timeout}, R.color.page_text_select, R.color.page_text_normal2, handler, Page_Indicator);
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
		Public_BaiduTJ.pageStart(this, Public_BaiduTJ.Activity_Ticket);	

	}
	
	@Override
	protected void onPause() {

		super.onPause();
		Public_BaiduTJ.pageEnd(this, Public_BaiduTJ.Activity_Ticket);	
	}
	
	private void initData(String state) {System.out.println("1");

		LoadAnimateHelper.start_animation();
		listview.setVisibility(View.GONE);
		String api = "api/me/coupon?currentPage=1&pageSize=100&state="+state+"&uid="+SharedPreferenceHelper.getUid(this)+"&applySource=2";
		
		new TicketList_Helper().initDataList(HttpHelper.Method_Get, this, api, null, null, handler, List_Data, 1, new TypeReference<ArrayList<TicketInfo>>() {});
	}
	
	private void initHandler() {

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				switch (msg.what) {

					case List_Data:
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){

							LoadAnimateHelper.load_success_animation();
							listview.setVisibility(View.VISIBLE);
							list = (ArrayList<TicketInfo>)msg.obj;//System.out.println(""+list.get(0).title);
							TicketList_Adapter adapter = new TicketList_Adapter(Activity_Ticket.this, list);
							listview.setAdapter(adapter);
				           	return;
						}
						LoadAnimateHelper.load_empty_animation();
						listview.setVisibility(View.GONE);
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Empty)){
							
				           	System.out.println("请求失败");	            
						}
						if(HandlerHelper.getString(msg).equals(HandlerHelper.DataFail)){
							
				           	System.out.println("请求失败");	            
						}
						
						break;

					case Page_Indicator:
						
						String state = new Integer(new Integer(HandlerHelper.getString(msg)).intValue()+2).toString();
						System.out.println("状态码"+state);
						
						switch (new Integer(HandlerHelper.getString(msg)).intValue()) {
							case 0:
								initData("2");
								break;
	
							case 1:
								initData("4");
								break;
								
							case 2:
								initData("5");
								break;
								
							default:
								break;
						}
						break;
						
					default:
						break;
				}
			}
		};
	}
	
}

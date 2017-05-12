package com.gjcar.activity.fragment6;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.TypeReference;
import com.baidu.mapapi.map.MapView;
import com.gjcar.activity.fragment1.Activity_Car_List;
import com.gjcar.activity.fragment1.Activity_Order_Submit;
import com.gjcar.activity.fragment1.WebActivity;
import com.gjcar.annotation.ContentWidget;
import com.gjcar.app.R;
import com.gjcar.data.adapter.ActivityList_Adapter;
import com.gjcar.data.adapter.CarList_Adapter;
import com.gjcar.data.bean.ActivityInfo;
import com.gjcar.data.bean.Model____Vendor_Store_Price;
import com.gjcar.data.data.Public_Param;
import com.gjcar.data.service.CarList_Helper;
import com.gjcar.framwork.BaiduMapHelper;
import com.gjcar.utils.AnnotationViewFUtils;
import com.gjcar.utils.HandlerHelper;
import com.gjcar.utils.HttpHelper;
import com.gjcar.utils.IntentHelper;
import com.gjcar.utils.TimeHelper;
import com.gjcar.utils.ToastHelper;
import com.gjcar.view.dialog.DateTimePickerDialog;
import com.gjcar.view.helper.LoadAnimateHelper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class Fragment6 extends Fragment{

	@ContentWidget(id = R.id.listview) ListView listview;
	
	/*Handler*/
	private Handler handler;
	private final static int List_Data = 1;	
	private final static int List_Show = 2;
	
	/*数据*/
	private List<ActivityInfo> list;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment6, null);		
		AnnotationViewFUtils.injectObject(this, getActivity(), view);
		
		initHandler();
		
		initData();
		
		initItemClick();
		
		return view;
	}
	
	private void initItemClick() {
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				if(list.get(position).phoneLink != null && !list.get(position).phoneLink.equals("")){
					
					IntentHelper.startActivity_StringExtras(getActivity(), WebActivity.class, new String[]{"title","fragment","url"}, new String[]{"活动详情","action_detail",list.get(position).phoneLink});
					Public_Param.send_toWeb = 2;
				
				}
				
			}
			
		});
	}

	private void initData() {System.out.println("1");
	
		String aPosition = "0";
		String aType = "2";

		String api = "api/actDisplay/list?aPosition="+aPosition+"&aType="+aType;
				
		new HttpHelper().initData(HttpHelper.Method_Get, getActivity(), api, null, null, handler, List_Data, 1, new TypeReference<ArrayList<ActivityInfo>>() {});
	}

	private void initHandler() {

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				System.out.println("size2");
				switch (msg.what) {

					case List_Data:
						System.out.println("size1");	   
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
							System.out.println("size");	   

							list = (ArrayList<ActivityInfo>)msg.obj;
				           	System.out.println("size"+list.size());	      
				           	System.out.println("解析结束"+list.get(0).description );	
				           	handler.sendEmptyMessage(List_Show);
				           	return;
						}

						if(HandlerHelper.getString(msg).equals(HandlerHelper.Empty)){
							
				           	System.out.println("请求失败");	            
						}
						if(HandlerHelper.getString(msg).equals(HandlerHelper.DataFail)){
							
				           	System.out.println("请求失败");	            
						}
						
						break;
						
					case List_Show:
						System.out.println("数量"+list.size());
						ActivityList_Adapter adapter = new ActivityList_Adapter(getActivity(), list);
						listview.setAdapter(adapter);
						break;
						
					default:
						break;
				}
			}
		};
	}
}

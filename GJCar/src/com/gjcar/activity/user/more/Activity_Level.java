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
import com.gjcar.data.bean.FreeRide;
import com.gjcar.data.bean.Model____Vendor_Store_Price;
import com.gjcar.data.bean.TradeAreaShow;
import com.gjcar.data.bean.User;
import com.gjcar.data.data.Public_BaiduTJ;
import com.gjcar.data.data.Public_Param;
import com.gjcar.data.data.Public_Data;
import com.gjcar.data.service.CarList_Helper;
import com.gjcar.framwork.TencentMapHelper;
import com.gjcar.utils.AnnotationViewUtils;
import com.gjcar.utils.HandlerHelper;
import com.gjcar.utils.HttpHelper;
import com.gjcar.utils.IntentHelper;
import com.gjcar.utils.SharedPreferenceHelper;
import com.gjcar.utils.TimeHelper;
import com.gjcar.view.helper.ListViewHelper;
import com.gjcar.view.helper.LoadAnimateHelper;
import com.gjcar.view.helper.TitleBarHelper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

@ContentView(R.layout.activity_level)
public class Activity_Level extends Activity{
	
	@ContentWidget(id = R.id.level) TextView level;
	
	@ContentWidget(id = R.id.level_icon1) ImageView level_icon1;
	@ContentWidget(id = R.id.level_line1) View level_line1;
	@ContentWidget(id = R.id.level_icon2) ImageView level_icon2;
	@ContentWidget(id = R.id.level_line2) View level_line2;
	@ContentWidget(id = R.id.level_icon3) ImageView level_icon3;
	@ContentWidget(id = R.id.level_line3) View level_line3;
	@ContentWidget(id = R.id.level_icon4) ImageView level_icon4;
	@ContentWidget(id = R.id.level_line4) View level_line4;
	
	@ContentWidget(id = R.id.c_count) TextView c_count;
	@ContentWidget(id = R.id.c_not_count) TextView c_not_count;
	@ContentWidget(id = R.id.c_level) TextView c_level;
	@ContentWidget(id = R.id.next_level_lin) LinearLayout next_level_lin;
	
	@ContentWidget(id = R.id.wb_aboutus) WebView wb_aboutus;
	
	/*Handler*/
	private Handler handler;
	private final static int Request_Level = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		AnnotationViewUtils.injectObject(this, this);
		
		/*handler*/
		initHandler();
		
		/*����*/
		TitleBarHelper.Back(this, "��Ա�ȼ�", 0);
		
		/*��ʼ������*/
		initData();
		
		/*����*/
		initCount(10,30);
		
		/*�ȼ�*/
		initLevel(1);
		
		/*�İ�*/
		initWebView();
		
	}

	@Override
	protected void onResume() {
		
		super.onResume();
		Public_BaiduTJ.pageStart(this, Public_BaiduTJ.Activity_Level);	
	}
	
	@Override
	protected void onPause() {

		super.onPause();
		Public_BaiduTJ.pageEnd(this, Public_BaiduTJ.Activity_Level);	
	}
	
	private void initCount(int already_count, int no_count){
		
		c_count.setText(already_count+"");
		c_not_count.setText(no_count+"");
	}
	
	private void initLevel(int levle) {
		
		/*����*/
		int rink = levle;
		String[] rinks = new String[]{"�տ���Ա","������Ա","�𿨻�Ա","���𿨻�Ա"};
		
		/*�ȼ�*/
		level.setText(rinks[rink-1]);
		
		/*�ȼ�:ͼƬ*/
		List<ImageView> icons = new ArrayList<ImageView>();
		List<View> views = new ArrayList<View>();
		
		icons.add(level_icon1);icons.add(level_icon2);icons.add(level_icon3);icons.add(level_icon4);
		views.add(level_line1);views.add(level_line2);views.add(level_line3);views.add(level_line4);
		
		for (int i = 0; i < rink; i++) {
			
			icons.get(i).setImageResource(R.drawable.level_icon);
			views.get(i).setBackgroundColor(getResources().getColor(R.color.page_text_select));
		}
		
		/*�ȼ�:�������ĵȼ�*/
		if(rink-1+1 >= 4){
			next_level_lin.setVisibility(View.GONE);
		}else{
			c_level.setText(rinks[rink-1+1]);
		}
		
	}

	private void initData() {System.out.println("1");
		
		new HttpHelper().initData(HttpHelper.Method_Get, this, "api/me", null, null, handler, Request_Level, 1, new TypeReference<User>() {});
	}
	
	private void initHandler() {

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				switch (msg.what) {

					case Request_Level:
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){

							User user = (User)msg.obj;System.out.println("lvl"+user.lvl.intValue());
							initLevel(user.lvl.intValue());
				           	return;
						}
						
						break;
						
					default:
						break;
				}
			}
		};
	}

	/**
	 * ��ʼ����ͼ
	 */
	private void initWebView() {
	
		WebSettings webSet = wb_aboutus.getSettings();
		webSet.setJavaScriptEnabled(true);// js�Ƿ����
		//webSet.setBuiltInZoomControls(true);////����ַŴ���С�İ�ť
//		webSet.setSupportZoom(true);//����
		webSet.setSupportMultipleWindows(true);
//		webSet.setUseWideViewPort(true);//���ô����ԣ�������������š� 
		
		wb_aboutus.loadUrl("file:///android_asset/copy.html");
	}
}

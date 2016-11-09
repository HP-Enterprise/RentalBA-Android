package com.gjcar.activity.user.more;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.gjcar.activity.fragment1.Activity_Area;
import com.gjcar.activity.user.User_Activity;
import com.gjcar.annotation.ContentView;
import com.gjcar.annotation.ContentWidget;
import com.gjcar.app.R;
import com.gjcar.data.adapter.CarList_Adapter;
import com.gjcar.data.adapter.FreeRide_List_Adapter;
import com.gjcar.data.adapter.ScoreList_Adapter;
import com.gjcar.data.bean.FreeRide;
import com.gjcar.data.bean.Model____Vendor_Store_Price;
import com.gjcar.data.bean.ScoreInfo;
import com.gjcar.data.bean.TradeAreaShow;
import com.gjcar.data.data.Public_Api;
import com.gjcar.data.data.Public_BaiduTJ;
import com.gjcar.data.data.Public_Param;
import com.gjcar.data.data.Public_Data;
import com.gjcar.data.data.Public_SP;
import com.gjcar.data.service.CarList_Helper;
import com.gjcar.data.service.TicketList_Helper;
import com.gjcar.fragwork.alipay.AlipayHelper;
import com.gjcar.framwork.TencentMapHelper;
import com.gjcar.utils.AnnotationViewUtils;
import com.gjcar.utils.HandlerHelper;
import com.gjcar.utils.HttpHelper;
import com.gjcar.utils.IntentHelper;
import com.gjcar.utils.SharedPreferenceHelper;
import com.gjcar.utils.TimeHelper;
import com.gjcar.utils.ToastHelper;
import com.gjcar.view.helper.ListViewHelper;
import com.gjcar.view.helper.LoadAnimateHelper;
import com.gjcar.view.helper.TitleBarHelper;
import com.gjcar.view.helper.ViewHelper;
import com.gjcar.view.helper.ViewInitHelper;
import com.gjcar.view.widget.ImageView_Round;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

@ContentView(R.layout.activity_score)
public class Activity_Score extends Activity{
	
	@ContentWidget(id = R.id.score) TextView score;
	
	@ContentWidget(id = R.id.note) TextView note;
	
	@ContentWidget(id = R.id.listview) ListView listview;
	@ContentWidget(id = R.id.line) View line;
	
	/*Handler*/
	private Handler handler;
	private final static int ActivityList_Data = 1;
	
	/*数据*/
	private List<ScoreInfo> list;
	
	private static boolean isLoadOk = false;
	private static int remain = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		AnnotationViewUtils.injectObject(this, this);
		
		/*handler*/
		initHandler();
		
		/*标题*/
		Back(this, "我的积分", 0);
		
		/*获取积分*/
		initScoreData();
		
		/*加载动画*/
		LoadAnimateHelper.Search_Animate(this, R.id.activity, handler, 0, false,true,3);
		
		/*初始化数据*/
		initData();
		
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
		Public_BaiduTJ.pageStart(this, Public_BaiduTJ.Activity_Score);	
		
		initScoreData();//刷新数据
		initData();
	}
	
	@Override
	protected void onPause() {

		super.onPause();
		Public_BaiduTJ.pageEnd(this, Public_BaiduTJ.Activity_Score);	
	}
	
	private void initData() {System.out.println("1");
		
		String api = "api/me/accumulate/detail?currentPage=1&pageSize=100";
					
		new TicketList_Helper().initDataList(HttpHelper.Method_Get, this, api, null, null, handler, ActivityList_Data, 1, new TypeReference<ArrayList<ScoreInfo>>() {});
	}
	
	private void initHandler() {

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				switch (msg.what) {

					case ActivityList_Data:
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){

							LoadAnimateHelper.load_success_animation();
							line.setVisibility(View.VISIBLE);
							
							list = (ArrayList<ScoreInfo>)msg.obj;

							ScoreList_Adapter adapter = new ScoreList_Adapter(Activity_Score.this, list);
							listview.setAdapter(adapter);
							
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

					default:
						break;
				}
			}
		};
	}
	
	public static void Back(final Context context, String titleName, int index){
		
		//添加标题
		View titleView = View.inflate(context, R.layout.titlebar_score, null);

		LinearLayout lin = (LinearLayout)((Activity)context).findViewById(R.id.activity);
		
		lin.addView(titleView,index);//从0开始：0在最上面
		
		ImageView back = (ImageView)titleView.findViewById(R.id.back);
		
		TextView change = (TextView)titleView.findViewById(R.id.change);
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				((Activity)context).finish();
			}
		});
		
		change.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if(!isLoadOk){
					//ToastHelper.showToastShort(context, "正在获取积分...");
					return;
				}
				
				if(remain  <= 0){
					ToastHelper.showToastShort(context, "积分不够，无法兑换");
					return;
				}
				
				Intent intent = new Intent(context, Activity_Score_Change.class);//跳转		
				intent.putExtra("remain", remain);
				context.startActivity(intent);	
			}
		});
		
		TextView title = (TextView)titleView.findViewById(R.id.title);
		title.setText(titleName);
		
	}
	
    /** 发送验证码  */
	private void initScoreData() {

		isLoadOk = false;
		
		/*  向服务器登陆  */
		AsyncHttpClient httpClient = new AsyncHttpClient();
		httpClient.setTimeout(Integer.parseInt(getResources().getString(
				R.string.app_connection_timeout)));

		RequestParams params = new RequestParams();//设置请求参数

		String url = Public_Api.appWebSite + "api/me/accumulate/remain";//设置请求的url	
		
		AddCookies(httpClient);
		
		httpClient.get(url, params, new AsyncHttpResponseHandler() {

			/* 处理请求成功  */
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

				//{"status":"true","message":{"id":47219,"uid":53395,"remain":400}}
				String smsData = new String(arg2);
				System.out.println("积分查询"+smsData);
				org.json.JSONObject jobject;
				try {

					jobject = new org.json.JSONObject(smsData);
					boolean status = jobject.getBoolean("status");
					String message = jobject.getString("message");

					if (status) {
						
						isLoadOk = true;
						
						jobject = new org.json.JSONObject(message);
						
						remain = jobject.getInt("remain");
						score.setText(jobject.getString("remain"));
					}

				} catch (JSONException e) {

					e.printStackTrace();
				}

			}

			/* 5.处理请求失败  */
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				
				
			}

		});
	}
	
	/**
     * 增加Cookie
     * @param request
     */
    public void AddCookies(AsyncHttpClient request)
    {
          StringBuilder sb = new StringBuilder();

          String key = "token";
          String val = SharedPreferenceHelper.getString(this, Public_SP.Account, key);
          sb.append(key);
          sb.append("=");
          sb.append(val);
          sb.append(";");

          request.addHeader("cookie", sb.toString());

          System.out.println(""+sb);
    }
    
}

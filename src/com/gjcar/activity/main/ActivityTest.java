package com.gjcar.activity.main;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.gjcar.activity.fragment1.Activity_Order_Submit;
import com.gjcar.app.R;
import com.gjcar.data.adapter.TicketList_Adapter;
import com.gjcar.data.bean.TicketInfo;
import com.gjcar.utils.HandlerHelper;
import com.gjcar.utils.HttpHelper;
import com.gjcar.view.helper.LoadAnimateHelper;

public class ActivityTest extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		initDialog();
	}
	
	/**
	 * 弹出活动对话框
	 */
	private void initActivityDialog(ArrayList<TicketInfo> ticketlist) {
		
		final Dialog updateDialog = new Dialog(ActivityTest.this, R.style.scorechange_dialog);
		
		View view = View.inflate(ActivityTest.this, R.layout.dialog_ticket_useinorder, null);
		
		/*初始化控件*/
		final ListView mylistview = (ListView)view.findViewById(R.id.listview);
		LinearLayout lin = (LinearLayout)view.findViewById(R.id.activity);
		TextView cancle = (TextView)view.findViewById(R.id.cancle);
		TextView ok = (TextView)view.findViewById(R.id.ok);

		/*设置事件*/		
		class MyOnClickListener implements OnClickListener{

			@Override
			public void onClick(View view) {
				
				switch (view.getId()) {
					
					case R.id.ok:
						updateDialog.dismiss();
						break;
				
					case R.id.cancle:
						updateDialog.dismiss();
						break;
						
					default:
						break;
				}
			}
			
		}
		MyOnClickListener onClickListener = new MyOnClickListener();
		
		ok.setOnClickListener(onClickListener);
		cancle.setOnClickListener(onClickListener);
		
		/*对话框:宽度设置*/		
		WindowManager windowManager = getWindowManager();  
		Display display = windowManager.getDefaultDisplay();  
		WindowManager.LayoutParams lp = updateDialog.getWindow().getAttributes();  
		
		final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources()
	                .getDisplayMetrics());//旁边的margin
		
		lp.width = (int)(display.getWidth()-pageMargin); //设置宽度  
		updateDialog.getWindow().setAttributes(lp); 
		
		/*对话框:属性设置*/	
		updateDialog.getWindow().setContentView(view);
		updateDialog.setCancelable(false);
		updateDialog.setCanceledOnTouchOutside(false);
		
		/*对话框:显示*/	
		updateDialog.show();
		
		/*显示数据*/
		mylistview.setVisibility(View.VISIBLE);
		TicketList_Adapter adapter = new TicketList_Adapter(ActivityTest.this, ticketlist);
		mylistview.setAdapter(adapter);
	}
	
	/**
	 * 弹出对话框
	 */
	private void initDialog() {
		
		final Dialog updateDialog = new Dialog(ActivityTest.this, R.style.scorechange_dialog);
		
		View view = View.inflate(ActivityTest.this, R.layout.dialog_ticket_useinorder, null);
		
		/*初始化控件*/
		final ListView mylistview = (ListView)view.findViewById(R.id.listview);
		LinearLayout lin = (LinearLayout)view.findViewById(R.id.activity);
		TextView cancle = (TextView)view.findViewById(R.id.cancle);
		TextView ok = (TextView)view.findViewById(R.id.ok);

		/*设置事件*/		
		class MyOnClickListener implements OnClickListener{

			@Override
			public void onClick(View view) {
				
				switch (view.getId()) {
					
					case R.id.ok:
						updateDialog.dismiss();
						break;
				
					case R.id.cancle:
						updateDialog.dismiss();
						break;
						
					default:
						break;
				}
			}
			
		}
		MyOnClickListener onClickListener = new MyOnClickListener();
		
		ok.setOnClickListener(onClickListener);
		cancle.setOnClickListener(onClickListener);
		
		/*对话框:宽度设置*/		
		WindowManager windowManager = getWindowManager();  
		Display display = windowManager.getDefaultDisplay();  
		WindowManager.LayoutParams lp = updateDialog.getWindow().getAttributes();  
		
		final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources()
	                .getDisplayMetrics());//旁边的margin
		
		lp.width = (int)(display.getWidth()-pageMargin); //设置宽度  
		updateDialog.getWindow().setAttributes(lp); 
		
		/*对话框:属性设置*/	
		updateDialog.getWindow().setContentView(view);
		updateDialog.setCancelable(false);
		updateDialog.setCanceledOnTouchOutside(false);
		
		/*对话框:显示*/	
		updateDialog.show();

		/*对话框:handler*/	
		Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);

					switch (msg.what) {

						case 101:
							
							if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
								LoadAnimateHelper.load_success_animation();

								mylistview.setVisibility(View.VISIBLE);
								ArrayList<TicketInfo> ticketlist = (ArrayList<TicketInfo>)msg.obj;
								TicketList_Adapter adapter = new TicketList_Adapter(ActivityTest.this, ticketlist);
								mylistview.setAdapter(adapter);
					           	return;
							}
							mylistview.setAdapter(null);
							if(HandlerHelper.getString(msg).equals(HandlerHelper.Empty)){
								LoadAnimateHelper.load_empty_animation();
					           	return;
							}
							LoadAnimateHelper.load_fail_animation();
							System.out.println("请求失败");	 
																									
							break;

					}
				}
		};
		
		/*请求数据*/
		String api = "api/me/coupon?consume=206&currentPage=1&pageSize=5&state=2&uid=49984&willBeUseTimeBegin=2016-08-27+10:00:00&willBeUseTimeEnd=2016-08-29+10:00:00";
		new HttpHelper().initData(HttpHelper.Method_Get, this, api, null, null, handler, 101, 1, new TypeReference<ArrayList<TicketInfo>>() {});
		
		/*加载动画*/
		LoadAnimateHelper.Search_Animate_Dialog(this, lin, handler, 10, false,true,0);
	}
}



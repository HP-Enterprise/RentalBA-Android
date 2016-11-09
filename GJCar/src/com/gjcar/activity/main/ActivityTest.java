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
	 * ������Ի���
	 */
	private void initActivityDialog(ArrayList<TicketInfo> ticketlist) {
		
		final Dialog updateDialog = new Dialog(ActivityTest.this, R.style.scorechange_dialog);
		
		View view = View.inflate(ActivityTest.this, R.layout.dialog_ticket_useinorder, null);
		
		/*��ʼ���ؼ�*/
		final ListView mylistview = (ListView)view.findViewById(R.id.listview);
		LinearLayout lin = (LinearLayout)view.findViewById(R.id.activity);
		TextView cancle = (TextView)view.findViewById(R.id.cancle);
		TextView ok = (TextView)view.findViewById(R.id.ok);

		/*�����¼�*/		
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
		
		/*�Ի���:�������*/		
		WindowManager windowManager = getWindowManager();  
		Display display = windowManager.getDefaultDisplay();  
		WindowManager.LayoutParams lp = updateDialog.getWindow().getAttributes();  
		
		final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources()
	                .getDisplayMetrics());//�Աߵ�margin
		
		lp.width = (int)(display.getWidth()-pageMargin); //���ÿ��  
		updateDialog.getWindow().setAttributes(lp); 
		
		/*�Ի���:��������*/	
		updateDialog.getWindow().setContentView(view);
		updateDialog.setCancelable(false);
		updateDialog.setCanceledOnTouchOutside(false);
		
		/*�Ի���:��ʾ*/	
		updateDialog.show();
		
		/*��ʾ����*/
		mylistview.setVisibility(View.VISIBLE);
		TicketList_Adapter adapter = new TicketList_Adapter(ActivityTest.this, ticketlist);
		mylistview.setAdapter(adapter);
	}
	
	/**
	 * �����Ի���
	 */
	private void initDialog() {
		
		final Dialog updateDialog = new Dialog(ActivityTest.this, R.style.scorechange_dialog);
		
		View view = View.inflate(ActivityTest.this, R.layout.dialog_ticket_useinorder, null);
		
		/*��ʼ���ؼ�*/
		final ListView mylistview = (ListView)view.findViewById(R.id.listview);
		LinearLayout lin = (LinearLayout)view.findViewById(R.id.activity);
		TextView cancle = (TextView)view.findViewById(R.id.cancle);
		TextView ok = (TextView)view.findViewById(R.id.ok);

		/*�����¼�*/		
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
		
		/*�Ի���:�������*/		
		WindowManager windowManager = getWindowManager();  
		Display display = windowManager.getDefaultDisplay();  
		WindowManager.LayoutParams lp = updateDialog.getWindow().getAttributes();  
		
		final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources()
	                .getDisplayMetrics());//�Աߵ�margin
		
		lp.width = (int)(display.getWidth()-pageMargin); //���ÿ��  
		updateDialog.getWindow().setAttributes(lp); 
		
		/*�Ի���:��������*/	
		updateDialog.getWindow().setContentView(view);
		updateDialog.setCancelable(false);
		updateDialog.setCanceledOnTouchOutside(false);
		
		/*�Ի���:��ʾ*/	
		updateDialog.show();

		/*�Ի���:handler*/	
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
							System.out.println("����ʧ��");	 
																									
							break;

					}
				}
		};
		
		/*��������*/
		String api = "api/me/coupon?consume=206&currentPage=1&pageSize=5&state=2&uid=49984&willBeUseTimeBegin=2016-08-27+10:00:00&willBeUseTimeEnd=2016-08-29+10:00:00";
		new HttpHelper().initData(HttpHelper.Method_Get, this, api, null, null, handler, 101, 1, new TypeReference<ArrayList<TicketInfo>>() {});
		
		/*���ض���*/
		LoadAnimateHelper.Search_Animate_Dialog(this, lin, handler, 10, false,true,0);
	}
}



package com.gjcar.data.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gjcar.activity.main.ActivityTest;
import com.gjcar.app.R;
import com.gjcar.data.adapter.CarList_Activity_Adapter;
import com.gjcar.data.adapter.OrderSubmit_TicketList_Adapter;
import com.gjcar.data.adapter.TicketList_Adapter;
import com.gjcar.data.bean.ActivityShow;
import com.gjcar.data.bean.TicketInfo;
import com.gjcar.data.data.Public_Param;
import com.gjcar.utils.HandlerHelper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class OrderSubmit_Helper {

	public static int ticket_position = -1;
	public static int activit_position = -1;
	
	public ArrayList<Map<String,Object>> getServiceAmout(int days){
		
		ArrayList<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		
		for (int i = 0; i < Public_Param.order_paramas.all_list.size(); i++) {
			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("description", Public_Param.order_paramas.all_list.get(i).chargeName);
			
			if(Public_Param.order_paramas.all_list.get(i).chargeName.equals("不计免赔")){
				
				if(days > 7){
					map.put("serviceAmount", Public_Param.order_paramas.all_list.get(i).details.get(0).price.intValue()*7);
				}else{
					map.put("serviceAmount", Public_Param.order_paramas.all_list.get(i).details.get(0).price.intValue()*days);
				}
				
			}else{
				map.put("serviceAmount", Public_Param.order_paramas.all_list.get(i).details.get(0).price);
			}
			
			map.put("serviceId", Public_Param.order_paramas.all_list.get(i).chargeId);

			System.out.println("费用名称："+Public_Param.order_paramas.all_list.get(i).chargeName);
			System.out.println("价格："+Public_Param.order_paramas.all_list.get(i).details.get(0).price);
			list.add(map);
		}
		
		return list;
	}
	
	/**
	 * 弹出活动对话框
	 */
	public void initActivityDialog(final Context context,List<ActivityShow> mylist,final Handler handler, final int what) {
		
		activit_position = -1;
		
		final Dialog updateDialog = new Dialog(context, R.style.scorechange_dialog);
		
		View view = View.inflate(context, R.layout.dialog_carlist_activity, null);
		
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
						
						if(activit_position != -1){
							HandlerHelper.sendInt(handler, what, activit_position);
							updateDialog.dismiss();
						}else{
							updateDialog.dismiss();
						}											
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
		WindowManager windowManager = ((Activity) context).getWindowManager();  
		Display display = windowManager.getDefaultDisplay();  
		WindowManager.LayoutParams lp = updateDialog.getWindow().getAttributes();  
		
		final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources()
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
		CarList_Activity_Adapter adapter = new CarList_Activity_Adapter(context, mylist);
		mylistview.setAdapter(adapter);
		
		/*点击事件*/
		mylistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				activit_position = position;
				//view.setBackgroundResource(context.getResources().getColor(R.color.page_text_select));
			}
		});
			
	}
	
	/**
	 * 弹出活动对话框
	 */
	public void initTicketDialog(final Context context,ArrayList<TicketInfo> list,final Handler handler, final int what) {
		
		ticket_position = -1;
		
		final Dialog updateDialog = new Dialog(context, R.style.scorechange_dialog);
		
		View view = View.inflate(context, R.layout.dialog_carlist_activity, null);
		
		/*初始化控件*/
		final ListView mylistview = (ListView)view.findViewById(R.id.listview);
		TextView name = (TextView)view.findViewById(R.id.activity_name_title);
		name.setText("优惠券");
		TextView cancle = (TextView)view.findViewById(R.id.cancle);
		TextView ok = (TextView)view.findViewById(R.id.ok);

		/*设置事件*/		
		class MyOnClickListener implements OnClickListener{

			@Override
			public void onClick(View view) {
				
				switch (view.getId()) {
					
					case R.id.ok:
						if(ticket_position != -1){
							HandlerHelper.sendInt(handler, what, ticket_position);
							updateDialog.dismiss();
						}else{
							updateDialog.dismiss();
						}
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
		WindowManager windowManager = ((Activity) context).getWindowManager();  
		Display display = windowManager.getDefaultDisplay();  
		WindowManager.LayoutParams lp = updateDialog.getWindow().getAttributes();  
		
		final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources()
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
		OrderSubmit_TicketList_Adapter adapter = new OrderSubmit_TicketList_Adapter(context, list);
		mylistview.setAdapter(adapter);
		
		/*点击事件*/
		mylistview.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				System.out.println("ticketxxxxxxxxxxxxxposition"+position);
				ticket_position = position;
				//view.setBackgroundResource(context.getResources().getColor(R.color.page_text_select));
			}
		});
	}
}

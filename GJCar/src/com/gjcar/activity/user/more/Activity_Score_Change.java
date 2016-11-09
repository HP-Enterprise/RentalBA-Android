package com.gjcar.activity.user.more;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.entity.StringEntity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.gjcar.activity.fragment4.Activity_LongRental_Content;
import com.gjcar.annotation.ContentView;
import com.gjcar.annotation.ContentWidget;
import com.gjcar.app.R;
import com.gjcar.data.adapter.CarList_Adapter;
import com.gjcar.data.adapter.FreeRide_List_Adapter;
import com.gjcar.data.adapter.ScoreChangeList_Adapter;
import com.gjcar.data.adapter.TicketList_Adapter;
import com.gjcar.data.bean.FreeRide;
import com.gjcar.data.bean.Model____Vendor_Store_Price;
import com.gjcar.data.bean.ScoreChange;
import com.gjcar.data.bean.TicketInfo;
import com.gjcar.data.bean.TradeAreaShow;
import com.gjcar.data.data.Public_BaiduTJ;
import com.gjcar.data.data.Public_Param;
import com.gjcar.data.data.Public_Data;
import com.gjcar.data.service.CarList_Helper;
import com.gjcar.data.service.ScoreChangeHelper;
import com.gjcar.data.service.TicketList_Helper;
import com.gjcar.framwork.TencentMapHelper;
import com.gjcar.utils.AnnotationViewUtils;
import com.gjcar.utils.HandlerHelper;
import com.gjcar.utils.HttpHelper;
import com.gjcar.utils.IntentHelper;
import com.gjcar.utils.SharedPreferenceHelper;
import com.gjcar.utils.TimeHelper;
import com.gjcar.utils.ToastHelper;
import com.gjcar.view.dialog.SubmitDialog;
import com.gjcar.view.helper.ListViewHelper;
import com.gjcar.view.helper.LoadAnimateHelper;
import com.gjcar.view.helper.PageIndicatorHelper;
import com.gjcar.view.helper.TitleBarHelper;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

@ContentView(R.layout.activity_score_chagne)
public class Activity_Score_Change extends Activity{
	
	@ContentWidget(id = R.id.listview) ListView listview;
	
	/*Handler*/
	private Handler handler;
	private final static int List_Data = 1;
	private final static int Request_Submit = 2;
	
	/*数据*/
	private List<ScoreChange> list;
	private int remain = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		AnnotationViewUtils.injectObject(this, this);
		
		/*handler*/
		initHandler();
		
		/*标题*/
		TitleBarHelper.Back(this, "积分兑换", 0);
		
		/*获取分数*/
		initScore();
		
		/*加载动画*/
		LoadAnimateHelper.Search_Animate(this, R.id.activity, handler, 0, false,false,1);
		
		/*初始化数据*/
		initData();
		
		/*Listener*/
		initListener();
	}

	@Override
	protected void onResume() {
		
		super.onResume();
		Public_BaiduTJ.pageStart(this, Public_BaiduTJ.Activity_Score_Change);	

	}
	
	@Override
	protected void onPause() {

		super.onPause();
		Public_BaiduTJ.pageEnd(this, Public_BaiduTJ.Activity_Score_Change);	
	}
	
	private void initScore(){
		if(getIntent().hasExtra("remain")){
			remain = getIntent().getIntExtra("remain", 0);
		}
	}
		
	private void initData() {System.out.println("1");
	
		/*获取列表*/
		LoadAnimateHelper.start_animation();
		String api = "api/me/couponType?currentPage=1&pageSize=20";
					  
		new TicketList_Helper().initDataList(HttpHelper.Method_Get, this, api, null, null, handler, List_Data, 1, new TypeReference<ArrayList<ScoreChange>>() {});
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
							list = (ArrayList<ScoreChange>)msg.obj;
							list = ScoreChangeHelper.getList(list);//获取applySource=2的优惠券
							ScoreChangeList_Adapter adapter = new ScoreChangeList_Adapter(Activity_Score_Change.this, list);
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

					case Request_Submit:
						SubmitDialog.closeSubmitDialog();
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
							finish();
							ToastHelper.showToastShort(Activity_Score_Change.this,"兑换成功");
				           	return;
						}
						
						ToastHelper.showToastShort(Activity_Score_Change.this,"兑换失败");
						break;
						
					default:
						break;
				}
			}
		};
	}
	
	private void initListener(){
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				
				if(list.get(position).remainNum.intValue() > 0){
					initDialog(position);
				}else{
					ToastHelper.showToastShort(Activity_Score_Change.this, "抱歉，没有剩余的兑换券了");
				}
				
			}
			
		});
	}
	
	private void initDialog(final int index) {

		final Dialog updateDialog = new Dialog(Activity_Score_Change.this, R.style.scorechange_dialog);
		
		View view = View.inflate(Activity_Score_Change.this, R.layout.dialog_scorechange, null);

		/*初始化控件*/
		TextView remainNum = (TextView)view.findViewById(R.id.remainNum);
		TextView title = (TextView)view.findViewById(R.id.title);
		TextView accumulate = (TextView)view.findViewById(R.id.accumulate);
		TextView add = (TextView)view.findViewById(R.id.add);
		final EditText number = (EditText)view.findViewById(R.id.number);
		TextView notadd = (TextView)view.findViewById(R.id.notadd);
		TextView cancle = (TextView)view.findViewById(R.id.cancle);
		TextView ok = (TextView)view.findViewById(R.id.ok);
		
		/*初始化*/
		remainNum.setText("剩余:"+list.get(index).remainNum.toString()+"张");		
		title.setText(list.get(index).title);
		accumulate.setText("需消耗"+list.get(index).accumulate.toString()+"积分");
		
		/*设置事件*/		
		class MyOnClickListener implements OnClickListener{

			@Override
			public void onClick(View view) {
				
				switch (view.getId()) {
					
					case R.id.ok:
																		
						if(number.getText().toString() == null || number.getText().toString().equals("") || number.getText().toString().equals("0")){
							ToastHelper.showToastShort(Activity_Score_Change.this, "请输入数量");
							return;		
						}
						if(Integer.parseInt(number.getText().toString()) > list.get(index).remainNum.intValue()){
							ToastHelper.showToastShort(Activity_Score_Change.this, "输入的数量不能超过剩余数量");
							return;	
						}
						if(Integer.parseInt(number.getText().toString()) * Integer.parseInt(list.get(index).accumulate.toString()) > remain){
							ToastHelper.showToastShort(Activity_Score_Change.this, "抱歉，积分不够");
							return;	
						}						
						updateDialog.dismiss();
						
						SubmitDialog.showSubmitDialog(Activity_Score_Change.this);
						/*提交*/System.out.println("数量:"+number.getText().toString()+"类型id:"+list.get(index).id.toString());
						new HttpHelper().initData(HttpHelper.Method_Post, Activity_Score_Change.this, "api/me/coupon/exchange?num="+number.getText().toString()+"&typeId="+list.get(index).id.toString(), null, null, handler, Request_Submit, 2, null);	
						
						break;
				
					case R.id.cancle:
						updateDialog.dismiss();
						break;

					case R.id.add://不能超过了剩余张数
						
						if(number.getText().toString() == null || number.getText().toString().equals("") || number.getText().toString().equals("0")){
							number.setText("1");
							return;		
						}
						
						if(Integer.parseInt(number.getText().toString()) < list.get(index).remainNum.intValue()){
							number.setText(""+(Integer.parseInt(number.getText().toString())+1));
						}
						break;
						
					case R.id.notadd:
						if(number.getText().toString() == null || number.getText().toString().equals("") || number.getText().toString().equals("0")){
							number.setText("1");
							return;		
						}
						
						if(Integer.parseInt(number.getText().toString()) > 1){
							number.setText(""+(Integer.parseInt(number.getText().toString())-1));
						}
						break;
						
					default:
						break;
				}
			}
			
		}
		MyOnClickListener onClickListener = new MyOnClickListener();
		
		ok.setOnClickListener(onClickListener);
		cancle.setOnClickListener(onClickListener);
		add.setOnClickListener(onClickListener);
		notadd.setOnClickListener(onClickListener);
		
		/*对话框的设置*/
		updateDialog.getWindow().setContentView(view);
		updateDialog.setCancelable(false);
		updateDialog.setCanceledOnTouchOutside(false);
		updateDialog.show();

		WindowManager windowManager = getWindowManager();  
		Display display = windowManager.getDefaultDisplay();  
		WindowManager.LayoutParams lp = updateDialog.getWindow().getAttributes();  
		
		 final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources()
	                .getDisplayMetrics());//旁边的margin
		
		lp.width = (int)(display.getWidth()-pageMargin); //设置宽度  
		updateDialog.getWindow().setAttributes(lp); 
	}
	
	
}

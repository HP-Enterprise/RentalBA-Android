package com.gjcar.activity.user.more;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.alibaba.fastjson.TypeReference;
import com.gjcar.activity.user.ValidationHelper;
import com.gjcar.annotation.ContentView;
import com.gjcar.annotation.ContentWidget;
import com.gjcar.app.R;
import com.gjcar.data.adapter.TicketList_Adapter;
import com.gjcar.data.bean.TicketInfo;
import com.gjcar.data.data.Public_BaiduTJ;
import com.gjcar.data.service.TicketList_Helper;
import com.gjcar.utils.AnnotationViewUtils;
import com.gjcar.utils.HandlerHelper;
import com.gjcar.utils.HttpHelper;
import com.gjcar.utils.SharedPreferenceHelper;
import com.gjcar.utils.ToastHelper;
import com.gjcar.view.dialog.SubmitDialog;
import com.gjcar.view.helper.LoadAnimateHelper;
import com.gjcar.view.helper.PageIndicatorHelper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
	
	private final static int Http_Post_Receive = 6;
	
	/*数据*/
	private List<TicketInfo> list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		AnnotationViewUtils.injectObject(this, this);
		
		/*handler*/
		initHandler();
		
		/*标题*/
		Back(this, "我的礼券", 0);
		
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
					
					case Http_Post_Receive:
						
						SubmitDialog.closeSubmitDialog();
						
						if (HandlerHelper.getString(msg).equals(HandlerHelper.Ok)) {

							String data = msg.getData().getString("data");
							ToastHelper.showToastShort(Activity_Ticket.this, data);
							
							new PageIndicatorHelper().initIndicator(Activity_Ticket.this, new TextView[]{t_notuse,t_use,t_timeout}, new View[]{l_notuse,l_use,l_timeout}, R.color.page_text_select, R.color.page_text_normal2, handler, Page_Indicator);

							initData("2");
						}else{
							if(HandlerHelper.getString(msg).equals(HandlerHelper.Fail)) {

								String data = msg.getData().getString("data");
								ToastHelper.showToastShort(Activity_Ticket.this, data);
									
							}else{
								
								ToastHelper.showToastShort(Activity_Ticket.this, "兑换失败");
							}
							
						}
						break;	
						
					default:
						break;
				}
			}
		};
	}
	
	private void Http_Post_Receive(String edit_number){

		/*请求*/
		String api = "api/me/coupon/receive"+"?number="+edit_number;
		new HttpHelper().initData(HttpHelper.Method_Post, this, api, null, null, handler, Http_Post_Receive, 2, null);
	}
	
	/**兑换框*/
	private void initDialog() {

		final Dialog updateDialog = new Dialog(Activity_Ticket.this, R.style.scorechange_dialog);
		
		View view = View.inflate(Activity_Ticket.this, R.layout.dialog_ticketcodechange, null);

		/*初始化控件*/

		final EditText number = (EditText)view.findViewById(R.id.number);
		TextView cancle = (TextView)view.findViewById(R.id.cancle);
		TextView ok = (TextView)view.findViewById(R.id.ok);
		
		/*设置事件*/		
		class MyOnClickListener implements OnClickListener{

			@Override
			public void onClick(View view) {
				
				switch (view.getId()) {
					
					case R.id.ok:
																		
						if(ValidationHelper.isNull(number)){
							ToastHelper.showToastShort(Activity_Ticket.this, "请输入优惠码");
							return;		
						}
						
						if(!Pattern.compile("^[A-Z0-9]+$").matcher(number.getText().toString()).find()){
							ToastHelper.showToastShort(Activity_Ticket.this, "只能输入大写字母和数字");
							return;		
						}
												
						/*消失*/
						updateDialog.dismiss();
						
						/*提交*/
						SubmitDialog.showSubmitDialog(Activity_Ticket.this);
												
						Http_Post_Receive(number.getText().toString());
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
	
	public void Back(final Context context, String titleName, int index){
		
		//添加标题
		View titleView = View.inflate(context, R.layout.titlebar_ticketchange, null);

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
				
				initDialog();
			}
		});
		
		TextView title = (TextView)titleView.findViewById(R.id.title);
		title.setText(titleName);
		
	}
}

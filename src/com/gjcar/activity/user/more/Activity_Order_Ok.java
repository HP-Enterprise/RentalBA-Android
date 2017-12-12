package com.gjcar.activity.user.more;

import com.alibaba.fastjson.JSONObject;
import com.gjcar.annotation.ContentView;
import com.gjcar.annotation.ContentWidget;
import com.gjcar.app.R;
import com.gjcar.data.data.Public_BaiduTJ;
import com.gjcar.data.data.Public_Param;
import com.gjcar.fragwork.alipay.AlipayHelper;
import com.gjcar.utils.AnnotationViewUtils;
import com.gjcar.utils.HttpHelper;
import com.gjcar.utils.IntentHelper;
import com.gjcar.view.helper.TitleBarHelper;
import com.gjcar.view.helper.ViewHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@ContentView(R.layout.activity_order_ok)
public class Activity_Order_Ok extends Activity{

	/*��ʼ���ؼ�*/
	@ContentWidget(id = R.id.iv_second) TextView iv_second;
	@ContentWidget(id = R.id.iv_model) TextView iv_model;
	@ContentWidget(id = R.id.iv_orderId) TextView iv_orderId;
	@ContentWidget(id = R.id.iv_payway) TextView iv_payway;
	@ContentWidget(id = R.id.iv_acount) TextView iv_acount;

	@ContentWidget(click = "onClick") TextView orderlist;
	@ContentWidget(click = "onClick") Button ok;
	
	@ContentWidget(id = R.id.second_lin) LinearLayout second_lin;
	@ContentWidget(id = R.id.pay_line) LinearLayout pay_line;
	
	/*Handler*/
	private Handler handler;
	private int second = 6;
	
	/*����*/
	private boolean isOut = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		AnnotationViewUtils.injectObject(this, this);

		/*���ر�����*/
		TitleBarHelper.Back(this, "ȷ�϶���", 0);

		initData();

		initHandler();
	}

	@Override
	protected void onResume() {
		
		super.onResume();
		Public_BaiduTJ.pageStart(this, Public_BaiduTJ.Activity_Order_Ok);	
	}
	
	@Override
	protected void onPause() {

		super.onPause();
		Public_BaiduTJ.pageEnd(this, Public_BaiduTJ.Activity_Order_Ok);	
	}
	
	public void onClick(View view){

		switch (view.getId()) {
		
			case R.id.orderlist:
				finish();
				Intent intent = new Intent(Activity_Order_Ok.this, Activity_Order_List.class);//��ת				
				intent.putExtra("way", getIntent().getStringExtra("way"));
				startActivity(intent);
				
				break;

			case R.id.ok:
				new AlipayHelper().pay(this, handler, "�Ͻ��⳵", getIntent().getStringExtra("model"), getIntent().getStringExtra("acount"),getIntent().getStringExtra("orderId"));
				break;
				
			default:
				break;
		}
		
	}

	private void initHandler() {
		
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				
				switch (msg.what) {
					case AlipayHelper.Pay_Ok:
						System.out.println("֧���ɹ�");
						finish();
						Intent intent = new Intent(Activity_Order_Ok.this, Activity_Order_List.class);//��ת				
						intent.putExtra("way", getIntent().getStringExtra("way"));
						startActivity(intent);
						break;
	
					default:
						break;
				}
				
			}
		};
		
		/*���������֧�����Ͳ�����ʱ��*/
		if(getIntent().getStringExtra("payWay").equals("3")){
			return;
		}
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				second = second -1;
				iv_second.setText(new Integer(second).toString());
				if(second > 0 && !isOut){
					iv_second.postDelayed(this, 1000);
				}else{
	
					if(!isOut){
						finish();
						Intent intent = new Intent(Activity_Order_Ok.this, Activity_Order_List.class);//��ת				
						intent.putExtra("way", getIntent().getStringExtra("way"));
						startActivity(intent);			
					}
					
				}
				
			}
		},1000);
		
	}

	@SuppressLint("UseValueOf")
	private void initData() {
		
		Intent intent = getIntent();

		String model = intent.getStringExtra("model");System.out.println("f5"+model);
		String days = intent.getStringExtra("days");System.out.println("f6"+days);
		String orderId = intent.getStringExtra("orderId");System.out.println("f7"+orderId);
		String acount = intent.getStringExtra("acount");System.out.println("f8"+acount);
		String payWay = intent.getStringExtra("payWay");System.out.println("f9"+payWay);
		
		iv_second.setText(new Integer(second).toString());
		iv_model.setText(model+"������Ϊ"+days+"��");
		iv_orderId.setText("�����ţ�"+orderId);System.out.println("f10");
		if(payWay.equals("3")){
			iv_payway.setText("֧����ʽ��"+"����֧��");
		}else{
			iv_payway.setText("֧����ʽ��"+"�ŵ�֧��");
		}
		
		iv_acount.setText("�����ܼۣ���"+acount);
		
		if(!payWay.equals("3")){
			ViewHelper.gone(new View[]{pay_line,ok});System.out.println("f11");
			ViewHelper.show(new View[]{second_lin});
		}else{
			ViewHelper.show(new View[]{pay_line,ok});System.out.println("f12");
			ViewHelper.gone(new View[]{second_lin});
		}
//		ViewHelper.gone(new View[]{pay_line,ok});
//		ViewHelper.show(new View[]{second_lin});		
	}
	
	@Override
	protected void onDestroy() {System.out.println("isout"+isOut);
		isOut = true;
		super.onDestroy();
	}
	
}

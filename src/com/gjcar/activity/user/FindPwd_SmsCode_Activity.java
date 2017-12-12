package com.gjcar.activity.user;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.gjcar.app.R;
import com.gjcar.data.data.Public_Api;
import com.gjcar.data.data.Public_Param;
import com.gjcar.utils.NetworkHelper;
import com.gjcar.utils.StringHelper;
import com.gjcar.utils.ToastHelper;
import com.gjcar.view.dialog.SubmitDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 *1.�����ֻ���
 *2.�����ֻ��ź���֤��
 */
public class FindPwd_SmsCode_Activity extends Activity implements OnClickListener{

	/*��ʼ���ؼ�*/
	private EditText find_phone;
	private ImageView find_phone_delete;

	private EditText find_code;
	private ImageView find_code_delete;
	private TextView find_code_get;

	private Button find_next;
	
	/*Handler*/
	private Handler handler;
	
	private final static int checkPhoneOk = 1;//���Ͷ��ųɹ�
	private final static int checkPhoneFail = 2;//���Ͷ���ʧ��
	private final static int checkPhoneDataFail = 3;//���Ͷ�������ʧ��
	
	private final static int getCodeOk = 4;//���Ͷ��ųɹ�
	private final static int getCodeFail = 5;//���Ͷ���ʧ��
	private final static int getCodeDataFail = 6;//���Ͷ�������ʧ�ܰ�
	 
	private final static int verifyCodeOk = 7;//���Ͷ��ųɹ�
	private final static int verifyCodeFail = 8;//���Ͷ���ʧ��
	private final static int verifyCodeDataFail = 9;//���Ͷ�������ʧ�ܰ�
	
	/*������֤��*/
	private int time = 120;

	private static boolean isGetSms = false;
	
	private Uri uri;
	private ContentResolver resolver;
	
	/*����*/
	private String errorMsg = "";
	private String phone = "";//��ȡ���ųɹ��󣬼�ס�ֻ���
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_findpassword);
//		LocationApplication.getInstance().addActivity(this);
		Public_Param.list_findpwd_activity.clear();
		Public_Param.list_findpwd_activity.add(this);
		
		initSmsConfig();
		
		initTitleBar();

		initView();
		
		initListener();
		
		initHander();
	}
	
	/**
	 * ��ʼ���ż���
	 */
	private void initSmsConfig() {

		uri = Uri.parse("content://sms");
        resolver = getContentResolver();        
        resolver.registerContentObserver(uri , true, new SmsObserver());
		
	}
	
	/**
	 * ��ʼ��������
	 */
	private void initTitleBar() {

		ImageView iv = (ImageView)findViewById(R.id.iv_back);
		iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
				
			}
		});

	}
	
	private void initView() {
				
		find_phone = (EditText)findViewById(R.id.find_phone);		 
		find_phone_delete = (ImageView)findViewById(R.id.find_phone_delete);
		 
		find_code = (EditText)findViewById(R.id.find_code);
		find_code_delete = (ImageView)findViewById(R.id.find_code_delete);		
		find_code_get = (TextView)findViewById(R.id.find_code_get);

		find_next = (Button)findViewById(R.id.find_next);
	}

	private void initListener() {

		/*��������¼�*/
		find_phone_delete.setOnClickListener(this);
		
		find_code_delete.setOnClickListener(this);
		find_code_get.setOnClickListener(this);
		
		find_next.setOnClickListener(this);
		
		/*�����ı��¼�*/
		find_phone.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {

				if(s.length() == 0){
					find_phone_delete.setVisibility(View.GONE);
				}else{
					if(find_phone.isFocused()){
						find_phone_delete.setVisibility(View.VISIBLE);
					}else{
						find_phone_delete.setVisibility(View.GONE);
					}
				}
				
				if(isGetSms){
					isGetSms = false;
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {

				
			}
		});
		
		find_phone.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_UP){
					
					find_code_delete.setVisibility(View.GONE);
					if(find_phone.getText().toString().length() > 0){
						find_phone.setVisibility(View.VISIBLE);
					}
				}
				return false;
			}
						
		});
	
		find_code.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				
				if(s.length() == 0){
					find_code_delete.setVisibility(View.GONE);
				}else{
					
					if(find_code.isFocused()){
						find_code_delete.setVisibility(View.VISIBLE);
					}else{
						find_code_delete.setVisibility(View.GONE);
					}
					find_code_delete.setVisibility(View.VISIBLE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				
			}
		});
		
		find_code.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_UP){
					
					find_phone_delete.setVisibility(View.GONE);
					if(find_code.getText().toString().length() > 0){
						find_code.setVisibility(View.VISIBLE);
					}
				}
				return false;
			}
						
		});
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		
			case R.id.find_phone_delete:
				find_phone.setText("");
				break;
	
			case R.id.find_code_delete:
				find_code.setText("");
				break;
				
			case R.id.find_code_get:
				
				checkPhoneMessage();
				if(errorMsg.equals("")){				
					
					System.out.println("aaaa");
							
					if(NetworkHelper.isNetworkAvailable(this)){
												
						verifyPhone();
						
					}
					
															
				}else{
					Toast.makeText(FindPwd_SmsCode_Activity.this, errorMsg, Toast.LENGTH_LONG).show();
					errorMsg = "";
				}
				
				break;
			
			case R.id.find_next:
							
				checkCodeMessage();
				if(errorMsg.equals("")){				
					
					//������֤�뵽�ֻ�
					if(NetworkHelper.isNetworkAvailable(this)){
						
						SubmitDialog.showSubmitDialog(this);
						
						new Thread(){
							
							public void run() {
								verifyCode();
							};
						}.start();
					}
					
				}else{
					Toast.makeText(FindPwd_SmsCode_Activity.this, errorMsg, Toast.LENGTH_LONG).show();
					errorMsg = "";
				}
				break;
				
			default:
				break;
		}
	}
	
	private void initHander() {
		
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				
				switch (msg.what) {

					case checkPhoneOk:
						//������֤�뵽�ֻ�
						if(NetworkHelper.isNetworkAvailable(FindPwd_SmsCode_Activity.this)){System.out.println("bbbb");
						  
							new Thread(){
								   public void run() {
									   getCode();
								   };
							}.start();
							
						}
						break;
						
					case checkPhoneFail:
						break;
						
					case checkPhoneDataFail:
						ToastHelper.showSendDataFailToast(FindPwd_SmsCode_Activity.this);
						break;
				
					case getCodeOk:
						phone = find_phone.getText().toString();
						isGetSms = true;							
						handler.post(myRunnable);
						break;
						
					case getCodeFail://�˴���Ϊ�ظ����ͣ�����λ�ò��䣬����Ҫ��ʾ
						ToastHelper.showToastLong(FindPwd_SmsCode_Activity.this,msg.getData().getString("errorMsg"));//��ʾ����ʧ�ܵ�ԭ��
						break;
												
					case getCodeDataFail:
						find_code_get.setText("���»�ȡ");
						break;
					
					case verifyCodeOk:
						SubmitDialog.closeSubmitDialog();
						next();
						break;
					
					case verifyCodeFail:
						SubmitDialog.closeSubmitDialog();
						ToastHelper.showToastLong(FindPwd_SmsCode_Activity.this,"��֤�����");//��ʾ����ʧ�ܵ�ԭ��
						break;
												
					case verifyCodeDataFail:
						SubmitDialog.closeSubmitDialog();
						ToastHelper.showSendDataFailToast(FindPwd_SmsCode_Activity.this);
						break;	
						
					default:
						break;
				}
				
				
			}
		};
		
	}
	
	/** ��ʾʱ��*/
	Runnable myRunnable = new Runnable(){

		@Override
		public void run() {
			
			if(time >=0 && isGetSms == true){
				find_code_get.setText(time+"��");				
				time = time -1;
				handler.postDelayed(myRunnable, 1000);
				find_code_get.setClickable(false);
			}else{
				if(time < 0){
					//100����û�л�ȡ��
					find_code_get.setText("���»�ȡ");
					isGetSms = false;
					time = 120;
					find_code_get.setClickable(true);
				}else{
					//��ȡ���ųɹ�
					find_code_get.setText("���»�ȡ");
					isGetSms = false;
					time = 120;
					find_code_get.setClickable(true);
				}				
			}
		}
		
	};

	/**
	 * ����ֻ����Ƿ���ȷ
	 */
	private void checkPhoneMessage(){
		
		if(find_phone.getText().toString().equals("") || find_phone.getText().toString() == null){
			errorMsg = errorMsg +"�ֻ��Ų���Ϊ��";		
			System.out.println("�ֻ��Ų���Ϊ��"+find_phone.getText().toString());
		}else{
			if(!find_phone.getText().toString().matches("^[1][358][0-9]{9}$") || find_phone.getText().toString().length() != 11){
				errorMsg = errorMsg +"�ֻ��Ÿ�ʽ����ȷ";		
				System.out.println("�ֻ��Ÿ�ʽ����ȷ"+find_phone.getText().toString());
			}
		}
	
	}
	
	/**
	 * �����֤���Ƿ���ȷ:������
	 */
	private void checkCodeMessage(){
		
		/*����ֻ���*/
		if(find_phone.getText().toString().equals("") || find_phone.getText().toString() == null){
			errorMsg = errorMsg +"�ֻ��Ų���Ϊ��";		
			System.out.println("�ֻ��Ų���Ϊ��"+find_phone.getText().toString());
		}else{
			
			if(!find_phone.getText().toString().matches("^[1][3578][0-9]{9}$")){//�����ж��Ƿ���11λ,��֤���Ѿ���11λ���ж�
				errorMsg = errorMsg +"�ֻ��Ÿ�ʽ����ȷ";		
				System.out.println("�ֻ��Ÿ�ʽ����ȷ"+find_phone.getText().toString());
			}else{
				
				if(!isGetSms){
					errorMsg = errorMsg +"���Ȼ�ȡ��֤��";
				}else{
				
					/*�����֤��*/
					if(find_code.getText().toString().equals("") || find_code.getText().toString() == null){
						errorMsg = errorMsg +"��֤�벻��Ϊ��";		
						System.out.println("��֤�벻��Ϊ��"+find_phone.getText().toString());
					}else{
						if(!find_code.getText().toString().matches("^[0-9]{6}$")){//��֤���Ƿ�Ϊ4λ
							errorMsg = errorMsg +"��֤�벻��ȷ";		
							System.out.println("��֤���ʽ����ȷ"+find_phone.getText().toString());
						}
					}
				}
			}
		}
			
	}
	
	/**
	 * �����ֻ���
	 */
	private void verifyPhone(){
		
	/*  ��ȡ��½��Ϣ  */
	String phone = find_phone.getText().toString();
	
	/*  ���������½  */
	AsyncHttpClient httpClient = new AsyncHttpClient();
	httpClient.setTimeout(Integer.parseInt(getResources().getString(R.string.app_connection_timeout)));
				
	RequestParams params = new RequestParams();//�����������

	String url = Public_Api.appWebSite + Public_Api.api_register +"/"+ phone;//���������url	
	httpClient.get(url, params, new AsyncHttpResponseHandler() {
		
		/* ��������ɹ�  */
		@Override
		public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				
			String smsData = new String(arg2);
			System.out.println("dd:"+smsData);
			org.json.JSONObject jobject;
			try {
				
				jobject = new org.json.JSONObject(smsData);
				boolean registered = jobject.getBoolean("registered");
				if(registered){
					
					//ok
					handler.sendEmptyMessage(checkPhoneOk);
					System.out.println("���ͳɹ�");
					
				}else{
					
					//������֤��ʧ��
					handler.sendEmptyMessage(checkPhoneFail);
					ToastHelper.showToastLong(FindPwd_SmsCode_Activity.this, "�ֻ���δע��");//��ʾ����ʧ�ܵ�ԭ��
				}	
				
			} catch (JSONException e) {
				
				e.printStackTrace();
			} catch (org.json.JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			
		}
					
		/* 5.��������ʧ��  */
		@Override
		public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

//			System.out.println("��½������ʧ��");
			arg3.printStackTrace();
			handler.sendEmptyMessage(checkPhoneDataFail);
//			String errors = new String(arg2);
//			Toast.makeText(LoginActivity.this, "ע��ʧ��"+errors, 3000).show();
		}
	});
}
	
	/** ��������*/
	private class SmsObserver extends ContentObserver {
		public SmsObserver() {
			super(new Handler());
		}
    	public void onChange(boolean selfChange) {
    		Cursor c = resolver.query(uri, null, null, null, "_id DESC LIMIT 1");
    		if (c.moveToNext()) {
    			String address = c.getString(c.getColumnIndex("address"));
    			String body = c.getString(c.getColumnIndex("body"));
    			int type = c.getInt(c.getColumnIndex("type"));
    			long date = c.getLong(c.getColumnIndex("date"));
    			System.out.println(new Date(date) + " " + (type == 1 ? "�� " : "�� ") + address + " " + body);
    			System.out.println(body);
    			String[] message = body.split("��");
    			if(message.length >= 2){
    				System.out.println(message[1].trim());
    				phone = find_phone.getText().toString();
    				find_code.setText(StringHelper.getBusNumber(message[1].trim()));//��ȡ������֮�󣬼�ס���룬����next�ɵ��****************************************************
    				find_next.setEnabled(true);
    			}
    			
			}
    	}
    }
	
	/** ������֤��  */
	private void next(){
		Intent intent = new Intent(FindPwd_SmsCode_Activity.this, FindPwd_ResetPwd_Activity.class);
		intent.putExtra("phone", find_phone.getText().toString());
		intent.putExtra("code", find_code.getText().toString());
		startActivity(intent);
		
	}
	
	/**
	 * ��ȡ��֤��
	 */
	private void getCode(){		
		
		/*  ��ȡ��½��Ϣ  */
		String phone = find_phone.getText().toString();

		// ����Ĭ�ϵĿͻ���ʵ��  
		HttpClient httpCLient = new DefaultHttpClient();

		JSONObject jsonObject = new JSONObject(); //**********************ע��json��������ʱ��Ҫ����
		jsonObject.put("target", phone);
		jsonObject.put("channel", "sms");
		jsonObject.put("purpose", "resetpwd");
		System.out.println(""+phone);
		StringEntity requestentity = null;
		try {
			requestentity = new StringEntity(jsonObject.toString(), "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}//���������������    
		requestentity.setContentEncoding("UTF-8");
		requestentity.setContentType("application/json");

		// ����get����ʵ��  
		HttpPost httppost = new HttpPost(Public_Api.appWebSite + Public_Api.api_smsCode);//**********************ע�����󷽷�  

		httppost.setHeader("Content-Type", "application/json;charset=UTF-8");

		httppost.setEntity(requestentity);
		try {

			// �ͻ���ִ��get���� ������Ӧʵ��  
			HttpResponse response = httpCLient.execute(httppost);
			if (response.getStatusLine().getStatusCode() == 200) {//����ɹ�

				// ��ȡ��Ӧ��Ϣʵ��  
				HttpEntity responseentity = response.getEntity();
				String data = EntityUtils.toString(responseentity);

				//�ж���Ӧ��Ϣ
				org.json.JSONObject datajobject;
				boolean status = false;
				String message = "";
				try {
					datajobject = new org.json.JSONObject(data);System.out.println("����ֵ"+datajobject);
					status = datajobject.getBoolean("status");
					message = datajobject.getString("message");
				} catch (org.json.JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("22222"+"message");
				System.out.println("22222");
				if (status) {
					System.out.println("33333");

					//�޸�����ɹ�	
					handler.sendEmptyMessage(getCodeOk);

				} else {
					System.out.println("4444");
					//�޸�����ʧ��

					Message msg = new Message();
					msg.what = getCodeFail;
					Bundle bundle = new Bundle();
					bundle.putString("errorMsg", message);
					msg.setData(bundle);
					handler.sendMessage(msg);

				}

			} else {//����ʧ��
				handler.sendEmptyMessage(getCodeDataFail);
				System.out.println("1sssssssssss");
			}

		} catch (ClientProtocolException e) { //�����쳣  
			e.printStackTrace();
			handler.sendEmptyMessage(getCodeDataFail);
			System.out.println("2sssssssssss");
		} catch (IOException e) { //�����쳣    
			e.printStackTrace();
			handler.sendEmptyMessage(getCodeDataFail);
			System.out.println("3sssssssssss");
		} catch (JSONException e) {
			e.printStackTrace();
			handler.sendEmptyMessage(getCodeDataFail);
			System.out.println("4sssssssssss");
		} finally {
			httpCLient.getConnectionManager().shutdown();
			System.out.println("sssssssssss");
		}
	}
	/**
	 * ��֤��֤��
	 */
	private void verifyCode(){		
		
		/*  ��ȡ��½��Ϣ  */
		String phone = find_phone.getText().toString();
		String code = find_code.getText().toString();
		
		// ����Ĭ�ϵĿͻ���ʵ��  
		HttpClient httpCLient = new DefaultHttpClient();

		JSONObject jsonObject = new JSONObject(); //**********************ע��json��������ʱ��Ҫ����
		jsonObject.put("phone", phone);
		jsonObject.put("code", code);
		
		System.out.println("phone"+phone);
		StringEntity requestentity = null;
		try {
			requestentity = new StringEntity(jsonObject.toString(), "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}//���������������    
		requestentity.setContentEncoding("UTF-8");
		requestentity.setContentType("application/json");

		// ����get����ʵ��  
		HttpPut httpput = new HttpPut(Public_Api.appWebSite + Public_Api.api_smsCode);//**********************ע�����󷽷�  

		httpput.setHeader("Content-Type", "application/json;charset=UTF-8");

		httpput.setEntity(requestentity);
		try {

			// �ͻ���ִ��get���� ������Ӧʵ��  
			HttpResponse response = httpCLient.execute(httpput);
			if (response.getStatusLine().getStatusCode() == 200) {//����ɹ�

				// ��ȡ��Ӧ��Ϣʵ��  
				HttpEntity responseentity = response.getEntity();
				String data = EntityUtils.toString(responseentity);

				//�ж���Ӧ��Ϣ
				org.json.JSONObject datajobject;
				boolean status = false;
				String message = "";
				try {
					datajobject = new org.json.JSONObject(data);
					status = datajobject.getBoolean("status");
					message = datajobject.getString("message");
				} catch (org.json.JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("22222"+"message");
				System.out.println("22222");
				if (status) {
					System.out.println("33333");

					//�޸�����ɹ�	
					handler.sendEmptyMessage(verifyCodeOk);

				} else {
					System.out.println("4444");
					//�޸�����ʧ��

					Message msg = new Message();
					msg.what = verifyCodeFail;
					Bundle bundle = new Bundle();
					bundle.putString("errorMsg", message);
					msg.setData(bundle);
					handler.sendMessage(msg);

				}

			} else {//����ʧ��
				handler.sendEmptyMessage(verifyCodeDataFail);
				System.out.println("1sssssssssss");
			}

		} catch (ClientProtocolException e) { //�����쳣  
			e.printStackTrace();
			handler.sendEmptyMessage(verifyCodeDataFail);
			System.out.println("2sssssssssss");
		} catch (IOException e) { //�����쳣    
			e.printStackTrace();
			handler.sendEmptyMessage(verifyCodeDataFail);
			System.out.println("3sssssssssss");
		} catch (JSONException e) {
			e.printStackTrace();
			handler.sendEmptyMessage(verifyCodeDataFail);
			System.out.println("4sssssssssss");
		} finally {
			httpCLient.getConnectionManager().shutdown();
			System.out.println("sssssssssss");
		}
	}
}

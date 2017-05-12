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
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.gjcar.app.R;
import com.gjcar.data.data.Public_Api;
import com.gjcar.data.data.Public_BaiduTJ;
import com.gjcar.data.data.Public_Param;
import com.gjcar.data.data.Public_Platform;
import com.gjcar.data.helper.Loginhelper;
import com.gjcar.data.service.JSONHelper;
import com.gjcar.utils.HttpHelper;
import com.gjcar.utils.NetworkHelper;
import com.gjcar.utils.SharedPreferenceHelper;
import com.gjcar.utils.StringHelper;
import com.gjcar.utils.SystemUtils;
import com.gjcar.utils.ToastHelper;
import com.gjcar.view.dialog.SubmitDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
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
public class Register_SmsCode_Activiity extends Activity implements OnClickListener{

	/*��ʼ���ؼ�*/
	private EditText code_phone;
	private ImageView code_phone_delete;

	private EditText code_code;
	private ImageView code_code_delete;
	private TextView code_code_get;

	private EditText register_pwd2;//����
	private ImageView register_pwd2_delete;
	private ImageView register_pwd2_show;
	
	private Button code_next;
	
	/*Handler*/
	private Handler handler;
	
	private final static int checkPhoneOk = 1;//���Ͷ��ųɹ�
	private final static int checkPhoneFail = 2;//���Ͷ���ʧ��
	private final static int checkPhoneDataFail = 3;//���Ͷ�������ʧ��
	
	private final static int getCodeOk = 4;//���Ͷ��ųɹ�
	private final static int getCodeFail = 5;//���Ͷ���ʧ��
	private final static int getCodeDataFail = 6;//���Ͷ�������ʧ��
 
	private final static int sendRegisterOk = 7;//�ɹ�
	private final static int sendRegisterFail = 8;//ʧ��
	private final static int sendRegisterDataFail = 9;//����ʧ��
	
	private final static int Request_Submit = 12;
	
	/*������֤��*/
	private int time = 120;

	private static boolean isGetSms = false;
	
	private Uri uri;
	private ContentResolver resolver;
	
	/*����*/
	private String errorMsg = "";
	private String phone = "";//��ȡ���ųɹ��󣬼�ס�ֻ���
	
	private String uid = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_smscode);
//		LocationApplication.getInstance().addActivity(this);
//		Public_Parameter.list_login_activity.clear();
//		Public_Parameter.list_login_activity.add(this);
		
		initSmsConfig();
		
		initTitleBar();
		
		initView();
		
		initListener();
		
		initHander();
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
		Public_BaiduTJ.pageStart(this, Public_BaiduTJ.Activity_Register);	
	
	}

	@Override
	protected void onPause() {

		super.onPause();
		Public_BaiduTJ.pageEnd(this, Public_BaiduTJ.Activity_Register);	
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
				
		code_phone = (EditText)findViewById(R.id.code_phone);		 
		code_phone_delete = (ImageView)findViewById(R.id.code_phone_delete);
		 
		code_code = (EditText)findViewById(R.id.code_code);
		code_code_delete = (ImageView)findViewById(R.id.code_code_delete);		
		code_code_get = (TextView)findViewById(R.id.code_code_get);
		
		register_pwd2 = (EditText)findViewById(R.id.register_pwd2);//����
		register_pwd2_delete = (ImageView)findViewById(R.id.register_pwd2_delete);
		register_pwd2_show = (ImageView)findViewById(R.id.register_pwd2_show);

		code_next = (Button)findViewById(R.id.code_next);
	}

	private void initListener() {

		/*��������¼�*/
		code_phone_delete.setOnClickListener(this);
		
		code_code_delete.setOnClickListener(this);
		code_code_get.setOnClickListener(this);
		
		register_pwd2_delete.setOnClickListener(this);		
		register_pwd2_show.setOnClickListener(this);
		
		code_next.setOnClickListener(this);
		
		/*�����ı��¼�*/
		code_phone.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {

				if(s.length() == 0){
					code_phone_delete.setVisibility(View.GONE);
				}else{
					if(code_phone.isFocused()){
						code_phone_delete.setVisibility(View.VISIBLE);
					}else{
						code_phone_delete.setVisibility(View.GONE);
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
		
		code_phone.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_UP){
					
					code_code_delete.setVisibility(View.GONE);
					register_pwd2_delete.setVisibility(View.GONE);
					
					if(code_phone.getText().toString().length() > 0){
						code_phone.setVisibility(View.VISIBLE);
					}
				}
				return false;
			}
						
		});
	
		code_code.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				
				if(s.length() == 0){
					code_code_delete.setVisibility(View.GONE);
				}else{
					
					if(code_code.isFocused()){
						code_code_delete.setVisibility(View.VISIBLE);
					}else{
						code_code_delete.setVisibility(View.GONE);
					}
					code_code_delete.setVisibility(View.VISIBLE);
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
		
		code_code.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_UP){
					
					code_phone_delete.setVisibility(View.GONE);
					register_pwd2_delete.setVisibility(View.GONE);
					
					if(code_code.getText().toString().length() > 0){
						code_code.setVisibility(View.VISIBLE);
					}
				}
				return false;
			}
						
		});
		
		register_pwd2.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				
				if(s.length() == 0){
					register_pwd2_delete.setVisibility(View.GONE);
				}else{
					register_pwd2_delete.setVisibility(View.VISIBLE);
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
		
		register_pwd2.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_UP){
					
					code_phone_delete.setVisibility(View.GONE);
					code_code_delete.setVisibility(View.GONE);
					
					if(register_pwd2.getText().toString().length() > 0){
						register_pwd2.setVisibility(View.VISIBLE);
					}
				}
				return false;
			}
						
		});
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		
			case R.id.code_phone_delete:
				code_phone.setText("");
				break;
	
			case R.id.code_code_delete:
				code_code.setText("");
				break;
				
			case R.id.register_pwd2_delete:
				register_pwd2.setText("");
				break;
				
			case R.id.register_pwd2_show:
				if(register_pwd2.getInputType() == 0x81){//����
					register_pwd2.setInputType(0x90);
					register_pwd2_show.setImageResource(R.drawable.register_pwd_show);
				}else{//����0x90
					register_pwd2.setInputType(0x81);
					register_pwd2_show.setImageResource(R.drawable.register_pwd_hide);
				}
				break;	
				
			case R.id.code_code_get:
				
				checkPhoneMessage();
				if(errorMsg.equals("")){				
					
					System.out.println("aaaa");
					if(NetworkHelper.isNetworkAvailable(this)){

						verifyPhone();
						
					}else{
						ToastHelper.showNoNetworkToast(this);
					}
					
															
				}else{
					Toast.makeText(Register_SmsCode_Activiity.this, errorMsg, Toast.LENGTH_LONG).show();
					errorMsg = "";
				}
				break;
			
			case R.id.code_next:
							
				checkCodeMessage();
				if(errorMsg.equals("")){				
					
				 	/*�����Ի���*/
					SubmitDialog.showSubmitDialog(this);
					
					//������֤�뵽�ֻ�
					if(NetworkHelper.isNetworkAvailable(this)){

						new Thread(){
							public void run() {
								sendRegister();
							}
						}.start();
						
					}else{
						ToastHelper.showNoNetworkToast(this);
					}
					
				}else{
					Toast.makeText(Register_SmsCode_Activiity.this, errorMsg, Toast.LENGTH_LONG).show();
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
						if(NetworkHelper.isNetworkAvailable(Register_SmsCode_Activiity.this)){System.out.println("bbbb");
						  
							new Thread(){
								   public void run() {
									   getCode();
								   };
							}.start();
							
						}else{System.out.println("ccc");
							ToastHelper.showNoNetworkToast(Register_SmsCode_Activiity.this);
						}
						break;
						
					case checkPhoneFail:
						break;
						
					case checkPhoneDataFail:
						ToastHelper.showSendDataFailToast(Register_SmsCode_Activiity.this);
						break;
				
					case getCodeOk:
						phone = code_phone.getText().toString();
						isGetSms = true;							
						handler.post(myRunnable);
						break;
						
					case getCodeFail:
						//code_code_get.setText("���»�ȡ");
						ToastHelper.showToastLong(Register_SmsCode_Activiity.this,msg.getData().getString("errorMsg"));//��ʾ����ʧ�ܵ�ԭ��
						break;
									
					case getCodeDataFail:
						code_code_get.setText("���»�ȡ");
						ToastHelper.showSendDataFailToast(Register_SmsCode_Activiity.this);
						break;
																
					case sendRegisterOk:		
						SubmitDialog.closeSubmitDialog();
						
						Public_Param.isRegisterOk = true;
						Public_Param.phone = code_phone.getText().toString();
						Public_Param.password = register_pwd2.getText().toString();
						
						Request_Submit();//쫷���
						
						finish();
						break;
						
					case sendRegisterFail:
						SubmitDialog.closeSubmitDialog();
						ToastHelper.showToastLong(
								Register_SmsCode_Activiity.this,"�ֻ���֤�����");//��ʾ����ʧ�ܵ�ԭ��

						break;
																							
					case sendRegisterDataFail:
						SubmitDialog.closeSubmitDialog();
						ToastHelper.showSendDataFailToast(Register_SmsCode_Activiity.this);
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
				code_code_get.setText(time+"��");				
				time = time -1;
				handler.postDelayed(myRunnable, 1000);
				code_code_get.setClickable(false);
			}else{
				if(time < 0){
					//120����û�л�ȡ��
					code_code_get.setText("���»�ȡ");
					isGetSms = false;
					time = 120;
					code_code_get.setClickable(true);
				}else{
					//��ȡ���ųɹ�
					code_code_get.setText("���»�ȡ");
					isGetSms = false;
					time = 120;
					code_code_get.setClickable(true);
				}
				
			}
		}
		
	};

	/**
	 * ����ֻ����Ƿ���ȷ
	 */
	
	private void checkPhoneMessage(){
		
		if(code_phone.getText().toString().equals("") || code_phone.getText().toString() == null){
			errorMsg = errorMsg +"�ֻ��Ų���Ϊ��";		
			System.out.println("�ֻ��Ų���Ϊ��"+code_phone.getText().toString());
		}else{
			if(!code_phone.getText().toString().matches("^[1][3578][0-9]{9}$") || code_phone.getText().toString().length() != 11){
				errorMsg = errorMsg +"�ֻ��Ÿ�ʽ����ȷ";		
				System.out.println("�ֻ��Ÿ�ʽ����ȷ"+code_phone.getText().toString());
			}
		}
	
	}
	
	/**
	 * �����֤���Ƿ���ȷ:������
	 */
	private void checkCodeMessage(){
				
		/*����ֻ���*/
		if(code_phone.getText().toString().equals("") || code_phone.getText().toString() == null){
			errorMsg = errorMsg +"�ֻ��Ų���Ϊ��";		
			System.out.println("�ֻ��Ų���Ϊ��"+code_phone.getText().toString());
		}else{
			
			if(!code_phone.getText().toString().matches("^[1][3578][0-9]{9}$")){//�����ж��Ƿ���11λ,��֤���Ѿ���11λ���ж�
				errorMsg = errorMsg +"�ֻ��Ÿ�ʽ����ȷ";		
				System.out.println("�ֻ��Ÿ�ʽ����ȷ"+code_phone.getText().toString());
			}else{
				
				if(!isGetSms){
					errorMsg = errorMsg +"���Ȼ�ȡ��֤��";
				}else{
					
					/*�����֤��*/
					if(code_code.getText().toString().equals("") || code_code.getText().toString() == null){
						errorMsg = errorMsg +"��֤�벻��Ϊ��";		
						System.out.println("��֤�벻��Ϊ��"+code_phone.getText().toString());
					}else{
						if(!code_code.getText().toString().matches("^[0-9]{6}$")){//��֤���Ƿ�Ϊ4λ
							errorMsg = errorMsg +"��֤�벻��ȷ";		
							System.out.println("��֤���ʽ����ȷ"+code_phone.getText().toString());
						}else{
							
							/*��֤����*/
							checkPasswordMessage();
						}
					}
				}
					
			}
		}
			
	}
	/**
	 * ����ֻ����Ƿ���ȷ
	 */
	private void checkPasswordMessage(){

			if(register_pwd2.getText().toString().equals("") || register_pwd2.getText().toString() == null){
				errorMsg = errorMsg +"���벻��Ϊ��";		
				System.out.println("���벻��Ϊ��" + register_pwd2.getText().toString());
			}else{
				
				if(!register_pwd2.getText().toString().matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,18}$")){//����ͬʱ������ĸ���ֲ�����6-18λ
					errorMsg = errorMsg +"�������ͬʱ������ĸ���ֲ�����6-18λ";	
				}
			}
	}
	/**
	 * �����ֻ���
	 */
	private void verifyPhone(){
			
		/*  ��ȡ��½��Ϣ  */
		String phone = code_phone.getText().toString();
		
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
					if(!registered){
						
						//ok
						handler.sendEmptyMessage(checkPhoneOk);
						System.out.println("���ͳɹ�");
						
					}else{
						
						//������֤��ʧ��
						handler.sendEmptyMessage(checkPhoneFail);
						ToastHelper.showToastLong(Register_SmsCode_Activiity.this, "�ֻ������Ѿ�ע��");//��ʾ����ʧ�ܵ�ԭ��
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

//				System.out.println("��½������ʧ��");
				arg3.printStackTrace();
				handler.sendEmptyMessage(checkPhoneDataFail);
//				String errors = new String(arg2);
//				Toast.makeText(LoginActivity.this, "ע��ʧ��"+errors, 3000).show();
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
    				phone = code_phone.getText().toString();
    				code_code.setText(StringHelper.getBusNumber(message[1].trim()));//��ȡ������֮�󣬼�ס���룬����next�ɵ��****************************************************
    				code_next.setEnabled(true);
    			}
    			
			}
    	}
    }
	
	/** ������֤��  
	 * @throws org.json.JSONException */
	private void next(){
		
//		Intent intent = new Intent(Register_SmsCode_Activiity.this, Register_Activity.class);
//		intent.putExtra("phone", code_phone.getText().toString());
//		intent.putExtra("code", code_code.getText().toString());
//		startActivity(intent);
//		/*�����Ի���*/
//		DialogHelper.submitDialogThread(handler);
//		
//		/*  ��ȡ��½��Ϣ  */
//		String phone = this.phone;
//		String code = code_code.getText().toString();
//		
//		/*  ���������½  */
//		AsyncHttpClient httpClient = new AsyncHttpClient();
//		httpClient.setTimeout(Integer.parseInt(getResources().getString(R.string.app_connection_timeout)));
//					
//		RequestParams params = new RequestParams();//�����������
//		
//		String url = PublicData.appWebSite + PublicData.api_smsCodeValidate + phone + "/"+ "validate" + "/" + code;//���������url	
//
//		httpClient.get(url, params, new AsyncHttpResponseHandler() {
//			
//			/* ��������ɹ�  */
//			@Override
//			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//					
//				String smsData = new String(arg2);
//				System.out.println("��֤��������ɹ�:"+smsData);
//				JSONObject jobject;
//				try {
//					
//					jobject = new JSONObject(smsData);
//					boolean status = jobject.getBoolean("status");
//					String message = jobject.getString("message");
//					
//					if(status){
//						
//						//������֤��ɹ�	
//						handler.sendEmptyMessage(sendSmsOk);
//						System.out.println("���ͳɹ�");
//						
//					}else{
//						
//						//������֤��ʧ��
//						handler.sendEmptyMessage(sendSmsFail);
//						Notifycation.showToastLong(Register_SmsCode_Activiity.this, message);//��ʾ����ʧ�ܵ�ԭ��
//					}	
//					
//				} catch (JSONException e) {
//					
//					e.printStackTrace();
//				}		
//				
//			}
//						
//			/* 5.��������ʧ��  */
//			@Override
//			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
//
////				System.out.println("��½������ʧ��");
//				arg3.printStackTrace();
//				handler.sendEmptyMessage(sendSmsDataFail);
////				String errors = new String(arg2);
////				Toast.makeText(LoginActivity.this, "ע��ʧ��"+errors, 3000).show();
//			}
//		});
	}
	
	private void getCode(){		
		
		/*  ��ȡ��½��Ϣ  */
		String phone = code_phone.getText().toString();

		// ����Ĭ�ϵĿͻ���ʵ��  
		HttpClient httpCLient = new DefaultHttpClient();

		JSONObject jsonObject = new JSONObject(); //**********************ע��json��������ʱ��Ҫ����
		jsonObject.put("target", phone);
		jsonObject.put("channel", "sms");
		jsonObject.put("purpose", "register");
//		jsonObject.put("purpose", "");

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
					datajobject = new org.json.JSONObject(data);
					status = datajobject.getBoolean("status");
					message = datajobject.getString("message");
				} catch (org.json.JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
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

	/** ע�� */
	 public void sendRegister() 
	    {  

			// ����Ĭ�ϵĿͻ���ʵ��  
			HttpClient httpCLient = new DefaultHttpClient();
			
			JSONObject jsonObject = new JSONObject();  
            jsonObject.put("phone", code_phone.getText().toString());  
            jsonObject.put("code", code_code.getText().toString());
            jsonObject.put("password", StringHelper.encryption(register_pwd2.getText().toString())); 
//			jsonObject.put("realName", "");
//			jsonObject.put("phone", "13397163438");  
//            jsonObject.put("code", code_code.getText().toString());
//            jsonObject.put("onePassword", "gu123456"); 
//            jsonObject.put("password", "b01422c31985d4580cb01c0972faec26"); 
                         
//            jsonObject.put("registerWay", Public_Platform.P_Android);
//           jsonObject.put("terminalType", "1"); 
System.out.println("phone"+phone);
System.out.println("code"+code_code.getText().toString());
System.out.println("password:"+StringHelper.encryption(register_pwd2.getText().toString()));
			StringEntity requestentity = null;
			try {
				requestentity = new StringEntity(jsonObject.toString(), "utf-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}//���������������    
			requestentity.setContentEncoding("UTF-8");
			requestentity.setContentType("application/json");
			
			// ����get����ʵ��  
			HttpPost httppost = new HttpPost(Public_Api.appWebSite
					+ Public_Api.api_register + "?registerWay=4");//**********************ע�����󷽷�  
			
			httppost.setHeader("Content-Type", "application/json;charset=UTF-8");
			
			httppost.setEntity(requestentity);
			try {

				// �ͻ���ִ��get���� ������Ӧʵ��  
				HttpResponse response = httpCLient.execute(httppost);
				if (response.getStatusLine().getStatusCode() == 200) {//����ɹ�

					// ��ȡ��Ӧ��Ϣʵ��  
					HttpEntity responseentity = response.getEntity();
					String data = EntityUtils.toString(responseentity);
					
					if(data==null ||data.equals("")){
						handler.sendEmptyMessage(sendRegisterDataFail);
						System.out.println("����ֵΪnull");
						return;
					}
					
					System.out.println("����ֵ"+data);
					//�ж���Ӧ��Ϣ
					JSONObject datajobject = JSONObject.parseObject(data);

					System.out.println("22222");
					if (datajobject.containsKey("uid")) {
						System.out.println("33333");
						uid = datajobject.getString("uid");	
						//�޸�����ɹ�	
						handler.sendEmptyMessage(sendRegisterOk);

					} else {
						System.out.println("4444");
						//�޸�����ʧ��
						String message = datajobject.getString("message");
						
						Message msg = new Message();
						msg.what = sendRegisterFail;
						Bundle bundle = new Bundle();
						bundle.putString("errorMsg", message);
						msg.setData(bundle);
						handler.sendMessage(msg);

					}

				} else {//����ʧ��
					handler.sendEmptyMessage(sendRegisterDataFail);
					System.out.println("1sssssssssss");
				}

			} catch (ClientProtocolException e) { //�����쳣  
				e.printStackTrace();
				handler.sendEmptyMessage(sendRegisterDataFail);
				System.out.println("2sssssssssss");
			} catch (IOException e) { //�����쳣    
				e.printStackTrace();
				handler.sendEmptyMessage(sendRegisterDataFail);
				System.out.println("3sssssssssss");
			} catch (JSONException e) {
				e.printStackTrace();
				handler.sendEmptyMessage(sendRegisterDataFail);
				System.out.println("4sssssssssss");
			} finally {
				httpCLient.getConnectionManager().shutdown();
				System.out.println("sssssssssss");
			}
	    } 

	 /** �ύ��Ϣ  */
	private void  Request_Submit(){
		
		/*��ȡ����*/
		String mediaId = Public_Platform.M_Media3;//��Ѷ���ţ�����΢����
		String deviceId = ((TelephonyManager) this.getSystemService(TELEPHONY_SERVICE)).getDeviceId();
		String appid = "1028405";//��쫷�ƽ̨����
		String pkg = "com.gjcar.app";
		
		String privateKey = "cb13cc8b69156692";
		String userId = uid;
		int appVersion = SystemUtils.getVersionCode(this);
		String channel = Public_Platform.C_JuFeng;
		
		/*����*/
		JSONObject jsonObject = new JSONObject(); //**********************ע��json��������ʱ��Ҫ����
		
		jsonObject.put("mediaId", mediaId);
		jsonObject.put("deviceId", deviceId);
		jsonObject.put("appid", appid);
		jsonObject.put("pkg", pkg);
		
		jsonObject.put("privateKey", privateKey);
		jsonObject.put("userId", userId);		
		jsonObject.put("appVersion", appVersion);
		jsonObject.put("channel", channel);
		
		/*�ύ*/
		new HttpHelper().initData(HttpHelper.Method_Post, this, "api/advertise/jf/android/register", jsonObject, null, handler, Request_Submit, 2, null);
		
	}
}

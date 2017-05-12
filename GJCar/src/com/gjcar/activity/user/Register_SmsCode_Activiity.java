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
 *1.发送手机号
 *2.发送手机号和验证码
 */
public class Register_SmsCode_Activiity extends Activity implements OnClickListener{

	/*初始化控件*/
	private EditText code_phone;
	private ImageView code_phone_delete;

	private EditText code_code;
	private ImageView code_code_delete;
	private TextView code_code_get;

	private EditText register_pwd2;//密码
	private ImageView register_pwd2_delete;
	private ImageView register_pwd2_show;
	
	private Button code_next;
	
	/*Handler*/
	private Handler handler;
	
	private final static int checkPhoneOk = 1;//发送短信成功
	private final static int checkPhoneFail = 2;//发送短信失败
	private final static int checkPhoneDataFail = 3;//发送短信请求失败
	
	private final static int getCodeOk = 4;//发送短信成功
	private final static int getCodeFail = 5;//发送短信失败
	private final static int getCodeDataFail = 6;//发送短信请求失败
 
	private final static int sendRegisterOk = 7;//成功
	private final static int sendRegisterFail = 8;//失败
	private final static int sendRegisterDataFail = 9;//请求失败
	
	private final static int Request_Submit = 12;
	
	/*发送验证码*/
	private int time = 120;

	private static boolean isGetSms = false;
	
	private Uri uri;
	private ContentResolver resolver;
	
	/*其它*/
	private String errorMsg = "";
	private String phone = "";//获取短信成功后，记住手机号
	
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
	 * 初始短信监听
	 */
	private void initSmsConfig() {

		uri = Uri.parse("content://sms");
        resolver = getContentResolver();        
        resolver.registerContentObserver(uri , true, new SmsObserver());
		
	}
	
	/**
	 * 初始化监听器
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
		
		register_pwd2 = (EditText)findViewById(R.id.register_pwd2);//密码
		register_pwd2_delete = (ImageView)findViewById(R.id.register_pwd2_delete);
		register_pwd2_show = (ImageView)findViewById(R.id.register_pwd2_show);

		code_next = (Button)findViewById(R.id.code_next);
	}

	private void initListener() {

		/*监听点击事件*/
		code_phone_delete.setOnClickListener(this);
		
		code_code_delete.setOnClickListener(this);
		code_code_get.setOnClickListener(this);
		
		register_pwd2_delete.setOnClickListener(this);		
		register_pwd2_show.setOnClickListener(this);
		
		code_next.setOnClickListener(this);
		
		/*监听文本事件*/
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
				if(register_pwd2.getInputType() == 0x81){//密文
					register_pwd2.setInputType(0x90);
					register_pwd2_show.setImageResource(R.drawable.register_pwd_show);
				}else{//明文0x90
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
					
				 	/*弹出对话框*/
					SubmitDialog.showSubmitDialog(this);
					
					//发送验证码到手机
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
						//发送验证码到手机
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
						//code_code_get.setText("重新获取");
						ToastHelper.showToastLong(Register_SmsCode_Activiity.this,msg.getData().getString("errorMsg"));//显示发送失败的原因
						break;
									
					case getCodeDataFail:
						code_code_get.setText("重新获取");
						ToastHelper.showSendDataFailToast(Register_SmsCode_Activiity.this);
						break;
																
					case sendRegisterOk:		
						SubmitDialog.closeSubmitDialog();
						
						Public_Param.isRegisterOk = true;
						Public_Param.phone = code_phone.getText().toString();
						Public_Param.password = register_pwd2.getText().toString();
						
						Request_Submit();//飓风广告
						
						finish();
						break;
						
					case sendRegisterFail:
						SubmitDialog.closeSubmitDialog();
						ToastHelper.showToastLong(
								Register_SmsCode_Activiity.this,"手机验证码错误");//显示发送失败的原因

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
	
	/** 显示时间*/
	Runnable myRunnable = new Runnable(){

		@Override
		public void run() {
			
			if(time >=0 && isGetSms == true){
				code_code_get.setText(time+"秒");				
				time = time -1;
				handler.postDelayed(myRunnable, 1000);
				code_code_get.setClickable(false);
			}else{
				if(time < 0){
					//120秒内没有获取到
					code_code_get.setText("重新获取");
					isGetSms = false;
					time = 120;
					code_code_get.setClickable(true);
				}else{
					//获取短信成功
					code_code_get.setText("重新获取");
					isGetSms = false;
					time = 120;
					code_code_get.setClickable(true);
				}
				
			}
		}
		
	};

	/**
	 * 检查手机号是否正确
	 */
	
	private void checkPhoneMessage(){
		
		if(code_phone.getText().toString().equals("") || code_phone.getText().toString() == null){
			errorMsg = errorMsg +"手机号不能为空";		
			System.out.println("手机号不能为空"+code_phone.getText().toString());
		}else{
			if(!code_phone.getText().toString().matches("^[1][3578][0-9]{9}$") || code_phone.getText().toString().length() != 11){
				errorMsg = errorMsg +"手机号格式不正确";		
				System.out.println("手机号格式不正确"+code_phone.getText().toString());
			}
		}
	
	}
	
	/**
	 * 检查验证码是否正确:逐个检查
	 */
	private void checkCodeMessage(){
				
		/*检查手机号*/
		if(code_phone.getText().toString().equals("") || code_phone.getText().toString() == null){
			errorMsg = errorMsg +"手机号不能为空";		
			System.out.println("手机号不能为空"+code_phone.getText().toString());
		}else{
			
			if(!code_phone.getText().toString().matches("^[1][3578][0-9]{9}$")){//无需判断是否是11位,验证码已经有11位的判断
				errorMsg = errorMsg +"手机号格式不正确";		
				System.out.println("手机号格式不正确"+code_phone.getText().toString());
			}else{
				
				if(!isGetSms){
					errorMsg = errorMsg +"请先获取验证码";
				}else{
					
					/*检查验证码*/
					if(code_code.getText().toString().equals("") || code_code.getText().toString() == null){
						errorMsg = errorMsg +"验证码不能为空";		
						System.out.println("验证码不能为空"+code_phone.getText().toString());
					}else{
						if(!code_code.getText().toString().matches("^[0-9]{6}$")){//验证码是否为4位
							errorMsg = errorMsg +"验证码不正确";		
							System.out.println("验证码格式不正确"+code_phone.getText().toString());
						}else{
							
							/*验证密码*/
							checkPasswordMessage();
						}
					}
				}
					
			}
		}
			
	}
	/**
	 * 检查手机号是否正确
	 */
	private void checkPasswordMessage(){

			if(register_pwd2.getText().toString().equals("") || register_pwd2.getText().toString() == null){
				errorMsg = errorMsg +"密码不能为空";		
				System.out.println("密码不能为空" + register_pwd2.getText().toString());
			}else{
				
				if(!register_pwd2.getText().toString().matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,18}$")){//必须同时包含字母数字并且是6-18位
					errorMsg = errorMsg +"密码必须同时包含字母数字并且是6-18位";	
				}
			}
	}
	/**
	 * 发送手机号
	 */
	private void verifyPhone(){
			
		/*  获取登陆信息  */
		String phone = code_phone.getText().toString();
		
		/*  向服务器登陆  */
		AsyncHttpClient httpClient = new AsyncHttpClient();
		httpClient.setTimeout(Integer.parseInt(getResources().getString(R.string.app_connection_timeout)));
					
		RequestParams params = new RequestParams();//设置请求参数

		String url = Public_Api.appWebSite + Public_Api.api_register +"/"+ phone;//设置请求的url	
		httpClient.get(url, params, new AsyncHttpResponseHandler() {
			
			/* 处理请求成功  */
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
						System.out.println("发送成功");
						
					}else{
						
						//发送验证码失败
						handler.sendEmptyMessage(checkPhoneFail);
						ToastHelper.showToastLong(Register_SmsCode_Activiity.this, "手机号码已经注册");//显示发送失败的原因
					}	
					
				} catch (JSONException e) {
					
					e.printStackTrace();
				} catch (org.json.JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
				
			}
						
			/* 5.处理请求失败  */
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

//				System.out.println("登陆请求处理失败");
				arg3.printStackTrace();
				handler.sendEmptyMessage(checkPhoneDataFail);
//				String errors = new String(arg2);
//				Toast.makeText(LoginActivity.this, "注册失败"+errors, 3000).show();
			}
		});
	}
	
	/** 监听短信*/
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
    			System.out.println(new Date(date) + " " + (type == 1 ? "收 " : "发 ") + address + " " + body);
    			System.out.println(body);
    			String[] message = body.split("：");
    			if(message.length >= 2){
    				System.out.println(message[1].trim());
    				phone = code_phone.getText().toString();
    				code_code.setText(StringHelper.getBusNumber(message[1].trim()));//获取到短信之后，记住号码，并且next可点击****************************************************
    				code_next.setEnabled(true);
    			}
    			
			}
    	}
    }
	
	/** 发送验证码  
	 * @throws org.json.JSONException */
	private void next(){
		
//		Intent intent = new Intent(Register_SmsCode_Activiity.this, Register_Activity.class);
//		intent.putExtra("phone", code_phone.getText().toString());
//		intent.putExtra("code", code_code.getText().toString());
//		startActivity(intent);
//		/*弹出对话框*/
//		DialogHelper.submitDialogThread(handler);
//		
//		/*  获取登陆信息  */
//		String phone = this.phone;
//		String code = code_code.getText().toString();
//		
//		/*  向服务器登陆  */
//		AsyncHttpClient httpClient = new AsyncHttpClient();
//		httpClient.setTimeout(Integer.parseInt(getResources().getString(R.string.app_connection_timeout)));
//					
//		RequestParams params = new RequestParams();//设置请求参数
//		
//		String url = PublicData.appWebSite + PublicData.api_smsCodeValidate + phone + "/"+ "validate" + "/" + code;//设置请求的url	
//
//		httpClient.get(url, params, new AsyncHttpResponseHandler() {
//			
//			/* 处理请求成功  */
//			@Override
//			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//					
//				String smsData = new String(arg2);
//				System.out.println("验证码请求处理成功:"+smsData);
//				JSONObject jobject;
//				try {
//					
//					jobject = new JSONObject(smsData);
//					boolean status = jobject.getBoolean("status");
//					String message = jobject.getString("message");
//					
//					if(status){
//						
//						//发送验证码成功	
//						handler.sendEmptyMessage(sendSmsOk);
//						System.out.println("发送成功");
//						
//					}else{
//						
//						//发送验证码失败
//						handler.sendEmptyMessage(sendSmsFail);
//						Notifycation.showToastLong(Register_SmsCode_Activiity.this, message);//显示发送失败的原因
//					}	
//					
//				} catch (JSONException e) {
//					
//					e.printStackTrace();
//				}		
//				
//			}
//						
//			/* 5.处理请求失败  */
//			@Override
//			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
//
////				System.out.println("登陆请求处理失败");
//				arg3.printStackTrace();
//				handler.sendEmptyMessage(sendSmsDataFail);
////				String errors = new String(arg2);
////				Toast.makeText(LoginActivity.this, "注册失败"+errors, 3000).show();
//			}
//		});
	}
	
	private void getCode(){		
		
		/*  获取登陆信息  */
		String phone = code_phone.getText().toString();

		// 创建默认的客户端实例  
		HttpClient httpCLient = new DefaultHttpClient();

		JSONObject jsonObject = new JSONObject(); //**********************注意json发送数据时，要这样
		jsonObject.put("target", phone);
		jsonObject.put("channel", "sms");
		jsonObject.put("purpose", "register");
//		jsonObject.put("purpose", "");

		StringEntity requestentity = null;
		try {
			requestentity = new StringEntity(jsonObject.toString(), "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}//解决中文乱码问题    
		requestentity.setContentEncoding("UTF-8");
		requestentity.setContentType("application/json");

		// 创建get请求实例  
		HttpPost httppost = new HttpPost(Public_Api.appWebSite + Public_Api.api_smsCode);//**********************注意请求方法  

		httppost.setHeader("Content-Type", "application/json;charset=UTF-8");

		httppost.setEntity(requestentity);
		try {

			// 客户端执行get请求 返回响应实体  
			HttpResponse response = httpCLient.execute(httppost);
			if (response.getStatusLine().getStatusCode() == 200) {//请求成功

				// 获取响应消息实体  
				HttpEntity responseentity = response.getEntity();
				String data = EntityUtils.toString(responseentity);

				//判断响应信息
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

					//修改密码成功	
					handler.sendEmptyMessage(getCodeOk);

				} else {
					System.out.println("4444");
					//修改密码失败

					Message msg = new Message();
					msg.what = getCodeFail;
					Bundle bundle = new Bundle();
					bundle.putString("errorMsg", message);
					msg.setData(bundle);
					handler.sendMessage(msg);

				}

			} else {//请求失败
				handler.sendEmptyMessage(getCodeDataFail);
				System.out.println("1sssssssssss");
			}

		} catch (ClientProtocolException e) { //请求异常  
			e.printStackTrace();
			handler.sendEmptyMessage(getCodeDataFail);
			System.out.println("2sssssssssss");
		} catch (IOException e) { //请求异常    
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

	/** 注册 */
	 public void sendRegister() 
	    {  

			// 创建默认的客户端实例  
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
			}//解决中文乱码问题    
			requestentity.setContentEncoding("UTF-8");
			requestentity.setContentType("application/json");
			
			// 创建get请求实例  
			HttpPost httppost = new HttpPost(Public_Api.appWebSite
					+ Public_Api.api_register + "?registerWay=4");//**********************注意请求方法  
			
			httppost.setHeader("Content-Type", "application/json;charset=UTF-8");
			
			httppost.setEntity(requestentity);
			try {

				// 客户端执行get请求 返回响应实体  
				HttpResponse response = httpCLient.execute(httppost);
				if (response.getStatusLine().getStatusCode() == 200) {//请求成功

					// 获取响应消息实体  
					HttpEntity responseentity = response.getEntity();
					String data = EntityUtils.toString(responseentity);
					
					if(data==null ||data.equals("")){
						handler.sendEmptyMessage(sendRegisterDataFail);
						System.out.println("返回值为null");
						return;
					}
					
					System.out.println("返回值"+data);
					//判断响应信息
					JSONObject datajobject = JSONObject.parseObject(data);

					System.out.println("22222");
					if (datajobject.containsKey("uid")) {
						System.out.println("33333");
						uid = datajobject.getString("uid");	
						//修改密码成功	
						handler.sendEmptyMessage(sendRegisterOk);

					} else {
						System.out.println("4444");
						//修改密码失败
						String message = datajobject.getString("message");
						
						Message msg = new Message();
						msg.what = sendRegisterFail;
						Bundle bundle = new Bundle();
						bundle.putString("errorMsg", message);
						msg.setData(bundle);
						handler.sendMessage(msg);

					}

				} else {//请求失败
					handler.sendEmptyMessage(sendRegisterDataFail);
					System.out.println("1sssssssssss");
				}

			} catch (ClientProtocolException e) { //请求异常  
				e.printStackTrace();
				handler.sendEmptyMessage(sendRegisterDataFail);
				System.out.println("2sssssssssss");
			} catch (IOException e) { //请求异常    
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

	 /** 提交信息  */
	private void  Request_Submit(){
		
		/*获取参数*/
		String mediaId = Public_Platform.M_Media3;//腾讯新闻，新浪微博等
		String deviceId = ((TelephonyManager) this.getSystemService(TELEPHONY_SERVICE)).getDeviceId();
		String appid = "1028405";//由飓风平台发放
		String pkg = "com.gjcar.app";
		
		String privateKey = "cb13cc8b69156692";
		String userId = uid;
		int appVersion = SystemUtils.getVersionCode(this);
		String channel = Public_Platform.C_JuFeng;
		
		/*参数*/
		JSONObject jsonObject = new JSONObject(); //**********************注意json发送数据时，要这样
		
		jsonObject.put("mediaId", mediaId);
		jsonObject.put("deviceId", deviceId);
		jsonObject.put("appid", appid);
		jsonObject.put("pkg", pkg);
		
		jsonObject.put("privateKey", privateKey);
		jsonObject.put("userId", userId);		
		jsonObject.put("appVersion", appVersion);
		jsonObject.put("channel", channel);
		
		/*提交*/
		new HttpHelper().initData(HttpHelper.Method_Post, this, "api/advertise/jf/android/register", jsonObject, null, handler, Request_Submit, 2, null);
		
	}
}

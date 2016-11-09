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
 *1.发送手机号
 *2.发送手机号和验证码
 */
public class FindPwd_SmsCode_Activity extends Activity implements OnClickListener{

	/*初始化控件*/
	private EditText find_phone;
	private ImageView find_phone_delete;

	private EditText find_code;
	private ImageView find_code_delete;
	private TextView find_code_get;

	private Button find_next;
	
	/*Handler*/
	private Handler handler;
	
	private final static int checkPhoneOk = 1;//发送短信成功
	private final static int checkPhoneFail = 2;//发送短信失败
	private final static int checkPhoneDataFail = 3;//发送短信请求失败
	
	private final static int getCodeOk = 4;//发送短信成功
	private final static int getCodeFail = 5;//发送短信失败
	private final static int getCodeDataFail = 6;//发送短信请求失败败
	 
	private final static int verifyCodeOk = 7;//发送短信成功
	private final static int verifyCodeFail = 8;//发送短信失败
	private final static int verifyCodeDataFail = 9;//发送短信请求失败败
	
	/*发送验证码*/
	private int time = 120;

	private static boolean isGetSms = false;
	
	private Uri uri;
	private ContentResolver resolver;
	
	/*其它*/
	private String errorMsg = "";
	private String phone = "";//获取短信成功后，记住手机号
	
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
				
		find_phone = (EditText)findViewById(R.id.find_phone);		 
		find_phone_delete = (ImageView)findViewById(R.id.find_phone_delete);
		 
		find_code = (EditText)findViewById(R.id.find_code);
		find_code_delete = (ImageView)findViewById(R.id.find_code_delete);		
		find_code_get = (TextView)findViewById(R.id.find_code_get);

		find_next = (Button)findViewById(R.id.find_next);
	}

	private void initListener() {

		/*监听点击事件*/
		find_phone_delete.setOnClickListener(this);
		
		find_code_delete.setOnClickListener(this);
		find_code_get.setOnClickListener(this);
		
		find_next.setOnClickListener(this);
		
		/*监听文本事件*/
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
					
					//发送验证码到手机
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
						//发送验证码到手机
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
						
					case getCodeFail://此处多为重复发送，所以位置不变，但是要提示
						ToastHelper.showToastLong(FindPwd_SmsCode_Activity.this,msg.getData().getString("errorMsg"));//显示发送失败的原因
						break;
												
					case getCodeDataFail:
						find_code_get.setText("重新获取");
						break;
					
					case verifyCodeOk:
						SubmitDialog.closeSubmitDialog();
						next();
						break;
					
					case verifyCodeFail:
						SubmitDialog.closeSubmitDialog();
						ToastHelper.showToastLong(FindPwd_SmsCode_Activity.this,"验证码错误");//显示发送失败的原因
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
	
	/** 显示时间*/
	Runnable myRunnable = new Runnable(){

		@Override
		public void run() {
			
			if(time >=0 && isGetSms == true){
				find_code_get.setText(time+"秒");				
				time = time -1;
				handler.postDelayed(myRunnable, 1000);
				find_code_get.setClickable(false);
			}else{
				if(time < 0){
					//100秒内没有获取到
					find_code_get.setText("重新获取");
					isGetSms = false;
					time = 120;
					find_code_get.setClickable(true);
				}else{
					//获取短信成功
					find_code_get.setText("重新获取");
					isGetSms = false;
					time = 120;
					find_code_get.setClickable(true);
				}				
			}
		}
		
	};

	/**
	 * 检查手机号是否正确
	 */
	private void checkPhoneMessage(){
		
		if(find_phone.getText().toString().equals("") || find_phone.getText().toString() == null){
			errorMsg = errorMsg +"手机号不能为空";		
			System.out.println("手机号不能为空"+find_phone.getText().toString());
		}else{
			if(!find_phone.getText().toString().matches("^[1][358][0-9]{9}$") || find_phone.getText().toString().length() != 11){
				errorMsg = errorMsg +"手机号格式不正确";		
				System.out.println("手机号格式不正确"+find_phone.getText().toString());
			}
		}
	
	}
	
	/**
	 * 检查验证码是否正确:逐个检查
	 */
	private void checkCodeMessage(){
		
		/*检查手机号*/
		if(find_phone.getText().toString().equals("") || find_phone.getText().toString() == null){
			errorMsg = errorMsg +"手机号不能为空";		
			System.out.println("手机号不能为空"+find_phone.getText().toString());
		}else{
			
			if(!find_phone.getText().toString().matches("^[1][3578][0-9]{9}$")){//无需判断是否是11位,验证码已经有11位的判断
				errorMsg = errorMsg +"手机号格式不正确";		
				System.out.println("手机号格式不正确"+find_phone.getText().toString());
			}else{
				
				if(!isGetSms){
					errorMsg = errorMsg +"请先获取验证码";
				}else{
				
					/*检查验证码*/
					if(find_code.getText().toString().equals("") || find_code.getText().toString() == null){
						errorMsg = errorMsg +"验证码不能为空";		
						System.out.println("验证码不能为空"+find_phone.getText().toString());
					}else{
						if(!find_code.getText().toString().matches("^[0-9]{6}$")){//验证码是否为4位
							errorMsg = errorMsg +"验证码不正确";		
							System.out.println("验证码格式不正确"+find_phone.getText().toString());
						}
					}
				}
			}
		}
			
	}
	
	/**
	 * 发送手机号
	 */
	private void verifyPhone(){
		
	/*  获取登陆信息  */
	String phone = find_phone.getText().toString();
	
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
				if(registered){
					
					//ok
					handler.sendEmptyMessage(checkPhoneOk);
					System.out.println("发送成功");
					
				}else{
					
					//发送验证码失败
					handler.sendEmptyMessage(checkPhoneFail);
					ToastHelper.showToastLong(FindPwd_SmsCode_Activity.this, "手机号未注册");//显示发送失败的原因
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

//			System.out.println("登陆请求处理失败");
			arg3.printStackTrace();
			handler.sendEmptyMessage(checkPhoneDataFail);
//			String errors = new String(arg2);
//			Toast.makeText(LoginActivity.this, "注册失败"+errors, 3000).show();
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
    				phone = find_phone.getText().toString();
    				find_code.setText(StringHelper.getBusNumber(message[1].trim()));//获取到短信之后，记住号码，并且next可点击****************************************************
    				find_next.setEnabled(true);
    			}
    			
			}
    	}
    }
	
	/** 发送验证码  */
	private void next(){
		Intent intent = new Intent(FindPwd_SmsCode_Activity.this, FindPwd_ResetPwd_Activity.class);
		intent.putExtra("phone", find_phone.getText().toString());
		intent.putExtra("code", find_code.getText().toString());
		startActivity(intent);
		
	}
	
	/**
	 * 获取验证码
	 */
	private void getCode(){		
		
		/*  获取登陆信息  */
		String phone = find_phone.getText().toString();

		// 创建默认的客户端实例  
		HttpClient httpCLient = new DefaultHttpClient();

		JSONObject jsonObject = new JSONObject(); //**********************注意json发送数据时，要这样
		jsonObject.put("target", phone);
		jsonObject.put("channel", "sms");
		jsonObject.put("purpose", "resetpwd");
		System.out.println(""+phone);
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
					datajobject = new org.json.JSONObject(data);System.out.println("返回值"+datajobject);
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
	/**
	 * 验证验证码
	 */
	private void verifyCode(){		
		
		/*  获取登陆信息  */
		String phone = find_phone.getText().toString();
		String code = find_code.getText().toString();
		
		// 创建默认的客户端实例  
		HttpClient httpCLient = new DefaultHttpClient();

		JSONObject jsonObject = new JSONObject(); //**********************注意json发送数据时，要这样
		jsonObject.put("phone", phone);
		jsonObject.put("code", code);
		
		System.out.println("phone"+phone);
		StringEntity requestentity = null;
		try {
			requestentity = new StringEntity(jsonObject.toString(), "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}//解决中文乱码问题    
		requestentity.setContentEncoding("UTF-8");
		requestentity.setContentType("application/json");

		// 创建get请求实例  
		HttpPut httpput = new HttpPut(Public_Api.appWebSite + Public_Api.api_smsCode);//**********************注意请求方法  

		httpput.setHeader("Content-Type", "application/json;charset=UTF-8");

		httpput.setEntity(requestentity);
		try {

			// 客户端执行get请求 返回响应实体  
			HttpResponse response = httpCLient.execute(httpput);
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
				System.out.println("22222"+"message");
				System.out.println("22222");
				if (status) {
					System.out.println("33333");

					//修改密码成功	
					handler.sendEmptyMessage(verifyCodeOk);

				} else {
					System.out.println("4444");
					//修改密码失败

					Message msg = new Message();
					msg.what = verifyCodeFail;
					Bundle bundle = new Bundle();
					bundle.putString("errorMsg", message);
					msg.setData(bundle);
					handler.sendMessage(msg);

				}

			} else {//请求失败
				handler.sendEmptyMessage(verifyCodeDataFail);
				System.out.println("1sssssssssss");
			}

		} catch (ClientProtocolException e) { //请求异常  
			e.printStackTrace();
			handler.sendEmptyMessage(verifyCodeDataFail);
			System.out.println("2sssssssssss");
		} catch (IOException e) { //请求异常    
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

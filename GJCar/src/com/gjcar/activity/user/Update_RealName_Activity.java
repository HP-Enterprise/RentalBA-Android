package com.gjcar.activity.user;



import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
import com.gjcar.app.R;
import com.gjcar.data.data.Public_Api;
import com.gjcar.data.data.Public_SP;
import com.gjcar.utils.NetworkHelper;
import com.gjcar.utils.SharedPreferenceHelper;
import com.gjcar.utils.ToastHelper;
import com.gjcar.view.dialog.SubmitDialog;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class Update_RealName_Activity extends Activity{

	/*初始化控件*/
	private EditText realname;
	private EditText number;
	
	private Button update_submit;

	/*Handler*/
	private Handler handler;

	private final static int UpdateOk = 1;//发送短信成功
	private final static int UpdateFail = 2;//发送短信失败
	private final static int UpdateDataFail = 3;//发送短信请求失败

	/*其它*/
	private String errorMsg = "";
	private boolean isClickManyTimes = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("faila");
		super.onCreate(savedInstanceState);System.out.println("failb");
		setContentView(R.layout.activity_update_realname);
		System.out.println("0");
		initTitleBar();System.out.println("1");

		initView();System.out.println("2");

		initListener();System.out.println("3");

		initHandler();System.out.println("4");
				
	}

	private void initTitleBar() {
		ImageView iv = (ImageView)findViewById(R.id.iv_back);System.out.println("hn");
		iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
				
			}
		});
	}

	private void initView() {
		
		realname = (EditText) findViewById(R.id.et_realname);System.out.println("a");
		number = (EditText) findViewById(R.id.et_number);System.out.println("a");
		update_submit = (Button) findViewById(R.id.update_submit);System.out.println("b");
		
		String myName = SharedPreferenceHelper.getString(this, Public_SP.Account, "realName");System.out.println("c"+myName+"");
		String myNumber = SharedPreferenceHelper.getString(this, Public_SP.Account, "credentialNumber");System.out.println("d"+myNumber+"number");
		
		if(!myName.equals("")){
			realname.setEnabled(false);
		}
		
		if(!myNumber.equals("")){
			number.setEnabled(false);System.out.println("wuyule");
		}
		
		realname.setText(myName);
		number.setText(myNumber);
	}
	
	private void initListener() {
		
		update_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				if(isClickManyTimes){
					
					return;
				}
				
				new Thread(){
					public void run() {
						
						try {
							isClickManyTimes = true;
							Thread.sleep(1000);
							isClickManyTimes = false;
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					};
				}.start();
				
				if(!checkNickNameMessage().equals("")){
					ToastHelper.showToastShort(Update_RealName_Activity.this, checkNickNameMessage());
					return;
				}
				
				if(!NetworkHelper.isNetworkAvailable(Update_RealName_Activity.this)){
					return;
				}
				
				SubmitDialog.showSubmitDialog(Update_RealName_Activity.this);
				
				new Thread(){
						public void run() {
							Update_RealName();
						};
				}.start();
			}
		});
		
	}

	private void initHandler() {

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				switch (msg.what) {

					case UpdateOk:
						SubmitDialog.closeSubmitDialog();
						finish();
						break;
	
					case UpdateFail:
						SubmitDialog.closeSubmitDialog();
						ToastHelper.showToastLong(Update_RealName_Activity.this, msg
								.getData().getString("errorMsg"));//显示发送失败的原因
						break;
	
					case UpdateDataFail:
						SubmitDialog.closeSubmitDialog();
						ToastHelper.showSendDataFailToast(Update_RealName_Activity.this);
						break;
	
					default:
						break;
				}

			}
		};

	}
	
	private String checkNickNameMessage(){

		errorMsg = "";
		
		/*姓名是否为null*/
		if(realname.getText().toString().trim().equals("") || realname.getText().toString().trim() == null){
			errorMsg = errorMsg +"姓名不能为空";				
			return errorMsg;
		}
		
		/*姓名格式是否正确*/
		if(!ValidationHelper.IsChineseName(realname.getText().toString().trim())){
			errorMsg = errorMsg +"姓名填写不正确";				
			return errorMsg;
		}
		
		/*姓名长度是否正确*/
		if(realname.getText().toString().length() > 10){
			errorMsg = errorMsg +"姓名长度不能超过10个字符";				
			return errorMsg;
		}
		
		/*身份证格式是否为null*/
		if(number.getText().toString().trim().equals("") || realname.getText().toString().trim() == null){
			errorMsg = errorMsg +"身份证号码不能为空";				
			return errorMsg;
		}

		/*身份证格式是否正确*/System.out.println("ff");		
		if(!SharedPreferenceHelper.getString(this, Public_SP.Account, "credentialNumber").equals("")){
			return errorMsg;
		}
		
		if(!(number.getText().toString().trim().matches("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$")||number.getText().toString().trim().matches("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X|x)$"))){
			System.out.println("f2");
			errorMsg = errorMsg +"身份证格式不正确";				
			return errorMsg;
		}
		return errorMsg;
	}
	
	/** 发送城市信息  */
	private void Update_RealName() {
		
		// 创建默认的客户端实例  
		HttpClient httpCLient = new DefaultHttpClient();

		JSONObject jsonObject = new JSONObject(); //**********************注意json发送数据时，要这样

		//参数含义https://github.com/HP-Enterprise/Rental653/issues/731		
		jsonObject.put("id", new Integer(SharedPreferenceHelper.getUid(this)));System.out.println("userId"+new Integer(SharedPreferenceHelper.getUid(this)));//租车用户ID
		jsonObject.put("realName", realname.getText().toString());	
		jsonObject.put("credentialNumber", number.getText().toString());		
		jsonObject.put("nickName", SharedPreferenceHelper.getString(this, Public_SP.Account, "nickName"));	
		
		StringEntity requestentity = null;
		try {System.out.println("3bbbbbbbbbbbb");
			requestentity = new StringEntity(jsonObject.toString(), "utf-8");System.out.println("4bbbbbbbbbbbb");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}//解决中文乱码问题    
		requestentity.setContentEncoding("UTF-8");
		requestentity.setContentType("application/json");

		System.out.println("5bbbbbbbbbbbb");
		// 创建get请求实例  
//		HttpPost httppost = new HttpPost("http://121.40.157.200:7890/api/user/"+new Integer(SharedPreferenceHelper.getUid(this)).toString() +"/order");//**********************注意请求方法  
		HttpPut httpput = new HttpPut(Public_Api.appWebSite+"api/me");//**********************注意请求方法  
		
		httpput.setHeader("Content-Type", "application/json;charset=UTF-8");
		AddCookies(httpput);
		
		httpput.setEntity(requestentity);
		try {
			System.out.println("6bbbbbbbbbbbb");
			// 客户端执行get请求 返回响应实体  
			HttpResponse response = httpCLient.execute(httpput);System.out.println("1aaaaaa");
			if (response.getStatusLine().getStatusCode() == 200) {//请求成功
				System.out.println("2aaaaaa");
				// 获取响应消息实体  
				HttpEntity responseentity = response.getEntity();
				String data = EntityUtils.toString(responseentity);
				System.out.println("3aaaaaa"+data);
				//判断响应信息
				org.json.JSONObject datajobject = new org.json.JSONObject(data);
				boolean status = datajobject.getBoolean("status");
				String message = datajobject.getString("message");
				
				if (status) {
					System.out.println("33333");

					//修改密码成功	
					handler.sendEmptyMessage(UpdateOk);

					saveNickName();
				} else {
					System.out.println("4444");
					//修改密码失败

					Message msg = new Message();
					msg.what = UpdateFail;
					Bundle bundle = new Bundle();
					bundle.putString("errorMsg", message);
					msg.setData(bundle);
					handler.sendMessage(msg);

				}

			} else {//请求失败
				HttpEntity responseentity = response.getEntity();
				String data = EntityUtils.toString(responseentity);
				System.out.println("FOUND NOT"+data);
				handler.sendEmptyMessage(UpdateDataFail);
				System.out.println("1sssssssssss");
			}

		} catch (ClientProtocolException e) { //请求异常  
			e.printStackTrace();
			handler.sendEmptyMessage(UpdateDataFail);
			System.out.println("2sssssssssss");
		} catch (IOException e) { //请求异常    
			e.printStackTrace();
			handler.sendEmptyMessage(UpdateDataFail);
			System.out.println("3sssssssssss");
		} catch (org.json.JSONException e) {
			e.printStackTrace();
			handler.sendEmptyMessage(UpdateDataFail);
			System.out.println("4sssssssssss");
		} finally {
			httpCLient.getConnectionManager().shutdown();
			System.out.println("sssssssssss");
		}
		
	}
	
	private void saveNickName(){
		SharedPreferences preferences = this.getSharedPreferences(Public_SP.Account, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();

		editor.putString("realName", realname.getText().toString());
		editor.putString("credentialNumber", number.getText().toString());
		
		editor.commit();
	}

	/**
     * 增加Cookie
     * @param request
     */
    public void AddCookies(HttpPut request)
    {
          StringBuilder sb = new StringBuilder();

          String key = "token";
          String val = SharedPreferenceHelper.getString(this, Public_SP.Account, key);
          sb.append(key);
          sb.append("=");
          sb.append(val);
          sb.append(";");

          request.addHeader("cookie", sb.toString());

          System.out.println(""+sb);
    }
}

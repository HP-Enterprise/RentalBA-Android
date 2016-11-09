package com.gjcar.activity.user;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import com.alibaba.fastjson.JSONObject;
import com.gjcar.app.R;
import com.gjcar.app.application.LocationApplication;
import com.gjcar.data.data.Public_Api;
import com.gjcar.data.data.Public_Param;
import com.gjcar.data.data.Public_SP;
import com.gjcar.utils.NetworkHelper;
import com.gjcar.utils.SharedPreferenceHelper;
import com.gjcar.utils.StringHelper;
import com.gjcar.utils.ToastHelper;
import com.gjcar.view.dialog.SubmitDialog;
import com.gjcar.view.helper.EditTextHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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

public class UpdatePwd_Activity extends Activity implements OnClickListener{

	/*��ʼ���ؼ�*/
	private EditText reset_pwd;
	private ImageView reset_pwd_delete;
	private ImageView reset_pwd_show;
	
	private EditText reset_againpwd;
	private ImageView reset_againpwd_delete;
	private ImageView reset_againpwd_show;
	
	private EditText reset_againpwd2;
	private ImageView reset_againpwd2_delete;
	private ImageView reset_againpwd2_show;
	
	private Button reset_submit;
	
	/*Handler*/
	private Handler handler;
	
	private final static int sendPwdOk = 1;//���Ͷ��ųɹ�
	private final static int sendPwdFail = 2;//���Ͷ���ʧ��
	private final static int sendPwdDataFail = 3;//���Ͷ�������ʧ��
	
	/*����*/
	private String errorMsg = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_update);
		//LocationApplication.getInstance().addActivity(this);
		
		initTitleBar();

		initView();
		
		initListener();
		
		initHandler();
	}

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
		
		reset_pwd = (EditText)findViewById(R.id.reset_pwd);		 
		reset_pwd_delete = (ImageView)findViewById(R.id.reset_pwd_delete);
		reset_pwd_show = (ImageView)findViewById(R.id.reset_pwd_show); 
		
		reset_againpwd = (EditText)findViewById(R.id.reset_againpwd);
		reset_againpwd_delete = (ImageView)findViewById(R.id.reset_againpwd_delete);		
		reset_againpwd_show = (ImageView)findViewById(R.id.reset_againpwd_show);
		
		reset_againpwd2 = (EditText)findViewById(R.id.reset_againpwd2);
		reset_againpwd2_delete = (ImageView)findViewById(R.id.reset_againpwd2_delete);		
		reset_againpwd2_show = (ImageView)findViewById(R.id.reset_againpwd2_show);
		
		reset_submit = (Button)findViewById(R.id.reset_submit);
	}

	private void initListener() {
		
		new EditTextHelper().setEditText_Password(reset_pwd, reset_pwd_delete, reset_pwd_show, new int[]{R.drawable.register_pwd_show, R.drawable.register_pwd_hide});
		new EditTextHelper().setEditText_Password(reset_againpwd, reset_againpwd_delete, reset_againpwd_show, new int[]{R.drawable.register_pwd_show, R.drawable.register_pwd_hide});
		new EditTextHelper().setEditText_Password(reset_againpwd2, reset_againpwd2_delete, reset_againpwd2_show, new int[]{R.drawable.register_pwd_show, R.drawable.register_pwd_hide});
		
		reset_submit.setOnClickListener(this);
	}

	private void initHandler() {
		
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				
				switch (msg.what) {
				
					case sendPwdOk:
						SubmitDialog.closeSubmitDialog();	
						SharedPreferences sp = getSharedPreferences(Public_SP.Account, Context.MODE_PRIVATE);
						Editor editor = sp.edit();
						editor.clear();
						editor.commit();
						
						Intent intent_loginout = new Intent(UpdatePwd_Activity.this, Login_Activity.class);
						startActivity(intent_loginout);
						
						finish();
						Public_Param.list_updatepwd_activity.get(0).finish();
						break;
						
					case sendPwdFail:
						SubmitDialog.closeSubmitDialog();
//						Notifycation.showToastLong(UpdatePwd_Activity.this, msg.getData().getString("errorMsg"));//��ʾ����ʧ�ܵ�ԭ��
						ToastHelper.showToastLong(UpdatePwd_Activity.this, "���������");//��ʾ����ʧ�ܵ�ԭ��
						break;
												
					case sendPwdDataFail:
						SubmitDialog.closeSubmitDialog();
						ToastHelper.showSendDataFailToast(UpdatePwd_Activity.this);
						break;
						
					default:
						break;
				}
								
			}
		};
		
	}
	
	@Override
	public void onClick(View view) {

		switch (view.getId()) {

			case R.id.reset_submit:
				
				checkPassword();
				if(errorMsg.equals("")){				
				
					//�������뵽������
					if(NetworkHelper.isNetworkAvailable(UpdatePwd_Activity.this)){System.out.println("bbbb");
						
					 	/*�����Ի���*/
						SubmitDialog.showSubmitDialog(this);
					
						/*�ύ����*/
						new Thread(){
							public void run() {
								sendPassword();
							}
						}.start();
										
					}
										
				}else{
					Toast.makeText(UpdatePwd_Activity.this, errorMsg, Toast.LENGTH_LONG).show();
					errorMsg = "";
				}
				break;
				
			default:
				break;
		}
	}
	
	/**
	 * ��������Ƿ���ȷ
	 */
	private void checkPassword(){
		
		if(reset_pwd.getText().toString().equals("") || reset_pwd.getText().toString() == null){
			errorMsg = errorMsg +"�����벻��Ϊ��";		
			System.out.println("�����벻��Ϊ��"+reset_pwd.getText().toString());
		}else{
			
			if(!reset_pwd.getText().toString().matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,18}$")){//����ͬʱ������ĸ���ֲ�����6-18λ
				errorMsg = errorMsg +"���������ͬʱ������ĸ���ֲ�����6-18λ";	
			}else{
				if(reset_againpwd.getText().toString().equals("") || reset_againpwd.getText().toString() == null){
					errorMsg = errorMsg +"�����벻��Ϊ��";		
					System.out.println("�����벻��Ϊ��"+reset_againpwd.getText().toString());
				}else{
					if(!reset_againpwd.getText().toString().matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,18}$")){//����ͬʱ������ĸ���ֲ�����6-18λ
						errorMsg = errorMsg +"���������ͬʱ������ĸ���ֲ�����6-18λ";	
					}else{
						if(!reset_againpwd.getText().toString().trim().equals(reset_againpwd2.getText().toString().trim())){
							errorMsg = errorMsg +"2�������벻һ�£�����������";	
						}
					}
				}
			}
			
		}
	
	}
	
	/** ��������  */
	private void sendPassword(){

        // ����Ĭ�ϵĿͻ���ʵ��  
        HttpClient httpCLient = new DefaultHttpClient();
        
        JSONObject jsonObject = new JSONObject();      //**********************ע��json��������ʱ��Ҫ����
        jsonObject.put("oldPwd", StringHelper.encryption(reset_pwd.getText().toString().trim()));  
        jsonObject.put("password", StringHelper.encryption(reset_againpwd.getText().toString().trim()));   
                  
        StringEntity requestentity = null;
		try {
			requestentity = new StringEntity(jsonObject.toString(),"utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}//���������������    
		requestentity.setContentEncoding("UTF-8");    
		requestentity.setContentType("application/json");    
                  
        // ����get����ʵ��  
		HttpPut httpput= new HttpPut(Public_Api.appWebSite + Public_Api.api_updatepwd);//**********************ע�����󷽷�  
//		HttpPut httpput= new HttpPut("http://10.0.12.199:51234/api/my/pwd");//**********************ע�����󷽷�  
	
		httpput.setHeader("Content-Type", "application/json;charset=UTF-8");
		AddCookies(httpput);
        httpput.setEntity(requestentity);   
        try  
        {  
              
            // �ͻ���ִ��get���� ������Ӧʵ��  
            HttpResponse response = httpCLient.execute(httpput);  
            if(response.getStatusLine().getStatusCode() == 200){//����ɹ�
            	
            	// ��ȡ��Ӧ��Ϣʵ��  
                HttpEntity responseentity = response.getEntity();  
                String data = EntityUtils.toString(responseentity);  	              	              

                //�ж���Ӧ��Ϣ
                org.json.JSONObject datajobject = new org.json.JSONObject(data);
    			boolean status = datajobject.getBoolean("status");
    			String message = datajobject.getString("message");
    			System.out.println("22222");  
    			if(status){
    				System.out.println("33333");  
    				
    				//�޸�����ɹ�	
    				handler.sendEmptyMessage(sendPwdOk);  				
    				
    			}else{
    				System.out.println("4444");  
    				//�޸�����ʧ��
    				
    				Message msg = new Message();
    				msg.what = sendPwdFail;
    				Bundle bundle = new Bundle();
    				bundle.putString("errorMsg", message);
    				msg.setData(bundle);
    				handler.sendMessage(msg);
    				
    			}
            	
            }else{//����ʧ��
            	handler.sendEmptyMessage(sendPwdDataFail); System.out.println("1sssssssssss"); 
            }
            
              
        } catch (ClientProtocolException e){ //�����쳣  
            e.printStackTrace(); handler.sendEmptyMessage(sendPwdDataFail);  System.out.println("2sssssssssss"); 
        } catch (IOException e){  //�����쳣    
            e.printStackTrace(); handler.sendEmptyMessage(sendPwdDataFail);  System.out.println("3sssssssssss");   
        } catch (JSONException e) {			
			e.printStackTrace();handler.sendEmptyMessage(sendPwdDataFail); System.out.println("4sssssssssss"); 
		}finally{  
            httpCLient.getConnectionManager().shutdown();  System.out.println("sssssssssss");  
        }  
    
	}
	
	/**
     * ����Cookie
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
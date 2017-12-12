package com.gjcar.activity.user;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

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
import android.content.Intent;
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

public class FindPwd_ResetPwd_Activity extends Activity implements OnClickListener{

	/*��ʼ���ؼ�*/
	private EditText reset_pwd;
	private ImageView reset_pwd_delete;

	private EditText reset_againpwd;
	private ImageView reset_againpwd_delete;

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

		setContentView(R.layout.activity_resetpassword);
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
		 
		reset_againpwd = (EditText)findViewById(R.id.reset_againpwd);
		reset_againpwd_delete = (ImageView)findViewById(R.id.reset_againpwd_delete);		

		reset_submit = (Button)findViewById(R.id.reset_submit);
	}

	private void initListener() {

		/*��������¼�*/
		reset_pwd_delete.setOnClickListener(this);
		
		reset_againpwd_delete.setOnClickListener(this);
		
		reset_submit.setOnClickListener(this);
		
		/*�����ı��¼�*/
		reset_pwd.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {

				if(s.length() == 0){
					reset_pwd_delete.setVisibility(View.GONE);
				}else{
					reset_pwd_delete.setVisibility(View.VISIBLE);
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
				
		reset_pwd.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_UP){
					
					reset_againpwd_delete.setVisibility(View.GONE);
					if(reset_pwd.getText().toString().length() > 0){
						reset_pwd_delete.setVisibility(View.VISIBLE);
					}
				}
				return false;
			}
						
		});
		
		reset_againpwd.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				
				if(s.length() == 0){
					reset_againpwd_delete.setVisibility(View.GONE);
				}else{
					reset_againpwd_delete.setVisibility(View.VISIBLE);
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
		
		reset_againpwd.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_UP){
					
					reset_pwd_delete.setVisibility(View.GONE);
					if(reset_againpwd.getText().toString().length() > 0){
						reset_againpwd_delete.setVisibility(View.VISIBLE);
					}
				}
				return false;
			}
						
		});
	}

	private void initHandler() {
		
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				
				switch (msg.what) {
				
					case sendPwdOk:
						SubmitDialog.closeSubmitDialog();						
						finish();
						Public_Param.list_findpwd_activity.get(0).finish();
						break;	
					
					case sendPwdFail:
						SubmitDialog.closeSubmitDialog();
						ToastHelper.showToastLong(FindPwd_ResetPwd_Activity.this, msg.getData().getString("errorMsg"));//��ʾ����ʧ�ܵ�ԭ��
						break;
												
					case sendPwdDataFail:
						SubmitDialog.closeSubmitDialog();
						ToastHelper.showSendDataFailToast(FindPwd_ResetPwd_Activity.this);
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
		
			case R.id.reset_pwd_delete:
				reset_pwd.setText("");
				break;
	
			case R.id.reset_againpwd_delete:
				reset_againpwd.setText("");
				break;
				
			case R.id.reset_submit:
				
				checkPassword();
				if(errorMsg.equals("")){				
				
				 	/*�����Ի���*/
					SubmitDialog.showSubmitDialog(this);
					
					//�������뵽������
					if(NetworkHelper.isNetworkAvailable(FindPwd_ResetPwd_Activity.this)){System.out.println("bbbb");
					
						new Thread(){
							public void run() {
								sendPassword();
							}
						}.start();
										
					}
										
				}else{
					Toast.makeText(FindPwd_ResetPwd_Activity.this, errorMsg, Toast.LENGTH_LONG).show();
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
			errorMsg = errorMsg +"���벻��Ϊ��";		
			System.out.println("���벻��Ϊ��"+reset_pwd.getText().toString());
		}else{
			
			if(!reset_pwd.getText().toString().matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,18}$")){//����ͬʱ������ĸ���ֲ�����6-18λ
				errorMsg = errorMsg +"�������ͬʱ������ĸ���ֲ�����6-18λ";	
			}else{
				if(!reset_pwd.getText().toString().equals(reset_againpwd.getText().toString())){
					errorMsg = errorMsg +"2���������벻һ��";		
					System.out.println("2���������벻һ��"+reset_pwd.getText().toString());
				}
			}
			
		}
	
	}
	
	/** ��������  */
	private void sendPassword(){
	
        // ����Ĭ�ϵĿͻ���ʵ��  
        HttpClient httpCLient = new DefaultHttpClient();
        
        Intent intent = getIntent();
                
        JSONObject jsonObject = new JSONObject();      //**********************ע��json��������ʱ��Ҫ����
        jsonObject.put("phone", intent.getStringExtra("phone"));  
        jsonObject.put("password", StringHelper.encryption(reset_pwd.getText().toString()));  
        jsonObject.put("code", intent.getStringExtra("code"));   
        System.out.println("phone"+intent.getStringExtra("phone"));
        System.out.println("code"+intent.getStringExtra("code"));
        System.out.println("password:"+StringHelper.encryption(reset_pwd.getText().toString()));     
        StringEntity requestentity = null;
		try {
			requestentity = new StringEntity(jsonObject.toString(),"utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}//���������������    
		requestentity.setContentEncoding("UTF-8");    
		requestentity.setContentType("application/json");    
                  
        // ����get����ʵ��  
		HttpPut httpput= new HttpPut(Public_Api.appWebSite + Public_Api.api_resetpwd);//**********************ע�����󷽷�  
	
		httpput.setHeader("Content-Type", "application/json;charset=UTF-8");
		
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

}

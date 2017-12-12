package com.gjcar.activity.user;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import com.gjcar.app.R;
import com.gjcar.data.data.Public_Api;
import com.gjcar.data.data.Public_BaiduTJ;
import com.gjcar.data.data.Public_Param;
import com.gjcar.data.data.Public_SP;
import com.gjcar.utils.NetworkHelper;
import com.gjcar.utils.SharedPreferenceHelper;
import com.gjcar.utils.StringHelper;
import com.gjcar.utils.ToastHelper;
import com.gjcar.view.dialog.SubmitDialog;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



/**
 * @author Administrator
 *
 */
public class User_Activity extends Activity implements OnClickListener{
	
	/*��ʼ���ؼ�*/
	private TextView user_name;
	private TextView user_nickname;
	private TextView user_phone;	
	private TextView user_certificateNumber;

	private ImageView user_name_go;
	private ImageView user_number_go;
	
	private LinearLayout nickname_lin;
	private LinearLayout user_resetpwd_lin;
	private LinearLayout realname_lin;
	private LinearLayout number_lin;
	private Button user_loginout_btn;
	private boolean isLoadOk = false;
	private boolean realNameIsNull = false;
	private boolean certificateNumberIsNull = false;
	
	/*Handler*/
	private Handler handler;

	private final static int Ok = 1;//���Ͷ��ųɹ�
	private final static int Fail = 2;//���Ͷ���ʧ��
	private final static int DataFail = 3;//���Ͷ�������ʧ��

	/*֤�����*/
	private int certificateNumberType = 0;//0(Ĭ��),1(����x,֤����x),2(����o,֤����x),3(����x,֤����o),4(����0,֤����o)
	
	@Override
	protected void onCreate(Bundle savedInstanceState){

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		
		Public_Param.list_updatepwd_activity.clear();
		Public_Param.list_updatepwd_activity.add(this);
		
		initTitleBar();

		initView();

		initData();
		
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
		
		user_name = (TextView) findViewById(R.id.user_name);
		user_nickname = (TextView) findViewById(R.id.user_nickname);
		user_phone = (TextView) findViewById(R.id.user_phone);
		user_certificateNumber = (TextView) findViewById(R.id.user_certificateNumber);
		
		user_name_go = (ImageView) findViewById(R.id.user_name_go);
		user_number_go = (ImageView) findViewById(R.id.user_number_go);
		
		nickname_lin = (LinearLayout) findViewById(R.id.nickname_lin);	
		user_resetpwd_lin = (LinearLayout) findViewById(R.id.user_resetpwd_lin);
		realname_lin = (LinearLayout) findViewById(R.id.realname_lin);
		number_lin = (LinearLayout) findViewById(R.id.number_lin);
		user_loginout_btn = (Button) findViewById(R.id.user_loginout_btn);
		
		user_nickname.setText(SharedPreferenceHelper.getString(this, Public_SP.Account, "nickName"));
	}
	
	private void initListener() {

//		/*��������¼�*/		
		nickname_lin.setOnClickListener(this);
		user_resetpwd_lin.setOnClickListener(this);		
		user_loginout_btn.setOnClickListener(this);
		realname_lin.setOnClickListener(this);
		number_lin.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View view) {

		switch (view.getId()) {

			case R.id.nickname_lin:
				Intent intent_nickname = new Intent(User_Activity.this, Update_NickName_Activity.class);
				startActivity(intent_nickname);
				break;
		
			case R.id.user_resetpwd_lin:
				Intent intent = new Intent(User_Activity.this, UpdatePwd_Activity.class);
				startActivity(intent);
				break;
			
			case R.id.realname_lin:
				if(!isLoadOk){
					ToastHelper.showToastShort(User_Activity.this, "��ʱ�޷��޸�");
					return;
				}
				if(!realNameIsNull){
					ToastHelper.showToastShort(User_Activity.this, "�����޷��޸�");
					return;
				}
				
				if(certificateNumberIsNull){
					
					certificateNumberType = 1;//����x,֤����x
				}else{
					certificateNumberType = 3;//����x,֤����o
				} 
				Intent intent_realname = new Intent(User_Activity.this, Update_RealName_Activity.class);
				intent_realname.putExtra("certificateNumberType", certificateNumberType);
				startActivity(intent_realname);
				break;
			
			case R.id.number_lin:System.out.println("fail");
				if(!isLoadOk){System.out.println("fail1");
					ToastHelper.showToastShort(User_Activity.this, "��ʱ�޷��޸�");
					return;
				}System.out.println("fail2");
				
				if(!certificateNumberIsNull){
					ToastHelper.showToastShort(User_Activity.this, "֤�������޷��޸�");
					return;
				}
				
				if(realNameIsNull){
					
					certificateNumberType = 1;//����x,֤����x
				}else{
					certificateNumberType = 2;//����0,֤����x
				} 
				Intent intent_number = new Intent(User_Activity.this, Update_RealName_Activity.class);
				intent_number.putExtra("certificateNumberType", certificateNumberType);
				startActivity(intent_number);
				break;
				
			case R.id.user_loginout_btn:
	
				//�������뵽������
				if (NetworkHelper.isNetworkAvailable(User_Activity.this)) {
	
					/*�����Ի���*/
					SubmitDialog.showSubmitDialog(this);
					
					/*�ǳ�*/
					new Thread() {
						public void run() {
							
							loginOut();
						}
					}.start();		
	
				} 
	
				break;

			default:
				break;
		}
	}

	private void initHandler() {

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				switch (msg.what) {

					case Ok:
						SubmitDialog.closeSubmitDialog();
						SharedPreferences sp = getSharedPreferences(Public_SP.Account, Context.MODE_PRIVATE);
						Editor editor = sp.edit();
						editor.clear();
						editor.commit();
						
						Intent intent_loginout = new Intent(User_Activity.this, Login_Activity.class);
						startActivity(intent_loginout);
						Public_Param.loginFrom = Public_Param.loginFrom_LoginOut;
						finish();
						System.out.println("�ǳ��ɹ���");						
						break;
	
					case Fail:
						SubmitDialog.closeSubmitDialog();
//						Notifycation.showToastLong(User_Activity.this, msg
//								.getData().getString("errorMsg"));//��ʾ�ǳ�ʧ�ܵ�ԭ��
						SharedPreferences sp1 = getSharedPreferences(Public_SP.Account, Context.MODE_PRIVATE);
						Editor editor1 = sp1.edit();
						editor1.clear();
						editor1.commit();
						
						Intent intent_fail = new Intent(User_Activity.this, Login_Activity.class);
						startActivity(intent_fail);
//						Public_Parameter.loginFrom = Public_Parameter.loginFrom_LoginOut;
						finish();
						break;
	
					case DataFail:
						SubmitDialog.closeSubmitDialog();
						ToastHelper.showSendDataFailToast(User_Activity.this);
						break;
	
					default:
						break;
				}
			}
		};

	}
	
	/* �����޸���Ϣ����֮�����������չʾ��Ϣ
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		
		System.out.println("nickName"+SharedPreferenceHelper.getString(User_Activity.this, Public_SP.Account, "nickName"));
		System.out.println("realName"+SharedPreferenceHelper.getString(User_Activity.this, Public_SP.Account, "realName"));
		System.out.println("credentialNumber"+SharedPreferenceHelper.getString(User_Activity.this, Public_SP.Account, "credentialNumber"));
		user_nickname.setText(SharedPreferenceHelper.getString(User_Activity.this, Public_SP.Account, "nickName"));
		user_name.setText(SharedPreferenceHelper.getString(User_Activity.this, Public_SP.Account, "realName"));
		user_certificateNumber.setText(SharedPreferenceHelper.getString(User_Activity.this, Public_SP.Account, "credentialNumber"));
		
		super.onResume();
		Public_BaiduTJ.pageStart(this, Public_BaiduTJ.Activity_User);	
	}
	
	@Override
	protected void onPause() {

		super.onPause();
		Public_BaiduTJ.pageEnd(this, Public_BaiduTJ.Activity_User);	
	}
	
	/** �ǳ�  */
	private void loginOut(){	
		
//		Public_Parameter.loginFrom = 0;
		
        // ����Ĭ�ϵĿͻ���ʵ��  
        HttpClient httpCLient = new DefaultHttpClient();;    
    
        // ����get����ʵ��  
		//HttpPut httpput= new HttpPost(PublicData.appWebSite + PublicData.api_updatepwd);//**********************ע�����󷽷�  
		HttpDelete httpdelete= new HttpDelete(Public_Api.appWebSite + Public_Api.api_loginout);//**********************ע�����󷽷�  
	
		httpdelete.setHeader("Content-Type", "application/json;charset=UTF-8");

		AddCookies(httpdelete); 
		
        try  
        {  
              
            // �ͻ���ִ��get���� ������Ӧʵ��  
            HttpResponse response = httpCLient.execute(httpdelete);  
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
    				handler.sendEmptyMessage(Ok);  				
    				
    			}else{
    				System.out.println("4444");  
    				//�޸�����ʧ��
    				
    				Message msg = new Message();
    				msg.what = Fail;
    				Bundle bundle = new Bundle();
    				bundle.putString("errorMsg", message);
    				msg.setData(bundle);
    				handler.sendMessage(msg);
    				
    			}
            	
            }else{//����ʧ��
            	handler.sendEmptyMessage(DataFail); System.out.println("1sssssssssss"); 
            }
            
              
        } catch (ClientProtocolException e){ //�����쳣  
            e.printStackTrace(); handler.sendEmptyMessage(DataFail);  System.out.println("2sssssssssss"); 
        } catch (IOException e){  //�����쳣    
            e.printStackTrace(); handler.sendEmptyMessage(DataFail);  System.out.println("3sssssssssss");   
        } catch (JSONException e) {			
			e.printStackTrace();handler.sendEmptyMessage(DataFail); System.out.println("4sssssssssss"); 
		}finally{  
            httpCLient.getConnectionManager().shutdown();  System.out.println("sssssssssss");  
        }  
    
	}
	
	/**
     * ����Cookie
     * @param request
     */
    public void AddCookies(HttpDelete request)
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
    
    /** ������֤��  */
	private void initData() {

		/*  ���������½  */
		AsyncHttpClient httpClient = new AsyncHttpClient();
		httpClient.setTimeout(Integer.parseInt(getResources().getString(
				R.string.app_connection_timeout)));

		RequestParams params = new RequestParams();//�����������

		String url = Public_Api.appWebSite + "api/me";//���������url	
		
		AddCookies(httpClient);
		
		httpClient.get(url, params, new AsyncHttpResponseHandler() {

			/* ��������ɹ�  */
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

				String smsData = new String(arg2);
				System.out.println("��֤��������ɹ�:" + smsData);
				org.json.JSONObject jobject;
				try {

					jobject = new org.json.JSONObject(smsData);
					boolean status = jobject.getBoolean("status");
					String message = jobject.getString("message");

					if (status) {
						
						isLoadOk = true;
						
						jobject = new org.json.JSONObject(message);
						
						String credentialType = jobject.getString("credentialType");
						String credentialNumber = jobject.getString("credentialNumber");
						String realName = jobject.getString("realName");
						String nickName = jobject.getString("nickName");
						String phone = jobject.getString("phone");
						
						user_nickname.setText(nickName);
						SharedPreferenceHelper.putString(User_Activity.this, Public_SP.Account, "nickName",nickName);
						
						if(!(realName == null || realName.equals("null") || realName.equals(""))){
							user_name.setText(realName);
							user_name_go.setVisibility(View.GONE);
							SharedPreferenceHelper.putString(User_Activity.this, Public_SP.Account, "realName",realName);
						}else{
							realNameIsNull = true;
						}
						
						if(!(credentialNumber == null || credentialNumber.equals("null") || credentialNumber.equals(""))){
							
							//user_certificate.setText(StringHelper.getcredentialType(credentialType));
							user_certificateNumber.setText(credentialNumber);
							user_number_go.setVisibility(View.GONE);					
							SharedPreferenceHelper.putString(User_Activity.this, Public_SP.Account, "credentialNumber",credentialNumber);
						}else{
							certificateNumberIsNull = true;
						}
						
						user_phone.setText(phone);
					}

				} catch (JSONException e) {

					e.printStackTrace();
				}

			}

			/* 5.��������ʧ��  */
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				
				initData();
			}

		});
	}
	
	/**
     * ����Cookie
     * @param request
     */
    public void AddCookies(AsyncHttpClient request)
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

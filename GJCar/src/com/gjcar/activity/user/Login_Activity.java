package com.gjcar.activity.user;

import com.gjcar.annotation.ContentView;
import com.gjcar.annotation.ContentWidget;
import com.gjcar.app.R;
import com.gjcar.data.data.Public_BaiduTJ;
import com.gjcar.data.data.Public_Param;
import com.gjcar.data.data.Public_SP;
import com.gjcar.data.helper.Loginhelper;
import com.gjcar.data.service.JSONHelper;
import com.gjcar.utils.AnnotationViewUtils;
import com.gjcar.utils.HandlerHelper;
import com.gjcar.utils.IntentHelper;
import com.gjcar.utils.NetworkHelper;
import com.gjcar.utils.SharedPreferenceHelper;
import com.gjcar.utils.StringHelper;
import com.gjcar.utils.ToastHelper;
import com.gjcar.view.dialog.SubmitDialog;
import com.gjcar.view.helper.EditTextHelper;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**��¼*/
@ContentView(R.layout.activity_login)
public class Login_Activity extends Activity{

	/*�ؼ�*/
	@ContentWidget(id = R.id.login_phone) EditText login_phone;
	@ContentWidget(id = R.id.login_phone_delete) ImageView login_phone_delete;

	@ContentWidget(id = R.id.login_pwd) EditText login_pwd;
	@ContentWidget(id = R.id.login_pwd_delete) ImageView login_pwd_delete;
	@ContentWidget(id = R.id.login_pwd_show) ImageView login_pwd_show;
	
	@ContentWidget(click = "onClick") TextView login_forgetpwd;
	
	@ContentWidget(click = "onClick") Button login_login,login_register;
	
	/*Handler*/
	private Handler handler;
	
	private final static int Login = 1;//��¼����ʧ��
	private final static int SetAlias = 2;//����Alias
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		AnnotationViewUtils.injectObject(this, this);
		
		initListener();
		
		initHander(); 
		
	}

	@Override
	protected void onResume() {
		
		super.onResume();
		
		/*�ٶ�ͳ��*/
		Public_BaiduTJ.pageStart(this, Public_BaiduTJ.Activity_Login);	
		
		/*ע�᷵��*/
		if(Public_Param.isRegisterOk){
			
			Public_Param.isRegisterOk = false;
			login_phone.setText(Public_Param.phone);
			login_pwd.setText(Public_Param.password);
			
			/*�����ύ�Ի���*/
			SubmitDialog.showSubmitDialog(this);
			
			/*��������*/
            new Loginhelper().login(Login_Activity.this, JSONHelper.getJSONObject(new String[]{"phone","password","agent"}, new Object[]{login_phone.getText().toString().trim(),StringHelper.encryption(login_pwd.getText().toString().trim()),"android"}), 
            		handler, Login);
			           
		}
	
	}

	@Override
	protected void onPause() {

		super.onPause();
		
		/*�ٶ�ͳ��*/
		Public_BaiduTJ.pageEnd(this, Public_BaiduTJ.Activity_Login);	
	}
	
	/**������*/
	private void initListener() {
		
		new EditTextHelper().setEditText_Clear(login_phone, login_phone_delete);
		
		new EditTextHelper().setEditText_Password(login_pwd, login_pwd_delete, login_pwd_show, new int[]{R.drawable.register_pwd_show, R.drawable.register_pwd_hide});
	}

	/**��ʼ��Handler*/
	private void initHander() {
		
		handler = new Handler(){
	
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				
				switch (msg.what) {
				
					/*��¼*/
					case Login:
						SubmitDialog.closeSubmitDialog();
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
							
							Loginhelper.setAlias(Login_Activity.this, SharedPreferenceHelper.getString(Login_Activity.this, Public_SP.Account, "phone"), handler, SetAlias);System.out.println("��¼�ɹ���"+SharedPreferenceHelper.getString(Login_Activity.this, Public_SP.Account, "phone"));//����Alias							
							setResult(RESULT_OK);
							finish();
							
							return;            
						}
						ToastHelper.showToastShort(Login_Activity.this, "�û��������ڻ��������");
						break;
					
					/*��������*/	
					case SetAlias:
						
						/*���óɹ�*/
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
							System.out.println("���ñ����ɹ�");
							
							return;            
						}
						
						/*����ʧ��*/
						this.postDelayed(new Runnable() {
							
							@Override
							public void run() {
								Loginhelper.setAlias(Login_Activity.this, SharedPreferenceHelper.getString(Login_Activity.this, Public_SP.Account, "phone"), handler, SetAlias);//����Alias								
							}
						}, 1000*60);
						
						break;	
						
					default:
						break;
				}
			}
		};
		
	}
	
	public void onClick(View view) {

		switch (view.getId()) {


			case R.id.login_forgetpwd:
				IntentHelper.startActivity(Login_Activity.this, FindPwd_SmsCode_Activity.class);
				break;
			
			case R.id.login_register:
				IntentHelper.startActivity(Login_Activity.this, Register_SmsCode_Activiity.class);
				break;
				
			case R.id.login_login:

				/*����ֻ�����Ϣ*/
				if(!ValidationHelper.Validate_Phone(this, login_phone)){
					return;
				}
				
				/*���������Ϣ*/
				if(!ValidationHelper.Validate_Password(this, login_pwd)){
					return;
				}
				
				/*�������*/
				if(!NetworkHelper.isNetworkAvailable(this)){
					ToastHelper.showNoNetworkToast(this);
					return;
				}
				
				/*�����ύ�Ի���*/
				SubmitDialog.showSubmitDialog(this);
				
				/*��������*/
	            new Loginhelper().login(Login_Activity.this, JSONHelper.getJSONObject(new String[]{"phone","password","agent"}, new Object[]{login_phone.getText().toString().trim(),StringHelper.encryption(login_pwd.getText().toString().trim()),"android"}), 
	            		handler, Login);
								
				break;
				
			default:
				break;
		}
	}
	
	
}

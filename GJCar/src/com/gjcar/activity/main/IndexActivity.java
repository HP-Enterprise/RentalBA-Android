package com.gjcar.activity.main;

import com.alibaba.fastjson.JSONObject;
import com.baidu.mobstat.StatService;
import com.gjcar.activity.user.Login_Activity;
import com.gjcar.activity.user.Register_SmsCode_Activiity;
import com.gjcar.annotation.ContentView;
import com.gjcar.annotation.ContentWidget;
import com.gjcar.app.R;
import com.gjcar.data.data.Public_Platform;
import com.gjcar.utils.AnnotationViewUtils;
import com.gjcar.utils.HttpHelper;
import com.gjcar.utils.IntentHelper;
import com.gjcar.utils.SystemUtils;
import com.gjcar.view.widget.RippleBackground;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;

@ContentView(R.layout.activity_index)
public class IndexActivity extends Activity {

	@ContentWidget(click = "onClick") TextView login;
	
	@ContentWidget(click = "onClick") TextView register;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AnnotationViewUtils.injectObject(this, this);
      
//        final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.content);
//        
//        rippleBackground.startRippleAnimation();

    }

	public void onClick(View view) {

		switch (view.getId()) {

			case R.id.login:
				IntentHelper.startActivity(IndexActivity.this, Login_Activity.class);
				break;
	
			case R.id.register:
				IntentHelper.startActivity(IndexActivity.this, Register_SmsCode_Activiity.class);
				break;
		
			default:
				break;
		}
	}

}


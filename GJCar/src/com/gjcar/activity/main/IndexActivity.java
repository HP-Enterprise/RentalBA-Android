package com.gjcar.activity.main;

import com.alibaba.fastjson.JSONObject;
import com.baidu.mobstat.StatService;
import com.gjcar.activity.user.Login_Activity;
import com.gjcar.activity.user.Register_SmsCode_Activiity;
import com.gjcar.annotation.ContentView;
import com.gjcar.annotation.ContentWidget;
import com.gjcar.app.R;
import com.gjcar.utils.AnnotationViewUtils;
import com.gjcar.utils.IntentHelper;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
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


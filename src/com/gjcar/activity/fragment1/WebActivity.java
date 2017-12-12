package com.gjcar.activity.fragment1;


import com.gjcar.app.R;
import com.gjcar.view.helper.TitleBarHelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class WebActivity extends FragmentActivity{
	
	private String param = "";
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_web);
		
		/*加载标题*/
		TitleBarHelper.Back(this, getIntent().getStringExtra("title"), 0);
				
		initParam();
		
		initFragment();
	}

	/**
	 * 获取传递的参数
	 */
	private void initParam() {
		
		param = getIntent().getStringExtra("fragment");
	}
	
	/**
	 * 初始化Fragment
	 */
	private void initFragment() {
		
		Fragment fragment = null;
		
		if(param.equals("order_read")){fragment = new Fragment_Web("file:///android_asset/user_reader.html");}
		if(param.equals("action_detail")){fragment = new Fragment_Web(getIntent().getStringExtra("url"));}
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fTransaction = fragmentManager.beginTransaction();	
		fTransaction.replace(R.id.func_center, fragment);
		fTransaction.commit();
		
	}
}

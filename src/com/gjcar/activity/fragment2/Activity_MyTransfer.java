package com.gjcar.activity.fragment2;


import com.gjcar.app.R;
import com.gjcar.view.helper.TitleBarHelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class Activity_MyTransfer extends FragmentActivity{
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_web);
		
		/*加载标题*/
		TitleBarHelper.Back(this, "接送机", 0);
		
		initFragment();
	}
	
	/**
	 * 初始化Fragment
	 */
	private void initFragment() {
		
		Fragment fragment = null;
		
		fragment = new Fragment_hhhhh();
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fTransaction = fragmentManager.beginTransaction();	
		fTransaction.replace(R.id.func_center, fragment);
		fTransaction.commit();
		
	}
}

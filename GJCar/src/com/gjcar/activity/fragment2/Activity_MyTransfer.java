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
		
		/*���ر���*/
		TitleBarHelper.Back(this, "���ͻ�", 0);
		
		initFragment();
	}
	
	/**
	 * ��ʼ��Fragment
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

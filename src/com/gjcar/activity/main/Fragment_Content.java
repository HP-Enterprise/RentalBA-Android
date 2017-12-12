package com.gjcar.activity.main;


import com.gjcar.activity.fragment1.Fragment1;
import com.gjcar.activity.fragment2.Fragment2;
import com.gjcar.activity.fragment2.Fragment_hhhhh;
import com.gjcar.activity.fragment3.Fragment3;
import com.gjcar.activity.fragment4.Fragment4;
import com.gjcar.activity.fragment5.Fragment5;
import com.gjcar.activity.fragment6.Fragment6;
import com.gjcar.app.R;
import com.gjcar.data.data.Public_Param;
import com.gjcar.view.widget.MyTabWidget;
import com.gjcar.view.widget.MyTabWidget.OnTabChangedListener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Fragment_Content extends Fragment {

	private long exitTime = 0;

	private MyTabWidget tab;

	private Fragment fragment1;
	private Fragment fragment2;
	private Fragment fragment3;
	private Fragment fragment4;
	private Fragment fragment5;
	private Fragment fragment6;
	private Fragment fragment7;
	
	private Fragment oldFragment;
	private int index;

	
	/*显示Menu*/
	private LinearLayout menu_out;
	/************************************************
	 ***系统方法
	 ************************************************/	
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container,  Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_content, null);
		
		initTab(view);
	
		initMenuOut(view);
		
		initFragment();
		
		return view;
	}
	
	@Override
	public void onResume() {
		
		if(Public_Param.send_toWeb == 3){
			
			if(index != 0){
				
				index = 0;
				switchContent(fragment1);
				tab.setTabTextBg(getActivity(), 0);
			}
		}
		super.onResume();
	}
	
	private void initMenuOut(View view) {
		
		menu_out = (LinearLayout)view.findViewById(R.id.menu_out);
		menu_out.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
											
				final SlidingPaneLayout slidingPaneLayout = (SlidingPaneLayout)getActivity().findViewById(R.id.slidingpanellayout);
				if(slidingPaneLayout.isOpen()){
					slidingPaneLayout.closePane();
				}else{
					slidingPaneLayout.openPane();
				}
				
			}
		});
		
	}


	/************************************************
	 ***方法
	 ************************************************/	
	private void initTab(View view) {
		tab = (MyTabWidget) view.findViewById(R.id.main_tab);
		tab.setOnTabChangedListener(new MyOnTabChangListener());
		
	}

	private void initFragment() {
		
		fragment1 = new Fragment1();
		fragment2 = new Fragment2();
		fragment3 = new Fragment3();
		fragment4 = new Fragment4();
		fragment5 = new Fragment5();
		fragment6 = new Fragment6();
		fragment7 = new Fragment_hhhhh();
		
		/* 初始化：默认中间添加的是homepage页面 */
		FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.center, fragment1);//因为加载地图时会出现闪视，所以提前加载f2
		transaction.commit();

		oldFragment = fragment1;
				
		//switchContent(fragment1);
		index = 0;
	}

	public void switchContent(Fragment newFragment) {
		
		FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		if (newFragment.isAdded()) {
			transaction.hide(oldFragment).show(newFragment).commit();
		} else {
			transaction.hide(oldFragment).add(R.id.center, newFragment).commit();
		}
		oldFragment = newFragment;

	}

	/************************************************
	 ***类，接口
	 ************************************************/

	// 实现tab点击时接口
	public class MyOnTabChangListener implements OnTabChangedListener {

		@Override
		public void OnTabChanged(int number) {

			if (index == number) {
				return;
			}
			index = number;
			switch (number) {
			
				case 0:
					switchContent(fragment1);
					break;
	
				case 1:
					switchContent(fragment4);
					break;
	
				case 2:
					switchContent(fragment6);
					break;
	
				case 3:
					switchContent(fragment6);
					break;
				
				case 4:
					switchContent(fragment4);
					break;
				
				case 5:
					switchContent(fragment5);
					break;
					
				case 6:
					switchContent(fragment6);
					break;	
					
				default:
					break;

			}
		}
	}
	
}

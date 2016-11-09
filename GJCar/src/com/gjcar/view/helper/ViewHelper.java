package com.gjcar.view.helper;

import com.gjcar.app.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

public class ViewHelper {

	public final static int Type_TextView = 1;
	public final static int Type_ImageView = 2;
	public final static int Type_ImageView_Service = 3;
	
	public static void gone(View[] views){
		
		for (int i = 0; i < views.length; i++) {
			views[i].setVisibility(View.GONE);
		}
	}
	
	public static void show(View[] views){
		
		for (int i = 0; i < views.length; i++) {
			views[i].setVisibility(View.VISIBLE);
		}
	}
	
	/*点击几个TextViwe,点击的变色，其它变灰色*/
	@SuppressLint("ResourceAsColor")
	public static void ClickOneFromAll(TextView[] views, int index, int normalBg, int selectBg,int normalColor, int selectColor){
		
		for (int i = 0; i < views.length; i++) {
			if(i == index){
				
				views[i].setBackgroundResource(selectBg);
				views[i].setTextColor(Color.parseColor("#f2c81c"));//R.不能使用，要用getResource
			}else{
				
				views[i].setBackgroundResource(normalBg);
				views[i].setTextColor(normalColor);
			}
			
		}
	}
}

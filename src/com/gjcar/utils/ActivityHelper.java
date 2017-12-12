package com.gjcar.utils;

import java.util.List;

import android.app.Activity;

import com.gjcar.data.data.Public_Param;

public class ActivityHelper {

	public static void clear(List<Activity> list){
		
		for (int i = 0; i < list.size(); i++) {
			list.get(i).finish();
		}
	}
}

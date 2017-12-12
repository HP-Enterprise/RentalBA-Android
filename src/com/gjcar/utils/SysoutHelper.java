package com.gjcar.utils;

import java.util.Set;

import com.alibaba.fastjson.JSONObject;

public class SysoutHelper {

	/**一.页面***********************************************/
	public final static String  Fragment_hhhhh = "Fragment_hhhhh(接送机)";
	public final static boolean Fragment_hhhhh_flag = true;
	
	/**二.方法***********************************************/
	public static void in(String pageName, boolean isShow){
		
		if(isShow){
			
			System.out.println(pageName + "--------------------------------------");
		}				
	}
	
	public static void parma_Json(JSONObject json, boolean isShow){
		
		if(isShow){
			
			Set<String> set = json.keySet();	
			
			for (String key : set) {
				
				System.out.println(key + ":" + json.get(key));
			}
		}
		
	}
	
	public static void parma_one(String key, String value, boolean isShow){
		
		if(isShow){
			
			System.out.println(key + ":" + value);
		}		
	}
}

package com.gjcar.data.service;

import com.alibaba.fastjson.JSONObject;

public class JSONHelper {

	public static JSONObject getJSONObject(String[] keys, Object[] values){
		
		JSONObject jsonObject = new JSONObject();  
        
		for (int i = 0; i < keys.length; i++) {
			jsonObject.put(keys[i], values[i]);
		}

        return jsonObject;
	}
}

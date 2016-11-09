package com.gjcar.data.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gjcar.app.R;
import com.gjcar.data.bean.StoreShows;
import com.gjcar.data.bean.TradeAreaShow;
import com.gjcar.utils.HandlerHelper;

public class Store_SelectHelper {

	
	public List<Map<String,Object>> getData(List<StoreShows> mylist, String[] keys){
		
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		
		for (int i = 0; i < mylist.size(); i++) {
			
			Map<String,Object> map = new HashMap<String ,Object>();
			map.put(keys[0], mylist.get(i).storeName);System.out.println("name"+mylist.get(i).storeName);
			map.put(keys[1], mylist.get(i).storeAddr);System.out.println("address"+mylist.get(i).storeAddr);
			
			list.add(map);
		}
		
		return list;
	}
	
	public void Search_Listener(final EditText et_area, final Handler handler, final int what){
		
		et_area.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				System.out.println("after");
				if(et_area.getText().toString().trim().equals("") || et_area.getText().toString().trim()==null){
					HandlerHelper.sendString(handler, what, "");
					return;
				}

				HandlerHelper.sendString(handler, what, et_area.getText().toString().trim());
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				System.out.println("before");
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
	}
}

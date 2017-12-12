package com.gjcar.data.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gjcar.app.R;
import com.gjcar.data.bean.CityShow;
import com.gjcar.view.helper.ViewHelper;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyBaseAdapter extends BaseAdapter {

	private List<Map<String,Object>> list;
	private Context context;
	private int layout;
	private String[] keys;
	private int[] ids;
	private int[] types;
	
	public MyBaseAdapter(Context context, List<Map<String,Object>> list, int layout, String keys[], int[] ids, int[] types) {
		
		this.list = list;
		this.context = context;
		this.layout = layout;
		this.keys = keys;
		this.ids = ids;
		this.types = types;
	}

	@Override
	public int getCount() {
	
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			
			convertView = View.inflate(context, layout, null);
			
			for (int i = 0; i < types.length; i++) {
				
				switch (types[i]) {
					case ViewHelper.Type_TextView:
						((TextView) convertView.findViewById(ids[i])).setText((String)list.get(position).get(keys[i]));

						break;
		
					case ViewHelper.Type_ImageView:
						
						break;
						
					case ViewHelper.Type_ImageView_Service:
	
						break;
						
					default:
						break;
				}
			}

			
		} else {
			
			for (int i = 0; i < types.length; i++) {
				
				switch (types[i]) {
					case ViewHelper.Type_TextView:
						((TextView) convertView.findViewById(ids[i])).setText((String)list.get(position).get(keys[i]));

						break;
		
					case ViewHelper.Type_ImageView:
						
						break;
						
					case ViewHelper.Type_ImageView_Service:
	
						break;
						
					default:
						break;
				}
			}
			
		}
		
	
		
		return convertView;
	}
	
}

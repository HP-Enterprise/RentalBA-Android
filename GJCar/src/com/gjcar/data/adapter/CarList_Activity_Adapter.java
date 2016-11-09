package com.gjcar.data.adapter;


import java.util.ArrayList;
import java.util.List;

import com.gjcar.activity.fragment1.Activity_Service;
import com.gjcar.activity.user.Login_Activity;
import com.gjcar.app.R;
import com.gjcar.data.bean.ActivityInfo;
import com.gjcar.data.bean.ActivityShow;
import com.gjcar.data.bean.Model____Vendor_Store_Price;
import com.gjcar.data.data.Public_Api;
import com.gjcar.data.data.Public_Param;
import com.gjcar.utils.ImageLoaderHelper;
import com.gjcar.utils.IntentHelper;
import com.gjcar.utils.NetworkHelper;
import com.gjcar.utils.SharedPreferenceHelper;
import com.gjcar.utils.StringHelper;
import com.nostra13.universalimageloader.core.ImageLoader;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CarList_Activity_Adapter extends BaseAdapter {

	private Context context;
	private List<ActivityShow> list;
	
	public CarList_Activity_Adapter(Context context, List<ActivityShow> list) {

		this.list = list;
		this.context = context;
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

		Holder holder;
		if (convertView == null) {
		
			holder = new Holder();
			convertView = View.inflate(context, R.layout.carlist_listview_activity_item, null);
			System.out.println("3******************");
			
			holder.activity_title = (TextView) convertView.findViewById(R.id.activity_title);
			
			System.out.println("4******************");
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		
		/*初始化数据*/
		holder.activity_title.setText(list.get(position).name);			

		return convertView;
	}

	public static class Holder {

		private TextView activity_title;

	}

}

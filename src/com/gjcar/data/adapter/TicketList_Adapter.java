package com.gjcar.data.adapter;


import java.util.ArrayList;
import java.util.List;

import com.gjcar.activity.fragment1.Activity_Service;
import com.gjcar.activity.user.Login_Activity;
import com.gjcar.app.R;
import com.gjcar.data.bean.ActivityInfo;
import com.gjcar.data.bean.Model____Vendor_Store_Price;
import com.gjcar.data.bean.TicketInfo;
import com.gjcar.data.data.Public_Param;
import com.gjcar.utils.IntentHelper;
import com.gjcar.utils.NetworkHelper;
import com.gjcar.utils.SharedPreferenceHelper;
import com.gjcar.utils.StringHelper;
import com.gjcar.utils.TimeHelper;
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

public class TicketList_Adapter extends BaseAdapter {

	private Context context;
	private List<TicketInfo> list;
	
	public TicketList_Adapter(Context context, List<TicketInfo> list) {

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
			convertView = View.inflate(context, R.layout.listview_ticket_item, null);
			System.out.println("3ticket******************");
			
			holder.money = (TextView) convertView.findViewById(R.id.money);
			holder.activity = (TextView) convertView.findViewById(R.id.activity);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			
			System.out.println("4ticket******************");
			convertView.setTag(holder);
			System.out.println("5ticket******************");
		} else {
			holder = (Holder) convertView.getTag();
		}
		
		/*初始化数据*/
		holder.money.setText(list.get(position).amount.toString());	
		holder.activity.setText(list.get(position).title);		
		if(list.get(position).validityBegin != null && !list.get(position).validityBegin.equals("null") && !list.get(position).validityBegin.equals("")){
			
			holder.time.setText(TimeHelper.getTimemis_to_StringTime(list.get(position).validityBegin.toString())+"至\n"+TimeHelper.getTimemis_to_StringTime(list.get(position).validityEnd.toString()));
		}
		
		return convertView;
	}

	public static class Holder {
		
		private TextView money;
		private TextView activity;
		private TextView time;

	}

}

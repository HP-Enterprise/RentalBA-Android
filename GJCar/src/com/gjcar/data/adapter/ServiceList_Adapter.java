package com.gjcar.data.adapter;


import java.util.ArrayList;
import java.util.List;

import com.gjcar.activity.fragment1.Activity_Service;
import com.gjcar.activity.user.Login_Activity;
import com.gjcar.app.R;
import com.gjcar.data.bean.Model____Vendor_Store_Price;
import com.gjcar.data.bean.ServiceAmount;
import com.gjcar.data.data.Public_Msg;
import com.gjcar.data.data.Public_Param;
import com.gjcar.utils.HandlerHelper;
import com.gjcar.utils.IntentHelper;
import com.gjcar.utils.ListenerHelper;
import com.gjcar.utils.NetworkHelper;
import com.gjcar.utils.SharedPreferenceHelper;
import com.gjcar.utils.StringHelper;
import com.gjcar.view.helper.ListViewHelper;
import com.nostra13.universalimageloader.core.ImageLoader;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ServiceList_Adapter extends BaseAdapter {

	private Context context;
	private List<ServiceAmount> list;
	private Handler handler;
	private int what;
	private int day;
	
	public ServiceList_Adapter(Context context, List<ServiceAmount> list, Handler handler, int what,int day) {

		this.list = list;
		this.context = context;
		
		this.handler = handler;
		this.what = what;
		
		this.day = day;
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
			convertView = View.inflate(context, R.layout.service_listview_item, null);
			System.out.println("3******************");
			
			holder.name = (TextView) convertView.findViewById(R.id.name);		
			holder.amount = (TextView) convertView.findViewById(R.id.amount);
			
			holder.ok = (ToggleButton) convertView.findViewById(R.id.ok);
			
			System.out.println("4******************");
			convertView.setTag(holder);
			System.out.println("5******************");
			
			holder.ok.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
					if(isChecked){
						HandlerHelper.sendStringData(handler, what, Public_Msg.Msg_Checked, new Integer(position).toString());
					}else{
						HandlerHelper.sendStringData(handler, what, Public_Msg.Msg_UnChecked, new Integer(position).toString());
					}
				}
			});
		} else {
			holder = (Holder) convertView.getTag();
		}
		
		/*初始化数据*/
	
		holder.name.setText(list.get(position).chargeName);	
		
		if(list.get(position).chargeName.equals("不计免赔")){
			
			if(day <= 7){
				holder.amount.setText("￥"+list.get(position).details.get(0).price.toString()+"X"+day+"天=￥"+list.get(position).details.get(0).price.intValue()*day+"(上限7天)");
			}else{
				holder.amount.setText("￥"+list.get(position).details.get(0).price.toString()+"X"+7+"天=￥"+list.get(position).details.get(0).price.intValue()*7+"(上限7天)");
			}
			
		}else{
			
			holder.amount.setText("￥"+list.get(position).details.get(0).price.toString());
		}
		
		return convertView;
	}

	public static class Holder {
		
		private TextView name;
		private TextView amount;
		
		private ToggleButton ok;	

	}

//	public void setDataChanged(ArrayList<Model___Store_Price> list) {
//		this.list = list;
//		this.notifyDataSetChanged();
//	}
}

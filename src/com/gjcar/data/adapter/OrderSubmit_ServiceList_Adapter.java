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

public class OrderSubmit_ServiceList_Adapter extends BaseAdapter {

	private Context context;
	private List<ServiceAmount> list;
	private int days;
	public OrderSubmit_ServiceList_Adapter(Context context, List<ServiceAmount> list, int days) {

		this.list = list;
		this.context = context;
		this.days = days;
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
			convertView = View.inflate(context, R.layout.ordersubmit_service_listview_item, null);
			System.out.println("3******************");
			
			holder.name = (TextView) convertView.findViewById(R.id.name);		
			holder.amount = (TextView) convertView.findViewById(R.id.amount);
			holder.all = (TextView) convertView.findViewById(R.id.all);
			
			System.out.println("4******************");
			convertView.setTag(holder);
			System.out.println("5******************");

		} else {
			holder = (Holder) convertView.getTag();
		}
		
		/*初始化数据*/
	
		holder.name.setText(list.get(position).chargeName);	
		
		if(list.get(position).chargeName.equals("不计免赔")){//均价50元/天(上限7天,每30天一周期)，共65天
			holder.amount.setText("均价"+list.get(position).details.get(0).price.toString()+"元/天(上限7天,每30天一周期),共"+days+"天");
//			holder.amount.setText("");
		}else{
			holder.amount.setText("￥"+list.get(position).details.get(0).price.toString());
		}
		
		if(list.get(position).chargeName.equals("不计免赔")){
			
//			if(days > 7){
//				holder.all.setText("￥"+list.get(position).details.get(0).price.intValue()*7+"元");
//			}else{
//				holder.all.setText("￥"+list.get(position).details.get(0).price.intValue()*days+"元");
//			}
			int numday = days / 30 * 7  +  (days % 30 > 7 ? 7 : days % 30);  	
			
			holder.all.setText("￥"+list.get(position).details.get(0).price.intValue()*numday+"元");
		}else{
			holder.all.setText("￥"+list.get(position).details.get(0).price.toString());
		}
		return convertView;
	}

	public static class Holder {
		
		private TextView name;
		private TextView amount;
		private TextView all;	

	}

//	public void setDataChanged(ArrayList<Model___Store_Price> list) {
//		this.list = list;
//		this.notifyDataSetChanged();
//	}
}

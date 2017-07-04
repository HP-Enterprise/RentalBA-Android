package com.gjcar.data.adapter;


import java.util.ArrayList;
import java.util.List;

import com.gjcar.activity.fragment1.Activity_Service;
import com.gjcar.activity.user.Login_Activity;
import com.gjcar.app.R;
import com.gjcar.data.bean.Model____Vendor_Store_Price;
import com.gjcar.data.bean.ServiceAmount;
import com.gjcar.data.bean.ServiceValueAddShow;
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

public class ServiceAmountList_Adapter extends BaseAdapter {

	private Context context;
	private List<ServiceValueAddShow> list;
	private Integer day;
	
	public ServiceAmountList_Adapter(Context context, List<ServiceValueAddShow> list, Integer day) {

		this.list = list;
		this.context = context;
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
			convertView = View.inflate(context, R.layout.serviceamount_listview_item, null);
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
	
		holder.name.setText(list.get(position).description);
		
		if(list.get(position).serviceId.intValue() == 1){
			holder.amount.setText(list.get(position).serviceAmount.toString()+"元/次，共1次");
		}else{
			if(list.get(position).serviceId.intValue() == 2){//均价50元/天(上限7天,每30天一周期)，共65天
				
				int numday = day.intValue() / 30 * 7  +  (day.intValue() % 30 > 7 ? 7 : day.intValue() % 30); 
				holder.amount.setText("均价"+list.get(position).serviceAmount.intValue()/numday+"元/天(上限7天,每30天一周期)，共"+day.toString()+"天");
			}else{
				holder.amount.setText("￥"+list.get(position).serviceAmount.toString());
			}
		}
				
		holder.all.setText("￥"+list.get(position).serviceAmount.toString());System.out.println("6***********");
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

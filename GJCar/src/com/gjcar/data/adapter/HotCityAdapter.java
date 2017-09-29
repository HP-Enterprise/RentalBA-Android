package com.gjcar.data.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.gjcar.app.R;
import com.gjcar.data.bean.CityShow;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 
 * 订单列表
 * 0：待支付，1：已下单 2：租赁中 3：已还车 4：已完成 5：已取消 6：NoShow
 * 一：待支付0
 * 二：已下单123
 * 三：已完成4
 * 四：已取消5
 * @author Administrator
 * 
 */
public class HotCityAdapter extends BaseAdapter {

	private List<CityShow> list = new ArrayList<CityShow>(); 
	private Context context;
	
	public HotCityAdapter(Context context, List<CityShow> list) {
		// TODO Auto-generated constructor stub
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
			convertView = View.inflate(context, R.layout.citylist_hot_item, null);
			
			holder.cityname = (TextView) convertView.findViewById(R.id.cityname);
			
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		
		holder.cityname.setText(list.get(position).cityName);
		
		return convertView;
	}

	public static class Holder {
		private TextView cityname;	
		
	}
	
}

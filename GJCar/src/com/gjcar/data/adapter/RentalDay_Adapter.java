package com.gjcar.data.adapter;


import java.util.List;
import java.util.Map;

import com.gjcar.app.R;
import com.gjcar.data.adapter.ScoreChangeList_Adapter.Holder;
import com.gjcar.data.bean.DayRental;
import com.gjcar.utils.TimeHelper;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RentalDay_Adapter extends BaseAdapter {

	private Context context;
	private List<DayRental> list;
	
	public RentalDay_Adapter(Context context, List<DayRental> list) {
		
		this.list = list;System.out.println("size"+list.size());
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

		convertView = View.inflate(context, R.layout.rental_day_gridview_item, null);
		System.out.println("3******************");
		LinearLayout time_money_lin = (LinearLayout) convertView.findViewById(R.id.time_money_lin);
		TextView time = (TextView) convertView.findViewById(R.id.time);
		TextView money = (TextView) convertView.findViewById(R.id.money);
		
		/*初始化数据*/System.out.println("3******************"+position);
		
		if(list.get(position).isOk){
			
			if(!list.get(position).isOk_isHasMoney){
				time.setText(list.get(position).id.toString());	
				money.setText("--");				
			}else{
				time.setText(list.get(position).id.toString());	System.out.println("价格xxxxxxxxxx"+list.get(position).rentalAmount);
				if(list.get(position).rentalAmount == null || list.get(position).rentalAmount.equals("") || list.get(position).rentalAmount.equals("null")){
					money.setText("--");
				}else{
					money.setText("￥"+list.get(position).rentalAmount);
				}
							
				if(list.get(position).isTime){
					time_money_lin.setBackgroundColor(context.getResources().getColor(R.color.page_text_select));
				}
			}
			
		}
		
		return convertView;

	}

}

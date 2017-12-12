package com.gjcar.data.adapter;


import java.util.ArrayList;
import java.util.List;

import com.gjcar.activity.fragment1.Activity_Service;
import com.gjcar.activity.user.Login_Activity;
import com.gjcar.app.R;
import com.gjcar.data.bean.FreeRide;
import com.gjcar.data.bean.Model____Vendor_Store_Price;
import com.gjcar.data.bean.ServiceAmount;
import com.gjcar.data.data.Public_Api;
import com.gjcar.data.data.Public_Msg;
import com.gjcar.data.data.Public_Param;
import com.gjcar.utils.HandlerHelper;
import com.gjcar.utils.ImageLoaderHelper;
import com.gjcar.utils.IntentHelper;
import com.gjcar.utils.ListenerHelper;
import com.gjcar.utils.NetworkHelper;
import com.gjcar.utils.SharedPreferenceHelper;
import com.gjcar.utils.StringHelper;
import com.gjcar.utils.TimeHelper;
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

public class FreeRide_List_Adapter extends BaseAdapter {

	private Context context;
	private List<FreeRide> list;
	
	public FreeRide_List_Adapter(Context context, List<FreeRide> list) {

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
			convertView = View.inflate(context, R.layout.freeride_carlist_listview_item, null);
			System.out.println("3******************");
			
			holder.a_picture = (ImageView)convertView.findViewById(R.id.a_picture); 
			holder.a_model = (TextView)convertView.findViewById(R.id.a_model);
			holder.a_note = (TextView)convertView.findViewById(R.id.a_note);

			holder.b_take_city = (TextView)convertView.findViewById(R.id.b_take_city);
			holder.b_take_time = (TextView)convertView.findViewById(R.id.b_take_time);
			holder.b_money = (TextView)convertView.findViewById(R.id.b_money);
			holder.b_days = (TextView)convertView.findViewById(R.id.b_days);
			holder.b_return_city = (TextView)convertView.findViewById(R.id.b_return_city);
			holder.b_return_time = (TextView)convertView.findViewById(R.id.b_return_time);
			
			System.out.println("4******************");
			convertView.setTag(holder);
			System.out.println("5******************");
			
		} else {
			holder = (Holder) convertView.getTag();
		}
		
		/*初始化数据*/	
		ImageLoader.getInstance().displayImage(Public_Api.appWebSite + list.get(position).vehicleShow.vehicleModelShow.picture, holder.a_picture, ImageLoaderHelper.initDisplayImageOptions());
		holder.a_model.setText(list.get(position).vehicleShow.vehicleModelShow.model);	
		
		String carGroup = "";
		if(list.get(position).vehicleShow.vehicleModelShow.carGroup != null){
			carGroup = StringHelper.getCarGroup(list.get(position).vehicleShow.vehicleModelShow.carGroup);
		}
		
		String carTrunk = "";
		if(list.get(position).vehicleShow.vehicleModelShow.carTrunk != null){
			carTrunk = StringHelper.getCarTrunk(list.get(position).vehicleShow.vehicleModelShow.carTrunk);
		}

		if(carTrunk.equals("1")){
			carTrunk = "3";
		}
		String seats = list.get(position).vehicleShow.vehicleModelShow.seats.toString();

		holder.a_note.setText(carGroup+"/"+carTrunk+"厢"+seats+"座");

		holder.b_take_city.setText(list.get(position).takeCarCityShow.cityName);	
		holder.b_take_time.setText(TimeHelper.getDateWeekTime(list.get(position).takeCarDateStart));	
		holder.b_money.setText("￥"+list.get(position).price.toString());	
		holder.b_days.setText("限租"+list.get(position).maxRentalDay.toString()+"天");	
		holder.b_return_city.setText(list.get(position).returnCarCityShow.cityName);	
		holder.b_return_time.setText(TimeHelper.getDateWeekTime(TimeHelper.getDateTime_YMD_XDays(list.get(position).maxRentalDay.intValue(), list.get(position).takeCarDateStart)));	

		return convertView;
	}

	public static class Holder {
		
		private ImageView a_picture;			
		private TextView a_model;
		private TextView a_note;
		
		private TextView b_take_city;
		private TextView b_take_time;
		private TextView b_money;
		private TextView b_days;
		private TextView b_return_city;
		private TextView b_return_time;
	}

//	public void setDataChanged(ArrayList<Model___Store_Price> list) {
//		this.list = list;
//		this.notifyDataSetChanged();
//	}
}

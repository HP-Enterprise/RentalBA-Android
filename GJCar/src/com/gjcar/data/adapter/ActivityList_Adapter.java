package com.gjcar.data.adapter;


import java.util.ArrayList;
import java.util.List;

import com.gjcar.activity.fragment1.Activity_Service;
import com.gjcar.activity.user.Login_Activity;
import com.gjcar.app.R;
import com.gjcar.data.bean.ActivityInfo;
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

public class ActivityList_Adapter extends BaseAdapter {

	private Context context;
	private List<ActivityInfo> list;
	private ImageLoader imageLoader;
	
	public ActivityList_Adapter(Context context, List<ActivityInfo> list) {
		// TODO Auto-generated constructor stub
		this.list = list;
		this.context = context;
		
		ImageLoaderHelper.initImageLoader(context);
		imageLoader = ImageLoader.getInstance();
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
			System.out.println("进入adapter");
			holder = new Holder();
			convertView = View.inflate(context, R.layout.f6_listview_item, null);
			System.out.println("3******************");
			
			holder.picture = (ImageView) convertView.findViewById(R.id.picture);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.note = (TextView) convertView.findViewById(R.id.note);
			
			System.out.println("4******************");
			convertView.setTag(holder);
			System.out.println("5******************");
		} else {
			holder = (Holder) convertView.getTag();
		}
		
		/*初始化数据*/
		imageLoader.displayImage(Public_Api.appWebSite + list.get(position).image, holder.picture, ImageLoaderHelper.initDisplayImageOptions_ActivityImage());

		holder.title.setText(list.get(position).title);	
		//holder.time.setText(list.get(position).activityShow.startDate+"至"+list.get(position).activityShow.endDate);		
		holder.time.setText(list.get(position).createDate);			
		holder.note.setText(list.get(position).description);
		
//		holder.go.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View view) {
//							
//				/*订单展示*/
//				Public_Param.order_paramas.model = list.get(position).vehicleModelShow.model;
//				Public_Param.order_paramas.carTrunk = list.get(position).vehicleModelShow.carTrunk;
//				Public_Param.order_paramas.seats = list.get(position).vehicleModelShow.seats;
//				Public_Param.order_paramas.carGroup = list.get(position).vehicleModelShow.carGroup;
//				
//				/*订单提交*/
//				Public_Param.order_paramas.vendorId = list.get(position).vendorStorePriceShowList.get(0).vendorShow.id;				
//				Public_Param.order_paramas.modelId = list.get(position).vendorStorePriceShowList.get(0).avgShow.modelId;
//				Public_Param.order_paramas.brandId = list.get(position).vehicleModelShow.brandId;
//				Public_Param.order_paramas.takeCarStoreId = list.get(position).vendorStorePriceShowList.get(0).avgShow.storeId.toString();
//     	
//				/*判断是否有网*/
//				if(!NetworkHelper.isNetworkAvailable(context)){
//					return;
//				}
//				
//				/*判断是否登录*/
//				if(!SharedPreferenceHelper.isLogin(context)){
//					
//					Intent intent = new Intent(context, Login_Activity.class);														
//					((Activity)context).startActivityForResult(intent, 316);
//					return;
//				}
//				
//				IntentHelper.startActivity(context, Activity_Service.class);
//			}
//		});
		return convertView;
	}

	public static class Holder {
		
		private ImageView picture;
		
		private TextView title;
		private TextView time;
		private TextView note;

		private LinearLayout go_lin;

	}

//	public void setDataChanged(ArrayList<Model___Store_Price> list) {
//		this.list = list;
//		this.notifyDataSetChanged();
//	}
}

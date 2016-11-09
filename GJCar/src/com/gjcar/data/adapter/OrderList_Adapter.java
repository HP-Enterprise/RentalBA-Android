package com.gjcar.data.adapter;


import java.util.ArrayList;
import java.util.List;

import com.gjcar.annotation.ContentWidget;
import com.gjcar.app.R;
import com.gjcar.data.bean.Model____Vendor_Store_Price;
import com.gjcar.data.bean.Order;
import com.gjcar.data.data.Public_Api;
import com.gjcar.data.data.Public_Param;
import com.gjcar.utils.ImageLoaderHelper;
import com.gjcar.utils.StringHelper;
import com.gjcar.utils.TimeHelper;
import com.nostra13.universalimageloader.core.ImageLoader;


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
/**
 * 
 * �����б�
 * 0����֧����1�����µ� 2�������� 3���ѻ��� 4������� 5����ȡ�� 6��NoShow
 * һ����֧��0
 * �������µ�123
 * ���������4
 * �ģ���ȡ��5
 * @author Administrator
 * 
 */
public class OrderList_Adapter extends BaseAdapter {

	private ArrayList<Order> orderlist = new ArrayList<Order>();
	private Context context;
	private boolean isDoorToDoor;
	
	public OrderList_Adapter(Context context, ArrayList<Order> orderlist, boolean isDoorToDoor) {
		// TODO Auto-generated constructor stub
		this.orderlist = orderlist;
		this.context = context;
		this.isDoorToDoor = isDoorToDoor;
	}
	
	@Override
	public int getCount() {
	
		return orderlist.size();
	}

	@Override
	public Object getItem(int position) {
		
		return orderlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(context, R.layout.orderlist_item, null);
			
			holder.a_picture = (ImageView) convertView.findViewById(R.id.a_picture);	
			holder.orderId = (TextView) convertView.findViewById(R.id.orderId);			
			holder.state = (TextView) convertView.findViewById(R.id.state);
			
			holder.a_model = (TextView) convertView.findViewById(R.id.a_model);	
			holder.a_note = (TextView) convertView.findViewById(R.id.a_note);
			
			holder.b_take_date = (TextView) convertView.findViewById(R.id.b_take_date);	
			holder.b_take_time = (TextView) convertView.findViewById(R.id.b_take_time);			
			holder.b_days = (TextView) convertView.findViewById(R.id.b_days);	
			holder.b_return_date = (TextView) convertView.findViewById(R.id.b_return_date);	
			holder.b_return_time = (TextView) convertView.findViewById(R.id.b_return_time);	
			
			holder.c_city = (TextView) convertView.findViewById(R.id.c_city);	
			holder.c_address = (TextView) convertView.findViewById(R.id.c_address);	
			
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		
//		if(isDoorToDoor){
//			//"��֧��","���µ�","���µ�","���µ�","������", "������ ","������ ","�ѻ���","�����","��ȡ��"
//			if(orderlist.get(position).orderState.intValue() == 8 || orderlist.get(position).orderState.intValue() == 9){
//				convertView.setVisibility(View.GONE);
//			}
//		}else{
//			if(orderlist.get(position).orderState.intValue() == 5 || orderlist.get(position).orderState.intValue() == 6){
//				convertView.setVisibility(View.GONE);
//			}				
//		}		
		System.out.println("p-"+position);
		ImageLoader.getInstance().displayImage(Public_Api.appWebSite + orderlist.get(position).picture, holder.a_picture, ImageLoaderHelper.initDisplayImageOptions());
		
		holder.orderId.setText(orderlist.get(position).orderId.toString());System.out.println("adapter"+orderlist.get(position).orderId);
		System.out.println("aaaaaaaaaaa�ŵ���"+isDoorToDoor);
		System.out.println("aaaaaaaaaaa����״̬"+orderlist.get(position).orderState.intValue());
		if(isDoorToDoor){
			
			holder.state.setText(StringHelper.getStringType(orderlist.get(position).orderState.intValue(), new String[]{"��֧��","���µ�","���µ�","���µ�","������", "������ ","������ ","�ѻ���","�����","��ȡ��"}));			
		}else{
			holder.state.setText(StringHelper.getStringType(orderlist.get(position).orderState.intValue(), new String[]{"��֧��","���µ� ","������","�ѻ���", "����� ","��ȡ�� ","NoShow"}));			
		}

		holder.a_model.setText(orderlist.get(position).model);System.out.println("modela");
		
		String carGroup = orderlist.get(position).carGroupstr;
		if(orderlist.get(position).carGroupstr == null){
			carGroup = "";
		}
		
		String carTrunk = orderlist.get(position).carTrunkStr;
		if(orderlist.get(position).carTrunkStr == null){
			carTrunk = "";
		}

		String seats = orderlist.get(position).seatsStr;
		if(orderlist.get(position).seatsStr == null){
			seats = "";
		}

		holder.a_note.setText(carGroup+"/"+carTrunk+"/"+seats);System.out.println("modelb");

		holder.b_take_date.setText(TimeHelper.getDateTime_YM(TimeHelper.getTimemis_to_StringTime(orderlist.get(position).takeCarDate)));System.out.println("modelc");
		holder.b_take_time.setText(TimeHelper.getWeekTime(TimeHelper.getTimemis_to_StringTime(orderlist.get(position).takeCarDate)));System.out.println("modeld");
		if(orderlist.get(position).tenancyDays == null){
			orderlist.get(position).tenancyDays = 0;
		}
		holder.b_days.setText(orderlist.get(position).tenancyDays.toString());	System.out.println("modele");	
		holder.b_return_date.setText(TimeHelper.getDateTime_YM(TimeHelper.getTimemis_to_StringTime(orderlist.get(position).returnCarDate)));System.out.println("modelf");
		holder.b_return_time.setText(TimeHelper.getWeekTime(TimeHelper.getTimemis_to_StringTime(orderlist.get(position).returnCarDate)));System.out.println("modelg");
		
		holder.c_city.setText(orderlist.get(position).takeCarCity);
		holder.c_address.setText(orderlist.get(position).returnCarCity);System.out.println("modelh");
		
		return convertView;
		
	}

	public static class Holder {
	
		/*��ʼ���ؼ�*/
		private TextView orderId;	
		private TextView state;	
		
		private ImageView a_picture;
		private TextView a_model;
		private TextView a_note;
		
		private TextView b_take_date;
		private TextView b_take_time;
		private TextView b_days;
		private TextView b_return_date;
		private TextView b_return_time;
		
		private TextView c_city;
		private TextView c_address;
	}

//	public void setDataChanged(ArrayList<Model___Store_Price> list) {
//		this.list = list;
//		this.notifyDataSetChanged();
//	}
}

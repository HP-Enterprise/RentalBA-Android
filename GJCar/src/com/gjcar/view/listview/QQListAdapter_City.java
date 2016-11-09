package com.gjcar.view.listview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.gjcar.app.R;
import com.gjcar.data.adapter.HotCityAdapter;
import com.gjcar.data.bean.CityShow;
import com.gjcar.data.bean.Letter_CityShow;
import com.gjcar.utils.HandlerHelper;
import com.gjcar.utils.SQL_Dao;
import com.gjcar.utils.ToastHelper;
import com.gjcar.view.listview.QQListView_City.QQHeaderAdapter;
import com.gjcar.view.widget.MyGridView;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class QQListAdapter_City extends BaseExpandableListAdapter implements QQHeaderAdapter {			
	
	private QQListView_City listView; 
	private Context context;
	private List<Letter_CityShow> data;
	private List<CityShow> hotcitys;
	private List<CityShow> citys_history;
	
	private int Other_count = 3;//定位和热门城市
	
	private CityShow locCity = null;//定位城市
	
	private Handler handler;
	private int what_hot;
	private int what_history;
	private int what_location;
	
	public QQListAdapter_City(Context context,QQListView_City listView,CityShow locCity,List<Letter_CityShow> data,List<CityShow> citys_history,List<CityShow> hotcitys, Handler handler, int what_location, int what_hot, int what_history) {
				
		this.listView = listView;
		this.context = context;
		this.data = data;
		
		this.locCity = locCity;
		this.hotcitys = hotcitys;
		this.citys_history = citys_history;
		
		this.handler = handler;
		this.what_hot = what_hot;
		this.what_history = what_history;
		this.what_location = what_location;
		System.out.println("ok");
	}

	@Override
	public int getQQHeaderState(int groupPosition, int childPosition) {
		System.out.println("Header1************"+groupPosition);
//		if(groupPosition == 0 || groupPosition == 1 || groupPosition ==2){
//			return PINNED_HEADER_VISIBLE;
//		}
		final int childCount = getChildrenCount(groupPosition);
		if(childPosition == childCount - 1){  System.out.println("up");
			return PINNED_HEADER_PUSHED_UP; 
		}
		else if(childPosition == -1 && !listView.isGroupExpanded(groupPosition)){ System.out.println("gone");
			return PINNED_HEADER_GONE; 
		}
		else{System.out.println("vis");
			return PINNED_HEADER_VISIBLE;
		}
//		final int childCount = getChildrenCount(groupPosition);
//		if(childPosition == -1 && !listView.isGroupExpanded(groupPosition)){ System.out.println("gone");
//			return PINNED_HEADER_GONE; 
//		}
//		else{System.out.println("vis");
//			return PINNED_HEADER_VISIBLE;
//		}
//		return PINNED_HEADER_VISIBLE;
	}

	@Override
	public void configureQQHeader(View header, int groupPosition,
			int childPosition, int alpha) {
		System.out.println("Header************"+groupPosition);

		/*数据*/
		TextView letter = (TextView)header.findViewById(R.id.header_letter);
		
		switch (groupPosition) {
			case 0:
				letter.setText("当前定位城市");
				break;
				
			case 1:				
				letter.setText("历史城市");
				break;
				
			case 2:
				letter.setText("热门城市");				
				break;
				
			default:System.out.println("Header************默认显示"+data.get(groupPosition-Other_count).letter);
				letter.setText(data.get(groupPosition-Other_count).letter);
				break;
		}
		
		letter.setTextColor(Color.parseColor("#F4C917"));
		
		letter.setVisibility(View.VISIBLE);

	}

	private HashMap<Integer,Integer> groupStatusMap = new HashMap<Integer, Integer>();
	
	@Override
	public void setGroupClickStatus(int groupPosition, int status) {
		// TODO Auto-generated method stub
		groupStatusMap.put(groupPosition, status);System.out.println("groupStatusMap"+groupPosition);
	}
	
	@Override
	public int getGroupClickStatus(int groupPosition) {System.out.println("groupStatusMapstatus"+groupStatusMap);
		// TODO Auto-generated method stub
		if(groupStatusMap.containsKey(groupPosition)){
			return groupStatusMap.get(groupPosition);
		}
		else{
			return 0;
		}
	}
	
	@Override  
	public View getGroupView(int groupPosition, boolean isExpanded,   
	                    View convertView, ViewGroup parent) {   
		System.out.println("groupPosition^^^^^^^^^");
		if(convertView==null){
			convertView = LayoutInflater.from(context).inflate(R.layout.citylist_group, null);
			convertView.setClickable(true);
		}
		System.out.println("^^^^^^^^^"+groupPosition);

		/*数据*/
		TextView letter = (TextView)convertView.findViewById(R.id.group_letter);
		
		switch (groupPosition) {
			case 0:
				letter.setText("当前定位城市");
				break;
				
			case 1:
				
				letter.setText("历史城市");
				break;
				
			case 2:
				letter.setText("热门城市");
				break;
				
			default:
				letter.setText(data.get(groupPosition-Other_count).letter);
				break;
		}
		System.out.println("pos"+groupPosition);


		//return super.getGroupView(groupPosition,isExpanded,convertView,parent);//此处不对，会调用父类方法
		return convertView;
	}
	
	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
			
		System.out.println("Child^^^^^^^^^"+childPosition);

		switch (groupPosition) {
			case 0:
				
				convertView = LayoutInflater.from(context).inflate(R.layout.citylist_children, null);
				TextView cityname = (TextView)convertView.findViewById(R.id.child_cityname);
				
				if(locCity.id.intValue() != -1){
					cityname.setText(locCity.cityName);
				}else{
					cityname.setText("没有定位到当前城市");
				}
				
				convertView.setClickable(false);
				convertView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						if(locCity.id.intValue() != -1){
							handler.sendEmptyMessage(what_location);
						}						
					}
				});
				break;
				
			case 1:
				
				OnClickListener MyOnClickListener = new OnClickListener(){

					@Override
					public void onClick(View view) {
						
						Bundle bundle = new Bundle();
						
						switch (view.getId()) {
							case R.id.city_history1:
								bundle.putInt("index", 0);							
								break;
								
							case R.id.city_history2:
								bundle.putInt("index", 1);							
								break;
								
							case R.id.city_history3:
								bundle.putInt("index", 2);
								break;
								
							default:
								break;
						}
						
						HandlerHelper.sendBundle(handler, what_history, bundle);
					}
					
				};
				convertView = LayoutInflater.from(context).inflate(R.layout.citylist_history, null);
				TextView city_history_no = (TextView)convertView.findViewById(R.id.city_history_no);
				
				if(citys_history.size() == 0){
					city_history_no.setVisibility(View.VISIBLE);
				}
				TextView tv1 = (TextView)convertView.findViewById(R.id.city_history1);
				TextView tv2 = (TextView)convertView.findViewById(R.id.city_history2);
				TextView tv3 = (TextView)convertView.findViewById(R.id.city_history3);
				
				tv1.setOnClickListener(MyOnClickListener);
				tv2.setOnClickListener(MyOnClickListener);
				tv3.setOnClickListener(MyOnClickListener);
				
				List<TextView> viewList = new ArrayList<TextView>();
				viewList.add(tv1);
				viewList.add(tv2);
				viewList.add(tv3);
				
				for (int i = 0; i < citys_history.size() && i < 3; i++) {
					viewList.get(i).setText(citys_history.get(i).cityName);
					viewList.get(i).setVisibility(View.VISIBLE);
				}
				break;
				
			case 2:
				convertView = LayoutInflater.from(context).inflate(R.layout.citylist_hot, null);
				MyGridView gridView = (MyGridView)convertView.findViewById(R.id.gridview);
				HotCityAdapter adapter = new HotCityAdapter(context, hotcitys);
				gridView.setAdapter(adapter);
				gridView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View view,
							int position, long arg3) {
						System.out.println("position-"+position);
						if(hotcitys.get(position).latitude == null){
							
							ToastHelper.showToastShort(context, "当前城市没有门店,请选择其它城市");
							return;
						}
						Bundle bundle = new Bundle();
						bundle.putInt("groupPosition", 2);
						bundle.putInt("childPosition", position);
						HandlerHelper.sendBundle(handler, what_hot, bundle);
					}
					
				});
				break;
				
			default:
				convertView = LayoutInflater.from(context).inflate(R.layout.citylist_children, null);
				TextView cityname2 = (TextView)convertView.findViewById(R.id.child_cityname);
				cityname2.setText(data.get(groupPosition-Other_count).citylist.get(childPosition).cityName);
				break;
		}
		System.out.println("child_poi"+groupPosition);
		return convertView;
	}
	
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return data.size()+Other_count;
	}

	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		
		return 0;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		
		if(groupPosition == 0 || groupPosition == 1 || groupPosition == 2){
			return 1;
		}
		
		return data.get(groupPosition-Other_count).citylist.size();
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}

		
}


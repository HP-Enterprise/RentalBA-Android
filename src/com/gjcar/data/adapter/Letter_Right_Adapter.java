package com.gjcar.data.adapter;

import java.util.List;

import com.gjcar.app.R;
import com.gjcar.data.bean.Letter_CityShow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Letter_Right_Adapter extends BaseAdapter{
	
	private List<Letter_CityShow> list;
	private Context context;
	private int count = 3;
	
	public Letter_Right_Adapter(Context context, List<Letter_CityShow> list){
		this.context = context;
		this.list = list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size()+count;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view=LayoutInflater.from(context).inflate(R.layout.citylist_item_letter_right, null);
		TextView tv=(TextView)view.findViewById(R.id.letterListTextView);
		
		switch (position) {
			case 0:
				tv.setText("定位");
				break;
				
			case 1:
				tv.setText("历史");
				break;
				
			case 2:
				tv.setText("热门");
				break;
				
			default:
				tv.setText(list.get(position-count).letter);
				break;
		}
		return view;
	}
	
}

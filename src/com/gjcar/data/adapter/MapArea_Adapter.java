package com.gjcar.data.adapter;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gjcar.annotation.ContentWidget;
import com.gjcar.app.R;
import com.gjcar.data.bean.Model____Vendor_Store_Price;
import com.gjcar.data.bean.Order;
import com.gjcar.utils.StringHelper;
import com.nostra13.universalimageloader.core.ImageLoader;


import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MapArea_Adapter extends BaseAdapter {

	private List<Map<String,Object>> list =new ArrayList<Map<String,Object>>() ;
	private Context context;
	
	public MapArea_Adapter(Context context, List<Map<String,Object>> list) {
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
	public View getView(int position, View convertView, ViewGroup parent) {Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(context, R.layout.listview_area_map, null);
			
			holder.title = (TextView) convertView.findViewById(R.id.title);			
			holder.address = (TextView) convertView.findViewById(R.id.address);			
			holder.checked = (ImageView) convertView.findViewById(R.id.checked);	
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		if(position == 0){System.out.println("position"+position+"---"+list.get(position).get("title").toString());
			holder.title.setTextColor(Color.parseColor("#f2c81c"));
			holder.checked.setVisibility(View.VISIBLE);
		}else{
			holder.title.setTextColor(Color.parseColor("#333333"));
			holder.checked.setVisibility(View.GONE);
		}
		holder.title.setText(list.get(position).get("title").toString());
		holder.address.setText(list.get(position).get("address").toString());

		return convertView;
	
	}

	public static class Holder {
	
		/*³õÊ¼»¯¿Ø¼þ*/
		private TextView title;	
		private TextView address;	
		private ImageView checked;
	}

//	public void setDataChanged(ArrayList<Model___Store_Price> list) {
//		this.list = list;
//		this.notifyDataSetChanged();
//	}
}

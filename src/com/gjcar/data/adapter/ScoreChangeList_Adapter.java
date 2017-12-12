package com.gjcar.data.adapter;


import java.util.ArrayList;
import java.util.List;

import com.gjcar.activity.fragment1.Activity_Service;
import com.gjcar.activity.user.Login_Activity;
import com.gjcar.app.R;
import com.gjcar.data.bean.ActivityInfo;
import com.gjcar.data.bean.Model____Vendor_Store_Price;
import com.gjcar.data.bean.ScoreChange;
import com.gjcar.data.bean.TicketInfo;
import com.gjcar.data.data.Public_Param;
import com.gjcar.utils.IntentHelper;
import com.gjcar.utils.NetworkHelper;
import com.gjcar.utils.SharedPreferenceHelper;
import com.gjcar.utils.StringHelper;
import com.gjcar.utils.TimeHelper;
import com.gjcar.view.widget.CircularProgressBar;
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

public class ScoreChangeList_Adapter extends BaseAdapter {

	private Context context;
	private List<ScoreChange> list;
	
	public ScoreChangeList_Adapter(Context context, List<ScoreChange> list) {

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
			convertView = View.inflate(context, R.layout.listview_scorechange_item, null);
			System.out.println("3******************");
			
			holder.progress_circular = (CircularProgressBar) convertView.findViewById(R.id.progress_circular);
			holder.progress_present = (TextView) convertView.findViewById(R.id.progress_present);
			
			holder.accumulate = (TextView) convertView.findViewById(R.id.accumulate);
//			holder.remainNum = (TextView) convertView.findViewById(R.id.remainNum);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			System.out.println("4******************");
			convertView.setTag(holder);
			System.out.println("5******************");
		} else {
			holder = (Holder) convertView.getTag();
		}
		
		/*初始化数据*/
		
		
		Float percent = list.get(position).percent == null ? 0 : list.get(position).percent;
		if(percent > 1){percent = 1f;}
		holder.progress_circular.setMax(1000);
		int percent_int = (int) (percent * 1000);
		
		holder.progress_circular.setProgress(percent_int);	
		
		String show_percent = (percent_int%10 == 0) ? ((int)(percent_int/10))+"" : ((float)percent_int)/10+"";//为10的倍数，则不显示小数
		holder.progress_present.setText(show_percent+"%");
		holder.accumulate.setText(list.get(position).accumulate.toString());	
//		holder.remainNum.setText("剩余"+list.get(position).remainNum.toString()+"张");	
		holder.title.setText(list.get(position).title);	
		
		if(list.get(position).validityBegin == null || list.get(position).validityEnd == null){
			holder.time.setText("");
		}else{
			holder.time.setText(TimeHelper.getTimemis_to_StringTime(list.get(position).validityBegin.toString())+"至\n"+TimeHelper.getTimemis_to_StringTime(list.get(position).validityEnd.toString()));
		}
		return convertView;
	}

	public static class Holder {
		
		private TextView accumulate;
		private TextView remainNum;
		private TextView title;
		private TextView time;
		private CircularProgressBar progress_circular;
		private TextView progress_present;
	}

}

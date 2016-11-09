package com.gjcar.data.adapter;

import java.util.List;

import com.gjcar.app.R;
import com.gjcar.data.bean.ScoreInfo;
import com.gjcar.utils.TimeHelper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ScoreList_Adapter extends BaseAdapter {

	private Context context;
	private List<ScoreInfo> list;
	
	public ScoreList_Adapter(Context context, List<ScoreInfo> list) {

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
			convertView = View.inflate(context, R.layout.listview_score_item, null);
			System.out.println("3******************");
			
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.score = (TextView) convertView.findViewById(R.id.score);
			
			System.out.println("4******************");
			convertView.setTag(holder);
			System.out.println("5******************");
		} else {
			holder = (Holder) convertView.getTag();
		}
		
		/*初始化数据*/
		holder.title.setText(list.get(position).remark);					
		holder.time.setText(TimeHelper.getTimemis_to_StringTime(list.get(position).changeTime.toString()));
		holder.score.setText(list.get(position).amount.toString());	
		
		return convertView;
	}

	public static class Holder {
		
		private TextView title;
		private TextView time;
		private TextView score;

	}

}

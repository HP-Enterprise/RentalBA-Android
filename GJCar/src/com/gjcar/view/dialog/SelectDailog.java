package com.gjcar.view.dialog;

import com.gjcar.app.R;
import com.gjcar.data.data.Public_Param;
import com.gjcar.utils.HandlerHelper;
import com.gjcar.utils.TimeHelper;
import com.gjcar.view.wheelview.ArrayWheelAdapter;
import com.gjcar.view.wheelview.WheelView;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SelectDailog {

	public static void select(Context context, String title, final TextView textView, final String[] items,final int[] ids){
		
		/* 创建对象 */
		final Dialog dialog = new Dialog(context, R.style.delete_dialog);
		
		/* 初始化视图 */
		View view = View.inflate(context,R.layout.dialog_select, null);
				
		final WheelView wheelView = (WheelView) view.findViewById(R.id.wheelView);
		ArrayWheelAdapter<String> arrayWheelAdapter = new ArrayWheelAdapter<String>(items,2);
		wheelView.setAdapter(arrayWheelAdapter);
		wheelView.setCurrentItem(0);
		
		TextView title_tv = (TextView) view.findViewById(R.id.title);
		title_tv.setText(title);
		title_tv.setTag(wheelView.getCurrentItem());
		
		/* 获取车型或价格 */
		TextView ok = (TextView) view.findViewById(R.id.ok);
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/* 获取*/
				
				textView.setText(items[wheelView.getCurrentItem()]);		
				textView.setTag(ids[wheelView.getCurrentItem()]);
				
				if (null != dialog) {
					dialog.dismiss();
				}
			}
		});
		
		TextView cancel = (TextView) view.findViewById(R.id.cancle);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
		
				if (null != dialog) {
					dialog.dismiss();
				}
			}
		});
			
		/* 初始化监属性 */
		dialog.getWindow().setContentView(view);
		dialog.getWindow().setGravity(Gravity.BOTTOM);
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		dialog.getWindow().setLayout(wm.getDefaultDisplay().getWidth(),LinearLayout.LayoutParams.WRAP_CONTENT);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
	
		/* 显示 */
		dialog.show();
	}
	
	public static void selectFreeRideTime(Context context, String title, final TextView textView_date,final TextView textView_time, final String[] dates,final String[] times,final Handler handler, final int what){
		
		/* 创建对象 */
		final Dialog dialog = new Dialog(context, R.style.delete_dialog);
		
		/* 初始化视图 */
		View view = View.inflate(context,R.layout.dialog_freeride_select, null);
		
		/*日期*/
		final WheelView wheelView_date = (WheelView) view.findViewById(R.id.wheelView_date);
		
		ArrayWheelAdapter<String> arrayWheelAdapter = new ArrayWheelAdapter<String>(dates,2);
		wheelView_date.setAdapter(arrayWheelAdapter);
		wheelView_date.setCurrentItem(0);
		
		/*时间*/
		final WheelView wheelView_time = (WheelView) view.findViewById(R.id.wheelView_time);
		
		ArrayWheelAdapter<String> time_arrayWheelAdapter = new ArrayWheelAdapter<String>(times,2);
		wheelView_time.setAdapter(time_arrayWheelAdapter);
		wheelView_time.setCurrentItem(0);
		
		/*标题*/
		TextView title_tv = (TextView) view.findViewById(R.id.title);
		title_tv.setText(title);
		
		/* 获取车型或价格 */
		TextView ok = (TextView) view.findViewById(R.id.ok);
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/* 获取*/
				
				textView_date.setText(TimeHelper.getDateTime_YM(dates[wheelView_date.getCurrentItem()]+" "+times[wheelView_time.getCurrentItem()]));System.out.println("b");
				textView_time.setText(TimeHelper.getWeekTime(dates[wheelView_date.getCurrentItem()]+" "+times[wheelView_time.getCurrentItem()]));	System.out.println("h"+TimeHelper.getDateTime_YM(dates[wheelView_date.getCurrentItem()]+times[wheelView_time.getCurrentItem()]));

				textView_date.setTag(dates[wheelView_date.getCurrentItem()]+" "+times[wheelView_time.getCurrentItem()]);
				
				handler.sendEmptyMessage(what);
				if (null != dialog) {
					dialog.dismiss();
				}
			}
		});
		
		TextView cancel = (TextView) view.findViewById(R.id.cancle);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
		
				if (null != dialog) {
					dialog.dismiss();
				}
			}
		});
			
		/* 初始化监属性 */
		dialog.getWindow().setContentView(view);
		dialog.getWindow().setGravity(Gravity.BOTTOM);
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		dialog.getWindow().setLayout(wm.getDefaultDisplay().getWidth(),LinearLayout.LayoutParams.WRAP_CONTENT);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
	
		/* 显示 */
		dialog.show();
	}
}

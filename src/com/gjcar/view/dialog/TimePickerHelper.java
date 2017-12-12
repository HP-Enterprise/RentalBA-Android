package com.gjcar.view.dialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.gjcar.app.R;
import com.gjcar.utils.TimeHelper;
import com.gjcar.utils.ToastHelper;
import com.gjcar.view.wheelview.NumericWheelAdapter;
import com.gjcar.view.wheelview.OnWheelScrollListener;
import com.gjcar.view.wheelview.WheelView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TimePickerHelper {
	
	/*选择时间*/
	private static WheelView hour;
	private static WheelView minute;

	public void pickTime(Context context,TextView textiView,String datetime,String name){

		/* 创建对象 */
		final Dialog dialog = new Dialog(context, R.style.delete_dialog);

		/* 初始化视图 */
		View view = View.inflate(context,R.layout.dialog_time, null);

		initView_DateTime(context,view,textiView, dialog,datetime,name);

		/* 初始化监听器 */
		dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
				
				return false;
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
	
	private static void initView_DateTime(final Context context,final View view, final TextView textiView,final Dialog dialog, final String datetime,String name) {

		/* 初始化时间 */
		Calendar c = Calendar.getInstance();
		
		c.setTime(TimeHelper.getTransferTime(datetime));

		int nowHour = c.get(Calendar.HOUR_OF_DAY);
		int nowMinute = c.get(Calendar.MINUTE);

		int curHour = nowHour - 1;
		int curMinute = nowMinute-1;

		hour = (WheelView) view.findViewById(R.id.hour);
		hour.setAdapter(new NumericWheelAdapter(0, 23, "%02d"));
		hour.setLabel("时");
		hour.setCyclic(true);
		hour.setCurrentItem(curHour-0+1);System.out.println("curHour"+curHour);
			
		minute = (WheelView) view.findViewById(R.id.minute);
		minute.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
		minute.setLabel("分");
		minute.setCyclic(true);
		minute.setCurrentItem(curMinute-0+1);System.out.println("curMinute"+nowMinute);
		
		/*中间标题*/
		TextView title = (TextView) view.findViewById(R.id.takecar_datepicker_takereturn);
		title.setText(name);
		
		/* 确定 */
		TextView ok = (TextView) view.findViewById(R.id.takecar_datepicker_ok);
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/* 获取时间 */
				String time = new StringBuilder()
				.append((hour.getCurrentItem()) < 10 ? "0"
						+ (hour.getCurrentItem()) : (hour
						.getCurrentItem()))
				.append(":")	
				.append((minute.getCurrentItem()) < 10 ? "0"
						+ (minute.getCurrentItem()) : (minute
						.getCurrentItem())).toString();
												
				/*设置时间*/
				textiView.setText(time);

				if (null != dialog) {System.out.println("ok");
					dialog.dismiss();System.out.println("fail");
				}
			}
		});
		
		/*取消*/
		TextView cancel = (TextView) view
				.findViewById(R.id.takecar_datepicker_cancle);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if (null != dialog) {
					dialog.dismiss();
				}
			}
		});

	}

}

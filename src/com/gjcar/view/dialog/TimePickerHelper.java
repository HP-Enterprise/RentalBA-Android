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
	
	/*ѡ��ʱ��*/
	private static WheelView hour;
	private static WheelView minute;

	public void pickTime(Context context,TextView textiView,String datetime,String name){

		/* �������� */
		final Dialog dialog = new Dialog(context, R.style.delete_dialog);

		/* ��ʼ����ͼ */
		View view = View.inflate(context,R.layout.dialog_time, null);

		initView_DateTime(context,view,textiView, dialog,datetime,name);

		/* ��ʼ�������� */
		dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
				
				return false;
			}

		});
		
		/* ��ʼ�������� */
		dialog.getWindow().setContentView(view);
		dialog.getWindow().setGravity(Gravity.BOTTOM);
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		dialog.getWindow().setLayout(wm.getDefaultDisplay().getWidth(),LinearLayout.LayoutParams.WRAP_CONTENT);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		
		/* ��ʾ */
		dialog.show();
		
	}
	
	private static void initView_DateTime(final Context context,final View view, final TextView textiView,final Dialog dialog, final String datetime,String name) {

		/* ��ʼ��ʱ�� */
		Calendar c = Calendar.getInstance();
		
		c.setTime(TimeHelper.getTransferTime(datetime));

		int nowHour = c.get(Calendar.HOUR_OF_DAY);
		int nowMinute = c.get(Calendar.MINUTE);

		int curHour = nowHour - 1;
		int curMinute = nowMinute-1;

		hour = (WheelView) view.findViewById(R.id.hour);
		hour.setAdapter(new NumericWheelAdapter(0, 23, "%02d"));
		hour.setLabel("ʱ");
		hour.setCyclic(true);
		hour.setCurrentItem(curHour-0+1);System.out.println("curHour"+curHour);
			
		minute = (WheelView) view.findViewById(R.id.minute);
		minute.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
		minute.setLabel("��");
		minute.setCyclic(true);
		minute.setCurrentItem(curMinute-0+1);System.out.println("curMinute"+nowMinute);
		
		/*�м����*/
		TextView title = (TextView) view.findViewById(R.id.takecar_datepicker_takereturn);
		title.setText(name);
		
		/* ȷ�� */
		TextView ok = (TextView) view.findViewById(R.id.takecar_datepicker_ok);
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/* ��ȡʱ�� */
				String time = new StringBuilder()
				.append((hour.getCurrentItem()) < 10 ? "0"
						+ (hour.getCurrentItem()) : (hour
						.getCurrentItem()))
				.append(":")	
				.append((minute.getCurrentItem()) < 10 ? "0"
						+ (minute.getCurrentItem()) : (minute
						.getCurrentItem())).toString();
												
				/*����ʱ��*/
				textiView.setText(time);

				if (null != dialog) {System.out.println("ok");
					dialog.dismiss();System.out.println("fail");
				}
			}
		});
		
		/*ȡ��*/
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

package com.gjcar.view.widget;

import java.util.ArrayList;
import java.util.List;

import com.gjcar.activity.main.MainActivity;
import com.gjcar.app.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author google
 *
 */
public class MyTabWidget extends LinearLayout {

	private int[] images = new int[]{R.drawable.mian_tab_nor,R.drawable.mian_tab_nor, R.drawable.mian_tab_nor, R.drawable.mian_tab_nor, R.drawable.mian_tab_nor, R.drawable.mian_tab_nor};
	private int[] selImages = new int[]{ R.drawable.mian_tab_sel, R.drawable.mian_tab_sel,R.drawable.mian_tab_sel, R.drawable.mian_tab_sel,R.drawable.mian_tab_sel, R.drawable.mian_tab_sel};
	private String[] names = new String[]{"�����Լ�", "����", "�Żݻ"};

	private List<View> childs = new ArrayList<View>();
	private List<TextView> textViews = new ArrayList<TextView>();

	private MainActivity activity;
	
	@SuppressLint("NewApi")
	public MyTabWidget(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs,defStyle);
		activity = (MainActivity) context;
		// ��ʼ���ؼ�
		init(context);
	}

	public MyTabWidget(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		activity = (MainActivity) context;
	}
	
	public MyTabWidget(Context context) {
		super(context);
		activity = (MainActivity) context;
		init(context);
	}
	
	//��ʼ���ؼ�
	private void init(final Context context){//��ǰ����
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.setBackgroundColor(Color.WHITE);
		
		//����5��tab
		LayoutInflater inflater = LayoutInflater.from(context);//���ز��ֵ���
		
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		
		final ViewGroup group = (ViewGroup) inflater.inflate(R.layout.mytab_title, null);
		for (int i = 0; i < group.getChildCount(); i++) {
			
			final int index = i;
			
			TextView child = (TextView) ((LinearLayout)group.getChildAt(i)).getChildAt(0);
			textViews.add(child);
			
			/*���õ�һ��ΪĬ��ѡ��*/
			if(i==0){	
				child.setBackgroundResource(selImages[0]);
				child.setTextColor(Color.WHITE);
			}
			
			child.setOnClickListener(new OnClickListener() {
			
				@Override
				public void onClick(View arg0) {
					//�޸�tab������	
					setTabTextBg(context, index);
					
					//����fragment���л�
					listener.OnTabChanged(index);
				}
			});
			
		}
		this.addView(group, params);
		
		
//		for(int i=0; i<images.length; i++){
//			
//			final int index = i;
//							
//			//���ÿ������
//			final View child = inflater.inflate(R.layout.main_tab_item, null);
//			final TextView name = (TextView)child.findViewById(R.id.tab_item_name);
//			name.setText(names[i]);	
//			name.setBackgroundResource(images[0]);
//			
//			/*���õ�һ��ΪĬ��ѡ��*/
//			if(i==0){	
//				name.setBackgroundResource(selImages[0]);
//				name.setTextColor(Color.WHITE);
//			}
//						
//			this.addView(child, params);
//			
//			//���ؼ���ӵ�List�У��Ժ��޸���������
//			childs.add(child);
//			textViews.add(name);
//								
//			//��ӵ���¼�
//			child.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View arg0) {
//					//�޸�tab������	
//					setTabTextBg(context, index);
//					
//					//����fragment���л�
//					listener.OnTabChanged(index);
//				}
//			});
//		}
		
	}
	
	//���õײ���������ɫ
	public void setTabTextBg(Context context, int index ){
		for(int i=0; i<names.length; i++){
			if(index == i){	
				textViews.get(i).setTextColor(Color.WHITE);
				textViews.get(i).setBackgroundResource(selImages[i]);
				System.out.println("���"+index);
			}else{
				textViews.get(i).setTextColor(Color.parseColor("#4e4e4e"));
				textViews.get(i).setBackgroundResource(images[i]);
				System.out.println("����"+index);
			}
		}	
	}
	
	//����interface����MainActivityͨ��
	public interface OnTabChangedListener{
		void OnTabChanged(int index);
	}

	private OnTabChangedListener listener;

	public void setOnTabChangedListener(OnTabChangedListener listener){
		this.listener = listener;
	}
	
}

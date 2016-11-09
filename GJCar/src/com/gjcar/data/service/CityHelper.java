package com.gjcar.data.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.gjcar.data.bean.CityShow;
import com.gjcar.data.bean.Letter_CityShow;
import com.gjcar.utils.HandlerHelper;
import com.gjcar.utils.ToastHelper;
import com.gjcar.view.listview.QQListView_City;

public class CityHelper {

	/**
	 * ��ȡ��ĸ�����֣�w {"�人","����","����"} S{""""""}
	 * array_cityLetter:W H X H K J T
	 * 
	 */
	public List<Letter_CityShow> getCitysData(List<CityShow> citylist){
		List<Letter_CityShow> mycitylist = new ArrayList<Letter_CityShow>();
		
		/*����26��Ӣ����ĸ*/
		String[] letter = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
			
		for(int i=0;i<letter.length;i++){
		
			Letter_CityShow letter_citys = new Letter_CityShow();
			
			for(int j=0;j<citylist.size();j++){
				
				if(letter[i].equals(citylist.get(j).belong)){
					//��Ӷ������
					
					letter_citys.citylist.add(citylist.get(j));
				}
			}
			
			if(letter_citys.citylist.size() != 0){
				
				letter_citys.letter = letter[i];
				mycitylist.add(letter_citys);
			}
		}
			
//			Letter_CityShow letter_citys = new Letter_CityShow();
//			letter_citys.letter = "A";
//			List<CityShow> list = new ArrayList<CityShow>();
//			CityShow a = new CityShow();a.cityName = "��ɽ";
//			CityShow b = new CityShow();b.cityName = "������1";
//			CityShow c = new CityShow();c.cityName = "������2";
//			CityShow e = new CityShow();e.cityName = "������3";
//			CityShow f = new CityShow();f.cityName = "������4";
//			CityShow g = new CityShow();g.cityName = "������5";
//			CityShow h = new CityShow();h.cityName = "������6";
//			CityShow i1 = new CityShow();i1.cityName = "������7";
//			CityShow j = new CityShow();j.cityName = "������7";
//			CityShow k = new CityShow();k.cityName = "������7";
//			CityShow k1 = new CityShow();k.cityName = "������7";
//			CityShow k2 = new CityShow();k.cityName = "������7";
//			CityShow k3 = new CityShow();k.cityName = "������7";
//			CityShow k4 = new CityShow();k.cityName = "������7";
//			CityShow k5 = new CityShow();k.cityName = "������7";
//			list.add(a);
//			list.add(b);
//			list.add(c);
//			list.add(e);
//			list.add(f);
//			list.add(g);
//			list.add(h);
//			list.add(i1);
//			list.add(j);
//			list.add(k);
//			list.add(k1);
//			list.add(k2);
//			list.add(k3);
//			list.add(k4);
//			list.add(k5);
//			letter_citys.citylist = list;
//			mycitylist.add(letter_citys);
		return mycitylist;
	}
	
	public void expandAll(QQListView_City exlistview, int size){
		
		for (int i = 0; i < size; i++) {
			exlistview.expandGroup(i);
		}
	}
	
	public void setClickListener(final Context context, final ExpandableListView exListView,final Handler handler,final int what){

		exListView.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				System.out.println("groupPosition"+groupPosition+"-"+childPosition);
				if(groupPosition == 0 || groupPosition == 1 || groupPosition == 2){
					return false;
				}
				Bundle bundle = new Bundle();
				bundle.putInt("groupPosition", groupPosition);
				bundle.putInt("childPosition", childPosition);
				HandlerHelper.sendBundle(handler, what, bundle);				
				return false;
			}
		});

	}
	
	
}

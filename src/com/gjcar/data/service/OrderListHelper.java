package com.gjcar.data.service;

import java.util.ArrayList;

import com.gjcar.data.bean.Order;

public class OrderListHelper {

	public static ArrayList<Order> getList(ArrayList<Order> list, boolean isDoorToDoor){
		
		ArrayList<Order> myList = new ArrayList<Order>();
		
		if(isDoorToDoor){//"��֧��","���µ�","���µ�","���µ�","������", "������ ","������ ","�ѻ���","�����","��ȡ��"
			
			for (int i = 0; i < list.size(); i++) {
				
				if(list.get(i).orderState.intValue() != 8 && list.get(i).orderState.intValue() != 9){
					myList.add(list.get(i));
				}
			}
		}else{//"��֧��","���µ� ","������","�ѻ���", "����� ","��ȡ�� ","NoShow"
			
			for (int i = 0; i < list.size(); i++) {
				
				if(list.get(i).orderState.intValue() != 5 && list.get(i).orderState.intValue() != 6){
					myList.add(list.get(i));
				}
			}
		}
		
		return myList;
		
	}
}

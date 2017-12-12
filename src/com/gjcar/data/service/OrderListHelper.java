package com.gjcar.data.service;

import java.util.ArrayList;

import com.gjcar.data.bean.Order;

public class OrderListHelper {

	public static ArrayList<Order> getList(ArrayList<Order> list, boolean isDoorToDoor){
		
		ArrayList<Order> myList = new ArrayList<Order>();
		
		if(isDoorToDoor){//"待支付","已下单","已下单","已下单","租赁中", "租赁中 ","租赁中 ","已还车","已完成","已取消"
			
			for (int i = 0; i < list.size(); i++) {
				
				if(list.get(i).orderState.intValue() != 8 && list.get(i).orderState.intValue() != 9){
					myList.add(list.get(i));
				}
			}
		}else{//"待支付","已下单 ","租赁中","已还车", "已完成 ","已取消 ","NoShow"
			
			for (int i = 0; i < list.size(); i++) {
				
				if(list.get(i).orderState.intValue() != 5 && list.get(i).orderState.intValue() != 6){
					myList.add(list.get(i));
				}
			}
		}
		
		return myList;
		
	}
}

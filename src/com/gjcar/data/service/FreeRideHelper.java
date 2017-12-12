package com.gjcar.data.service;

import java.util.List;

import com.gjcar.data.bean.StoreShows;

public class FreeRideHelper {

	public String[] getStores(List<StoreShows> list){
		if(list == null){System.out.println("空");}
		System.out.println("门店多少"+list.size());
		String[] stores = new String[list.size()];
		
		for (int i = 0; i < list.size(); i++) {
			System.out.println("商店名称"+list.get(i).storeName);
			stores[i] = list.get(i).storeName;
		}
		
		return stores;
	}
	
	public int[] getIds(int size){
		
		int[] Ids = new int[size];
		
		for (int i = 0; i < size; i++) {
			Ids[i] = i;
		}
		
		return Ids;
	}
}

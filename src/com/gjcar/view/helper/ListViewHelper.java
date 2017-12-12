package com.gjcar.view.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gjcar.app.R;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ListViewHelper {

//	private static Context context_simple;
//	private static SimpleAdapter simpleAdapter;
//	private static ListView listview_simple;
//	private static List<Map<String, Object>> data_simple;
//	private static int layout_simple;
//	private static String[] keys_simple;
//	private static int[] ids_simple;
	
	public static void SimpleAdapter(Context context, ListView listview, List<Map<String, Object>> data,int layout_item, String[] keys, int[] ids){
//		context_simple = context;
//		listview_simple = listview;
		listview.setVisibility(View.VISIBLE);
		SimpleAdapter simpleAdapter = new SimpleAdapter(context, data, layout_item, keys, ids);
//		layout_simple = layout_item;
//		data_simple = data;
//		keys_simple = keys;
//		ids_simple = ids;
		
		listview.setAdapter(simpleAdapter);
	
	}
	
//	public static void hide(){
//		listview_simple.setVisibility(View.GONE);
//	}
	
//	public static void flush(List<Map<String, Object>> data){
//		data_simple = data;
//		listview_simple.setVisibility(View.VISIBLE);
//		simpleAdapter = new SimpleAdapter(context_simple, data, layout_simple ,keys_simple, ids_simple);
//		listview_simple.setAdapter(simpleAdapter);
//	}
}

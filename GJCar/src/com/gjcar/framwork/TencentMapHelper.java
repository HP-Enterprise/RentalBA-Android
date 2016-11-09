package com.gjcar.framwork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.gjcar.data.data.Public_Param;
import com.gjcar.utils.HandlerHelper;
import com.gjcar.utils.NetworkHelper;
import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.BaseObject;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.Location;
import com.tencent.lbssearch.object.param.SearchParam;
import com.tencent.lbssearch.object.param.SearchParam.Nearby;
import com.tencent.lbssearch.object.param.SuggestionParam;
import com.tencent.lbssearch.object.result.SearchResultObject;
import com.tencent.lbssearch.object.result.SearchResultObject.SearchResultData;
import com.tencent.lbssearch.object.result.SuggestionResultObject;
import com.tencent.lbssearch.object.result.SuggestionResultObject.SuggestionData;

public class TencentMapHelper {

	/**************************************************************************************************************************************
	 *
	 *关键词搜索动画
	 *
	 *
	 ***************************************************************************************************************************************/	
	private static String city_suggestion;
	/**
	 * 关键字提示
	 */
	public static void suggestion(Context context, final String city, String keyword, final Handler handler, final int what) {
		
		city_suggestion = city;
		
		/*判断是否有网络*/
		if(!NetworkHelper.isNetworkAvailable(context)){
			HandlerHelper.sendStringObject(handler, what, "noNet", null);
			return;
		}
		TencentSearch tencentSearch = new TencentSearch(context);
		SuggestionParam suggestionParam = new SuggestionParam().region(city).keyword(keyword);
	
		tencentSearch.suggestion(suggestionParam, new HttpResponseListener() {

			@Override
			public void onSuccess(int arg0, Header[] headers, BaseObject objcet) {
				// 没有搜索到
				if (objcet == null ) {
					HandlerHelper.sendStringObject(handler, what, "fail", null);
					return;
				}System.out.println("城市："+city);
//				for( SuggestionData data :  (( SuggestionResultObject)objcet).data){
//				System.out.println("s_title"+data.title);
//				System.out.println("s_address"+data.address);
//				System.out.println("s_latitude"+data.location.lat);
//				System.out.println("s_longtitude"+data.location.lng);
//				System.out.println("s_city"+data.city);
//			}
				//搜索到了分：没有或有
				List<SuggestionData> data = (( SuggestionResultObject)objcet).data;
				
				int size = 0;
				
				List<Map<String, Object>> list_data = new ArrayList<Map<String,Object>>();
				for (int i = 0; i < data.size() && size <= 10; i++) {
					
					if(data.get(i).city == null || data.get(i).city.equals("")){
						continue;
					}
					
					if(data.get(i).city.equals(city_suggestion+"市")){
						
						Map<String ,Object> map = new HashMap<String, Object>();
						map.put("title", data.get(i).title);
						map.put("address", data.get(i).address);
						if(data.get(i).location == null){
							continue;
							
						}else{
							map.put("latitude",  (double)data.get(i).location.lat);
							map.put("longitude", (double)data.get(i).location.lng);
							System.out.println("地址"+1);
							if(Public_Param.points != null && Public_Param.points.size() > 2){
								
								if(!new BaiduMapHelper().isPolygon(Public_Param.points, new LatLng((double)data.get(i).location.lat, (double)data.get(i).location.lng))){
									System.out.println("超出了范围"+data.get(i).address);
									continue;//超出范围就不添加：不执行list_data.add(map);
								}
								
							}System.out.println("地址a"+1);
						}
						list_data.add(map);
						size = size + 1;System.out.println("ok"+i);
					}
					
				}
				
				if(list_data.size() == 0){System.out.println("fail");
					HandlerHelper.sendStringObject(handler, what, "fail", null);
				}else{System.out.println("ok");
					HandlerHelper.sendStringObject(handler, what, "ok", list_data);
				}

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				HandlerHelper.sendStringObject(handler, what, "fail", null);
				System.out.println("失败"+arg2);
			}
		});
	}

	/**
	 * 中心点：搜索周边
	 */
	public static void search_nearby(Context context, LatLng latlng, String keyword, final Handler handler, final int what) {		
		
		/*判断是否有网络*/
		if(!NetworkHelper.isNetworkAvailable(context)){
			HandlerHelper.sendStringObject(handler, what, "noNet", null);
			return;
		}
		
		/*搜索*/
		TencentSearch tencentSearch = new TencentSearch(context);
		
		Location location = new Location().lat((float) latlng.latitude).lng((float) latlng.longitude);//搜索条件
		Nearby nearBy = new Nearby().point(location).r(1000);
		
		SearchParam object = new SearchParam().keyword(keyword).boundary(nearBy);
	
		tencentSearch.search(object,new HttpResponseListener(){

			@Override
			public void onSuccess(int arg0, Header[] headers, BaseObject object) {
				
				// 没有搜索到
				if (object == null ) {
					//HandlerHelper.sendStringObject(handler, what, "fail", null);
					return;
				}

				for( SearchResultData  data :  (( (SearchResultObject)object).data)){
					System.out.println("s_title"+data.title);
					System.out.println("s_address"+data.address);
					System.out.println("s_latitude"+data.location.lat);
					System.out.println("s_longtitude"+data.location.lng);
					
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				
			}});			
	
	}
}

package com.gjcar.activity.fragment1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baidu.mapapi.search.core.CityInfo;
import com.gjcar.annotation.ContentView;
import com.gjcar.annotation.ContentWidget;
import com.gjcar.app.R;
import com.gjcar.data.adapter.Letter_Right_Adapter;
import com.gjcar.data.bean.CityShow;
import com.gjcar.data.bean.Letter_CityShow;
import com.gjcar.data.bean.TradeAreaShow;
import com.gjcar.data.data.Public_BaiduTJ;
import com.gjcar.data.service.CityHelper;
import com.gjcar.utils.AnnotationViewUtils;
import com.gjcar.utils.HandlerHelper;
import com.gjcar.utils.HttpHelper;
import com.gjcar.utils.SQL_Dao;
import com.gjcar.utils.SQL_OpenHelper;
import com.gjcar.utils.ToastHelper;
import com.gjcar.view.helper.LoadAnimateHelper;
import com.gjcar.view.helper.TitleBarHelper;
import com.gjcar.view.listview.QQListAdapter_City;
import com.gjcar.view.listview.QQListView_City;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

@ContentView(R.layout.activity_citylist)
public class Activity_City_List extends Activity{

	@ContentWidget(id = R.id.expandableListView) QQListView_City expandableListView;
	@ContentWidget(id = R.id.letter_listview) ListView letter_listview;
	
	/*Handler*/
	private Handler handler;
	private final static int Request_Hot = 1;
	private final static int Request_City = 2;
	private final static int Data_Show = 3;
	private final static int Click_City = 4;
	private final static int Click_History = 5;
	private final static int Click_Location = 6;
	private final static int Request_Free_City = 7;
	
	private int request_count = 0;//���������2��
	private boolean isOut = false;
	
	/*��ȡ����*/
	ArrayList<CityShow> citys_all = new ArrayList<CityShow>();
	ArrayList<CityShow> citys_hot = new ArrayList<CityShow>();
	List<Letter_CityShow> list = new ArrayList<Letter_CityShow>();
	List<CityShow> citys_history;
		
	/*��ȡ���ݲ���:"loc_cityId","loc_cityName","loc_latitude","loc_longitude"*/	
	private CityShow locCity = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AnnotationViewUtils.injectObject(this, this);
		
		/*����Ĭ�ϳ���*/
		initHistoryCity();
		
		/*��ǰ��λ����*/
		initParams();
		
		/*handler*/
		initHandler();
		
		/*���ر���*/
		TitleBarHelper.Back(this, "ѡ�����", 0);
		
		/*���ض���*/
		LoadAnimateHelper.Search_Animate(this, R.id.activity, handler, Request_City, true,true,1);
		
		/*��������*/
		
		if(getIntent().hasExtra("cityId")){

			new HttpHelper().initData(HttpHelper.Method_Get, this, "api/china/cityHasStore?isHot=1", null, null, handler, Request_Hot, 1, new TypeReference<ArrayList<CityShow>>() {});			
			
			new HttpHelper().initData(HttpHelper.Method_Get, this, "api/freeRide/returnCarCity?getCarCityId="+getIntent().getIntExtra("cityId", -1), null, null, handler, Request_City, 1, new TypeReference<ArrayList<CityShow>>() {});

		}else{
			initData();
		}
		
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
		Public_BaiduTJ.pageStart(this, Public_BaiduTJ.Activity_City_List);	

	}
	
	@Override
	protected void onPause() {

		super.onPause();
		Public_BaiduTJ.pageEnd(this, Public_BaiduTJ.Activity_City_List);	
	}
	
	private void initParams() {//"loc_cityId","loc_cityName","loc_latitude","loc_longitude"
		
		int loc_cityId = getIntent().getIntExtra("loc_cityId", -1);
		String loc_cityName = getIntent().getStringExtra("loc_cityName");
		locCity = new CityShow(loc_cityId, loc_cityName);
				
	}

	private void initData() {System.out.println("1");

		new HttpHelper().initData(HttpHelper.Method_Get, this, "api/china/cityHasStore?isHot=1&available=1&isLocation=1", null, null, handler, Request_Hot, 1, new TypeReference<ArrayList<CityShow>>() {});
		
		new HttpHelper().initData(HttpHelper.Method_Get, this, "api/china/cityHasStore?available=1&isLocation=1", null, null, handler, Request_City, 1, new TypeReference<ArrayList<CityShow>>() {});
		System.out.println("��ַ�㶮��");
	}
	
	private void initHandler() {

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				switch (msg.what) {

					case Request_City:
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
						
							citys_all = (ArrayList<CityShow>)msg.obj;
								
							citys_history = getCityHistory(citys_history,citys_all);
							
				           	request_count++;
				           		
				           	handler.sendEmptyMessage(Data_Show);
				           		
				           	return;
						}
//						if(HandlerHelper.getString(msg).equals(HandlerHelper.DataFail) && !isOut){
//							new HttpHelper().initData(HttpHelper.Method_Get, Activity_City_List.this, "api/china/city", null, null, handler, Request_City, 1, new TypeReference<ArrayList<CityShow>>() {});
//							
//				           	System.out.println("����ʧ��");	            
//						}
						
						break;
					
					case Request_Hot:
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){

							citys_hot = (ArrayList<CityShow>)msg.obj;
							
				           	request_count++;
				           	
				           	handler.sendEmptyMessage(Data_Show);
				           	
				           	return;
						}
//						if(HandlerHelper.getString(msg).equals(HandlerHelper.DataFail) && !isOut){
//							new HttpHelper().initData(HttpHelper.Method_Get, Activity_City_List.this, "api/china/city?isHot=1", null, null, handler, Request_Hot, 1, new TypeReference<ArrayList<CityShow>>() {});
//							
//				           	System.out.println("����ʧ��hot");	            
//						}
						break;
						
					case Request_Free_City:
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){

							citys_all = (ArrayList<CityShow>)msg.obj;
							
				           	request_count++;
				           	
				           	handler.sendEmptyMessage(Data_Show);
				           
				           	return;
						}
						break;
						
					case Data_Show:
						
						if(request_count != 2){
							
			           		return;
			           	}
												
		           		LoadAnimateHelper.load_success_animation();
		           		
		           		/*QQListView*/
		           		final QQListView_City exListView = (QQListView_City)findViewById(R.id.expandableListView);	
		           		exListView.setVisibility(View.VISIBLE);
		           		exListView.setHeaderView(getLayoutInflater().inflate(R.layout.citylist_header,exListView,false));
						System.out.println("kaishi1");
						
						list = new CityHelper().getCitysData(citys_all);
						//citys_history = new SQL_Dao().findAll(new SQL_OpenHelper(Activity_City_List.this, null, null, 0).getWritableDatabase(),CityShow.class);

		           		QQListAdapter_City adatper = new QQListAdapter_City(Activity_City_List.this,exListView,locCity, list,citys_history, citys_hot, handler, Click_Location, Click_City, Click_History);
		           	
						exListView.setAdapter(adatper);
												
						//if (mHeaderView.getTop() != -1)263�е�-1��Ϊ0��
						//������ʽ��1.�ֶ�չ��2.����ʱ������getQQHeaderState
						//�����Ѿ�չ��������1�������У�����2�����ķ���ֵmHeaderView.getTop() ==0����ʼд����0������Ҳ���ᴥ�������Ծ�û�д�����ʽ��
						new CityHelper().expandAll(exListView, list.size()+3);	//չ��ȫ���������bug�����ǲ���ʾƯ����Ŀ
						
						new CityHelper().setClickListener(Activity_City_List.this, exListView, handler, Click_City);
						
						/*�Ҳ���ĸ*/						
						Letter_Right_Adapter adapter = new Letter_Right_Adapter(Activity_City_List.this, list);
						letter_listview.setAdapter(adapter);
						letter_listview.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int position, long arg3) {
//								exListView.setSelection(position);System.out.println("kkkkkkkkkkkk"+position);
								exListView.setSelectedGroup(position);System.out.println("kkkkkkkkkkkk"+position);
//								exListView.setSelectionFromTop(position, 0);System.out.println("kkkkkkkkkkkk"+position);
							}
						});
						break;
					 
					case Click_City:					
						Bundle bundle = msg.getData();//��ȡ������ݵĲ���
						int groupPosition = bundle.getInt("groupPosition");
						int childPosition = bundle.getInt("childPosition");
						
						Intent intent = new Intent();//���÷��ز���
						Bundle d = new Bundle();
						if(groupPosition == 2){
							d.putSerializable("cityShow",citys_hot.get(childPosition));
							saveCity(citys_hot.get(childPosition));
						}else{
							d.putSerializable("cityShow",list.get(groupPosition-3).citylist.get(childPosition));
							saveCity(list.get(groupPosition-3).citylist.get(childPosition));
						}
							
						intent.putExtras(d);
						setResult(groupPosition, intent);
						finish();
						//ToastHelper.showToastShort(Activity_CityList.this, ""+groupPosition+"+"+childPosition);
						
						
						break;
					case Click_History:	
						
						Bundle his_bundle = msg.getData();
						int index = his_bundle.getInt("index");		
						
						Intent intent_bundle = new Intent();
						Bundle d_his = new Bundle();
						d_his.putSerializable("cityShow",getCity_History(citys_history.get(index)));//Ҫ��id ���cityId
						saveCity_History(citys_history.get(index));
						intent_bundle.putExtras(d_his);
						
						setResult(0, intent_bundle);
						finish();
						System.out.println("index"+index);
						break;
					
					case Click_Location:

						Intent intent_loc = new Intent();
						Bundle loc_b = new Bundle();
						loc_b.putSerializable("cityShow",locCity);
						saveCity(locCity);
						intent_loc.putExtras(loc_b);
						
						setResult(101, intent_loc);
						finish();
						break;
						
					default:
						break;
				}
			}
		};
	}
	
	/*jjj*/
	private void initHistoryCity(){
		/*����*/
		SQLiteDatabase db = new SQL_OpenHelper(this, null, null, 0).getWritableDatabase();
		
			
//			CityShow show4 = new CityShow();
////			show.id = 3;	/*����id*/
//			show4.cityNum = "003";	/*���б��*/
//			show4.cityName = "����";	/*��������*/	
//			show4.parentNum = "001";	/*����ʡ�ݱ��*/
//			show4.latitude = 180.00;	/*γ��*/
//			show4.longitude = 190.00;	/*����*/
//			
//			show4.belong = "N";
//			show4.isHot = 1;
//			show4.label = "label";
//			show4.pinyin = "nanjing";
//			show4.storeShows = null;
//			show4.cityId = 3;
////			
//			new SQL_Dao().insert(db, show4);
//		}
		System.out.println("������ѯ��ʼ");
		System.out.println("����"+new SQL_Dao().count(db, CityShow.class));
		List<CityShow> l = new SQL_Dao().findAll(db,CityShow.class);
		System.out.println("��ѯ"+((List<CityShow>)new SQL_Dao().findAll(db,CityShow.class)).size());	
		getData(l);
	}
	
	private void getData(List<CityShow> l){
		
		List<CityShow> newlist = new ArrayList<CityShow>();
		
		if(l.size() == 0){
			citys_history = newlist;
			return;
		}
		
		/*��һ��*/
		newlist.add(l.get(0));
		
		/*�ڶ���*/
		for (int i = 1; i < l.size(); i++) {
			if(newlist.get(0).cityId.intValue() != l.get(i).cityId.intValue()){
				newlist.add(l.get(i));
				 break;
			}
		}
		
		/*������*/
		if(newlist.size() == 2){
			for (int j = 2; j < l.size(); j++) {
				if((newlist.get(0).cityId.intValue() != l.get(j).cityId.intValue()) && (newlist.get(1).cityId.intValue() != l.get(j).cityId.intValue())){
					newlist.add(l.get(j));
					break;
				}
			}
		}
		System.out.println("��ʷ����###########################");
		for (int i = 0; i < newlist.size(); i++) {
			System.out.println("����"+newlist.get(i).cityName);
			System.out.println("����id"+newlist.get(i).cityId);
			System.out.println("�������к�"+newlist.get(i).id);
		}
		
		citys_history = newlist;
		
	}
	
	@Override
	protected void onDestroy() {
		isOut = true;
		super.onDestroy();
	}
	
	public void saveCity(CityShow cityInfo){
				
		SQLiteDatabase db = new SQL_OpenHelper(this, null, null, 0).getWritableDatabase();
		
		cityInfo.cityId = cityInfo.id;
		
		new SQL_Dao().insert(db, cityInfo);
	}
	
	public void saveCity_History(CityShow cityInfo){
		
		SQLiteDatabase db = new SQL_OpenHelper(this, null, null, 0).getWritableDatabase();
		new SQL_Dao().insert(db, cityInfo);
	}
	
	public CityShow getCity_History(CityShow cityInfo){
		
		cityInfo.id = cityInfo.cityId;
		return cityInfo;
	}
	
	public ArrayList<CityShow> getCityHistory(List<CityShow> list_history, ArrayList<CityShow> list_all){
		
		ArrayList<CityShow> list = new ArrayList<CityShow>();
		
		for (int i = 0; i < list_history.size(); i++) {
			
			for (int j = 0; j < list_all.size(); j++) {
				
				if(list_history.get(i).cityName.equals(list_all.get(j).cityName)){
					
					list.add(list_history.get(i));
				}
			}
			
		}
		
		return list;
	}
}

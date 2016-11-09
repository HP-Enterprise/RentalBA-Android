package com.gjcar.activity.fragment2;

import com.alibaba.fastjson.JSONObject;
import com.gjcar.activity.fragment1.Activity_City_List;
import com.gjcar.activity.user.ValidationHelper;
import com.gjcar.annotation.ContentWidget;
import com.gjcar.app.R;
import com.gjcar.data.bean.CityShow;
import com.gjcar.data.data.Public_Param;
import com.gjcar.data.data.Public_SP;
import com.gjcar.utils.AnnotationViewFUtils;
import com.gjcar.utils.HttpHelper;
import com.gjcar.utils.IntentHelper;
import com.gjcar.utils.SharedPreferenceHelper;
import com.gjcar.utils.TimeHelper;
import com.gjcar.utils.ValidateHelper;
import com.gjcar.view.dialog.DateTimePickerHelper;
import com.gjcar.view.dialog.SelectDailog;
import com.gjcar.view.dialog.TimePickerHelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Fragment_hhhhh extends Fragment{

	/*�ؼ�*/
	@ContentWidget(id = R.id.image) ImageView image;
	
	@ContentWidget(click = "onClick") LinearLayout take_city_lin;
	@ContentWidget(id = R.id.take_city) TextView take_city;
	
	@ContentWidget(id = R.id.company) EditText company;
	@ContentWidget(id = R.id.airport) EditText airport;
	@ContentWidget(id = R.id.number) EditText number;
	@ContentWidget(click = "onClick") LinearLayout take_date_lin;
	@ContentWidget(id = R.id.take_date) TextView take_date;
	@ContentWidget(click = "onClick") LinearLayout take_time_lin;
	@ContentWidget(id = R.id.take_time) TextView take_time;
	@ContentWidget(id = R.id.address) EditText address;
	
	@ContentWidget(click = "onClick") Button ok;
	
	/*����*/
	private final static int RequestCode_Take = 1;
	private final static int RequestCode_Return = 2;
	private int getCarCityId = -1;
	private int returnCityId = -1;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment2_fffff, null);
		AnnotationViewFUtils.injectObject(this, getActivity(), view);
		
		return view;                                                                                                                                                                                                                                                                                                        
	}
	
	public void onClick(View view){
		
		switch (view.getId()) {
		
			case R.id.take_city_lin://"id","cityName","latitude","longitude"				
				IntentHelper.Fragment_startActivityForResult_Extra(getActivity(), this, Activity_City_List.class,RequestCode_Take, new String[]{"loc_cityId","loc_cityName","loc_latitude","loc_longitude"}, new Object[]{SharedPreferenceHelper.getInt(getActivity(), Public_SP.City, "id", -1), SharedPreferenceHelper.getString(getActivity(), Public_SP.City, "cityName"),(double)SharedPreferenceHelper.getFloat(getActivity(), Public_SP.City, "latitude"),(double)SharedPreferenceHelper.getFloat(getActivity(), Public_SP.City, "longitude")}, new int[]{IntentHelper.Type_Int, IntentHelper.Type_String,IntentHelper.Type_Double,IntentHelper.Type_Double});				
				break;
				
			case R.id.return_city_lin:
				IntentHelper.Fragment_startActivityForResult_Extra(getActivity(), this, Activity_City_List.class,RequestCode_Return, new String[]{"loc_cityId","loc_cityName","loc_latitude","loc_longitude"}, new Object[]{SharedPreferenceHelper.getInt(getActivity(), Public_SP.City, "id", -1), SharedPreferenceHelper.getString(getActivity(), Public_SP.City, "cityName"),(double)SharedPreferenceHelper.getFloat(getActivity(), Public_SP.City, "latitude"),(double)SharedPreferenceHelper.getFloat(getActivity(), Public_SP.City, "longitude")}, new int[]{IntentHelper.Type_Int, IntentHelper.Type_String,IntentHelper.Type_Double,IntentHelper.Type_Double});				
				break;
				
			case R.id.take_date_lin:
				
				if(take_date.getText().toString().equals("������ӻ�����")){
					new DateTimePickerHelper().pickTime(getActivity(), take_date, TimeHelper.getNowTime_YMD(), "ѡ��ӻ�����");
				}else{
					new DateTimePickerHelper().pickTime(getActivity(), take_date, take_date.getText().toString(), "ѡ��ӻ�����");
				}					
				break;
				
			case R.id.take_time_lin:
				
				if(take_time.getText().toString().equals("������ӻ�ʱ��")){
					new TimePickerHelper().pickTime(getActivity(), take_time, TimeHelper.getNowTime_Hm(), "ѡ��ӻ�ʱ��");
				}else{
					new TimePickerHelper().pickTime(getActivity(), take_time, take_time.getText().toString(), "ѡ��ӻ�ʱ��");
				}				
				break;
				
			case R.id.ok:
			
				/*�ж������Ƿ���ȷ*/
				if(!ValidateHelper.Validate(getActivity(), new boolean[]{
					take_city.getText().toString().equals("��ѡ���ó�����"),ValidationHelper.isNull(company),ValidationHelper.isNull(airport),ValidationHelper.isNull(number),ValidationHelper.isNull(address),
					take_date.getText().toString().equals("������ӻ�����"),take_time.getText().toString().equals("������ӻ�ʱ��"),!TimeHelper.isLate(take_date.getText().toString()+" "+take_time.getText().toString())}, 
						new String[]{"��ѡ���ó�����","���չ�˾������д","����������д","����ű�����д", "�ʹ�ص������д",
									  "�ӻ����ڱ�����д","�ӻ�ʱ�������д","�ӻ�ʱ��������ڵ�ǰʱ��"})){
					return;
				}
				System.out.println("��֤���");
				
				/*����*/
				Public_Param.transfer_params.city = take_city.getText().toString();
				Public_Param.transfer_params.company = company.getText().toString();
				Public_Param.transfer_params.airport = airport.getText().toString();
				Public_Param.transfer_params.number = number.getText().toString();
				Public_Param.transfer_params.time = take_date.getText().toString()+" "+take_time.getText().toString();
				
				/*��ת*/
				break;
				
			default:
				break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(data == null){//��back�򷵻ؼ��˳�
			
			return;
		}
		
		switch (requestCode) {
				
			case RequestCode_Take:
				if(!data.hasExtra("cityShow")){
					return;
				}
				CityShow show = (CityShow) data.getExtras().get("cityShow");
				
				getCarCityId = show.id.intValue();
				take_city.setText(show.cityName);
				break;
			
			case RequestCode_Return:
				
				if(!data.hasExtra("cityShow")){
					return;
				}
				CityShow show_return = (CityShow) data.getExtras().get("cityShow");
				returnCityId = show_return.id.intValue();
			
				break;
				
			default:
				break;
		}
						
	}

}

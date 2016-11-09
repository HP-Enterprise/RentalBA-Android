package com.gjcar.activity.fragment2;

import com.gjcar.activity.fragment1.Activity_City_List;
import com.gjcar.activity.fragment3.Activity_FreeRide_List;
import com.gjcar.annotation.ContentWidget;
import com.gjcar.app.R;
import com.gjcar.data.bean.CityShow;
import com.gjcar.data.bean.OtherDrive_Params;
import com.gjcar.data.data.Public_BaiduTJ;
import com.gjcar.data.data.Public_Param;
import com.gjcar.data.data.Public_SP;
import com.gjcar.utils.AnnotationViewFUtils;
import com.gjcar.utils.IntentHelper;
import com.gjcar.utils.NetworkHelper;
import com.gjcar.utils.SharedPreferenceHelper;
import com.gjcar.utils.TimeHelper;
import com.gjcar.utils.ValidateHelper;
import com.gjcar.view.dialog.DateTimePickerHelper;
import com.gjcar.view.dialog.DateTimePicker_DriveByOther;

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

public class Fragment2 extends Fragment{

	/*控件*/
	@ContentWidget(id = R.id.image) ImageView image;
	
	@ContentWidget(id = R.id.take_city) TextView take_city;
	@ContentWidget(id = R.id.return_city) TextView return_city;
	@ContentWidget(id = R.id.take_time) TextView take_time;
	
	@ContentWidget(id = R.id.days) EditText days;
	@ContentWidget(id = R.id.take_address) EditText take_address;
	@ContentWidget(id = R.id.return_address) EditText return_address;
	
	@ContentWidget(click = "onClick") LinearLayout take_city_lin,return_city_lin,take_time_lin;
	@ContentWidget(click = "onClick") Button ok;
	
	/*其它*/
	private final static int RequestCode_Take = 1;
	private final static int RequestCode_Return = 2;
	private int getCarCityId = -1;
	private int returnCityId = -1;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment2, null);
		AnnotationViewFUtils.injectObject(this, getActivity(), view);
		
		return view;                                                                                                                                                                                                                                                                                                         
	}
	
	@Override
	public void onResume() {
		
		super.onResume();
		Public_BaiduTJ.pageStart(getActivity(), Public_BaiduTJ.Fragment_LongRentalCar);	
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Public_BaiduTJ.pageEnd(getActivity(), Public_BaiduTJ.Fragment_LongRentalCar);	
	}
	
	public void onClick(View view){
		
		switch (view.getId()) {
			
			case R.id.take_city_lin://"id","cityName","latitude","longitude"				
				IntentHelper.Fragment_startActivityForResult_Extra(getActivity(), this, Activity_City_List.class,RequestCode_Take, new String[]{"loc_cityId","loc_cityName","loc_latitude","loc_longitude"}, new Object[]{SharedPreferenceHelper.getInt(getActivity(), Public_SP.City, "id", -1), SharedPreferenceHelper.getString(getActivity(), Public_SP.City, "cityName"),(double)SharedPreferenceHelper.getFloat(getActivity(), Public_SP.City, "latitude"),(double)SharedPreferenceHelper.getFloat(getActivity(), Public_SP.City, "longitude")}, new int[]{IntentHelper.Type_Int, IntentHelper.Type_String,IntentHelper.Type_Double,IntentHelper.Type_Double});				
				break;
				
			case R.id.return_city_lin:
				IntentHelper.Fragment_startActivityForResult_Extra(getActivity(), this, Activity_City_List.class,RequestCode_Return, new String[]{"loc_cityId","loc_cityName","loc_latitude","loc_longitude"}, new Object[]{SharedPreferenceHelper.getInt(getActivity(), Public_SP.City, "id", -1), SharedPreferenceHelper.getString(getActivity(), Public_SP.City, "cityName"),(double)SharedPreferenceHelper.getFloat(getActivity(), Public_SP.City, "latitude"),(double)SharedPreferenceHelper.getFloat(getActivity(), Public_SP.City, "longitude")}, new int[]{IntentHelper.Type_Int, IntentHelper.Type_String,IntentHelper.Type_Double,IntentHelper.Type_Double});				
				break;
	
			case R.id.take_time_lin:
				
				if(take_time.getText().toString().equals("请选择时间")){
					new DateTimePicker_DriveByOther().pickTime(getActivity(), take_time, TimeHelper.getNowTime_YMDHM(), "选择时间");
				}else{
					new DateTimePicker_DriveByOther().pickTime(getActivity(), take_time, take_time.getText().toString(), "选择时间");
				}	
				break;
				
			case R.id.ok:
				
				/*验证*/
				if(!ValidateHelper.Validate(getActivity(), new boolean[]{getCarCityId == -1, returnCityId == -1, take_time.getText().toString().equals("请选择时间"),days.getText() == null || days.getText().toString().equals(""),
						take_address.getText() == null || take_address.getText().toString().equals(""),return_address.getText() == null || return_address.getText().toString().equals("")},
					new String[]{"请选择xx城市","请选择yy城市","请选择xx时间","请输入xx天数","请选择xx上车地点","请选择xx下车地点"})){
						return;
				}
		
				/*参数*/
				System.out.println("城市"+take_city.getText().toString());
				System.out.println("时间"+take_time.getText());
				System.out.println(""+take_time.getText());
				System.out.println(""+take_address.getText());
				System.out.println(""+return_address.getTag());
				System.out.println(""+days.getTag());
				
				Public_Param.other_params.take_city = take_city.getText().toString();
				Public_Param.other_params.return_city = return_city.getText().toString();
				Public_Param.other_params.take_time = take_time.getText().toString();
				Public_Param.other_params.days = days.getText().toString();
				Public_Param.other_params.up_address = take_address.getText().toString();
				Public_Param.other_params.down_address = return_address.getText().toString();
	
				/*跳转*/
				
				break;
				
			default:
				break;
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(data == null){//按back或返回键退出
			
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
				return_city.setText(show_return.cityName);
				break;
				
			default:
				break;
		}
						
	}

}

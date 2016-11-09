package com.gjcar.activity.fragment3;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.gjcar.annotation.ContentWidget;
import com.gjcar.app.R;
import com.gjcar.data.data.Public_Param;
import com.gjcar.utils.AnnotationViewUtils;
import com.gjcar.utils.StringHelper;
import com.gjcar.utils.TimeHelper;
import com.gjcar.view.helper.TitleBarHelper;

public class Activity_FreeRide_Order_Submit_test1 extends Activity{

	@ContentWidget(id = R.id.c_level) TextView c_level;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		AnnotationViewUtils.injectObject(this, this);

		Public_Param.list_order_activity.add(this);
		
		/*Handler*/
		//initHandler();
		
		/*标题*/
		TitleBarHelper.Back(this, "确认订单", 0);
		
		/*获取参数*/
		initView();
		
		/*加载费用*/
		//Request_AddService();
		//Request_AmountDetail();
		
	}

	private void initView() {
		
		/*车型信息*/
//		holder.a_model.setText(Public_Param.freeRide.vehicleShow.vehicleModelShow.model);	
//		
//		String carGroup = StringHelper.getCarGroup(Public_Param.freeRide.vehicleShow.vehicleModelShow.carGroup);
//		String carTrunk = StringHelper.getCarTrunk(Public_Param.freeRide.vehicleShow.vehicleModelShow.carTrunk);
//		if(carTrunk.equals("1")){
//			carTrunk = "3";
//		}
//		String seats = Public_Param.freeRide.vehicleShow.vehicleModelShow.seats.toString();
//		holder.a_note.setText(carGroup+"/"+carTrunk+"厢"+seats+"座");
//
//		/*取车信息*/
//		holder.b_take_city.setText(Public_Param.freeRide.takeCarCityShow.cityName);	
//		holder.b_take_time.setText(TimeHelper.getDateWeekTime(Public_Param.freeRide.takeCarDateStart));	
//		holder.b_money.setText("￥"+Public_Param.freeRide.price.toString());	
//		holder.b_days.setText(Public_Param.freeRide.maxRentalDay.toString());	
//		holder.b_return_city.setText(Public_Param.freeRide.returnCarCityShow.cityName);	
//		holder.b_return_time.setText(TimeHelper.getDateWeekTime(TimeHelper.getDateTime_YMD_XDays(Public_Param.freeRide.maxRentalDay.intValue(), Public_Param.freeRide.takeCarDateStart)));	

	}
}

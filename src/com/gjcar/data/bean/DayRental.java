package com.gjcar.data.bean;

public class DayRental {

	public Integer id;
	public String date;
	public String rentalAmount;
	
	/*是否有日期*/
	public boolean isOk = false;
	
	/*日期中是否有价格*/
	public boolean isOk_isHasMoney = false;//isOk = true;是时间，但是数据位null
	
	/*是否标注为租车时间*/
	public boolean isTime = false;//是否是租车时间
}

package com.gjcar.data.bean;

public class DayRental {

	public Integer id;
	public String date;
	public String rentalAmount;
	
	/*�Ƿ�������*/
	public boolean isOk = false;
	
	/*�������Ƿ��м۸�*/
	public boolean isOk_isHasMoney = false;//isOk = true;��ʱ�䣬��������λnull
	
	/*�Ƿ��עΪ�⳵ʱ��*/
	public boolean isTime = false;//�Ƿ����⳵ʱ��
}

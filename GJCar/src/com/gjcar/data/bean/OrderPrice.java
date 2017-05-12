package com.gjcar.data.bean;

import java.util.List;

public class OrderPrice {

	public Integer averagePrice;
	public Integer daySum;
	public Integer prepayAmount;
	public Integer basicInsuranceAmount;
	public Integer delayAmount;
	public Integer payment;//支付方式
	
	public String preAuthorization;//预授权
	
	public String reduce;
	
	public ServiceAmount poundageAmount;
	
	public List<ServiceAmount> doorToDoor;

	public Integer totalAmount;
	public Integer totalPrepayAmount;
	public Integer totalBasicInsuranceAmount;
	public Integer totalDelayAmount;
	public Float totalPrice;
	
	public Integer toStoreReduce;
	
	public Object rentalIds;
	
	public List<ActivityShow> activityShows;
}

package com.gjcar.data.bean;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;


public class OrderParams {
	
	/*订单服务费*/
	public List<ServiceAmount> server_list = new ArrayList<ServiceAmount>();
	public List<ServiceAmount> all_list = new ArrayList<ServiceAmount>();//总的费用
	
	/*订单详情*/
	public Integer isDoorToDoor = 0;//0不是，1是门到门服务
	
	
	/*订单详情*/
	public String picture;
	public String model;
	public Integer carGroup;
	public Integer carTrunk;
	public Integer seats;
	public String takeCarCity;
	public String returnCarCity;
	public String takeCarAddress_Store;
	public String returnCarAddress_Store;
		
	/*订单活动*/	
	public ActivityShow activityShow;
	public List<ActivityShow> activityShowList;
	public Integer activityId = 0;//活动
	public boolean isHasActivity = false;
	
	/*订单提交参数*/
	public Integer averagePrice;
	public Integer basicInsuranceAmount;
	public Integer brandId;
	public Integer modelId;
	public Integer orderState;
	
	public Integer orderType;
	public Integer payAmount;//订单总金额
	public Integer payWay;//支付方式（0：门店现金 1：门店POS刷卡 2：在线网银 3：在线支付宝）
	public Integer poundageAmount;//手续费
	public Integer rentalAmount;//租车金额
	
	public Object  rentalId;
	public String returnCarAddress;
	public String returnCarCityId;
	public String returnCarDate;//用字符串代替Long
	public double returnCarLatitude;
	
	public double returnCarLongitude;
	public String returnCarStoreId;
	public Integer serviceType;//取车还车方式 0仅支持门店取还 1门店取上门还 2 上门取门店还 3上门取上门还
	public String takeCarAddress;
	public String takeCarCityId;

	public String takeCarDate;
	public double takeCarLatitude;
	public double takeCarLongitude;
	public String takeCarStoreId;
	public Integer tenancyDays;
	
	public Integer timeoutPrice;
	public Integer totalTasicInsuranceAmount;
	public Integer totalTimeoutPrice;
	public Integer userId;
	public Integer vendorId;
	
}

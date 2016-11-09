package com.gjcar.data.bean;

import java.util.ArrayList;
import java.util.List;


/*** 
* 
* Json To JavaBean
* @author www.json123.com
* 
*/
public class Order{
	
	public	Integer	id;	/*1*/	
	public	Integer	orderId;	/*1000001*/
	public	Integer	userId;	/*1*/
	public	Integer	brandId;	/*1*/
	public	String	takeCarDate;	/*2016-01-11 05:00:00*/
	public	String	returnCarDate;	/*2016-04-11 15:00:00*/
	public	String	takeCarCity;	/*武汉*/
	public	String	returnCarCity;	/*武汉*/
	public	String	takeCarAddress;	/*光谷软件园*/
	public	String	returnCarAddress;	/*光谷软件园*/
	public	Integer	payWay;	/*1*/
	public	Integer	rentalAmount;	/*88*/
	public	Integer	basicInsuranceAmount;	/*30*/
	public	Integer	poundageAmount;	/*15*/
	public	Integer	orderState;	/*1*/
	public	String	payAmount;	/*0*/
	public	Integer	vendorId;	/*1*/
	public	Integer	takeCarStoreId;	/*1*/
	public	Integer	returnCarStoreId;	/*1*/
	public	String	processId;	/*Object*/
	
	public	Integer	orderType;//业务类型(1:短租自驾 2:短租带驾 3:接送机)
	public  String  vehicleId;
	public  String  finishDate;
	public  String  takeCarFuel;
	public  String  returnCarFuel;
	public  String  takeCarMileage;
	public  String  returnCarMileage;
	
	public  String  takeCarActualDate;
	public  String  returnCarActualDate;
	public  String  totalAmount;
	public  String  vehicleShow;
	
	public  Integer  activityId;//优惠活动
	public  String  couponNumber;
	public  String  reduce;
	public  Activity_Order_Info couponShowForAdmin;
	public  ActivityShow activityShow;
	
	public	Integer	serviceType;	/*0*/
	public	Integer	modelId;	/*1*/	
	public	String	model;	/*宝马X6*/
	public	String	gear;	/*MT*/
	public	String	displacement;	/*2.5L*/
	public	String	fuelStr;	/*汽油*/
	public	String	carTrunkStr;	/*3厢*/	
	public	String	seatsStr;	/*5座*/	
	public	String	licenseTypeStr;	/*C1*/
	public	String	carGroupstr;	/*豪华型轿车*/
	public	String	picture;	/**/
	
	public  Integer tenancyDays;
	public  Integer timeoutPrice;
	public  Integer totalTimeoutPrice;
	public  Integer averagePrice;
	public  Integer totalTasicInsuranceAmount;
	
	public  Integer hasContract;
	
	public	User	userShow;	/*UserShow*/
	public	VehicleBrandShow	vehicleBrandShow;	/*TVehicleBrandShow*/
	
	public  StoreShows	takeCarStore;	/*TTakeCarStore*/
	public  StoreShows  returnCarStore;/*TReturnCarStore*/
	public	VendorShow	vendorShow;	/*TVendorShow*/
	
	public	ArrayList<ValueAddServiceShow>	valueAddServiceShowList = new ArrayList<ValueAddServiceShow>();
	
	public ArrayList<ServiceValueAddShow> orderValueAddedServiceRelativeShow = new ArrayList<ServiceValueAddShow>();

} 

package com.gjcar.data.bean;

import java.util.List;

public class FreeRide {

	public	Integer	id;	/*1*/	
	public	Long	number;	
	public	Integer	vehicleId;
	
	public	VehicleShow vehicleShow ;
	
	public	Integer	vendorId;
	public	Integer	takeCarCity;
	public	Integer	returnCarCity;
	
	public  CityShow takeCarCityShow;
	public  CityShow returnCarCityShow;
	
	public	Integer	takeCarStoreId;
	
	public	StoreShows takeCarStoreShow;
	
	public	String returnCarStoreIds;
	
	public	List<StoreShows> returnCarStoreShows;
	
	public	String takeCarDateStart;
	public	String takeCarDateEnd;
	
	public	Integer	price;
	public	Integer	maxMileage;
	public	Integer	outMaxMileagePrice;
	public	Integer	maxRentalDay;
	public	Integer	status;
	public	String createUser;
	public	String modifyUser;
	public	String publishDate;
	public	Integer	carBelongStoreId;
	
	public	StoreShows carBelongStoreShow;
	
}

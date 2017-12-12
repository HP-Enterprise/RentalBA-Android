package com.gjcar.data.bean;

public class VehicleModelShows {

	public	Integer	id;	/*编号:1*/	
	public  Integer brandId	;/*牌子:1*/	
	public	Integer	seriesId;	/*1*/	
	public	String	model;	/*车型：宝马X6*/	
	public	String	gear;	/*1*/
	public	String	displacement;	/*2.5L*/	
	public	Integer	fuel;	/*1*/	
	public	Integer	carTrunk;	/*1*/
	public	Integer	seats;	/*5*/
	public	Integer	licenseType;	/*6*/
	public	Integer	carGroup;	/*3*/
	public	String	picture;	/**/
	
	public	Integer plateType;/*1*/

	public VehicleBrandShow vehicleBrandShow = new VehicleBrandShow();
	
	public VehicleSeriesShow vehicleSeriesShow = new VehicleSeriesShow();
	
	public VehicleModelShows(){
		
	}
	
	public Integer getBrandId() {
		return brandId;
	}

	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}

	public Integer getPlateType() {
		return plateType;
	}

	public void setPlateType(Integer plateType) {
		this.plateType = plateType;
	}

	public VehicleBrandShow getVehicleBrandShow() {
		return vehicleBrandShow;
	}

	public void setVehicleBrandShow(VehicleBrandShow vehicleBrandShow) {
		this.vehicleBrandShow = vehicleBrandShow;
	}

	public VehicleSeriesShow getVehicleSeriesShow() {
		return vehicleSeriesShow;
	}

	public void setVehicleSeriesShow(VehicleSeriesShow vehicleSeriesShow) {
		this.vehicleSeriesShow = vehicleSeriesShow;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSeriesId() {
		return seriesId;
	}

	public void setSeriesId(Integer seriesId) {
		this.seriesId = seriesId;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getGear() {
		return gear;
	}

	public void setGear(String gear) {
		this.gear = gear;
	}

	public String getDisplacement() {
		return displacement;
	}

	public void setDisplacement(String displacement) {
		this.displacement = displacement;
	}

	public Integer getFuel() {
		return fuel;
	}

	public void setFuel(Integer fuel) {
		this.fuel = fuel;
	}

	public Integer getCarTrunk() {
		return carTrunk;
	}

	public void setCarTrunk(Integer carTrunk) {
		this.carTrunk = carTrunk;
	}

	public Integer getSeats() {
		return seats;
	}

	public void setSeats(Integer seats) {
		this.seats = seats;
	}

	public Integer getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(Integer licenseType) {
		this.licenseType = licenseType;
	}

	public Integer getCarGroup() {
		return carGroup;
	}

	public void setCarGroup(Integer carGroup) {
		this.carGroup = carGroup;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
	
}

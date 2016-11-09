package com.gjcar.data.bean;

import java.util.List;

public class AvgShow {
	
	public Integer storeId;//"id": 0,
	public Integer modelId;//"rentalPackId": 7,
	public Integer avgAmount;//"storeId": 3,
	public Integer prepayAmount;//"modelId": 6,
	public Integer basicInsuranceAmount;//"rentalAmount": 490,
	public Integer delayAmount;//"prepayAmount": 3000,
	public Integer totalAmount;//"basicInsuranceAmount": 50,
	public Integer totalDay;//"delayAmount": 30,
	
//	public String createDate;// "provinceNum": "ºþ±±",
//	public String modifyDate;// "provinceNum": "00003",
//	public String createUser;
//	public String modifyUser;
//	public String isEnable;//"isEnable": "1"
	
	public ActivityShow activityShow;//1:Âú¼õ  2:ÂúÕÛ  3:ÂúÔù 4:Ò»¿Ú¼Û   5:Ä¨Áã 6.ÔÂ×â
	public List<ActivityShow> activityShows;
	
	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getModelId() {
		return modelId;
	}

	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}

	public Integer getAvgAmount() {
		return avgAmount;
	}

	public void setAvgAmount(Integer avgAmount) {
		this.avgAmount = avgAmount;
	}

	public Integer getPrepayAmount() {
		return prepayAmount;
	}

	public void setPrepayAmount(Integer prepayAmount) {
		this.prepayAmount = prepayAmount;
	}

	public Integer getBasicInsuranceAmount() {
		return basicInsuranceAmount;
	}

	public void setBasicInsuranceAmount(Integer basicInsuranceAmount) {
		this.basicInsuranceAmount = basicInsuranceAmount;
	}

	public Integer getDelayAmount() {
		return delayAmount;
	}

	public void setDelayAmount(Integer delayAmount) {
		this.delayAmount = delayAmount;
	}

	public Integer getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Integer getTotalDay() {
		return totalDay;
	}

	public void setTotalDay(Integer totalDay) {
		this.totalDay = totalDay;
	}

	public AvgShow(){
		
	}


}

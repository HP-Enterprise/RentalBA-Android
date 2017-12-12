package com.gjcar.data.bean;

import java.io.Serializable;
import java.util.List;


@SuppressWarnings("serial")
public class CityShow implements Serializable{

	public	Integer	id;	/*����id*/
	public	String	cityNum;	/*���б��*/
	public	String	cityName;	/*��������*/	
	public	String	parentNum;	/*����ʡ�ݱ��*/
	public	Double	latitude;	/*γ��*/
	public	Double	longitude;	/*����*/
	
	public	String belong;
	public  Integer isHot;
	public	String label;
	public	String pinyin;
	public	List<StoreShows> storeShows;

	public Integer cityId;//����id:ֻ��Բ������ݿ�ʱʹ��
	
	public CityShow(){
		
	}
	
	public CityShow(Integer id, String cityName, Double latitude, Double longitude){
		this.id = id;
		this.cityName = cityName;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public CityShow(Integer id, String cityName){
		this.id = id;
		this.cityName = cityName;
	}
	
	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getCityNum() {
		return cityNum;
	}


	public void setCityNum(String cityNum) {
		this.cityNum = cityNum;
	}


	public String getCityName() {
		return cityName;
	}


	public void setCityName(String cityName) {
		this.cityName = cityName;
	}


	public String getParentNum() {
		return parentNum;
	}


	public void setParentNum(String parentNum) {
		this.parentNum = parentNum;
	}


	public Double getLatitude() {
		return latitude;
	}


	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}


	public Double getLongitude() {
		return longitude;
	}


	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}


	public String getBelong() {
		return belong;
	}


	public void setBelong(String belong) {
		this.belong = belong;
	}


	public Integer getIsHot() {
		return isHot;
	}


	public void setIsHot(Integer isHot) {
		this.isHot = isHot;
	}


	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}


	public String getPinyin() {
		return pinyin;
	}


	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public List<StoreShows> getStoreShows() {
		return storeShows;
	}

	public void setStoreShows(List<StoreShows> storeShows) {
		this.storeShows = storeShows;
	}
	
}

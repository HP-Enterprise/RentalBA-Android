package com.gjcar.data.bean;

public class StoreShows {

	public	Integer	id;	/**/	
	public	String	storeNum;	/*���:H430302*/
	public	String	storeName;	/*����:�人��ӻ�����*/
	public	String	storeAddr;	/*���ַ:����ʡ�人�л������������12�� */
	public	String	businessHoursStart;	/*������ʼʱ�䣺08:00*/
	public	String	businessHoursEnd;	/*��������ʱ��:20:00*/
	public	Integer	isLocation;	/*1*/
	public	String	northSouth;	/*Object:null*/
	public	String	eastWest;	/*:""*/
	public	Double	latitude;	/*γ�ȣ�30.785196*/
	public	Double	longitude;	/*���ȣ�114.229124*/
	public	Integer	available;	/*1*/	
	public	Integer	cityId;	/*3*/
	public	Integer	serialNumber;	/*1*/
	public	String	phone;	/*�绰*/
	public	String	maxGetCarTime;	/*Object:null*/
	public	String	minGetCarTime;	/*Object��null*/

	public Integer pid;
	public Integer tid;
	public String manager;
	public String managerPhone;
	
	public CityShow cityShow = new CityShow();
	
//	public ProvinceShow provinceShow = new ProvinceShow();
//	
//	public TradeAreaShow tradeAreaShow = new TradeAreaShow();
//	
//	public VendorShow vendorShow = new VendorShow();
	
	public Double distance;/*����*/
	
	public StoreShows(){
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStoreNum() {
		return storeNum;
	}

	public void setStoreNum(String storeNum) {
		this.storeNum = storeNum;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getStoreAddr() {
		return storeAddr;
	}

	public void setStoreAddr(String storeAddr) {
		this.storeAddr = storeAddr;
	}

	public String getBusinessHoursStart() {
		return businessHoursStart;
	}

	public void setBusinessHoursStart(String businessHoursStart) {
		this.businessHoursStart = businessHoursStart;
	}

	public String getBusinessHoursEnd() {
		return businessHoursEnd;
	}

	public void setBusinessHoursEnd(String businessHoursEnd) {
		this.businessHoursEnd = businessHoursEnd;
	}

	public Integer getIsLocation() {
		return isLocation;
	}

	public void setIsLocation(Integer isLocation) {
		this.isLocation = isLocation;
	}

	public String getNorthSouth() {
		return northSouth;
	}

	public void setNorthSouth(String northSouth) {
		this.northSouth = northSouth;
	}

	public String getEastWest() {
		return eastWest;
	}

	public void setEastWest(String eastWest) {
		this.eastWest = eastWest;
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

	public Integer getAvailable() {
		return available;
	}

	public void setAvailable(Integer available) {
		this.available = available;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(Integer serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMaxGetCarTime() {
		return maxGetCarTime;
	}

	public void setMaxGetCarTime(String maxGetCarTime) {
		this.maxGetCarTime = maxGetCarTime;
	}

	public String getMinGetCarTime() {
		return minGetCarTime;
	}

	public void setMinGetCarTime(String minGetCarTime) {
		this.minGetCarTime = minGetCarTime;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}
	
	
}

package com.gjcar.data.bean;

public class ActivityShow {
	
	public Integer id;
	
	public String name;
	public String startDate;
	public String endDate;
	public String isMeet;
	
	public Integer activityTypeId;
	
	public Integer totalAmount;
	public String userType;
	public Integer userGrade;
	public Integer releaseUser;
	
	//public  staffUserShow;
	
	public Long releaseDate;
	
	public Integer state;
	
	public String processId;
	public Long modifyDate;
	public String modifyUser;
	public Long createDate;
	public String createUser;
	public String isEnable;
	
	public Integer isSdew;//=1,默认选择不计免赔
	
	public String activityDescription;
	public ActivityTypeShow activityTypeShow;
}

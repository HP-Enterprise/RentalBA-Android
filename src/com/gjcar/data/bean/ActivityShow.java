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
	
	public Integer isSdew;//=1,Ĭ��ѡ�񲻼�����
	
	public String activityDescription;
	public ActivityTypeShow activityTypeShow;
	
	public String applySource;//Ӧ����Դ0������ 1��web 2:android 3:ios Ĭ��web zwc
	public String promptMessage;//��ʾ��Ϣ zwc
	
	public Integer payment;//֧����ʽ
}

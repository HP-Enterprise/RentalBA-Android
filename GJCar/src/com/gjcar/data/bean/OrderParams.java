package com.gjcar.data.bean;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;


public class OrderParams {
	
	/*���������*/
	public List<ServiceAmount> server_list = new ArrayList<ServiceAmount>();
	public List<ServiceAmount> all_list = new ArrayList<ServiceAmount>();//�ܵķ���
	
	/*��������*/
	public Integer isDoorToDoor = 0;//0���ǣ�1���ŵ��ŷ���
	
	
	/*��������*/
	public String picture;
	public String model;
	public Integer carGroup;
	public Integer carTrunk;
	public Integer seats;
	public String takeCarCity;
	public String returnCarCity;
	public String takeCarAddress_Store;
	public String returnCarAddress_Store;
		
	/*�����*/	
	public ActivityShow activityShow;
	public List<ActivityShow> activityShowList;
	public Integer activityId = 0;//�
	public boolean isHasActivity = false;
	
	/*�����ύ����*/
	public Integer averagePrice;
	public Integer basicInsuranceAmount;
	public Integer brandId;
	public Integer modelId;
	public Integer orderState;
	
	public Integer orderType;
	public Integer payAmount;//�����ܽ��
	public Integer payWay;//֧����ʽ��0���ŵ��ֽ� 1���ŵ�POSˢ�� 2���������� 3������֧������
	public Integer poundageAmount;//������
	public Integer rentalAmount;//�⳵���
	
	public Object  rentalId;
	public String returnCarAddress;
	public String returnCarCityId;
	public String returnCarDate;//���ַ�������Long
	public double returnCarLatitude;
	
	public double returnCarLongitude;
	public String returnCarStoreId;
	public Integer serviceType;//ȡ��������ʽ 0��֧���ŵ�ȡ�� 1�ŵ�ȡ���Ż� 2 ����ȡ�ŵ껹 3����ȡ���Ż�
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

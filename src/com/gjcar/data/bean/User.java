package com.gjcar.data.bean;

public class User {

	public int id;
	public String nickName;//昵称
	public String realName;//真实姓名
	public String gender;//性别
	public String birth;//出生日期
	public String country;//国籍
	public String address;//通信地址
	public String postCode;//邮政编码
	public String phone;//手机
	public String email;//手机
	public Integer emailStatus;//email的状态：0
	public String credentialType;//证件类型
	public String credentialNumber;//证件号码	
	public String contactPerson;//紧急联络人
	public String contactPhone;//紧急联络人电话	
	public String registerWay;//注册方式
	public String customerSource;//用户来源
	public String modifyDate;//最近更新时间
	public String modifyUser;//最近更新人
	public String createDate;//创建时间
	public String createUser;//创建人
	public String isEnable;//有效标志
	
	public Integer lvl;//等级
	
	public String token;//登录之后从token中获取token
	
	public User(){
		
	}
}

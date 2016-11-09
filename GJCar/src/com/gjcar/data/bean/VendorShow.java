package com.gjcar.data.bean;

/**租车公司信息
 * @author Administrator
 *
 */
public class VendorShow {

	public	Integer	id;	/*商户id*/
	public	String	vendorName;	/*商户名称*/
	public	String	address;	/*公司地址*/
	public	String	contact;	/*联系人*/
	public	String	contactPhone;	/*联系电话*/
	public	String	license;	/*企业执照注册号*/
	public	String	legalPerson;	/*法人代表*/
	public	String	legalPhone;	/*法人联系方式*/
	public	Integer	available;	/*账号状态，0-不可用，1-可用*/
	public	String	vendorNum;	/*商户编号*/
	
	public	String	cityShows;	/*Object*/
	public	Integer	onlinePay;	/*1*/
	public	Integer	vechileScale;	/*200*/
	public String description;
	public String organizationCode;
	public String taxRegist;
	public String remark;
	public String taxDesc;
	public String taxCategory;
	public String rentalCategory;
	public String licenseImage;
	public String organizationCodeImage;
	public String taxRegistImage;
	
	
	/**Order:下订单返回值*/
	public	String	logo;/*租车公司图片路径*/
	
	public VendorShow(){
		
	}
}

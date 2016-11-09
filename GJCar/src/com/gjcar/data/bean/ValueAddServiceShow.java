package com.gjcar.data.bean;

/**增值服务
 * @author Administrator
 *
 */
public class ValueAddServiceShow {

	public	Integer	id;	/*1*/
	public	String	serviceName;	/*增值服务名称：不计免赔*/
	public	Integer	price;	/*25*/
	public	String	description;	/*说明：不计免赔服务，元/天*/
	public	Integer	amount;	/*数量：2*/

	public Integer unit;
	public Integer vendorId;
	public Integer serializeNumber;
	public Integer isEnable;

	public ValueAddServiceShow(){
		
	}
}

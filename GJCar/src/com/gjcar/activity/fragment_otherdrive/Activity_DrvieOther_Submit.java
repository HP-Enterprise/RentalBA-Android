package com.gjcar.activity.fragment_otherdrive;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gjcar.activity.fragment1.Activity_Area;
import com.gjcar.activity.fragment1.Activity_Order_Submit;
import com.gjcar.activity.fragment1.WebActivity;
import com.gjcar.activity.user.ValidationHelper;
import com.gjcar.activity.user.more.Activity_Order_Ok;
import com.gjcar.annotation.ContentView;
import com.gjcar.annotation.ContentWidget;
import com.gjcar.app.R;
import com.gjcar.data.bean.OrderPrice;
import com.gjcar.data.data.Public_Api;
import com.gjcar.data.data.Public_Param;
import com.gjcar.utils.AnnotationViewUtils;
import com.gjcar.utils.HandlerHelper;
import com.gjcar.utils.HttpHelper;
import com.gjcar.utils.ImageLoaderHelper;
import com.gjcar.utils.IntentHelper;
import com.gjcar.utils.ListenerHelper;
import com.gjcar.utils.NetworkHelper;
import com.gjcar.utils.SharedPreferenceHelper;
import com.gjcar.utils.StringHelper;
import com.gjcar.utils.TimeHelper;
import com.gjcar.utils.ToastHelper;
import com.gjcar.utils.ValidateHelper;
import com.gjcar.view.dialog.SubmitDialog;
import com.gjcar.view.helper.ListViewHelper;
import com.gjcar.view.helper.LoadAnimateHelper;
import com.gjcar.view.helper.TitleBarHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

@ContentView(R.layout.activity_order_otherdrive)
public class Activity_DrvieOther_Submit extends Activity{
	
	/*�ؼ�*/
	@ContentWidget(id = R.id.m_where) TextView m_where;
	@ContentWidget(id = R.id.m_city) TextView m_city;
	@ContentWidget(id = R.id.m_outcity) TextView m_outcity;	
	@ContentWidget(id = R.id.m_time) TextView m_time;
	@ContentWidget(id = R.id.m_days) TextView m_days;
	@ContentWidget(id = R.id.m_up_address) TextView m_up_address;
	@ContentWidget(id = R.id.m_down_address) TextView m_down_address;
	
	@ContentWidget(id = R.id.u_name) EditText u_name;
	@ContentWidget(id = R.id.u_phone) EditText u_phone;
	@ContentWidget(id = R.id.u_note) EditText u_note;
	
	@ContentWidget(id = R.id.f_usecar) TextView f_usecar;
	@ContentWidget(id = R.id.f_usecar_all) TextView f_usecar_all;
	@ContentWidget(id = R.id.f_out) TextView f_out;
	@ContentWidget(id = R.id.f_out_all) TextView f_out_all;
	@ContentWidget(id = R.id.f_drive) TextView f_drive;
	@ContentWidget(id = R.id.f_drive_all) TextView f_drive_all;
	@ContentWidget(id = R.id.f_all) TextView f_all;
	
	@ContentWidget(click = "onClick") TextView read_prevision;
	@ContentWidget(click = "onClick") Button ok;
	
	/*Handler*/
	private Handler handler;
	private final static int Request_Data = 1;
	private boolean isRequestPriceOk = false;	
	private OrderPrice orderPrice = null;
	
	private final static int OK = 5;
	private final static int Fail = 6;
	private final static int DataFail = 7;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		AnnotationViewUtils.injectObject(this, this);
		
		/*Handler*/
		initHandler();
		
		/*����*/
		TitleBarHelper.Back(this, "ȷ�϶���", 0);
		
		/*��ȡ����*/
		initView();
		
		/*���ط���*/
		//Request_AmountDetail();
		
	}

	public void onClick(View view){

		switch (view.getId()) {
		
			case R.id.read_prevision:
				IntentHelper.startActivity(Activity_DrvieOther_Submit.this, WebActivity.class);
				break;
			
			case R.id.ok:
				
				/*�ж��Ƿ�����*/
				if(!NetworkHelper.isNetworkAvailable(this)){
					return;
				}

				/*�жϼ۸�*/
				if(!isRequestPriceOk){//�۸�û�м�ֵ����
					ToastHelper.showNoNetworkToast(this);
					return;
				}
				
				/*�ж������Ƿ���ȷ*/
				if(!ValidateHelper.Validate(Activity_DrvieOther_Submit.this, new boolean[]{
						u_name.getText().toString()==null||u_name.getText().toString().equals(""),ValidationHelper.IsChineseName(u_name.getText().toString().trim()),
						u_phone.getText().toString()==null||u_phone.getText().toString().equals(""),!u_name.getText().toString().trim().matches("^[1][358][0-9]{9}$")},
						new String[]{"��ѡ���ó�����","���չ�˾������д","����������д","����ű�����д"})){
					return;
				}
				System.out.println("��֤���");
				
				/*�����ύ�Ի���*/	
				SubmitDialog.showSubmitDialog(this);	
				
				Request_Submit();
				
				//IntentHelper.startActivity(Activity_Order_Submit.this, Activity_Order_Ok.class);
				break;

			default:
				break;
		}
		
	}
	
	private void initView(){

//		m_where.setText("����");
//		m_city.setText(Public_Param.other_params.take_city);
//		m_outcity.setText(Public_Param.other_params.return_city);
//		m_time.setText(Public_Param.other_params.take_time);
//		m_days.setText(Public_Param.other_params.days);
//		m_up_address.setText(Public_Param.other_params.up_address);
//		m_down_address.setText(Public_Param.other_params.down_address);
		m_where.setText("����");
		m_city.setText("�人");
		m_outcity.setText("����");
		m_time.setText("2014-05-06 12:09");
		m_days.setText("����");
		m_up_address.setText("�人�й�������C6405");
		m_down_address.setText("�����а˴��볤��43����Žֵ�");
		
	}
	
	private void initView_Price() {
				
		Integer str_usecar = 500;//��������
		Integer str_count = 3;		
		Integer str_out = 600; //���������
		Integer str_out_all = 600; 
		Integer str_drive = 300; //��ʻԱ����ʳ�޷�
		Integer str_day = 2; 		
		String str_all = "2700";//�ܷ���
		
		f_usecar.setText(str_usecar.toString()+"Ԫ/�Σ�����"+str_count.toString()+"��");//��������
		f_usecar_all.setText("��"+str_usecar.intValue()*str_count.intValue()+"Ԫ");
		f_out.setText(str_out.toString()+"Ԫ");//���������
		f_out_all.setText("��"+str_out_all.toString()+"Ԫ");
		f_drive.setText(str_drive.toString()+"Ԫ/�죬��"+str_day.toString()+"��");//��ʻԱ����ʳ�޷�
		f_drive_all.setText("��"+str_drive.intValue()*str_day.intValue()+"Ԫ");
		f_all.setText("��"+str_all+"Ԫ");//�ܷ���
		
	}
	
	private void Request_AmountDetail() {
		
		String userId = new Integer(SharedPreferenceHelper.getUid(this)).toString();System.out.println("userId"+userId);
		
		String activityId = Public_Param.order_paramas.activityId.toString();
		String isDoorToDoor = Public_Param.order_paramas.isDoorToDoor.toString();
		
		String startDate = TimeHelper.getSearchTime_Mis(Public_Param.order_paramas.takeCarDate);
		String endDate = TimeHelper.getSearchTime_Mis(Public_Param.order_paramas.returnCarDate);
		String takeCarStoreId = Public_Param.order_paramas.takeCarStoreId;
		String modelId = Public_Param.order_paramas.modelId.toString();
		
		String latitude = new Double(Public_Param.order_paramas.takeCarLatitude).toString();
		String longitude = new Double(Public_Param.order_paramas.takeCarLongitude).toString();
		
		String api = "api/searchAmountDetail?userId="+userId+"&activityId="+activityId+"&isDoorToDoor="+isDoorToDoor+"&endDate="+endDate+"&modelId="+modelId+"&startDate="+startDate+"&storeId="+takeCarStoreId;
		
		if(Public_Param.order_paramas.isDoorToDoor.intValue() == 1){System.out.println("�ŵ���");
			api = api+"&latitude="+latitude+"&longitude="+longitude;
			System.out.println(""+api);
		}
		new HttpHelper().initData(HttpHelper.Method_Get, this, api, null, null, handler, Request_Data, 1, new TypeReference<OrderPrice>() {});		
	}
	
	private void initHandler() {

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				switch (msg.what) {
					case OK:
//						SubmitDialog.closeSubmitDialog();//�رյ���
//						
//						finish();//����֮ǰ��ҳ��
//						for (int i = 0; i < Public_Param.list_order_activity.size(); i++) {
//							Public_Param.list_order_activity.get(i).finish();
//						}							
//						Public_Param.list_order_activity.clear();
//											
//						Intent intent = new Intent(Activity_Order_Submit.this, Activity_Order_Ok.class);//��ת
//						
//						intent.putExtra("model", Public_Param.order_paramas.model);
//						intent.putExtra("days", orderPrice.daySum.toString());
//						intent.putExtra("orderId", orderId);						
//						intent.putExtra("acount", ""+(orderPrice.totalPrice.intValue()+serviceAllAmount));
//						intent.putExtra("payWay", Public_Param.order_paramas.payWay.toString());
//						
//						if(Public_Param.order_paramas.isDoorToDoor.intValue() == 1){
//							intent.putExtra("way", "doortodoor");
//						}else{
//							intent.putExtra("way", "order");
//						}
//						
//						startActivity(intent);
						break;
						
					case Fail:
//						test.setText("��ʾʧ��\n"+data);
//						SubmitDialog.closeSubmitDialog();
//						ToastHelper.showToastShort(Activity_Order_Submit.this, msg.getData().getString("errorMsg"));
						break;
						
					case DataFail:
//						test.setText("��ʾʧ��\n"+data);
//						SubmitDialog.closeSubmitDialog();
//						ToastHelper.showToastShort(Activity_Order_Submit.this, "�����ύʧ��,�����·���");
						break;
						
					case Request_Data:
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
							
							isRequestPriceOk = true;
							orderPrice = (OrderPrice)msg.obj;
							initView_Price();
							
				           	return;
						}
						//Request_AmountDetail();
						
						break;
						
					default:
						break;
				}
			}
		};
	}
	
	/** �ύ��Ϣ  */
	private void  Request_Submit(){

		/*����*/
		JSONObject jsonObject = new JSONObject(); //**********************ע��json��������ʱ��Ҫ����
//		carNumber:"999"
//			customerName:"����"
//			email:"10001@qq.com"
//			phone:"13888888888"
//			rentalTime:"90��-120��"
//			requirement:"����Ͱ�"
//			useCarCityId:"3"
//			useCarDate:"2016-07-07"
//			vehicleBrandId:"6"
//			vehicleModelId:"3"
				//"take_city","take_time","take_days","count","brand","car","cityId","brandId","carId"		
		String aaa = "";	
		jsonObject.put("aaaaaa", aaa);System.out.println("aaaaaa"+aaa);
		jsonObject.put("aaaaaa", aaa);System.out.println("aaaaaa"+aaa);
		jsonObject.put("aaaaaa", aaa);System.out.println("aaaaaa"+aaa);
		jsonObject.put("aaaaaa", aaa);System.out.println("aaaaaa"+aaa);
		jsonObject.put("aaaaaa", aaa);System.out.println("aaaaaa"+aaa);
		
		jsonObject.put("aaaaaa", aaa);System.out.println("aaaaaa"+aaa);
		jsonObject.put("aaaaaa", aaa);System.out.println("aaaaaa"+aaa);
		jsonObject.put("aaaaaa", aaa);System.out.println("aaaaaa"+aaa);
		jsonObject.put("aaaaaa", aaa);System.out.println("aaaaaa"+aaa);
		jsonObject.put("aaaaaa", aaa);System.out.println("aaaaaa"+aaa);
		
		jsonObject.put("aaaaaa", aaa);System.out.println("aaaaaa"+aaa);
		jsonObject.put("aaaaaa", aaa);System.out.println("aaaaaa"+aaa);
		jsonObject.put("aaaaaa", aaa);System.out.println("aaaaaa"+aaa);
		jsonObject.put("aaaaaa", aaa);System.out.println("aaaaaa"+aaa);
		jsonObject.put("aaaaaa", aaa);System.out.println("aaaaaa"+aaa);
		
		jsonObject.put("aaaaaa", aaa);System.out.println("aaaaaa"+aaa);
		jsonObject.put("aaaaaa", aaa);System.out.println("aaaaaa"+aaa);
		jsonObject.put("aaaaaa", aaa);System.out.println("aaaaaa"+aaa);
		jsonObject.put("aaaaaa", aaa);System.out.println("aaaaaa"+aaa);
		jsonObject.put("aaaaaa", aaa);System.out.println("aaaaaa"+aaa);
		
		/*�ύ*/
		//new HttpHelper().initData(HttpHelper.Method_Post, this, "api/longRentalAsk", jsonObject, null, handler, Request_Submit, 2, null);
		
	}	
}

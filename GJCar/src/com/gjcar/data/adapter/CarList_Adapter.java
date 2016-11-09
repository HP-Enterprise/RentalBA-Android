package com.gjcar.data.adapter;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.TypeReference;
import com.gjcar.activity.fragment1.Activity_Order_Submit;
import com.gjcar.activity.fragment1.Activity_Service;
import com.gjcar.activity.main.ActivityTest;
import com.gjcar.activity.user.Login_Activity;
import com.gjcar.app.R;
import com.gjcar.data.bean.ActivityShow;
import com.gjcar.data.bean.DayRental;
import com.gjcar.data.bean.Model____Vendor_Store_Price;
import com.gjcar.data.bean.Order;
import com.gjcar.data.bean.ServiceAmount;
import com.gjcar.data.bean.TicketInfo;
import com.gjcar.data.data.Public_Api;
import com.gjcar.data.data.Public_Param;
import com.gjcar.data.data.Public_SP;
import com.gjcar.data.service.RentalDayHelper;
import com.gjcar.data.service.RightHelper;
import com.gjcar.utils.HandlerHelper;
import com.gjcar.utils.HttpHelper;
import com.gjcar.utils.ImageLoaderHelper;
import com.gjcar.utils.IntentHelper;
import com.gjcar.utils.NetworkHelper;
import com.gjcar.utils.SharedPreferenceHelper;
import com.gjcar.utils.StringHelper;
import com.gjcar.utils.TimeHelper;
import com.gjcar.view.helper.LoadAnimateHelper;
import com.gjcar.view.widget.MyGridView;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class CarList_Adapter extends BaseAdapter {

	private Context context;
	private List<Model____Vendor_Store_Price> list;
	private ImageLoader imageLoader;
	
	/*Handler*/
	private Handler handler;
	private final static int Request_token = 1001;
	
	/*List*/
	private MyGridView[] myGridList;
	private boolean[] openList;
	private boolean[] isLoadingList;//�Ƿ����ڼ����У������У��Ͳ��ܵ��������һ���µ�
	private int[] LoadingMonthList;//���ڼ����ĸ��µģ�Ĭ���ǵ�ǰ�£���Χ�ǵ�ǰ�£�����2���£�����0��1��2
	
	/*ѡ���˵ڼ����*/
//	private int activit_position = -1;
	private int[] activity_position;
	
	public CarList_Adapter(Context context, List<Model____Vendor_Store_Price> list) {

		this.list = list;
		this.context = context;
		myGridList = new MyGridView[list.size()];
		openList = new boolean[list.size()];
		isLoadingList = new boolean[list.size()];
		LoadingMonthList = new int[list.size()];
		
		/*handler*/
		initHandler();
				
		ImageLoaderHelper.initImageLoader(context);
		imageLoader = ImageLoader.getInstance();
		
		activity_position = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			activity_position[i]=-1;
		}
	}

	@Override
	public int getCount() {
	
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final Holder holder;
		if (convertView == null) {
			
			holder = new Holder();
			convertView = View.inflate(context, R.layout.carlist_listview_item, null);
			System.out.println("3******************");
			holder.picture = (ImageView) convertView.findViewById(R.id.picture);
			
			holder.model = (TextView) convertView.findViewById(R.id.model);
			holder.box = (TextView) convertView.findViewById(R.id.box);
			
			holder.activity_show = (LinearLayout) convertView.findViewById(R.id.activity_show);//�
			holder.activity_flag = (TextView) convertView.findViewById(R.id.activity_flag);	
			holder.activity_title = (TextView) convertView.findViewById(R.id.activity_title);	
			holder.activity_arrow = (ImageView) convertView.findViewById(R.id.activity_arrow);
			
			holder.go = (LinearLayout) convertView.findViewById(R.id.go);	
			holder.price = (TextView) convertView.findViewById(R.id.price);	
			
			holder.days_price = (LinearLayout) convertView.findViewById(R.id.days_price);
			
			holder.rental_lin = (LinearLayout) convertView.findViewById(R.id.rental_lin);
			holder.lin_data = (LinearLayout) convertView.findViewById(R.id.lin_data);			
			holder.gridview  = (MyGridView) convertView.findViewById(R.id.gridview);
			holder.left = (LinearLayout) convertView.findViewById(R.id.left);
			holder.right = (LinearLayout) convertView.findViewById(R.id.right);
			holder.rental_time = (TextView) convertView.findViewById(R.id.rental_time);
			System.out.println("4******************");
			convertView.setTag(holder);
			System.out.println("5******************");
		} else {
			holder = (Holder) convertView.getTag();
		}
		
		/*��ʼ������*/
		imageLoader.displayImage(Public_Api.appWebSite + list.get(position).vehicleModelShow.picture, holder.picture, ImageLoaderHelper.initDisplayImageOptions());
				
		holder.model.setText(list.get(position).vehicleModelShow.model);	
		
		String carGroup = "";
		if(list.get(position).vehicleModelShow.carGroup != null){
			carGroup = StringHelper.getCarGroup(list.get(position).vehicleModelShow.carGroup);
		}
		
		String carTrunk = "";
		if(list.get(position).vehicleModelShow.carTrunk != null){
			carTrunk = StringHelper.getCarTrunk(list.get(position).vehicleModelShow.carTrunk);
		}

		if(carTrunk.equals("1")){
			carTrunk = "3";
		}
		String seats = list.get(position).vehicleModelShow.seats.toString();
		holder.box.setText(carGroup+"/"+carTrunk+"��/"+seats+"��");
		
		holder.price.setText(list.get(position).vendorStorePriceShowList.get(0).avgShow.avgAmount.toString());
		System.out.println(position+"######################");
		
		/*�����Ż�*/
		holder.activity_show.setVisibility(View.GONE);//����֮ǰ������
		if(list.get(position).vendorStorePriceShowList.get(0).avgShow.activityShow != null){
			
			if(activity_position[position] == -1){
				activity_position[position] = 0;
			}
			
			holder.activity_show.setVisibility(View.VISIBLE);
			holder.activity_flag.setText(list.get(position).vendorStorePriceShowList.get(0).avgShow.activityShows.get(activity_position[position]).activityTypeShow.hostTypeDascribe);
			holder.activity_title.setText(list.get(position).vendorStorePriceShowList.get(0).avgShow.activityShows.get(activity_position[position]).name);
			
			List<ActivityShow> mylist = list.get(position).vendorStorePriceShowList.get(0).avgShow.activityShows;
			
			if(mylist != null && mylist.size()>1){
				holder.activity_arrow.setVisibility(View.VISIBLE);
			}
			
		}
		
		/*���*/
		myGridList[position] = holder.gridview;
		openList[position] = false;
		isLoadingList[position] = false;
		LoadingMonthList[position] = 0;
			
		holder.right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if(LoadingMonthList[position] > 1){//���ü���
					return;
				}
				
				if(isLoadingList[position]){//���ڼ���ǰһ���Ͳ�����ִ��
					return;
				}
				
				LoadingMonthList[position] = LoadingMonthList[position]+1;
				
				holder.rental_time.setText(RentalDayHelper.getRental_TimeTitle(LoadingMonthList[position]));
				holder.gridview.setAdapter(null);
				//��������
				LoadAnimateHelper.start_animation();
				
				isLoadingList[position] = true;
				String api = "api/rentalPack/prices?endDate="+RentalDayHelper.getRental_Time(LoadingMonthList[position]+1)+"&modelId="+list.get(position).vendorStorePriceShowList.get(0).avgShow.modelId.toString()+"&startDate="+RentalDayHelper.getRental_Time(LoadingMonthList[position])+"&storeId="+list.get(position).vendorStorePriceShowList.get(0).avgShow.storeId.toString();					
				new HttpHelper().initData(HttpHelper.Method_Get, context, api, null, null, handler, position, 1, new TypeReference<ArrayList<DayRental>>() {});		
				
			}
			
		});
		
		holder.left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				if(LoadingMonthList[position] < 1){//���ü���
					return;
				}
				
				if(isLoadingList[position]){//���ڼ���ǰһ���Ͳ�����ִ��
					return;
				}
				
				LoadingMonthList[position] = LoadingMonthList[position]-1;
				
				holder.rental_time.setText(RentalDayHelper.getRental_TimeTitle(LoadingMonthList[position]));
				holder.gridview.setAdapter(null);
				
				//��������
				LoadAnimateHelper.start_animation();
				
				isLoadingList[position] = true;
				String api = "api/rentalPack/prices?endDate="+RentalDayHelper.getRental_Time(LoadingMonthList[position]+1)+"&modelId="+list.get(position).vendorStorePriceShowList.get(0).avgShow.modelId.toString()+"&startDate="+RentalDayHelper.getRental_Time(LoadingMonthList[position])+"&storeId="+list.get(position).vendorStorePriceShowList.get(0).avgShow.storeId.toString();					
				new HttpHelper().initData(HttpHelper.Method_Get, context, api, null, null, handler, position, 1, new TypeReference<ArrayList<DayRental>>() {});		
				
			}
			
		});
		
		holder.days_price.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				if(holder.rental_lin.getVisibility() == View.VISIBLE){
					
					LoadingMonthList[position] = 0;
					openList[position] = false;
					holder.rental_lin.setVisibility(View.GONE);
					holder.gridview.setAdapter(null);
					LoadAnimateHelper.load_success_animation();
				}else{
					
					holder.rental_time.setText(RentalDayHelper.getRental_TimeTitle(0));
					
					openList[position] = true;
					holder.rental_lin.setVisibility(View.VISIBLE);
					LoadAnimateHelper.Search_Animate_Dialog(context, holder.lin_data, null, 10, false,true,0);
					
					isLoadingList[position] = true;
					String api = "api/rentalPack/prices?endDate="+RentalDayHelper.getRental_Time(1)+"&modelId="+list.get(position).vendorStorePriceShowList.get(0).avgShow.modelId.toString()+"&startDate="+RentalDayHelper.getRental_Time(0)+"&storeId="+list.get(position).vendorStorePriceShowList.get(0).avgShow.storeId.toString();					
					new HttpHelper().initData(HttpHelper.Method_Get, context, api, null, null, handler, position, 1, new TypeReference<ArrayList<DayRental>>() {});		
					
				}
								
			}
		});
		
		/*��ĵ���¼�*/
		holder.activity_show.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				List<ActivityShow> mylist = list.get(position).vendorStorePriceShowList.get(0).avgShow.activityShows;
				
				if(mylist != null && mylist.size()>1){
					initActivityDialog(mylist,holder.activity_title,position);
				}
				
			}
		});
		
		holder.go.setOnClickListener(new MyOnClickListener(position));
		holder.picture.setOnClickListener(new MyOnClickListener(position));
		
		return convertView;
	}

	public static class Holder {
		
		private ImageView picture;
		
		private TextView model;
		private TextView box;
		
		private LinearLayout activity_show;//�
		private TextView activity_flag;	
		private TextView activity_title;
		private ImageView activity_arrow;
		
		private LinearLayout go;		
		private TextView price;	
		private LinearLayout days_price;
		
		private LinearLayout rental_lin;		
		private LinearLayout lin_data;
		private MyGridView gridview;
		private LinearLayout left;
		private LinearLayout right;
		private TextView rental_time;
		
	}

	private void initHandler() {

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxx"+msg.what);
				if(msg.what == Request_token){
					if(msg.getData().getString("message").equals(HandlerHelper.Ok)){
						
						IntentHelper.startActivity(context, Activity_Service.class);
					}else{
						SharedPreferences sp = context.getSharedPreferences(Public_SP.Account, Context.MODE_PRIVATE);
						Editor editor = sp.edit();
						editor.clear();
						editor.commit();
						
						Intent intent = new Intent(context, Login_Activity.class);														
						((Activity)context).startActivityForResult(intent, 316);
					}
					
				}else{
					
					isLoadingList[msg.what] = false;
					
					/*����û��ر�������*/
					if(!openList[msg.what]){
						return;
					}
					
					/*������չ����*/		
					if(msg.getData().getString("message").equals(HandlerHelper.Ok)){
						LoadAnimateHelper.load_success_animation();
						ArrayList<DayRental> rentallist = (ArrayList<DayRental>)msg.obj;
						
						if(rentallist != null && rentallist.size() != 0){
							
							RentalDay_Adapter adapter = new RentalDay_Adapter(context, new RentalDayHelper().getList(rentallist,RentalDayHelper.getRental_Time(LoadingMonthList[msg.what])));
							myGridList[msg.what].setAdapter(adapter);
						}
					}else{
						LoadAnimateHelper.load_fail_animation();
					}
				}
								
			}
		};
	}

	public class MyOnClickListener implements OnClickListener{

		private int position;
		
		public MyOnClickListener(int position){
			this.position = position;
		}
		
		@Override
		public void onClick(View view) {
						
			/*����չʾ*/
			Public_Param.order_paramas.picture = list.get(position).vehicleModelShow.picture;
			Public_Param.order_paramas.model = list.get(position).vehicleModelShow.model;
			Public_Param.order_paramas.carTrunk = list.get(position).vehicleModelShow.carTrunk;
			Public_Param.order_paramas.seats = list.get(position).vehicleModelShow.seats;
			Public_Param.order_paramas.carGroup = list.get(position).vehicleModelShow.carGroup;
			
			/*�����*/
			if(list.get(position).vendorStorePriceShowList.get(0).avgShow.activityShow != null){
				
				/*ע��*/
				//Public_Param.order_paramas.activityId = list.get(position).vendorStorePriceShowList.get(0).avgShow.activityShow.id;	
				Public_Param.order_paramas.activityId = list.get(position).vendorStorePriceShowList.get(0).avgShow.activityShows.get(activity_position[position]).id;
				Public_Param.order_paramas.isHasActivity = true;
				
				//Public_Param.order_paramas.activityShow = list.get(position).vendorStorePriceShowList.get(0).avgShow.activityShow;
				System.out.println("activit_position*************************************"+activity_position[position]);
				Public_Param.order_paramas.activityShow = list.get(position).vendorStorePriceShowList.get(0).avgShow.activityShows.get(activity_position[position]);
				Public_Param.order_paramas.activityShowList = list.get(position).vendorStorePriceShowList.get(0).avgShow.activityShows;
			}else{
				
				Public_Param.order_paramas.activityId = 0;	
				Public_Param.order_paramas.isHasActivity = false;
			}
				
			/*�����ύ*/
			Public_Param.order_paramas.vendorId = list.get(position).vendorStorePriceShowList.get(0).vendorShow.id;				
			Public_Param.order_paramas.modelId = list.get(position).vendorStorePriceShowList.get(0).avgShow.modelId;
			Public_Param.order_paramas.brandId = list.get(position).vehicleModelShow.brandId;
			Public_Param.order_paramas.takeCarStoreId = list.get(position).vendorStorePriceShowList.get(0).avgShow.storeId.toString();
 	
			/*�ж��Ƿ�����*/
			if(!NetworkHelper.isNetworkAvailable(context)){
				return;
			}
			
			/*�ж��Ƿ��¼*/
			if(!SharedPreferenceHelper.isLogin(context)){
					
				Intent intent = new Intent(context, Login_Activity.class);														
				((Activity)context).startActivityForResult(intent, 316);
				return;
			}
					
			new RightHelper().IsTokenPass(context, handler, Request_token);
					
		}		
	}
		
	/**
	 * ������Ի���
	 */
	private void initActivityDialog(List<ActivityShow> mylist,final TextView textView, final int position) {
		
		final Dialog updateDialog = new Dialog(context, R.style.scorechange_dialog);
		
		View view = View.inflate(context, R.layout.dialog_carlist_look_activity, null);
		
		/*��ʼ���ؼ�*/
		final ListView mylistview = (ListView)view.findViewById(R.id.listview);
		LinearLayout lin = (LinearLayout)view.findViewById(R.id.activity);
		TextView cancle = (TextView)view.findViewById(R.id.cancle);
		TextView ok = (TextView)view.findViewById(R.id.ok);
			
		/*�����¼�*/		
		class MyOnClickListener implements OnClickListener{
			
			@Override
			public void onClick(View view) {
				
				switch (view.getId()) {
					
					case R.id.ok:
						updateDialog.dismiss();
						
						if(activity_position[position] != -1){
							textView.setText(list.get(position).vendorStorePriceShowList.get(0).avgShow.activityShows.get(activity_position[position]).name);
							updateDialog.dismiss();
						}else{
							updateDialog.dismiss();
						}											
						break;
				
					case R.id.cancle:
						updateDialog.dismiss();
						break;
						
					default:
						break;
				}
			}
			
		}
		MyOnClickListener onClickListener = new MyOnClickListener();
		
		ok.setOnClickListener(onClickListener);
		cancle.setOnClickListener(onClickListener);
		
		/*�Ի���:�������*/		
		WindowManager windowManager = ((Activity) context).getWindowManager();  
		Display display = windowManager.getDefaultDisplay();  
		WindowManager.LayoutParams lp = updateDialog.getWindow().getAttributes();  
		
		final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources()
	                .getDisplayMetrics());//�Աߵ�margin
		
		lp.width = (int)(display.getWidth()-pageMargin); //���ÿ��  
		updateDialog.getWindow().setAttributes(lp); 
		
		/*�Ի���:��������*/	
		updateDialog.getWindow().setContentView(view);
		updateDialog.setCancelable(false);
		updateDialog.setCanceledOnTouchOutside(false);
		
		/*�Ի���:��ʾ*/	
		updateDialog.show();
		
		/*��ʾ����*/
		mylistview.setVisibility(View.VISIBLE);
		CarList_Activity_Adapter adapter = new CarList_Activity_Adapter(context, mylist);
		mylistview.setAdapter(adapter);
		
		/*����¼�*/
		mylistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int myposition,
					long arg3) {
				activity_position[position] = myposition;
				//view.setBackgroundResource(context.getResources().getColor(R.color.page_text_select));
			}
		});
	}
	
	
}

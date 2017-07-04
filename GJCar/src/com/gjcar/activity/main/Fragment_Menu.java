package com.gjcar.activity.main;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.TypeReference;
import com.gjcar.activity.user.Login_Activity;
import com.gjcar.activity.user.User_Activity;
import com.gjcar.activity.user.more.Activity_Level;
import com.gjcar.activity.user.more.Activity_Order_List;
import com.gjcar.activity.user.more.Activity_Score;
import com.gjcar.activity.user.more.Activity_Ticket;
import com.gjcar.app.R;
import com.gjcar.data.bean.Order;
import com.gjcar.data.data.Public_BaiduTJ;
import com.gjcar.data.data.Public_Data;
import com.gjcar.data.data.Public_Param;
import com.gjcar.data.data.Public_SP;
import com.gjcar.data.service.CarList_Helper;
import com.gjcar.data.service.RightHelper;
import com.gjcar.utils.HandlerHelper;
import com.gjcar.utils.HttpHelper;
import com.gjcar.utils.IntentHelper;
import com.gjcar.utils.NetworkHelper;
import com.gjcar.utils.SharedPreferenceHelper;
import com.gjcar.utils.SystemUtils;
import com.gjcar.view.widget.CustomDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Fragment_Menu extends Fragment implements OnClickListener{

	/*头像*/	
	private ImageView main_menu_userinfo_logo;
	private LinearLayout main_menu_userinfo_lin;
	
	private TextView main_menu_userinfo_nickname;
	private TextView main_menu_userinfo_phone;
	
	/*中间栏*/
	private LinearLayout main_menu_order;//我的订单
	private LinearLayout order_all;//所有订单
	
	private LinearLayout order_drive_lin;
	private TextView order_drive;
	
	private LinearLayout order_other_lin;
	private TextView order_other;
	
	private LinearLayout order_wind_lin;
	private TextView order_wind;
	
	private LinearLayout gift_lin;
	private LinearLayout score_lin;
	private LinearLayout rink_lin;
	private LinearLayout system_lin;
	private LinearLayout phone_lin;
	
	private TextView version_code;
	
	/*Handler*/
	private Handler handler;
	private final static int Request_Order_Count = 1;
	private final static int Request_doortodoor_Count = 2;
	private final static int Request_wind_Count = 3;
	private final static int Request_token = 4;
	
	private boolean isTokenOverTime = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		/* 初始化 */
		View view  = inflater.inflate(R.layout.fragment_menu, null);

		initView(view);
				
		/*handler*/
		initHandler();
		
		/* 设置监听器 */		
		initListener();
		
		/*加载订单数量*/
		Request_Order_Count();
		
		/*判断token是否过期*/
		new RightHelper().IsTokenPass(getActivity(), handler, Request_token);
		
		return view;
	}
	
	@Override
	public void onResume() {
		
		flush();

		Request_Order_Count();
		
		new RightHelper().IsTokenPass(getActivity(), handler, Request_token);
				
		Public_BaiduTJ.pageStart(getActivity(), Public_BaiduTJ.Fragment_Menu);
		
		super.onResume();
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Public_BaiduTJ.pageEnd(getActivity(), Public_BaiduTJ.Fragment_Menu);	
	}
	
	/** 初始化试图 */
	private void initView(View view){
		
		/*头像*/	
		main_menu_userinfo_logo = (ImageView)view.findViewById(R.id.main_menu_userinfo_logo);
		main_menu_userinfo_lin = (LinearLayout)view.findViewById(R.id.main_menu_userinfo_lin);
		main_menu_userinfo_nickname = (TextView)view.findViewById(R.id.main_menu_userinfo_nickname);
		main_menu_userinfo_phone = (TextView)view.findViewById(R.id.main_menu_userinfo_phone);
		
		/*订单栏*/
		main_menu_order = (LinearLayout)view.findViewById(R.id.main_menu_order);
		order_all = (LinearLayout)view.findViewById(R.id.order_all);
		
		order_drive_lin = (LinearLayout)view.findViewById(R.id.order_drive_lin);
		order_drive = (TextView)view.findViewById(R.id.order_drive);
		
		order_other_lin = (LinearLayout)view.findViewById(R.id.order_other_lin);
		order_other = (TextView)view.findViewById(R.id.order_other);
		
		order_wind_lin = (LinearLayout)view.findViewById(R.id.order_wind_lin);
		order_wind = (TextView)view.findViewById(R.id.order_wind);
		
		gift_lin = (LinearLayout)view.findViewById(R.id.gift_lin);
		score_lin = (LinearLayout)view.findViewById(R.id.score_lin);
		rink_lin = (LinearLayout)view.findViewById(R.id.rink_lin);
		system_lin = (LinearLayout)view.findViewById(R.id.system_lin);
		phone_lin = (LinearLayout)view.findViewById(R.id.phone_lin);
		
		version_code = (TextView)view.findViewById(R.id.version_code);
		version_code.setText("版本号:"+SystemUtils.getVersion(getActivity()));
		/*订单栏*/
		order_drive.setText("自驾订单");
		order_other.setText("门到门订单");
		order_wind.setText("顺风车订单");
		
		flush();//初始化话数据
					
	}
	
	public void flush(){
		/*判断是否登录*/
		if(!SharedPreferenceHelper.isLogin(getActivity())){
			
			main_menu_userinfo_nickname.setText("未登录");
			main_menu_userinfo_phone.setVisibility(View.GONE);
			
			order_drive.setText("自驾订单");
			order_other.setText("门到门订单");
			order_wind.setText("顺风车订单");
					
		}else{
			
			/*用户栏*/
			String phone = SharedPreferenceHelper.getString(getActivity(), Public_SP.Account, "phone");
			String nickName = SharedPreferenceHelper.getString(getActivity(), Public_SP.Account, "nickName");
					
			if(nickName.equals(phone)){
				
				main_menu_userinfo_phone.setVisibility(View.VISIBLE);
				main_menu_userinfo_phone.setText(phone);
				main_menu_userinfo_nickname.setText("未填写昵称");
				
			}else{
				
			    main_menu_userinfo_phone.setVisibility(View.VISIBLE);
				main_menu_userinfo_phone.setText(phone);
				main_menu_userinfo_nickname.setText(nickName);			
			}
			
			
		}
	
	}
	
//	private void initLogo(){
//		if(createFile()){
//			Bitmap bitmap = BitmapFactory.decodeFile(filePath.getAbsolutePath());
//			mypage_logo.setImageBitmap(bitmap);
//		}	
//	}


//	
////		@Override
////		public void onActivityResult(int requestCode, int resultCode, Intent data) {
////			super.onActivityResult(requestCode, resultCode, data);
////			if(requestCode == ok){
////				System.out.println("登陆成功k");
////				SharedPreferences preferences =getActivity().getSharedPreferences(getActivity().getResources().getString(R.string.password), Context.MODE_PRIVATE);		
////				System.out.println("登陆成功"+preferences.getBoolean("loginstate", false));
////				if(preferences.getBoolean("loginstate", false)){
////					System.out.println("登陆成功1");
////					show(preferences.getString("Name", "没有用户名"));
////				}
////			}
////		}
//	
	
	private void Request_Order_Count(){

		/*判断是否有网*/
		if(!NetworkHelper.isNetworkAvailable(getActivity())){
			return;
		}
		System.out.println("2");
		/*判断是否登录*/
		if (!SharedPreferenceHelper.isLogin(getActivity())) {					
			return;
		}
		
		String userId = new Integer(SharedPreferenceHelper.getUid(getActivity())).toString();
		
		new HttpHelper().initData(HttpHelper.Method_Get, getActivity(), "api/user/"+userId+"/order?currentPage=1&pageSize=100", null, null, handler, Request_Order_Count, 1, new TypeReference<ArrayList<Order>>() {});
		
		String api_door = "api/door/user/orders?currentPage=1&pageSize=100&userId="+userId;
		new CarList_Helper().initData(HttpHelper.Method_Get, getActivity(), api_door, null, null, handler, Request_doortodoor_Count, 1, new TypeReference<ArrayList<Order>>() {});

		new HttpHelper().initData(HttpHelper.Method_Get, getActivity(), "api/user/"+userId+"/freeRideOrder?currentPage=1&pageSize=100", null, null, handler, Request_wind_Count, 1, new TypeReference<ArrayList<Order>>() {});
		
	}
	
	private void initHandler() {

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				switch (msg.what) {

					case Request_token:
						if(msg.getData().getString("message").equals(HandlerHelper.Ok)){
							
							isTokenOverTime = false;
						}else{
							
							isTokenOverTime = true;
						}
						
						break;
				
					case Request_Order_Count:
						
						if(msg.getData().getString("message").equals("ok")){
							order_drive.setText("自驾订单("+((List<Order>)msg.obj).size()+")");
						}else{
							order_drive.setText("自驾订单");
						}
						break;
						
					case Request_doortodoor_Count:
						
						if(msg.getData().getString("message").equals("ok")){
							order_other.setText("门到门订单("+((List<Order>)msg.obj).size()+")");
						}else{
							order_other.setText("门到门订单");
						}
						break;
						
					case Request_wind_Count:
						
						if(msg.getData().getString("message").equals("ok")){
							order_wind.setText("顺风车订单("+((List<Order>)msg.obj).size()+")");
						}else{
							order_wind.setText("顺风车订单");
						}
						break;	
						
					default:
						break;
				}
			}
		};
	}
		
	
	/** 设置监听器 */
	private void initListener(){
		main_menu_userinfo_lin.setOnClickListener(this);
		main_menu_order.setOnClickListener(this);
		order_drive_lin.setOnClickListener(this);
		order_other_lin.setOnClickListener(this);
		order_wind_lin.setOnClickListener(this);
		
		gift_lin.setOnClickListener(this);
		score_lin.setOnClickListener(this);
		rink_lin.setOnClickListener(this);
		phone_lin.setOnClickListener(this);
	}

	/** 实现监听器  */
	@Override
	public void onClick(View view) {
	
		if(!Public_Param.isOpened){
			return;
		}
		
		switch(view.getId()){
		
			case R.id.main_menu_userinfo_lin:
				System.out.println("1");
				/*判断是否有网*/
				if(!NetworkHelper.isNetworkAvailable(getActivity())){
					return;
				}
				System.out.println("2");
				/*判断是否登录*/
				if (!SharedPreferenceHelper.isLogin(getActivity())) {

					Public_Param.loginFrom = Public_Param.loginFrom_NotLogin;
					Intent intent = new Intent(getActivity(), Login_Activity.class);
					getActivity().startActivity(intent);
										
					return;
				}
				System.out.println("3");
				/*判断token是否过期*/
				if(!isTokenOverTime){
					
					Intent userinfo_intent = new Intent();
					userinfo_intent.setClass(getActivity(), User_Activity.class);
					getActivity().startActivity(userinfo_intent);
				}else{
					
					Public_Param.loginFrom = Public_Param.loginFrom_NotLogin;
					Intent intent = new Intent(getActivity(), Login_Activity.class);
					getActivity().startActivity(intent);
				}				
		    	break;
		    
			case R.id.main_menu_order:
				if(order_all.getVisibility() == View.GONE){
					order_all.setVisibility(View.VISIBLE);
				}else{
					order_all.setVisibility(View.GONE);
				}
				break;
		    	
			case R.id.order_drive_lin:
				/*判断是否有网*/
				if(!NetworkHelper.isNetworkAvailable(getActivity())){
					return;
				}
				
				/*判断是否登录*/
				if (!SharedPreferenceHelper.isLogin(getActivity())) {

					Intent intent = new Intent(getActivity(), Login_Activity.class);
					getActivity().startActivity(intent);
					return;
				}
				Intent order = new Intent();
				order.setClass(getActivity(), Activity_Order_List.class);
				order.putExtra("way", "order");
				startActivity(order);
		    	break;
		    	
			case R.id.order_other_lin:
				/*判断是否有网*/
				if(!NetworkHelper.isNetworkAvailable(getActivity())){
					return;
				}
				
				/*判断是否登录*/
				if (!SharedPreferenceHelper.isLogin(getActivity())) {

					Intent intent = new Intent(getActivity(), Login_Activity.class);
					getActivity().startActivity(intent);
					return;
				}
								
				Intent order_other = new Intent();
				order_other.setClass(getActivity(), Activity_Order_List.class);
				order_other.putExtra("way", "doortodoor");
				startActivity(order_other);
		    	break;
		    
			case R.id.order_wind_lin:
				/*判断是否有网*/
				if(!NetworkHelper.isNetworkAvailable(getActivity())){
					return;
				}
				
				/*判断是否登录*/
				if (!SharedPreferenceHelper.isLogin(getActivity())) {

					Intent intent = new Intent(getActivity(), Login_Activity.class);
					getActivity().startActivity(intent);
					return;
				}
				Intent order_wind = new Intent();
				order_wind.setClass(getActivity(), Activity_Order_List.class);
				order_wind.putExtra("way", "freeride");
				startActivity(order_wind);
				break;
		    
			case R.id.gift_lin:
				/*判断是否有网*/
				if(!NetworkHelper.isNetworkAvailable(getActivity())){
					return;
				}
				
				/*判断是否登录*/
				if (!SharedPreferenceHelper.isLogin(getActivity())) {

					Intent intent = new Intent(getActivity(), Login_Activity.class);
					getActivity().startActivity(intent);
					return;
				}
				IntentHelper.startActivity(getActivity(), Activity_Ticket.class);
				break;
				
			case R.id.score_lin:
				/*判断是否有网*/
				if(!NetworkHelper.isNetworkAvailable(getActivity())){
					return;
				}
				
				/*判断是否登录*/
				if (!SharedPreferenceHelper.isLogin(getActivity())) {

					Intent intent = new Intent(getActivity(), Login_Activity.class);
					getActivity().startActivity(intent);
					return;
				}
				IntentHelper.startActivity(getActivity(), Activity_Score.class);
				break;	
			
			case R.id.rink_lin:
				/*判断是否有网*/
				if(!NetworkHelper.isNetworkAvailable(getActivity())){
					return;
				}
				
				/*判断是否登录*/
				if (!SharedPreferenceHelper.isLogin(getActivity())) {

					Intent intent = new Intent(getActivity(), Login_Activity.class);
					getActivity().startActivity(intent);
					return;
				}
				
				/*判断token是否过期*/
				if(!isTokenOverTime){
					
					IntentHelper.startActivity(getActivity(), Activity_Level.class);
				}else{
					
					Public_Param.loginFrom = Public_Param.loginFrom_NotLogin;
					Intent intent = new Intent(getActivity(), Login_Activity.class);
					getActivity().startActivity(intent);
				}	
								
				break;	
			
			case R.id.phone_lin:
				final CustomDialog.Builder ibuilder;
				ibuilder = new CustomDialog.Builder(getActivity());
				ibuilder.setTitle("提示");
				ibuilder.setMessage("您确认要拨打"+Public_Data.phone+"吗");
				ibuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						
						Uri uri = Uri.parse("tel:" + Public_Data.phone);  
				        Intent intent = new Intent(Intent.ACTION_CALL, uri);  
				        startActivity(intent); 

					}
				});
				ibuilder.setNegativeButton("取消", null);
				
				ibuilder.create().show();
				break;
				
		    default:
		    	break;
		}

	}
//	
//	private void closeSliding(){
//		if(slidingPaneLayout.isOpen()){
//			slidingPaneLayout.closePane();
//		}else{
//			slidingPaneLayout.openPane();
//		}
//	}
//	
//	private void show(String name){
//		System.out.println("登陆成功2");
//			note.setText(name);
//			note.setTextSize(15);
//			go.setVisibility(View.VISIBLE);
//			mypage_logo.setVisibility(View.VISIBLE);
//			tv_mypage_login.setVisibility(View.GONE);
//			my_order.setVisibility(View.VISIBLE);
//			mypage_lin_userinfo.setOnClickListener(new OnClickListener(){
//
//				@Override
//				public void onClick(View arg0) {
//					Toast.makeText(getActivity(), "用户信息", 3000).show();
//					Intent intent = new Intent();
//					intent.setClass(getActivity(), UserInfoActivity.class);
//					getActivity().startActivity(intent);				
//				}
//				
//			});
//		}
//	private void hide(){
//		note.setText("您还没登陆~");
//		note.setTextSize(12);
//		tv_mypage_login.setVisibility(View.VISIBLE);
//		go.setVisibility(View.GONE);
//		mypage_logo.setVisibility(View.GONE);
//		my_order.setVisibility(View.GONE);
//	}
//	
//	private boolean createFile(){
//		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
//			File  dir = new File(Environment.getExternalStorageDirectory()+"/hbzh/logo");
//			if(!dir.exists()){
//				dir.mkdirs();
//			}
//			filePath = new File(dir, "logo.png");
//			//PublicMessage.showToast(UserInfoActivity.this, filePath.getAbsolutePath(), Toast.LENGTH_SHORT);
//			if(filePath.exists()){
//				return true;
//			}else{
//				return false;
//			}
//		}else{
//			//PublicMessage.showToast(UserInfoActivity.this, "sd卡不可用，请检查是否插入...", Toast.LENGTH_SHORT);
//			return false;
//		}
//		
//	}
//	
//	
//	/** 更新*/
//	public void update(){
//		UpdateManager update = new UpdateManager(getActivity());
//		update.setListener(this);
//		update.UpdateManager_do();
//	}  
//	  
//	@Override
//	public void update_finish() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void closeApp() {
//		getActivity().finish();
//	}
//	  
//	
//	/**核对apk的版本信息
//	 * 1.判断网络是否可以2.是否登陆3.下载网络的版本4.比对网络版本和本地版本是否一致
//	 * 5.更新成功后，重新安装之后，要将xml文件中的版本信息更新至最新，否则会一直更新，xml中的版本未更新
//	 *  下载完成后，跟新xml
//	 * 6.这里没有限制了，因为如果welcomeActivity中没有下载到更新信息如何办*/
//	private void CheckApkInfo(){
//		System.out.println("0");
//		if(PublicMessage.isNetworkAvailable(getActivity())){
//			final SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.apkinfo), Context.MODE_PRIVATE);
//			int apkNumber = preferences.getInt("apkNumber", 0);
//			int apkId = preferences.getInt("apkId", 100);
//			String apkVersion = preferences.getString("apkVersion", "lkl");
////			this.apkId = apkId;
////			this.apkName = apkName;
////			this.apkVersion = apkVersion;
////			this.apkNote = apkNote;
////			this.apkNumber = apkNumber;
//			System.out.println("1:"+apkNumber+":"+apkId+":"+apkVersion);
//			//从来没获取过apk信息
//			if(true){
//				
//				//1.获取本地data秘钥：如果没有秘钥则不获取
//				SharedPreferences passwordpreferences = getActivity().getSharedPreferences(getResources().getString(R.string.password), Context.MODE_PRIVATE);
//				String data = passwordpreferences.getString("data", "");
////				String data = "BABBFF0C6CD7B77E25CB26E0488C9F915C00D6394807A6E569620CA9B3AA5862FC6DBB03B2C3318F70655C188D931BFBE1326549450BD095136C1C8F30F05D4A567D947F35E493FFEE1489DE1370F89AD97E454710D355B0D7D104B0F6B74DCDC88186169240D60EA603ACF745016BA14787FEAA1B3A66270674FB4BA26CC3B4A8BD8CCAB50647742D9AEE5838FF2A798AE1EA07E7DF8976E7AEE8474DC68346204D9F7966841EDED5F6B02CA26058AD5B09D2DF6079B245E5ACB9141E13A4469FA1FE0CC61650CD65BD71EF2D174C8E8E866DDF539E8CC12B6C6EA71DBDDEB84D2B7AE673A47E5CA389457F27AF5AC04E404191B6C3E0B3125BA1F822D9CF7245DE5A91B097ADD5F58836E3E90376B810E2C864A057A72D5FD0677A24F3E967D199148847F368032263DE7418930E46367D6990F3F4D8BD5A2BEE026628DE10A33EB9B89839F8A9BE07C3F41B9A586750B39B4E95F0AB2C0B727C53E6F745AABA79F59F8DDCF145DE25D2FB4AC43A8540DA3A577DD86A6A753B6E08FAF43DD6A767D76C1AE5797F7CEA5E7835AF41F0943396D9FF78A2CEB95BC20C53D20E8AC0CCD859DDAB6E40037190028EA85CB7C5872EA84A625DCE93481F47E7BEFC77830C29C3C4C25E6D61CC214F517F1433EE35BB4526A901A6062334C4C41F6B22B20A4373E87D356617C917E4BC48B5A9036D56B0B8F36EF39862A085444B9AC9B5E3ADD94701C9774C7833BC0FA706B8FEDB1FFBD76DC40CB02017A2CBD8C7D279A17CD96B23B27C0C83C8A8A32A3A3F4CEF1367221BA02C3F6BEC2938CA636AD43820B757E3FE3E4D4D38FBF4FEE881343432D645A44BABE88438445B4A80805AB6246E4E3DB45A7BF7348851F8650F";
//				System.out.println("2"+data);
//				if(true){
//					//2.从服务器加载版本信息
//					AsyncHttpClient httpClient = new AsyncHttpClient();
//					
//					RequestParams params = new RequestParams();//设置请求参数
////					params.put("token", data);//无需传递请求参数
//					System.out.println("3");
//					String url = getResources().getString(R.string.website)+"IAPPInfo.cc";//设置请求的url
//
//					httpClient.get(url, params, new AsyncHttpResponseHandler(){
//
//						@Override
//						public void onFailure(int arg0, Header[] arg1,
//								byte[] arg2, Throwable arg3) {
//							Toast.makeText(getActivity(), "数据发送失败", Toast.LENGTH_SHORT).show();
//						}
//
//						@Override
//						public void onSuccess(int arg0, Header[] arg1,
//								byte[] arg2) {
//							System.out.println("5");
//							ApkInfo apkinfo = JSONHelper.getApkInfo(new String(arg2).trim());			
//							if(preferences.getString("apkVersion", "").equals(apkinfo.getApkVersion())){
//								Toast.makeText(getActivity(), getResources().getString(R.string.msg_lastversion), Toast.LENGTH_SHORT).show();
//							}else{
//								//如果版本不一致则进入更新
//								update();
//							}
//							System.out.println("6");
//						}
//						
//					});
//				}
//				
//			}else{
////				
////				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.msg_notlogin), Toast.LENGTH_SHORT).show();
//			}
//		}else{
//			//网络不可用
//			Toast.makeText(getActivity(), getResources().getString(R.string.network), Toast.LENGTH_SHORT).show();
//		}	
//	}
}

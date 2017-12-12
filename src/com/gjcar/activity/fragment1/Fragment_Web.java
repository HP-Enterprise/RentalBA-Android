package com.gjcar.activity.fragment1;

import com.gjcar.activity.user.Login_Activity;
import com.gjcar.app.R;
import com.gjcar.data.data.Public_Param;
import com.gjcar.data.data.Public_SP;
import com.gjcar.utils.SharedPreferenceHelper;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * @author Administrator
 *http://www.wpf123.com/news/IOS/201306/946.html
 */
@SuppressLint("ValidFragment")
public class Fragment_Web extends Fragment{

	/* 功能区 */
	private WebView webView;
	private ProgressBar loadhtml_progressbar;
	private int progress = 0;
	
	/*Handler*/
	private Handler handler;
	private final static int msg_start = 101;
	private final static int msg_loading = 102;
	private final static int msg_end = 103;
	private final static int msg_error = 104;
	
	/*网址*/
	private String url;
	
	public Fragment_Web(String url){
		this.url = url;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_web, null);

		initView(view);

		initHandler();
		
		return view;
		
	}

	/**
	 * 初始Handler
	 */
	private void initHandler() {
	
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {				
				super.handleMessage(msg);
				
				switch (msg.what) {
					case msg_start:
						loadhtml_progressbar.setVisibility(View.VISIBLE);						
						break;
	
					case msg_loading:
						loadhtml_progressbar.setProgress(progress);
						break;
						
					case msg_end:
						loadhtml_progressbar.setVisibility(View.GONE);
						break;
						
					case msg_error:
						loadhtml_progressbar.setVisibility(View.GONE);
						break;
					default:
						break;
				}
			}
		};
		
	}

	/**
	 * 初始化视图
	 */
	@SuppressLint("NewApi")
	private void initView(View view) {

		loadhtml_progressbar = (ProgressBar)view.findViewById(R.id.loadhtml_progressbar);
		
		// 加载网页
		webView = (WebView) view.findViewById(R.id.wb_aboutus);
		 webView.setWebViewClient(new WebViewClient() {

	            @Override
	            public boolean shouldOverrideUrlLoading(WebView view, String urls) {
	                if (Public_Param.send_toWeb == 2) {
	                    if ("gjcar://share.loginAddress".equals(urls)) {   //没登录，跳转到登录界面
	                        Intent intent = new Intent(getActivity(), Login_Activity.class);
	                        startActivityForResult(intent, 0x00);
	                        return true;
	                    } else if ("gjcar://share.shareAddress".equals(urls)) {  //分享，调起分享面板
	                        webView.evaluateJavascript("getAddress()", new ValueCallback<String>() {
	                            @Override
	                            public void onReceiveValue(String value) {
	                                
	                            	String[] strs = value.split("\"");
	                                String url = "";
	                                if (strs.length == 0){
	                                    url = "";
	                                } else if (strs.length == 1){
	                                    url = strs[0];
	                                }else {
	                                    for (String urls : strs){
	                                        url = url + urls;
	                                    }
	                                }
	                                showSharePop(url);   //分享

	                            }
	                        });
	                        return true;
	                    }else if (url.equals(urls)){
	                    	
	                        view.loadUrl(urls);
	                        return true;
	                    }
	                    else {
	                        Public_Param.send_toWeb = 3;
	                        getActivity().finish();
	                        return true;
	                    }
	                }
//	                if (url != null && Public_Param.send_toWeb == 2) {
	//////
//////						webView.evaluateJavascript("getCount()", new ValueCallback<String>() {
//////							@Override
//////							public void onReceiveValue(String value) {
//////								Toast.makeText(getActivity(),value,Toast.LENGTH_LONG).show();
//////							}
//////						});
	////
	////
	////
	////
	//
//	                }

	                return true;
	            }

	            @Override
	            public void onPageStarted(WebView view, String url, Bitmap favicon) {
	                super.onPageStarted(view, url, favicon);
	                //开始加载
	                handler.sendEmptyMessage(msg_start);
	            }

	            @Override
	            public void onPageFinished(WebView view, String url) {
	                super.onPageFinished(view, url);
	                //加载结束
	                handler.sendEmptyMessage(msg_end);

	                webView.loadUrl("javascript:getTel(" + SharedPreferenceHelper.getString(getActivity(), Public_SP.Account, "phone") + ")");
//					webView.loadUrl("javascript:shareStatus(true)");
	            }

	            @Override
	            public void onReceivedError(WebView view, int errorCode,
	                                        String description, String failingUrl) {
	                super.onReceivedError(view, errorCode, description, failingUrl);
	                //加载出错
	                handler.sendEmptyMessage(msg_error);
	            }

	        });// 在新的WebView中打开连接

		webView.canGoBackOrForward(10);// 前进后退的步数
		
		WebSettings webSet = webView.getSettings();
		webSet.setJavaScriptEnabled(true);// js是否可用
		webSet.setBuiltInZoomControls(true);////会出现放大缩小的按钮
		webSet.setSupportZoom(true);//缩放
		
		webSet.setUseWideViewPort(true);//设置此属性，可任意比例缩放。 
		webSet.setLoadWithOverviewMode(true); //设置webview加载的页面的模式

		webSet.setSupportMultipleWindows(true);
		webView.setWebChromeClient(new MyWebChromeClient(getActivity()));
		webView.setInitialScale(10);//为25%，最小缩放等级  
		webView.loadUrl(url);
//		webView.loadUrl("file:///android_asset/nationalDayActivity/index.html");
//		webView.loadUrl("http://182.61.22.80:8082/activity/mobile/nationalDay/index.html");
	}

	public class MyWebChromeClient extends WebChromeClient{
		
		public MyWebChromeClient(Activity activity){
			
		}

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			
			if(newProgress < 100){
				progress = newProgress;
				handler.sendEmptyMessage(msg_loading);		
				
			}
			
			super.onProgressChanged(view, newProgress);
		}
		
		@Override
		public void onReceivedTitle(WebView view, String title) {
			
			//PublicMessage.showToast(activity, title);
			super.onReceivedTitle(view, title);
		}
	}

	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (getActivity().RESULT_OK == resultCode){
            switch (requestCode){
                case 0x00:
                    webView.loadUrl("javascript:getTel(" + SharedPreferenceHelper.getString(getActivity(), Public_SP.Account, "phone") + ")");
                    break;
            }
        }
    }

	 /**
     * 分享
     *
     * @param url
     */
    public void showSharePop(String url) {
        UMImage thumb = new UMImage(getActivity(), R.drawable.deskicon);
        thumb.compressFormat = Bitmap.CompressFormat.PNG;
        
        UMWeb web = new UMWeb(url);
        web.setTitle("high玩国庆");
        web.setDescription("国庆宅在家？" + SharedPreferenceHelper.getString(getActivity(), Public_SP.Account, "phone") + "分享给你红包，快点出来high~");
        web.setThumb(thumb);
        new ShareAction(getActivity())
//				.withTitle(getString(R.string.app_name))
//				.withText("测试")
//				.withTargetUrl("")
                .withMedia(web)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        webView.loadUrl("javascript:shareStatus(true)");  //分享成功回调
                        Toast.makeText(getActivity(), "分享成功", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
//                        webView.loadUrl("javascript:shareStatus(false)");//分享取消回调
                        Toast.makeText(getActivity(), "分享取消", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        webView.loadUrl("javascript:shareStatus(false)");//分享失败回调
                        Toast.makeText(getActivity(), "分享失败" + throwable.toString(), Toast.LENGTH_LONG).show();
                    }
                }).open();
    }

}

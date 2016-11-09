package com.gjcar.activity.fragment1;

import com.gjcar.app.R;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

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
		webView.setWebViewClient(new WebViewClient(){
			
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
	
		webSet.setSupportMultipleWindows(true);
		webView.setWebChromeClient(new MyWebChromeClient(getActivity()));
		webView.setInitialScale(160);//为25%，最小缩放等级  
		webView.loadUrl(url);

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

}

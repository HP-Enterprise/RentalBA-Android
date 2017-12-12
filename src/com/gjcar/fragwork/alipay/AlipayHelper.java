package com.gjcar.fragwork.alipay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.gjcar.data.data.Public_Api;
import com.gjcar.utils.TimeHelper;

public class AlipayHelper {

	// 商户PID
//	public static final String PARTNER = "2088221353698177";
//	// 商户收款账号
//	public static final String SELLER = "zucheyun@b-car.cn";
//	// 商户私钥，pkcs8格式
//	public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALTC/6PbrpCw2IK5pNFwfJ9xmR5tpRkbcLcGHbuu0mB9cBeK5mzvGiCSQp1SYXaaNIsffA1wkKtuwTD2KXYraG1wh2bYNb8WtBTUtwJxLEHaPtMV9/uBrbqDfOtddp96KR+GnQEAF/6HkKsMfxf8mHzfrBv2kyQCbhiAVBrx6PzdAgMBAAECgYB+aYBuD0vdVE+V3E4vSgNdXgw/A17aWB5TYKuafYASiqbBUBolRHF5JdAARYRzdRQZ10LiAz6pJSNmIkCMq36zHXK6WakLn4iNmxKYuwkCvz0Jmk1EBurBIvrcmlESW1IPTp/EFVoD/6m4q133d3XsxjlVUCGZ5AFKX9+9eVWd4QJBANv8D9Af2zzPzhXyIQ4ajQnnC8yHaTWGTFHRk/+Nn114dpZNEfXZNs3Pog4I6epz+VgSw2du/0Dyafgmn2kmKoMCQQDSWwljq7BqYSRcuqvHPjmSRtaSo9WdQ5TnYW9J/mDgByfAMRYFeX0Xpls7BHDjo+MK+7jvth4zUpSnIeyJUx0fAkEAg/dzOQRTTejPlaS6Ja7R2xXqoxi8iap2EEMsiIraBoWkhkfXtWdIFDEx4z9/q/FErIwdAui4YarK3V22FasapwJBAIlLwAIc8mVMiCY59Jpz47G0qKJHaspdbNfkgXXDIUm3gdtwblYeaGZCPzNy/5ekxTDLAXb74BRRZxL7El7DL7MCQAN+bV2TmstYA7Ea2osPloAMZHuKZNhcgPr4gxMNLeWPlKjnnydY7Xg2xFvmmSfJ6Wh5et1wdpleXGeMArsx8YM=";
//	// 支付宝公钥
//	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
//	
	// 商户PID
	public static final String PARTNER = "2088311106439495";
	// 商户收款账号
	public static final String SELLER = "huachen_zulin@163.com";
	// 商户私钥，pkcs8格式
	
	public static final String RSA_PRIVATE = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALfGjge8qE7k+7aZxRyrDHxynviQSRp1awTt6uuQrYJr6XbVZZJcYf39tOMiRE2AzksZVWlZcq/sQKRUwC9yVqZzNq4Ke2vVdfftkNk+6oNSvG10HSUSTDKhCxDwluO2+NqCoCAQw1P/168V/7YdcyvptOuP1vgPWH2s/X63j7flAgMBAAECgYAC93+ffFozO9scbYsTFWfUMn2CgcHMXYzmvXiHaQSEEH3qXzOOk1M5qHjdGdaEccniyHvqgXkqgePhQ0T+/xeK/Vq3npBMyTPg2BbuFi/fRCErcOpdQOXmtbze/FKsSS0HJ7QeNgLnGjrJ5KhknKQmGNEFjQXBdE+44TDHf11T2QJBAOJQCb5WjhZ2tSz5D3XNtrGf7CFyTLQVFklUS0YABYtXIZ9QPJaZkaNtEMxr8PeEnL462pBhrDnaQlZuvoaulHMCQQDP4g0AL6uJmpdyvApcuMH7gEyNwRCuwKRqNGtdugwuN6k5oLnT56zLQ2Pdl1WX9Kv44JIycFdunWYv7PNz8kRHAkAbcGzeAQyVOKta2o+/TsPZ4XP10i/unafoGCpQQGxrqpLPCCFweQopcG3a+zNqL0/52JTrcIw7L3VfmWnMVpp1AkBdczvu6n8NY65TSI7L8c5aFenUC4dJV5ZRm/Dr+FfDawgqvMLsrIfz8/5vvbkfj0DDp4hxHilfs2gdgUJLzAu/AkAyZj56I8sJ/2DK+e0xRivwDNcNepp7ZbgUTgQSZGl8cNH3gqWmd3CG4MYLrHXZC0vrnNdAhU+lqFcvN3w4TSB/";
	// 支付宝公钥
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
	
	private static final int SDK_PAY_FLAG = 1;	
	
	//hanlder
	private Activity activity;
	private Handler handler;
	public static final int Pay_Ok = 111;
	public static final int Pay_Process = 112;
	public static final int Pay_Fail = 113;
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unused")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				/**
				 * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
				 * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
				 * docType=1) 建议商户依赖异步通知
				 */
				String resultInfo = payResult.getResult();// 同步返回需要验证的信息
				System.out.println("支付结果"+resultInfo);
				String resultStatus = payResult.getResultStatus();System.out.println("支付返回码"+payResult.getResultStatus());
				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					
					handler.sendEmptyMessage(Pay_Ok);
					
					Toast.makeText(activity, "支付成功", Toast.LENGTH_SHORT).show();
				} else {
					// 判断resultStatus 为非"9000"则代表可能支付失败
					// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(activity, "支付结果确认中", Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						if(TextUtils.equals(resultStatus, "4000")){
							Toast.makeText(activity, "请先安装支付宝", Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(activity, "支付失败", Toast.LENGTH_SHORT).show();
						}
						
					}
				}
				break;
			}
			default:
				break;
			}
		};
	};
	
	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	public void pay(final Activity activity, Handler handler, String subject, String body, String price,String orderId) {

		this.activity = activity;
		this.handler = handler;
		
		String orderInfo = getOrderInfo(subject, body, price,orderId);

		/**
		 * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
		 */
		String sign = sign(orderInfo);
		try {
			/**
			 * 仅需对sign 做URL编码
			 */
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		/**
		 * 完整的符合支付宝参数规范的订单信息
		 */
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(activity);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo, true);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}
	
	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	private String getOrderInfo(String subject, String body, String price,String orderId) {

		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo(orderId) + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";
		
		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + Public_Api.appWebSite+"api/alipay/notify" + "\"";
		//orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";
		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}
	
	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	private String getOutTradeNo(String orderId) {
//		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
//		Date date = new Date();
//		String key = format.format(date);
//
//		Random r = new Random();
//		key = key + r.nextInt();
//		key = key.substring(0, 15);
//		return key;
		String key = TimeHelper.getTradeCode(orderId);
        return  key;
		
	}
	
	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	private String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}
	
	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	private String getSignType() {
		return "sign_type=\"RSA\"";
	}
}

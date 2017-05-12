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

	// �̻�PID
//	public static final String PARTNER = "2088221353698177";
//	// �̻��տ��˺�
//	public static final String SELLER = "zucheyun@b-car.cn";
//	// �̻�˽Կ��pkcs8��ʽ
//	public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALTC/6PbrpCw2IK5pNFwfJ9xmR5tpRkbcLcGHbuu0mB9cBeK5mzvGiCSQp1SYXaaNIsffA1wkKtuwTD2KXYraG1wh2bYNb8WtBTUtwJxLEHaPtMV9/uBrbqDfOtddp96KR+GnQEAF/6HkKsMfxf8mHzfrBv2kyQCbhiAVBrx6PzdAgMBAAECgYB+aYBuD0vdVE+V3E4vSgNdXgw/A17aWB5TYKuafYASiqbBUBolRHF5JdAARYRzdRQZ10LiAz6pJSNmIkCMq36zHXK6WakLn4iNmxKYuwkCvz0Jmk1EBurBIvrcmlESW1IPTp/EFVoD/6m4q133d3XsxjlVUCGZ5AFKX9+9eVWd4QJBANv8D9Af2zzPzhXyIQ4ajQnnC8yHaTWGTFHRk/+Nn114dpZNEfXZNs3Pog4I6epz+VgSw2du/0Dyafgmn2kmKoMCQQDSWwljq7BqYSRcuqvHPjmSRtaSo9WdQ5TnYW9J/mDgByfAMRYFeX0Xpls7BHDjo+MK+7jvth4zUpSnIeyJUx0fAkEAg/dzOQRTTejPlaS6Ja7R2xXqoxi8iap2EEMsiIraBoWkhkfXtWdIFDEx4z9/q/FErIwdAui4YarK3V22FasapwJBAIlLwAIc8mVMiCY59Jpz47G0qKJHaspdbNfkgXXDIUm3gdtwblYeaGZCPzNy/5ekxTDLAXb74BRRZxL7El7DL7MCQAN+bV2TmstYA7Ea2osPloAMZHuKZNhcgPr4gxMNLeWPlKjnnydY7Xg2xFvmmSfJ6Wh5et1wdpleXGeMArsx8YM=";
//	// ֧������Կ
//	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
//	
	// �̻�PID
	public static final String PARTNER = "2088311106439495";
	// �̻��տ��˺�
	public static final String SELLER = "huachen_zulin@163.com";
	// �̻�˽Կ��pkcs8��ʽ
	
	public static final String RSA_PRIVATE = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALfGjge8qE7k+7aZxRyrDHxynviQSRp1awTt6uuQrYJr6XbVZZJcYf39tOMiRE2AzksZVWlZcq/sQKRUwC9yVqZzNq4Ke2vVdfftkNk+6oNSvG10HSUSTDKhCxDwluO2+NqCoCAQw1P/168V/7YdcyvptOuP1vgPWH2s/X63j7flAgMBAAECgYAC93+ffFozO9scbYsTFWfUMn2CgcHMXYzmvXiHaQSEEH3qXzOOk1M5qHjdGdaEccniyHvqgXkqgePhQ0T+/xeK/Vq3npBMyTPg2BbuFi/fRCErcOpdQOXmtbze/FKsSS0HJ7QeNgLnGjrJ5KhknKQmGNEFjQXBdE+44TDHf11T2QJBAOJQCb5WjhZ2tSz5D3XNtrGf7CFyTLQVFklUS0YABYtXIZ9QPJaZkaNtEMxr8PeEnL462pBhrDnaQlZuvoaulHMCQQDP4g0AL6uJmpdyvApcuMH7gEyNwRCuwKRqNGtdugwuN6k5oLnT56zLQ2Pdl1WX9Kv44JIycFdunWYv7PNz8kRHAkAbcGzeAQyVOKta2o+/TsPZ4XP10i/unafoGCpQQGxrqpLPCCFweQopcG3a+zNqL0/52JTrcIw7L3VfmWnMVpp1AkBdczvu6n8NY65TSI7L8c5aFenUC4dJV5ZRm/Dr+FfDawgqvMLsrIfz8/5vvbkfj0DDp4hxHilfs2gdgUJLzAu/AkAyZj56I8sJ/2DK+e0xRivwDNcNepp7ZbgUTgQSZGl8cNH3gqWmd3CG4MYLrHXZC0vrnNdAhU+lqFcvN3w4TSB/";
	// ֧������Կ
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
				 * ͬ�����صĽ��������õ�����˽�����֤����֤�Ĺ����뿴https://doc.open.alipay.com/doc2/
				 * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
				 * docType=1) �����̻������첽֪ͨ
				 */
				String resultInfo = payResult.getResult();// ͬ��������Ҫ��֤����Ϣ
				System.out.println("֧�����"+resultInfo);
				String resultStatus = payResult.getResultStatus();System.out.println("֧��������"+payResult.getResultStatus());
				// �ж�resultStatus Ϊ��9000�������֧���ɹ�������״̬�������ɲο��ӿ��ĵ�
				if (TextUtils.equals(resultStatus, "9000")) {
					
					handler.sendEmptyMessage(Pay_Ok);
					
					Toast.makeText(activity, "֧���ɹ�", Toast.LENGTH_SHORT).show();
				} else {
					// �ж�resultStatus Ϊ��"9000"��������֧��ʧ��
					// "8000"����֧�������Ϊ֧������ԭ�����ϵͳԭ���ڵȴ�֧�����ȷ�ϣ����ս����Ƿ�ɹ��Է�����첽֪ͨΪ׼��С����״̬��
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(activity, "֧�����ȷ����", Toast.LENGTH_SHORT).show();

					} else {
						// ����ֵ�Ϳ����ж�Ϊ֧��ʧ�ܣ������û�����ȡ��֧��������ϵͳ���صĴ���
						if(TextUtils.equals(resultStatus, "4000")){
							Toast.makeText(activity, "���Ȱ�װ֧����", Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(activity, "֧��ʧ��", Toast.LENGTH_SHORT).show();
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
	 * call alipay sdk pay. ����SDK֧��
	 * 
	 */
	public void pay(final Activity activity, Handler handler, String subject, String body, String price,String orderId) {

		this.activity = activity;
		this.handler = handler;
		
		String orderInfo = getOrderInfo(subject, body, price,orderId);

		/**
		 * �ر�ע�⣬�����ǩ���߼���Ҫ���ڷ���ˣ�����˽Կй¶�ڴ����У�
		 */
		String sign = sign(orderInfo);
		try {
			/**
			 * �����sign ��URL����
			 */
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		/**
		 * �����ķ���֧���������淶�Ķ�����Ϣ
		 */
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// ����PayTask ����
				PayTask alipay = new PayTask(activity);
				// ����֧���ӿڣ���ȡ֧�����
				String result = alipay.pay(payInfo, true);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// �����첽����
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}
	
	/**
	 * create the order info. ����������Ϣ
	 * 
	 */
	private String getOrderInfo(String subject, String body, String price,String orderId) {

		// ǩԼ���������ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// ǩԼ����֧�����˺�
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// �̻���վΨһ������
		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo(orderId) + "\"";

		// ��Ʒ����
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// ��Ʒ����
		orderInfo += "&body=" + "\"" + body + "\"";

		// ��Ʒ���
		orderInfo += "&total_fee=" + "\"" + price + "\"";
		
		// �������첽֪ͨҳ��·��
		orderInfo += "&notify_url=" + "\"" + Public_Api.appWebSite+"api/alipay/notify" + "\"";
		//orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";
		// ����ӿ����ƣ� �̶�ֵ
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// ֧�����ͣ� �̶�ֵ
		orderInfo += "&payment_type=\"1\"";

		// �������룬 �̶�ֵ
		orderInfo += "&_input_charset=\"utf-8\"";

		// ����δ����׵ĳ�ʱʱ��
		// Ĭ��30���ӣ�һ����ʱ���ñʽ��׾ͻ��Զ����رա�
		// ȡֵ��Χ��1m��15d��
		// m-���ӣ�h-Сʱ��d-�죬1c-���죨���۽��׺�ʱ����������0��رգ���
		// �ò�����ֵ������С���㣬��1.5h����ת��Ϊ90m��
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_tokenΪ���������Ȩ��ȡ����alipay_open_id,���ϴ˲����û���ʹ����Ȩ���˻�����֧��
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// ֧��������������󣬵�ǰҳ����ת���̻�ָ��ҳ���·�����ɿ�
		orderInfo += "&return_url=\"m.alipay.com\"";

		// �������п�֧���������ô˲���������ǩ���� �̶�ֵ ����ҪǩԼ���������п����֧��������ʹ�ã�
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}
	
	/**
	 * get the out_trade_no for an order. �����̻������ţ���ֵ���̻���Ӧ����Ψһ�����Զ����ʽ�淶��
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
	 * sign the order info. �Զ�����Ϣ����ǩ��
	 * 
	 * @param content
	 *            ��ǩ��������Ϣ
	 */
	private String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}
	
	/**
	 * get the sign type we use. ��ȡǩ����ʽ
	 * 
	 */
	private String getSignType() {
		return "sign_type=\"RSA\"";
	}
}

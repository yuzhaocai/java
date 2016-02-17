/**
 * 
 */
package com.lczy.media.util;

import java.io.IOException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.lczy.common.web.HttpRequests;

/**
 * 发送短信的工具类.
 * 
 * @author wu
 *
 */
public class SmsSender {
	
	private static Logger logger = LoggerFactory.getLogger(SmsSender.class);
	
	private static String url = "http://www.lx198.com/sdk/send";
	private static String sign = "采媒在线";
	private static String username = "meilifang@cnmei.com";
	private static String pwd = "cnmei123";
	
	
	public static String send(String phoneNumber, String msg) throws Exception {
		try {
			return send(url,username,pwd,phoneNumber,msg,sign);
		} catch( Exception e ) {
			logger.error("短信发送失败", e);
			throw e;
		}
	}

	private static String send(String url,String userId,String password,String phoneNumber,String msg,String smsSign) throws IOException {

		Map<String, String> params = Maps.newHashMap();
		params.put("accName", userId);
		params.put("accPwd", MD5.getMd5String(password));
		params.put("aimcodes", phoneNumber);
		params.put("content", msg + "【" + smsSign + "】");
		params.put("bizId", BizNumberUtil.createBizId());
		params.put("dataType", "string");
		
		String res = HttpRequests.doPost(url, params);
		
		return res;
	}
	
	static class BizNumberUtil {
		public static  int curttNo;
		private final static String dataFormatString="yyMMddHHmmss";
		public  synchronized static final String createBizId(){
			if(curttNo<999) {
				curttNo++;
			}else{
				curttNo=1;
			}
			String curttNoStr=String.valueOf(curttNo);
			while(curttNoStr.length()<3){;
				curttNoStr="0"+curttNoStr;
			}
			return new SimpleDateFormat(dataFormatString).format(new Date())+curttNoStr;
		}
	}
	
	static class MD5 {
		private final static char[] hexDigits = { '0', '1', '2', '3', '4', '5','6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		private static String bytes2hex(byte[] bytes) {
			StringBuffer sb = new StringBuffer();
			int t;
			for (int i = 0; i < 16; i++) {// 16 == bytes.length;
				t = bytes[i];
				if (t < 0)
					t += 256;
				sb.append(hexDigits[(t >>> 4)]);
				sb.append(hexDigits[(t % 16)]);
			}
			return sb.toString();
		}
		
		public static String getMd5String(String strSrc) {
			try {
				// 确定计算方法
				MessageDigest md5 = MessageDigest.getInstance("MD5");
				// 加密后的字符串
				return bytes2hex(md5.digest(strSrc.getBytes()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}

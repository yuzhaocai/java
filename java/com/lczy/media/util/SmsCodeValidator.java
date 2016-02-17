package com.lczy.media.util;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.lczy.common.util.Randoms;

/**
 * 短信验证码校验器, 只应用于单机部署.
 * 
 * @author wu
 *
 */
@Component
public class SmsCodeValidator {
	
	private static final Logger log = LoggerFactory.getLogger(SmsCodeValidator.class);
	
	/**
	 * 验证码过期时间.
	 */
	private static final long expiration = 10 * 60 * 1000;
	
	private Map<String, Entry> cached = new ConcurrentHashMap<String, Entry>();
	
	class Entry {
		final String mobPhone;
		final String code;
		final long timestamp;
		
		Entry(String phoneNum, String code) {
			this.mobPhone = phoneNum;
			this.code = code;
			this.timestamp = System.currentTimeMillis();
		}
		
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("{mobPhone=").append(mobPhone).append(", ")
			  .append("code=").append(code).append(", ")
			  .append("timestamp=").append(timestamp).append("}");
			return sb.toString();
		}
	}
	
	class WatchDog implements Runnable {
		@Override
		public void run() {
			Collection<Entry> entries = cached.values();
			long now = System.currentTimeMillis();
			for( Entry entry : entries) {
				if( now - entry.timestamp > expiration) {
					cached.remove(entry.mobPhone);
					log.debug("==>> Remove expiration entry {} from cache.", entry);
				}
			}
		}
	}
	
	private ScheduledExecutorService excutor;
	
	public SmsCodeValidator() {
		excutor = Executors.newSingleThreadScheduledExecutor();
		excutor.scheduleAtFixedRate(new WatchDog(), 1, 1, TimeUnit.SECONDS);
	}
	
	/**
	 * 生成新的校验码.
	 * 
	 * @param mobPhone 手机号码.
	 * @return 校验码.
	 */
	public String generate(String mobPhone) {
		String code = Randoms.fixLength(6);
		cached.put(mobPhone, new Entry(mobPhone, code));
		
		return code;
	}
	
	
	/**
	 * 校验验证码是否有效.
	 * @param mobPhone 手机号码.
	 * @param code 验证码.
	 * @return 验证码有效,则返回true, 否则返回false.
	 */
	public boolean check(String mobPhone, String code) {
		Entry entry = cached.get(mobPhone);
		
		return entry != null 
				&& entry.code.equals(code)
				&& (System.currentTimeMillis() - entry.timestamp) < expiration ;
	}

}

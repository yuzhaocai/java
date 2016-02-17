package com.lczy.media.controller.common;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.lczy.media.service.common.BlacklistProvider;
import com.lczy.media.util.Constants;
import com.lczy.media.util.SmsCodeValidator;
import com.lczy.media.util.SmsSender;

/**
 * 发送短信.
 * 
 * @author wu
 *
 */
@Controller
@RequestMapping("/common")
public class SendSMSController {
	
	private static final Logger log = LoggerFactory.getLogger(SendSMSController.class);
	
	@Autowired
	private SmsCodeValidator smsCodeValidator;
	
	private static final String template = "您在采媒在线请求的验证码是：%s ";
	
	/**
	 * 发送短信验证码.
	 */
	@RequestMapping(value = "sendSmsCode")
	@ResponseBody
	public boolean sendSmsCode(String mobPhone, HttpServletRequest request) {
		boolean result = true;
		if( allow(mobPhone, request) ) {
			String code = smsCodeValidator.generate(mobPhone);
			try {
				SmsSender.send(mobPhone, String.format(template, code));
			} catch (Exception e) {
				log.error("无法发送短信验证码", e);
				result = false;
			}
		} else {
			String host = "", ip = "", user = "";
			if ( request != null ) {
				host = request.getRemoteHost();
				ip = request.getRemoteAddr();
				user = request.getRemoteUser();
			}
			log.debug("===>> 系统阻止向 {} 发送短信, remoteHost = {}, remoteAddr = {}, remoteUser = {}", mobPhone, host, ip, user);
		}
		return result;
	}

	private boolean allow(String mobPhone, HttpServletRequest request) {
		if( !isMobPhone(mobPhone) || isBlocked(mobPhone) ) {
			return false;
		}
		
		return true;
	}

	@Autowired
	private BlacklistProvider blacklistProvider;
	
	static class Counter {
		private AtomicInteger count;
		private long timestamp;
		public Counter(String num) {
			this.count = new AtomicInteger(1);
			this.timestamp = System.currentTimeMillis();
		}
	}
	
	/**
	 * 缓存发送过短信的号码.
	 */
	private static final Map<String, Counter> counterMap = Maps.newConcurrentMap();
	
	private static final long PERIOD = 24 * 60 * 60 * 1000;
	
	private static final int LIMIT = 10;
	
	/**
	 * 检查号码是否在黑名单中.
	 */
	private boolean isBlocked(String mobPhone) {
		boolean blocked = blacklistProvider.isBlocked(mobPhone, Constants.BlacklistType.PHONE);
		if ( !blocked ) {
			Counter c = counterMap.get(mobPhone);
			if( c != null ) {
				if ( c.count.get() >= LIMIT ) {
					if ( System.currentTimeMillis() - c.timestamp < PERIOD ) {
						blocked = true;
						log.warn("===>> 号码 {} 请求验证码的次数超过 {} 次", mobPhone, LIMIT);
					} else {
						c.count.set(1);
						c.timestamp = System.currentTimeMillis();
					}
				} else {
					c.count.incrementAndGet();
				}
			} else {
				counterMap.put(mobPhone, new Counter(mobPhone));
			}
		}
		return blocked;
	}

	private boolean isMobPhone(String mobPhone) {
		if( mobPhone == null)
			return false;
		
		String p = "1[3-9]{1}[0-9]{9}";
		return mobPhone.matches(p);
	}

}

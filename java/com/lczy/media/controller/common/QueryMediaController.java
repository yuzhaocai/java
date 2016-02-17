/**
 * 
 */
package com.lczy.media.controller.common;

import javax.servlet.ServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lczy.media.service.CustomerService;
import com.lczy.media.service.UserService;
import com.lczy.media.util.SmsCodeValidator;

/**
 * 公用的用于检查名称重复的 controller.
 * @author wu
 *
 */
@Controller
@RequestMapping("/common")
public class QueryMediaController {
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SmsCodeValidator smsCodeValidator;
	
	
	
	/**
	 * 检查手机码是否可注册.
	 */
	@RequestMapping(value = "queryMedia")
	@ResponseBody
	public Object queryMedia(ServletRequest request) {
		
		
		
		return "";
	}
	
}

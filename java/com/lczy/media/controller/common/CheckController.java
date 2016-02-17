/**
 * 
 */
package com.lczy.media.controller.common;

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
public class CheckController {
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SmsCodeValidator smsCodeValidator;
	
	/**
	 * 检查客户名称是否可用.
	 */
	@RequestMapping(value = "checkCustomerName")
	@ResponseBody
	public boolean checkName(String oldName, String name) {
		if (name.equals(oldName)) {
			return true;
		} else {
			return customerService.countBy("name", name) == 0;
		}
	}
	
	/**
	 * 检查登录名是否可用.
	 */
	@RequestMapping(value = "checkLoginName")
	@ResponseBody
	public boolean checkLoginName(String oldName, String loginName) {
		if (loginName.equals(oldName)) {
			return true;
		} else {
			return userService.getByLoginName(loginName) == null;
		}
	}
	
	/**
	 * 检查手机码是否可注册.
	 */
	@RequestMapping(value = "checkMobPhone")
	@ResponseBody
	public boolean checkMobPhone(String mobPhone) {
		if(StringUtils.isBlank(mobPhone) || mobPhone.length() < 11)
			return false;
		
		return customerService.countBy("mobPhone", mobPhone) == 0;
	}
	
	/**
	 * 检查手机码是否已经注册.
	 */
	@RequestMapping(value = "checkMobPhoneRegist")
	@ResponseBody
	public boolean checkMobPhoneRegist(String mobPhone) {
		if(StringUtils.isBlank(mobPhone) || mobPhone.length() < 11)
			return false;
		
		return customerService.countBy("mobPhone", mobPhone) == 1;
	}
	
	/**
	 * 查询短信验证码是否正确.
	 */
	@RequestMapping(value = "checkSmscode")
	@ResponseBody
	public boolean checkSmscode(String mobPhone, String smscode) {
		if( StringUtils.isBlank(smscode) || smscode.length() < 6
				|| StringUtils.isBlank(mobPhone) 
				|| mobPhone.length() < 11 )
			return false;
		
		return smsCodeValidator.check(mobPhone, smscode);
	}
	
}

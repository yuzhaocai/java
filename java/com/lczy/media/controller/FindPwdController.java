/**
 * 
 */
package com.lczy.media.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lczy.common.web.Token;
import com.lczy.common.web.TokenInterceptor;
import com.lczy.media.entity.User;
import com.lczy.media.service.UserService;
import com.lczy.media.util.SmsCodeValidator;
import com.lczy.media.vo.CustomerVO;

/**
 * 忘记密码 Controller
 * @author wang.xiaoxiang
 *
 */
@Controller
@RequestMapping("/findPwd")
public class FindPwdController extends MediaController{
	
	private static final Logger log = LoggerFactory.getLogger(FindPwdController.class);
	@Autowired
	private UserService userService;
	
	@Autowired
	private SmsCodeValidator smsCodeValidator;
	
	@RequestMapping(value={"", "/"}, method = RequestMethod.GET)
	@Token
	public String findPwd(Model model, HttpServletRequest request) {
		setModelAttribute(model);
		return "findPwd";
	}
	
	@RequestMapping(value="", method=RequestMethod.POST)
	public String doFindPwd(@Valid @ModelAttribute("vo") CustomerVO vo, BindingResult result, 
			HttpServletRequest request,RedirectAttributes redirectAttrs) {
		try {
			String mobPhone = vo.getMobPhone();
			String smscode = vo.getSmscode();
			String password = vo.getPassword();
			if(!checkSmscode(mobPhone, smscode) ) {
				log.error("找回密码失败,手机验证码错误");
				result.addError(new FieldError("vo", "smscode", "短信验证码不正确!"));
			}
			User user = userService.getByLoginName(mobPhone);
			if (user == null) {
				log.error("找回密码失败,该手机不存在！");
				result.addError(new FieldError("vo", "mobPhone", "手机号码未注册!"));
			}
			if( result.hasErrors() ) {
				TokenInterceptor.newToken(request);
				return "findPwd";
			}
			userService.changePassword(user.getId(), password);
			return "redirect:/findPwd/success";
		} catch (Exception e) {
			log.error("找回密码失败", e);
			result.addError(new FieldError("vo", "mobPhone", "找回密码失败！"));
			return "findPwd";
		}
	}
	
	@RequestMapping("success")
	public String success(Model model, HttpServletRequest request) {
		return "findPwdSuccess";
	}
	
	@RequestMapping("failure")
	public String failure(Model model, HttpServletRequest request) {
		return "findPwdFailure";
	}
	
	private boolean checkSmscode(String mobPhone, String smsCode) {
		return smsCodeValidator.check(mobPhone, smsCode);
	}
}

/**
 * 
 */
package com.lczy.media.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
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
import com.lczy.common.web.WebHelper;
import com.lczy.common.web.Token.Type;
import com.lczy.common.web.TokenInterceptor;
import com.lczy.media.service.CustomerService;
import com.lczy.media.service.UserService;
import com.lczy.media.service.common.DicProvider;
import com.lczy.media.util.SmsCodeValidator;
import com.lczy.media.util.UserContext;
import com.lczy.media.vo.CustomerVO;

/**
 * 客户注册 Controller
 * @author wu
 *
 */
@Controller
@RequestMapping("/register")
public class RegisterController {
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private DicProvider dicProvider;
	
	@RequestMapping(value={"", "/"}, method = RequestMethod.GET)
	@Token
	public String register(Model model) {
		
		model.addAttribute("cust", new CustomerVO());
		
		return "register";
	}
	
	@RequestMapping(value="", method=RequestMethod.POST)
	@Token(Type.REMOVE)
	public String doRegister(@Valid @ModelAttribute("vo") CustomerVO vo, BindingResult result, HttpServletRequest request, RedirectAttributes redirectAttrs) {
		
		if( existsMobPhone(vo) ) {
			result.addError(new FieldError("vo", "mobPhone", "手机号码已经被注册!"));
		}

		if( !checkSmscode(vo) ) {
			result.addError(new FieldError("vo", "smscode", "短信验证码不正确!"));
		}
		
		if( result.hasErrors() ) {
			TokenInterceptor.newToken(request);
			return "register";
		}
		
		vo.setLoginName(vo.getMobPhone());
		customerService.register(vo);
		
		//保存后自动登录
		Subject subject = SecurityUtils.getSubject();
		subject.login(new UsernamePasswordToken(vo.getLoginName(), vo.getPassword(), false));
		
		String ip = WebHelper.getRealIP(request);
		String userId = UserContext.getCurrent().getId();
		userService.updateUserLoginInfo(userId, ip);
		
		return "redirect:/register/success";
	}
	
	@Autowired
	private SmsCodeValidator smsCodeValidator;
	
	private boolean checkSmscode(CustomerVO vo) {
		
		return smsCodeValidator.check(vo.getMobPhone(), vo.getSmscode());
	}

	private boolean existsMobPhone(CustomerVO cust) {
		
		return customerService.countBy("mobPhone", cust.getMobPhone()) > 0;
	}

	@RequestMapping("success")
	public String success(Model model, HttpServletRequest request) {
		
		return "registerSuccess";
	}
	
}

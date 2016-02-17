package com.lczy.media.controller;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lczy.common.util.MessageBean;
import com.lczy.common.web.WebHelper;
import com.lczy.media.entity.OpLogging;
import com.lczy.media.security.IncorrectCaptchaException;
import com.lczy.media.service.OpLoggingService;
import com.lczy.media.service.UserService;
import com.lczy.media.util.Constants.ActionType;
import com.lczy.media.util.UserContext;

@Controller
@RequestMapping("/")
public class LoginModalController {
	
	private static final Logger log = LoggerFactory.getLogger(LoginModalController.class);
	
	@Autowired
    private OpLoggingService loggingService;
	
	@Autowired
    private UserService userService;

	@RequestMapping("loginModal")
	public String loginModal(Model model, HttpServletRequest request) {
		model.addAttribute("savedURL", request.getParameter("savedURL"));
		return "loginModal";
	}
/*	
	@RequestMapping(value = "doLogin", method = RequestMethod.POST)
	@ResponseBody
	public MessageBean doLogin(Model model, HttpServletRequest request) {
		MessageBean bean = new MessageBean(1, "登录成功！");
		AuthenticationToken token = null;
        try {
        	//图形验证码校验
            doCaptchaValidate(request);
            
            token = (UsernamePasswordToken)createToken(request);  
            Subject subject = SecurityUtils.getSubject();
            subject.login(token); 
            
            onLoginSuccess(request);
            
        } catch (DisabledAccountException e) {
        	bean = new MessageBean(0, "此账号已经被禁用！");
        } catch (UnknownAccountException | IncorrectCredentialsException e) {
        	bean = new MessageBean(0, "无效的账号或者密码！");
        } catch (IncorrectCaptchaException e) {
            bean = new MessageBean(0, e.getMessage());
        } catch (Exception e) {
            log.warn("===>> 登录失败：" + request.getRemoteAddr() + ", token: " + token, e);
            bean = new MessageBean(0, "登录异常！");
        }
		return bean;
	}

	private void doCaptchaValidate(HttpServletRequest request) {
    	//session中的图形码字符串  
        String captcha = (String) request.getSession().getAttribute(  
                com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);  
        //比对  
        if (captcha != null && !captcha.equalsIgnoreCase(getCaptcha(request))) {  
            throw new IncorrectCaptchaException("验证码错误！");  
        }
	}

	private String getCaptcha(HttpServletRequest request) {
		return WebHelper.getString(request, "captcha");
	}

	private AuthenticationToken createToken(HttpServletRequest request) {
		String loginName = WebHelper.getString(request, "username");
		String password = WebHelper.getString(request, "password");
		
		AuthenticationToken authToken = new UsernamePasswordToken(loginName, password);
		return authToken;
	}
	
	private void onLoginSuccess(ServletRequest request) {
		String loginName = SecurityUtils.getSubject().getPrincipal().toString();
		saveLoginLog(request, loginName + " - 登录成功");
        updateUserLoginInfo(request);
	}
	
	private void saveLoginLog(ServletRequest request, String description) {
		OpLogging opLog = OpLoggingService.newOpLogging(request);
		opLog.setActionType(ActionType.LOGIN);
		opLog.setDescription(description);
		loggingService.save(opLog);
	}
	
	private void updateUserLoginInfo(ServletRequest request) {
		String userId = UserContext.getCurrent().getId();
		String ip = request.getRemoteAddr();
		userService.updateUserLoginInfo(userId, ip);
	}*/

}

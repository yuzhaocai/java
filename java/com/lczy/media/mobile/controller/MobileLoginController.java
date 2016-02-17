/**
 * 
 */
package com.lczy.media.mobile.controller;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.lczy.common.util.MyGson;
import com.lczy.common.web.WebHelper;
import com.lczy.media.controller.common.SendSMSController;
import com.lczy.media.entity.Customer;
import com.lczy.media.entity.OpLogging;
import com.lczy.media.entity.User;
import com.lczy.media.mobile.util.Request;
import com.lczy.media.mobile.util.Response;
import com.lczy.media.mobile.util.Token;
import com.lczy.media.mobile.util.TokenManager;
import com.lczy.media.security.ShiroDbRealm;
import com.lczy.media.service.OpLoggingService;
import com.lczy.media.service.UserService;
import com.lczy.media.util.Constants;
import com.lczy.media.util.FileServerUtils;
import com.lczy.media.util.JsonBean;
import com.lczy.media.util.SmsCodeValidator;
import com.lczy.media.util.UserContext;
import com.lczy.media.util.Constants.ActionType;

/**
 * 移动端登录.
 * 
 * @author wu
 *
 */
@Controller
@RequestMapping("/mobile/mcLogin")
public class MobileLoginController {
	
	private Logger log = LoggerFactory.getLogger(MobileLoginController.class);
	
	private Gson gson = MyGson.getInstance();
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TokenManager tokenManager;
	
	@Autowired
	private SendSMSController sendSMSController;
	
	@Autowired
	private SmsCodeValidator smsCodeValidator;
	
	@Autowired
    private OpLoggingService loggingService;

	/**
	 * 登录接口.
	 */
	@RequestMapping("Login")
	@ResponseBody
	public Response login(HttpServletRequest request) {
		Request req = Request.parseRequest(request);
		log.debug("==>> request body: {}", req.getBodyContent());
		
		String loginName = req.getBodyAsString("login_name");
		String password = req.getBodyAsString("password");
		
		AuthenticationToken authToken = new UsernamePasswordToken(loginName, password);
		Response resp = null;
		try {
			ThreadContext.unbindSubject();
			Subject subject = SecurityUtils.getSubject();
			subject.login(authToken);
			
			onLoginSuccess(request);
			
			String sessionId = subject.getSession().getId().toString();
			
			User user = UserContext.getCurrent();
			Customer cust = user.getCustomer();
			
			Token token = new Token(loginName, sessionId);
			token.setUserId(user.getId());
			token.setCustId(cust.getId());
			token.setCustType(cust.getCustType());
			tokenManager.save(token);
			
			resp = new Response();
			resp.addHead("token", sessionId);
			resp.addBody("t_customer", toJson(cust));
			
		} catch (DisabledAccountException e) {
			resp = new Response(900, "账号被禁用");
		} catch (UnknownAccountException | IncorrectCredentialsException e) {
			log.debug("登录失败", e);
			resp = new Response(900, "无效的账号或者密码");
		} catch (Exception e) {
			log.debug("登录异常", e);
			resp = new Response(900, "登录异常：" + e.getMessage());
		}
		
		log.debug("==>> response: {}", gson.toJson(resp));
		return resp;
	}
	
	/**
	 * 发送短信验证码.
	 */
	@RequestMapping(value = "sendVerCode")
	@ResponseBody
	public Response sendVerCode(HttpServletRequest request) {

		Response resp = null;
		try {
			Request req = Request.parseRequest(request);
			log.debug("request: " + req.getBodyContent());
			String mobPhone = req.getBodyAsString("mob_phone");
			boolean result = sendSMSController.sendSmsCode(mobPhone, null);
			if (result) {
				resp = new Response(0, "");
			} else {
				resp = new Response(900, "发送验证码失败!");
			}
		} catch (Exception e) {
			resp = new Response(900, "发送验证码失败!");
		}
		
		log.debug("response: " + gson.toJson(resp));
		return resp;
	}
	
	
	/**
	 * 随机码登录接口.
	 */
	@RequestMapping("confirmVerCode")
	@ResponseBody
	public Response confirmVerCode(HttpServletRequest request) {
		
		Request req = Request.parseRequest(request);
		log.debug("request: " + req.getBodyContent());
		String loginName = req.getBodyAsString("mob_phone");
		String verCode = req.getBodyAsString("vercode");
		
		Response resp = null;
		try {
			User user = userService.getByLoginName(loginName);
			if (user == null) {
				throw new UnknownAccountException("用户未找到!");
			}
			if (!user.getStatus().equals(Constants.UserStatus.ENABLED)) {
				throw new DisabledAccountException("用户被禁用!");
			}
			if (!smsCodeValidator.check(loginName, verCode)) {
				throw new IncorrectCredentialsException("验证码不正确!");
			}
			
			Subject subject = setLoginStatus(user);
			
			onLoginSuccess(request);
			
			String sessionId = subject.getSession().getId().toString();
			Customer cust = user.getCustomer();
			Token token = new Token(loginName, sessionId);
			token.setUserId(user.getId());
			token.setCustId(cust.getId());
			token.setCustType(cust.getCustType());
			tokenManager.save(token);
			
			resp = new Response();
			resp.addHead("token", sessionId);
			resp.addBody("t_customer", toJson(cust));
		} catch (Exception e) {
			log.debug("登录异常", e);
			resp = new Response(900, e.getMessage());
		}
		
		//log.debug("response: " + gson.toJson(resp));
		return resp;
	}

	/**
	 * 设置用户为登录成功状态.
	 * 
	 * @param user
	 */
	private Subject setLoginStatus(User user) {
		ThreadContext.unbindSubject();
		Subject subject = new Subject.Builder()
			.authenticated(true)
			.principals(new SimplePrincipalCollection(new ShiroDbRealm.ShiroUser(user), ShiroDbRealm.class.getName())).buildSubject();
		ThreadContext.bind(subject);
		return subject;
	}
		
	private JsonBean toJson(Customer cust) {
		JsonBean bean = new JsonBean();
		bean.add("id", cust.getId());
		bean.add("name", cust.getName());
		bean.add("cust_type", cust.getCustType());
		bean.add("mob_phone", cust.getMobPhone());
		bean.add("tel_phone", cust.getTelPhone());
//		bean.add("cust_level", cust.getCustLevel().getName());
		bean.add("credit", cust.getCredit());
		bean.add("linkman", cust.getLinkman());
		bean.add("email", cust.getEmail());
		bean.add("qq", cust.getQq());
		bean.add("status", cust.getStatus());
		bean.add("certified", cust.isCertified());
		bean.add("cert_status", cust.getCertStatus());
		bean.add("cert_name", cust.getCertName());
		bean.add("cert_identity", cust.getCertIdentity());
		bean.add("cert_matter", FileServerUtils.getFileUrl(cust.getCertMatter()));
		if( cust.getCertSubmitTime() != null )
			bean.add("cert_submit_time", new DateTime(cust.getCertSubmitTime()).toString("yyyy-MM-dd"));
		
		return bean;
	}

	/**
	 * 退出接口.
	 */
	@RequestMapping("Logout")
	@ResponseBody
	public Response logout(HttpServletRequest request) {
		Request req = Request.parseRequest(request);
		log.debug("==>> request body: {}", req.getBodyContent());
		
		String token = req.getToken();
		tokenManager.invalidate(token);
		
		return new Response();
	}
	

	/**
	 * 修改密码接口.
	 */
	@RequestMapping("setPwd")
	@ResponseBody
	public Response setPwd(HttpServletRequest httpRequest) {
		
		Request req = Request.parseRequest(httpRequest);
		log.debug("request: " + req.getBodyContent());
		Token token = tokenManager.getToken(req.getToken());
		String uid = token.getUserId();
		String oldpwd = req.getBodyAsString("oldpwd");
		String newpwd = req.getBodyAsString("newpwd");
		
		Response resp = null;
		try {
			userService.changePassword(uid, oldpwd, newpwd);
			resp = new Response();
		} catch (Exception e) {
			log.debug("修改密码异常", e);
			resp = new Response(900, e.getMessage());
		}
		
		log.debug("response: " + gson.toJson(resp));
		return resp;
	}
	
	/**
	 * 重置密码接口.
	 */
	@RequestMapping("resetPwd")
	@ResponseBody
	public Response resetPwd(HttpServletRequest httpRequest) {
		
		Request req = Request.parseRequest(httpRequest);
		log.debug("request: " + req.getBodyContent());
		Token token = tokenManager.getToken(req.getToken());
		String uid = token.getUserId();
		String newpwd = req.getBodyAsString("newpwd");
		
		Response resp = null;
		try {
			userService.changePassword(uid, newpwd);
			resp = new Response();
		} catch (Exception e) {
			log.debug("重置密码异常", e);
			resp = new Response(900, e.getMessage());
		}
		
		log.debug("response: " + gson.toJson(resp));
		return resp;
	}
	
	/**
	 * 更改手机接口.
	 */
	@RequestMapping("confirmPhoneVerCode")
	@ResponseBody
	public Response confirmPhoneVerCode(HttpServletRequest httpRequest) {
		
		Request req = Request.parseRequest(httpRequest);
		log.debug("request: " + req.getBodyContent());
		Token token = tokenManager.getToken(req.getToken());
		String uid = token.getUserId();
		String mobPhone = req.getBodyAsString("mob_phone");
		String verCode = req.getBodyAsString("vercode");
		
		Response resp = null;
		try {
			if (!smsCodeValidator.check(mobPhone, verCode)) {
				throw new IncorrectCredentialsException("验证码不正确!");
			}
			
			userService.changeMobPhone(uid, mobPhone);
			resp = new Response();
		} catch (Exception e) {
			log.debug("修改手机失败", e);
			resp = new Response(900, e.getMessage());
		}
		
		log.debug("response: " + gson.toJson(resp));
		return resp;
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
		String ip = WebHelper.getRealIP((HttpServletRequest)request);;
		userService.updateUserLoginInfo(userId, ip);
	}

}

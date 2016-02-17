package com.lczy.media.security.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lczy.common.util.Globals;
import com.lczy.common.util.PropertyUtils;
import com.lczy.common.web.WebHelper;
import com.lczy.media.entity.OpLogging;
import com.lczy.media.entity.User;
import com.lczy.media.security.IncorrectCaptchaException;
import com.lczy.media.service.OpLoggingService;
import com.lczy.media.service.UserService;
import com.lczy.media.util.Constants.ActionType;
import com.lczy.media.util.UserContext;

@Component
public class CaptchaFormLoginFilter extends FormAuthenticationFilter {  
    private static final Logger log = LoggerFactory.getLogger(CaptchaFormLoginFilter.class); 
    
    @Autowired
    private OpLoggingService loggingService;
    
    @Autowired
    private UserService userService;
    
    public CaptchaFormLoginFilter() {
    }
    
    /** 
     * 登录验证 
     */  
    @Override  
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {  
    	UsernamePasswordToken token = (UsernamePasswordToken)createToken(request, response);  
        try {
        	//图形验证码校验
            doCaptchaValidate(request);
            
            Subject subject = getSubject(request, response);
            
            subject.login(token);//正常验证 
            log.info("{}-登录成功", token.getUsername());
            
            return onLoginSuccess(token, subject, request, response); 
            
        } catch (AuthenticationException e) {
            log.info("登录失败", e);
            return onLoginFailure(token, e, request, response);  
        }
    }
    
	public boolean onLoginSuccess(AuthenticationToken token,
			Subject subject, ServletRequest request, ServletResponse response)
			throws Exception {
		String loginName = SecurityUtils.getSubject().getPrincipal().toString();
		saveLoginLog(request, loginName + " - 登录成功");
        updateUserLoginInfo(request);
        
        String url = getSavedURL(request);
        if (url != null) {
        	WebUtils.issueRedirect(request, response, url);
        	return false;
        } else {
        	SavedRequest savedRequest = WebUtils.getSavedRequest(request);
        	if (savedRequest != null) {
        		return super.onLoginSuccess(token, subject, request, response);
        	} else {
        		url = getDefaultUrl();
        		WebUtils.issueRedirect(request, response, url);
        		return false;
        	}
        }
	}

	
	private String getDefaultUrl() {
		
		return PropertyUtils.getProperty("home." + UserContext.getUserRole(), "/");
	}
	

	private String getSavedURL(ServletRequest request) {
		HttpServletRequest hreq = (HttpServletRequest) request;
		String url = hreq.getParameter(Globals.SAVED_URL_KEY);
		
		return StringUtils.isBlank(url) ? null : url;
	}

	private void updateUserLoginInfo(ServletRequest request) {
		String userId = getCurrentUser().getId();
		String ip = WebHelper.getRealIP((HttpServletRequest)request);
		userService.updateUserLoginInfo(userId, ip);
	}

	private User getCurrentUser() {
		return UserContext.getCurrent();
	}

	private void saveLoginLog(ServletRequest request, String description) throws Exception {
		OpLogging opLog = OpLoggingService.newOpLogging(request);
		opLog.setActionType(ActionType.LOGIN);
		opLog.setDescription(description);
		loggingService.save(opLog);
	}
  
    // 验证码校验  
    protected void doCaptchaValidate(ServletRequest request) {
    	HttpServletRequest httpRequest = (HttpServletRequest)request;
    	//session中的图形码字符串  
        String captcha = (String) httpRequest.getSession().getAttribute(  
                com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);  
        //比对  
        if (captcha != null && !captcha.equalsIgnoreCase(getCaptcha(request))) {  
            throw new IncorrectCaptchaException("验证码错误！");  
        }  
    } 
  
    public static final String DEFAULT_CAPTCHA_PARAM = "captcha"; 
  
    private String captchaParam = DEFAULT_CAPTCHA_PARAM;  
  
    public String getCaptchaParam() {  
        return captchaParam;  
    }
  
    public void setCaptchaParam(String captchaParam) {  
        this.captchaParam = captchaParam;  
    }
  
    protected String getCaptcha(ServletRequest request) {  
        return WebUtils.getCleanParam(request, getCaptchaParam());  
    }
      
    //保存异常对象到request  
    @Override  
    protected void setFailureAttribute(ServletRequest request,  
            AuthenticationException ae) {  
        request.setAttribute(getFailureKeyAttribute(), ae);  
    }
    
    
}

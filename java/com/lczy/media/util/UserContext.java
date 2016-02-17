package com.lczy.media.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lczy.common.util.Encrypts;
import com.lczy.common.util.SpringUtils;
import com.lczy.media.entity.Customer;
import com.lczy.media.entity.SysConfig;
import com.lczy.media.entity.User;
import com.lczy.media.security.ShiroDbRealm.ShiroUser;
import com.lczy.media.service.SysConfigService;
import com.lczy.media.util.Constants.RoleCode;

public abstract class UserContext {
	
	private static final Logger log = LoggerFactory.getLogger(UserContext.class);

	/**
	 * @return 当前登录用户对象.
	 */
	public static User getCurrent() {
		try {
			if (SecurityUtils.getSubject().isAuthenticated()) {
				return ((ShiroUser)SecurityUtils.getSubject().getPrincipal()).getUser();
			} 
		} catch (Exception e) {
			log.warn("获取当前用户发生异常", e);
		}
		
		return null;
	}
	
	/**
	 * @return 当前登录会员对象.
	 */
	public static Customer getCurrentCustomer() {
		if( getCurrent() != null ) {
			return getCurrent().getCustomer();
		}
		
		return null;
	}
	
	

	/**
	 * @return 当前登录用户是否是“系统用户”
	 */
	public static boolean isSysUser() {
		
		return getCurrent() != null && getCurrent().getCustomer().getId().equals("0");
	}
	
	
	/**
	 * 用于定时任务上下文中获取系统用户.
	 * 
	 * @return 系统用户.
	 */
	public static User getSystemUser() {
		bindSecurityManager();
		if (getCurrent() == null || !"root".equals(getCurrent().getLoginName())) {
			return loginAsSystemUser();
		} else {
			return getCurrent();
		}
	}

	private static void bindSecurityManager() {
		if (ThreadContext.getSecurityManager() == null) {
			log.debug("Binding SecurityManager...");
			ThreadContext.bind(SpringUtils.getBean(SecurityManager.class));
		}
	}
	
	private static User loginAsSystemUser() {
		bindSecurityManager();
		ThreadContext.unbindSubject();
		
		Subject subject = SecurityUtils.getSubject();
		
		String password = getSystemPassword();
		if (password == null) password = "superadmin";
		
		log.debug("Subject loging as root...");
		subject.login(new UsernamePasswordToken("root", password));
		
		return getCurrent();
	}
	
	
	public static final String SYSTEM_PASSWORD_KEY = "system.password";
	
	// ENCODE_KEY = AESCodec.initKey();
	public static final String ENCODE_KEY = "f5cJRPkyNeEdkCFI7Kx8XA==";
	
	private static String getSystemPassword() {
		SysConfigService configService = SpringUtils.getBean(SysConfigService.class);
		SysConfig config = configService.get(SYSTEM_PASSWORD_KEY);
		
		String password = config == null ? null : config.getValue();
		if (password != null) {
			try {
				return Encrypts.decryptAES(password, ENCODE_KEY);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		return null;
	}
	
	/**
	 * @return 当前登录用户的角色代码.
	 */
	public static String getUserRole() {
		
		return UserContext.getCurrent().getRole().getRoleCode();
	}
	
	/**
	 * 当前登录用户是否为“媒体主”.
	 * @return 当前用户为“媒体主”则返回 true，否则返回 false。
	 */
	public static boolean isProvider() {
		return RoleCode.PROVIDER.equals(getUserRole());
	}
	
	/**
	 * 当前登录用户是否为“广告主”.
	 * @return 当前用户为“广告主”则返回 true，否则返回 false。
	 */
	public static boolean isAdvertiser() {
		return RoleCode.ADVERTISER.equals(getUserRole());
	}
}

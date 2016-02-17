/**
 * 
 */
package com.lczy.media.security.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.lczy.common.util.PropertyUtils;
import com.lczy.media.util.UserContext;

/**
 * 根据角色重定向到默认页面.
 * 
 * @author wu
 *
 */
@Component
public class RedirectFilter extends PathMatchingFilter {

	private static final Logger log = LoggerFactory.getLogger(RedirectFilter.class);
	
	@Override
	protected boolean onPreHandle(ServletRequest request,
			ServletResponse response, Object mappedValue) throws Exception {
		String uri = ((HttpServletRequest)request).getRequestURI();
		log.debug("=====>> 访问：{}, 配置：{}", uri, mappedValue);
		
		if (SecurityUtils.getSubject().isAuthenticated() && isNeedRedirect(mappedValue)) {
			String redirecUrl = getRedirectUrl();
			log.debug("=====>> 当前用户角色为 {}，重定向到：{}", UserContext.getUserRole(), redirecUrl);
			WebUtils.issueRedirect(request, response, redirecUrl);
			
			return false;
		}
		
		return true;
	}

	private String getRedirectUrl() {
		String role = UserContext.getUserRole();
		String url = PropertyUtils.getProperty("home." + role, "/");
		return url;
	}


	private boolean isNeedRedirect(Object mappedValue) {
		String[] roles = (String[])mappedValue;
		for(String role : roles) {
			if (role.equals(UserContext.getUserRole())) {
				return true;
			}
		}
		
		return false;
	}
	
	

}

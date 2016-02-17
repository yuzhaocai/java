/**
 * 
 */
package com.lczy.media.mobile.security;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.filter.AccessControlFilter;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.lczy.common.util.MyGson;
import com.lczy.common.web.WebHelper;
import com.lczy.media.mobile.util.Request;
import com.lczy.media.mobile.util.Response;

/**
 * 客户端安全访问控制器.
 * 
 * @author wu
 *
 */
public class MobileSecurityFilter extends AccessControlFilter {

	private Gson gson = MyGson.getInstance();
	
	private List<String> bypassPaths;
	
	/* (non-Javadoc)
	 * @see org.apache.shiro.web.filter.AccessControlFilter#isAccessAllowed(javax.servlet.ServletRequest, javax.servlet.ServletResponse, java.lang.Object)
	 */
	@Override
	protected boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object mappedValue) throws Exception {
		
		if ( isBypass(request) ) {
			return true;
		}
		
		String sessionId = Request.getToken(request);
		if( StringUtils.isBlank(sessionId) ) {
			return false;
		} else {
			Subject subject = new Subject.Builder().sessionId(sessionId).buildSubject();
			boolean allowed = subject.isAuthenticated();
			if( allowed) {
				ThreadContext.bind(subject);
				subject.getSession().touch();
			}
			return allowed;
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.shiro.web.filter.AccessControlFilter#onAccessDenied(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws Exception {
		String sessionId = Request.getToken(request);
		Response resp = new Response(908, "非法的令牌！token = " + sessionId);
		
		WebHelper.responseJSON(response, gson.toJson(resp));
		
		return false;
	}
	
	@PostConstruct
	public void sterilize() {
		List<String> newList = Lists.newArrayList();
		for( String path : bypassPaths ) {
			newList.add(path.trim());
		}
		bypassPaths = newList;
	}

	private boolean isBypass(ServletRequest request) {
		String uri = ((HttpServletRequest)request).getServletPath();

		return bypassPaths.contains(uri);
	}

	public List<String> getBypassPaths() {
		return bypassPaths;
	}

	public void setBypassPaths(List<String> bypassPaths) {
		this.bypassPaths = bypassPaths;
	}
	
}

/**
 * 
 */
package com.lczy.media.mobile.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.lczy.common.web.WebHelper;

/**
 * 移动端请求.
 * @author wu
 *
 */
public class Request {
	
	public static final String TOKEN_KEY = "token";
	
	private static Gson gson = new Gson();

	private RequestBody requestBody;
	
	private HttpServletRequest request;
	
	private String bodyContent;
	
	private String token;
	
	private Request() {}
	
	private Request(HttpServletRequest request) {
		this.request = request;
		bodyContent = WebHelper.getBodyContent(request);
		
		this.requestBody = gson.fromJson(bodyContent, RequestBody.class);
	}
	
	private Request(String bodyContent) {
		this.bodyContent = bodyContent;
		this.requestBody = gson.fromJson(bodyContent, RequestBody.class);
	}
	
	public String getBodyContent() {
		return bodyContent;
	}

	public void setBodyContent(String bodyContent) {
		this.bodyContent = bodyContent;
	}

	public Map<String, Object> getHead() {
		return requestBody.getHead();
	}
	
	public Map<String, Object> getBody() {
		return requestBody.getBody();
	}
	
	public String getHeadAsString(String key) {
		Object value = getHead().get(key);
		return value == null ? null : value.toString();
	}
	
	public String getBodyAsString(String key) {
		Object value = getBody().get(key);
		return value == null ? null : value.toString();
	}
	
	public String getToken() {
		if( token != null )
			return token;
		
		if( request != null )
			token = request.getHeader(TOKEN_KEY);
		else
			token = (String)getHead().get(TOKEN_KEY);
		
		return token;
	}
	
	public static Request parseRequest(String bodyContent) {
		return new Request(bodyContent);
	}
	
	public static Request parseRequest(HttpServletRequest request) {
		return new Request(request);
	}
	
	public static Request parseRequest(ServletRequest request) {
		return new Request((HttpServletRequest)request);
	}
	
	public static String getToken(HttpServletRequest request) {
		String token = request.getHeader(TOKEN_KEY);
		if( StringUtils.isBlank(token) ) {
			token = request.getParameter(TOKEN_KEY);
		}
		return token;
	}
	
	public static String getToken(ServletRequest request) {
		return getToken((HttpServletRequest)request);
	}
	
	public static class RequestBody {
		private Map<String, Object> head;
		
		private Map<String, Object> body;

		public RequestBody() {
			this.head = new HashMap<String, Object>();
			this.body = new HashMap<String, Object>();
		}

		public Map<String, Object> getHead() {
			return head;
		}

		public void setHead(Map<String, Object> head) {
			this.head = head;
		}

		public Map<String, Object> getBody() {
			return body;
		}

		public void setBody(Map<String, Object> body) {
			this.body = body;
		}
	}
}

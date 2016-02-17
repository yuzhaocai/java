/**
 * 
 */
package com.lczy.media.mobile.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 移动端响应类.
 * 
 * @author wu.
 *
 */
public class Response {
	
	private Map<String, Object> head;
	
	private Map<String, Object> body;
	
	public Response() {
		this(0, null);
	}
	
	public Response(int respCode, String respError) {
		head = new HashMap<String, Object>();
		body = new HashMap<String, Object>();
		
		head.put("resp_code", respCode);
		if( respError != null)
			head.put("resp_error", respError);
		
		head.put("timestamp", System.currentTimeMillis());
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
	
	public void addHead(String key, Object value) {
		if( value == null )
			return;
		
		head.put(key, value);
	}
	
	public void addBody(String key, Object value) {
		if( value == null )
			return;
		
		body.put(key, value);
	}

}

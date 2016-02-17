/**
 * 
 */
package com.lczy.media.util;

import java.util.HashMap;

import org.springside.modules.mapper.JsonMapper;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author wu
 *
 */
public class JsonBean extends HashMap<String, Object> {

	private static final long serialVersionUID = -6842060976459237509L;
	
	public JsonBean() { 
		super(); 
	}
	
	/**
	 * @param status 状态码
	 * @param message 消息内容
	 */
	public JsonBean(String status, String message) {
		super();
		this.put("status", status);
		this.put("message", message);
	}
	
	/**
	 * @param status 状态码
	 */
	public JsonBean(String status) {
		super();
		this.put("status", status);
	}
	
	@JsonIgnore
	public String toJson() {
		return JsonMapper.nonEmptyMapper().toJson(this);
	}
	
	public void add(String key, Object value) {
		if( value == null )
			return;
		
		this.put(key, value);
	}
}

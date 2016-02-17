/**
 * 
 */
package com.lczy.media.mobile.util;



/**
 * @author wu
 *
 */
public class Token {
	
	private String loginName;
	
	private String userId;
	
	private String custId;
	
	private String custType;
	
	private String sessionId;
	
	public Token() {}
	
	public Token(String sessionId) {
		this(null, sessionId);
	}

	public Token(String loginName, String sessionId) {
		this.loginName = loginName;
		this.sessionId = sessionId;
	}

	public String getSessionId() {
		return this.sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getCustType() {
		return custType;
	}

	public void setCustType(String custType) {
		this.custType = custType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}

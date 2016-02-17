/**
 * 
 */
package com.lczy.media.vo;

import org.springframework.web.multipart.MultipartFile;



/**
 * Customer 值对象.
 * @author wu
 *
 */
public class CustomerVO {
	
	private String id;
	
	private String name;
	
	/**
	 * 会员类型:广告主/媒体账户
	 */
	private String custType;
	
	/**
	 * 客户性质:个人/企业
	 */
	private String custProperty;
	
	private String telPhone;
	private String mobPhone;
	private String status;
	private String linkman;
	private String email;
	private String qq;
	
	// user
	private String loginName;
	private String password;
	
	private String orgId;
	private String orgType;
	private String orgSummary;
	
	/**
	 * 短信验证码.
	 */
	private String smscode;
	
	
	/**
	 * 实名认证
	 */
	private String certName;
	private String certIdentity;
	private MultipartFile certImg;;
	
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCustType() {
		return custType;
	}
	
	public void setCustType(String custType) {
		this.custType = custType;
	}
	
	public String getCustProperty() {
		return custProperty;
	}
	
	public void setCustProperty(String custProperty) {
		this.custProperty = custProperty;
	}

	public String getTelPhone() {
		return telPhone;
	}
	
	public void setTelPhone(String telPhone) {
		this.telPhone = telPhone;
	}
	
	public String getMobPhone() {
		return mobPhone;
	}
	
	public void setMobPhone(String mobPhone) {
		this.mobPhone = mobPhone;
	}

	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getLoginName() {
		return loginName;
	}
	
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getOrgId() {
		return orgId;
	}
	
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getSmscode() {
		return smscode;
	}

	public void setSmscode(String smscode) {
		this.smscode = smscode;
	}

	public String getCertName() {
		return certName;
	}

	public void setCertName(String certName) {
		this.certName = certName;
	}

	public String getCertIdentity() {
		return certIdentity;
	}

	public void setCertIdentity(String certIdentity) {
		this.certIdentity = certIdentity;
	}

	public MultipartFile getCertImg() {
		return certImg;
	}

	public void setCertImg(MultipartFile certImg) {
		this.certImg = certImg;
	}
	

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getOrgSummary() {
		return orgSummary;
	}

	public void setOrgSummary(String orgSummary) {
		this.orgSummary = orgSummary;
	}
	

	

	
	
	
}

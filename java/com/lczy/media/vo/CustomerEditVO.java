/**
 * 
 */
package com.lczy.media.vo;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

/**
 * Customer 值对象.
 * @author wu
 *
 */
public class CustomerEditVO {
	
	private String id;
	private String name;
	private String custType;
	private String custProperty;
	private String summary;
	private String adType;
	private String taobao;
	private String coName;
	private String coAddress;
	private String bankNo;
	private String bankName;
	private String bankAddress;
	private String linkman;
	private String telPhone;
	private String mobPhone;
	private String qq;
	private String email;
	private MultipartFile idImgFile;
	private String idImgUrl;
	private MultipartFile opImgFile;
	private String opImgUrl;
	private MultipartFile orImgFile;
	private String orImgUrl;
	
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
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getAdType() {
		return adType;
	}
	public void setAdType(String adType) {
		this.adType = adType;
	}
	public String getTaobao() {
		return taobao;
	}
	public void setTaobao(String taobao) {
		this.taobao = taobao;
	}
	public String getCoName() {
		return coName;
	}
	public void setCoName(String coName) {
		this.coName = coName;
	}
	public String getCoAddress() {
		return coAddress;
	}
	public void setCoAddress(String coAddress) {
		this.coAddress = coAddress;
	}
	public String getBankNo() {
		return bankNo;
	}
	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankAddress() {
		return bankAddress;
	}
	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}
	public String getLinkman() {
		return linkman;
	}
	public void setLinkman(String linkman) {
		this.linkman = linkman;
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
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public MultipartFile getIdImgFile() {
		return idImgFile;
	}
	public void setIdImgFile(MultipartFile idImgFile) {
		this.idImgFile = idImgFile;
	}
	public MultipartFile getOpImgFile() {
		return opImgFile;
	}
	public void setOpImgFile(MultipartFile opImgFile) {
		this.opImgFile = opImgFile;
	}
	public MultipartFile getOrImgFile() {
		return orImgFile;
	}
	public void setOrImgFile(MultipartFile orImgFile) {
		this.orImgFile = orImgFile;
	}
	
}

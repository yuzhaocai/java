package com.lczy.media.vo;

import org.springframework.web.multipart.MultipartFile;

import com.lczy.media.entity.Customer;

public class MediaVO {
	
	private String id;
	private Customer customer;
	private Customer organization;
	private String name;
	private String account;
	private String description;
	private String showPic;
	private String qrCode;
	private String mediaType;
	private String category;
	private String industryType;
	private String region;
	private String products;
	private String tags;
	private Integer fans;
	private String fansDir;
	private String status;
	
	private MultipartFile showPicFile;
	private MultipartFile qrCodeFile;
	private MultipartFile fansNumFile;

	private String orgId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Customer getOrganization() {
		return organization;
	}

	public void setOrganization(Customer organization) {
		this.organization = organization;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getShowPic() {
		return showPic;
	}

	public void setShowPic(String showPic) {
		this.showPic = showPic;
	}

	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getIndustryType() {
		return industryType;
	}

	public void setIndustryType(String industryType) {
		this.industryType = industryType;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getProducts() {
		return products;
	}

	public void setProducts(String products) {
		this.products = products;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public Integer getFans() {
		return fans;
	}

	public void setFans(Integer fans) {
		this.fans = fans;
	}

	public String getFansDir() {
		return fansDir;
	}

	public void setFansDir(String fansDir) {
		this.fansDir = fansDir;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public MultipartFile getShowPicFile() {
		return showPicFile;
	}

	public void setShowPicFile(MultipartFile showPicFile) {
		this.showPicFile = showPicFile;
	}

	public MultipartFile getQrCodeFile() {
		return qrCodeFile;
	}

	public void setQrCodeFile(MultipartFile qrCodeFile) {
		this.qrCodeFile = qrCodeFile;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public MultipartFile getFansNumFile() {
		return fansNumFile;
	}

	public void setFansNumFile(MultipartFile fansNumFile) {
		this.fansNumFile = fansNumFile;
	}
	
}

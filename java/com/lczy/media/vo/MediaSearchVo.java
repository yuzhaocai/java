package com.lczy.media.vo;

public class MediaSearchVo {
	
	private int pageSize = 20;
	
	private int pageNum = 1;
	
	private String mediaType;
	
	private String category;
	
	private String industryTypes;
	
	private String fans;
	
	private String regions;
	
	private String tags;
	
	private String sort;
	
	private String name;
	
	private String level;

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
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

	public String getIndustryTypes() {
		return industryTypes;
	}

	public void setIndustryTypes(String industryTypes) {
		this.industryTypes = industryTypes;
	}

	public String getFans() {
		return fans;
	}

	public void setFans(String fans) {
		this.fans = fans;
	}

	public String getRegions() {
		return regions;
	}

	public void setRegions(String regions) {
		this.regions = regions;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
	
}

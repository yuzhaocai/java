package com.lczy.media.vo;

public class RequirementSearchVo {
	
	private int pageSize = 20;
	
	private int pageNum = 1;
	
	private String mediaTypes;
	
	private String industryTypes;
	
	private String budget;
	
	private String sort;
	
	private String name;

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

	public String getIndustryTypes() {
		return industryTypes;
	}

	public void setIndustryTypes(String industryTypes) {
		this.industryTypes = industryTypes;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getMediaTypes() {
		return mediaTypes;
	}

	public void setMediaTypes(String mediaTypes) {
		this.mediaTypes = mediaTypes;
	}

	public String getBudget() {
		return budget;
	}

	public void setBudget(String budget) {
		this.budget = budget;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}

package com.lczy.media.vo;

import java.util.ArrayList;
import java.util.List;

public class MediaTypeVO {
	
	private String id;
	private String name;
	private String category;
	private List<String> tagList = new ArrayList<String>();
	
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
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public List<String> getTagList() {
		return tagList;
	}
	
	public void setTagList(List<String> tagList) {
		this.tagList = tagList;
	}

}

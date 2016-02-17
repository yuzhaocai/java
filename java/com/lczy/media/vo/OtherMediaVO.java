package com.lczy.media.vo;

import java.util.List;

import com.lczy.media.entity.OtherMediaTab;

public class OtherMediaVO {

	private String id;
	private String name;
	private String showPic;
	private String category;
	private String industryType;
	private String region;
	private String attachment;
	private List<OtherMediaTab> mediaTabs;
	
	public OtherMediaVO() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShowPic() {
		return this.showPic;
	}

	public void setShowPic(String showPic) {
		this.showPic = showPic;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getIndustryType() {
		return this.industryType;
	}

	public void setIndustryType(String industryType) {
		this.industryType = industryType;
	}

	public String getRegion() {
		return this.region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getAttachment() {
		return this.attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public List<OtherMediaTab> getMediaTabs() {
		return this.mediaTabs;
	}

	public void setMediaTabs(List<OtherMediaTab> mediaTabs) {
		this.mediaTabs = mediaTabs;
	}

}

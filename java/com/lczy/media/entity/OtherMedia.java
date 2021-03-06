package com.lczy.media.entity;

// Generated 2015-3-27 17:06:05 by Hibernate Tools 4.3.1

import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

/**
 * Media generated by hbm2java
 */
@Entity
@Table(name = "t_other_media")
@Cacheable
public class OtherMedia {

	private String id;
	private String name;
	private String showPic;
	private String category;
	private String industryType;
	private String region;
	private String attachment;
	private String createBy;
	private Date createTime;
	private String modifyBy;
	private Date modifyTime;
	private List<OtherMediaTab> mediaTabs;
	
	public OtherMedia() {
	}

	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "NAME", nullable = false, length = 64)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "SHOW_PIC", length = 256)
	public String getShowPic() {
		return this.showPic;
	}

	public void setShowPic(String showPic) {
		this.showPic = showPic;
	}

	@Column(name = "CATEGORY", length = 32)
	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Column(name = "INDUSTRY_TYPE", length = 32)
	public String getIndustryType() {
		return this.industryType;
	}

	public void setIndustryType(String industryType) {
		this.industryType = industryType;
	}

	@Column(name = "REGION", nullable = false, length = 32)
	public String getRegion() {
		return this.region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	@Column(name = "ATTACHMENT", length = 256)
	public String getAttachment() {
		return this.attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	@Column(name = "CREATE_BY", length = 32)
	public String getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME", nullable = false, length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "MODIFY_BY", length = 32)
	public String getModifyBy() {
		return this.modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFY_TIME", nullable = false, length = 19)
	public Date getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "media", cascade = {CascadeType.REMOVE})
	public List<OtherMediaTab> getMediaTabs() {
		return this.mediaTabs;
	}

	public void setMediaTabs(List<OtherMediaTab> mediaTabs) {
		this.mediaTabs = mediaTabs;
	}

	@Override
	public String toString() {
		return "Media [id=" + id + ", name=" + name
				+ ", showPic=" + showPic + ", category=" + category + ", industryType=" + industryType + ", region=" + region
				+ ", createBy=" + createBy + ", createTime=" + createTime + ", modifyBy=" + modifyBy + ", modifyTime="
				+ modifyTime + "]";
	}
	
}

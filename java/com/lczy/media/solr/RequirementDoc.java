package com.lczy.media.solr;

import java.util.Arrays;
import java.util.List;

import org.apache.solr.client.solrj.beans.Field;

import com.google.common.collect.Lists;
import com.lczy.common.util.SpringUtils;
import com.lczy.media.entity.Requirement;
import com.lczy.media.service.common.DicProvider;

/**
 * 需求文档.
 * 
 * @author wu
 *
 */
public class RequirementDoc implements SolrBean {
	
	public static final String C_NAME = "requirement";

	@Override
	public String getCName() {
		return C_NAME;
	}
	
	@Field
	private String id;
	
	@Field
	private String name;
	
	@Field
	private String summary;
	
	@Field
	private long startTime;
	
	@Field
	private long endTime;
	
	@Field
	private long deadline;
	
	@Field
	private boolean isPublic;
	
	@Field
	private String status;
	
	@Field
	private long createTime;
	
	@Field
	private long modifyTime;
	
	@Field
	private List<String> mediaTypes;
	
	@Field
	private List<String> serviceTypes;
	
	@Field
	private List<String> regions;
	
	@Field
	private List<String> industryTypes;
	
	@Field
	private List<String> industryTypeNames;
	
	@Field
	private int budget;
	
	@Field
	private boolean allowChange;
	
	@Field
	private String article;

	public RequirementDoc() {}
	
	public RequirementDoc(Requirement r) {
		DicProvider dicProvider = SpringUtils.getBean(DicProvider.class);
		
		this.id = r.getId();
		this.budget = r.getBudget();
		this.name = r.getName();
		this.summary = r.getSummary();
		this.mediaTypes = r.getMediaTypes() != null ? Arrays.asList(r.getMediaTypes().split(",")) : null;
		this.serviceTypes = r.getServiceTypes() != null ? Arrays.asList(r.getServiceTypes().split(",")) : null;
		this.regions = r.getRegions() != null ? Arrays.asList(r.getRegions().split(",")) : null;
		this.industryTypes = r.getIndustryTypes() != null ?Arrays.asList(r.getIndustryTypes().split(",")) : null;
		if( this.industryTypes != null ) {
			this.industryTypeNames = Lists.newArrayList(dicProvider.getItemNames(this.industryTypes).split(","));
		}
		this.startTime = r.getStartTime().getTime();
		this.endTime = r.getEndTime().getTime();
		this.deadline = r.getDeadline().getTime();
		this.createTime = r.getCreateTime().getTime();
		this.modifyTime = r.getModifyTime().getTime();
		this.status = r.getStatus();
		this.isPublic = r.getIsPublic();
		this.allowChange = r.isAllowChange();
		this.article = r.getArticle();
	}

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

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public long getDeadline() {
		return deadline;
	}

	public void setDeadline(long deadline) {
		this.deadline = deadline;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(long modifyTime) {
		this.modifyTime = modifyTime;
	}

	public List<String> getMediaTypes() {
		return mediaTypes;
	}

	public void setMediaTypes(List<String> mediaTypes) {
		this.mediaTypes = mediaTypes;
	}

	public List<String> getServiceTypes() {
		return serviceTypes;
	}

	public void setServiceTypes(List<String> serviceTypes) {
		this.serviceTypes = serviceTypes;
	}

	public List<String> getRegions() {
		return regions;
	}

	public void setRegions(List<String> regions) {
		this.regions = regions;
	}

	public List<String> getIndustryTypes() {
		return industryTypes;
	}

	public void setIndustryTypes(List<String> industryTypes) {
		this.industryTypes = industryTypes;
	}

	public int getBudget() {
		return budget;
	}

	public void setBudget(int budget) {
		this.budget = budget;
	}

	public boolean isAllowChange() {
		return allowChange;
	}

	public void setAllowChange(boolean allowChange) {
		this.allowChange = allowChange;
	}

	public String getArticle() {
		return article;
	}

	public void setArticle(String article) {
		this.article = article;
	}

	public List<String> getIndustryTypeNames() {
		return industryTypeNames;
	}

	public void setIndustryTypeNames(List<String> industryTypeNames) {
		this.industryTypeNames = industryTypeNames;
	}
	
}

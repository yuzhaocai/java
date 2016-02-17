package com.lczy.media.vo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.lczy.media.entity.MediaQuote;
import com.lczy.media.entity.Requirement;

public class RequirementVO {

	private String id;
	private String name;
	private String summary;
	private String startTime;
	private String endTime;
	private String deadline;
	private String certImg;
	private String article;
	private boolean isPublic;
	private String status;
	private String createBy;
	private String createTime;
	private String modifyBy;
	private String modifyTime;
	
	private boolean hasArticle;
	private String[] mediaTypes;
	private String[] regions;
	private String[] serviceTypes;
	private String[] industryTypes;
	private int budget;
	private String inviteNum;
	
	private MultipartFile articleFile; //稿件
	private MultipartFile articleMatterFile; //撰稿素材
	private MultipartFile certImgFile; //资质文件
	
	private boolean allowChange;
	
	private int articlePrice;
	
	private String currentMediaType; //当前媒体类型
	
	private List<String> doneMediaTypes = new ArrayList<String>(); //已经处理过的媒体类型
	
	private List<MediaQuote> quotes = new ArrayList<MediaQuote>(); //选择的媒体报价
	
	private String excludes; //排除的媒体ID，用于查询
	
	private String category; //媒体认证分类
	
	private String fans; //粉丝数
	
	public RequirementVO() {}
	
	public RequirementVO(Requirement entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.summary = entity.getSummary();
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		this.startTime = df.format(entity.getStartTime());
		this.endTime = df.format(entity.getEndTime());
		this.deadline = df.format(entity.getDeadline());
		
		this.certImg = entity.getCertImg();
		this.article = entity.getArticle();
		this.isPublic = entity.getIsPublic();
		this.status = entity.getStatus();
		this.mediaTypes = entity.getMediaTypes().split(",");
		this.regions = entity.getRegions().split(",");
		this.serviceTypes = entity.getServiceTypes().split(",");
		this.industryTypes = entity.getIndustryTypes().split(",");
		this.budget = entity.getBudget();
		this.inviteNum = entity.getInviteNum();
		this.allowChange = entity.isAllowChange();
		this.articlePrice = entity.getArticlePrice();
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getStartTime() {
		return startTime;
	}
	
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	public String getEndTime() {
		return endTime;
	}
	
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public MultipartFile getCertImgFile() {
		return certImgFile;
	}
	
	public void setCertImgFile(MultipartFile certImgFile) {
		this.certImgFile = certImgFile;
	}
	
	public String getDeadline() {
		return deadline;
	}
	
	public void setDeadline(String deadline) {
		this.deadline = deadline;
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

	public boolean getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public String getCertImg() {
		return certImg;
	}

	public void setCertImg(String certImg) {
		this.certImg = certImg;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getModifyBy() {
		return modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public boolean isHasArticle() {
		return hasArticle;
	}

	public void setHasArticle(boolean hasArticle) {
		this.hasArticle = hasArticle;
	}

	public String[] getMediaTypes() {
		return mediaTypes;
	}

	public void setMediaTypes(String[] mediaTypes) {
		this.mediaTypes = mediaTypes;
	}

	public String[] getRegions() {
		return regions;
	}

	public void setRegions(String[] regions) {
		this.regions = regions;
	}

	public String[] getServiceTypes() {
		return serviceTypes;
	}

	public void setServiceTypes(String[] serviceTypes) {
		this.serviceTypes = serviceTypes;
	}

	public String[] getIndustryTypes() {
		return industryTypes;
	}

	public void setIndustryTypes(String[] industryTypes) {
		this.industryTypes = industryTypes;
	}

	public int getBudget() {
		return budget;
	}

	public void setBudget(int budget) {
		this.budget = budget;
	}

	public String getInviteNum() {
		return inviteNum;
	}

	public void setInviteNum(String inviteNum) {
		this.inviteNum = inviteNum;
	}

	public MultipartFile getArticleMatterFile() {
		return articleMatterFile;
	}

	public void setArticleMatterFile(MultipartFile articleMatter) {
		this.articleMatterFile = articleMatter;
	}

	public boolean isAllowChange() {
		return allowChange;
	}

	public void setAllowChange(boolean allowChange) {
		this.allowChange = allowChange;
	}

	public MultipartFile getArticleFile() {
		return articleFile;
	}

	public void setArticleFile(MultipartFile articleFile) {
		this.articleFile = articleFile;
	}

	public int getArticlePrice() {
		return articlePrice;
	}

	public void setArticlePrice(int articlePrice) {
		this.articlePrice = articlePrice;
	}

	public String getArticle() {
		return article;
	}

	public void setArticle(String article) {
		this.article = article;
	}

	public String getCurrentMediaType() {
		return currentMediaType;
	}

	public void setCurrentMediaType(String currentMediaType) {
		this.currentMediaType = currentMediaType;
	}

	public List<String> getDoneMediaTypes() {
		return doneMediaTypes;
	}

	public void setDoneMediaTypes(List<String> doneMediaTypes) {
		this.doneMediaTypes = doneMediaTypes;
	}

	public List<MediaQuote> getQuotes() {
		return quotes;
	}

	public void setQuotes(List<MediaQuote> quotes) {
		this.quotes = quotes;
	}

	public String getExcludes() {
		return excludes;
	}

	public void setExcludes(String excludes) {
		this.excludes = excludes;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getFans() {
		return fans;
	}

	public void setFans(String fans) {
		this.fans = fans;
	}
}

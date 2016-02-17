package com.lczy.media.entity;

// Generated 2015-3-27 17:06:05 by Hibernate Tools 4.3.1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import static com.lczy.media.util.Constants.*;

@Entity
@Table(name = "t_requirement")
@Cacheable
public class Requirement implements java.io.Serializable {

	private static final long serialVersionUID = -4328463927953533404L;

	private String id;
	private Customer customer;
	private String name;
	private String summary;
	private Date startTime;
	private Date endTime;
	private Date deadline;
	private String certImg;
	private boolean isPublic;
	private String status;
	private String createBy;
	private Date createTime;
	private String modifyBy;
	private Date modifyTime;
	private Set<ReqMedia> reqMedias = new HashSet<ReqMedia>(0);
	private Set<Order> orders = new HashSet<Order>(0);
	
	private boolean hasArticle;
	private String mediaTypes;
	private String regions;
	private String serviceTypes;
	private String industryTypes;
	private int budget;
	private String inviteNum;
	private boolean isFinished;
	private boolean allowChange;
	
	private String article;
	private int articlePrice;
	private String articleMatter;
	
	private boolean deleted;

	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	@GenericGenerator( name="id_gen", strategy="com.lczy.common.data.FormatTableGenerator", 
	   parameters = {
			@Parameter( name = "format",         value = "%1$ty%1$tm%1$td%2$08d"),
			@Parameter( name = "table_name",     value = "sys_sequences"),
			@Parameter( name = "segment_value",  value = "req_seq"),
			@Parameter( name = "increment_size", value = "10"), 
			@Parameter( name = "optimizer",      value = "pooled-lo")
	   })
	@GeneratedValue(generator="id_gen")
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUST_ID", nullable = false)
	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Column(name = "NAME", nullable = false, length = 128)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "SUMMARY", length = 1500)
	public String getSummary() {
		return this.summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_TIME", nullable = false, length = 19)
	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_TIME", nullable = false, length = 19)
	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DEADLINE", nullable = false, length = 19)
	public Date getDeadline() {
		return this.deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	@Column(name = "CERT_IMG", length = 256)
	public String getCertImg() {
		return this.certImg;
	}

	public void setCertImg(String certImg) {
		this.certImg = certImg;
	}

	@Column(name = "IS_PUBLIC")
	public boolean getIsPublic() {
		return this.isPublic;
	}

	public void setIsPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	@Column(name = "STATUS", length = 32)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "requirement", cascade = {CascadeType.ALL})
	public Set<ReqMedia> getReqMedias() {
		return this.reqMedias;
	}

	public void setReqMedias(Set<ReqMedia> reqMedias) {
		this.reqMedias = reqMedias;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "requirement")
	public Set<Order> getOrders() {
		return this.orders;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	@Column(name = "HAS_ARTICLE")
	public boolean isHasArticle() {
		return hasArticle;
	}

	public void setHasArticle(boolean hasArticle) {
		this.hasArticle = hasArticle;
	}

	@Column(name = "MEDIA_TYPES")
	public String getMediaTypes() {
		return mediaTypes;
	}

	public void setMediaTypes(String mediaTypes) {
		this.mediaTypes = mediaTypes;
	}

	@Column(name = "REGIONS")
	public String getRegions() {
		return regions;
	}

	public void setRegions(String regions) {
		this.regions = regions;
	}

	@Column(name = "SERVICE_TYPES")
	public String getServiceTypes() {
		return serviceTypes;
	}

	public void setServiceTypes(String serviceTypes) {
		this.serviceTypes = serviceTypes;
	}

	@Column(name = "INDUSTRY_TYPES")
	public String getIndustryTypes() {
		return industryTypes;
	}

	public void setIndustryTypes(String industryTypes) {
		this.industryTypes = industryTypes;
	}

	@Column(name = "BUDGET")
	public int getBudget() {
		return budget;
	}

	public void setBudget(int budget) {
		this.budget = budget;
	}

	@Column(name = "INVITE_NUM")
	public String getInviteNum() {
		return inviteNum;
	}

	public void setInviteNum(String inviteNum) {
		this.inviteNum = inviteNum;
	}

	@Column(name = "IS_FINISHED")
	public boolean getIsFinished() {
		return isFinished;
	}

	public void setIsFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}

	@Column(name = "ALLOW_CHANGE")
	public boolean isAllowChange() {
		return allowChange;
	}

	public void setAllowChange(boolean allowChange) {
		this.allowChange = allowChange;
	}

	@Column(name = "ARTICLE")
	public String getArticle() {
		return article;
	}

	public void setArticle(String article) {
		this.article = article;
	}

	@Column(name = "ARTICLE_PRICE")
	public int getArticlePrice() {
		return articlePrice;
	}

	public void setArticlePrice(int articlePrice) {
		this.articlePrice = articlePrice;
	}

	@Column(name = "ARTICLE_MATTER")
	public String getArticleMatter() {
		return articleMatter;
	}

	public void setArticleMatter(String articleMatter) {
		this.articleMatter = articleMatter;
	}
	
	@Transient
	public int getOrderAmount() {
		int amount = 0;
		if( getOrders().size() > 0 ) {
			for( Order order : getOrders() ) {
				amount += order.getAmount();
			}
		}
		return amount;
	}
	
	/**
	 * @return 确认应邀的媒体数量
	 */
	@Transient
	public int getPassiveNum() {
		int num = 0;
		if( getReqMedias().size() > 0 ) {
			for( ReqMedia rm : getReqMedias() ) {
				if( MediaFeedback.ACCEPT.equalsIgnoreCase(rm.getFbStatus()) 
					&& InviteType.PASSIVE.equalsIgnoreCase(rm.getInviteType())&&AdverConfirm.NULL.equalsIgnoreCase(rm.getCfStatus())) {
					num++;
				}
			}
		}
		return num;
	}
	
	/**
	 * @return 主动应征的媒体数量
	 */
	@Transient
	public int getActiveNum() {
		int num = 0;
		if( getReqMedias().size() > 0 ) {
			for( ReqMedia rm : getReqMedias() ) {
				if( MediaFeedback.ACCEPT.equalsIgnoreCase(rm.getFbStatus()) 
						&& InviteType.ACTIVE.equalsIgnoreCase(rm.getInviteType())&&AdverConfirm.NULL.equalsIgnoreCase(rm.getCfStatus())) {
					num++;
				}
			}
		}
		return num;
	}
	
	@Column(name = "IS_DELETED")
	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
}

package com.lczy.media.entity;

// Generated 2015-6-30 13:49:25 by Hibernate Tools 4.3.1

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

/**
 * ReqMedia generated by hbm2java
 */
@Entity
@Table(name = "t_req_media", uniqueConstraints = @UniqueConstraint(columnNames = {
		"REQ_ID", "MEDIA_ID" }))
public class ReqMedia implements java.io.Serializable {

	private static final long serialVersionUID = 5312353570356625518L;
	
	private String id;
	private Media media;
	private Requirement requirement;
	private String inviteType;
	private String quoteType;
	private Integer price;
	private Integer priceMedia;
	private Integer tax;
	private Date createTime;
	private String fbStatus;
	private Date fbTime;
	private String cfStatus;
	private Date cfTime;
	private String refuseReason;
	private boolean changed;
	private String changedArticle;
	private String changedReason;
	private String status;


	public ReqMedia() {
	}

	public ReqMedia(String id, Media media, Requirement requirement,
			String inviteType, String quoteType, Date createTime,
			String fbStatus, String cfStatus) {
		this.id = id;
		this.media = media;
		this.requirement = requirement;
		this.inviteType = inviteType;
		this.quoteType = quoteType;
		this.createTime = createTime;
		this.fbStatus = fbStatus;
		this.cfStatus = cfStatus;
	}

	public ReqMedia(String id, Media media, Requirement requirement,
			String inviteType, String quoteType, Integer price, Integer priceMedia, Integer tax,
			Date createTime, String fbStatus, Date fbTime, String cfStatus,
			Date cfTime) {
		this.id = id;
		this.media = media;
		this.requirement = requirement;
		this.inviteType = inviteType;
		this.quoteType = quoteType;
		this.price = price;
		this.priceMedia = priceMedia;
		this.tax = tax;
		this.createTime = createTime;
		this.fbStatus = fbStatus;
		this.fbTime = fbTime;
		this.cfStatus = cfStatus;
		this.cfTime = cfTime;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEDIA_ID", nullable = false)
	public Media getMedia() {
		return this.media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REQ_ID", nullable = false)
	public Requirement getRequirement() {
		return this.requirement;
	}

	public void setRequirement(Requirement requirement) {
		this.requirement = requirement;
	}

	@Column(name = "INVITE_TYPE", nullable = false, length = 32)
	public String getInviteType() {
		return this.inviteType;
	}

	public void setInviteType(String inviteType) {
		this.inviteType = inviteType;
	}

	@Column(name = "QUOTE_TYPE", nullable = false, length = 32)
	public String getQuoteType() {
		return this.quoteType;
	}

	public void setQuoteType(String quoteType) {
		this.quoteType = quoteType;
	}

	@Column(name = "PRICE")
	public Integer getPrice() {
		return this.price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}
	
	@Column(name = "PRICE_MEDIA")
	public Integer getPriceMedia() {
		return priceMedia;
	}

	public void setPriceMedia(Integer priceMedia) {
		this.priceMedia = priceMedia;
	}
	
	@Column(name = "TAX")
	public Integer getTax() {
		return tax;
	}

	public void setTax(Integer tax) {
		this.tax = tax;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME", nullable = false, length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "FB_STATUS", nullable = false, length = 32)
	public String getFbStatus() {
		return this.fbStatus;
	}

	public void setFbStatus(String fbStatus) {
		this.fbStatus = fbStatus;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FB_TIME", length = 19)
	public Date getFbTime() {
		return this.fbTime;
	}

	public void setFbTime(Date fbTime) {
		this.fbTime = fbTime;
	}

	@Column(name = "CF_STATUS", nullable = false, length = 32)
	public String getCfStatus() {
		return this.cfStatus;
	}

	public void setCfStatus(String cfStatus) {
		this.cfStatus = cfStatus;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CF_TIME", length = 19)
	public Date getCfTime() {
		return this.cfTime;
	}

	public void setCfTime(Date cfTime) {
		this.cfTime = cfTime;
	}
	
	@Column(name = "REFUSE_REASON", length = 255)
	public String getRefuseReason() {
		return refuseReason;
	}

	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}

	
	
	@Column(name = "CHANGED")
	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	@Column(name = "CHANGED_ARTICLE", length = 1000)
	public String getChangedArticle() {
		return changedArticle;
	}

	public void setChangedArticle(String changedArticle) {
		this.changedArticle = changedArticle;
	}
	
	@Column(name = "CHANGED_REASON", length = 1000)
	public String getChangedReason() {
		return changedReason;
	}

	public void setChangedReason(String changedReason) {
		this.changedReason = changedReason;
	}

	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	

}

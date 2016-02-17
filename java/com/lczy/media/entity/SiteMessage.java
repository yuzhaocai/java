package com.lczy.media.entity;

// Generated 2015-7-9 17:03:47 by Hibernate Tools 4.3.1

import java.util.Date;

import javax.persistence.Cacheable;
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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * 站内信实体.
 */
@Entity
@Table(name = "t_site_message")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class SiteMessage implements java.io.Serializable {

	private String id;
	private Customer sender;
	private Customer receiver;
	private String type;
	private String title;
	private String content;
	private Date createTime;
	private Integer readFlag;

	public SiteMessage() {
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

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "SENDER")
	public Customer getSender() {
		return this.sender;
	}

	public void setSender(Customer sender) {
		this.sender = sender;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "RECEIVER")
	public Customer getReceiver() {
		return this.receiver;
	}

	public void setReceiver(Customer receiver) {
		this.receiver = receiver;
	}

	@Column(name = "TYPE", nullable = false, length = 60)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "TITLE", length = 100)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "CONTENT", nullable = false, length = 1500)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME", nullable = false, length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "READ_FLAG")
	public Integer getReadFlag() {
		return this.readFlag;
	}

	public void setReadFlag(Integer readFlag) {
		this.readFlag = readFlag;
	}

}

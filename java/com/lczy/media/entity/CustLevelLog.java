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

import org.hibernate.annotations.GenericGenerator;

/**
 * LevelLog generated by hbm2java
 */
@Entity
@Table(name = "t_cust_level_log")
public class CustLevelLog implements java.io.Serializable {

	private static final long serialVersionUID = -3521660362224445387L;
	
	private String id;
	private Customer customer;
	private String oldLevel;
	private String newLevel;
	private Date createTime;
	private String memo;

	public CustLevelLog() {
	}

	public CustLevelLog(String id, Customer customer, String oldLevel,
			String newLevel, Date createTime) {
		this.id = id;
		this.customer = customer;
		this.oldLevel = oldLevel;
		this.newLevel = newLevel;
		this.createTime = createTime;
	}

	public CustLevelLog(String id, Customer customer, String oldLevel,
			String newLevel, Date createTime, String memo) {
		this.id = id;
		this.customer = customer;
		this.oldLevel = oldLevel;
		this.newLevel = newLevel;
		this.createTime = createTime;
		this.memo = memo;
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
	@JoinColumn(name = "CUST_ID", nullable = false)
	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Column(name = "OLD_LEVEL", nullable = false, length = 32)
	public String getOldLevel() {
		return this.oldLevel;
	}

	public void setOldLevel(String oldLevel) {
		this.oldLevel = oldLevel;
	}

	@Column(name = "NEW_LEVEL", nullable = false, length = 32)
	public String getNewLevel() {
		return this.newLevel;
	}

	public void setNewLevel(String newLevel) {
		this.newLevel = newLevel;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME", nullable = false, length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "MEMO", length = 150)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}

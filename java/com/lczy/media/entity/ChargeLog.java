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
import org.hibernate.annotations.Parameter;

/**
 * ChargeLog generated by hbm2java
 */
@Entity
@Table(name = "t_charge_log")
public class ChargeLog implements java.io.Serializable {

	private static final long serialVersionUID = 1227322141555311292L;
	
	private String id;
	private Customer customer;
	private int amount;
	private String platform;
	private String data;
	private String status;
	private String transactionId;
	private Date createTime;
	private Date modifyTime;

	public ChargeLog() {
	}

	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	@GenericGenerator( name="id_gen", strategy="com.lczy.common.data.FormatTableGenerator", 
	   parameters = {
			@Parameter( name = "format",         value = "CL%1$ty%1$tm%1$td%2$08d"),
			@Parameter( name = "table_name",     value = "sys_sequences"),
			@Parameter( name = "segment_value",  value = "charge_seq"),
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
	@JoinColumn(name = "CUST_ID")
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Column(name = "AMOUNT", nullable = false)
	public int getAmount() {
		return this.amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	@Column(name = "PLATFORM", nullable = false, length = 32)
	public String getPlatform() {
		return this.platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}
	
	@Column(name = "DATA")
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME", nullable = false, length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFY_TIME", nullable = false, length = 19)
	public Date getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "TRANSACTION_ID")
	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

}

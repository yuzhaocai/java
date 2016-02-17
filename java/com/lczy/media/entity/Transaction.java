package com.lczy.media.entity;

// Generated 2015-4-10 18:58:21 by Hibernate Tools 4.3.1

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 交易记录.
 */
@Entity
@Table(name = "t_transaction")
public class Transaction implements java.io.Serializable {

	private static final long serialVersionUID = 1511084835832208378L;
	private String id;
	private Customer customer;
	private Order order;
	private String type;
	private int amount;
	private Date createTime;
	private String remark;
	private OfferLine offerLine;
	
	
	public Transaction() {
	}


	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	@GenericGenerator( name="id_gen", strategy="com.lczy.common.data.FormatTableGenerator", 
	   parameters = {
			@Parameter( name = "format",         value = "%1$ty%1$tm%1$td%2$08d"),
			@Parameter( name = "table_name",     value = "sys_sequences"),
			@Parameter( name = "segment_value",  value = "tran_seq"),
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
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="order_id")
	public Order getOrder() {
		return order;
	}


	public void setOrder(Order order) {
		this.order = order;
	}


	@Column(name = "TYPE", nullable = false, length = 32)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "AMOUNT", nullable = false)
	public int getAmount() {
		return this.amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME", nullable = false, length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "REMARK", length = 300)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OFFER_ID")
	public OfferLine getOfferLine() {
		return offerLine;
	}


	public void setOfferLine(OfferLine offerLine) {
		this.offerLine = offerLine;
	}
}

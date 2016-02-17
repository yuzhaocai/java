package com.lczy.media.entity;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "t_favorites_req")
@Cacheable
public class FavoritesReq implements java.io.Serializable {
	
	/**
	 *  序列号ID
	 */
	private static final long serialVersionUID = 1L;

	/** ID */
	private String id;
	
	/** 客户 */
	private Customer customer;
	
	/** 媒体 */
	private Requirement req;
	
	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUST_ID", nullable = false)
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REQ_ID", nullable = false)
	public Requirement getReq() {
		return req;
	}

	public void setReq(Requirement req) {
		this.req = req;
	}
	
}

package com.lczy.media.entity;

// Generated 2015-6-30 13:49:25 by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * LotteryTime generated by hbm2java
 */
@Entity
@Table(name = "t_lottery_time")
public class LotteryTime implements java.io.Serializable {

	private static final long serialVersionUID = -840124823099498251L;
	
	private String id;
	private Customer customer;
	private int times;

	public LotteryTime() {
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
	@JoinColumn(name = "CUST_ID")
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Column(name = "TIMES", nullable = false)
	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}


}

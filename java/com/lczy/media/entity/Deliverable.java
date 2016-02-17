package com.lczy.media.entity;

// Generated 2015-6-30 13:49:25 by Hibernate Tools 4.3.1

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * 订单任务交付单.
 */
@Entity
@Table(name = "t_deliverable")
public class Deliverable implements java.io.Serializable {

	private static final long serialVersionUID = 5657032828697787740L;

	private String id;
	private Order order;
	private String url;
	private String pics;
	private Date createTime;
	private String createBy;

	public Deliverable() {
	}

	public Deliverable(String id, Order order, String pics, Date createTime) {
		this.id = id;
		this.order = order;
		this.pics = pics;
		this.createTime = createTime;
	}

	public Deliverable(String id, Order order, String url, String pics,
			Date createTime, String createBy, Set<Order> orders) {
		this.id = id;
		this.order = order;
		this.url = url;
		this.pics = pics;
		this.createTime = createTime;
		this.createBy = createBy;
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

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORDER_ID", nullable = false)
	public Order getOrder() {
		return this.order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@Column(name = "URL", length = 256)
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "PICS", nullable = false, length = 500)
	public String getPics() {
		return this.pics;
	}

	public void setPics(String pics) {
		this.pics = pics;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME", nullable = false, length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "CREATE_BY", length = 32)
	public String getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

}

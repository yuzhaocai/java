package com.lczy.media.entity;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/** 活动
 * @author wang.xiaoxiang
 *
 */
@Entity
@Table(name = "t_activity")
@Cacheable
public class Activity implements java.io.Serializable {
	/**
	 *  序列号ID
	 */
	private static final long serialVersionUID = 1L;
	

	/** ID */
	private String id;
	
	/**	名称 */
	private String name; 
	
	/**	比例 */
	private float percent; 
	
	/**	开始时间 */
	private Date startTime;
	
	/**	结束时间 */
	private Date endTime;
	
	/** 创建时间	 */
	private Date createTime;
	
	/**	状态 */
	private String status;
	
	private int mediaNum;
	
	
	@Transient
	public int getMediaNum() {
		return mediaNum;
	}

	public void setMediaNum(int mediaNum) {
		this.mediaNum = mediaNum;
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
	
	@Column(name = "NAME", length = 64)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "PERCENT", precision = 3, scale = 2)
	public float getPercent() {
		return percent;
	}

	public void setPercent(float percent) {
		this.percent = percent;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_TIME", nullable = false, length = 19)
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_TIME", nullable = false, length = 19)
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME", nullable = false, length = 19)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Column(name = "STATUS", length = 32)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
	
}

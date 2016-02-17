package com.lczy.media.entity;


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

import org.hibernate.annotations.GenericGenerator;

/** 更多媒体生成的意向
 * @author wang.xiaoxiang
 *
 */
@Entity
@Table(name = "t_intention")
@Cacheable
public class Intention implements java.io.Serializable {
	/**
	 *  序列号ID
	 */
	private static final long serialVersionUID = 1L;

	/** ID */
	private String id;
	
	/** 更多媒体 */
	private OtherMedia target;
	
	/** 所在媒体详情页 */
	private String targetTab;
	
	/**	提交时间 */
	private Date createTime;
	
	/**	状态 */
	private String status;
	
	/**	客户电话 */
	private String custPhone;
	
	/**	客户名称 */
	private String custName;
	
	/**	对接人名称 */
	private String custManager;
	
	/**	处理人 */
	private User handler;
	
	/**	处理时间 */
	private Date handleTime; 
	
	/**	处理结果 */
	private String handleResult; 
	

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
	
	public void setTarget(OtherMedia target) {
		this.target = target;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TARGET_ID", nullable = false)
	public OtherMedia getTarget() {
		return target;
	}
	
	public String getTargetTab() {
		return targetTab;
	}
	
	@Column(name = "TARGET_TAB", length = 150)
	public void setTargetTab(String targetTab) {
		this.targetTab = targetTab;
	}

	@Column(name = "STATUS", length = 32)
	public String getStatus() {
		return this.status;
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
	
	@Column(name = "CUST_PHONE", length = 32)
	public String getCustPhone() {
		return custPhone;
	}

	public void setCustPhone(String custPhone) {
		this.custPhone = custPhone;
	}
	
	@Column(name = "CUST_NAME", length = 32)
	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}
	
	@Column(name = "CUST_MANAGER", length = 32)
	public String getCustManager() {
		return custManager;
	}

	public void setCustManager(String custManager) {
		this.custManager = custManager;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "HANDLER", nullable = false)
	public User getHandler() {
		return handler;
	}

	public void setHandler(User handler) {
		this.handler = handler;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "HANDLE_TIME", nullable = false, length = 19)
	public Date getHandleTime() {
		return handleTime;
	}

	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}
	
	@Column(name = "HANDLE_RESULT", length = 32)
	public String getHandleResult() {
		return handleResult;
	}

	public void setHandleResult(String handleResult) {
		this.handleResult = handleResult;
	}
	
	
	
}

package com.lczy.media.entity;

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

@Entity
@Table(name = "t_opinion")
public class Opinion implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1771315730769155975L;
	
	private String id;
	
	private String phone;
	
	private String feedbackType;
	
	private Date createTime;
	
	private String userType;
	
	private String feedbackContent;
	
	private String feedbackAttachment;
	
	private boolean status;
	
	private User handleBy;
	
	private Date handleTime;
	
	private String handleResult;

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

	@Column(name = "PHONE", nullable = false, length = 11)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "FEEDBACK_TYPE", nullable = false, length = 32)
	public String getFeedbackType() {
		return feedbackType;
	}

	public void setFeedbackType(String feedbackType) {
		this.feedbackType = feedbackType;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME", nullable = false, length = 19)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "USER_TYPE")
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	@Column(name = "FEEDBACK_CONTENT")
	public String getFeedbackContent() {
		return feedbackContent;
	}

	public void setFeedbackContent(String feedbackContent) {
		this.feedbackContent = feedbackContent;
	}

	@Column(name = "FEEDBACK_ATTACHMENT", length = 32)
	public String getFeedbackAttachment() {
		return feedbackAttachment;
	}

	public void setFeedbackAttachment(String feedbackAttachment) {
		this.feedbackAttachment = feedbackAttachment;
	}

	@Column(name = "STATUS")
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "HANDLE_BY")
	public User getHandleBy() {
		return handleBy;
	}

	public void setHandleBy(User handleBy) {
		this.handleBy = handleBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "HANDLE_TIME", length = 19)
	public Date getHandleTime() {
		return handleTime;
	}

	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}

	@Column(name = "HANDLE_RESULT")
	public String getHandleResult() {
		return handleResult;
	}

	public void setHandleResult(String handleResult) {
		this.handleResult = handleResult;
	}
	
}

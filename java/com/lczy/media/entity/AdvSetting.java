package com.lczy.media.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "t_adv_setting")
public class AdvSetting implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7361917409695178380L;
	
	private String id;
	
	private String title;
	
	private String type;
	
	private String link;
	
	private String pic;
	
	private Date createTime;
	
	private String createBy;
	
	private int weight;
	
	private String mediaId;
	
	private boolean coop;
	
	private String outLine;
	
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

	@Column(name = "TITLE", nullable = false, length = 45)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "TYPE", nullable = false, length = 45)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "LINK", nullable = false, length = 256)
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@Column(name = "PIC", nullable = false, length = 256)
	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME", nullable = false, length = 19)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "CREATE_BY", length = 32)
	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	@Column(name = "WEIGHT", length = 10)
	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	@Transient
	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	@Column(name = "COOP")
	public boolean isCoop() {
		return coop;
	}

	public void setCoop(boolean coop) {
		this.coop = coop;
	}

	@Column(name = "OUT_LINE")
	public String getOutLine() {
		return outLine;
	}

	public void setOutLine(String outLine) {
		this.outLine = outLine;
	}
	
	

}

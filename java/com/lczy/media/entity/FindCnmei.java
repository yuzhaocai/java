package com.lczy.media.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "t_find_cnmei")
public class FindCnmei implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5188896623781369865L;

	private String id;
	
	private String url;
	
	private String title;
	
	private String img;
	
	private Date createTime;
	
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

	@Column(name = "URL", nullable = false, length = 200)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "TITLE", nullable = false, length = 200)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "IMG", nullable = false, length = 200)
	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME", nullable = false, length = 19)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "OUT_LINE", nullable = false)
	public String getOutLine() {
		return outLine;
	}

	public void setOutLine(String outLine) {
		this.outLine = outLine;
	}
	
	
}

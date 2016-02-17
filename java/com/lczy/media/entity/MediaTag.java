package com.lczy.media.entity;

// Generated 2015-3-27 17:06:05 by Hibernate Tools 4.3.1

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * MediaTag generated by hbm2java
 */
@Entity
@Table(name = "t_media_tag")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class MediaTag {

	private String id;
	private String name;
	private boolean hot;//热门
	private Integer count;
	private String createBy;
	private Date createTime;
	private String modifyBy;
	private Date modifyTime;
	private boolean rec;//推荐

	public MediaTag() {
	}

	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	@GenericGenerator( name="id_gen", strategy="com.lczy.common.data.FormatTableGenerator", 
	   parameters = {
			@Parameter( name = "format",         value = "%2$06d"),
			@Parameter( name = "table_name",     value = "sys_sequences"),
			@Parameter( name = "segment_value",  value = "tag_seq"),
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

	@Column(name = "NAME", nullable = false, length = 64)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name="HOT")
	public boolean getHot() {
		return this.hot;
	}

	public void setHot(boolean hot) {
		this.hot = hot;
	}
	
	@Column(name = "COUNT")
	public Integer getCount() {
		return this.count;
	}	

	public void setCount(Integer count) {
		this.count = count;
	}	

	@Column(name = "CREATE_BY", length = 32)
	public String getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME", nullable = false, length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "MODIFY_BY", length = 32)
	public String getModifyBy() {
		return this.modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFY_TIME", nullable = false, length = 19)
	public Date getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name="REC")
	public boolean isRec() {
		return rec;
	}

	public void setRec(boolean rec) {
		this.rec = rec;
	}
	
}

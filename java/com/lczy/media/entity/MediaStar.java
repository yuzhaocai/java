package com.lczy.media.entity;


import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/** 媒体的星级
 * @author wang.xiaoxiang
 *
 */
@Entity
@Table(name = "t_media_star")
@Cacheable
public class MediaStar implements java.io.Serializable {
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
	
}

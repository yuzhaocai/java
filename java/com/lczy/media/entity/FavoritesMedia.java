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
@Table(name = "t_favorites_media")
@Cacheable
public class FavoritesMedia implements java.io.Serializable {
	
	private static final long serialVersionUID = -1199379601628892046L;
	/** ID */
	private String id;
	
	/** 类型 ： 微博 、 微信 、更多媒体 */
	private String type;
	
	/** 客户 */
	private Customer customer;
	
	/** 媒体 */
	private Media media;
	
	/** 其他媒体 */
	private OtherMedia otherMedia;
	
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
	
	
	@Column(name = "TYPE", length = 32)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
	@JoinColumn(name = "MEDIA_ID", nullable = false)
	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OTHERMEDIA_ID", nullable = false)
	public OtherMedia getOtherMedia() {
		return otherMedia;
	}

	public void setOtherMedia(OtherMedia otherMedia) {
		this.otherMedia = otherMedia;
	}
	
	
	
	
}

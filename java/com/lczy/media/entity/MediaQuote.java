package com.lczy.media.entity;

// Generated 2015-6-30 13:49:25 by Hibernate Tools 4.3.1

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
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.annotations.GenericGenerator;

import com.lczy.media.util.Constants;

/**
 * MediaQuote generated by hbm2java
 */
@Entity
@Table(name = "t_media_quote")
public class MediaQuote {
	
	private String id;
	private Media media;
	private String type;
	private int price;
	private int priceMedia;
	private Integer priceActivity;
	private Integer tax;
	private String createBy;
	private Date createTime;
	private String modifyBy;
	private Date modifyTime;
	private String modifyType;
	
	private boolean enlisted;

	public MediaQuote() {
	}

	public MediaQuote(String id, Media media, String type, int price, int priceMedia, Integer priceActivity, Integer tax,
			Date createTime, Date modifyTime, String modifyType) {
		this.id = id;
		this.media = media;
		this.type = type;
		this.price = price;
		this.priceMedia = priceMedia;
		this.priceActivity = priceActivity;
		this.tax = tax;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
		this.modifyType = modifyType;
	}

	public MediaQuote(String id, Media media, String type, int price, int priceMedia, Integer priceActivity, Integer tax,
			String createBy, Date createTime, String modifyBy, Date modifyTime, String modifyType) {
		this.id = id;
		this.media = media;
		this.type = type;
		this.price = price;
		this.priceMedia = priceMedia;
		this.priceActivity = priceActivity;
		this.tax = tax;
		this.createBy = createBy;
		this.createTime = createTime;
		this.modifyBy = modifyBy;
		this.modifyTime = modifyTime;
		this.modifyType = modifyType;
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
	@JoinColumn(name = "MEDIA_ID", nullable = false)
	public Media getMedia() {
		return this.media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}

	@Column(name = "TYPE", nullable = false, length = 32)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "PRICE", nullable = false)
	public int getPrice() {
		return media.isActivitying() ? priceActivity : price;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}
	
	@Column(name = "PRICE_MEDIA", nullable = false)
	public int getPriceMedia() {
		return priceMedia;
	}

	public void setPriceMedia(int priceMedia) {
		this.priceMedia = priceMedia;
	}
	
	@Column(name = "PRICE_ACTIVITY")
	public Integer getPriceActivity() {
		return priceActivity;
	}

	public void setPriceActivity(Integer priceActivity) {
		this.priceActivity = priceActivity;
	}
	
	@Column(name = "TAX")
	public Integer getTax() {
		return tax;
	}

	public void setTax(Integer tax) {
		this.tax = tax;
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
	
	@Column(name = "MODIFY_TYPE", length = 32)
	public String getModifyType() {
		return modifyType;
	}

	public void setModifyType(String modifyType) {
		this.modifyType = modifyType;
	}

	@Transient
	public boolean isEnlisted() {
		return enlisted;
	}

	public void setEnlisted(boolean enlisted) {
		this.enlisted = enlisted;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		
		MediaQuote that = (MediaQuote) obj;
		return new EqualsBuilder().append(id, that.getId())
				.append(type, that.getType())
				.append(price, that.getPrice())
				.isEquals();
	}
	
}

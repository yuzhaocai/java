package com.lczy.media.vo;

import java.util.Date;

import com.lczy.media.entity.MediaQuote;
import com.lczy.media.solr.MediaDoc;

public class MediaQuoteVo {
	
	private String id;
	
	private MediaDoc media;
	
	private String type;
	
	private int price;
	
	private int priceMedia;
	
	private String createBy;
	
	private Date createTime;
	
	private String modifyBy;
	
	private Date modifyTime;
	
	public MediaQuoteVo(MediaQuote mediaQuote) {
		this.id = mediaQuote.getId();
		this.media = new MediaDoc(mediaQuote.getMedia());
		this.type = mediaQuote.getType();
		this.price = mediaQuote.getPrice();
		this.priceMedia = mediaQuote.getPriceMedia();
		this.createBy = mediaQuote.getCreateBy();
		this.createTime = mediaQuote.getCreateTime();
		this.modifyBy = mediaQuote.getModifyBy();
		this.modifyTime = mediaQuote.getModifyTime();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public MediaDoc getMedia() {
		return media;
	}

	public void setMedia(MediaDoc media) {
		this.media = media;
	}
	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
	
	public int getPriceMedia() {
		return priceMedia;
	}

	public void setPriceMedia(int priceMedia) {
		this.priceMedia = priceMedia;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getModifyBy() {
		return modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	
}

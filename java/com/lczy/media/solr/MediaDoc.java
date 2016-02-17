package com.lczy.media.solr;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.solr.client.solrj.beans.Field;

import com.google.common.collect.Lists;
import com.lczy.common.util.SpringUtils;
import com.lczy.media.entity.Media;
import com.lczy.media.entity.MediaQuote;
import com.lczy.media.entity.MediaTag;
import com.lczy.media.service.MediaTagService;
import com.lczy.media.service.common.DicProvider;


public class MediaDoc implements SolrBean {
	
	public static final String C_NAME = "media";
	
	@Override
	public String getCName() {
		return C_NAME;
	}

	@Field
	private String id;
	
	@Field
	private String name;
	
	@Field
	private String description;
	
	@Field
	private String showPic;
	
	@Field
	private String qrCode;
	
	@Field
	private String mediaType;
	
	@Field
	private String category;
	
	@Field
	private String categoryName;
	
	@Field
	private List<String> industryTypes;
	
	@Field
	private List<String> industryTypeNames;
	
	@Field
	private List<String> regions;
	
	@Field
	private List<String> products;
	
	@Field
	private List<String> tags;
	
	@Field
	private List<String> tagNames;
	
	@Field
	private int fans;
	
	@Field
	private List<String> fansDirs;
	
	@Field
	private long createTime;
	
	@Field
	private long modifyTime;
	
	@Field
	private String status;
	
	@Field
	private String level;
	
	@Field
	private List<Integer> prices;
	
	@Field
	private List<String> serviceTypes;
	
	public MediaDoc() {}
	
	public MediaDoc(Media m) {
		DicProvider dicProvider = SpringUtils.getBean(DicProvider.class);
		MediaTagService mediaTagService = SpringUtils.getBean(MediaTagService.class);
		
		this.id = m.getId();
		this.name = m.getName();
		this.description = m.getDescription();
		this.showPic = m.getShowPic();
		this.qrCode = m.getQrCode();
		this.mediaType = m.getMediaType();
		this.category = m.getCategory();
		this.categoryName = dicProvider.getDicItemName(m.getCategory());
		this.industryTypes = m.getIndustryType() != null ? Lists.newArrayList(m.getIndustryType().split(",")) : null;
		if( this.industryTypes != null ) {
			this.industryTypeNames = Lists.newArrayList(dicProvider.getItemNames(this.industryTypes).split(","));
		}
		this.regions = m.getRegion() != null ? Lists.newArrayList(m.getRegion().split(",")) : null;
		this.products = m.getProducts() != null ? Lists.newArrayList(m.getProducts().split(",")) : null;;
		this.tags = m.getTags() != null ? Lists.newArrayList(m.getTags().split(",")) : null;
		if( this.tags != null ) {
			this.tagNames = Lists.newArrayList();
			for( String tagId : this.tags ) {
				MediaTag tag = mediaTagService.get(tagId);
				if ( tag != null )
					tagNames.add(tag.getName());
			}
		}
		this.fans = m.getFans();
		this.fansDirs = m.getFansDir() != null ? Lists.newArrayList(m.getFansDir().split(",")) : null;;
		this.createTime = m.getCreateTime().getTime();
		this.modifyTime = m.getModifyTime().getTime();
		this.status = m.getStatus();
		this.level = m.getLevel();
		
		this.serviceTypes = Lists.newArrayList();
		this.prices = Lists.newArrayList();
		Set<MediaQuote> quotes = m.getMediaQuotes();
		for(MediaQuote mq : quotes) {
			serviceTypes.add(mq.getType());
			prices.add(mq.getPrice());
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getShowPic() {
		return showPic;
	}

	public void setShowPic(String showPic) {
		this.showPic = showPic;
	}

	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<String> getRegions() {
		return regions;
	}

	public void setRegions(List<String> regions) {
		this.regions = regions;
	}

	public List<String> getProducts() {
		return products;
	}

	public void setProducts(List<String> products) {
		this.products = products;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(long modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public List<Integer> getPrices() {
		return prices;
	}

	public void setPrices(List<Integer> prices) {
		this.prices = prices;
	}
	
	public int getFans() {
		return fans;
	}

	public void setFans(int fans) {
		this.fans = fans;
	}

	public List<String> getIndustryTypes() {
		return industryTypes;
	}

	public void setIndustryTypes(List<String> industryTypes) {
		this.industryTypes = industryTypes;
	}

	public List<String> getFansDirs() {
		return fansDirs;
	}

	public void setFansDirs(List<String> fansDirs) {
		this.fansDirs = fansDirs;
	}

	public List<String> getServiceTypes() {
		return serviceTypes;
	}

	public void setServiceTypes(List<String> serviceTypes) {
		this.serviceTypes = serviceTypes;
	}
	
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("id", id)
			.append("name", name)
			.append("mediaType", mediaType)
			.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MediaDoc other = (MediaDoc) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public List<String> getIndustryTypeNames() {
		return industryTypeNames;
	}

	public void setIndustryTypeNames(List<String> industryTypeNames) {
		this.industryTypeNames = industryTypeNames;
	}

	public List<String> getTagNames() {
		return tagNames;
	}

	public void setTagNames(List<String> tagNames) {
		this.tagNames = tagNames;
	}
	
}

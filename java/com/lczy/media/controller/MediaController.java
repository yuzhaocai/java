package com.lczy.media.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lczy.common.util.Strings;
import com.lczy.media.controller.common.BaseController;
import com.lczy.media.entity.Area;
import com.lczy.media.entity.MediaTag;
import com.lczy.media.service.AreaService;
import com.lczy.media.service.MediaService;
import com.lczy.media.service.MediaTagService;
import com.lczy.media.service.common.AreaProvider;
import com.lczy.media.service.common.DicProvider;
import com.lczy.media.solr.MediaDoc;
import com.lczy.media.solr.Page;
import com.lczy.media.util.Constants;
import com.lczy.media.util.FileServerUtils;
import com.lczy.media.util.JsonBean;
import com.lczy.media.vo.MediaSearchVo;

public abstract class MediaController extends BaseController {
	
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	protected DicProvider dicProvider;
	
	@Autowired
	protected AreaProvider areaProvider;
	
	@Autowired
	protected MediaTagService mediaTagService;
	
	@Autowired
	protected MediaService mediaService;
	
	@Autowired
	protected AreaService areaService;
	
	/**
	 * 组装 solr 查询 map.
	 */
	protected Map<String, Object> getFieldsMap(MediaSearchVo vo) {
		Map<String, Object> fieldsMap = Maps.newLinkedHashMap();
		
		fieldsMap.put("status", Constants.MediaStatus.NORMAL);
		
		if( StringUtils.isNotBlank(vo.getMediaType()) ) {
			fieldsMap.put("mediaType", vo.getMediaType());
		}
		if( StringUtils.isNotBlank(vo.getCategory()) ) {
			fieldsMap.put("category", vo.getCategory());
		}
		if( StringUtils.isNotBlank(vo.getLevel()) ) {
			fieldsMap.put("level", vo.getLevel());
		}
		
		if( StringUtils.isNotBlank(vo.getIndustryTypes()) && !"ALL".equalsIgnoreCase(vo.getIndustryTypes()) ) {
			fieldsMap.put("industryTypes", vo.getIndustryTypes());
		}
		
		if( StringUtils.isNotBlank(vo.getFans()) ) {
			String[] range = vo.getFans().split(",");
			if( range.length == 1 ) {
				fieldsMap.put("fans", "[" + Integer.parseInt(range[0]) + " TO *]");
			} else {
				fieldsMap.put("fans", "[" + Integer.parseInt(range[0]) 
						+ " TO " + Integer.parseInt(range[1]) + "]");
			}
		}
		
		if( StringUtils.isNotBlank(vo.getRegions()) ) {
			
			Area area = areaService.get(vo.getRegions());
			String parentId = area.getParentId();
			if (Integer.parseInt(parentId) == 1)
				fieldsMap.put("regions", vo.getRegions());
			else {
				if (StringUtils.isNotBlank(parentId) && parentId.length() == 5
						&& Integer.parseInt(parentId) > 1) {
					ArrayList<String> regionsValues = new ArrayList<>();
					regionsValues.add(parentId);
					regionsValues.add(vo.getRegions());
					fieldsMap.put("regions", regionsValues);
				}
			}
		}
		
		if( StringUtils.isNotBlank(vo.getTags()) ) {
			fieldsMap.put("tags", vo.getTags());
		}
		
		if( StringUtils.isNotBlank(vo.getName()) ) {
			fieldsMap.put("name", "*" + vo.getName() + "*");
		}
		
		return fieldsMap;
	}
	
	protected JsonBean toJsonBean(Page<MediaDoc> page) {
		JsonBean bean = new JsonBean();
		bean.put("total", page.getTotal());
		bean.put("totalPage", page.getTotalPage());
		bean.put("pageNum", page.getPageNum());
		bean.put("pageSize", page.getPageSize());
		bean.put("next", page.isNext());
		bean.put("previous", page.isPrevious());
		List<JsonBean> data = Lists.newArrayList();
		for( MediaDoc doc : page.getData() ) {
			data.add(toJsonBean(doc));
		}
		bean.put("data", data);
		
		return bean;
	}
	protected JsonBean toJsonBean(MediaDoc doc) {
		JsonBean bean = new JsonBean();
		bean.put("id", doc.getId());
		bean.put("name", doc.getName());
		List<String> mediaTags = doc.getTags();
		if (mediaTags != null && mediaTags.contains("000151")) {
			bean.put("cooperate", true);
		} else {
			bean.put("cooperate", false);
		}
		bean.put("description", Strings.ellipsis(doc.getDescription(), 30));
		bean.put("showPic", FileServerUtils.getFileUrl(doc.getShowPic()));
		bean.put("mediaType", dicProvider.getDicItemName(doc.getMediaType()));
		bean.put("category", dicProvider.getDicItemName(doc.getCategory()));
		bean.put("regions", areaProvider.getAreaNames(doc.getRegions()));
		bean.put("fans", doc.getFans());
		bean.put("fansDirs", getItemNames(doc.getFansDirs()));
		List<String> industryTypes =  doc.getIndustryTypes();
		bean.put("industryTypes", getItemNames(industryTypes));
		if (industryTypes != null && industryTypes.size() > 1) {
			bean.put("industryType1", dicProvider.getDicItemName(industryTypes.get(0)));
			bean.put("industryType2", dicProvider.getDicItemName(industryTypes.get(1)));
		} else {
			bean.put("industryType1", bean.get("industryTypes"));
			bean.put("industryType2", "");
		}
		
		bean.put("serviceTypes", getItemNames(doc.getServiceTypes()));
		bean.put("prices", doc.getPrices());
		
		if( Constants.MediaType.WEIXIN.equals(doc.getMediaType()) ) {
			bean.put("type", "weixin");
		} else if( Constants.MediaType.WEIBO.equals(doc.getMediaType()) ) {
			bean.put("type", "weibo");
		}
		
		return bean;
	}
	
	

/*	private Integer getPrice(List<Integer> prices) {
		if( prices != null && prices.size() > 0 ) {
			return prices.get(0);
		} else {
			return 0;
		}
	}*/

	protected String getItemNames(List<String> codes) {
		
		return dicProvider.getItemNames(codes);
	}
	
	/**
	 * 设置公共的模型属性.
	 */
	protected void setModelAttribute(Model model) {
		model.addAttribute("industryTypes", dicProvider.getDic(Constants.IndustryType.DIC_CODE).getDicItems());
		model.addAttribute("regions", getRegions());
		model.addAttribute("tags", getTags());
		model.addAttribute("recTags", getRecTags());
		model.addAttribute("weixinCategories", dicProvider.getDic(Constants.WeixinCategory.DIC_CODE).getDicItems());
		model.addAttribute("weiboCategories", dicProvider.getDic(Constants.WeiboCategory.DIC_CODE).getDicItems());
		model.addAttribute("mediaTypes", dicProvider.getDic(Constants.MediaType.DIC_CODE).getDicItems());
		model.addAttribute("otherMediaCategories", dicProvider.getDic(Constants.OtherMediaCategory.DIC_CODE).getDicItems());
		
	}

	private List<MediaTag> getTags() {
		
		return mediaTagService.findHotTags();
	}
	
	private List<MediaTag> getRecTags() {
		return mediaTagService.findRecTags();
	}

	private List<Area> getRegions() {
		return areaProvider.getProvinceMap().get(AreaProvider.HOT_CITIES_KEY);
	}
	
}

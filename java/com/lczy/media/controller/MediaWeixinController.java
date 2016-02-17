package com.lczy.media.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lczy.media.entity.Media;
import com.lczy.media.entity.MediaCase;
import com.lczy.media.entity.MediaTag;
import com.lczy.media.service.CustomerService;
import com.lczy.media.solr.MediaDoc;
import com.lczy.media.solr.Page;
import com.lczy.media.util.Constants;
import com.lczy.media.util.FileServerUtils;
import com.lczy.media.util.JsonBean;
import com.lczy.media.util.UserContext;
import com.lczy.media.vo.MediaSearchVo;

/**
 * 
 * 微信营销控制器.
 * 
 * @author wu
 *
 */
@Controller
@RequestMapping("/weixin")
public class MediaWeixinController extends MediaController {
	
	@Autowired
	private CustomerService customerService;
	
	@RequestMapping({"", "list"})
	public String list(Model model) {
		
		//页面高亮显示“微信”导航菜单
		model.addAttribute("activeSearch", "weixin");
		model.addAttribute("active", "weixin");
		model.addAttribute("categories", dicProvider.getDic(Constants.WeixinCategory.DIC_CODE).getDicItems());
		setModelAttribute(model);
		
		return "weixin/list";
	}
	
	@RequestMapping("load")
	@ResponseBody
	public String load(MediaSearchVo vo) {
		
		Map<String, Object> fieldsMap = getFieldsMap(vo);
		fieldsMap.put("mediaType", Constants.MediaType.WEIXIN);
		
		JsonBean result = new JsonBean();
		try {
			Page<MediaDoc> page = mediaService.find(fieldsMap, vo.getSort(), vo.getPageNum(), vo.getPageSize());
			result.put("result", true);
			result.put("page", toJsonBean(page));
		} catch (Exception e) {
			log.error("solr查询失败", e);
			result.put("result", false);
		}
		
		return result.toJson();
	}
	
	/**
	 *  查询推荐的媒体列表.
	 */
	@RequestMapping("loadRecommendMedias")
	@ResponseBody
	public String loadRecommendMedias(HttpServletRequest request) {
		
		Map<String, Object> fieldsMap = Maps.newLinkedHashMap();
		fieldsMap.put("mediaType", Constants.MediaType.WEIXIN);
		fieldsMap.put("status", Constants.MediaStatus.NORMAL);
		
		JsonBean result = new JsonBean();
		try {
			Page<MediaDoc> page = mediaService.find(fieldsMap, null, 1, 5);
			result.put("result", true);
			result.put("page", toJsonBean(page));
		} catch (Exception e) {
			log.error("solr查询失败", e);
			result.put("result", false);
		}
		
		return result.toJson();
	}
	
	@RequestMapping("detail/{id}")
	public String detail(@PathVariable String id, Model model) {
		
		setModelAttribute(model);
		//页面高亮显示“微信”导航菜单
		model.addAttribute("active", "weixin");
		model.addAttribute("categories", dicProvider.getDic(Constants.WeixinCategory.DIC_CODE).getDicItems());
		
		Media media = mediaService.get(id);
		List<MediaTag>mediaTags = Lists.newArrayList();
		//媒体标签
		if(StringUtils.isNotBlank(media.getTags())){
			if (media.getTags().contains("000151")) {
				model.addAttribute("cooperate", true);
			} else {
				model.addAttribute("cooperate", false);
			}
			String[] tagsId = media.getTags().split(",");
			//mediaTags =  mediaTagService.findMediaTags(tagsId);
			for( String tagId: tagsId ) {
				mediaTags.add(mediaTagService.get(tagId));
			}
		}
		//案例图片
		for(MediaCase mdc :media.getMediaCases() ){
			if(StringUtils.isNotBlank(mdc.getShowPic())){
				String[] showPicArr = mdc.getShowPic().split(";");
				String showPics="";
				//循环案例图片
				for(int i=0;i<showPicArr.length;i++){
					showPics+=FileServerUtils.getFileUrl(showPicArr[i])+",";
				}
				mdc.setShowPic(showPics);
			}
		}
		media.setShowPic(FileServerUtils.getFileUrl(media.getShowPic()));
		
    	//行业类型
    	List<String> industryTypeList = Lists.newArrayList();
    	//地区
    	List<String> regionList = Lists.newArrayList();
    	//适合产品
    	List<String> productsList = Lists.newArrayList();
    	//粉丝方向
    	List<String> fansDirList = Lists.newArrayList();
    	
    	if( media.getIndustryType() != null ) {
			for(String industryType:media.getIndustryType().split(",")){
				industryTypeList.add(industryType);
			}
    	}
    	
    	if( media.getRegion() != null ) {
			for(String region:media.getRegion().split(",")){
				regionList.add(region);
			}
    	}
    	
		if (media.getProducts() != null) {
			for(String products:media.getProducts().split(",")){
				productsList.add(products);
			}
		}
		
		if(null!=media.getFansDir()){
			for(String fansDir:media.getFansDir().split(",")){
				fansDirList.add(fansDir);
			}
		}
		
		model.addAttribute("media", media);
		model.addAttribute("mediaTags", mediaTags);
		model.addAttribute("mediaIndustryTypeList", industryTypeList);
		model.addAttribute("mediaRegionList", regionList);
		model.addAttribute("mediaProductsList", productsList);
		model.addAttribute("mediaFansDirList", fansDirList);
		if(UserContext.getCurrentCustomer()!=null)
			model.addAttribute("cust",customerService.get(UserContext.getCurrentCustomer().getId()));
		
		return "weixin/detail";
	}

}

package com.lczy.media.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lczy.common.util.Strings;
import com.lczy.common.web.Token;
import com.lczy.common.web.WebHelper;
import com.lczy.media.entity.Area;
import com.lczy.media.entity.Intention;
import com.lczy.media.entity.MediaTag;
import com.lczy.media.entity.OtherMedia;
import com.lczy.media.service.AreaService;
import com.lczy.media.service.IntentionService;
import com.lczy.media.service.OtherMediaService;
import com.lczy.media.service.common.AreaProvider;
import com.lczy.media.solr.MediaDoc;
import com.lczy.media.util.Constants;
import com.lczy.media.util.FileServerUtils;
import com.lczy.media.util.JsonBean;
import com.lczy.media.vo.MediaSearchVo;
import com.lczy.media.vo.OtherMediaSearchVo;

/**
 * 
 * 更多媒体营销控制器.
 * 
 * @author wang.haibin
 *
 */
@Controller
@RequestMapping("/other")
public class OtherMediaController extends MediaController {

	@Autowired
	private OtherMediaService otherMediaService;
	
	
	@Autowired
	private IntentionService intentionService;
	
	@Autowired
	protected AreaService areaService;
	
	@RequestMapping({"", "index"})
	public String index(Model model, ServletRequest request) {
		//页面高亮显示“更多媒体”导航菜单
		model.addAttribute("active", "other-media");
		
		model.addAttribute("categories", dicProvider.getDic(Constants.OtherMediaCategory.DIC_CODE).getDicItems());
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		int page = WebHelper.getPage(request);
		//页面每行5个媒体  要求显示4列
		int size = 20;
		Page<OtherMedia> data= otherMediaService.find(searchParams, page, size, "DESC_createTime");
		
		model.addAttribute("total", data.getTotalElements());
		model.addAttribute("totalPage", data.getTotalPages());
		model.addAttribute("pageNum", page);
		model.addAttribute("pageSize", size);
		model.addAttribute("next", page < data.getTotalPages());
		model.addAttribute("previous", data.getTotalElements() > 0 && page > 1);
		setModelAttribute(model);
		
		model.addAttribute("data", parseShowPic(data));
		if(null!=String.valueOf(searchParams.get("EQ_category"))&&!String.valueOf(searchParams.get("EQ_category")).equals("null")) {
			model.addAttribute("categoryName", dicProvider.getDicItemName(searchParams.get("EQ_category").toString()));
		}
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		
		return "other/index";
	}
	
	@RequestMapping("load")
	@ResponseBody
	public String load(OtherMediaSearchVo vo) {
		
		Map<String, Object> searchParams = Maps.newHashMap();
		
		if( StringUtils.isNotBlank(vo.getCategory()) ) {
			searchParams.put("EQ_category", vo.getCategory());
		}
		
		if( StringUtils.isNotBlank(vo.getRegions()) ) {
			Area area = areaService.get(vo.getRegions());
			String parentId = area.getParentId();
			if (Integer.parseInt(parentId) == 1)
				searchParams.put("IN_region", vo.getRegions());
			else {
				if (StringUtils.isNotBlank(parentId) && parentId.length() == 5
						&& Integer.parseInt(parentId) > 1) {
					ArrayList<String> regionsValues = new ArrayList<>();
					regionsValues.add(parentId);
					regionsValues.add(vo.getRegions());
					searchParams.put("IN_region", regionsValues);
				}
			}
		}
		
		if( StringUtils.isNotBlank(vo.getIndustryType()) ) {
			searchParams.put("LIKE_industryType", vo.getIndustryType());
		}
		
		if( StringUtils.isNotBlank(vo.getName()) ) {
			searchParams.put("LIKE_name", vo.getName());
		}
		
		String sort ="DESC_createTime";
		if( StringUtils.isNotBlank(vo.getSort()) ) {
			sort = vo.getSort();
		}
		
		JsonBean result = new JsonBean();
		try {
			Page<OtherMedia> data= otherMediaService.find(searchParams, vo.getPageNum(), vo.getPageSize(), sort);
			result.put("result", true);
			result.put("page", otherJsonBean(data,vo.getPageNum(),vo.getPageSize()));
		} catch (Exception e) {
			log.error("查询失败", e);
			result.put("result", false);
		}
		
		return result.toJson();
	}
	
	
	private Page<OtherMedia> parseShowPic(Page<OtherMedia> data){
		for(OtherMedia om:data.getContent()){
			om.setShowPic(FileServerUtils.getFileUrl(om.getShowPic()));
		}
		return data;
	}
	
	/**
	 * 设置公共的模型属性.
	 */
	protected void setModelAttribute(Model model) {
		model.addAttribute("regions", areaProvider.getProvinceMap().get(AreaProvider.HOT_CITIES_KEY));
		model.addAttribute("industryTypes", dicProvider.getDic(Constants.IndustryType.DIC_CODE).getDicItems());
		model.addAttribute("weixinCategories", dicProvider.getDic(Constants.WeixinCategory.DIC_CODE).getDicItems());
		model.addAttribute("weiboCategories", dicProvider.getDic(Constants.WeiboCategory.DIC_CODE).getDicItems());
		model.addAttribute("mediaTypes", dicProvider.getDic(Constants.MediaType.DIC_CODE).getDicItems());
		model.addAttribute("otherMediaCategories", dicProvider.getDic(Constants.OtherMediaCategory.DIC_CODE).getDicItems());
		model.addAttribute("recTags", getRecTags());
	}
	/**
	 * 媒体详情
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("view/{id}")
	public String view(@PathVariable String id, Model model) {
		model.addAttribute("active", "other-media");
		
		OtherMedia media = otherMediaService.get(id);
		model.addAttribute("media", media);
		setModelAttribute(model);
		return "other/view";
	}
	
	
	
	@RequestMapping(value="/createIntention", method=RequestMethod.POST)
	@Token
	private String create(@RequestParam("targetTab") String targetTab, @RequestParam("custName") String custName,
			@RequestParam("custPhone") String custPhone, @RequestParam("mediaId") String mediaId) {
		if(checkName(custName)&&isMobile(custPhone)){
			Intention intention = new Intention();
			intention.setCreateTime(new Date());
			OtherMedia media = new OtherMedia();
			media.setId(mediaId);
			intention.setTarget(media);
			intention.setTargetTab(targetTab);
			intention.setStatus(Constants.IntentionStatus.PENDING);
			intention.setCustName(custName);
			intention.setCustPhone(custPhone);
			intentionService.save(intention);
			return "other/intentionSuccess";
		}
		return "redirect:view/" + mediaId;
	}
	
	
	private JsonBean otherJsonBean(Page<OtherMedia> page,int pageNum,int pageSize) {
		JsonBean bean = new JsonBean();
		bean.put("total", page.getTotalElements());
		bean.put("totalPage", page.getTotalPages());
		bean.put("pageNum", pageNum);
		bean.put("pageSize", pageSize);
		bean.put("next", pageNum < page.getTotalPages());
		bean.put("previous", page.getTotalElements() > 0 && pageNum > 1);
		List<JsonBean> data = Lists.newArrayList();
		for( OtherMedia om : page.getContent() ) {
			data.add(toJsonBean(om));
		}
		bean.put("data", data);
		
		return bean;
	}
	private JsonBean toJsonBean(OtherMedia om) {
		JsonBean bean = new JsonBean();
		bean.put("id", om.getId());
		bean.put("name", om.getName());
		bean.put("showPic", FileServerUtils.getFileUrl(om.getShowPic()));
		
		return bean;
	}
	
	private List<MediaTag> getRecTags() {
		return mediaTagService.findRecTags();
	}
	
	/**
	 * 验证全中文名
	 * @param str
	 * @return
	 */
	public boolean checkName(String str){  
		for (int i = 0; i < str.length(); i++) {
			String s = str.substring(i, i+1);
			//生成一个Pattern,同时编译一个正则表达式.
			if(!java.util.regex.Pattern.matches("[\u4E00-\u9FA5]", s)){
				return false;
			}
		}
		return true;
	}
	
    /**
     * 验证手机号
     * @param str
     * @return
     */
    public boolean isMobile(String str) {   
        return java.util.regex.Pattern.matches("^[1][3,4,5,8][0-9]{9}$", str);
    } 
}

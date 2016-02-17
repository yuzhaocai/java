package com.lczy.media.controller;

import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springside.modules.web.Servlets;

import com.lczy.common.web.WebHelper;
import com.lczy.media.entity.OtherMedia;
import com.lczy.media.service.OtherMediaService;
import com.lczy.media.util.Constants;
import com.lczy.media.util.FileServerUtils;

/**
 * 
 * 官方媒体控制器.
 * 
 * @author wang.xiaoxiang
 *
 */
@Controller
@RequestMapping("/official")
public class OfficialMediaController extends MediaController{
	
	@Autowired
	private OtherMediaService otherMediaService;
	
	@RequestMapping({"", "weixin"})
	public String weixin(Model model) {
		//页面高亮显示“微博”导航菜单
		model.addAttribute("active", "official");
		model.addAttribute("categories", dicProvider.getDic(Constants.WeixinCategory.DIC_CODE).getDicItems());
		setModelAttribute(model);
		
		return "official/weixin";
	}
	@RequestMapping("weibo")
	public String weibo(Model model) {
		//页面高亮显示“微博”导航菜单
		model.addAttribute("active", "official");
		model.addAttribute("categories", dicProvider.getDic(Constants.WeiboCategory.DIC_CODE).getDicItems());
		setModelAttribute(model);
		
		return "official/weibo";
	}
	@RequestMapping("other")
	public String other(Model model, ServletRequest request) {
		//页面高亮显示“微博”导航菜单
		model.addAttribute("active", "official");
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
		
		
		String categoryCode = (String) searchParams.get("EQ_category");
		model.addAttribute("otherName", dicProvider.getDicItemName(categoryCode));
		setModelAttribute(model);
		model.addAttribute("data",  parseShowPic(data));
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		
		return "official/other";
	}
	private Page<OtherMedia> parseShowPic(Page<OtherMedia> data){
		for(OtherMedia om:data.getContent()){
			om.setShowPic(FileServerUtils.getFileUrl(om.getShowPic()));
		}
		return data;
	}
	
}

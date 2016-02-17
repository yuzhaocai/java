package com.lczy.media.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lczy.media.entity.MediaTag;
import com.lczy.media.service.MediaTagService;
import com.lczy.media.service.common.DicProvider;
import com.lczy.media.util.Constants;


@Controller
@RequestMapping("/helpCenter")
public class HelpCenterController {
	
	@Autowired
	private DicProvider dicProvider;
	
	@Autowired
	protected MediaTagService mediaTagService;
	
	@RequestMapping({"about"})
	public String about(Model model) {
		setModelAttribute(model);
		return "helpCenter/about";
	}
	@RequestMapping({"adsProcess"})
	public String adsProcess(Model model) {
		setModelAttribute(model);
		return "helpCenter/adsProcess";
	}
	@RequestMapping({"businessCooperation"})
	public String businessCooperation(Model model) {
		setModelAttribute(model);
		model.addAttribute("active", "join");
		return "helpCenter/businessCooperation";
	}
	@RequestMapping({"contact"})
	public String contact(Model model) {
		setModelAttribute(model);
		return "helpCenter/contact";
	}
	@RequestMapping({"enterprisesJoin"})
	public String enterprisesJoin(Model model) {
		setModelAttribute(model);
		return "helpCenter/enterprisesJoin";
	}
	@RequestMapping({"joinUs"})
	public String joinUs(Model model) {
		setModelAttribute(model);
		return "helpCenter/joinUs";
	}
	@RequestMapping({"meidiaJoin"})
	public String meidiaJoin(Model model) {
		setModelAttribute(model);
		return "helpCenter/meidiaJoin";
	}
	@RequestMapping({"memberRegister"})
	public String memberMyreq(Model model) {
		setModelAttribute(model);
		return "helpCenter/memberRegister";
	}
	@RequestMapping({"memberLevel"})
	public String memberLevel(Model model) {
		setModelAttribute(model);
		return "helpCenter/memberLevel";
	}
	@RequestMapping({"news"})
	public String news(Model model) {
		setModelAttribute(model);
		return "helpCenter/news";
	}
	@RequestMapping({"question"})
	public String question(Model model) {
		setModelAttribute(model);
		return "helpCenter/question";
	}
	@RequestMapping({"serviceTerms"})
	public String serviceTerms(Model model) {
		setModelAttribute(model);
		return "helpCenter/serviceTerms";
	}
	
	
	/**
	 * 设置公共的模型属性.
	 */
	protected void setModelAttribute(Model model) {
		model.addAttribute("active", "helpCenter");
		model.addAttribute("mediaTypes", dicProvider.getDic(Constants.MediaType.DIC_CODE).getDicItems());
		model.addAttribute("weixinCategories", dicProvider.getDic(Constants.WeixinCategory.DIC_CODE).getDicItems());
		model.addAttribute("weiboCategories", dicProvider.getDic(Constants.WeiboCategory.DIC_CODE).getDicItems());
		model.addAttribute("recTags", getTags());
		model.addAttribute("otherMediaCategories", dicProvider.getDic(Constants.OtherMediaCategory.DIC_CODE).getDicItems());
	}
	
	private List<MediaTag> getTags() {
		return mediaTagService.findRecTags();
	}
}

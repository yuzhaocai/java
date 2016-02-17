package com.lczy.media.controller.member;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.mapper.JsonMapper;

import com.google.common.collect.Lists;
import com.lczy.media.entity.Dic;
import com.lczy.media.entity.DicItem;
import com.lczy.media.entity.Requirement;
import com.lczy.media.service.RequirementService;
import com.lczy.media.service.common.AreaProvider;
import com.lczy.media.service.common.DicProvider;
import com.lczy.media.util.Constants;
import com.lczy.media.util.Constants.IndustryType;
import com.lczy.media.util.Constants.MediaType;
import com.lczy.media.util.FileServerUtils;
import com.lczy.media.util.JsonBean;

/**
 * 需求控制器.
 * 
 * @author wu
 *
 */
@Controller
@RequestMapping("/member/req")
public class ReqController {
	
	private static final Logger log = LoggerFactory.getLogger(ReqController.class);
	
	
	@Autowired
	private DicProvider dicProvider;
	
	@Autowired
	private RequirementService requirementService;
	
	@Autowired
	private AreaProvider areaProvider;
	
	
	/**
	 * 下载稿件.
	 */
	@RequestMapping("download/article/{fid}")
	public String downloadArticle(@PathVariable String fid) {
		
		String url = FileServerUtils.getFileUrl(fid);
		try {
			url += "?token=" + FileServerUtils.token();
		} catch (Exception e) {
			log.error("下载文件发生异常", e);
		}
		
		return "redirect:" + url;
	}
	
	/**
	 * 加载服务类型.
	 */
	@RequestMapping(value="serviceTypes")
	@ResponseBody
	public String serviceTypes(String mediaTypes) {
		
		List<JsonBean> serviceTypes;
		
		if( Constants.MediaType.WEIBO.equals(mediaTypes) ) {
			serviceTypes = getServiceOptions(Constants.WeiboService.DIC_CODE);
		} else if( Constants.MediaType.WEIXIN.equals(mediaTypes) ) {
			serviceTypes = getServiceOptions(Constants.WeixinService.DIC_CODE);
		} else {
			serviceTypes = Lists.newArrayList();
		}
		
		return JsonMapper.nonEmptyMapper().toJson(serviceTypes);
	}

	private List<JsonBean> getServiceOptions(String dicCode) {
		Dic dic = dicProvider.getDic(dicCode);
		List<JsonBean> options = Lists.newArrayList();
		if( dic != null ) {
			List<DicItem> items = dic.getDicItems();
			for( DicItem item : items ) {
				JsonBean opt = new JsonBean();
				opt.put("code", item.getItemCode());
				opt.put("name", item.getItemName());
				options.add(opt);
			}
		}
		return options;
	}
	
	@RequestMapping(value="advertiser/createReq")
	public String createReq(Model model, HttpServletRequest request) {
		setAttribute4Create(model, request);
		return "member/req/advertiser/createReq";
	}

	/**
	 * 设置创建需求页面需要的属性.
	 */
	private void setAttribute4Create(Model model, HttpServletRequest request) {
		model.addAttribute("mediaTypes", dicProvider.getDic(MediaType.DIC_CODE).getDicItems());
		model.addAttribute("industryTypes", dicProvider.getDic(IndustryType.DIC_CODE).getDicItems());
	}
	
	/**
	 * 查看需求详情
	 */
	@RequestMapping(value = "/view")
	public String view(String id, Model model){
		Requirement requirement = requirementService.get(id);
		requirement.setRegions(areaProvider.getAreaNames(Arrays.asList(requirement.getRegions().split(","))));
		
		model.addAttribute("req", requirement);
		return "/member/order/advertiser/view";
	}

}

/**
 * 
 */
package com.lczy.media.controller.member;

import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.google.common.collect.Maps;
import com.lczy.common.web.WebHelper;
import com.lczy.media.entity.SiteMessage;
import com.lczy.media.service.SiteMessageService;
import com.lczy.media.util.UserContext;

/**
 * 通知中心控制器.
 * 
 * @author wu
 *
 */
@Controller
@RequestMapping("/member/message")
public class SiteMessageController {
	
	@Autowired
	private SiteMessageService siteMessageService;
	
	@RequestMapping({"", "list"})
	public String list(Model model, HttpServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		
		int page = WebHelper.getPage(request);
		int size = WebHelper.getPageSize(request);
		
		setDefault(searchParams);
		
		String sort = getSortRoles(request);
		
		Page<SiteMessage> aPage= siteMessageService.find(searchParams, page, size, sort);
		
		setForPaging(searchParams, model, aPage, sort);
		
		return "member/message/list";
	}

	/**
	 * 设置用于分页的模型属性.
	 */
	private void setForPaging(Map<String, Object> searchParams, Model model,
			Page<SiteMessage> aPage, String sort) {
		model.addAttribute("data", aPage);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		model.addAttribute("sort", sort);
	}
	
	/**
	 * 设置默认的查询条件.
	 */
	private void setDefault(Map<String, Object> searchParams) {
		if( searchParams.get("EQ_readFlag") == null ) {
			searchParams.put("EQ_readFlag", "0");
		}
		
		//只能查看自己的通知
		searchParams.put("EQ_receiver.id", UserContext.getCurrent().getCustomer().getId());
	}

	/**
	 * 计算排序规则.
	 */
	private String getSortRoles(HttpServletRequest request) {
		String sort = WebHelper.getString(request, "sort");
		if (StringUtils.isBlank(sort)) {
			sort = "DESC_createTime";
		}
		return sort;
	}
	
	@RequestMapping("loadBadge")
	@ResponseBody
	public int loadBadge() {
		Map<String, Object> searchParams = Maps.newHashMap();
		setDefault(searchParams);
		searchParams.put("EQ_readFlag", "0");
		
		int count = siteMessageService.countBy(searchParams);
		
		return count;
	}
	
	/**
	 * 更新消息为'已读'.
	 * 
	 * @param ids 以逗号分隔的id列表.
	 */
	@RequestMapping("setReadFlag")
	public String setReadFlag(String ids) {
		if( StringUtils.isNotBlank(ids) ) {
			siteMessageService.setReadFlag(ids.split(","));
		}
		return "redirect:/member/message";
	}

}

package com.lczy.media.controller.admin;

import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;

import com.lczy.common.web.WebHelper;
import com.lczy.media.entity.Requirement;
import com.lczy.media.service.RequirementService;
import com.lczy.media.util.Constants;

/**
 * 需求审核 controller
 * @author wanghaibin
 *
 */
@Controller
@RequestMapping(value = "/admin/audit/req")
public class AuditReqController {

	private static Logger log = LoggerFactory.getLogger(AuditReqController.class);
	
	@Autowired
	private RequirementService requirementService;
	
	/**
	 * 需求审核 - 首页
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "")
	public String index(Model model, ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		
		int page = WebHelper.getPage(request);
		int size = WebHelper.getPageSize(request);
		
		String sort = WebHelper.getString(request, "sort");
		if (StringUtils.isBlank(sort)) {
			sort = "DESC_createTime";
		}
		searchParams.put("EQ_status", Constants.ReqStatus.AUDIT);
		searchParams.put("EQ_hasArticle", true);
		
		Page<Requirement> data= requirementService.find(searchParams, page, size, sort);
		
		model.addAttribute("data", data);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		
		return "admin/audit/req/index";
	}
	
	/**
	 * 需求审核 - 通过
	 * 
	 * @param ids
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "pass", method = RequestMethod.POST)
	public String pass(String[] ids, RedirectAttributes redirectAttributes) {
		if (ids != null) {
			for (String id : ids) {
				log.info("requirement to pass: {}", id);
				requirementService.auditPass(id);
			}
		}
		
		redirectAttributes.addFlashAttribute("message", "需求审核通过成功!");
		
		return "redirect:/admin/audit/req";
	}
	
	/**
	 * 需求审核 - 屏蔽
	 * 
	 * @param ids
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "reject", method = RequestMethod.POST)
	public String reject(String[] ids, RedirectAttributes redirectAttributes) {
		if (ids != null) {
			for (String id : ids) {
				log.info("requirement to reject: {}", id);
				//需求审核时，状态为AUDIT
				requirementService.setStatus(id,Constants.MediaStatus.AUDIT);
			}
		}
		
		redirectAttributes.addFlashAttribute("message", "需求审核屏蔽成功!");
		
		return "redirect:/admin/audit/req";
	}
	
}

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
import com.lczy.media.util.Constants.ReqStatus;

/**
 * 撰稿需求审核 controller
 * @author wanghaibin
 *
 */
@Controller
@RequestMapping(value = "/admin/audit/write")
public class AuditWriteController {

	private static Logger log = LoggerFactory.getLogger(AuditWriteController.class);
	
	@Autowired
	private RequirementService requirementService;
	
	/**
	 * 撰稿需求审核 - 首页
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
		//状态
		String status = String.valueOf(searchParams.get("EQ_status"));
		if (StringUtils.isNotBlank(status)) {
			if(status.equals("1")){
				//转稿需求待处理状态
				searchParams.put("EQ_status", ReqStatus.PENDING);
				searchParams.put("EQ_isFinished", false);
			}else if(status.equals("2")){
				///撰稿需求完成状态
				searchParams.remove("EQ_status");
				searchParams.put("EQ_isFinished", true);
			}else if(status.equals("null")){
				//通过String.valueOf()转换null为"null"
				searchParams.remove("EQ_status");
			}
		}
		
		searchParams.put("EQ_hasArticle", false);
//		searchParams.put("EQ_isFinished", false);
		
		Page<Requirement> data= requirementService.find(searchParams, page, size, sort);
		
		model.addAttribute("data", data);
		
		//重新赋值 status
		if (StringUtils.isNotBlank(status)&&!status.equals("null")) {
			searchParams.put("EQ_status",status);
		}
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		
		return "admin/audit/write/index";
	}
	
	/**
	 * 撰稿需求审核 - 处理
	 * 
	 * @param ids
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "finish", method = RequestMethod.POST)
	public String finish(String[] ids, RedirectAttributes redirectAttributes) {
		if (ids != null) {
			for (String id : ids) {
				log.info("requirement to pass: {}", id);
				requirementService.finish(id);
			}
		}
		
		redirectAttributes.addFlashAttribute("message", "撰稿需求处理成功!");
		
		return "redirect:/admin/audit/write";
	}
	
}

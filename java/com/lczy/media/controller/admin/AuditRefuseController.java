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
import com.lczy.media.entity.Complaint;
import com.lczy.media.service.ComplaintService;
import com.lczy.media.util.Constants;

/**
 * 拒付审核 controller
 * @author wanghaibin
 *
 */
@Controller
@RequestMapping(value = "/admin/audit/refuse")
public class AuditRefuseController {

	private static Logger log = LoggerFactory.getLogger(AuditRefuseController.class);
	
	@Autowired
	private ComplaintService complaintService;
	
	/**
	 * 拒付审核 - 首页
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
		searchParams.put("EQ_type", Constants.ComplaintType.REJECT);
		searchParams.put("EQ_handleResult", Constants.HandleResult.CREATED);
		
		Page<Complaint> data= complaintService.find(searchParams, page, size, sort);
		
		model.addAttribute("data", data);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		
		return "admin/audit/refuse/index";
	}
	
	/**
	 * 拒付审核 - 通过
	 * 
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "pass", method = RequestMethod.POST)
	public String pass(String id, RedirectAttributes redirectAttributes) {
		log.info("refuse to pass: {}", id);
		complaintService.auditPass(id);
		
		redirectAttributes.addFlashAttribute("message", "拒付审核通过成功!");
		
		return "redirect:/admin/audit/refuse";
	}
	
	/**
	 * 拒付审核 - 强制支付
	 * 
	 * @param id
	 * @param comment
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "reject", method = RequestMethod.POST)
	public String reject(String id, String comment, RedirectAttributes redirectAttributes) {
		log.info("refuse to reject: {}", id);
		complaintService.auditReject(id, comment);
		
		redirectAttributes.addFlashAttribute("message", "拒付审核强制支付成功!");
		
		return "redirect:/admin/audit/refuse";
	}
	
}

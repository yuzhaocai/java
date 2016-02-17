package com.lczy.media.controller.admin;

import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
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
import com.lczy.media.entity.Loan;
import com.lczy.media.service.LoanService;
import com.lczy.media.util.Constants;

/**
 * 垫资审核 controller
 * 
 * @author wanghaibin
 *
 */
@Controller
@RequestMapping(value = "/admin/order/loan")
public class AdminLoanController {

	private static Logger log = LoggerFactory.getLogger(AdminLoanController.class);
	
	@Autowired
	private LoanService loanService;
	
	/**
	 * 订单查询 - 首页
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
		
		String startTime = (String)searchParams.get("GTE_createTime");
		String endTime = (String)searchParams.get("LTE_createTime");
		if (StringUtils.isNotBlank(startTime)) {
			DateTime start = DateTime.parse(startTime + "T00:00:00");
			searchParams.put("GTE_createTime", start.toDate());
		}
		if (StringUtils.isNotBlank(endTime)) {
			DateTime end = DateTime.parse(endTime + "T23:59:59");
			searchParams.put("LTE_createTime", end.toDate());
		}
		
		searchParams.put("EQ_status", Constants.LoanStatus.AUDIT);
		
		Page<Loan> data= loanService.find(searchParams, page, size, sort);
		
		model.addAttribute("data", data);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		
		return "admin/order/loan/index";
	}
	
	/**
	 * 垫资审核 - 通过
	 * 
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "approve", method = RequestMethod.POST)
	public String approve(String id, RedirectAttributes redirectAttributes) {
		log.info("loan to approve: {}", id);
		try {
			loanService.approve(id);
			redirectAttributes.addFlashAttribute("message", "垫资审核成功!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", "垫资审核失败：" + e.getMessage());
		}
		
		return "redirect:/admin/order/loan";
	}
	
	/**
	 * 垫资审核 - 驳回
	 * 
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "reject", method = RequestMethod.POST)
	public String reject(String id, RedirectAttributes redirectAttributes) {
		log.info("loan to approve: {}", id);
		try {
			loanService.reject(id);
			redirectAttributes.addFlashAttribute("message", "垫资审核成功!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", "垫资审核失败：" + e.getMessage());
		}
		
		return "redirect:/admin/order/loan";
	}
	
}

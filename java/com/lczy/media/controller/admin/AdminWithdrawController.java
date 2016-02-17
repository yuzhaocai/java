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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;

import com.lczy.common.web.WebHelper;
import com.lczy.media.entity.WithdrawLog;
import com.lczy.media.service.WithdrawLogService;

/**
 * 提现申请审核 controller
 * 
 * @author wanghaibin
 *
 */
@Controller
@RequestMapping(value = "/admin/withdraw")
public class AdminWithdrawController {

	private static Logger log = LoggerFactory.getLogger(AdminWithdrawController.class);
	
	@Autowired
	private WithdrawLogService withdrawLogService;
	
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
		
		Page<WithdrawLog> data= withdrawLogService.find(searchParams, page, size, sort);
		
		model.addAttribute("data", data);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		
		return "admin/withdraw/index";
	}
	
	/**
	 * 提现审核
	 * 
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "audit/{id}", method = RequestMethod.POST)
	public String audit(@PathVariable String id, RedirectAttributes redirectAttributes) {
		log.info("withdraw to audit: {}", id);
		try {
			withdrawLogService.audit(id);
			redirectAttributes.addFlashAttribute("message", "提现审核成功!");
		} catch(Exception e) {
			redirectAttributes.addFlashAttribute("message", "提现审核失败:" + e.getMessage());
		}
		
		return "redirect:/admin/withdraw";
	}
	
}

package com.lczy.media.controller.admin;

import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springside.modules.web.Servlets;

import com.lczy.common.web.WebHelper;
import com.lczy.media.entity.Transaction;
import com.lczy.media.service.TransactionService;

/**
 * 后台充值记录 controller
 * 
 * @author wanghaibin
 *
 */
@Controller
@RequestMapping(value = "/admin/transaction")
public class AdminTransactionController {

	@Autowired
	private TransactionService transactionService;
	
	/**
	 * 后台交易流水查询 - 首页
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
		
		Page<Transaction> data= transactionService.find(searchParams, page, size, sort);
		
		model.addAttribute("data", data);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		
		return "admin/transaction/index";
	}
	
	
}

package com.lczy.media.controller.admin;

import java.util.List;
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
import com.lczy.media.entity.DicItem;
import com.lczy.media.entity.Invoice;
import com.lczy.media.service.DicService;
import com.lczy.media.service.InvoiceService;
import com.lczy.media.service.OrderService;
import com.lczy.media.util.Constants;

/**
 * 发票管理 controller
 * @author wanghaibin
 *
 */
@Controller
@RequestMapping(value = "/admin/order/invoice")
public class AdminInvoiceController {

	private static Logger log = LoggerFactory.getLogger(AdminInvoiceController.class);
	
	@Autowired
	private InvoiceService invoiceService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private DicService dicService;
	
	/**
	 * 发票管理 - 首页
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
		
		Page<Invoice> data= invoiceService.find(searchParams, page, size, sort);
		
		model.addAttribute("data", data);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		
		List<DicItem> invoiceStatus = dicService.findItemByCodeType(Constants.InvoiceStatus.DIC_CODE);
		model.addAttribute("invoiceStatus", invoiceStatus);
		return "admin/order/invoice/index";
	}
	
	/**
	 * 发票管理 - 确认收到
	 * 
	 * @param id
	 * @param tax
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "finish", method = RequestMethod.POST)
	public String finish(String id, RedirectAttributes redirectAttributes) {
		log.info("invoice to finish: {}", id);
		orderService.finish(id);
		redirectAttributes.addFlashAttribute("message", "操作成功!");
		
		return "redirect:/admin/order/invoice";
	}
	
}

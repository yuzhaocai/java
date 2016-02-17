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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;

import com.lczy.common.web.WebHelper;
import com.lczy.media.entity.Deliverable;
import com.lczy.media.entity.DicItem;
import com.lczy.media.entity.Order;
import com.lczy.media.service.DeliverableService;
import com.lczy.media.service.DicService;
import com.lczy.media.service.LoanService;
import com.lczy.media.service.OrderService;
import com.lczy.media.util.Constants;

/**
 * 订单查询 controller
 * @author wanghaibin
 *
 */
@Controller
@RequestMapping(value = "/admin/order/order")
public class AdminOrderController {

	private static Logger log = LoggerFactory.getLogger(AdminOrderController.class);
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private LoanService loanService;
	
	@Autowired
	private DicService dicService;
	
	@Autowired
	private DeliverableService deliverableService;
	
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
		
		Page<Order> data= orderService.find(searchParams, page, size, sort);
		
		model.addAttribute("data", data);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		
		List<DicItem> orderStatus = dicService.findItemByCodeType(Constants.OrderStatus.DIC_CODE);
		model.addAttribute("orderStatus", orderStatus);
		return "admin/order/order/index";
	}
	
	/**
	 * 订单查询 - 冻结
	 * 
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "freeze", method = RequestMethod.POST)
	public String freeze(String id, RedirectAttributes redirectAttributes) {
		log.info("requirement to freeze: {}", id);
//		orderService.auditReject(id);

		redirectAttributes.addFlashAttribute("message", "订单冻结成功!");
		
		return "redirect:/admin/order/order";
	}
	
	/**
	 * @param id 订单ID
	 * @return 验收页面.
	 */
	@RequestMapping(value="check")
	public String check(@RequestParam(required=true) String id, Model model) {
		
		Deliverable deliverable = deliverableService.findByOrderId(id);
		if( deliverable != null ) {
			model.addAttribute("deliverable", deliverable);
			model.addAttribute("pics", deliverable.getPics().split(";"));
		}
		return "admin/order/order/check";
	}
	
	@RequestMapping("/view")
	public String certificate(String pic,Model model) throws Exception {
		pic= pic.substring(pic.lastIndexOf("/")+1);
		model.addAttribute("pic", pic);
		return "admin/order/order/viewCheckModal";
	}
	
	/**
	 * 垫资申请
	 * 
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "loan", method = RequestMethod.POST)
	public String loan(String id, RedirectAttributes redirectAttributes) {
		try {
			loanService.apply(id);
			redirectAttributes.addFlashAttribute("message", "垫资申请成功!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", "垫资申请失败：" + e.getMessage());
		}
		
		return "redirect:/admin/order/order";
	}
	
}

/**
 * 
 */
package com.lczy.media.controller;

import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springside.modules.web.Servlets;

import com.lczy.common.web.WebHelper;
import com.lczy.media.controller.common.BaseController;
import com.lczy.media.entity.Account;
import com.lczy.media.entity.Customer;
import com.lczy.media.entity.Media;
import com.lczy.media.entity.MediaTag;
import com.lczy.media.entity.Order;
import com.lczy.media.entity.Transaction;
import com.lczy.media.service.AccountService;
import com.lczy.media.service.ChargeLogService;
import com.lczy.media.service.CustomerService;
import com.lczy.media.service.MediaService;
import com.lczy.media.service.MediaTagService;
import com.lczy.media.service.OrderService;
import com.lczy.media.service.TransactionService;
import com.lczy.media.service.common.AreaProvider;
import com.lczy.media.util.Constants;
import com.lczy.media.util.UserContext;

/**
 * @author wu
 *
 */
@Controller
@RequestMapping("/org")
public class OrganizationController  extends BaseController{
	
	@Autowired
	private MediaService mediaService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private AreaProvider areaProvider;
	
	@Autowired
	private MediaTagService mediaTagService;	
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private CustomerService customerService;
	
	@RequestMapping({"", "/"})
	public String main(Model model) {
		
		return "redirect:/org/media";
	}
	
	@RequestMapping("media")
	public String media(Model model, HttpServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		
		searchParams.put("EQ_organization.id", UserContext.getCurrent().getCustomer().getId());
		
		int page = WebHelper.getPage(request);
		int size = WebHelper.getPageSize(request);
		
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
		
		String sort = WebHelper.getString(request, "sort");
		if (StringUtils.isBlank(sort)) {
			sort = "DESC_createTime";
		}
		
		Page<Media> aPage= mediaService.find(searchParams, page, size, sort);
		
		model.addAttribute("data", aPage);
		model.addAttribute("organizationId", UserContext.getCurrent().getCustomer().getId());
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		
		return "org/media";
	}
	
	
	@RequestMapping("order")
	public String order(Model model, HttpServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_media.organization.id", UserContext.getCurrent().getCustomer().getId());
		
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
		
		Page<Order> aPage= orderService.find(searchParams, page, size, sort);
		
		model.addAttribute("data", aPage);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		model.addAttribute("sort", sort);
		
		return "org/order";
	}
	
	/**媒体详情
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/mediaOperation/detail")
	public String detail(String id, Model model){
		Media media = mediaService.get(id);
		media.setRegion(areaProvider.getAreaNames(Arrays.asList(media
				.getRegion().split(","))));
		
		if(StringUtils.isNotBlank(media.getTags())){
			String[] mediaTagIds = media.getTags().split(",");
			String tagNames = "";
			for (String mediaTagId : mediaTagIds) {
				MediaTag temp = mediaTagService.get(mediaTagId);
				tagNames += temp.getName() + " ";
			}
			media.setTags(tagNames);
		}
		
		model.addAttribute("media", media);
		return "org/mediaOperation/detail";
	}
	
	/**解除监管
	 * @param mediaId
	 * @param organizationId
	 * @param model
	 * @return
	 */
	@RequestMapping("/mediaOperation/deregulation")
	public String deregulation(String mediaId,String organizationId, Model model){
		Media media = mediaService.get(mediaId);
		System.out.println(media.getOrganization().getId());
		if(null!=media&&media.getOrganization().getId().equals(organizationId)){
			media.setOrganization(null);
			mediaService.save(media);
		}
		return "redirect:/org/media";
	}
	

	
	/**资金流水
	 * @param id
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/mediaOperation/transaction")
	public String transaction(String id,String mediaName,Model model,HttpServletRequest request){
		int page = WebHelper.getPage(request);
		int size = WebHelper.getPageSize(request);
		
		String sort = getSort(request);

		Map<String, Object> searchParams = getSearchParams(request);
		
		//查询条件加入媒体id
		if(null!=id&&!id.equals("")){
			searchParams.put("EQ_order.media.id", id);
		}
		
		Page<Transaction> aPage = transactionService.find(searchParams, page, size, sort);
		setModalAttrsForPaging(model, searchParams, aPage, sort);
		if(aPage.getContent().size()>0){
			model.addAttribute("mediaName",aPage.getContent().get(0).getOrder().getMedia().getName());
		}else{
			model.addAttribute("mediaName",mediaName);
		}
		return "/org/mediaOperation/transaction";
	}
	
	
	/**订单详情
	 * @param id
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/mediaOperation/orderRecord")
	public String orderRecord(String id,String mediaName,Model model,HttpServletRequest request){
		int page = WebHelper.getPage(request);
		int size = WebHelper.getPageSize(request);

		String sort = getSort(request);
		Map<String, Object> searchParams = getSearchParams(request);

		//查询条件加入媒体id
		if(null!=id&&!id.equals("")){
			searchParams.put("EQ_media.id", id);
		}

		Page<Order> aPage = orderService.find(searchParams, page, size, sort);
		setModalAttrsForPaging(model, searchParams, aPage, sort);
		if(aPage.getContent().size()>0){
			model.addAttribute("mediaName",aPage.getContent().get(0).getMedia().getName());
		}else{
			model.addAttribute("mediaName",mediaName);
		}
		model.addAttribute("id",searchParams.get("EQ_media.id"));
		return "/org/mediaOperation/orderRecord";
	}
	/**媒体详情
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/mediaOperation/userInfo")
	public String userInfo(String id, Model model){
		Media media = mediaService.get(id);
		//会员信息
		Customer cust = customerService.get(media.getCustomer().getId());
		model.addAttribute("mediaName", media.getName());
		model.addAttribute("cust", cust);
		return "org/mediaOperation/userInfo";
	}

	
}

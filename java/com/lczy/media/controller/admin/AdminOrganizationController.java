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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;

import com.lczy.common.web.Token;
import com.lczy.common.web.Token.Type;
import com.lczy.common.web.WebHelper;
import com.lczy.media.entity.Customer;
import com.lczy.media.entity.DicItem;
import com.lczy.media.entity.Media;
import com.lczy.media.service.CustomerService;
import com.lczy.media.service.DicService;
import com.lczy.media.service.MediaService;
import com.lczy.media.service.UserService;
import com.lczy.media.util.Constants;
import com.lczy.media.vo.CustomerVO;

@Controller
@RequestMapping("/admin/media/organization")
public class AdminOrganizationController {
	
	private static Logger log = LoggerFactory.getLogger(AdminOrganizationController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private DicService dicService;
	
	@Autowired
	private MediaService mediaService;
	
	/**
	 * 媒体标签 - 首页
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value={"", "/"})
	public String index(ServletRequest request, Model model) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		
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
		
		searchParams.put("EQ_custType", Constants.CustType.CUST_ORG);
		
		Page<Customer> data= customerService.find(searchParams, page, size, sort);
		model.addAttribute("data", data);
		
		List<DicItem> organizationTypes = dicService.findItemByCodeType(Constants.OrganizationType.DIC_CODE);
		model.addAttribute("organizationTypes", organizationTypes);
		
		return "/admin/media/organization/index";
	}
	
	/**
	 * 机构 - 创建表单
	 * 
	 * @param model
	 * @return
	 */
	@Token
	@RequestMapping(value="create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("action", "create");
		List<DicItem> organizationTypes = dicService.findItemByCodeType(Constants.OrganizationType.DIC_CODE);
		model.addAttribute("organizationTypes", organizationTypes);
		return "/admin/media/organization/form";
	}
	
	/**
	 * 机构 - 创建
	 * 
	 * @param customer
	 * @param redirectAttributes
	 * @return
	 */
	@Token(Type.REMOVE)
	@RequestMapping(value="create", method = RequestMethod.POST)
	public String create(CustomerVO customer, RedirectAttributes redirectAttributes) {
		customer.setId(null);
		customer.setPassword(customer.getLoginName());
		customer.setMobPhone(customer.getLoginName());
		customerService.createOrganization(customer);
		redirectAttributes.addFlashAttribute("message", "创建成功!");
		return "redirect:/admin/media/organization/";
	}
	
	/**
	 * 机构 - 修改表单
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@Token
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable String id, Model model) {
		model.addAttribute("org", customerService.get(id));
		model.addAttribute("action", "update");
		List<DicItem> organizationTypes = dicService.findItemByCodeType(Constants.OrganizationType.DIC_CODE);
		model.addAttribute("organizationTypes", organizationTypes);
		return "/admin/media/organization/form";
	}
	
	/**
	 * 机构 - 修改
	 * 
	 * @param customer
	 * @param redirectAttributes
	 * @return
	 */
	@Token(Type.REMOVE)
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(CustomerVO customer,	RedirectAttributes redirectAttributes) {
		customerService.updateOrganization(customer);
		redirectAttributes.addFlashAttribute("message", "修改成功");
		
		return "redirect:/admin/media/organization/";
	}
	
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@ModelAttribute("preloadCusomer")
	public Customer getCustomer(@RequestParam(required = false) String id) {
		Customer customer = new Customer();
		if (StringUtils.isNotBlank(id)) {
			customer = customerService.get(id);
		}
		
		return customer;
	}	
	
	
	/**
	 * 机构 - 删除
	 * 
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="delete", method = RequestMethod.POST)
	public String delete(String id, RedirectAttributes redirectAttributes) {
		try {
			customerService.deleteOrganization(id);
			redirectAttributes.addFlashAttribute("message", "删除成功!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", "删除失败:" + e.getMessage());
			log.error(e.getMessage(), e);
		}
		return "redirect:/admin/media/organization/";
	}
	
	/**
	 * 机构 - 下属媒体
	 * 
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="subMedia", method = RequestMethod.POST)
	public String subMedia(String id,String name, Model model) {
		List<Media> subMedia=mediaService.subMedia(id);
		model.addAttribute("subMedia",subMedia);
		model.addAttribute("orgName",name);
		return "/admin/media/organization/subMedia";
	}
	
}

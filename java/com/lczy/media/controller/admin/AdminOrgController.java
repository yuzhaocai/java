/**
 * 
 */
package com.lczy.media.controller.admin;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
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
import com.lczy.media.entity.Customer;
import com.lczy.media.entity.User;
import com.lczy.media.service.CustomerService;
import com.lczy.media.service.UserService;
import com.lczy.media.util.Constants;
import com.lczy.media.util.UserContext;
import com.lczy.media.vo.CustomerVO;

/**
 * 组织机构管理 controller
 * @author wu
 *
 */
@Controller
@RequestMapping("/admin/org")
public class AdminOrgController {
	
	@Autowired
	private CustomerService customerService;

	@RequestMapping({"", "/", "/list"})
	public String list(Model model, HttpServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_custType", Constants.CustType.CUST_ORG);
		
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
			sort = "DESC_modifyTime";
		}
		
		Page<Customer> aPage= customerService.find(searchParams, page, size, sort);
		
		model.addAttribute("data", aPage);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		
		return "admin/org/list";
	}
	
	
	@RequestMapping(value="create", method = RequestMethod.GET)
	public String create(Model model, HttpServletRequest request) {
		
		return "admin/org/create";
	}
	
	
	@RequestMapping(value="create", method = RequestMethod.POST)
	public String doCreate(CustomerVO vo, RedirectAttributes redirectAttrs) {
		vo.setMobPhone(vo.getTelPhone());
		customerService.createOrganization(vo);
		
		redirectAttrs.addFlashAttribute("message", "创建组织『" + vo.getName() + "』成功！");
		
		return "redirect:/admin/org/list";
	}
	
	
	@Autowired
	private UserService userService;
	
	
	@RequestMapping(value="{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable String id, Model model, HttpServletRequest request) {
		Customer org = customerService.get(id);
		User user = userService.getByCustomerId(id);
		
		model.addAttribute("org", org);
		model.addAttribute("user", user);
		
		return "admin/org/edit";
	}
	
	
	@RequestMapping(value="{id}/edit", method = RequestMethod.POST)
	public String doEdit(CustomerVO vo, @PathVariable String id, RedirectAttributes redirectAttrs) {
		Customer org = customerService.get(id);
		org.setName(vo.getName());
		org.setTelPhone(vo.getTelPhone());
		org.setMobPhone(vo.getTelPhone());
		org.setLinkman(vo.getLinkman());
		org.setEmail(vo.getEmail());
		
		org.setModifyBy(UserContext.getCurrent().getId());
		org.setModifyTime(new Date());
		
		customerService.save(org);
		
		redirectAttrs.addFlashAttribute("message", "保存成功！");
		
		return "redirect:/admin/org/list";
	}
	
	
	@RequestMapping(value="{id}/delete", method = RequestMethod.POST)
	public String doDelete(@PathVariable String id, RedirectAttributes redirectAttrs) {
		
		Customer org = customerService.delete(id);
		
		redirectAttrs.addFlashAttribute("message", "删除组织『" + org.getName() + "』成功！");
		
		return "redirect:/admin/org/list";
	}
	
}

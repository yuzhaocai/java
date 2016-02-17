package com.lczy.media.controller.admin;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.lczy.media.entity.DicItem;
import com.lczy.media.entity.Media;
import com.lczy.media.entity.MediaTag;
import com.lczy.media.entity.ReqMedia;
import com.lczy.media.entity.Requirement;
import com.lczy.media.service.DicService;
import com.lczy.media.service.RequirementService;
import com.lczy.media.util.Constants;
import com.lczy.media.util.UserContext;

/**
 * 需求查询 controller
 * @author wanghaibin
 *
 */
@Controller
@RequestMapping(value = "/admin/order/req")
public class AdminReqController {

	private static Logger log = LoggerFactory.getLogger(AdminReqController.class);
	
	@Autowired
	private RequirementService requirementService;
	
	@Autowired
	private DicService dicService;
	
	/**
	 * 需求查询 - 首页
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
		//过滤未审核需求
		searchParams.put("NEQ_status", Constants.ReqStatus.AUDIT);
		
		Page<Requirement> data= requirementService.find(searchParams, page, size, sort);
		
		model.addAttribute("data", data);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		
		List<DicItem> mediaTypes = dicService.findItemByCodeType(Constants.MediaType.DIC_CODE);
		model.addAttribute("mediaTypes", mediaTypes);
		
		List<DicItem> reqStatus = dicService.findItemByCodeType(Constants.ReqStatus.DIC_CODE);
		//页面查询条件去掉 “未审核和待处理”状态
		reqStatus.remove(0);
		reqStatus.remove(2);
		model.addAttribute("reqStatus", reqStatus);
		return "admin/order/req/index";
	}
	
	/**
	 * 需求查询 - 屏蔽
	 * 
	 * @param id
	 * @param redirectAttributes
	 * @param status = REQ_S_DISABLED 开放
	 * @param status = REQ_S_NORMAL 屏蔽
	 * @return
	 */
	@RequestMapping(value = "setStatus", method = RequestMethod.POST)
	public String setStatus(String id, String status,RedirectAttributes redirectAttributes) {
		log.info("requirement to reject: {}", id);
		requirementService.setStatus(id,status);
		String msg ="";
		if(status.equals(Constants.ReqStatus.DISABLED)){
			msg="开放成功!";
		}else{
			msg="屏蔽成功!";
		}
		redirectAttributes.addFlashAttribute("message", msg);
		
		return "redirect:/admin/order/req";
	}
	
	/**
	 * 需求查询 - 修改表单
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@Token
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable String id, Model model) {
		model.addAttribute("requirement", requirementService.get(id));
		return "/admin/order/req/form";
	}
	
	/**
	 * 需求查询 - 修改
	 * 
	 * @param requirement
	 * @param redirectAttributes
	 * @return
	 */
	@Token(Type.REMOVE)
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@ModelAttribute("preload") Requirement requirement, RedirectAttributes redirectAttributes) {
		requirement.setModifyBy(UserContext.getCurrent().getId());
		requirement.setModifyTime(new Date());
		requirementService.save(requirement);
		redirectAttributes.addFlashAttribute("message", "修改成功");
		
		return "redirect:/admin/order/req";
	}
	
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@ModelAttribute("preload")
	public Requirement preload(@RequestParam(required = false) String id) {
		Requirement requirement = new Requirement();
		if (StringUtils.isNotBlank(id)) {
			return requirementService.get(id);
		}
		
		return requirement;
	}	
	
	/**
	 * 查看邀请的媒体列表
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="viewInvite")
	public String viewInvite(String id, Model model) {
		Requirement requirement = requirementService.get(id);
		Set<ReqMedia> reqMedias = requirement.getReqMedias();
		model.addAttribute("reqMedias", reqMedias);
		return "/admin/audit/req/viewInvite";
	}
	
	
}

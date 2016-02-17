package com.lczy.media.controller.admin;

import java.util.Date;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;

import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.lczy.common.web.Token;
import com.lczy.common.web.WebHelper;
import com.lczy.media.entity.Complaint;
import com.lczy.media.entity.Deliverable;
import com.lczy.media.entity.User;
import com.lczy.media.service.ComplaintService;
import com.lczy.media.service.DeliverableService;
import com.lczy.media.util.Constants;
import com.lczy.media.util.UserContext;

/**
 * 举报管理 controller
 * @author wanghaibin
 *
 */
@Controller
@RequestMapping(value = "/admin/audit/complaint")
public class AuditComplaintController {

	private static Logger log = LoggerFactory.getLogger(AuditComplaintController.class);
	
	@Autowired
	private ComplaintService complaintService;
	
	@Autowired
	private DeliverableService deliverableService;
	
	/**
	 * 举报管理 - 首页
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
		searchParams.put("EQ_type", Constants.ComplaintType.COMPLAINT);
		searchParams.put("EQ_handleResult", Constants.HandleResult.UNDEAL);
		
		Page<Complaint> data= complaintService.find(searchParams, page, size, sort);
		
		model.addAttribute("data", data);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		
		return "admin/audit/complaint/index";
	}
	
	/**
	 * 举报处理
	 * 
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "deal", method = RequestMethod.POST)
	public String deal(String id, String comment,
			RedirectAttributes redirectAttributes) {
		log.info("complaint to deal: {}", id);
		User user = UserContext.getCurrent();

		Complaint complaint = complaintService.get(id);
		complaint.setHandleComment(comment);
		complaint.setHandleTime(new Date());
		complaint.setHandler(user.getId());
		complaint.setHandleResult(Constants.HandleResult.DEAL);

		complaintService.save(complaint);
		redirectAttributes.addFlashAttribute("message", "处理成功!");

		return "redirect:/admin/audit/complaint";
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
		return "/admin/audit/complaint/check";
	}
	
	@RequestMapping("/view")
	public String view(String pic,Model model) throws Exception {
		pic= pic.substring(pic.lastIndexOf("/")+1);
		model.addAttribute("pic", pic);
		return "/admin/audit/complaint/viewCheckModal";
	}
}

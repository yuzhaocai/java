/**
 * 
 */
package com.lczy.media.controller.member;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lczy.common.util.MessageBean;
import com.lczy.common.web.WebHelper;
import com.lczy.media.controller.common.BaseController;
import com.lczy.media.entity.RefundLog;
import com.lczy.media.service.RefundLogService;
import com.lczy.media.util.UserContext;

/**
 * @author wu
 *
 */
@Controller
@RequestMapping("/member/refund")
public class RefundLogController extends BaseController {
	
	@Autowired
	private RefundLogService refundLogService;

	@RequestMapping({"", "list"})
	public String list(Model model, HttpServletRequest request) {
		int page = WebHelper.getPage(request);
		int size = WebHelper.getPageSize(request);
		
		String sort = getSort(request);
		
		Map<String, Object> searchParams = getSearchParams(request);
		//只能查看自己的
		searchParams.put("EQ_customer.id", UserContext.getCurrent().getCustomer().getId());
		searchParams.put("EQ_deleted", false);
		
		Page<RefundLog> aPage = refundLogService.find(searchParams, page, size, sort);
		
		setModalAttrsForPaging(model, searchParams, aPage, sort);
		
		return "member/refund/list";
		
	}
	
	@RequestMapping(value="delete", method = RequestMethod.POST)
	public String delete(String id, RedirectAttributes redirectAttrs) {
		
		refundLogService.delete(id);
		
		redirectAttrs.addFlashAttribute("message", new MessageBean(1, "操作成功！").toJSON());
		
		return "redirect:/member/refund/list";
		
	}

}

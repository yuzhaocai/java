/**
 * 
 */
package com.lczy.media.controller.member;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lczy.common.util.MessageBean;
import com.lczy.common.web.Token;
import com.lczy.common.web.WebHelper;
import com.lczy.common.web.Token.Type;
import com.lczy.media.entity.Complaint;
import com.lczy.media.entity.Customer;
import com.lczy.media.entity.Deliverable;
import com.lczy.media.entity.Invoice;
import com.lczy.media.entity.MediaCase;
import com.lczy.media.entity.Order;
import com.lczy.media.entity.OrderMessage;
import com.lczy.media.entity.User;
import com.lczy.media.service.ComplaintService;
import com.lczy.media.service.DeliverableService;
import com.lczy.media.service.MessageTemplate;
import com.lczy.media.service.MessageType;
import com.lczy.media.service.OrderMessageService;
import com.lczy.media.service.OrderService;
import com.lczy.media.service.SiteMessageService;
import com.lczy.media.service.UserService;
import com.lczy.media.service.common.DicProvider;
import com.lczy.media.util.Constants;
import com.lczy.media.util.Constants.ComplaintType;
import com.lczy.media.util.Constants.InvoiceType;
import com.lczy.media.util.Constants.OrderStatus;
import com.lczy.media.util.UserContext;

/**
 * 广告主的订单控制器.
 * 
 * @author wu
 *
 */
@Controller
@RequestMapping("/member/order/advertiser")
public class OrderAdvertiserController extends OrderController {
	
	private Logger log = LoggerFactory.getLogger(OrderAdvertiserController.class);
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private SiteMessageService siteMessageService;
	
	@Autowired
	private DicProvider dicProvider;
	
	@Autowired
	private DeliverableService deliverableService;
	
	@Autowired
	private ComplaintService complaintService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrderMessageService orderMessageService;

	/**
	 * 进行中的订单.
	 */
	@RequestMapping({"", "list"})
	public String list(Model model, HttpServletRequest request) {
		
		int page = WebHelper.getPage(request);
		int size = WebHelper.getPageSize(request);
		
		String sort = getSort(request);
		
		Map<String, Object> searchParams = getSearchParams(request);
		//只能查看自己的订单
		searchParams.put("EQ_reqOwner.id", UserContext.getCurrent().getCustomer().getId());
		searchParams.put("EQ_finished", false);
		
		Page<Order> aPage= orderService.find(searchParams, page, size, sort);
		
		setModalAttrsForPaging(model, searchParams, aPage, sort);
		
		model.addAttribute("types", dicProvider.getDic(InvoiceType.DIC_CODE).getDicItems());
		
		return "member/order/advertiser/list";
		
	}
	
	/**
	 * 已完成的订单.
	 */
	@RequestMapping("finished")
	public String finished(Model model, HttpServletRequest request) {
		
		int page = WebHelper.getPage(request);
		int size = WebHelper.getPageSize(request);
		
		String sort = getSort(request);
		
		Map<String, Object> searchParams = getSearchParams(request);
		//只能查看自己的订单
		searchParams.put("EQ_reqOwner.id", UserContext.getCurrent().getCustomer().getId());
		searchParams.put("EQ_finished", true);
		
		Page<Order> aPage= orderService.find(searchParams, page, size, sort);
		
		setModalAttrsForPaging(model, searchParams, aPage, sort);
		
		return "member/order/advertiser/finished";
		
	}
	
	/**
	 * 订单付款.
	 */
	@RequestMapping(value="pay", method = RequestMethod.POST)
	public String pay(@RequestParam(required=true)String id, 
			String needInvoice, Invoice invoice,
			RedirectAttributes redirectAttrs) {
		
		Order order = orderService.get(id);
		if( StringUtils.isNotBlank(needInvoice) ) {
			//需要开发发票
			order.setInvoice(1);
		}
		
		MessageBean bean = null;
		try {
			orderService.pay(order, invoice);
			bean = new MessageBean(1, "订单 "+ id + " 付款成功!");
		} catch (Exception e) {
			log.error("订单支付失败", e);
			bean = new MessageBean(0, e.getMessage());
		}
		
		redirectAttrs.addFlashAttribute("message", bean.toJSON());
		
		return "redirect:/member/order/advertiser";
	}
	
	/**
	 * 催单.
	 */
	@RequestMapping(value="remind", method = RequestMethod.POST)
	@ResponseBody
	public boolean remind(@RequestParam(required=true) String id, RedirectAttributes redirectAttrs) {
		Order order = orderService.get(id);
		Customer reqOwner = order.getReqOwner();
		String receiverId = order.getMediaOwner().getId();
		
		String content = MessageTemplate.get(
				MessageType.MEDIA_REMINDER.key(), 
				reqOwner.getName(), 
				order.getId());
		
		siteMessageService.send(siteMessageService.getSystemSender().getId(), receiverId, 
				MessageType.MEDIA_REMINDER, content);
		return true;
	}
	
	/**
	 * 拒付.
	 */
	@RequestMapping(value="refuse", method = RequestMethod.POST)
	public String refuse(@RequestParam(required=true) String id,
			@RequestParam(required=true) String refuseReason,
			RedirectAttributes redirectAttrs) {
		
		MessageBean bean = null;
		
		Order order = orderService.get(id);
		if( order.getStatus().equals(OrderStatus.ACCEPTANCE) ) {
			if (StringUtils.isBlank(order.getRefuseReason()))
				order.setStatus(OrderStatus.PROGRESS);
			else
				order.setStatus(OrderStatus.REFUSEPAY);
			
			order.setRefuseReason(refuseReason);
			orderService.save(order);
			
			if (order.getStatus().equals(Constants.OrderStatus.REFUSEPAY)) {
				Complaint complaint = new Complaint();
				complaint.setOrder(order);
				complaint.setReason(refuseReason);
				complaint.setType(ComplaintType.REJECT);
				complaint.setCreateTime(new Date());
				complaint.setCreateBy(UserContext.getCurrent().getId());
				complaint.setHandleResult(Constants.HandleResult.CREATED);
				complaintService.save(complaint);
			}
			
			Customer reqOwner = order.getReqOwner();
			String receiverId = order.getMediaOwner().getId();
			
			String content = MessageTemplate.get(
					MessageType.MEDIA_REJECTION.key(), 
					reqOwner.getName(), 
					order.getId(),
					refuseReason);
			
			siteMessageService.send(siteMessageService.getSystemSender().getId(), 
					receiverId, MessageType.MEDIA_REJECTION, content);
			
			bean = new MessageBean(1, "订单操作成功！");
		} else {
			String status = dicProvider.getDicItemName(order.getStatus());
			bean = new MessageBean(0, "当前订单状态为：" + status + ", 不允许执行此操作！");
		}
		
		redirectAttrs.addFlashAttribute("message", bean.toJSON());
		
		return "redirect:/member/order/advertiser";
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
		return "member/order/advertiser/check";
	}
	
	/**
	 * 举报.
	 */
	@RequestMapping(value="complaint")
	@ResponseBody
	public String complaint(@RequestParam(required=true) String id, 
			@RequestParam(required=true) String reason ) {
		MessageBean bean = null;
		String userId = UserContext.getCurrent().getId();
		Complaint complaint = complaintService.findByOrderId(id,userId);
		if( complaint == null) {
			Order order = orderService.get(id);
			complaint = new Complaint();
			complaint.setOrder(order);
			complaint.setReason(reason);
			complaint.setType(ComplaintType.COMPLAINT);
			complaint.setType(ComplaintType.COMPLAINT);
			complaint.setHandleResult(Constants.HandleResult.UNDEAL);
			complaint.setCreateTime(new Date());
			complaint.setCreateBy(userId);
			complaintService.save(complaint);
			
			bean = new MessageBean(1, "您已成功提交举报信息！");
		} else {
			bean = new MessageBean(0, "您已成功提交举报信息，请匆重复操作！");
		}
		return bean.toJSON();
	}
	
	@RequestMapping("/view")
	public String certificate(String pic,Model model) throws Exception {
		pic= pic.substring(pic.lastIndexOf("/")+1);
		model.addAttribute("pic", pic);
		return "member/order/advertiser/viewCheckModal";
	}
	
	/**
	 * 验收订单
	 */
	@RequestMapping(value="acceptance", method = RequestMethod.POST)
	public String orderAcceptance(@RequestParam(required=true) String id,RedirectAttributes redirectAttributes){
		Order order = orderService.get(id);
		MessageBean bean = null;
		if(order!=null){
			if(order.getStatus().equals(Constants.OrderStatus.DELIVERED)&&
					order.getReqOwner().getId().equals(UserContext.getCurrent().getCustomer().getId())){
				order.setStatus(Constants.OrderStatus.ACCEPTANCE);
				orderService.save(order);
				bean =  new MessageBean(1, "订单: "+order.getId()+"验收成功！");
			}else{
				bean =  new MessageBean(0, "订单: "+order.getId()+"验收失败！");
			}
		}else{
			bean =  new MessageBean(0, "未查询到订单: "+order.getId());
		}
		redirectAttributes.addFlashAttribute("message", bean.toJSON());
		return "redirect:/member/order/advertiser";
	}
}

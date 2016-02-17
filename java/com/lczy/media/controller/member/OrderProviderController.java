package com.lczy.media.controller.member;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lczy.common.util.MessageBean;
import com.lczy.common.web.Token;
import com.lczy.common.web.Token.Type;
import com.lczy.common.web.WebHelper;
import com.lczy.media.entity.Complaint;
import com.lczy.media.entity.Deliverable;
import com.lczy.media.entity.Order;
import com.lczy.media.service.ComplaintService;
import com.lczy.media.service.DeliverableService;
import com.lczy.media.service.MediaService;
import com.lczy.media.service.MessageTemplate;
import com.lczy.media.service.MessageType;
import com.lczy.media.service.OrderMessageService;
import com.lczy.media.service.OrderService;
import com.lczy.media.service.ReqMediaService;
import com.lczy.media.service.RequirementService;
import com.lczy.media.service.SiteMessageService;
import com.lczy.media.service.UserService;
import com.lczy.media.util.Constants;
import com.lczy.media.util.Constants.ComplaintType;
import com.lczy.media.util.FileServerUtils;
import com.lczy.media.util.UserContext;

/**
 * 媒体主订单控制器.
 * 
 * @author wu
 * 
 */
@Controller
@RequestMapping("/member/order/provider")
public class OrderProviderController extends OrderController{
	@Autowired
	private RequirementService requirementService;

	@Autowired
	private ReqMediaService reqMediaService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private DeliverableService deliverableService;
	
	@Autowired
	private SiteMessageService siteMessageService;
	
	@Autowired
	private ComplaintService complaintService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrderMessageService orderMessageService;
	
	@Autowired
	private MediaService mediaService;
	
	@RequestMapping({ "", "list" })
	public String list(Model model, HttpServletRequest request) {
		int page = WebHelper.getPage(request);
		int size = WebHelper.getPageSize(request);

		String sort = getSort(request);
		Map<String, Object> searchParams = getSearchParams(request);

		searchParams.put("EQ_mediaOwner.id", UserContext.getCurrent()
				.getCustomer().getId());
		searchParams.put("EQ_finished", false);

		Page<Order> aPage = orderService.find(searchParams, page, size, sort);
		setModalAttrsForPaging(model, searchParams, aPage, sort);
		return "member/order/provider/list";
	}
	
	@RequestMapping(value= "/deliver" )
	@Token
	public String deliver(Model model, String id) {

		model.addAttribute("order", orderService.get(id));
		return "member/order/provider/deliverableModal";
	}
	

	/**
	 * 新增交付单
	 */
	@RequestMapping(value = "deliverable", method = RequestMethod.POST)
	@Token(Type.REMOVE)
	public String deliverable(
			@RequestParam("images") MultipartFile[] images,
			Deliverable deliverable, String orderId) throws Exception {
		String customerId = UserContext.getCurrent().getCustomer().getId();
		Date now = new Date();
		Order order = orderService.get(orderId);
		String showPicUrl = "";// 媒体提交成稿图片
		if (images != null && images.length > 0) {
			for (int i = 0; i < images.length; i++) {
				MultipartFile img = null;
				String url = null;
				if (images[i].getSize() > 0) {
					img = images[i];
					String fileId = FileServerUtils.upload(null,
							img.getOriginalFilename(), img.getBytes(), false,
							"image");
					url = FileServerUtils.getFileUrl(fileId);
					url = url + ";";
					showPicUrl += url;
				}
			}
		}

		deliverable.setPics(showPicUrl);
		deliverable.setOrder(order);
		deliverable.setCreateBy(customerId);
		deliverable.setCreateTime(now);
		Deliverable deliver = deliverableService.save(deliverable);

		order.setDeliverable(deliver);
		order.setStatus(Constants.OrderStatus.DELIVERED);
		order.setModifyTime(now);
		order.setModifyBy(customerId);
		orderService.deliverable(order);
		return "redirect:/member/order/provider";
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
		searchParams.put("EQ_mediaOwner.id", UserContext.getCurrent().getCustomer().getId());
		searchParams.put("EQ_finished", true);
		
		Page<Order> aPage= orderService.find(searchParams, page, size, sort);
		
		setModalAttrsForPaging(model, searchParams, aPage, sort);
		
		return "member/order/provider/finished";
	}
	
	/**
	 * 催款
	 */
	@RequestMapping(value="remind", method = RequestMethod.POST)
	@ResponseBody
	public boolean remind(@RequestParam(required=true) String id, RedirectAttributes redirectAttrs) {
		Order order = orderService.get(id);
		String receiverId = order.getReqOwner().getId();
		String content = MessageTemplate.get(
				MessageType.ADVERTISER_REMINDER_PAY.key(), 
				order.getMedia().getName(), 
				order.getId());
		
		siteMessageService.send(siteMessageService.getSystemSender().getId(), 
				receiverId, MessageType.ADVERTISER_REMINDER_PAY, content);
		return true;
	}
	
	
	/**
	 * 举报
	 */
	@RequestMapping(value="complaint")
	@ResponseBody
	public String complaint(@RequestParam(required=true) String id, 
			@RequestParam(required=true) String reason ) {
		MessageBean bean = null;
		String userId = UserContext.getCurrent().getId();
		Complaint complaint = complaintService.findByOrderId(id,userId);
		Order order = orderService.get(id);
		if( complaint == null) {
			complaint = new Complaint();
			complaint.setOrder(order);
			complaint.setReason(reason);
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
	
}

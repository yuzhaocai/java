/**
 * 
 */
package com.lczy.media.mobile.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.lczy.common.util.MyGson;
import com.lczy.media.entity.Complaint;
import com.lczy.media.entity.Customer;
import com.lczy.media.entity.Deliverable;
import com.lczy.media.entity.Media;
import com.lczy.media.entity.Order;
import com.lczy.media.entity.ReqMedia;
import com.lczy.media.entity.Requirement;
import com.lczy.media.mobile.util.Request;
import com.lczy.media.mobile.util.Response;
import com.lczy.media.mobile.util.Token;
import com.lczy.media.mobile.util.TokenManager;
import com.lczy.media.service.ComplaintService;
import com.lczy.media.service.DeliverableService;
import com.lczy.media.service.MessageTemplate;
import com.lczy.media.service.MessageType;
import com.lczy.media.service.OrderService;
import com.lczy.media.service.SiteMessageService;
import com.lczy.media.service.common.AreaProvider;
import com.lczy.media.service.common.DicProvider;
import com.lczy.media.util.Constants;
import com.lczy.media.util.Constants.ComplaintType;
import com.lczy.media.util.Constants.InviteType;
import com.lczy.media.util.Constants.MediaFeedback;
import com.lczy.media.util.Constants.OrderStatus;
import com.lczy.media.util.JsonBean;

/**
 * 广告主订单接口.
 * 
 * @author zhang.cj
 * 
 */
@Controller
@RequestMapping("/mobile/mcOrder")
public class MobileAdvertiserOrderController {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	private Gson gson = MyGson.getInstance();

	@Autowired
	private TokenManager tokenManager;

	@Autowired
	private OrderService orderService;

	@Autowired
	private DeliverableService deliverableService;

	@Autowired
	private ComplaintService complaintService;
	
	@Autowired
	private DicProvider dicProvider;
	
	@Autowired
	private SiteMessageService siteMessageService;

	
	@Autowired
	private AreaProvider areaProvider;
	/**
	 * 验收订单 .
	 */
	@RequestMapping("checkOrder")
	@ResponseBody
	public Response checkOrder(HttpServletRequest httpRequest) {
		Response resp = null;
		try {
			Request request = Request.parseRequest(httpRequest);
			log.debug("checkOrder request: {}", request.getBodyContent());
			String orderId = request.getBodyAsString("order_id");
			Order order = orderService.get(orderId);
			Deliverable deliverable = deliverableService.findByOrderId(orderId);
			if (order != null) {
				resp = new Response();
				resp.addBody("t_order", toJson(order));
				if (deliverable != null)
					resp.addBody("t_deliverable", toJson(deliverable));
				else
					log.warn("未查询到订单对应的验收单：订单ID:{}", orderId);
			} else {
				log.warn("未查询到订单：订单ID:{}", orderId);
			}
		} catch (Exception e) {
			log.error("验收订单失败！", e);
			resp = new Response(900, "验收订单失败！");
		}
		log.debug("checkOrder response: {}", gson.toJson(resp));
		return resp;
	}
	/**
	 * 验收订单通过 .
	 */
	@RequestMapping("checkOrderPass")
	@ResponseBody
	public Response checkOrderPass(HttpServletRequest httpRequest) {
		Response resp = null;
		try {
			Request request = Request.parseRequest(httpRequest);
			log.debug("checkOrderPass request: {}", request.getBodyContent());
			String orderId = request.getBodyAsString("order_id");
			Order order = orderService.get(orderId);
			if (order != null) {
				order.setStatus(OrderStatus.ACCEPTANCE);
				orderService.save(order);
				resp = new Response();
				resp.addBody("t_order", toJson(order));
			} else {
				log.warn("未查询到订单：订单ID:{}", orderId);
			}
		} catch (Exception e) {
			log.error("验收订单失败！", e);
			resp = new Response(900, "验收订单失败！");
		}
		log.debug("checkOrderPass response: {}", gson.toJson(resp));
		return resp;
	}

	
	/**
	 * 订单拒绝付款接口.
	 * 
	 */
	@RequestMapping("refuseOrder")
	@ResponseBody
	public Response refuseOrder(HttpServletRequest httpRequest) {
		Response resp = null;
		try {
			Request request = Request.parseRequest(httpRequest);
			log.debug("refuseOrder request :{}", request);
			Token token = tokenManager.getToken(request.getToken());
			String orderId = request.getBodyAsString("order_id");
			String refuseReason = request.getBodyAsString("refuse_reason");
			Order order = orderService.get(orderId);
			if (order != null) {
				if (order.getStatus().equals(OrderStatus.ACCEPTANCE)) {
					if (StringUtils.isBlank(order.getRefuseReason()))
						order.setStatus(OrderStatus.PROGRESS);
					else
						order.setStatus(OrderStatus.REFUSEPAY);
					
					order.setRefuseReason(refuseReason);
					order = orderService.save(order);
					
					if (order.getStatus().equals(Constants.OrderStatus.REFUSEPAY)) {
						Complaint complaint = new Complaint();
						complaint.setOrder(order);
						complaint.setReason(refuseReason);
						complaint.setType(ComplaintType.REJECT);
						complaint.setCreateTime(new Date());
						complaint.setCreateBy(token.getUserId());
						complaint.setHandleResult(Constants.HandleResult.CREATED);
						complaintService.save(complaint);
					}
					
					String senderId = order.getReqOwner().getId();
					String receiverId = order.getMediaOwner().getId();
					
					String content = MessageTemplate.get(MessageType.MEDIA_REJECTION
							.key(), order.getRequirement().getName(), order
							.getReqOwner().getName(), refuseReason);
					
					siteMessageService.send(senderId, receiverId,
							MessageType.MEDIA_REJECTION, content);
					resp = new Response();
					resp.addBody("t_order", toJson(order));
				} else {
					String status = dicProvider.getDicItemName(order.getStatus());
					resp = new Response(900, "当前订单 :" + orderId + " 状态为：" + status
							+ ", 不允许执行拒付操作！");
				}
			} else {
				log.warn("未查询到订单：订单ID:{}", orderId);
			}
		} catch (Exception e) {
			log.error("订单拒绝付款失败！", e);
			resp = new Response(900, "订单拒绝付款失败！");
		}
		log.debug("refuseOrder response :{}", gson.toJson(resp));
		return resp;
	}
	
	private JsonBean toJson(Order order) {
		JsonBean bean = new JsonBean();
		bean.add("id", order.getId());
		Customer reqOwner = order.getReqOwner();
		if (reqOwner != null) {
			bean.add("req_owner_name", reqOwner.getName());
		}
		Customer mediaOwner = order.getMediaOwner();
		if (mediaOwner != null) {
			bean.add("media_owner_name", mediaOwner.getName());
		}
		Deliverable deleverable = order.getDeliverable();
		if (deleverable != null) {
			bean.add("deliver_id", deleverable.getId());
		}
		Media media = order.getMedia();
		if (media != null) {
			bean.add("media_name", media.getName());
		}
		Requirement requirement = order.getRequirement();
		if (requirement != null) {
			bean.add("t_requirement", reqToJson(requirement));
		}
		bean.add("quote_type", order.getQuoteType());
		bean.add("amount", order.getAmount());
		bean.add("status", order.getStatus());
		bean.add("create_by", order.getCreateBy());
		if (order.getCreateTime() != null) {
			bean.add("create_time",
					new DateTime(order.getCreateTime()).toString("yyyy-MM-dd"));
		}
		bean.add("modify_by", order.getModifyBy());
		;
		if (order.getModifyTime() != null) {
			bean.add("modify_time",
					new DateTime(order.getModifyTime()).toString("yyyy-MM-dd"));
		}
		bean.add("finished", order.isFinished());
		bean.add("refuse_reason", order.getRefuseReason());
		bean.add("invoice", order.getInvoice());
		return bean;
	}
	/**
	 * 将需求表集合转换为json数组
	 * @param List<Requirement> reqList
	 * @return JSONArray
	 */
	private JsonBean reqToJson(Requirement req) {
	    	JsonBean bean = new JsonBean();
			bean.add("id", req.getId());
			if(null!= req.getCustomer()){
				bean.add("cust_id", req.getCustomer().getId());
			}
			bean.add("name", req.getName());
			bean.add("summary", req.getSummary());
			if(null!= req.getStartTime()){
				bean.add("start_time", new DateTime(req.getStartTime()).toString("yyyy-MM-dd"));
			}
			if(null!= req.getEndTime()){
				bean.add("end_time", new DateTime(req.getEndTime()).toString("yyyy-MM-dd"));
			}
			if(null!= req.getDeadline()){
				bean.add("deadline", new DateTime(req.getDeadline()).toString("yyyy-MM-dd"));
			}
			bean.add("cert_img", req.getCertImg());
			bean.add("is_public", req.getIsPublic());
			bean.add("status", req.getStatus());
			bean.add("create_by", req.getCreateBy());
			if(null!= req.getCreateTime()){
				bean.add("create_time", new DateTime(req.getCreateTime()).toString("yyyy-MM-dd"));
			}
			bean.add("modify_by", req.getModifyBy());
			if(null!= req.getModifyTime()){
				bean.add("modify_time", new DateTime(req.getModifyTime()).toString("yyyy-MM-dd"));
			}
			bean.add("media_types", req.getMediaTypes());
			bean.add("regions", areaProvider.getAreaNames(Lists.newArrayList(req.getRegions().split(","))));
			bean.add("service_types", req.getServiceTypes());
			bean.add("industry_types", req.getIndustryTypes());
			bean.add("budget", req.getBudget());
			bean.add("invite_num", req.getInviteNum());
			bean.add("has_article", req.isHasArticle());
			bean.add("article", req.getArticle());
			bean.add("allow_change", req.isAllowChange());
			bean.add("article_matter", req.getArticleMatter());
			bean.add("article_price", req.getArticlePrice());
			bean.add("is_finished", req.getIsFinished());
			bean.add("is_deleted", req.isDeleted());
//			List<JsonBean> initiative = Lists.newArrayList();
//			List<JsonBean> askInitiative = Lists.newArrayList();
			int initiative = 0;
			int askInitiative = 0;
			if(req.getReqMedias().size()>0){
				for(ReqMedia reqMedia:req.getReqMedias()){
					if(MediaFeedback.ACCEPT.equalsIgnoreCase(reqMedia.getFbStatus()) 
							&& InviteType.PASSIVE.equalsIgnoreCase(reqMedia.getInviteType())){
						//应邀媒体
//						initiative.add(reqMediaToJson(reqMedia));
						initiative++;
					}else if(MediaFeedback.ACCEPT.equalsIgnoreCase(reqMedia.getFbStatus()) 
							&& InviteType.ACTIVE.equalsIgnoreCase(reqMedia.getInviteType())){
						//应征媒体
//						askInitiative.add(reqMediaToJson(reqMedia));
						askInitiative++;
					}
				}
			}
			bean.add("initiative", initiative);
			bean.add("ask_initiative", askInitiative);
	
			return bean;
	}
	private JsonBean toJson(Deliverable deliverable) {
		JsonBean bean = new JsonBean();
		bean.add("id", deliverable.getId());
		bean.add("order_id", deliverable.getOrder().getId());
		bean.add("create_time", new DateTime(deliverable.getCreateTime())
				.toString("yyyy-MM-dd"));
		bean.add("create_by", deliverable.getCreateBy());
		bean.add("url", deliverable.getUrl());
		bean.add("pics", deliverable.getPics());
		return bean;
	}

}

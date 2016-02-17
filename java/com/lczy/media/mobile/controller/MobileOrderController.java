/**
 * 
 */
package com.lczy.media.mobile.controller;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.lczy.common.util.MyGson;
import com.lczy.common.util.UUID;
import com.lczy.media.entity.Customer;
import com.lczy.media.entity.Deliverable;
import com.lczy.media.entity.Invoice;
import com.lczy.media.entity.Media;
import com.lczy.media.entity.Order;
import com.lczy.media.entity.OrderMessage;
import com.lczy.media.entity.ReqMedia;
import com.lczy.media.entity.Requirement;
import com.lczy.media.mobile.util.Request;
import com.lczy.media.mobile.util.Response;
import com.lczy.media.mobile.util.Token;
import com.lczy.media.mobile.util.TokenManager;
import com.lczy.media.service.DeliverableService;
import com.lczy.media.service.MessageTemplate;
import com.lczy.media.service.MessageType;
import com.lczy.media.service.OrderMessageService;
import com.lczy.media.service.OrderService;
import com.lczy.media.service.SiteMessageService;
import com.lczy.media.service.common.AreaProvider;
import com.lczy.media.util.Constants;
import com.lczy.media.util.FileServerUtils;
import com.lczy.media.util.JsonBean;
import com.lczy.media.util.Constants.InviteType;
import com.lczy.media.util.Constants.MediaFeedback;

/**
 * 订单相关网络接口.
 * 
 * @author wu
 *
 */
@SuppressWarnings("restriction")
@Controller
@RequestMapping("/mobile/mcOrder")
public class MobileOrderController {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private Gson gson = MyGson.getInstance();
	
	@Autowired
	private TokenManager tokenManager;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private OrderMessageService orderMessageService;
	
	@Autowired
	private DeliverableService deliverableService;
	
	@Autowired
	private SiteMessageService siteMessageService;
	
	
	@Autowired
	private AreaProvider areaProvider;
	
	/**
	 * 得到所有定单.
	 */
	@RequestMapping("getOrder")
	@ResponseBody
	public Response getOrder(HttpServletRequest httpRequest) {
		Response resp = null;
		try {
			Request request = Request.parseRequest(httpRequest);
			log.debug("order request: {}", request.getBodyContent());
			Token token = tokenManager.getToken(request.getToken());
			String custId = token.getCustId();
			String custType = token.getCustType();
			Map<String, Object> searchParams = Maps.newLinkedHashMap();
			if (Constants.CustType.CUST_ADV.equals(custType)) {
				searchParams.put("EQ_reqOwner.id", custId);
			} else if (Constants.CustType.CUST_PRO.equals(custType)) {
				searchParams.put("EQ_mediaOwner.id", custId);
			}
			String datatype = (String) request.getBodyAsString("datatype");
			if ("1".equals(datatype)) {
				searchParams.put("EQ_finished", false);
			} else if ("2".equals(datatype)) {
				searchParams.put("EQ_finished", true);
			}
			Collection<Order> orders = orderService.find(searchParams);
			List<JsonBean> datas = Lists.newArrayList();
			if (orders.size() > 0) {
				for (Order order : orders) {
					datas.add(toJson(order));
				}
			} else {
				log.warn("未查询到订单：客户ID:{}, 客户类型:{}", custId, custType);
			}
			resp = new Response();
			resp.addBody("t_order", datas);
		} catch (Exception e) {
			log.error("获取订单失败", e);
			resp = new Response(900, e.getMessage());
		}
		log.debug("order response: {}", gson.toJson(resp));
		return resp;
	}
	
	/**
	 * 支付定单.
	 */
	@RequestMapping("payOrder")
	@ResponseBody
	public Response payOrder(HttpServletRequest httpRequest) {
		Response resp = new Response();
		try {
			Request request = Request.parseRequest(httpRequest);
			log.debug("request: {}", request.getBodyContent());
			String orderId = request.getBodyAsString("order_id");
			String needInvoice = request.getBodyAsString("invoice");
			String invoiceType = request.getBodyAsString("invoice_type");
			String invoiceTitle = request.getBodyAsString("invoice_title");
			
			// 查询用户订单, 并构造 Response
			Order order = orderService.get(orderId);
			Invoice invoice = new Invoice();
			if( "1".equals(needInvoice) ) {
				//需要开发发票
				order.setInvoice(1);
				invoice.setType(invoiceType);
				invoice.setTitle(invoiceTitle);
			}
			
			orderService.pay(order, invoice);
			resp.addBody("t_order", toJson(order));
		} catch (Exception e) {
			log.error("订单支付失败", e);
			resp = new Response(900, e.getMessage());
		}
		
		log.debug("response: {}", gson.toJson(resp));
		return resp;
	}
	
	/** 得到订单消息
	 * @param body 请求的参数
	 * @return 返回结果
	 */
	@RequestMapping("getOrderMessage")
	@ResponseBody
	public Response getOrderMessage(HttpServletRequest httpRequest) {
		Response resp = null;
		try {
			Request request = Request.parseRequest(httpRequest);
			log.debug("orderMessage request: {}", request.getBodyContent());
			String orderId = request.getBodyAsString("order_id");
			Collection<OrderMessage> orderMessages = orderMessageService.findByOrderId(orderId);
			List<JsonBean> datas = Lists.newArrayList();
			if (orderMessages.size() > 0) {
				for (OrderMessage orderMessage : orderMessages) {
					datas.add(toJson(orderMessage));
				}
			} else {
				log.warn("未查询到订单消息：订单ID:{}", orderId);
			}
			resp = new Response();
			resp.addBody("t_order_message", datas);
		} catch (Exception e) {
			log.error("获取订单消息失败", e);
			resp = new Response(900, e.getMessage());
		}
		log.debug("orderMessage response: {}", gson.toJson(resp));
		return resp;
	}
	
	/** 交付任务
	 * @param body 请求信息
	 * @return 返回结果
	 */
	@RequestMapping("setDeliverable")
	@ResponseBody
	public Response setDeliverable(HttpServletRequest httpRequest) {
		Response resp = null;
		try {
			Request request = Request.parseRequest(httpRequest);
			log.debug("setDeliverable request: {}", request.getBodyContent());
			Token token = tokenManager.getToken(request.getToken());
			String custId = token.getCustId();
			Date now = new Date();
			String orderId = request.getBodyAsString("order_id");
			Deliverable deliverable = deliverableService.findByOrderId(orderId);
			if (null == deliverable) {
				deliverable = new Deliverable();
			}
			Order order = orderService.get(orderId);
			deliverable.setOrder(order);
			deliverable.setCreateTime(now);
			deliverable.setCreateBy(custId);
			String url = request.getBodyAsString("url");
			deliverable.setUrl(url);
			StringBuffer picsUrl = new StringBuffer();
			String file1 = request.getBodyAsString("file1");
			if (file1 != null) {
				String fileId = generateImage(file1);
				picsUrl.append(FileServerUtils.getFileUrl(fileId)).append(";");
			}
			String file2 = request.getBodyAsString("file2");
			if (file2 != null) {
				String fileId = generateImage(file2);
				picsUrl.append(FileServerUtils.getFileUrl(fileId)).append(";");
			}
			String file3 = request.getBodyAsString("file3");
			if (file3 != null) {
				String fileId = generateImage(file3);
				picsUrl.append(FileServerUtils.getFileUrl(fileId)).append(";");
			}
			String file4 = request.getBodyAsString("file4");
			if (file4 != null) {
				String fileId = generateImage(file4);
				picsUrl.append(FileServerUtils.getFileUrl(fileId)).append(";");
			}
//			if (picsUrl.length() > 0) {
			deliverable.setPics(picsUrl.toString());
//			} else {
//				log.warn("交付时没有图片");
//			}
			Deliverable deliver = deliverableService.save(deliverable);
			order.setDeliverable(deliver);
			order.setStatus(Constants.OrderStatus.DELIVERED);
			order.setModifyTime(now);
			order.setModifyBy(custId);
			Order resultOrder = orderService.save(order);
			resp = new Response();
			resp.addBody("t_order", toJson(resultOrder));
		} catch (Exception e) {
			log.error("交付任务失败", e);
			resp = new Response(900, e.getMessage());
		}
		log.debug("setDeliverable response: {}", gson.toJson(resp));
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
		if (deleverable != null){
			bean.add("deliver_id", deleverable.getId());
		}
		Media media = order.getMedia();
		if (media != null) {
			bean.add("media_name", media.getName());
		}
		Requirement requirement = order.getRequirement();
		if (requirement != null) {
			bean.add("t_requirement",reqToJson(requirement));
		}
		bean.add("quote_type", order.getQuoteType());
		bean.add("amount", order.getAmount());
		bean.add("status", order.getStatus());
		bean.add("create_by", order.getCreateBy());
		if (order.getCreateTime() != null) {
			bean.add("create_time", new DateTime(order.getCreateTime()).toString("yyyy-MM-dd"));
		}
		bean.add("modify_by", order.getModifyBy());;
		if (order.getModifyTime() != null) {
			bean.add("modify_time", new DateTime(order.getModifyTime()).toString("yyyy-MM-dd"));
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
	
	private JsonBean toJson(OrderMessage orderMessage) {
		JsonBean bean = new JsonBean();
		bean.add("id", orderMessage.getId());
		bean.add("order_id", orderMessage.getOrder().getId());
		if (orderMessage.getCreateTime() != null) {
			bean.add("create_time",   new DateTime(orderMessage.getCreateTime()).toString("yyyy-MM-dd"));
		}
		bean.add("create_by", orderMessage.getCreateBy());
		bean.add("speaker", orderMessage.getSpeaker());
		bean.add("content", orderMessage.getContent());
		bean.add("display", orderMessage.isDisplay());
		return bean;
	}
	
	/** 对字节数组字符串进行Base64解码并生成图片
	 * @param imgStr base64图片数据
	 * @param imgFilePath 生成的文件路径
	 * @return 返回文件fieldId
	 * @throws Exception 
	 */
	private String generateImage(String imgStr) throws Exception {
        if (imgStr == null) {
        	return null;
        }
        try {
            // Base64解码
            byte[] bytes = Base64.decodeBase64(imgStr);
            String name = UUID.get();
            return FileServerUtils.upload(null, name + ".jpg", bytes, false, "image", true);
        } catch (Exception e) {
        	throw e;
        }
    }
	
	/**
	 * 催单(广告主)
	 */
	@RequestMapping("promptOrder")
	@ResponseBody
	public Response promptOrder(HttpServletRequest httpRequest) {
		Response resp = null;
		try {
			Request request = Request.parseRequest(httpRequest);
			log.debug("request: {}", request.getBodyContent());
			String orderId = request.getBodyAsString("order_id");
			Token token = tokenManager.getToken(request.getToken());
			Order order = orderService.get(orderId);
			
			//判断客户端登录人是否为订单拥有者
			if(token.getCustId().equals(order.getReqOwner().getId())){
				String senderId = order.getReqOwner().getId();
				String receiverId = order.getMediaOwner().getId();
				
				String content = MessageTemplate.get(
						MessageType.MEDIA_REMINDER.key(), 
						order.getRequirement().getName(), 
						order.getReqOwner().getName());
				
				siteMessageService.send(senderId, receiverId, MessageType.MEDIA_REMINDER, content);
				resp = new Response();
			}else{
				resp = new Response(900,"您无权操作此订单:"+orderId);
			}
		} catch (Exception e) {
			log.error("发送催单消息失败", e);
			resp = new Response(900, "发送催单消息失败");
		}
		
		log.debug("response: {}", gson.toJson(resp));
		
		return resp;
	}
	
	
	/**
	 * 催款(媒体主)
	 */
	@RequestMapping("promptOrderMoney")
	@ResponseBody
	public Response promptOrderMoney(HttpServletRequest httpRequest) {
		Response resp = null;
		try {
			Request request = Request.parseRequest(httpRequest);
			log.debug("request: {}", request.getBodyContent());
			String orderId = request.getBodyAsString("order_id");
			Token token = tokenManager.getToken(request.getToken());
			Order order = orderService.get(orderId);
			//判断客户端登录人是否为订单媒体拥有者
			if(token.getCustId().equals(order.getMediaOwner().getId())){
				String senderId = order.getMediaOwner().getId();
				String receiverId = order.getReqOwner().getId();
				
				String content = MessageTemplate.get(
						MessageType.REMINDER_PAY.key(), 
						order.getRequirement().getName(), 
						order.getMedia().getName());
				
				siteMessageService.send(senderId, receiverId, MessageType.REMINDER_PAY, content);
				resp = new Response();
			}else{
				resp = new Response(900,"您无权操作此订单:"+orderId);
			}
		} catch (Exception e) {
			log.error("发送催款消息失败", e);
			resp = new Response(900, "发送催款消息失败");
		}
		
		log.debug("response: {}", gson.toJson(resp));
		
		return resp;
	}
}

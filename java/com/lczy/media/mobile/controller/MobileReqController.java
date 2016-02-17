package com.lczy.media.mobile.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.lczy.media.entity.Order;
import com.lczy.media.entity.ReqMedia;
import com.lczy.media.entity.Requirement;
import com.lczy.media.mobile.util.Request;
import com.lczy.media.mobile.util.Response;
import com.lczy.media.service.ReqMediaService;
import com.lczy.media.service.RequirementService;
import com.lczy.media.service.common.AreaProvider;
import com.lczy.media.util.Constants.InviteType;
import com.lczy.media.util.Constants.MediaFeedback;
import com.lczy.media.util.JsonBean;
import com.lczy.media.util.UserContext;

@Controller
@RequestMapping("/mobile/mcReq")
public class MobileReqController {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private Gson gson = MyGson.getInstance();
	
	@Autowired
	private RequirementService requirementService;
	
	@Autowired
	private ReqMediaService reqMediaService;
	
	@Autowired
	private AreaProvider areaProvider;
	/**
	 * 得到所有需求(广告主)
	 */
	@RequestMapping("getRequirement")
	@ResponseBody
	public Response getRequirement(HttpServletRequest httpRequest) {
		Response resp = new Response();
		try {
			Request request = Request.parseRequest(httpRequest);
			log.debug("request: {}", request.getBodyContent());
			String custId = request.getBodyAsString("cust_id");
			List<Requirement> reqList = requirementService.findByAdCustId(custId);
			List<JsonBean> jsonList = Lists.newArrayList();
			if(reqList.size()>0){
				for(Requirement req:reqList){
					jsonList.add(reqToJson(req));
				}
			}
			resp.addBody("t_requirement", jsonList);
		} catch (Exception e) {
			log.error("查询广告主需求失败", e);
			resp = new Response(900, "查询广告主需求失败");
		}
		
		log.debug("response: {}", gson.toJson(resp));
		
		return resp;
	}
	
	/**
	 * 删除需求
	 */
	@RequestMapping("delRequirement")
	@ResponseBody
	public Response delRequirement(HttpServletRequest httpRequest) {
		
		Request request = Request.parseRequest(httpRequest);
		log.debug("request: {}", request.getBodyContent());
		String reqId = request.getBodyAsString("req_id");
		
		Requirement req = requirementService.get(reqId);
		Response resp = check4Delete(req);
		if( resp == null) {
			try {
				requirementService.delete(req);
				resp = new Response();
			} catch(Exception e) {
				log.error("删除需求时发生异常", e);
				resp = new Response(900, "操作失败！");
			}
		}
		
		log.debug("response: {}", gson.toJson(resp));
		
		return resp;
	}
	
	
	/**
	 * 广告主设置需求状态
	 */
	@RequestMapping("setReqStatusByReq")
	@ResponseBody
	public Response setReqStatusByReq(HttpServletRequest httpRequest) {
		Response resp = new Response();
		Request request = Request.parseRequest(httpRequest);
		log.debug("request: {}", request.getBodyContent());
		String reqMediaId = request.getBodyAsString("req_media_id");
		String status = request.getBodyAsString("status");
		try{
			if(status.equals("0")||status.equals("0.0")){
				//生成订单
				Order order = reqMediaService.accept(reqMediaId,status);
				resp.addBody("t_order", orderToJson(order));
			}else if(status.equals("1")||status.equals("1.0")){
				//拒绝媒体
				reqMediaService.refuse(reqMediaId);
			}
		}catch(Exception e){
			log.error("设置需求状态时发生异常", e);
			resp = new Response(900, e.getMessage());
		}
		
		log.debug("response: {}", gson.toJson(resp));
		
		return resp;
	}

	/**
	 * 检查需求是否可删除.
	 * @param req 目标实体
	 * @return 如果可删除则返回 null，否则返回 Response 说明理由.
	 */
	private Response check4Delete(Requirement req) {
		Response resp = checkPermission(req);
		if( resp == null ) {
			if( req.getPassiveNum() > 0 ) {
				resp = new Response(900, "您不能删除已有‘应邀’媒体的需求");
			}
		}
		return resp;
	}
	
	
	/**
	 * 检查当前用户是否有权限操作实体.
	 * @param req 目标实体
	 * @return 有权限则返回 null，无权限则返回 Response 对象.
	 */
	private Response checkPermission(Requirement req) {
		Response resp = null;
		if( req == null) {
			resp = new Response(900, "需求不存在！");
		} else if( !hasPermission(req) ) {
			resp = new Response(900, "您无权操作此需求：" + req.getId());
		}
		return resp;
	}
	
	/**
	 * @return 是否有权限操作此需求.
	 */
	private boolean hasPermission(Requirement req) {
		return req.getCreateBy().equals(UserContext.getCurrent().getId());
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
	
	/**
	 * 将订单转换为json
	 * @param order
	 * @return JsonBean
	 */
	private JsonBean orderToJson(Order order){
		JsonBean bean = new JsonBean();
		bean.add("id", order.getId());
		bean.add("req_id", order.getRequirement().getId());
		if(null!= order.getReqOwner()){
			bean.add("req_owner", order.getReqOwner().getId());
		}
		bean.add("media_id", order.getMedia().getId());
		if(null!= order.getMediaOwner()){
			bean.add("media_owner", order.getMediaOwner().getId());
		}
		bean.add("quote_type", order.getQuoteType());
		bean.add("amount", order.getAmount());
		bean.add("status", order.getStatus());
		bean.add("refuse_reason", order.getRefuseReason());
		bean.add("finished", order.isFinished());
		bean.add("invoice", order.getInvoice());
		if(null!= order.getDeliverable()){
			bean.add("deliver_id", order.getDeliverable().getId());
		}
		bean.add("create_by", order.getCreateBy());
		if(null!= order.getCreateTime()){
			bean.add("create_time", new DateTime(order.getCreateTime()).toString("yyyy-MM-dd"));
		}
		bean.add("modify_by", order.getModifyBy());
		if(null!= order.getModifyTime()){
			bean.add("modify_time", new DateTime(order.getModifyTime()).toString("yyyy-MM-dd"));
		}
		return bean;
	}
}

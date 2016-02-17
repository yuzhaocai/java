/**
 * 
 */
package com.lczy.media.mobile.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.lczy.common.util.MyGson;
import com.lczy.media.entity.Customer;
import com.lczy.media.entity.Media;
import com.lczy.media.entity.ReqMedia;
import com.lczy.media.entity.Requirement;
import com.lczy.media.mobile.util.Request;
import com.lczy.media.mobile.util.Response;
import com.lczy.media.mobile.util.Token;
import com.lczy.media.mobile.util.TokenManager;
import com.lczy.media.service.MediaService;
import com.lczy.media.service.ReqMediaService;
import com.lczy.media.service.RequirementService;
import com.lczy.media.service.SiteMessageService;
import com.lczy.media.service.common.AreaProvider;
import com.lczy.media.util.Constants;
import com.lczy.media.util.Constants.InviteType;
import com.lczy.media.util.Constants.MediaFeedback;
import com.lczy.media.util.FileServerUtils;
import com.lczy.media.util.JsonBean;

/**
 * 应征单接口.
 * 
 * @author zhang.cj
 * 
 */
@Controller
@RequestMapping("/mobile/mcReq")
public class MobileReqMediaController {

	private Logger log = LoggerFactory
			.getLogger(MobileReqMediaController.class);

	private Gson gson = MyGson.getInstance();

	@Autowired
	private TokenManager tokenManager;

	@Autowired
	private SiteMessageService siteMessageService;

	@Autowired
	private MediaService mediaService;

	@Autowired
	private ReqMediaService reqMediaService;
	
	@Autowired
	private RequirementService requirementService;
	
	
	@Autowired
	private AreaProvider areaProvider;
	
	
	/**
	 * 广告主得到所有应征单.
	 */
	@RequestMapping("getReqMedia")
	@ResponseBody
	public Response getReqMedia(HttpServletRequest httpRequest) {

		Response resp = null;
		try {
			Request req = Request.parseRequest(httpRequest);
			log.debug("getReqMedia request: " + req.getBodyContent());
			String id = req.getBodyAsString("req_id");
			Map<String, Object> searchParams = Maps.newLinkedHashMap();
			searchParams.put("EQ_requirement.id", id);
			searchParams.put("EQ_fbStatus", Constants.MediaFeedback.ACCEPT);
			searchParams.put("EQ_cfStatus", Constants.AdverConfirm.NULL);
			List<ReqMedia> reqMediaList = reqMediaService.find(searchParams);
			List<JsonBean> datas = Lists.newArrayList();
			if (reqMediaList != null && reqMediaList.size() > 0) {
				for (ReqMedia reqMedia : reqMediaList) {
					datas.add(toJson(reqMedia));
				}
			} else {
				log.warn("未查询到需求对应的应征单：需求ID:{}", id);
			}
			resp = new Response();
			resp.addBody("t_req_media", datas);
		} catch (Exception e) {
			log.debug("获取所有应征单失败！");
			resp = new Response(900, "获取所有应征单失败！");
		}

		log.debug("getReqMedia response: " + gson.toJson(resp));
		
		return resp;
	}
	
	
	/**
	 * 媒体主得到所有待处理.
	 */
	@RequestMapping("getReqMediaByMedia")
	@ResponseBody
	public Response getReqMediaByMedia(HttpServletRequest httpRequest) {

		Response resp = null;
		try {
			Request req = Request.parseRequest(httpRequest);
			log.debug("getReqMediaByMedia request: " + req.getBodyContent());
			Token token = tokenManager.getToken(req.getToken());

			Map<String, Object> searchParams = Maps.newLinkedHashMap();
			searchParams.put("EQ_media.customer.id", token.getCustId());
			searchParams.put("EQ_fbStatus", Constants.MediaFeedback.NULL);
			List<ReqMedia> reqMediaList = reqMediaService.find(searchParams);
			List<JsonBean> datas = Lists.newArrayList();
			if (reqMediaList != null && reqMediaList.size() > 0) {
				for (ReqMedia reqMedia : reqMediaList) {
					datas.add(toJson(reqMedia));
				}
			} else {
				log.warn("未查询到媒体主对应的待处理单：客户ID:{}", token.getCustId());
			}
			resp = new Response();
			resp.addBody("t_req_media", datas);
		} catch (Exception e) {
			log.debug("获取所有待处理失败！");
			resp = new Response(900, "获取所有待处理失败！");
		}

		log.debug("getReqMediaByMedia response: " + gson.toJson(resp));

		return resp;
	}

	/**
	 * 媒体改稿应邀
	 */
	@RequestMapping("changeArticle")
	@ResponseBody
	public Response changeArticle(HttpServletRequest httpRequest) {

		Request req = Request.parseRequest(httpRequest);
		log.debug("changeArticle request: " + req.getBodyContent());
		String reqMediaId = req.getBodyAsString("req_media_id");
		String changedReason = req.getBodyAsString("changed_reason");
		Response resp = null;
		try {
			ReqMedia reqMedia = reqMediaService.findOne(reqMediaId);
			if (reqMedia != null) {
				reqMedia.setChangedReason(changedReason);
				reqMedia.setFbStatus(Constants.MediaFeedback.ACCEPT);
				reqMedia.setFbTime(new Date());
				reqMedia.setChanged(true);
				reqMedia = reqMediaService.update(reqMedia);
				resp = new Response();
				resp.addBody("t_req_media", toJson(reqMedia));
			}else{
				log.warn("未查询到媒体要改稿的应征单：应征单ID:{}", reqMediaId);
				resp = new Response(900, "未查询到媒体要改稿的应征单！");
			}
		} catch (Exception e) {
			log.debug("媒体改稿应邀失败！");
			resp = new Response(900, "媒体改稿应邀失败！");
		}

		log.debug("changeArticle response: " + gson.toJson(resp));
		
		return resp;
	}
	
	
	/**
	 * 媒体设置应邀单.
	 */
	@RequestMapping("setReqStatusByMedia")
	@ResponseBody
	public Response setReqStatusByMedia(HttpServletRequest httpRequest) {

		Request req = Request.parseRequest(httpRequest);
		log.debug("setReqStatusByMedia request: " + req.getBodyContent());
		String reqMediaId = req.getBodyAsString("req_media_id");
		String fbStatus = req.getBodyAsString("fb_status");
		String refuseReason = req.getBodyAsString("refuse_reason");
		Response resp = null;
		try {
			Date now = new Date();
			ReqMedia reqMedia = reqMediaService.findOne(reqMediaId);
			if (reqMedia != null) {
				if (Integer.parseInt(fbStatus) == 0) {
					// 媒体拒绝广告主邀请
					reqMedia.setFbStatus(Constants.MediaFeedback.REFUSE);
					reqMedia.setRefuseReason(refuseReason);
				} else {
					// 媒体接受广告主邀请
					reqMedia.setFbStatus(Constants.MediaFeedback.ACCEPT);
				}
				reqMedia.setFbTime(now);
				reqMedia = reqMediaService.update(reqMedia);
				resp = new Response();
				resp.addBody("t_req_media", toJson(reqMedia));
			} else {
				log.warn("未查询到媒体要设置的应征单：应征单ID:{}", reqMediaId);
			}
		} catch (Exception e) {
			log.debug("媒体设置应邀单失败！");
			resp = new Response(900, "媒体设置应邀单失败！");
		}

		log.debug("setReqStatusByMedia response: " + gson.toJson(resp));

		return resp;
	}

	
	
	private JsonBean toJson(ReqMedia reqMedia) {
		JsonBean bean = new JsonBean();
		bean.add("id", reqMedia.getId());
		if (reqMedia.getMedia() != null) {
			bean.add("t_media", mediaToJson(reqMedia.getMedia()));
		}
		if (reqMedia.getRequirement() != null) {
			bean.add("t_requirement", requirementToJson(reqMedia.getRequirement()));
		}
		bean.add("invite_type", reqMedia.getInviteType());
		bean.add("quote_type", reqMedia.getQuoteType());
		bean.add("price", reqMedia.getPrice());
		bean.add("fb_status", reqMedia.getFbStatus());
		bean.add("fb_time",
				new DateTime(reqMedia.getFbTime()).toString("yyyy-MM-dd"));
		bean.add("cf_status", reqMedia.getCfStatus());
		bean.add("cf_time",
				new DateTime(reqMedia.getCfTime()).toString("yyyy-MM-dd"));
		bean.add("refuse_reason", reqMedia.getRefuseReason());
		bean.add("changed", reqMedia.isChanged());
		if (StringUtils.isNotBlank(reqMedia.getChangedArticle()))
			bean.add("changed_article", reqMedia.getChangedArticle());
		bean.add("changed_reason", reqMedia.getChangedReason());
		bean.add("status", reqMedia.getStatus());
		bean.add("create_time",
				new DateTime(reqMedia.getCreateTime()).toString("yyyy-MM-dd"));

		return bean;
	}
	
	private JsonBean mediaToJson(Media media) {
		JsonBean bean = new JsonBean();
		bean.add("cust_name", media.getCustomer().getName());
		bean.add("id", media.getId());
		bean.add("cust_id", media.getCustomer().getId());
		Customer org = media.getOrganization();
		if (org != null) {
			bean.add("org_id", org.getId());
		}
		bean.add("name", media.getName());
		bean.add("description", media.getDescription());
		bean.add("show_pic", FileServerUtils.getFileUrl(media.getShowPic()));
		bean.add("qr_code", FileServerUtils.getFileUrl(media.getQrCode()));
		bean.add("media_type", media.getMediaType());
		bean.add("category", media.getCategory());
		bean.add("industry_type", media.getIndustryType());
		bean.add("region", areaProvider.getAreaNames(Lists.newArrayList(media.getRegion().split(","))));
		bean.add("products", media.getProducts());
		bean.add("tags", media.getTags());
		bean.add("fans", media.getFans());
		bean.add("fans_dir", media.getFansDir());
		bean.add("create_by", media.getCreateBy());
		bean.add("modify_by", media.getModifyBy());
		bean.add("status", media.getStatus());
		bean.add("provide_invoice", media.isProvideInvoice());
		bean.add("level", media.getLevel());
		bean.add("create_time",
				new DateTime(media.getCreateTime()).toString("yyyy-MM-dd"));
		bean.add("modify_time",
				new DateTime(media.getModifyTime()).toString("yyyy-MM-dd"));

		return bean;
	}

	private JsonBean requirementToJson(Requirement req) {
		JsonBean bean = new JsonBean();
		bean.add("cust_name", req.getCustomer().getName());
		bean.add("id", req.getId());
		if (null != req.getCustomer()) {
			bean.add("cust_id", req.getCustomer().getId());
		}
		bean.add("name", req.getName());
		bean.add("summary", req.getSummary());
		if (null != req.getStartTime()) {
			bean.add("start_time",
					new DateTime(req.getStartTime()).toString("yyyy-MM-dd"));
		}
		if (null != req.getEndTime()) {
			bean.add("end_time",
					new DateTime(req.getEndTime()).toString("yyyy-MM-dd"));
		}
		if (null != req.getDeadline()) {
			bean.add("deadline",
					new DateTime(req.getDeadline()).toString("yyyy-MM-dd"));
		}
		bean.add("cert_img", req.getCertImg());
		bean.add("is_public", req.getIsPublic());
		bean.add("status", req.getStatus());
		bean.add("create_by", req.getCreateBy());
		if (null != req.getCreateTime()) {
			bean.add("create_time",
					new DateTime(req.getCreateTime()).toString("yyyy-MM-dd"));
		}
		bean.add("modify_by", req.getModifyBy());
		if (null != req.getModifyTime()) {
			bean.add("modify_time",
					new DateTime(req.getModifyTime()).toString("yyyy-MM-dd"));
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
		int initiative = 0;
		int askInitiative = 0;
		if (req.getReqMedias().size() > 0) {
			for (ReqMedia reqMedia : req.getReqMedias()) {
				if (MediaFeedback.ACCEPT.equalsIgnoreCase(reqMedia
						.getFbStatus())
						&& InviteType.PASSIVE.equalsIgnoreCase(reqMedia
								.getInviteType())) {
					initiative++;
				} else if (MediaFeedback.ACCEPT.equalsIgnoreCase(reqMedia
						.getFbStatus())
						&& InviteType.ACTIVE.equalsIgnoreCase(reqMedia
								.getInviteType())) {
					askInitiative++;
				}
			}
		}
		bean.add("initiative", initiative);
		bean.add("ask_initiative", askInitiative);

		return bean;
	}
}

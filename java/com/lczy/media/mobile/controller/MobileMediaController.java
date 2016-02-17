/**
 * 
 */
package com.lczy.media.mobile.controller;

import java.util.HashMap;
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
import com.google.gson.Gson;
import com.lczy.common.util.MyGson;
import com.lczy.media.entity.Customer;
import com.lczy.media.entity.Media;
import com.lczy.media.entity.MediaCase;
import com.lczy.media.entity.MediaQuote;
import com.lczy.media.entity.ReqMedia;
import com.lczy.media.mobile.util.Request;
import com.lczy.media.mobile.util.Response;
import com.lczy.media.mobile.util.Token;
import com.lczy.media.mobile.util.TokenManager;
import com.lczy.media.service.MediaService;
import com.lczy.media.service.SiteMessageService;
import com.lczy.media.service.common.AreaProvider;
import com.lczy.media.util.Constants;
import com.lczy.media.util.FileServerUtils;
import com.lczy.media.util.JsonBean;

/**
 * 移动端媒体接口.
 * 
 * @author zhang.cj
 * 
 */
@Controller
@RequestMapping("/mobile/mcUser")
public class MobileMediaController {

	private Logger log = LoggerFactory.getLogger(MobileMediaController.class);

	private Gson gson = MyGson.getInstance();

	@Autowired
	private TokenManager tokenManager;

	@Autowired
	private SiteMessageService siteMessageService;

	@Autowired
	private MediaService mediaService;
	
	@Autowired
	private AreaProvider areaProvider;

	/**
	 * 得到我的媒体.
	 */
	@RequestMapping("myMedia")
	@ResponseBody
	public Response myMedia(HttpServletRequest httpRequest) {

		Request req = Request.parseRequest(httpRequest);
		log.debug("myMedia request: {}", req.getBodyContent());
		Token token = tokenManager.getToken(req.getToken());
		String custId = token.getCustId();
		Response resp = null;
		try {
			Map<String, Object> searchParams = new HashMap<String, Object>();
			searchParams.put("EQ_customer.id", custId);

			List<Media> mediaList = mediaService.find(searchParams);
			List<JsonBean> datas = Lists.newArrayList();
			if (mediaList != null && mediaList.size() > 0) {
				for (Media media : mediaList)
					datas.add(mediaToJson(media));
			} else {
				log.warn("未查询到该客户的媒体：客户ID:{}", custId);
			}
			resp = new Response();
			resp.addBody("t_media", datas);
		} catch (Exception e) {
			log.debug("获取我的媒体失败！");
			resp = new Response(900, "获取我的媒体失败！");
		}

		log.debug("myMedia response: " + gson.toJson(resp));
		return resp;
	}
	
	
	private JsonBean mediaToJson(Media media) {
		JsonBean bean = new JsonBean();
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
		
		
		bean.add("pending_num",getPendingNum(media));
		bean.add("mediaCases", getJsons("case", media));
		bean.add("mediaQuotes", getJsons("quote", media));
		return bean;
	}
	
	

	private int getPendingNum(Media media) {
		int res = 0;
		for (ReqMedia reqM : media.getReqMedias()) {
			if (reqM.getFbStatus().equals(Constants.MediaFeedback.NULL))
				res++;
		}
		return res;
	}
	
	private List<JsonBean> getJsons(String str, Media media) {
		List<JsonBean> jsonBean = Lists.newArrayList();
		if (str.equals("case"))
			for (MediaCase mediaCase : media.getMediaCases())
				jsonBean.add(mediaCaseToJson(mediaCase));
		else if (str.equals("quote"))
			for (MediaQuote mediaQuote : media.getMediaQuotes())
				jsonBean.add(mediaQuoteToJson(mediaQuote));

		return jsonBean;
	}
	
	private JsonBean mediaCaseToJson(MediaCase mediaCase) {
		JsonBean bean = new JsonBean();
		bean.add("id", mediaCase.getId());
		bean.add("media_id", mediaCase.getMedia().getId());
		bean.add("title", mediaCase.getTitle());
		bean.add("light", mediaCase.getLight());
		bean.add("content", mediaCase.getContent());
		bean.add("show_pic", FileServerUtils.getFileUrl(mediaCase.getShowPic()));
		bean.add("create_by", mediaCase.getCreateBy());
		bean.add("create_time",
				new DateTime(mediaCase.getCreateTime()).toString("yyyy-MM-dd"));
		bean.add("modify_by", mediaCase.getModifyBy());
		bean.add("modify_time",
				new DateTime(mediaCase.getModifyTime()).toString("yyyy-MM-dd"));
		return bean;
	}

	private JsonBean mediaQuoteToJson(MediaQuote quote) {
		JsonBean bean = new JsonBean();
		bean.add("id", quote.getId());
		bean.add("media_id", quote.getMedia().getId());
		bean.add("type", quote.getType());
		bean.add("price", quote.getPrice());
		bean.add("create_by", quote.getCreateBy());
		bean.add("create_time",
				new DateTime(quote.getCreateTime()).toString("yyyy-MM-dd"));
		bean.add("modify_by", quote.getModifyBy());
		bean.add("modify_time",
				new DateTime(quote.getModifyTime()).toString("yyyy-MM-dd"));
		return bean;
	}

}

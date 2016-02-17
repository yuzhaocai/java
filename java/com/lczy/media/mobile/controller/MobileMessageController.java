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
import com.lczy.media.entity.SiteMessage;
import com.lczy.media.mobile.util.Request;
import com.lczy.media.mobile.util.Response;
import com.lczy.media.mobile.util.Token;
import com.lczy.media.mobile.util.TokenManager;
import com.lczy.media.service.SiteMessageService;
import com.lczy.media.util.JsonBean;

/**
 * 移动端站内消息.
 * 
 * @author zhang.cj
 * 
 */
@Controller
@RequestMapping("/mobile/mcUser")
public class MobileMessageController {

	private Logger log = LoggerFactory.getLogger(MobileMessageController.class);

	private Gson gson = MyGson.getInstance();

	@Autowired
	private TokenManager tokenManager;

	@Autowired
	private SiteMessageService siteMessageService;

	/**
	 * 得到站内消息.
	 */
	@RequestMapping("getSiteMessage")
	@ResponseBody
	public Response getSiteMessage(HttpServletRequest httpRequest) {

		Request req = Request.parseRequest(httpRequest);
		log.debug("getSiteMessage request: {}", req.getBodyContent());
		Token token = tokenManager.getToken(req.getToken());
		String custId = token.getCustId();
		Response resp = null;
		try {
			Map<String, Object> searchParams = new HashMap<String, Object>();
			searchParams.put("EQ_receiver.id", custId);

			List<SiteMessage> messageList = siteMessageService
					.find(searchParams);
			List<JsonBean> datas = Lists.newArrayList();
			for (SiteMessage message : messageList)
				datas.add(toJson(message));
			resp = new Response();
			resp.addBody("t_site_message", datas);
		} catch (Exception e) {
			log.debug("获取站内消息失败！");
			resp = new Response(900, "获取站内消息失败！");
		}

		log.debug("getSiteMessage response: " + gson.toJson(resp));
		return resp;
	}



	/**
	 * 设置站内消息为已读.
	 */
	@RequestMapping("setMessageRead")
	@ResponseBody
	public Response setMessageRead(HttpServletRequest httpRequest) {
		Request req = Request.parseRequest(httpRequest);
		log.debug("setMessageRead request: " + req.getBodyContent());
		String messageIds = req.getBodyAsString("message_ids");
		Response resp = null;
		try {
			if (StringUtils.isNotBlank(messageIds)) {
				siteMessageService.setReadFlag(messageIds.split(","));
			}
			resp = new Response();
		} catch (Exception e) {
			log.debug("设置站内消息为已读失败！");
			resp = new Response(900, "设置站内消息为已读失败！");
		}

		log.debug("setMessageRead response: " + gson.toJson(resp));

		return resp;
	}
	
	
	private JsonBean toJson(SiteMessage message) {
		JsonBean bean = new JsonBean();
		bean.add("id", message.getId());
		bean.add("receiver", message.getReceiver().getId());
		bean.add("sender", message.getSender().getId());
		bean.add("type", message.getType());
		bean.add("title", message.getTitle());
		bean.add("content", message.getContent());
		bean.add("read_flag", message.getReadFlag());
		bean.add("create_time",
				new DateTime(message.getCreateTime()).toString("yyyy-MM-dd"));
		return bean;
	}
}

package com.lczy.media.mobile.controller;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.lczy.common.util.MessageBean;
import com.lczy.common.util.MyGson;
import com.lczy.media.controller.member.OrderController;
import com.lczy.media.entity.Order;
import com.lczy.media.entity.OrderMessage;
import com.lczy.media.entity.User;
import com.lczy.media.mobile.util.Request;
import com.lczy.media.mobile.util.Response;
import com.lczy.media.service.OrderMessageService;
import com.lczy.media.service.OrderService;
import com.lczy.media.service.UserService;
import com.lczy.media.util.Constants;
import com.lczy.media.util.JsonBean;
import com.lczy.media.util.UserContext;

@Controller
@RequestMapping("/mobile/mcOrder")
public class MobileOrderMessageController extends OrderController{
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private Gson gson = MyGson.getInstance();
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrderMessageService orderMessageService;
	
	/** 设置留言信息
	 */
	@RequestMapping("setOrderMessage")
	@ResponseBody
	public Response setOrderMessage(HttpServletRequest httpRequest) {
		Response resp = null;
		try {
			Request request = Request.parseRequest(httpRequest);
			log.debug("setOrderMessage request: {}", request.getBodyContent());
			String orderId = request.getBodyAsString("order_id");
			String message = request.getBodyAsString("message");
			String userId = UserContext.getCurrent().getId();
			//判断权限
			MessageBean bean = orderMessagePermission(orderId,customer4UserId(userId));
			//是否禁言
			bean = (bean == null) ? userShutup(userId) : bean;
			if ( bean == null ) {
				User user = UserContext.getCurrent();
				OrderMessage om = new OrderMessage();
				om.setOrder(orderService.get(orderId));
				if(user.getCustomer().getCustType().equals(Constants.CustType.CUST_PRO)){
					//媒体主
					om.setSpeaker(om.getOrder().getMedia().getName());
				}else if(user.getCustomer().getCustType().equals(Constants.CustType.CUST_ADV)){
					//广告主
					om.setSpeaker(user.getCustomer().getName());
				}
				om.setCreateBy(userId);
				om.setContent(message.trim());
				om.setCreateTime(new Date());
				om = orderMessageService.save(om);
				resp = new Response();
				resp.addBody("t_order_message", toJson(om));
			} else {
				resp = new Response(900, bean.getContent());
			}
		} catch (Exception e) {
			log.error("留言失败", e);
			resp = new Response(900, e.getMessage());
		}
		log.debug("setOrderMessage response: {}", gson.toJson(resp));
		return resp;
	}
	
	
	private JsonBean toJson(OrderMessage om) {
		JsonBean bean = new JsonBean();
		bean.add("id", om.getId());
		bean.add("order_id", om.getOrder().getId());
		bean.add("speaker", om.getSpeaker());
		bean.add("content", om.getContent());
		bean.add("create_by", om.getCreateBy());
		bean.add("create_time", om.getCreateTime());
		bean.add("display", om.isDisplay());
		return bean;
	}

	
	
	
}

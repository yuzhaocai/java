package com.lczy.media.mobile.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.lczy.common.util.MyGson;
import com.lczy.media.entity.ChargeLog;
import com.lczy.media.entity.Customer;
import com.lczy.media.mobile.util.Request;
import com.lczy.media.mobile.util.Response;
import com.lczy.media.mobile.util.Token;
import com.lczy.media.mobile.util.TokenManager;
import com.lczy.media.service.ChargeLogService;
import com.lczy.media.service.CustomerService;
import com.lczy.media.util.Constants;

/**
 *  移动端:用户充值相关网络接口
 * @author wang.haibin
 *
 */
@Controller
@RequestMapping("/mobile/mcUser")
public class MobileChargeController {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private Gson gson = MyGson.getInstance();
	
	@Autowired
	private TokenManager tokenManager;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private ChargeLogService chargeLogService;
	
	/** 
	 * 充值
	 * 
	 * @param body 请求信息
	 * @return 返回结果
	 */
	@RequestMapping("charge")
	@ResponseBody
	public Response charge(HttpServletRequest httpRequest) {
		Response resp = null;
		try {
			resp = new Response();
			Request request = Request.parseRequest(httpRequest);
			log.debug("withdraw request: {}", request.getBodyContent());
			Token token = tokenManager.getToken(request.getToken());
			String custId = token.getCustId();
			Customer customer = customerService.get(custId);
			
			// 获取参数
			int amount = Integer.parseInt(request.getBodyAsString("amount"));
			String platform = request.getBodyAsString("platform");
			
			ChargeLog chargeLog = new ChargeLog();
			chargeLog.setAmount(amount);
			chargeLog.setPlatform(Constants.PayPlatform.ALIPAY);
			chargeLog.setCustomer(customer);
			chargeLog.setStatus(Constants.ChargeLogStatus.CREATED);
			chargeLog.setPlatform(platform);
			chargeLog.setCreateTime(new Date());
			chargeLog.setModifyTime(new Date());
			chargeLogService.save(chargeLog);			
			resp.addBody("out_trade_no", chargeLog.getId());
		} catch(Exception e) {
			log.error("设置用户实名信息失败", e);
			resp = new Response(900, e.getMessage());
		}
		log.debug("withdraw response: {}", gson.toJson(resp));
		return resp;
	}
	
}

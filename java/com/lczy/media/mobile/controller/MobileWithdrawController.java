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
import com.lczy.media.entity.Account;
import com.lczy.media.entity.Customer;
import com.lczy.media.entity.WithdrawLog;
import com.lczy.media.mobile.util.Request;
import com.lczy.media.mobile.util.Response;
import com.lczy.media.mobile.util.Token;
import com.lczy.media.mobile.util.TokenManager;
import com.lczy.media.service.AccountService;
import com.lczy.media.service.CustomerService;
import com.lczy.media.util.Constants;
import com.lczy.media.util.SmsCodeValidator;

/**
 *  移动端:用户提现相关网络接口
 * @author wang.haibin
 *
 */
@Controller
@RequestMapping("/mobile/mcUser")
public class MobileWithdrawController {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private Gson gson = MyGson.getInstance();
	
	@Autowired
	private TokenManager tokenManager;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private SmsCodeValidator smsCodeValidator;
	/** 
	 * 提现申请
	 * 
	 * @param body 请求信息
	 * @return 返回结果
	 */
	@RequestMapping("withdraw")
	@ResponseBody
	public Response withdraw(HttpServletRequest httpRequest) {
		Response resp = null;
		try {
			resp = new Response();
			Request request = Request.parseRequest(httpRequest);
			log.debug("withdraw request: {}", request.getBodyContent());
			Token token = tokenManager.getToken(request.getToken());
			String custId = token.getCustId();
			Customer customer = customerService.get(custId);
			Account account = accountService.get(customer);
			
			// 获取参数
			int amount = Integer.parseInt(request.getBodyAsString("amount"));
			String platform = request.getBodyAsString("platform");
			String alipayAccount = request.getBodyAsString("account");
			String vercode = request.getBodyAsString("vercode");
			
			// 校验验证码
			validateVercode(customer.getMobPhone(), vercode);
			
			WithdrawLog withdrawLog = new WithdrawLog();
			withdrawLog.setAmount(amount);
			withdrawLog.setPlatform(platform);
			withdrawLog.setCustomer(account.getCustomer());
			withdrawLog.setPlatformAccount(alipayAccount);
			withdrawLog.setStatus(Constants.WithdrawStatus.CREATED);
			withdrawLog.setCreateTime(new Date());
			
			accountService.withdraw(account, withdrawLog);
		} catch(Exception e) {
			log.error("设置用户实名信息失败", e);
			resp = new Response(900, e.getMessage());
		}
		log.debug("withdraw response: {}", gson.toJson(resp));
		return resp;
	}
	
	/**
	 * 校验验证码
	 * 
	 * @param mobPhone
	 * @param vercode
	 * @throws Exception
	 */
	private void validateVercode(String mobPhone, String vercode) throws Exception {
		if (!smsCodeValidator.check(mobPhone, vercode)) {
			throw new Exception("验证码错误!");
		}
	}	
}

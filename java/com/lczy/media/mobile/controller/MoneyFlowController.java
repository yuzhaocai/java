/**
 * 
 */
package com.lczy.media.mobile.controller;

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

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.lczy.common.util.MyGson;
import com.lczy.media.entity.User;
import com.lczy.media.mobile.util.Request;
import com.lczy.media.mobile.util.Response;
import com.lczy.media.mobile.util.Token;
import com.lczy.media.mobile.util.TokenManager;
import com.lczy.media.service.MoneyFlowService;
import com.lczy.media.service.UserService;
import com.lczy.media.vo.MoneyFlowVO;

/**
 * 移动端登录.
 * 
 * @author wu
 *
 */
@Controller
@RequestMapping("/mobile/mcUser")
public class MoneyFlowController {
	
	private Logger log = LoggerFactory.getLogger(MoneyFlowController.class);
	
	private Gson gson = MyGson.getInstance();
	
	@Autowired
	private MoneyFlowService moneyFlowService;
	
	@Autowired
	private TokenManager tokenManager;
	
	
	@Autowired
	private UserService userService;
	
	/**
	 * 资金流水接口.
	 */
	@RequestMapping("myMoneyFlow")
	@ResponseBody
	public Response myMoneyFlow(HttpServletRequest httpRequest) {
		
		Request req = Request.parseRequest(httpRequest);
		log.debug("request: " + req.getBodyContent());
		Token token = tokenManager.getToken(req.getToken());
		String uid = token.getUserId();
		
		Response resp = null;
		try {
			resp = new Response();
			User user = userService.get(uid);
			Map<String, Object> searchParams = Maps.newLinkedHashMap();
			searchParams.put("EQ_customer.id", user.getCustomer().getId());
			String startTime = (String) req.getBodyAsString("start_date");;
			String endTime = (String) req.getBodyAsString("end_date");
			if (StringUtils.isNotBlank(startTime)) {
				DateTime start = DateTime.parse(startTime + "T00:00:00");
				searchParams.put("GTE_createTime", start.toDate());
			}
			if (StringUtils.isNotBlank(endTime)) {
				DateTime end = DateTime.parse(endTime + "T23:59:59.999");
				searchParams.put("LTE_createTime", end.toDate());
			}
			List<MoneyFlowVO> moneyFlow = moneyFlowService.getMoneyFlow(searchParams);
			user.getCustomer().getAccount().getAvBalance();
			resp.addBody("datas", moneyFlow);
			resp.addBody("balance", user.getCustomer().getAccount().getAvBalance());
		} catch (Exception e) {
			log.debug("查询异常", e);
			resp = new Response(900, e.getMessage());
		}
		
		log.debug("response: " + gson.toJson(resp));
		return resp;
	}
	
}

package com.lczy.media.controller.payment;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lczy.media.entity.ChargeLog;
import com.lczy.media.service.ChargeLogService;
import com.lczy.media.util.Constants;
import com.lczy.payment.alipay.config.AlipayConfig;
import com.lczy.payment.alipay.util.AlipayNotify;
import com.lczy.payment.alipay.util.AlipaySubmit;

/**
 * @author wanghaibin
 *
 */
@Controller
public class AlipayController {
	
	private static Logger logger = LoggerFactory.getLogger(AlipayController.class);
	
    // 服务异步通知接口
    private static String notify_url = "http://www.cnmei.com/alipay/notify";

    // 服务异步通知接口
    private static String return_url = "http://www.cnmei.com/alipay/return";
    
	@Autowired
	private ChargeLogService chargeLogService;
	
	/**
	 * 调用即时到账接口，传参数给支付宝 
	 * @param out_trade_no 订单号
	 * @param subject 订单名
	 * @param total_fee 价格
	 * @param body 订单描述
	 * @param show_url 商品url
	 * @return
	 */
	@RequestMapping(value="/member/account/charge/alipay/{id}")
	public String charge(@PathVariable String id, HttpServletRequest request) {
		// host 是在 nginx 处传递过来的
//		String host = request.getHeader("Host");
		String subject = "会员在线充值";//订单名称，此处对应充值日志的描述
		String body = "会员中心充值，实时到账。";//订单描述
		
		//需以http://开头的完整路径，例如：http://www.商户网址.com/myorder.html
		//防钓鱼时间戳
		String anti_phishing_key = "";
		//若要使用请调用类文件submit中的query_timestamp函数
		//客户端的IP地址
		String exter_invoke_ip = "";
		//非局域网的外网IP地址，如：221.0.0.1
		
		//////////////////////////////////////////////////////////////////////////////////
		//把请求参数打包成数组
		ChargeLog chargeLog = chargeLogService.get(id);
		Map<String, String> params = new HashMap<String, String>();
		params.put("service", "create_direct_pay_by_user");
        params.put("partner", AlipayConfig.partner);
        params.put("_input_charset", AlipayConfig.input_charset);
		params.put("payment_type", AlipayConfig.payment_type);
		params.put("seller_email", AlipayConfig.seller_email);
		params.put("subject", subject);
		params.put("total_fee", String.valueOf(chargeLog.getAmount()));
		params.put("body", body);
		params.put("anti_phishing_key", anti_phishing_key);
		params.put("exter_invoke_ip", exter_invoke_ip);
		params.put("notify_url", notify_url);
		params.put("return_url", return_url);
		params.put("out_trade_no", chargeLog.getId());
		logger.info("alipayapi__input : "+params);
		return "redirect:" + AlipaySubmit.signRequest(params);
	}

	/**
	 功能：支付宝服务器异步通知页面
	 版本：3.3
	 日期：2012-08-17
	 //***********页面功能说明***********
	 创建该页面文件时，请留心该页面文件中无任何HTML代码及空格。
	 该页面不能在本机电脑测试，请到服务器上做测试。请确保外部可以访问该页面。
	 该页面调试工具请使用写文本函数logResult，该函数在com.alipay.util文件夹的AlipayNotify.java类文件中
	 如果没有收到该页面返回的 success 信息，支付宝会在24小时内按一定的时间策略重发通知
	 //********************************
	 **/
	@RequestMapping(value="/alipay/notify")
	@ResponseBody
	public String notify_url(HttpServletRequest request) {
		// 获取支付宝POST过来反馈信息
		Map<String, String> params = getRequestParams(request);
		logger.info("alipay notify params : {}", params);
		
		// 订单号
		String out_trade_no = request.getParameter("out_trade_no");
		// 支付宝交易号
		String trade_no = request.getParameter("trade_no");
		// 交易状态
		String trade_status = request.getParameter("trade_status");
		// 金额
		String total_fee = request.getParameter("total_fee");
		
		logger.info("out_trade_no : {}", out_trade_no);
		logger.info("trade_no : {}", trade_no);
		logger.info("total_fee : {}", total_fee);
		
		if("TRADE_FINISHED".equals(trade_status) || "TRADE_SUCCESS".equals(trade_status)){
			if(AlipayNotify.verify(params)){
				//验证成功
				try {
					chargeLogService.charge(out_trade_no, Constants.PayPlatform.ALIPAY, trade_no);
					return "success";
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		return "fail";
	}

	/**
	 功能：支付宝页面跳转同步通知页面
	 版本：3.2
	 日期：2011-03-17
	 //***********页面功能说明***********
	 该页面可在本机电脑测试
	 可放入HTML等美化页面的代码、商户业务逻辑程序代码
	 TRADE_FINISHED(表示交易已经成功结束，并不能再对该交易做后续操作);
	 TRADE_SUCCESS(表示交易已经成功结束，可以对该交易做后续操作，如：分润、退款等);
	 //********************************
	 **/
	@RequestMapping(value="/alipay/return")
	public String return_url(HttpServletRequest request, RedirectAttributes redirectAttrs) {
		//获取支付宝POST过来反馈信息
		Map<String, String> params = getRequestParams(request);
		logger.info("alipay return params : {}", params);
		
		String out_trade_no = new String(request.getParameter("out_trade_no"));
		//支付宝交易号
		String trade_no = new String(request.getParameter("trade_no"));
		//交易状态
		String trade_status = new String(request.getParameter("trade_status"));
		
		if("TRADE_FINISHED".equals(trade_status) || "TRADE_SUCCESS".equals(trade_status)){
			if(AlipayNotify.verify(params)){//验证成功
				try {
					chargeLogService.charge(out_trade_no, Constants.PayPlatform.ALIPAY, trade_no);
					redirectAttrs.addFlashAttribute("message", "充值成功！");
				} catch (Exception e) {
					redirectAttrs.addFlashAttribute("message", "充值失败:" + e.getMessage());
				}
			}
		}
		return "redirect:/member/account/chargeList";
	}
	
	/**
	 * 获取支付宝POST过来反馈信息
	 * 
	 * @param request
	 * @return
	 */
	private Map<String, String> getRequestParams(HttpServletRequest request) {
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = StringUtils.join(values, ",");
			params.put(name, valueStr);
		}
		return params;
	}
	
}

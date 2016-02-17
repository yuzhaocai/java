package com.lczy.media.controller.payment;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lczy.common.util.UUID;
import com.lczy.media.entity.ChargeLog;
import com.lczy.media.service.ChargeLogService;
import com.lczy.media.util.Constants;
import com.lczy.media.util.Constants.ChargeLogStatus;
import com.lczy.media.util.RoundLineQRCodeUtils;
import com.lczy.payment.wxpay.config.WxpayConfig;
import com.lczy.payment.wxpay.util.MD5Util;

/**
 * @author wanghaibin
 *
 */
@Controller
public class WxpayController {

    private static Logger logger = LoggerFactory.getLogger(WxpayController.class);

    //微信支付接口
    private static String unifiedorderUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    // 服务异步通知接口
    private static String notifyUrl = "http://www.cnmei.com/wxpay/notify";
    
    @Autowired
    private ChargeLogService chargeLogService;

    /**
     * 充值
     * 
	 * @param id
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/member/account/charge/wxpay/{id}")
	public String charge(@PathVariable String id, Model model, HttpServletRequest request) throws Exception {
		getCodeUrl(id, request);
		ChargeLog chargeLog = chargeLogService.get(id);
		model.addAttribute("chargeLog", chargeLog);
		return "wxpay/charge";
    }

    /**
     * 返回微信支付二维码
     * 
     * @param id
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/wxpay/qrcode/{id}")
    public void qrcode(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String code_url = getCodeUrl(id, request);
        response.setDateHeader("Expires", 0);

		// Set standard HTTP/1.1 no-cache headers.
		response.setHeader("Cache-Control","no-store, no-cache, must-revalidate");

		// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");

		// Set standard HTTP/1.0 no-cache header.
		response.setHeader("Pragma", "no-cache");

		// return a jpeg
		response.setContentType("image/jpeg");

		// create the image with the text
		ServletOutputStream out = response.getOutputStream();

		BufferedImage ii = new RoundLineQRCodeUtils(4,3).createImg(null, code_url, Color.BLACK, Color.WHITE);
		ImageIO.write(ii, "jpg", out);
		
		try {
			out.flush();
		} finally {
			out.close();
		}
    }

	private String getCodeUrl(String id, HttpServletRequest request) throws Exception {
		ChargeLog chargeLog = chargeLogService.get(id);
		
		if (!chargeLog.getStatus().equals(ChargeLogStatus.CREATED)) {
			throw new Exception("订单已支付!");
		}
		
		if (chargeLog.getData() != null) {
			return chargeLog.getData();
		}
		
		SortedMap<String, String> signParams = new TreeMap<String, String>();
        signParams.put("appid", WxpayConfig.appid);
        signParams.put("mch_id", WxpayConfig.mch_id);
        signParams.put("nonce_str", UUID.get());
        signParams.put("attach", "充值");
        signParams.put("body", "充值");
        signParams.put("spbill_create_ip", request.getRemoteAddr());
        signParams.put("out_trade_no", id);
        signParams.put("trade_type", "NATIVE");
        signParams.put("total_fee", String.valueOf(chargeLog.getAmount()*100));
        signParams.put("notify_url", notifyUrl);

		encodeParams(signParams);

        String requestBody = converteToXml(signParams);
        logger.error("xml : {}", requestBody);

        PostMethod method = new PostMethod(unifiedorderUrl);
        try {
            method.addRequestHeader("Content-Type", "text/plain; charset=utf-8");
            RequestEntity req = new StringRequestEntity(requestBody, "text/xml", "UTF-8");
            method.setRequestEntity(req);
            HttpClient client = new HttpClient();
            client.getHttpConnectionManager().getParams().setConnectionTimeout(15000);
            client.executeMethod(method);

            String res = method.getResponseBodyAsString();
            res = new String(res.getBytes("ISO-8859-1"), "UTF-8");
            logger.error("res=" + res);

            Map<String, String> result = converteToMap(res);
            decodeParams(result);
            if ("SUCCESS".equals(result.get("return_code")) && "SUCCESS".equals(result.get("result_code"))) {
            	chargeLog.setData(result.get("code_url"));
            	chargeLogService.save(chargeLog);
            	return result.get("code_url");
            } else {
            	throw new Exception(result.get("return_msg"));
            }
        } finally {
            method.releaseConnection();
        }
	}

	/**
	 * 签名比较
	 * 
	 * @param params
	 * @throws Exception
	 */
	private void decodeParams(Map<String, String> params) throws Exception {
		StringBuffer sb;
		String sign = params.remove("sign");
		sb = new StringBuffer();
		for (String key : params.keySet()) {
		    sb.append(key).append("=").append(params.get(key)).append("&");
		}
		sb.append("key=").append(WxpayConfig.app_key);
		if (!sign.equals(MD5Util.MD5Encode(sb.toString(), "UTF-8").toUpperCase())) {
			throw new Exception("签名错误");
		}
	}

	/**
	 * 签名
	 * 
	 * @param params
	 */
	private void encodeParams(SortedMap<String, String> params) {
		StringBuffer sb = new StringBuffer();
        for (String key : params.keySet()) {
            sb.append(key).append("=").append(params.get(key)).append("&");
        }
        sb.append("key=").append(WxpayConfig.app_key);
        logger.debug("params : {}", sb);
        String sign = MD5Util.MD5Encode(sb.toString(), "UTF-8");
        params.put("sign", sign.toUpperCase());
        logger.error("sign : {}", sign);
	}

    /**
     * 支付通知接口
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/wxpay/notify")
    @ResponseBody
    public String notify(ServletRequest request) {
        Map<String, String> result = new HashMap<>();
        try {
        	String body = getRequestBody(request);
            logger.info("wxpay notify params: {}", body);
            Map<String, String> params = converteToMap(body);

            decodeParams(params);
            if ("SUCCESS".equals(params.get("result_code"))) {
            	chargeLogService.charge(params.get("out_trade_no"), Constants.PayPlatform.WXPAY, params.get("transaction_id"));
            	logger.debug("支付成功：{}", params.get("out_trade_no"));
            	result.put("return_code", "SUCCESS");
            	result.put("return_msg", "OK");
            }
        } catch (Exception e) {
            logger.error("支付失败: {}", e.getMessage());
            result.put("return_code", "FAIL");
            result.put("return_msg", e.getMessage());
        }
        return converteToXml(result);
    }

    /**
     * 获取通知参数
     * 
     * @param request
     * @return
     * @throws IOException
     */
    private String getRequestBody(ServletRequest request) throws IOException {
        StringBuffer sb = new StringBuffer();
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = request.getReader();
            char[] charBuffer = new char[128];
            int bytesRead;
            while ((bytesRead = bufferedReader.read(charBuffer)) != -1) {
                sb.append(charBuffer, 0, bytesRead);
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }
        return sb.toString();
    }

    /**
     * map 转换为 xml字符串
     * 
     * @param map
     * @return
     */
    public static String converteToXml(Map<String, String> map) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("<xml>");
        for (String key : map.keySet()) {
            strBuilder.append("<").append(key.toString()).append(">");
            strBuilder.append("<![CDATA[");
            String value = map.get(key);
            strBuilder.append(value);
            strBuilder.append("]]>");
            strBuilder.append("</").append(key.toString()).append(">");
        }
        strBuilder.append("</xml>");
        return strBuilder.toString();
    }

    /**
     * xml字符串 转换为 sortedmap
     * 
     * @param xml
     * @return
     */
    public static Map<String, String> converteToMap(String xml) {
        Map<String, String> map = new TreeMap<String, String>();
        try {
            Document document = DocumentHelper.parseText(xml);
            Element nodeElement = document.getRootElement();
            List node = nodeElement.elements();
            for (Iterator it = node.iterator(); it.hasNext();) {
                Element elm = (Element) it.next();
                map.put(elm.getName(), elm.getText());
                elm = null;
            }
        } catch (Exception e) {
        	logger.error("", e.getMessage());
        }
        return map;
    }

}

package com.lczy.media.mobile.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.lczy.common.util.MyGson;
import com.lczy.common.util.UUID;
import com.lczy.media.entity.Customer;
import com.lczy.media.mobile.util.Request;
import com.lczy.media.mobile.util.Response;
import com.lczy.media.mobile.util.Token;
import com.lczy.media.mobile.util.TokenManager;
import com.lczy.media.service.CustomerService;
import com.lczy.media.util.Constants;
import com.lczy.media.util.FileServerUtils;

/**
 *  用户相关网络接口
 * @author wang.xiaoxiang
 *
 */
@Controller
@RequestMapping("/mobile/mcUser")
public class MobileUserInfoController {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private Gson gson = MyGson.getInstance();
	
	@Autowired
	private TokenManager tokenManager;
	
	@Autowired
	private CustomerService customerService;
	
	/** 设置用户实名信息
	 * @param body 请求信息
	 * @return 返回结果
	 */
	@RequestMapping("setUserRealInfo")
	@ResponseBody
	public Response setUserRealInfo(HttpServletRequest httpRequest) {
		Response resp = null;
		try {
			resp = new Response();
			Request request = Request.parseRequest(httpRequest);
			log.debug("userRealInfo request: {}", request.getBodyContent());
			Token token = tokenManager.getToken(request.getToken());
			String custId = token.getCustId();
			Customer customer = customerService.get(custId);
			String certName = request.getBodyAsString("cert_name");
			customer.setCertName(certName);
			String certIdentity = request.getBodyAsString("cert_identity");
			customer.setCertIdentity(certIdentity);
			String picData = request.getBodyAsString("picdata");
			String fileId = generateImage(picData);
			customer.setCertMatter(fileId);
			customer.setCertSubmitTime(new Date());
			customer.setCertStatus(Constants.CertStatus.AUDIT);
			customer.setCertified(false);
			customerService.save(customer);
			resp.addBody("fid", fileId);
		} catch(Exception e) {
			log.error("设置用户实名信息失败", e);
			resp = new Response(900, e.getMessage());
		}
		log.debug("userRealInfo response: {}", gson.toJson(resp));
		return resp;
	}
	
	/** 设置用户信息
	 * @param body 请求的信息
	 * @return 返回结果
	 */
	@RequestMapping("setUserInfo")
	@ResponseBody
	public Response setUserInfo(HttpServletRequest httpRequest) {
		Response resp = null;
		try {
			resp = new Response();
			Request request = Request.parseRequest(httpRequest);
			log.debug("userInfo request: {}", request.getBodyContent());
			Token token = tokenManager.getToken(request.getToken());
			String custId = token.getCustId();
			Customer customer = customerService.get(custId);
			String linkman = request.getBodyAsString("linkman");
			customer.setLinkman(linkman);
			String email = request.getBodyAsString("email");
			customer.setEmail(email);
			String qq = request.getBodyAsString("qq");
			customer.setQq(qq);
//			String mobPhone = request.getBodyAsString("mob_phone");
//			if (!customer.getMobPhone().equals(mobPhone)) {
//				customer.setMobPhone(mobPhone);
//				String vercode = request.getBodyAsString("vercode");
//				if (existsMobPhone(mobPhone)) {
//					log.error("手机号码已经被注册!");
//					resp = new Response(900, "手机号码已经被注册!");
//					return resp;
//				}
//				if (!checkSmscode(mobPhone, vercode)) {
//					log.error("短信验证码不正确!");
//					resp = new Response(900, "短信验证码不正确!");
//					return resp;
//				}
//			}
			customerService.save(customer);
		} catch(Exception e) {
			log.error("设置用户信息失败", e);
			resp = new Response(900, e.getMessage());
		}
		log.debug("userInfo response: {}", gson.toJson(resp));
		return resp;
	}
	
	/** 对字节数组字符串进行Base64解码并生成图片
	 * @param imgStr base64图片数据
	 * @param imgFilePath 生成的文件路径
	 * @return 返回文件fieldId
	 * @throws Exception 
	 */
	private String generateImage(String imgStr) throws Exception {
        if (imgStr == null) {
        	return null;
        }
        //BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] bytes = Base64.decodeBase64(imgStr);
            String name = UUID.get();
            return FileServerUtils.upload(null, name + ".jpg", bytes, false, "image", true);
        } catch (Exception e) {
        	throw e;
        }
    }
}

package com.lczy.media.controller;

import java.util.Date;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lczy.common.util.MessageBean;
import com.lczy.common.web.Token;
import com.lczy.common.web.Token.Type;
import com.lczy.media.entity.Customer;
import com.lczy.media.entity.Dic;
import com.lczy.media.entity.Opinion;
import com.lczy.media.service.CustomerService;
import com.lczy.media.service.OpinionService;
import com.lczy.media.service.common.DicProvider;
import com.lczy.media.util.Constants;
import com.lczy.media.util.FileServerUtils;
import com.lczy.media.util.UserContext;

@Controller
@RequestMapping("/suggestion")
public class SuggestionController {

	private static final Logger log = LoggerFactory
			.getLogger(SuggestionController.class);

	@Autowired
	private DicProvider dicProvider;

	@Autowired
	private OpinionService opinionService;

	@Autowired
	private CustomerService customerService;

	@Token
	@RequestMapping(value = "suggestionModal", method = RequestMethod.GET)
	public String suggestionModal(Model model) {
		Dic suggestions = dicProvider.getDicMap().get(
				Constants.Opinion.DIC_CODE);
		model.addAttribute("suggestions", suggestions);
		return "suggestionModal";
	}

	@RequestMapping("suggest")
	@ResponseBody
	@Token(Type.REMOVE)
	public MessageBean suggest(String phone, String feedbackContent,
			String feedbackType, Model model, HttpServletRequest request,
			@RequestParam(value="feedbackAttachment",required = false) MultipartFile image, String url,RedirectAttributes redirectAttributes)
			throws Exception {
		if (isMobile(phone)) {
			Opinion opinion = new Opinion();
			if (image != null && image.getSize() > 0) {
				String suffix = image.getOriginalFilename().substring(
						image.getOriginalFilename().lastIndexOf(".") + 1);
				String fileId = FileServerUtils.upload(null,
						image.getOriginalFilename(), image.getBytes(), false,
						suffix);
				opinion.setFeedbackAttachment(fileId);
			}
			Date now = new Date();
			opinion.setCreateTime(now);
			opinion.setFeedbackContent(feedbackContent);
			opinion.setPhone(phone);
			opinion.setFeedbackType(feedbackType);
			String userType = null;
			if (null != UserContext.getCurrent()) {
				String custId = UserContext.getCurrent().getCustomer().getId();
				Customer cust = customerService.get(custId);
				if (cust.getCustType().equals(Constants.CustType.CUST_ADV)) {
					userType = "广告主";
				} else if (cust.getCustType().equals(
						Constants.CustType.CUST_PRO)) {
					userType = "媒体";
				}
			} else {
				userType = "游客";
			}
			opinion.setUserType(userType);
			opinionService.save(opinion);
		}
		MessageBean bean = new MessageBean(1, "投诉建议成功！");
		redirectAttributes.addFlashAttribute("message", bean.toJSON());
		return bean;
	}

	@RequestMapping(value = "checkCaptcha")
	@ResponseBody
	public boolean checkMediaAccount(ServletRequest request, String captcha) {

		return doCaptchaValidate(request, captcha);
	}

	// 验证码校验
	protected boolean doCaptchaValidate(ServletRequest request, String captcha) {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		// session中的图形码字符串
		String captchaPic = (String) httpRequest.getSession().getAttribute(
				com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		// 比对
		return captchaPic != null && captchaPic.equalsIgnoreCase(captcha);
	}

	/**
	 * 验证手机号
	 * 
	 * @param str
	 * @return
	 */
	public boolean isMobile(String str) {
		return java.util.regex.Pattern.matches("^[1][3,4,5,8][0-9]{9}$", str);
	}

}

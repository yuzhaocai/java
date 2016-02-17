/**
 * 
 */
package com.lczy.media.controller.member;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lczy.common.util.Files;
import com.lczy.common.util.MessageBean;
import com.lczy.media.entity.Customer;
import com.lczy.media.entity.MediaCase;
import com.lczy.media.service.CustomerService;
import com.lczy.media.util.Constants;
import com.lczy.media.util.FileServerUtils;
import com.lczy.media.util.SmsCodeValidator;
import com.lczy.media.util.UserContext;
import com.lczy.media.vo.CustomerVO;

/**
 * 用户信息.
 * 
 * @author wu
 * 
 */
@Controller
@RequestMapping("/member")
public class UserInfoController {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private SmsCodeValidator smsCodeValidator;

	private boolean checkSmscode(CustomerVO vo) {

		return smsCodeValidator.check(vo.getMobPhone(), vo.getSmscode());
	}

	private boolean existsMobPhone(CustomerVO cust) {

		return customerService.countBy("mobPhone", cust.getMobPhone()) > 0;
	}

	@RequestMapping("userInfo")
	public String userInfo(Model model) {
		// 会员信息
		Customer cust = customerService.get(UserContext.getCurrent()
				.getCustomer().getId());
		customerService.refresh(cust);
		model.addAttribute("cust", cust);

		return "/member/userInfo";
	}

	@RequestMapping("updateUserInfo")
	public String updateUserInfo(@Valid @ModelAttribute("vo") CustomerVO vo,
			BindingResult result, HttpServletRequest request,
			RedirectAttributes redirectAttrs) {
		Customer cust = customerService.get(vo.getId());

		if (!cust.getMobPhone().equals(vo.getMobPhone())) { // 修改了手机号码
			if (existsMobPhone(vo)) {
				result.addError(new FieldError("vo", "mobPhone", "手机号码已经被注册!"));
			}

			if (!checkSmscode(vo)) {
				result.addError(new FieldError("vo", "smscode", "短信验证码不正确!"));
			}
		}

		MessageBean bean = null;

		if (result.hasErrors()) {
			// 如果存在错误，则把错误结果暴露给页面的<spring:hasBindErrors>标签显示
			// 因为最终要redirect到新页面，所以需要调用.addFlashAttribute。
			redirectAttrs.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX
					+ "vo", result);
			bean = new MessageBean(0, "更新用户信息失败！");
		} else {
			customerService.updateUserInfo(vo, cust);
			bean = new MessageBean(1, "更新用户信息成功！");
		}

		redirectAttrs.addFlashAttribute("message", bean.toJSON());
		return "redirect:/member/userInfo";
	}

	@RequestMapping("toCertification")
	public String toCertification(Model model, String custId,
			RedirectAttributes redirectAttributes) {
		Customer customer = customerService.get(custId);
		MessageBean bean = checkPermission(customer);
		if (bean == null) {
			model.addAttribute("cust", customer);
			model.addAttribute("pics", getPics(customer));

			if (customer.getCertStatus().equals(Constants.CertStatus.PASS))
				return "/member/verify/verify";
			else
				return "/member/verify/verifying";
		} else {
			redirectAttributes.addFlashAttribute("message", bean.toJSON());
			return "redirect:/member/userInfo";
		}

	}

	@RequestMapping("verify")
	public String verify(Model model, String custId,
			RedirectAttributes redirectAttributes) {
		Customer customer = customerService.get(custId);
		MessageBean bean = checkPermission(customer);
		if (bean == null) {
			model.addAttribute("cust", customer);
			return "/member/verify/verify";
		} else {
			redirectAttributes.addFlashAttribute("message", bean.toJSON());
			return "redirect:/member/userInfo";
		}

	}

	/**
	 * 获取图片id
	 * 
	 * @param mCase
	 * @return
	 */
	private List<String> getPics(Customer customer) {

		List<String> mCasePic = new ArrayList<String>();
		if (StringUtils.isNotBlank(customer.getCertMatter())) {
			String[] pics = customer.getCertMatter().split(";");
			for (String pic : pics) {
				mCasePic.add(pic);
			}
		}
		return mCasePic;
	}

	/**
	 * 检查当前用户是否有权限操作实体.
	 * 
	 * @param customer
	 *            目标实体
	 * @return 有权限则返回 null，无权限则返回 MessageBean 对象.
	 */
	private MessageBean checkPermission(Customer customer) {
		MessageBean bean = null;
		if (customer == null) {
			bean = new MessageBean(0, "用户不存在！");
		} else if (!hasPermission(customer)) {
			bean = new MessageBean(0, "您无权操作此用户：" + customer.getId());
		}

		return bean;
	}

	/**
	 * @return 是否有权限操作此用户.
	 */
	private boolean hasPermission(Customer customer) {

		return customer.getId().equals(
				UserContext.getCurrent().getCustomer().getId());
	}

	@RequestMapping("certification")
	public String realName(CustomerVO vo,
			@RequestParam("images") MultipartFile[] images,
			@RequestParam(value = "pics", required = false) String[] pics)
			throws Exception {
		Customer customer = customerService.get(vo.getId());
		customer.setCertified(true);
		customer.setCertStatus(Constants.CertStatus.AUDIT);
		customer.setCertName(vo.getCertName());
		customer.setCertIdentity(vo.getCertIdentity());
		customer.setCertMatter(this.uploadFile(images, pics));
		customer.setCertSubmitTime(new Date());
		customerService.save(customer);
		return "redirect:/member/userInfo";
	}

	@RequestMapping("certificate")
	@ResponseBody
	public Map<String, Object> certificate(String customerId) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Customer customer = customerService.get(customerId);
		String fileSrc = FileServerUtils.getFileUrl(customer.getCertMatter())
				+ "?token=" + FileServerUtils.token();
		map.put("certMatter", fileSrc);
		return map;
	}
	
	/**
	 * 检查客户名称是否可修改.
	 */
	@RequestMapping(value = "checkCustNameEdit")
	@ResponseBody
	public boolean checkCustNameEdit(String name, String oldName) {
		if (StringUtils.isBlank(name))
			return false;
		if (name.equals(oldName))
			return true;
		else
			return customerService.countBy(name) == 0;
	}
	
	
	private String uploadFile(MultipartFile[] images ,String[] pics) throws Exception {
		String showPicUrl = "";// 媒体创建案例图片
		if (images != null && images.length > 0) {
			for (int i = 0; i < images.length; i++) {
				MultipartFile img = null;
				String url = null;
				if (images[i].getSize() > 0) {
					img = images[i];
					String fileId = FileServerUtils.upload(null,
							img.getOriginalFilename(), img.getBytes(), false,
							"image");
					url = fileId + ";";
					showPicUrl += url;
				}else {
					if(null!=pics&&pics.length>0)
						if(pics[i].trim().length()>0)
							showPicUrl += pics[i] + ";";
				}
			}
		}
		return showPicUrl;
	}

}

package com.lczy.media.controller.member;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lczy.common.exception.FileUploadException;
import com.lczy.common.exception.IllegalFileTypeException;
import com.lczy.common.util.MessageBean;
import com.lczy.common.web.Token;
import com.lczy.common.web.Token.Type;
import com.lczy.common.web.TokenInterceptor;
import com.lczy.media.entity.Media;
import com.lczy.media.entity.MediaWeixin;
import com.lczy.media.service.CustomerService;
import com.lczy.media.service.MediaService;
import com.lczy.media.util.Constants;
import com.lczy.media.vo.MediaVO;

/**
 * 微信控制器
 * 
 * @Author Zhang.CJ
 * @Date 2015年8月28日 下午5:29:39
 */

@Controller
@RequestMapping("/member/media/weixin")
public class MyWeiXinController extends MyMediaController {
	private static final Logger log = LoggerFactory
			.getLogger(ReqCreateController.class);

	@Autowired
	private CustomerService customerService;

	@Autowired
	private MediaService mediaService;

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@Token(Type.REMOVE)
	public String save(@ModelAttribute("weixin") MediaVO vo,
			BindingResult result, HttpServletRequest request, Model model,RedirectAttributes redirectAttrs) {
		MessageBean bean = null;
		try {

			MediaWeixin weixin = null;
			if (StringUtils.isNotBlank(vo.getId())) {
				weixin = (MediaWeixin) mediaService.get(vo.getId());
				weixin.setStatus(Constants.MediaStatus.AUDIT);
				bean = new MessageBean(1, "修改成功！");
			} else {
				weixin = new MediaWeixin();
				weixin.setWeixinCode(vo.getName());
				bean = new MessageBean(1, "创建成功！");
			}
			try {
				mediaService.saveLogoImg(vo, weixin);
			} catch (IllegalFileTypeException e) {
				throw new FileUploadException("showPicFile", "LOGO："
						+ e.getMessage(), e);
			} catch (IOException e) {
				throw new FileUploadException("showPicFile", "LOGO上传失败，请稍后重试！",
						e);
			}
			try {
				mediaService.saveQrCodeImg(vo, weixin);
			} catch (IllegalFileTypeException e) {
				throw new FileUploadException("qrCodeFile", "二维码："
						+ e.getMessage(), e);
			} catch (IOException e) {
				throw new FileUploadException("qrCodeFile", "二维码上传失败，请稍后重试！", e);
			}

			try {
				mediaService.saveFansNumImg(vo, weixin);
			} catch (IllegalFileTypeException e) {
				throw new FileUploadException("fansNumFile", "粉丝数截图："
						+ e.getMessage(), e);
			} catch (IOException e) {
				throw new FileUploadException("fansNumFile",
						"粉丝数截图上传失败，请稍后重试！", e);
			}
			mediaService.save(weixin, vo);
			
		} catch (FileUploadException e) {
			log.warn("文件上传失败", e);
			if (e.getField().equals("showPicFile")) {
				result.addError(new FieldError("weixin", e.getField(), "文件类型["
						+ vo.getShowPicFile().getOriginalFilename()
								.split("\\.")[1]
						+ "]不允许上传，允许上传的文件类型为：jpg, png, bmp"));
				model.addAttribute("showPicFile", true);
			} else if (e.getField().equals("qrCodeFile")) {
				result.addError(new FieldError("weixin", e.getField(),
						"文件类型["
								+ vo.getQrCodeFile().getOriginalFilename()
										.split("\\.")[1]
								+ "]不允许上传，允许上传的文件类型为：jpg, png, bmp"));
				model.addAttribute("qrCodeFile", true);
			} else if (e.getField().equals("fansNumFile")) {
				result.addError(new FieldError("weixin", e.getField(), "文件类型["
						+ vo.getFansNumFile().getOriginalFilename()
								.split("\\.")[1]
						+ "]不允许上传，允许上传的文件类型为：jpg, png, bmp"));
				model.addAttribute("fansNumFile", true);
			} else {
				result.addError(new FieldError("weixin", e.getField(), e
						.getMessage()));
			}
			bean = new MessageBean(0, "修改失败！");
		}
		if (result.hasErrors()) {
			TokenInterceptor.newToken(request);
			if (StringUtils.isNotBlank(vo.getId())) {
				Media media = mediaService.findOneMedia(vo.getId());
				setAttributeEdit(model, media);
				return "member/media/"
						+ vo.getMediaType().substring(8).toLowerCase()
						+ "/edit";
			} else {
				setAttribute2Create(model, vo);
				return "member/media/"
						+ vo.getMediaType().substring(8).toLowerCase()
						+ "/create";
			}
		} else {
			redirectAttrs.addFlashAttribute("message", bean.toJSON());
			return "redirect:/member/media/list";
		}

	}

}

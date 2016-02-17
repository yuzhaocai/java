package com.lczy.media.controller.admin;

import java.util.List;

import javax.servlet.ServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lczy.common.web.Token;
import com.lczy.common.web.Token.Type;
import com.lczy.media.entity.DicItem;
import com.lczy.media.entity.MediaInput;
import com.lczy.media.service.DicService;
import com.lczy.media.service.MediaInputService;
import com.lczy.media.util.Constants;
import com.lczy.media.util.FileServerUtils;

/**
 * 媒体录入
 * 
 * @author tiger
 *
 */
@Controller
@RequestMapping("/admin/mediaInput")
public class AdminMediaInputController {
	
	private static Logger log = LoggerFactory.getLogger(AdminMediaInputController.class);
	
	@Autowired
	private MediaInputService mediaInputService;
	
	@Autowired
	private DicService dicService;
	
	/**
	 * 列表页
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value={"", "/"})
	public String index(ServletRequest request, Model model) {
		List<MediaInput> data= mediaInputService.find(null);
		model.addAttribute("data", data);
		
		return "/admin/mediaInput/index";
	}
	
	/**
	 * 媒体 - 创建表单
	 * 
	 * @param model
	 * @return
	 */
	@Token
	@RequestMapping(value="create", method = RequestMethod.GET)
	public String createForm(String mediaType, Model model) {
		MediaInput media = new MediaInput();
		media.setMediaType(mediaType);
		model.addAttribute("media", media);
		model.addAttribute("action", "create");
		
		setDicItems(mediaType, model);
		return "/admin/mediaInput/form";
	}

	/**
	 * 媒体 - 创建
	 * 
	 * @param customer
	 * @param redirectAttributes
	 * @return
	 */
	@Token(Type.REMOVE)
	@RequestMapping(value="create", method = RequestMethod.POST)
	public String create(MediaInput media, MultipartFile logo, RedirectAttributes redirectAttributes) {
		try {
			if (!logo.isEmpty()) {
				String fileId = FileServerUtils.upload(null, logo.getName(), logo.getBytes(), false, "image", false);
				media.setShowPic(fileId);
			}
			mediaInputService.save(media);
			redirectAttributes.addFlashAttribute("message", "创建成功!");
		} catch (Exception e) {
		}
		return "redirect:/admin/mediaInput";
	}
	
	/**
	 * 修改媒体 - 表单
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@Token
	@RequestMapping(value="update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable String id, Model model) {
		MediaInput media = mediaInputService.get(id);
		model.addAttribute("media", media);
		model.addAttribute("action", "update");
		
		setDicItems(media.getMediaType(), model);
		return "/admin/mediaInput/form";
	}
	
	/**
	 * 修改媒体 - 更新
	 * 
	 * @param media
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="update", method = RequestMethod.POST)
	public String update(@ModelAttribute("preload") MediaInput media, MultipartFile logo, RedirectAttributes redirectAttributes) {
		try {
			if (!logo.isEmpty()) {
				String fileId = FileServerUtils.upload(null, logo.getName(), logo.getBytes(), false, "image", false);
				media.setShowPic(fileId);
			}
			
			mediaInputService.save(media);
			redirectAttributes.addFlashAttribute("message", "修改成功!");
		} catch (Exception e) {
		}
		return "redirect:/admin/mediaInput";
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@ModelAttribute("preload")
	public MediaInput preload(@RequestParam(required = false) String id) {
		MediaInput media = new MediaInput();
		if (StringUtils.isNotBlank(id)) {
			return mediaInputService.get(id);
		}
		
		return media;
	}
	
	/**
	 * 查看媒体
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="view/{id}")
	public String view(@PathVariable String id, Model model) {
		MediaInput media = mediaInputService.get(id);
		model.addAttribute("media", media);
		return "/admin/mediaInput/view";
	}
	
	/**
	 * 屏蔽媒体
	 * 
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="audit/{id}", method = RequestMethod.POST)
	public String audit(@PathVariable String id, RedirectAttributes redirectAttributes) {
		try {
			mediaInputService.audit(id);
			redirectAttributes.addFlashAttribute("message", "审核成功!");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			redirectAttributes.addFlashAttribute("message", "审核失败:" + e.getMessage());
		}
		return "redirect:/admin/mediaInput";
	}
	
	/**
	 * 查询字典传给页面
	 * 
	 * @param mediaType
	 * @param model
	 */
	private void setDicItems(String mediaType, Model model) {
		if (mediaType.equals(Constants.MediaType.WEIBO)) {
			List<DicItem> weiboCategories = dicService.findItemByCodeType(Constants.WeiboCategory.DIC_CODE);
			List<DicItem> weiboServices = dicService.findItemByCodeType(Constants.WeiboService.DIC_CODE);
			List<DicItem> weiboFans = dicService.findItemByCodeType(Constants.WeiboFans.DIC_CODE);
			
			model.addAttribute("categories", weiboCategories);
			model.addAttribute("services", weiboServices);
			model.addAttribute("fansDirs", weiboFans);
			
		} else {
			List<DicItem> weixinCategories = dicService.findItemByCodeType(Constants.WeixinCategory.DIC_CODE);
			List<DicItem> weixinServices = dicService.findItemByCodeType(Constants.WeixinService.DIC_CODE);
			List<DicItem> weixinFans = dicService.findItemByCodeType(Constants.WeixinFans.DIC_CODE);
			model.addAttribute("categories", weixinCategories);
			model.addAttribute("services", weixinServices);
			model.addAttribute("fansDirs", weixinFans);
		}
		
		List<DicItem> products = dicService.findItemByCodeType(Constants.WeixinFitProduct.DIC_CODE);
		List<DicItem> industryTypes = dicService.findItemByCodeType(Constants.IndustryType.DIC_CODE);
		
		model.addAttribute("products", products);
		model.addAttribute("industryTypes", industryTypes);
	}
	
}

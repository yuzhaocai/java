package com.lczy.media.controller.admin;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;

import com.lczy.common.web.WebHelper;
import com.lczy.media.entity.DicItem;
import com.lczy.media.entity.Media;
import com.lczy.media.entity.MediaStar;
import com.lczy.media.entity.MediaTag;
import com.lczy.media.service.DicService;
import com.lczy.media.service.MediaService;
import com.lczy.media.service.MediaStarService;
import com.lczy.media.service.MediaTagService;
import com.lczy.media.util.Constants;
import com.lczy.media.util.FileServerUtils;
import com.lczy.media.util.UserContext;
import com.mchange.v1.lang.BooleanUtils;

@Controller
@RequestMapping("/admin/media/media")
public class AdminMediaController {
	
	private static Logger log = LoggerFactory.getLogger(AdminMediaController.class);
	
	@Autowired
	private MediaService mediaService;
	
	@Autowired
	private DicService dicService;
	
	@Autowired
	private MediaTagService mediaTagService;
	
	@Autowired
	private MediaStarService mediaStarService;
	
	
	@RequestMapping(value={"", "/"})
	public String index(ServletRequest request, Model model) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		
		int page = WebHelper.getPage(request);
		int size = WebHelper.getPageSize(request);
		
		String startTime = (String)searchParams.get("GTE_createTime");
		String endTime = (String)searchParams.get("LTE_createTime");
		if (StringUtils.isNotBlank(startTime)) {
			DateTime start = DateTime.parse(startTime + "T00:00:00");
			searchParams.put("GTE_createTime", start.toDate());
		}
		if (StringUtils.isNotBlank(endTime)) {
			DateTime end = DateTime.parse(endTime + "T23:59:59");
			searchParams.put("LTE_createTime", end.toDate());
		}
		
		String certified = (String)searchParams.get("EQ_customer.certified");
		if (StringUtils.isNotBlank(certified)) {
			searchParams.put("EQ_customer.certified", BooleanUtils.parseBoolean(certified));
		}
		
		String sort = WebHelper.getString(request, "sort");
		if (StringUtils.isBlank(sort)) {
			sort = "DESC_modifyTime";
		}
		//过滤未审核媒体
		searchParams.put("NEQ_status", Constants.MediaStatus.AUDIT);
		
		Page<Media> data= mediaService.find(searchParams, page, size, sort);
		
		model.addAttribute("data", data);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		
		List<DicItem> mediaTypes = dicService.findItemByCodeType(Constants.MediaType.DIC_CODE);
		model.addAttribute("mediaTypes", mediaTypes);
		List<DicItem> mediaLevels = dicService.findItemByCodeType(Constants.MediaLevel.DIC_CODE);
		model.addAttribute("mediaLevels", mediaLevels);
		List<DicItem> mediaStatus = dicService.findItemByCodeType(Constants.MediaStatus.DIC_CODE);
		//页面查询条件去掉 “未审核”状态
		mediaStatus.remove(0);
		model.addAttribute("mediaStatus", mediaStatus);
		searchParams = new HashMap<String, Object>();
		model.addAttribute("mediaStars",mediaStarService.find(searchParams));
		return "/admin/media/media/index";
	}
	
	/**
	 * 屏蔽媒体
	 * 
	 * @param id
	 * @param redirectAttributes
	 * @param status = MEDIA_S_DISABLED 开放
	 * @param status = MEDIA_S_NORMAL 屏蔽
	 * @return
	 */
	@RequestMapping(value="disable", method = RequestMethod.POST)
	@ResponseBody
	public boolean disable(String id,String status, RedirectAttributes redirectAttributes) {
		System.out.println(status);
		mediaService.auditReject(id,status);
		String msg ="";
		if(status.equals(Constants.MediaStatus.DISABLED)){
			msg="开放成功!";
		}else{
			msg="屏蔽成功!";
		}
		redirectAttributes.addFlashAttribute("message", msg);
		return true;
	}
	
	/**
	 * 设置媒体级别
	 * 
	 * @param id
	 * @param level
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="assignLevel", method = RequestMethod.POST)
	@ResponseBody
	public boolean assignLevel(String id, String level, RedirectAttributes redirectAttributes) {
		mediaService.assignLevel(id, level);
		redirectAttributes.addFlashAttribute("message", "级别设置成功!");
		return true;
	}
	
	
	/**
	 * 查看媒体
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="view")
	public String view(String id, Model model) {
		Media media = mediaService.get(id);
		String tagNames = "";
		if(StringUtils.isNotBlank(media.getTags())){
			String[] mediaTagIds = media.getTags().split(",");
			for (String mediaTagId : mediaTagIds) {
				MediaTag temp = mediaTagService.get(mediaTagId);
				tagNames += temp.getName() + " ";
			}
		}
		media.setTags(tagNames);
		model.addAttribute("media", media);
		return "/admin/media/media/view";
	}
	
	/**
	 * 修改媒体 - 表单
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable String id, Model model) {
		Media media = mediaService.get(id);
		model.addAttribute("media", media);
		setDicItems(media.getMediaType(), model);
		
		if (media.getMediaType().equals(Constants.MediaType.WEIBO)) {
			return "/admin/media/media/weiboForm";
		} else {
			return "/admin/media/media/weixinForm";
		}
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
	
	/**
	 * 修改媒体 - 更新
	 * 
	 * @param media
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="update", method = RequestMethod.POST)
	public String update(@ModelAttribute("preload") Media media, MultipartFile logo, RedirectAttributes redirectAttributes) {
		try {
			if (!logo.isEmpty()) {
				String fileId = FileServerUtils.upload(null, logo.getName(), logo.getBytes(), false, "image", false);
				media.setShowPic(fileId);
			}
			media.setModifyBy(UserContext.getCurrent().getId());
			media.setModifyTime(new Date());
			mediaService.save(media);
			redirectAttributes.addFlashAttribute("message", "修改成功!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", "修改失败：" + e.getMessage());
		}
		return "redirect:/admin/media/media";
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@ModelAttribute("preload")
	public Media preload(@RequestParam(required = false) String id) {
		Media media = new Media();
		if (StringUtils.isNotBlank(id)) {
			return mediaService.get(id);
		}
		
		return media;
	}

	/**
	 * 检查weibo名是否重复.
	 */
	@RequestMapping(value = "checkWeiboName")
	@ResponseBody
	public boolean checkWeiboName(String oldName, String name, ServletRequest request) {
		if (name.equals(oldName)) {
			return true;
		} 

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_name", name);
		searchParams.put("EQ_mediaType", Constants.MediaType.WEIBO);
		return mediaService.findOne(searchParams) == null;
	}

	/**
	 * 检查微信号是否重复，两个空微信号媒体不算重复
	 */
	@RequestMapping(value = "checkWeixinAccount")
	@ResponseBody
	public boolean checkWeixinAccount(String oldAccount, String account, ServletRequest request) {
		if (account.equals(oldAccount)) {
			return true;
		} 
		
		if (StringUtils.isBlank(account)) {
			return true;
		} 

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_account", account);
		searchParams.put("EQ_mediaType", Constants.MediaType.WEIXIN);
		return mediaService.findOne(searchParams) == null;
	}
	
}

package com.lczy.media.controller.admin;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;

import com.google.common.collect.Maps;
import com.lczy.common.web.WebHelper;
import com.lczy.media.entity.MediaTag;
import com.lczy.media.entity.MediaTagMergeLog;
import com.lczy.media.service.MediaService;
import com.lczy.media.service.MediaTagMergeLogService;
import com.lczy.media.service.MediaTagService;
import com.lczy.media.util.UserContext;

@Controller
@RequestMapping("/admin/media/mediaTag")
public class AdminMediaTagController {
	
	private static Logger log = LoggerFactory.getLogger(AdminMediaTagController.class);
	
	@Autowired
	private MediaService mediaService;
	
	@Autowired
	private MediaTagService mediaTagService;
	
	@Autowired
	private MediaTagMergeLogService mediaTagMergeLogService;
	
	/**
	 * 媒体标签 - 首页
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value={"", "/"})
	public String index(ServletRequest request, Model model) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		
		String sort = WebHelper.getString(request, "sort");
		if (StringUtils.isBlank(sort)) {
			sort = "DESC_count";
		}
		
		List<MediaTag> data= mediaTagService.find(searchParams, sort);
		model.addAttribute("tags", data);
		
		return "/admin/media/mediaTag/index";
	}
	
	/**
	 * 媒体标签 - 合并
	 * 
	 * @param masterId
	 * @param slaveId
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="merge", method = RequestMethod.POST)
	public String merge(String masterId, String slaveId, RedirectAttributes redirectAttributes) {
		mediaService.merge(masterId, slaveId);
		redirectAttributes.addFlashAttribute("message", "合并成功!");
		return "redirect:/admin/media/mediaTag/";
	}
	
	/**
	 * 媒体标签 - 创建
	 * 
	 * @param tag
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(MediaTag tag, RedirectAttributes redirectAttributes) {
		MediaTag mediaTag = mediaTagService.findByTagName(tag.getName().trim());
		String msg = "创建成功!";
		if (mediaTag == null) {
			mediaService.saveMediaTag(tag);
		} else {
			mediaTag.setModifyBy(UserContext.getCurrent().getId());
			mediaTag.setModifyTime(new Date());
			if(tag.getHot()&&!mediaTag.getHot()){//将已存在便签变为热门，并且查出标签数据不是热门
				mediaTag.setHot(true);
				mediaService.saveMediaTag(mediaTag);
			}else if(tag.isRec()&&!mediaTag.isRec()){//将已存在便签变为推荐，并且查出标签数据不是推荐
				mediaTag.setRec(true);
				mediaService.saveMediaTag(mediaTag);
			}else{//已存在的便签操作数据库，并返回提示
				msg = "{\"result\":false, \"content\": \"创建失败，已有标签："+ tag.getName() + "！\"}";
			}
		}
		redirectAttributes.addFlashAttribute("message", msg);
		return "redirect:/admin/media/mediaTag/";
	}
	
	/**
	 * 媒体标签 - 删除
	 * 
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="delete", method = RequestMethod.POST)
	public String delete(String id,boolean sign, RedirectAttributes redirectAttributes) {
		MediaTag mediaTag = mediaTagService.get(id);
		mediaTag.setModifyBy(UserContext.getCurrent().getId());
		mediaTag.setModifyTime(new Date());
		if (sign) {
			mediaTag.setHot(false);
		} else { 
			mediaTag.setRec(false);
		}
		mediaTagService.save(mediaTag);
		redirectAttributes.addFlashAttribute("message", "删除成功!");
		return "redirect:/admin/media/mediaTag/";
	}
	
	/**
	 * 媒体标签 - 合并日志
	 * 
	 * @return
	 */
	@RequestMapping(value="mergeLog")
	public String mergeLog(ServletRequest request, Model model) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		
		int page = WebHelper.getPage(request);
		int size = WebHelper.getPageSize(request);
		
		String sort = WebHelper.getString(request, "sort");
		if (StringUtils.isBlank(sort)) {
			sort = "DESC_createTime";
		}
		Page<MediaTagMergeLog> data = mediaTagMergeLogService.find(searchParams, page, size, sort);
		model.addAttribute("data", data);
		return "/admin/media/mediaTag/mergeLog";
	}
	
}

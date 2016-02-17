package com.lczy.media.controller.admin;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;

import com.lczy.common.web.Token;
import com.lczy.common.web.Token.Type;
import com.lczy.common.web.WebHelper;
import com.lczy.media.entity.DicItem;
import com.lczy.media.entity.OtherMedia;
import com.lczy.media.service.DicService;
import com.lczy.media.service.OtherMediaService;
import com.lczy.media.util.Constants;
import com.lczy.media.util.FileServerUtils;
import com.lczy.media.vo.OtherMediaVO;

/**
 * 更多媒体管理 controller
 * 
 * @author wanghaibin
 *
 */
@Controller
@RequestMapping(value = "/admin/otherMedia/")
public class AdminOtherMediaController {

	private static Logger log = LoggerFactory.getLogger(AdminOtherMediaController.class);
	
	@Autowired
	private OtherMediaService otherMediaService;
	
	@Autowired
	private DicService dicService;
	
	/**
	 * 更多媒体管理 - 首页
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "")
	public String index(Model model, ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		
		int page = WebHelper.getPage(request);
		int size = WebHelper.getPageSize(request);
		
		String sort = WebHelper.getString(request, "sort");
		if (StringUtils.isBlank(sort)) {
			sort = "DESC_createTime";
		}
		
		String startTime = (String)searchParams.get("GTE_modifyTime");
		String endTime = (String)searchParams.get("LTE_modifyTime");
		if (StringUtils.isNotBlank(startTime)) {
			DateTime start = DateTime.parse(startTime + "T00:00:00");
			searchParams.put("GTE_modifyTime", start.toDate());
		}
		if (StringUtils.isNotBlank(endTime)) {
			DateTime end = DateTime.parse(endTime + "T23:59:59");
			searchParams.put("LTE_modifyTime", end.toDate());
		}
		
		Page<OtherMedia> data= otherMediaService.find(searchParams, page, size, sort);
		
		model.addAttribute("data", data);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		
		List<DicItem> categories = dicService.findItemByCodeType(Constants.OtherMediaCategory.DIC_CODE);
		model.addAttribute("categories", categories);
		
		return "admin/otherMedia/index";
	}
	
	/**
	 * 更多媒体 - 创建表单
	 * 
	 * @param model
	 * @return
	 */
	@Token
	@RequestMapping(value="create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("action", "create");
		List<DicItem> categories = dicService.findItemByCodeType(Constants.OtherMediaCategory.DIC_CODE);
		model.addAttribute("categories", categories);
		List<DicItem> industryTypes = dicService.findItemByCodeType(Constants.IndustryType.DIC_CODE);
		model.addAttribute("industryTypes", industryTypes);
		return "/admin/otherMedia/form";
	}

	/**
	 * 更多媒体 - 创建
	 * 
	 * @param customer
	 * @param redirectAttributes
	 * @return
	 */
	@Token(Type.REMOVE)
	@RequestMapping(value="create", method = RequestMethod.POST)
	public String create(OtherMedia media, MultipartFile logo, MultipartFile attach, RedirectAttributes redirectAttributes) {
		try {
			if (!logo.isEmpty()) {
				String fileId = FileServerUtils.upload(null, logo.getOriginalFilename(), logo.getBytes(), false, "image", false);
				media.setShowPic(fileId);
			}
			if (!attach.isEmpty()) {
				String fileId = FileServerUtils.upload(null, attach.getOriginalFilename(), attach.getBytes(), false, "file", false);
				media.setAttachment(fileId);
			}
			otherMediaService.createOtherMedia(media);
			redirectAttributes.addFlashAttribute("message", "创建成功!");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			redirectAttributes.addFlashAttribute("message", "创建失败：" + e.getMessage());
		}
		return "redirect:/admin/otherMedia/";
	}

	/**
	 * 更多媒体 - 修改表单
	 * 
	 * @param model
	 * @return
	 */
	@Token
	@RequestMapping(value="update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable String id, Model model) {
		OtherMedia media = otherMediaService.get(id);
		model.addAttribute("media", media);
		model.addAttribute("action", "update");
		List<DicItem> categories = dicService.findItemByCodeType(Constants.OtherMediaCategory.DIC_CODE);
		model.addAttribute("categories", categories);
		List<DicItem> industryTypes = dicService.findItemByCodeType(Constants.IndustryType.DIC_CODE);
		model.addAttribute("industryTypes", industryTypes);
		
		return "/admin/otherMedia/form";
	}

	/**
	 * 修改媒体 - 更新
	 * 
	 * @param media
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="update", method = RequestMethod.POST)
	public String update(OtherMediaVO media, MultipartFile logo, MultipartFile attach, RedirectAttributes redirectAttributes) {
		try {
			if (!logo.isEmpty()) {
				String fileId = FileServerUtils.upload(null, logo.getOriginalFilename(), logo.getBytes(), false, "image", false);
				media.setShowPic(fileId);
			}
			if (!attach.isEmpty()) {
				String fileId = FileServerUtils.upload(null, attach.getOriginalFilename(), attach.getBytes(), false, "file", false);
				media.setAttachment(fileId);
			}
			otherMediaService.updateOtherMedia(media);
			redirectAttributes.addFlashAttribute("message", "修改成功!");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			redirectAttributes.addFlashAttribute("message", "保存失败：" + e.getMessage());
		}
		return "redirect:/admin/otherMedia/";
	}
	
	/**
	 * 删除媒体
	 * 
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="delete/{id}", method = RequestMethod.POST)
	public String delete(@PathVariable String id, RedirectAttributes redirectAttributes) {
		try {
			otherMediaService.deleteValidate(id);
			redirectAttributes.addFlashAttribute("message", "删除成功!");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			redirectAttributes.addFlashAttribute("message", "删除失败：" + e.getMessage());
		}
		
		return "redirect:/admin/otherMedia/";
	}
	
}

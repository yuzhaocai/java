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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.lczy.common.web.WebHelper;
import com.lczy.media.controller.common.BaseController;
import com.lczy.media.entity.DicItem;
import com.lczy.media.entity.Media;
import com.lczy.media.entity.MediaStar;
import com.lczy.media.service.DicService;
import com.lczy.media.service.MediaService;
import com.lczy.media.service.MediaStarService;
import com.lczy.media.util.Constants;
import com.mchange.v1.lang.BooleanUtils;

/**
 * 报价属性 controller
 * 
 * @author wang.haibin
 *
 */
@Controller
public class AdminMediaStarController extends BaseController{

	private static Logger log = LoggerFactory.getLogger(AdminMediaStarController.class);
	
	@Autowired
	private MediaStarService mediaStarService;

	@Autowired
	private MediaService mediaService;

	@Autowired
	private DicService dicService;
	
	/**
	 * 报价属性首页
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/admin/mediaStar")
	public String index(Model model) throws Exception {
		List<MediaStar> mediaStars = mediaStarService.find(null);
		model.addAttribute("mediaStars", mediaStars);
		return "admin/mediaStar/index";
	}

	/**
	 * 星级媒体
	 * 
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/admin/mediaStar/medias")
	public String medias(Model model, ServletRequest request) throws Exception {
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
				
		Page<Media> data = mediaService.find(searchParams, page, size, null);
		model.addAttribute("data", data);
		List<DicItem> mediaTypes = dicService.findItemByCodeType(Constants.MediaType.DIC_CODE);
		model.addAttribute("mediaTypes", mediaTypes);
		List<DicItem> mediaLevels = dicService.findItemByCodeType(Constants.MediaLevel.DIC_CODE);
		model.addAttribute("mediaLevels", mediaLevels);
		List<DicItem> mediaStatus = dicService.findItemByCodeType(Constants.MediaStatus.DIC_CODE);
		//页面查询条件去掉 “未审核”状态
		mediaStatus.remove(0);
		model.addAttribute("mediaStatus", mediaStatus);

		List<MediaStar> mediaStars = mediaStarService.find(null);
		model.addAttribute("mediaStars", mediaStars);
		return "admin/mediaStar/medias";
	}

	/**
	 * 设置星级
	 * 
	 * @param id
	 * @param mediaId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/mediaStar/assign", method = RequestMethod.POST)
	@ResponseBody
	public boolean assign(String id, String mediaId) throws Exception {
		try {
			mediaStarService.assign(id, mediaId);
			log.info("修改星级成功！");
			return true;
		} catch (Exception e) {
			log.error("修改星级失败：{}", e.getMessage());
			return false;
		}
	}
	
}

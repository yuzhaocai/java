package com.lczy.media.controller.admin;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;

import com.google.common.collect.Lists;
import com.lczy.common.web.Token;
import com.lczy.common.web.Token.Type;
import com.lczy.common.web.WebHelper;
import com.lczy.media.controller.MediaController;
import com.lczy.media.entity.Activity;
import com.lczy.media.entity.Area;
import com.lczy.media.entity.Media;
import com.lczy.media.entity.MediaActivity;
import com.lczy.media.entity.MediaTag;
import com.lczy.media.service.ActivityService;
import com.lczy.media.service.AreaService;
import com.lczy.media.service.MediaActivityService;
import com.lczy.media.service.MediaQuoteLogService;
import com.lczy.media.service.MediaService;
import com.lczy.media.service.MediaTagService;
import com.lczy.media.service.common.AreaProvider;
import com.lczy.media.service.common.DicProvider;
import com.lczy.media.solr.MediaDoc;
import com.lczy.media.util.Constants;
import com.lczy.media.util.Constants.IndustryType;
import com.lczy.media.util.Constants.MediaType;
import com.lczy.media.util.JsonBean;
import com.lczy.media.vo.ActivityVO;
import com.lczy.media.vo.MediaSearchVo;

@Controller
@RequestMapping("/admin/activity")
public class AdminActivityController extends MediaController{

	private static Logger log = LoggerFactory
			.getLogger(AdminActivityController.class);

	@Autowired
	private MediaService mediaService;
	
	@Autowired
	protected AreaService areaService;
	
	@Autowired
	private MediaTagService mediaTagService;


	@Autowired
	private ActivityService activityService;

	@Autowired
	private MediaActivityService mediaActivityService;

	@Autowired
	private MediaQuoteLogService mediaQuoteLogService;

	@Autowired
	protected DicProvider dicProvider;

	@Autowired
	protected AreaProvider areaProvider;

	@RequestMapping(value = { "", "/" })
	public String index(ServletRequest request, Model model) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(
				request, "search_");

		int page = WebHelper.getPage(request);
		int size = WebHelper.getPageSize(request);

		String sort = WebHelper.getString(request, "sort");
		if (StringUtils.isBlank(sort)) {
			sort = "DESC_createTime";
		}
		Page<Activity> data = activityService.find(searchParams, page, size,
				sort);
		for (Activity activity : data)
			activity.setMediaNum(mediaActivityService.getMediaNum(activity
					.getId()));
		model.addAttribute("data", data);
		return "/admin/activity/index";
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	@Token(Type.NEW)
	public String createForm(Model model) {
		setAttribute4Create(model);
		return "/admin/activity/activityForm";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@Token(Type.REMOVE)
	public String create(String mediaIds, ActivityVO vo,String activityName,
			RedirectAttributes redirectAttributes) throws Exception {
		try {
			String[] ids = mediaIds.split(",");
			Activity activity = activityService.save(vo, ids.length , activityName);
			if (activity != null && activity.getId() != null) {
				List<MediaActivity> mediaActivities = mediaActivityService
						.save(activity, ids);
				if (mediaActivities != null && mediaActivities.size() > 0) {
					for (MediaActivity mediaActivity : mediaActivities) {
						mediaQuoteLogService.joinActivity(
								mediaActivity.getMedia(), activity);
					}
					log.info("添加活动成功！");
					redirectAttributes.addFlashAttribute("message", "添加活动成功！");
				}
			}
		} catch (Exception e) {
			log.error("添加活动失败:{}", e.getMessage());
			redirectAttributes.addFlashAttribute("message",
					"添加活动失败," + e.getMessage());
		}
		return "redirect:/admin/activity";
	}

	@RequestMapping(value = "/disable/{id}", method = RequestMethod.GET)
	public String disable(@PathVariable String id, Model model)
			throws Exception {
		activityService.disable(id, Constants.ActivityStatus.INACTIVE);
		return "redirect:/admin/activity";
	}

	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
	public String view(@PathVariable String id, ServletRequest request,
			Model model) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(
				request, "search_");

		int page = WebHelper.getPage(request);
		int size = WebHelper.getPageSize(request);

		String sort = WebHelper.getString(request, "sort");
		searchParams.put("EQ_activity.id", id);

		Page<MediaActivity> data = mediaActivityService.find(searchParams,
				page, size, sort);

		model.addAttribute("data", data);
		model.addAttribute("activityName", data.getContent().get(0)
				.getActivity().getName());
		return "/admin/activity/view";
	}
	
	
	@RequestMapping("load")
	@ResponseBody
	public String load(MediaSearchVo vo) {
		
		Map<String, Object> fieldsMap = getFieldsMap(vo);
		
		JsonBean result = new JsonBean();
		try {
			com.lczy.media.solr.Page<MediaDoc> page = mediaService.find(fieldsMap, vo.getSort(), vo.getPageNum(), vo.getPageSize());
			result = toJsonBean(page);
			result.put("result", true);
		} catch (Exception e) {
			log.error("solr查询失败", e);
			result.put("result", false);
		}
		
		return result.toJson();
	}

	
	@RequestMapping(value = "/medias", method = RequestMethod.POST)
	public String medias(String arr, Model model) {
		if (arr != null && arr.split(",").length > 0) {
			String[] ids =  arr.split(",");
			List<Media> medias = Lists.newArrayList();
			for (String id : ids) {
				Media media = mediaService.get(id);
				medias.add(media);
			}
			model.addAttribute("medias", medias);
		}
		return "/admin/activity/medias";
	}
	
	protected void setAttribute4Create(Model model) {
		model.addAttribute("weibo",
				dicProvider.getDic(Constants.WeiboCategory.DIC_CODE)
						.getDicItems());
		model.addAttribute("weixin",
				dicProvider.getDic(Constants.WeixinCategory.DIC_CODE)
						.getDicItems());
		model.addAttribute("mediaTypes", dicProvider.getDic(MediaType.DIC_CODE)
				.getDicItems());
		model.addAttribute("regions", getRegions());
		model.addAttribute("industryTypes",
				dicProvider.getDic(IndustryType.DIC_CODE).getDicItems());
		model.addAttribute("tags", getRecTags());
	}
	
	private List<MediaTag> getRecTags() {
		return mediaTagService.findRecTags();
	}
	
	private List<Area> getRegions() {
		return areaProvider.getProvinceMap().get(AreaProvider.HOT_CITIES_KEY);
	}
}

package com.lczy.media.controller.admin;

import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springside.modules.web.Servlets;

import com.lczy.common.web.Token;
import com.lczy.common.web.Token.Type;
import com.lczy.common.web.WebHelper;
import com.lczy.media.entity.DicItem;
import com.lczy.media.entity.Intention;
import com.lczy.media.entity.User;
import com.lczy.media.service.DicService;
import com.lczy.media.service.IntentionService;
import com.lczy.media.util.Constants;
import com.lczy.media.util.UserContext;

/**
 * 更多媒体订单查询 controller
 * @author wang.xiaoxiang
 *
 */
@Controller
@RequestMapping(value = "admin/intention")
public class AdminIntentionController {

	private static Logger log = LoggerFactory.getLogger(AdminIntentionController.class);
	
	@Autowired
	private IntentionService intentionService;
	
	@Autowired
	private DicService dicService;
	
	/**
	 * 订单查询 - 首页
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping({"", "list"})
	public String index(Model model, ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		
		int page = WebHelper.getPage(request);
		int size = WebHelper.getPageSize(request);
		
		String sort = WebHelper.getString(request, "sort");
		if (StringUtils.isBlank(sort)) {
			sort = "DESC_createTime";
		}
		
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
		
		Page<Intention> data= intentionService.find(searchParams, page, size, sort);
		
		model.addAttribute("data", data);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		
		List<DicItem> inventionStatus = dicService.findItemByCodeType(Constants.IntentionStatus.DIC_CODE);
		model.addAttribute("inventionStatus", inventionStatus);
		
		List<DicItem> categories = dicService.findItemByCodeType(Constants.OtherMediaCategory.DIC_CODE);
		model.addAttribute("otherMediaCategories", categories);
		return "admin/intention/list";
	}

	
	@RequestMapping(value= "deal" )
	@Token
	public String deal(Model model, String id) {
		model.addAttribute("intention", intentionService.get(id));
		return "admin/intention/deal";
	}
	@RequestMapping(value= "view" )
	@Token
	public String view(Model model, String id) {
		model.addAttribute("intention", intentionService.get(id));
		return "admin/intention/view";
	}
	
	@RequestMapping(value = "dealintention", method = RequestMethod.POST)
	@Token(Type.REMOVE)
	public String dealintention(@RequestParam("id") String id, @RequestParam("custManager") String custManager,
			@RequestParam("handleResult") String handleResult) {
		Intention intention = intentionService.get(id);
		intention.setCustManager(custManager);
		intention.setHandleResult(handleResult);
		User currentUser = UserContext.getCurrent();
		intention.setHandler(currentUser);
		intention.setStatus(Constants.IntentionStatus.PROCESSED);
		intention.setHandleTime(new Date());
		intentionService.save(intention);
		return "redirect:list";
	}
	
}

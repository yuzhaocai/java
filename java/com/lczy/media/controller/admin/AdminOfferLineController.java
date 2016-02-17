package com.lczy.media.controller.admin;

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
import org.springside.modules.web.Servlets;

import com.lczy.common.web.WebHelper;
import com.lczy.media.controller.MediaController;
import com.lczy.media.entity.OfferLine;
import com.lczy.media.service.OfferLineService;

@Controller
@RequestMapping("/admin/offerline")
public class AdminOfferLineController extends MediaController {

	private static Logger log = LoggerFactory
			.getLogger(AdminOfferLineController.class);

	@Autowired
	private OfferLineService offerLineService;

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

		String startTime = (String) searchParams.get("GTE_createTime");
		String endTime = (String) searchParams.get("LTE_createTime");
		if (StringUtils.isNotBlank(startTime)) {
			DateTime start = DateTime.parse(startTime + "T00:00:00");
			searchParams.put("GTE_createTime", start.toDate());
		}
		if (StringUtils.isNotBlank(endTime)) {
			DateTime end = DateTime.parse(endTime + "T23:59:59");
			searchParams.put("LTE_createTime", end.toDate());
		}
		
		Page<OfferLine> data = offerLineService.find(searchParams, page, size,
				sort);
		model.addAttribute("data", data);
		return "admin/offerline/index";
	}

}

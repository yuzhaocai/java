package com.lczy.media.controller.admin;

import java.util.Date;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;

import com.lczy.common.web.WebHelper;
import com.lczy.media.controller.common.BaseController;
import com.lczy.media.entity.Opinion;
import com.lczy.media.service.OpinionService;
import com.lczy.media.service.common.DicProvider;
import com.lczy.media.util.UserContext;
@Controller
@RequestMapping("/admin/opinion")
public class OpinionController extends BaseController{
	
	private static Logger log = LoggerFactory.getLogger(OpinionController.class);
	
	@Autowired
	private OpinionService opinionService;
	
	@Autowired
	protected DicProvider dicProvider;
	
	@RequestMapping(value={"/list/{type}"})
	public String index(Model model,@PathVariable String type, ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_feedbackType", type);
		int page = WebHelper.getPage(request);
		int size = WebHelper.getPageSize(request);
		String sort = WebHelper.getString(request, "sort");
		if (StringUtils.isBlank(sort)) {
			sort = "DESC_createTime";
		}
		//判断是否处理
		boolean status = Boolean.valueOf((String)searchParams.get("EQ_status"));
		if(status){
			searchParams.put("EQ_status", status);
		}else{
			searchParams.put("EQ_status", false);
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
		
		String handleResult = (String)searchParams.get("EQ_handleResult");
		if(StringUtils.isNotBlank(handleResult)){
			if(handleResult.equals("已处理")){
				searchParams.remove("EQ_handleResult");
				searchParams.put("NEQ_handleResult", "无效信息");
			}
		}
		Page<Opinion> data= opinionService.find(searchParams, page, size, sort);
		model.addAttribute("opinionTitle", dicProvider.getDicItemName(type));
		model.addAttribute("opinionActive", type);
		model.addAttribute("data", data);
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		
		
		if(status){
			//跳转已处理页面
			return "/admin/opinion/finished";
		}else{
			//跳转未处理页面
			return "/admin/opinion/list";
		}
	}
	
	/**
	 * 处理用户意见
	 * */
	@RequestMapping(value = "/mark")
	public String mark(String id,String handleResult,String feedbackType,RedirectAttributes redirectAttributes) throws Exception  {
		try{
			Opinion op = opinionService.get(id);
			op.setStatus(true);
			op.setHandleBy(UserContext.getCurrent());
			op.setHandleResult(handleResult);
			op.setHandleTime(new Date());
			opinionService.save(op);
			redirectAttributes.addFlashAttribute("message", "处理成功！");
		}catch(Exception e){
			log.error(e.getMessage(), e);
			redirectAttributes.addFlashAttribute("message", "处理失败!");
		}
		return "redirect:/admin/opinion/list/"+feedbackType;
	}
}

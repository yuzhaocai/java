package com.lczy.media.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lczy.common.web.WebHelper;
import com.lczy.media.controller.common.BaseController;
import com.lczy.media.entity.FavoritesReq;
import com.lczy.media.entity.Requirement;
import com.lczy.media.service.FavoritesReqService;
import com.lczy.media.util.JsonBean;
import com.lczy.media.util.UserContext;

/** 收藏媒体控制器
 * @author wang.xiaoxiang
 *
 */
@Controller
@RequestMapping("/member/favoritesReq")
public class FavoritesReqController extends BaseController {
	
	
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private FavoritesReqService favoritesReqService;
	
	
	@RequestMapping(value={"", "list"})
	public String list(Model model, HttpServletRequest request) {
		int page = WebHelper.getPage(request);
		int size = WebHelper.getPageSize(request);
		String sort = WebHelper.getString(request, "sort");
		Map<String, Object> searchParams = getSearchParams(request);
		searchParams.put("EQ_customer.id", UserContext.getCurrentCustomer().getId());
		searchParams.put("EQ_customer.id", UserContext.getCurrentCustomer().getId());
		Date date = null;
		try {
			SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
			date = formater.parse(formater.format(new Date()));
		} catch (ParseException e) {
			date = new Date();
			log.error("时间转换错误", e);
		}
		searchParams.put("GTE_req.deadline", date);
		Page<FavoritesReq> aPage =  favoritesReqService.find(searchParams, page, size, sort);
		setModalAttrsForPaging(model, searchParams, aPage, sort);
		return "member/favorites/req/list";
	}
	
	@RequestMapping(value="invalid")
	public String invalid(Model model, HttpServletRequest request) {
		int page = WebHelper.getPage(request);
		int size = WebHelper.getPageSize(request);
		String sort = WebHelper.getString(request, "sort");
		Map<String, Object> searchParams = getSearchParams(request);
		searchParams.put("EQ_customer.id", UserContext.getCurrentCustomer().getId());
		Date date = null;
		try {
			SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
			date = formater.parse(formater.format(new Date()));
		} catch (ParseException e) {
			date = new Date();
			log.error("时间转换错误", e);
		}
		searchParams.put("LT_req.deadline", date);
		Page<FavoritesReq> aPage =  favoritesReqService.find(searchParams, page, size, sort);
		setModalAttrsForPaging(model, searchParams, aPage, sort);
		return "member/favorites/req/invalid";
	}
	
	
	
	@ResponseBody
	@RequestMapping(value="add")
	public String add(Model model, HttpServletRequest request) {
		String reqId = (String) request.getParameter("reqId");
		JsonBean result = new JsonBean();
		try {
			FavoritesReq entity = new FavoritesReq();
			Requirement req = new Requirement();
			req.setId(reqId);
			entity.setReq(req);
			entity.setCustomer(UserContext.getCurrentCustomer());
			favoritesReqService.save(entity);
			result.put("result", true);
		} catch (Exception e) {
			log.error("添加收藏失败", e);
			result.put("result", false);
		}
		return result.toJson();
	}
	
	@ResponseBody
	@RequestMapping(value="delete")
	public String delete(Model model, HttpServletRequest request) {
		String reqId = (String) request.getParameter("reqId");
		JsonBean result = new JsonBean();
		try {
			Map<String, Object> searchParams = getSearchParams(request);
			searchParams.put("EQ_customer.id", UserContext.getCurrentCustomer().getId());
			searchParams.put("EQ_req.id", reqId);
			FavoritesReq req = favoritesReqService.findOne(searchParams);
			favoritesReqService.remove(req);
			result.put("result", true);
		} catch (Exception e){
			log.error("取消关注失败", e);
			result.put("result", false);
		}
		return result.toJson();
	}
	
	
	
}

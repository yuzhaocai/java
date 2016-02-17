package com.lczy.media.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lczy.common.web.WebHelper;
import com.lczy.media.entity.AdvSetting;
import com.lczy.media.entity.FindCnmei;
import com.lczy.media.service.AdvSettingService;
import com.lczy.media.service.FindCnmeiService;
import com.lczy.media.solr.MediaDoc;
import com.lczy.media.util.Constants;
import com.lczy.media.util.JsonBean;
import com.lczy.media.vo.RequirementVO;

@Controller
@RequestMapping("/findCnmei")
public class FindCnmeiController extends IndexController{
	
	@Autowired
	private FindCnmeiService findCnmeiService;
	
	@RequestMapping({"","/index" })
	public String index(Model model) throws Exception {
		setAdSetting(model);	
		model.addAttribute("active", "findCnmei");
		return "findCnmei";
	}
	
	@RequestMapping("queryCnmei")
	@ResponseBody
	public String queryCnmei(RequirementVO vo, HttpServletRequest request) {
		
		Map<String, Object> fieldsMap = Maps.newHashMap();
		
		int pageNum = WebHelper.getInt(request, "pageNum");
		int pageSize = WebHelper.getInt(request, "pageSize");
		JsonBean result = new JsonBean();
		try {
			Page<FindCnmei> page = findCnmeiService.find(fieldsMap,pageNum, pageSize,"DESC_createTime");
			result = toJsonBean(page,pageNum,pageSize);
			result.put("result", true);
		} catch (Exception e) {
			log.error("solr 查询异常", e);
			result.put("result", false);
		}
		
		return result.toJson();
	}
	
	private JsonBean toJsonBean(Page<FindCnmei> page,int pageNum,int pageSize) {
		JsonBean bean = new JsonBean();
		
		bean.put("total", page.getTotalElements());
		bean.put("totalPage", page.getTotalPages());
		bean.put("pageNum", pageNum);
		bean.put("pageSize", pageSize);
		List<JsonBean> data = Lists.newArrayList();
		for( FindCnmei fc : page.getContent() ) {
			data.add(toJsonBean(fc));
		}
		bean.put("data", data);
		
		return bean;
	}
	
	private JsonBean toJsonBean(FindCnmei fc){
		JsonBean bean = new JsonBean();
		bean.put("id", fc.getId());
		bean.put("createTime", fc.getCreateTime());
		bean.put("img", fc.getImg());
		bean.put("outLine", fc.getOutLine());
		bean.put("title", fc.getTitle());
		bean.put("url", fc.getUrl());
		return bean;
	}
	
	
}

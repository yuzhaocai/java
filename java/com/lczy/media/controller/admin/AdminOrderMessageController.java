package com.lczy.media.controller.admin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;

import com.lczy.common.util.MessageBean;
import com.lczy.common.web.WebHelper;
import com.lczy.media.entity.OrderMessage;
import com.lczy.media.service.OrderMessageService;

/**
 * 订单留言板查询 controller
 * @author
 *
 */
@Controller
@RequestMapping(value = "/admin/order/orderMessage")
public class AdminOrderMessageController {
	
	private static Logger log = LoggerFactory.getLogger(AdminOrderMessageController.class);
	
	@Autowired
	private OrderMessageService orderMessageService;
	
	/**
	 * 订单查询 - 首页
	 * 
	 * @param model
	 * @param request
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping(value = "")
	public String index(Model model, ServletRequest request) throws ParseException {
		
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		int page = WebHelper.getPage(request);
		int size = WebHelper.getPageSize(request);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if(StringUtils.isNotBlank(WebHelper.getString(request,"search_GTE_createTime"))){
			searchParams.put("GTE_createTime", sdf.parse(WebHelper.getString(request,"search_GTE_createTime")+" 00:00:00"));
		}
		if(StringUtils.isNotBlank(WebHelper.getString(request,"search_LTE_createTime"))){
			searchParams.put("LTE_createTime", sdf.parse(WebHelper.getString(request,"search_LTE_createTime")+" 23:59:59"));
		}
		if(StringUtils.isNotBlank(WebHelper.getString(request,"search_EQ_display"))){
			searchParams.put("EQ_display",Boolean.valueOf(WebHelper.getString(request,"search_EQ_display")) );
		}
		String sort = WebHelper.getString(request, "sort");
		if (StringUtils.isBlank(sort)) {
			sort = "DESC_createTime";
		}
		Page<OrderMessage> data= orderMessageService.find(searchParams, page, size, sort);
		
		model.addAttribute("data", data);
		
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		
		return "admin/order/message/index";
	}
	
	/**
	 * 设置留言状态
	 * 
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "display", method = RequestMethod.POST)
	@ResponseBody
	public String messageDisplay(String id,boolean display,RedirectAttributes redirectAttributes) {
		MessageBean bean = null;
		OrderMessage oms = orderMessageService.get(id);
		oms.setDisplay(display);
		orderMessageService.save(oms);
		if(display==true){
			bean = new MessageBean(1, "留言已恢复显示");
		}else if(display==false){
			bean = new MessageBean(1, "留言已屏蔽");
		}
		return bean.toJSON();
	}
}

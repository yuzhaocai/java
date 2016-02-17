package com.lczy.media.mobile.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.lczy.common.util.MyGson;
import com.lczy.media.entity.DicItem;
import com.lczy.media.mobile.util.Response;
import com.lczy.media.service.DicService;
import com.lczy.media.util.JsonBean;

@Controller
@RequestMapping("/mobile/mcReq")
public class GetDicItemController {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private Gson gson = MyGson.getInstance();
	
	@Autowired
	private DicService dicService;
	
	/**
	 * 得到所有字典项.
	 */
	@RequestMapping("getDicItem")
	@ResponseBody
	public Response getOrder(HttpServletRequest httpRequest) {
		Response resp = new Response();
		try {
			List<DicItem> dicList = dicService.findAllItem();
			List<JsonBean> jsonList = Lists.newArrayList();
			for(DicItem dicItem:dicList){
				jsonList.add(diItemToJson(dicItem));
			}
			resp.addBody("items", jsonList);
		} catch (Exception e) {
			log.error("查询字典项失败", e);
			resp = new Response(900, "查询字典项失败");
		}
		
		log.debug("response: {}", gson.toJson(resp));
		
		return resp;
	}
	
	private JsonBean diItemToJson(DicItem dicItem){
    	JsonBean bean = new JsonBean();
		bean.add("item_code", dicItem.getItemCode());
		bean.add("item_name", dicItem.getItemName());
		return bean;
	}
	
}

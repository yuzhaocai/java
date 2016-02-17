package com.lczy.media.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/notFound")
public class NotFoundController extends IndexController{
	
	@RequestMapping("")
	public String index(Model model) throws Exception {
		setAdSetting(model);	
		return "notFound";
	}
	
	
	
}

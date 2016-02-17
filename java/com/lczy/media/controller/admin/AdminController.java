package com.lczy.media.controller.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	private static Logger log = LoggerFactory.getLogger(AdminController.class);
	
	@RequestMapping(value={"", "/"})
	public String main() {
		return "admin/index";
	}
	
	/**
	 * 查看系统环境变量.
	 */
	@RequestMapping("debug/env")
	public String env() {
		return "admin/debug/env";
	}


}

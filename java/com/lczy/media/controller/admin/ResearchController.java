/**
 * 
 */
package com.lczy.media.controller.admin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 竞品分析控制器.
 * 
 * @author wu
 *
 */
@Controller
@RequestMapping("/admin/research")
public class ResearchController {
	
	private static final String SPIDER_URL = "http://180.76.155.162:32767";

	@RequestMapping({"", "/"})
	public String main(HttpServletRequest request) {
		request.setAttribute("url", SPIDER_URL);
		
		return "/admin/research/main";
		
	}

}

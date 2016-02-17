/**
 * 
 */
package com.lczy.media.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author wu
 *
 */
@Controller
public class BadgeController {

	@RequestMapping("/badge/req")
	@ResponseBody
	public String getBadgeNum() {
		//TODO
		return "0";
	}
	
}

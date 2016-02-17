package com.lczy.media.controller.member;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 会员中心.
 * 
 * @author wu
 *
 */
@Controller
@RequestMapping("/member")
public class MemberController {

	@RequestMapping({"", "/"})
	public String main() {
		return "redirect:/member/userInfo";
	}
	
}

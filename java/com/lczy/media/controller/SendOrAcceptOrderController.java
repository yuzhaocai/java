package com.lczy.media.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lczy.media.entity.Dic;
import com.lczy.media.util.Constants;
import com.lczy.media.util.UserContext;

@Controller
@RequestMapping("/order/direct")
public class SendOrAcceptOrderController extends MediaController {

	@RequestMapping({ "", "send" })
	public String send(Model model) {
		setAttribute(model);
		return "order/direct/send";
	}

	@RequestMapping({ "", "accept" })
	public String accept(Model model) {
		setAttribute(model);
		return "order/direct/accept";
	}

	protected void setAttribute(Model model) {
		Dic mediaType = dicProvider.getDicMap().get(
				Constants.MediaType.DIC_CODE);

		model.addAttribute("mediaType", mediaType);

		model.addAttribute("categories",
				dicProvider.getDic(Constants.OtherMediaCategory.DIC_CODE)
						.getDicItems());
	}
}

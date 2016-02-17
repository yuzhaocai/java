package com.lczy.media.controller.admin;

import java.util.Date;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;

import com.lczy.common.web.Token;
import com.lczy.common.web.Token.Type;
import com.lczy.common.web.WebHelper;
import com.lczy.media.entity.Blacklist;
import com.lczy.media.service.BlacklistService;
import com.lczy.media.service.common.DicProvider;
import com.lczy.media.util.Constants;
import com.lczy.media.util.UserContext;

@Controller
@RequestMapping(value = "/admin/blacklist")
public class BlacklistController {
	
	private Logger log = LoggerFactory.getLogger(BlacklistController.class);
	
	@Autowired
	private BlacklistService blacklistService;
	
	@Autowired
	private DicProvider dicProvider;

	@RequestMapping(value={"", "/list"})
	public String list(Model model, ServletRequest request) throws Exception {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		
		int page = WebHelper.getPage(request);
		int size = WebHelper.getPageSize(request);
		
		String sort = WebHelper.getString(request, "sort");
		if (StringUtils.isBlank(sort)) {
			sort = "DESC_createTime";
		}
		
		Page<Blacklist> aPage= blacklistService.find(searchParams, page, size, sort);
		
		model.addAttribute("data", aPage);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		
		model.addAttribute("types", dicProvider.getDic(Constants.BlacklistType.DIC_CODE).getDicItems());
		
		return "admin/blacklist/list";
	}
	
	@Token
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String create(@ModelAttribute("blacklist")Blacklist blacklist ,Model model) throws Exception {
		model.addAttribute("types", dicProvider.getDic(Constants.BlacklistType.DIC_CODE).getDicItems());
		
		return "admin/blacklist/create";
	}

	@Token(Type.REMOVE)
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String doCreate(Blacklist blacklist, RedirectAttributes redirectAttributes) {
		blacklist.setCreateTime(new Date());
		blacklist.setCreateBy(UserContext.getCurrent().getId());
		blacklistService.save(blacklist);
		
		redirectAttributes.addFlashAttribute("message", "{\"result\":true, \"content\": \"保存成功！\"}");
		return "redirect:/admin/blacklist";
	}
	
	@RequestMapping(value = "delete/{id}", method = RequestMethod.POST)
	public String delete(@PathVariable String id, RedirectAttributes redirectAttributes) throws Exception {
		try {
			blacklistService.remove(id);
			redirectAttributes.addFlashAttribute("message", "操作成功！");
		} catch (Exception e) {
			log.error("【删除黑名单】操作失败！", e);
			redirectAttributes.addFlashAttribute("message", "操作失败！");
		}
		return "redirect:/admin/blacklist";
	}

}

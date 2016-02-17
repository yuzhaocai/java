/**
 * 
 */
package com.lczy.media.controller.member;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;

import com.lczy.common.web.Token;
import com.lczy.common.web.Token.Type;
import com.lczy.common.web.WebHelper;
import com.lczy.media.entity.Article;
import com.lczy.media.service.ArticleService;
import com.lczy.media.util.Constants;
import com.lczy.media.util.UserContext;

/**
 * @author wu
 *
 */
@Controller
@RequestMapping("/member/article")
public class ArticleController {
	
	@Autowired
	private ArticleService articleService;
	
	@RequestMapping({"", "list"})
	public String list(Model model, HttpServletRequest request) {
		
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		
		int page = WebHelper.getPage(request);
		int size = WebHelper.getPageSize(request);
		
		String sort = WebHelper.getString(request, "sort");
		if (StringUtils.isBlank(sort)) {
			sort = "DESC_createTime";
		}
		
		String startTime = (String)searchParams.get("GTE_createTime");
		String endTime = (String)searchParams.get("LTE_createTime");
		if (StringUtils.isNotBlank(startTime)) {
			DateTime start = DateTime.parse(startTime + "T00:00:00");
			searchParams.put("GTE_createTime", start.toDate());
		}
		
		if (StringUtils.isNotBlank(endTime)) {
			DateTime end = DateTime.parse(endTime + "T23:59:59");
			searchParams.put("LTE_createTime", end.toDate());
		}
		
		//只能查看自己创建的
		searchParams.put("EQ_createBy", UserContext.getCurrent().getId());
		
		Page<Article> aPage= articleService.find(searchParams, page, size, sort);
		
		model.addAttribute("data", aPage);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		model.addAttribute("sort", sort);
		
		
		return "member/article/list";
	}
	
	@RequestMapping(value="create", method=RequestMethod.GET)
	@Token
	public String create(Model model) {
		
		return "member/article/create";
	}
	
	enum Action {DRAFT,PUBLISH}
	
	@RequestMapping(value="create", method=RequestMethod.POST)
	@Token(Type.REMOVE)
	public String doCreate(Article vo, String action, HttpServletRequest request, RedirectAttributes redirectAttrs) {
		vo.setCreateBy(UserContext.getCurrent().getId());
		vo.setCreateTime(new Date());
		vo.setModifyBy(vo.getCreateBy());
		vo.setModifyTime(vo.getCreateTime());
		vo.setCustomer(UserContext.getCurrent().getCustomer());
		vo.setVisable(Constants.ArticleVisable.SHOW);
		
		Action a = Action.valueOf(action);
		switch (a) {
		case DRAFT:
			vo.setStatus(Constants.ArticleStatus.DRAFT);
			break;
		case PUBLISH:
			vo.setStatus(Constants.ArticleStatus.AUDIT);
			break;
		default:
			throw new IllegalArgumentException("无法识别的参数 action = '" + action + "'");
		}
		
		articleService.save(vo);
		
		return "redirect:/member/article/list";
	}
	
	@RequestMapping(value="{id}/view")
	public String view(@PathVariable String id, Model model) {
		Article article = articleService.get(id);
		model.addAttribute("article", article);
		
		return "member/article/view";
	}
	
	@RequestMapping(value="{id}/share")
	public String share(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) {
		Article article = articleService.get(id);
		
		//TODO
		
		return "member/article/share";
	}

}

package com.lczy.media.controller.admin;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lczy.common.web.WebHelper;
import com.lczy.media.controller.common.BaseController;
import com.lczy.media.entity.Dic;
import com.lczy.media.entity.User;
import com.lczy.media.service.CustomerService;
import com.lczy.media.service.UserService;
import com.lczy.media.service.common.DicProvider;
import com.lczy.media.util.Constants;
import com.lczy.media.util.UserContext;
import com.mchange.v1.lang.BooleanUtils;

@Controller
@RequestMapping("/admin/member")
public class UserManageController extends BaseController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private CustomerService customerService;

	@Autowired
	private DicProvider dicProvider;

	@RequestMapping({ "", "/" })
	public String list(Model model, HttpServletRequest request)
			throws Exception {
		int page = WebHelper.getPage(request);
		int size = WebHelper.getPageSize(request);

		String sort = getSort(request);

		Map<String, Object> searchParams = getSearchParams(request);
		
		//处理最近登陆时间
		String latestStart = (String)searchParams.get("GTE_latestTime");
		String latestEnd = (String)searchParams.get("LTE_latestTime");
		if (StringUtils.isNotBlank(latestStart)) {
			DateTime start = DateTime.parse(latestStart + "T00:00:00");
			searchParams.put("GTE_latestTime", start.toDate());
		}
		if (StringUtils.isNotBlank(latestEnd)) {
			DateTime end = DateTime.parse(latestEnd + "T23:59:59");
			searchParams.put("LTE_latestTime", end.toDate());
		}

		searchParams.put("IN_status", Arrays.asList(
				Constants.UserStatus.DISABLED, Constants.UserStatus.ENABLED));
		searchParams.put("EQ_type", Constants.UserType.MEMBER);
		searchParams.put("IN_customer.custType", Arrays.asList(
				Constants.CustType.CUST_PRO, Constants.CustType.CUST_ADV));
		//实名认证
		String certified = (String)searchParams.get("EQ_customer.certified");
		if (StringUtils.isNotBlank(certified)) {
			searchParams.put("EQ_customer.certified", BooleanUtils.parseBoolean(certified));
		}
		Page<User> aPage = userService.find(searchParams, page, size, sort);
		Dic dic = dicProvider.getDicMap().get(Constants.CustType.DIC_CODE);
		model.addAttribute("types", dic);
		setModalAttrsForPaging(model, searchParams, aPage, sort);

		return "admin/member/list";
	}

	@RequestMapping("freeze")
	public String freeze(String userId, RedirectAttributes redirectAttributes)
			throws Exception {
		userService.freeze(userId, Constants.UserStatus.DISABLED);

		redirectAttributes.addFlashAttribute("message", "冻结成功！");
		return "redirect:/admin/member";
	}

	@RequestMapping("unfreeze")
	public String unfreeze(String userId, RedirectAttributes redirectAttributes)
			throws Exception {
		userService.unfreeze(userId, Constants.UserStatus.ENABLED);

		redirectAttributes.addFlashAttribute("message", "解冻成功！");
		return "redirect:/admin/member";
	}
	
	@RequestMapping("banned")
	public String banned(String userId, RedirectAttributes redirectAttributes)
			throws Exception {
		userService.banned(userId, true);
		
		redirectAttributes.addFlashAttribute("message", "禁言成功！");
		return "redirect:/admin/member";
	}
	
	@RequestMapping("unbanned")
	public String unbanned(String userId, RedirectAttributes redirectAttributes)
			throws Exception {
		userService.unbanned(userId, false);
		
		redirectAttributes.addFlashAttribute("message", "解禁成功！");
		return "redirect:/admin/member";
	}

	@RequestMapping("resetPwd")
	public String resetPwd(String userId, RedirectAttributes redirectAttributes)
			throws Exception {
		userService.resetPassword(userId);

		redirectAttributes.addFlashAttribute("message",
				" \"用户密码\" 已经重置为与 \"登录账号\" 相同！");
		return "redirect:/admin/member";
	}

	/**
	 * @param id 
	 * @param mobPhone 
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("updatePhone")
	public String updatePhone(String id,String mobPhone, RedirectAttributes redirectAttributes)
			throws Exception {
		User user = userService.get(id);
		//修改用户登陆账号
		user.setLoginName(mobPhone);
		//修改会员手机号码
		user.getCustomer().setMobPhone(mobPhone);
		user.getCustomer().setModifyBy(UserContext.getCurrent().getId());
		user.getCustomer().setModifyTime(new Date());
		userService.save(user);
		redirectAttributes.addFlashAttribute("message","手机号码修改成功");
		return "redirect:/admin/member";
	}
	
	/**
	 * 查看用户信息
	 * 
	 * @param id
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("preview")
	public String preview(String id, Model model)	throws Exception {
		User user = userService.get(id);
		model.addAttribute("provider", user.getRole().getId().equals(Constants.RoleId.PROVIDER));
		model.addAttribute("customer", user.getCustomer());
		return "/admin/member/preview";
	}
}

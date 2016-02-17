package com.lczy.media.controller.admin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;

import com.google.common.collect.Maps;
import com.lczy.common.web.Token;
import com.lczy.common.web.Token.Type;
import com.lczy.common.web.WebHelper;
import com.lczy.media.entity.Role;
import com.lczy.media.entity.User;
import com.lczy.media.service.CustomerService;
import com.lczy.media.service.RoleService;
import com.lczy.media.service.UserService;
import com.lczy.media.service.common.DicProvider;
import com.lczy.media.util.Constants;
import com.lczy.media.util.Constants.UserStatus;

@Controller
@RequestMapping(value = "/admin/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private DicProvider dicProvider;
	
	@Autowired
	private CustomerService customerService;

	@RequestMapping(value={"", "/"})
	public String list(Model model, ServletRequest request) throws Exception {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_role.roleType", Constants.RoleType.SYSTEM);
		searchParams.put("NEQ_loginName", "root");
		
		int page = WebHelper.getPage(request);
		int size = WebHelper.getPageSize(request);
		
		String sort = WebHelper.getString(request, "sort");
		if (StringUtils.isBlank(sort)) {
			sort = "DESC_createTime";
		}
		
		//searchParams.put("IN_loginName", Arrays.asList("org01","org02"));
		
		Page<User> aPage= userService.find(searchParams, page, size, sort);
		
		model.addAttribute("data", aPage);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		
		model.addAttribute("roles", getSystemRoles());
		
		return "admin/userList";
	}
	

	private Collection<Role> getSystemRoles() {
		Map<String, Object> searchParams = Maps.newLinkedHashMap();
		searchParams.put("EQ_roleType", Constants.RoleType.SYSTEM);
		return roleService.find(searchParams);
	}



	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) throws Exception {
		model.addAttribute("user", userService.get(id));
		model.addAttribute("action", "update");
		
		setForm(model);
		
		return "admin/userForm";
	}
	
	@ModelAttribute("preloadUser")
	public User getUser(@RequestParam(required = false) String id, HttpServletRequest request) throws Exception {
		User user = new User();
		if (StringUtils.isNotBlank(id)) {
			user = userService.get(id);
			String roleId = WebHelper.getString(request, "role.id");
			if( StringUtils.isNotBlank(roleId) && !user.getRole().getId().equals(roleId)) {
				user.setRole(roleService.getRole(roleId));
			}
		}
		
		return user;
	}
	

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@ModelAttribute("preloadUser") User user,
			RedirectAttributes redirectAttributes) {
		
		userService.save(user);
		redirectAttributes.addFlashAttribute("message", "保存用户信息成功");
		
		return "redirect:/admin/user";
	}

	
	@RequestMapping(value = "checkNickname")
	@ResponseBody
	public boolean checkNickname(String oldName, String nickname) {
		if (oldName.equals(nickname)) {
			return true;
		} else {
			return userService.getByNickname(nickname) == null;
		}
	}
	
	@Token
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) throws Exception {
		User user = new User();
		user.setStatus(UserStatus.ENABLED);
		model.addAttribute("user", user);
		model.addAttribute("action", "create");
		
		setForm(model);
		
		return "admin/userForm";
	}

	private void setForm(Model model) throws Exception {
		List<Role> roles = new ArrayList<>();
		roles.addAll(getSystemRoles());
//		Role admin = roleService.getByRoleCode("admin");
//		if (admin != null) roles.add(admin);
		Role operator = roleService.getByRoleCode("operator");
		if (operator != null) roles.add(operator);
		model.addAttribute("roles", roles);
		model.addAttribute("status", dicProvider.getDicMap().get(UserStatus.DIC_CODE).getDicItems());
	}

	@Token(Type.REMOVE)
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@ModelAttribute("preloadUser") User newUser, RedirectAttributes redirectAttributes) {
		newUser.setCustomer(customerService.get("0"));
		//newUser.setRole(roleService.getRole(newUser.getRoleId()));
		newUser.setType(Constants.UserType.SYSTEM);
		//用户名与登陆账号同步
		newUser.setNickname(newUser.getLoginName());
		//添加创建时间
		newUser.setCreateTime(new Date());
		userService.save(newUser);
		redirectAttributes.addFlashAttribute("message", "{\"result\":true, \"content\": \"创建用户成功\"}");
		return "redirect:/admin/user";
	}
	
	@RequestMapping(value = "delete/{id}", method = RequestMethod.POST)
	public String delete(@PathVariable String id, RedirectAttributes redirectAttributes) throws Exception {
		
		userService.delete(id);
		
		redirectAttributes.addFlashAttribute("message", "删除用户成功");
		return "redirect:/admin/user";
	}
	
	@RequestMapping(value = "resetPwd/{id}")
	public String resetPwd(@PathVariable String id, RedirectAttributes redirectAttributes) throws Exception {
		
		userService.resetPassword(id);
		
		redirectAttributes.addFlashAttribute("message", "用户密码已经重置为与登录账号相同！");
		return "redirect:/admin/user";
	}
	
	//操作——离职
	@RequestMapping(value = "dimission/{id}", method = RequestMethod.POST)
	public String dimission(@PathVariable String id, RedirectAttributes redirectAttributes) throws Exception {
		userService.dimission(id);
		
		redirectAttributes.addFlashAttribute("message", "用户状态已更改为离职");
		return "redirect:/admin/user";
	}

}

package com.lczy.media.controller.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lczy.common.web.WebHelper;
import com.lczy.media.controller.common.BaseController;
import com.lczy.media.entity.Customer;
import com.lczy.media.service.CustomerService;
import com.lczy.media.util.Constants;
import com.lczy.media.util.FileServerUtils;

@Controller
@RequestMapping("/admin/member/cert")
public class MemberManageController extends BaseController {

	@Autowired
	private CustomerService customerService;

	@RequestMapping({ "", "/" })
	public String list(Model model, HttpServletRequest request)
			throws Exception {
		int page = WebHelper.getPage(request);
		int size = WebHelper.getPageSize(request);

		String sort = getSort(request);

		Map<String, Object> searchParams = getSearchParams(request);

		searchParams.put("IN_custType", Arrays.asList(
				Constants.CustType.CUST_PRO, Constants.CustType.CUST_ADV));
		searchParams.put("EQ_certStatus", Constants.CertStatus.AUDIT);
		Page<Customer> aPage = customerService.find(searchParams, page, size,
				sort);

		setModalAttrsForPaging(model, searchParams, aPage, sort);

		return "admin/member/cert/list";
	}
	
	@RequestMapping("pass")
	public String pass(String customerId) throws Exception {
		customerService.customerPass(customerId);
		return "redirect:/admin/member/cert";
	}

	@RequestMapping("unpass")
	public String unpass(String customerId) throws Exception {
		customerService.customerUnPass(customerId);
		return "redirect:/admin/member/cert";
	}
	
	@RequestMapping("certificate")
	@ResponseBody
	public Map<String, Object> certificate(String customerId) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Customer customer = customerService.get(customerId);
		map.put("custType", customer.getCustType());
		map.put("certName", customer.getCertName());
		map.put("certIndentity", customer.getCertIdentity());

		map.put("certMatter1",
				FileServerUtils.getFileUrl(getPics(customer).get(0))
						+ "?token=" + FileServerUtils.token());
		if (getPics(customer) != null && getPics(customer).size() > 1)
			map.put("certMatter2",
					FileServerUtils.getFileUrl(getPics(customer).get(1))
							+ "?token=" + FileServerUtils.token());
		if (getPics(customer) != null && getPics(customer).size() > 2)
			map.put("certMatter3",
					FileServerUtils.getFileUrl(getPics(customer).get(2))
							+ "?token=" + FileServerUtils.token());
		return map;
	}
	
	/**
	 * 获取图片id
	 * @param mCase
	 * @return
	 */
	private List<String> getPics(Customer customer) {

		List<String> mCasePic = new ArrayList<String>();
		if (StringUtils.isNotBlank(customer.getCertMatter())) {
			String[] pics = customer.getCertMatter().split(";");
			for (String pic : pics) {
				mCasePic.add(pic);
			}
		}
		return mCasePic;
	}
}

package com.lczy.media.controller.member;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lczy.common.service.AbstractService;
import com.lczy.common.web.Token;
import com.lczy.common.web.Token.Type;
import com.lczy.common.web.WebHelper;
import com.lczy.media.controller.common.BaseController;
import com.lczy.media.entity.Account;
import com.lczy.media.entity.ChargeLog;
import com.lczy.media.entity.Customer;
import com.lczy.media.entity.OfferLine;
import com.lczy.media.entity.Transaction;
import com.lczy.media.entity.User;
import com.lczy.media.entity.WithdrawLog;
import com.lczy.media.repositories.AccountDao;
import com.lczy.media.service.AccountService;
import com.lczy.media.service.ChargeLogService;
import com.lczy.media.service.CustomerService;
import com.lczy.media.service.OfferLineService;
import com.lczy.media.service.TransactionService;
import com.lczy.media.service.WithdrawLogService;
import com.lczy.media.util.Constants;
import com.lczy.media.util.SmsCodeValidator;
import com.lczy.media.util.UserContext;


/**
 * 资金管理controller.
 *
 */
@Controller
@RequestMapping("/member/account")
public class AccountController extends BaseController {
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private ChargeLogService chargeLogService;
	
	@Autowired
	private WithdrawLogService withdrawLogService;
	
	@Autowired
	private SmsCodeValidator smsCodeValidator;
	
	@Autowired
	private OfferLineService offerLineService;
	

	/**
	 * 资金管理主页.
	 */
	@RequestMapping({"", "/"})
	public String main(Model model, HttpServletRequest request) {
		
		return transaction(model, request);
	}
	
	/**
	 * 交易明细.
	 */
	@RequestMapping("transaction")
	public String transaction(Model model, HttpServletRequest request) {
		
		doList(model, request, transactionService);
		
		return "member/account/transaction";
	}
	
	/**
	 * 充值记录.
	 */
	@RequestMapping("chargeList")
	public String chargeList(Model model, HttpServletRequest request) {
		
		doList(model, request, chargeLogService);
		
		return "member/account/chargeList";
	}
	
	/**
	 * 提现记录.
	 */
	@RequestMapping("withdrawList")
	public String withdrawList(Model model, HttpServletRequest request) {
		
		doList(model, request, withdrawLogService);
		
		return "member/account/withdrawList";
	}
	
	private <T> void doList(Model model, HttpServletRequest request, AbstractService<T> service) {
		Account account = getAccount();
		model.addAttribute("account", account);
		
		int page = WebHelper.getPage(request);
		int size = WebHelper.getPageSize(request);
		
		String sort = getSort(request);
		
		Map<String, Object> searchParams = getSearchParams(request);
		//只能查看自己的
		searchParams.put("EQ_customer.id", UserContext.getCurrent().getCustomer().getId());
		
		// 对于充值记录,只查充值成功的
		if (service instanceof ChargeLogService) {
			searchParams.put("EQ_status", Constants.ChargeLogStatus.FINISHED);
		}
		
		Page<T> aPage = service.find(searchParams, page, size, sort);
		
		setModalAttrsForPaging(model, searchParams, aPage, sort);
	}


	private Account getAccount() {
		User user = UserContext.getCurrent();
		Customer customer = customerService.get(user.getCustomer().getId());
		return accountService.get(customer);
	}
	
	/**
	 * 充值表单.
	 */
	@RequestMapping(value="charge", method = RequestMethod.GET)
	@Token
	public String charge(Model model) {
		Account account = getAccount();
		model.addAttribute("account", account);
		
		return "member/account/charge";
	}
	
	/**
	 * 充值.
	 */
	@RequestMapping(value="charge", method = RequestMethod.POST)
	@Token(Type.REMOVE)
	public String doCharge(int amount, String platform) {
		Account account = getAccount();
		ChargeLog chargeLog = new ChargeLog();
		chargeLog.setAmount(amount);
		chargeLog.setPlatform(Constants.PayPlatform.ALIPAY);
		chargeLog.setCustomer(account.getCustomer());
		chargeLog.setStatus(Constants.ChargeLogStatus.CREATED);
		chargeLog.setPlatform(platform);
		chargeLog.setCreateTime(new Date());
		chargeLog.setModifyTime(new Date());
		chargeLogService.save(chargeLog);

		if (platform.equals(Constants.PayPlatform.ALIPAY)) {
			return "redirect:/member/account/charge/alipay/" + chargeLog.getId();
		} else {
			return "redirect:/member/account/charge/wxpay/" + chargeLog.getId();
		}
	}
	
	/**
	 * 提现表单.
	 */
	@RequestMapping(value="withdraw", method = RequestMethod.GET)
	@Token
	public String withdraw(Model model) {
		Account account = getAccount();
		model.addAttribute("account", account);
		
		return "member/account/withdraw";
	}
	
	/**
	 * 提现.
	 */
	@RequestMapping(value="withdraw", method = RequestMethod.POST)
	@Token(Type.REMOVE)
	public String doWithdraw(Integer amount, String platform, String alipayAccount, String smscode, RedirectAttributes redirectAttrs) {
		Account account = getAccount();
		try {
			if (!smsCodeValidator.check(account.getCustomer().getMobPhone(), smscode)) {
				throw new Exception("验证码错误!");
			}
			WithdrawLog withdrawLog = new WithdrawLog();
			withdrawLog.setAmount(amount);
			withdrawLog.setPlatform(platform);
			withdrawLog.setCustomer(account.getCustomer());
			withdrawLog.setPlatformAccount(alipayAccount);
			withdrawLog.setStatus(Constants.WithdrawStatus.CREATED);
			withdrawLog.setCreateTime(new Date());
			
			accountService.withdraw(account, withdrawLog);
			redirectAttrs.addFlashAttribute("message", "提现申请成功！");
		} catch (Exception e) {
			redirectAttrs.addFlashAttribute("message", "提现申请失败：" + e.getMessage());
		}
		
		return "redirect:/member/account/withdrawList";
	}
	
	/**
	 * 提现表单.
	 */
	@RequestMapping(value="offerLine", method = RequestMethod.GET)
	@Token
	public String offerLine(Model model) {
		Account account = getAccount();
		model.addAttribute("account", account);
		return "member/account/offerLineModal";
	}
	
	/**
	 * 提现表单.
	 */
	@RequestMapping(value="offerLine", method = RequestMethod.POST)
	@Token(Type.REMOVE)
	public String offerLine(Model model,int money,RedirectAttributes redirectAttrs) {
		try{
			Account account = getAccount();
			offerLineService.saveOfferLine(money, account);
			redirectAttrs.addFlashAttribute("message", "操作成功！");
		}catch(Exception e){
			redirectAttrs.addFlashAttribute("message", e.getMessage());
		}
		
		return "redirect:/member/account/transaction";
	}
}

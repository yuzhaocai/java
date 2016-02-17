package com.lczy.media.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lczy.common.service.AbstractService;
import com.lczy.media.entity.Account;
import com.lczy.media.entity.ChargeLog;
import com.lczy.media.repositories.ChargeLogDao;
import com.lczy.media.util.Constants.ChargeLogStatus;

@Service
@Transactional(readOnly = true)
public class ChargeLogService extends AbstractService<ChargeLog> {
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private ChargeLogDao chargeLogDao;
	
	@Autowired
	private CustCreditService custCreditService;
	
	/**
	 * 充值
	 * 
	 * @param id				充值日志
	 * @param platform			支付平台
	 * @param transactionId		交易号
	 * @throws Exception
	 */
	@Transactional(readOnly = false)
	public void charge(String id, String platform, String transactionId) throws Exception {
		ChargeLog chargeLog = this.get(id);
		if (chargeLog.getStatus().equals(ChargeLogStatus.CREATED)) {
			chargeLog.setStatus(ChargeLogStatus.FINISHED);
			chargeLog.setTransactionId(transactionId);
			chargeLog.setModifyTime(new Date());
			this.save(chargeLog);
			
//			ChargeLog history = chargeLogDao.findTopByCustomerAndStatusOrderByModifyTimeAsc(chargeLog.getCustomer(), ChargeLogStatus.FINISHED);
			Account account = chargeLog.getCustomer().getAccount();
//			if (history.getId().equals(chargeLog.getId())) {
//				// 第一次充值
//				System.out.println("first time");
//				accountService.charge(account, chargeLog.getAmount() + 1);
//				custCreditService.changeCredit(chargeLog.getCustomer(), 100, "第一次充值");
//			} else {
//				System.out.println("not first time");
				accountService.charge(account, chargeLog.getAmount());
//			}
		} else if (!(chargeLog.getStatus().equals(ChargeLogStatus.FINISHED)
					&& chargeLog.getPlatform().equals(platform)
					&& chargeLog.getTransactionId().equals(transactionId))) {
			throw new Exception("非法充值日志记录!");
		}
	}
}

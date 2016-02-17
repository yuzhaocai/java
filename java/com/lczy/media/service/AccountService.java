package com.lczy.media.service;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lczy.media.entity.Account;
import com.lczy.media.entity.AccountLog;
import com.lczy.media.entity.Customer;
import com.lczy.media.entity.WithdrawLog;
import com.lczy.media.exception.BalanceNotEnoughException;
import com.lczy.media.repositories.AccountDao;
import com.lczy.media.repositories.AccountLogDao;
import com.lczy.media.repositories.ChargeLogDao;
import com.lczy.media.repositories.TransactionDao;
import com.lczy.media.repositories.WithdrawLogDao;


@Service
@Transactional(readOnly=true)
public class AccountService {

	@Autowired
	private AccountDao accountDao;

	@Autowired
	private AccountLogDao accountLogDao;

	@Autowired
	private TransactionDao transactionDao;
	
	@Autowired
	private ChargeLogDao chargeLogDao;
	
	@Autowired
	private WithdrawLogDao withdrawLogDao;
	
	
	/**
	 * 账户充值.
	 * @param account 充值目标账户
	 * @param amount 充值金额
	 */
	@Transactional(readOnly=false)
	public void charge(Account account, int amount) {
		
		accountDao.refresh(account);
		account.setAvBalance(account.getAvBalance() + amount);
		account.setModifyTime(new Date());
		accountDao.save(account);
		
		//保存充值记录
		AccountLog log = new AccountLog();
		log.setCustomer(account.getCustomer());
		log.setValue(amount);
		log.setMemo("充值");
		log.setCreateTime(new Date());
		accountLogDao.save(log);
	}

	public Account get(Customer customer) {
		
		return accountDao.findByCustomerId(customer.getId());
	}
	
	public Account findByCustomerId(String customerId) {
		return accountDao.findByCustomerId(customerId);
	}
	

	/**
	 * 提现.
	 * @param account 提现账户
	 * @param withdrawLog 提现日志
	 * @throws Exception 
	 */
	@Transactional(readOnly=false)
	public void withdraw(Account account, WithdrawLog withdrawLog) throws Exception {
		accountDao.refresh(account);
		
		if (withdrawLog.getAmount() <= 0) {
			throw new Exception("提现金额需大于0!");
		}
		if (account.getAvBalance() - withdrawLog.getAmount() < 0) {
			throw new BalanceNotEnoughException("余额不足!");
		}
		
		account.setAvBalance(account.getAvBalance() - withdrawLog.getAmount());
		accountDao.save(account);
		
		//保存提现记录
		saveWithdrawLog(withdrawLog);
	}

	private void saveWithdrawLog(WithdrawLog withdrawLog) {
		withdrawLogDao.save(withdrawLog);
	}
}

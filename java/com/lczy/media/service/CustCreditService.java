/**
 * 
 */
package com.lczy.media.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lczy.media.entity.Account;
import com.lczy.media.entity.AccountLog;
import com.lczy.media.entity.CustCreditLog;
import com.lczy.media.entity.CustLevel;
import com.lczy.media.entity.CustLevelLog;
import com.lczy.media.entity.Customer;
import com.lczy.media.repositories.AccountDao;
import com.lczy.media.repositories.AccountLogDao;
import com.lczy.media.repositories.CustCreditLogDao;
import com.lczy.media.repositories.CustLevelDao;
import com.lczy.media.repositories.CustLevelLogDao;
import com.lczy.media.repositories.CustomerDao;

/**
 * 会员信用变更服务类.
 * 
 * @author wu
 *
 */
@Service
@Transactional(readOnly = true)
public class CustCreditService {
	
	@Autowired
	private CustCreditLogDao creditLogDao;
	
	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private CustLevelDao custLevelDao;
	
	@Autowired
	private AccountDao accountDao;
	
	@Autowired
	private AccountLogDao accountLogDao;
	
	@Autowired
	private CustLevelLogDao custLevelLogDao;
	
	/**
	 * 改变会员的信用值.
	 * 
	 * @param cust 目标会员.
	 * @param credit 奖励(+)或者扣除(-)的信用值.
	 * @param vc 奖励(+)或者扣除(-)的虚拟币.
	 * @param memo 备注说明.
	 */
	@Transactional(readOnly = false)
	public void change(Customer cust, int credit, int vc, String memo) {
		// 变更会员账户余额
		if( vc != 0 ) {
			updateAccount(cust, vc, memo);
		}
		
		changeCredit(cust, credit, memo);
	}

	/**
	 * 改变会员的信用值.
	 * 
	 * @param cust 目标会员.
	 * @param credit 奖励(+)或者扣除(-)的信用值.
	 * @param memo 备注说明.
	 */
	@Transactional(readOnly = false)
	public void changeCredit(Customer cust, int credit, String memo) {
		// 变更会员信用值
		if( credit != 0 ) {
			saveCreditLog(cust, credit, memo);
			cust.setCredit(cust.getCredit() + credit);
			checkLevel(cust, memo);
			customerDao.save(cust);
		}
	}

	private static final String PATTERN_REWARD = "升级会员级至 %s, 系统奖励 %+d 虚拟币。";
	
	/**
	 * 检查会员信用值是否达到升级阀值.
	 * 
	 * @param cust 会员.
	 * @return 如果达到升级阀值，则返回 true，否则返回 false.
	 */
	private boolean checkLevel(Customer cust, String memo) {
		boolean up = false;
		CustLevel level = cust.getCustLevel();
		CustLevel nextLevel = level.getNextLevel();
		
		if( cust.getCredit() >= nextLevel.getCredit() ) {
			cust.setCustLevel(nextLevel);
			saveLevelLog(cust, level.getId(), nextLevel.getId(), memo);
			
			if( nextLevel.getReward() > 0 ) {
				//赠送虚拟币
				updateAccount(cust, nextLevel.getReward(), String.format(PATTERN_REWARD, nextLevel.getName(), nextLevel.getReward()));
			}
			
			up = true;
		}
		
		return up;
	}

	/**
	 * 更新会员账户可用余额.
	 * 
	 * @param cust 会员.
	 * @param value 增加或者减少余额数量.
	 * @param memo 变更说明.
	 */
	private void updateAccount(Customer cust, int value, String memo) {
		Account account = cust.getAccount();
		account.setAvBalance(account.getAvBalance() + value);
		accountDao.save(account);
		
		saveAccountLog(cust, value, memo);
	}

	/**
	 * 保存会员账户余额变更日志.
	 * @param cust 会员.
	 * @param value 余额变更数量.
	 * @param memo 变更说明.
	 */
	private void saveAccountLog(Customer cust, int value, String memo) {
		AccountLog log = new AccountLog();
		log.setCustomer(cust);
		log.setValue(value);
		log.setMemo(memo);
		log.setCreateTime(new Date());
		
		accountLogDao.save(log);
	}

	/**
	 * 保存会员升级日志.
	 * @param id 会员 id.
	 * @param old 原级别.
	 * @param next 新级别.
	 * @param date 升级时间.
	 * @param memo 备注.
	 */
	private void saveLevelLog(Customer cust, String old, String next, String memo) {
		CustLevelLog log = new CustLevelLog();
		log.setCustomer(cust);
		log.setOldLevel(old);
		log.setNewLevel(next);
		log.setMemo(memo);
		log.setCreateTime(new Date());
		
		custLevelLogDao.save(log);
	}
	
	/**
	 * 保存信用变更记录.
	 */
	private void saveCreditLog(Customer cust, int credit, String memo) {
		CustCreditLog log = new CustCreditLog();
		log.setCustomer(cust);
		log.setValue("" + credit);
		log.setMemo(memo);
		log.setCreateTime(new Date());
		log.setCreateBy("SYSTEM");
		
		creditLogDao.save(log);
	}
}

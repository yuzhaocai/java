package com.lczy.media.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lczy.common.exception.ServiceException;
import com.lczy.common.service.AbstractService;
import com.lczy.media.entity.Account;
import com.lczy.media.entity.Customer;
import com.lczy.media.entity.OfferLine;
import com.lczy.media.entity.Transaction;
import com.lczy.media.repositories.AccountDao;
import com.lczy.media.repositories.OfferLineDao;
import com.lczy.media.repositories.TransactionDao;
import com.lczy.media.util.Constants;

@Service
@Transactional(readOnly = true)
public class OfferLineService extends AbstractService<OfferLine> {

	@Autowired
	private OfferLineDao offerLineDao;
	
	@Autowired
	private AccountDao accountDao;
	
	@Autowired
	private TransactionDao transactionDao;
	

	@Override
	public OfferLineDao getDao() {
		return (OfferLineDao) super.getDao();
	}
	
	@Transactional(readOnly = false)
	public void saveOfferLine(int money,Account account){
		Date now = new Date();
		//插入线下服务
		OfferLine offer = new OfferLine();
		offer.setCreateTime(now);
		offer.setCustomer(account.getCustomer());
		offer.setMoney(money);
		OfferLine tranOffer = offerLineDao.save(offer);
		if(null!=tranOffer){
			//扣除金额
			account.setAvBalance(account.getAvBalance() - money);
			accountDao.save(account);
			//插入流水表
			Transaction transaction = new Transaction();
			transaction.setOfferLine(tranOffer);
			transaction.setCustomer(account.getCustomer());
			transaction.setAmount(money);
			transaction.setCreateTime(now);
			transaction.setType(Constants.TransactionType.OFFLINE);
			transaction.setRemark("订单编号:"+tranOffer.getId());
			transactionDao.save(transaction);
		}else{
			throw new ServiceException("操作失败！");
		}
	}

}

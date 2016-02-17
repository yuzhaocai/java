package com.lczy.media.service;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lczy.media.entity.ChargeLog;
import com.lczy.media.entity.RefundLog;
import com.lczy.media.entity.Transaction;
import com.lczy.media.entity.WithdrawLog;
import com.lczy.media.util.Constants;
import com.lczy.media.vo.MoneyFlowVO;


@Service
@Transactional(readOnly=true)
public class MoneyFlowService {

	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private ChargeLogService chargeLogService;
	
	@Autowired
	private WithdrawLogService withdrawLogService;
	
	@Autowired
	private RefundLogService refundLogService;
	
	
	public List<MoneyFlowVO> getMoneyFlow(Map<String, Object> searchParams) {
		List<RefundLog> refundLogs = refundLogService.find(searchParams);
		List<MoneyFlowVO> result = new ArrayList<MoneyFlowVO>();
		for (RefundLog refundLog : refundLogs) {
			result.add(toMoneyFlow(refundLog));
		}
		
		List<WithdrawLog> withdrawLogs = withdrawLogService.find(searchParams);
		for (WithdrawLog withdrawLog : withdrawLogs) {
			result.add(toMoneyFlow(withdrawLog));
		}
		
		List<Transaction> transactions = transactionService.find(searchParams);
		for (Transaction transaction : transactions) {
			result.add(toMoneyFlow(transaction));
		}

		searchParams.put("EQ_status", Constants.ChargeLogStatus.FINISHED);
		List<ChargeLog> chargeLogs = chargeLogService.find(searchParams);
		for (ChargeLog chargeLog : chargeLogs) {
			result.add(toMoneyFlow(chargeLog));
		}
		searchParams.remove("EQ_status");
		
		Collections.sort(result, new Comparator<MoneyFlowVO>() {
			
			@Override
			public int compare(MoneyFlowVO o1, MoneyFlowVO o2) {
				return o2.getCreateTime().compareTo(o1.getCreateTime());
			}
			
		});
		return result;
	}
	
	MoneyFlowVO toMoneyFlow(RefundLog log) {
		MoneyFlowVO moneyFlowVO = new MoneyFlowVO();
		moneyFlowVO.setId(log.getId());
		moneyFlowVO.setCreateTime(log.getCreateTime());
		moneyFlowVO.setAmount(log.getOrder().getAmount());
		moneyFlowVO.setAction("退款");
		return moneyFlowVO;
	}
	
	MoneyFlowVO toMoneyFlow(WithdrawLog log) {
		MoneyFlowVO moneyFlowVO = new MoneyFlowVO();
		moneyFlowVO.setId(log.getId());
		moneyFlowVO.setCreateTime(log.getCreateTime());
		moneyFlowVO.setAmount(log.getAmount());
		moneyFlowVO.setAction("提现");
		return moneyFlowVO;
	}
	
	MoneyFlowVO toMoneyFlow(ChargeLog log) {
		MoneyFlowVO moneyFlowVO = new MoneyFlowVO();
		moneyFlowVO.setId(log.getId());
		moneyFlowVO.setCreateTime(log.getCreateTime());
		moneyFlowVO.setAmount(log.getAmount());
		moneyFlowVO.setAction("充值");
		return moneyFlowVO;
	}
	
	MoneyFlowVO toMoneyFlow(Transaction transaction) {
		MoneyFlowVO moneyFlowVO = new MoneyFlowVO();
		moneyFlowVO.setId(transaction.getId());
		moneyFlowVO.setCreateTime(transaction.getCreateTime());
		moneyFlowVO.setAmount(transaction.getAmount());
		moneyFlowVO.setAction(transaction.getType().equals(Constants.TransactionType.CONSUME) ? "消费" : "收入");
		return moneyFlowVO;
	}
	
}

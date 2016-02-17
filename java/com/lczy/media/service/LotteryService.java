package com.lczy.media.service;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lczy.common.service.AbstractService;
import com.lczy.media.entity.Customer;
import com.lczy.media.entity.LotteryPrize;
import com.lczy.media.entity.LotteryTime;
import com.lczy.media.repositories.LotteryPrizeDao;
import com.lczy.media.repositories.LotteryTimeDao;
import com.lczy.media.util.Constants;
import com.lczy.media.util.UserContext;

@Service
@Transactional(readOnly=true)
public class LotteryService extends AbstractService<LotteryPrize> {
	
	@Autowired
	private LotteryTimeDao lotteryTimeDao;
	
	@Autowired
	private LotteryPrizeDao lotteryPrizeDao;
	
	/**
	 * 初始化抽奖次数
	 * 
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly=false)
	public LotteryTime getLotteryTime() {
		Customer customer = UserContext.getCurrentCustomer();
		LotteryTime lotteryTime = lotteryTimeDao.findByCustomer(customer);
		if (lotteryTime == null) {
			lotteryTime = new LotteryTime();
			lotteryTime.setCustomer(customer);
			lotteryTime.setTimes(3);
			lotteryTimeDao.save(lotteryTime);
		}
		
		return lotteryTime;
	}
		
	/**
	 * 抽奖
	 * 
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly=false)
	public LotteryPrize startLottery() throws Exception {
		Customer customer = UserContext.getCurrentCustomer();
		if (lotteryTimeDao.decTimes(customer) == 0) {
			throw new Exception("对不起，您的抽奖次数已经用完！");
		}
		
		// 最后一次抽奖
		LotteryTime lotteryTime = lotteryTimeDao.findByCustomer(customer);
		if (lotteryTime.getTimes() == 0) {
			if (new Random().nextInt(5) == 0) { // 一元广告中奖概率20%
				LotteryPrize prize = lotteryPrizeDao.findTopByCategoryAndCustomer(Constants.LotteryPrizeCategory.ADVERTISE, null);
				if (prize != null) {	// 还有剩余奖品
					prize(customer, prize);
					return prize;
				} else {	// 一元广告奖品已经抽完，发放参与奖
					prize = lotteryPrizeDao.findTopByCategoryAndCustomer(Constants.LotteryPrizeCategory.PARTICIPATE, null);
					if (prize != null) {
						prize(customer, prize);
						return prize;
					}					
				}
			} else {
				LotteryPrize prize = lotteryPrizeDao.findTopByCategoryAndCustomer(Constants.LotteryPrizeCategory.PARTICIPATE, null);
				if (prize != null) {	// 还有剩余奖品
					prize(customer, prize);
					return prize;
				} else {	// 参与奖奖品已经抽完，发放一元广告
					prize = lotteryPrizeDao.findTopByCategoryAndCustomer(Constants.LotteryPrizeCategory.ADVERTISE, null);
					if (prize != null) {
						prize(customer, prize);
						return prize;
					}					
				}
				
			}
		}
		
		return null;
	}

	/**
	 * 分配奖品给用户
	 * 
	 * @param customer
	 * @param prize
	 */
	private void prize(Customer customer, LotteryPrize prize) {
		prize.setCustomer(customer);
		prize.setCreateTime(new Date());
		lotteryPrizeDao.save(prize);
	}
	
	/**
	 * 中奖用户列表
	 * 
	 * @return
	 */
	public List<LotteryPrize> findLotteryPrizes() {
		return lotteryPrizeDao.findByCustomerNotNullOrderByCreateTimeDesc();
	}
	
}

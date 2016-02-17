package com.lczy.media.repositories;

import java.io.Serializable;
import java.util.List;

import com.lczy.common.data.Dao;
import com.lczy.media.entity.Customer;
import com.lczy.media.entity.LotteryPrize;

public interface LotteryPrizeDao extends Dao<LotteryPrize, Serializable> {

	LotteryPrize findTopByCategoryAndCustomer(String category, Customer customer);
	
	List<LotteryPrize> findByCustomerNotNullOrderByCreateTimeDesc();
	
}

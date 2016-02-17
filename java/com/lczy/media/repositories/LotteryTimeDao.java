package com.lczy.media.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.lczy.common.data.Dao;
import com.lczy.media.entity.Customer;
import com.lczy.media.entity.LotteryTime;

public interface LotteryTimeDao extends Dao<LotteryTime, Serializable> {
	LotteryTime findByCustomer(Customer customer);
	
	@Modifying
	@Query(value="update LotteryTime l set l.times = l.times - 1 where l.customer = ?1 and l.times > 0")
	int decTimes(Customer customer);
}

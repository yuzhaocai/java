/**
 * 
 */
package com.lczy.media.repositories;

import java.io.Serializable;

import com.lczy.common.data.Dao;
import com.lczy.media.entity.ChargeLog;
import com.lczy.media.entity.Customer;

/**
 * @author wu
 *
 */
public interface ChargeLogDao extends Dao<ChargeLog, Serializable> {
	
	/**
	 * 查询以往充值成功记录
	 * 
	 * @param customer
	 * @param status
	 * @return
	 */
	ChargeLog findTopByCustomerAndStatusOrderByModifyTimeAsc(Customer customer, String status);

}

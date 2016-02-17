package com.lczy.media.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.lczy.common.data.Dao;
import com.lczy.media.entity.FreezedLog;

public interface FreezedLogDao extends Dao<FreezedLog, Serializable> {

	/**
	 * 根据订单ID更新冻结单状态.
	 * 
	 * @param orderId 订单ID
	 * @param statusCode 新的状态编码.
	 * 
	 * @return 更新的记录数。
	 */
	@Modifying
	@Query(value="update FreezedLog l set l.status = ?2 where l.order.id = ?1")
	int setStatus(String orderId, String statusCode);

}

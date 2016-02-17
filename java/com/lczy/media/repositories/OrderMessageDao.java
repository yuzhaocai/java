package com.lczy.media.repositories;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.lczy.common.data.Dao;
import com.lczy.media.entity.OrderMessage;

public interface OrderMessageDao extends Dao<OrderMessage, Serializable> {
	
	@Query(value = "select order_id, count(id) as counter from t_order_message "
			+ "where order_id in (?1) and create_time > ?2 group by order_id",
			nativeQuery = true)
	List<Object[]> countMap(List<String> ids, Date time);
}

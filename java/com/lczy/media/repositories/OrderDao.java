/**
 * 
 */
package com.lczy.media.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.lczy.common.data.Dao;
import com.lczy.media.entity.Customer;
import com.lczy.media.entity.Order;

/**
 * @author wu
 *
 */
public interface OrderDao extends Dao<Order, Serializable> {

	@Query(value = "select * from t_order where media_id=?1 ", nativeQuery = true)
	List<Order> findByMediaId(String mediaId);
	
	@Modifying
	@Query(value="update Order l set l.receiver = ?2, l.paymentType = 'PAYMENT_T_OFFLINE' where l.id = ?1 and l.paymentType = 'PAYMENT_T_ONLINE' and l.status = 'ORDER_S_PROGRESS'")
	int loan(String id, Customer customer);
}

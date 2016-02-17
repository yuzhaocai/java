/**
 * 
 */
package com.lczy.media.repositories;

import java.io.Serializable;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.QueryHints;

import com.lczy.common.data.Dao;
import com.lczy.media.entity.Deliverable;

/**
 * @author wu
 *
 */
public interface DeliverableDao extends Dao<Deliverable, Serializable> {
	
	@QueryHints({@QueryHint(name="org.hibernate.cacheable", value="true")})
	Deliverable findByOrderId(String orderId);
}

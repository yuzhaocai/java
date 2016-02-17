/**
 * 
 */
package com.lczy.media.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lczy.common.service.AbstractService;
import com.lczy.media.entity.Deliverable;
import com.lczy.media.repositories.DeliverableDao;

/**
 * @author wu
 * 
 */
@Service
@Transactional(readOnly=true)
public class DeliverableService extends AbstractService<Deliverable> {
	
	@Transactional(readOnly=false)
	public Deliverable save(Deliverable deliverable) {
		return getDao().save(deliverable);
	}
	
	public Deliverable findByOrderId(String orderId) {
		return ((DeliverableDao)getDao()).findByOrderId(orderId);
	}
}

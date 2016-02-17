/**
 * 
 */
package com.lczy.media.service;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lczy.common.service.AbstractService;
import com.lczy.media.entity.Complaint;
import com.lczy.media.entity.User;
import com.lczy.media.util.Constants;
import com.lczy.media.util.UserContext;

/**
 * 举报投诉服务类.
 * 
 * @author wu
 *
 */
@Service
@Transactional(readOnly=true)
public class ComplaintService extends AbstractService<Complaint> {
	
	@Autowired
	private OrderService orderService;

	public Complaint findByOrderId(String id,String userId) {
		Map<String, Object> searchParams = Maps.newHashMap();
		searchParams.put("EQ_order.id", id);
		searchParams.put("EQ_createBy", userId);
		
		return findOne(searchParams);
	}
	
	/**
	 * 拒付审核 - 通过
	 * 
	 * @param id
	 */
	@Transactional(readOnly=false)
	public void auditPass(String id) {
		User user = UserContext.getCurrent();
		
		// 处理拒付单
		Complaint complaint = this.get(id);
		complaint.setHandleResult(Constants.HandleResult.REFUND);
		complaint.setHandler(user.getId());
		complaint.setHandleTime(new Date());
		this.save(complaint);
		
		// 退款
		orderService.refund(complaint.getOrder());		
	}
	
	/**
	 * 拒付审核 - 强制支付
	 * 
	 * @param id
	 * @param comment
	 */
	@Transactional(readOnly=false)
	public void auditReject(String id, String comment) {
		User user = UserContext.getCurrent();
		
		// 处理拒付单
		Complaint complaint = this.get(id);
		complaint.setHandleResult(Constants.HandleResult.PAY);
		complaint.setHandleComment(comment);
		complaint.setHandler(user.getId());
		complaint.setHandleTime(new Date());
		this.save(complaint);
		
		// 强制付款
		orderService.forcePay(complaint.getOrder());
	}
	
}

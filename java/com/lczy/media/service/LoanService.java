package com.lczy.media.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lczy.common.exception.ServiceException;
import com.lczy.common.service.AbstractService;
import com.lczy.media.entity.Loan;
import com.lczy.media.entity.Order;
import com.lczy.media.entity.User;
import com.lczy.media.repositories.OrderDao;
import com.lczy.media.util.Constants;
import com.lczy.media.util.UserContext;

@Service
@Transactional(readOnly=true)
public class LoanService extends AbstractService<Loan>{
	
	@Value("#{application['repayment.account']}")
	private String repaymentAccount = "18888888888";
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 垫资支付申请
	 * 
	 * @param orderId
	 * @throws Exception
	 */
	@Transactional(readOnly=false)
	public void apply(String orderId) throws Exception {
		Order order = orderDao.findOne(orderId);
		
		if (!order.getStatus().equals(Constants.OrderStatus.PROGRESS)) {
			throw new ServiceException("订单状态错误！");
		}
		
		if (!order.getPaymentType().equals(Constants.PaymentType.ONLINE)) {
			throw new ServiceException("订单已经线下垫资支付！");
		}
		
		if (order.getLoan() != null) {
			throw new ServiceException("垫资已经申请！");
		}
		
		Loan loan = new Loan();
		loan.setOrder(order);
		loan.setStatus(Constants.LoanStatus.AUDIT);
		loan.setCreateBy(UserContext.getCurrentCustomer());
		loan.setCreateTime(new Date());
		this.save(loan);
		
		order.setLoan(loan);
		orderDao.save(order);
	}
	
	/**
	 * 垫资支付审核通过
	 * 
	 * @param id
	 * @throws Exception
	 */
	@Transactional(readOnly=false)
	public void approve(String id) throws Exception {
		Loan loan = this.get(id);
		loan.setModifyBy(UserContext.getCurrentCustomer());
		loan.setModifyTime(new Date());
		loan.setStatus(Constants.LoanStatus.PASS);
		this.save(loan);
		
		User user = userService.getByLoginName(repaymentAccount);
		if (orderDao.loan(loan.getOrder().getId(), user.getCustomer()) != 1) {
			throw new ServiceException("订单状态错误或者已经垫资支付成功！");
		}
	}
	
	/**
	 * 垫资支付审核驳回
	 * 
	 * @param id
	 * @throws Exception
	 */
	@Transactional(readOnly=false)
	public void reject(String id) throws Exception {
		Loan loan = this.get(id);
		loan.setModifyBy(UserContext.getCurrentCustomer());
		loan.setModifyTime(new Date());
		loan.setStatus(Constants.LoanStatus.REJECT);
		this.save(loan);
		
		Order order = loan.getOrder();
		order.setLoan(null);
		orderDao.save(order);
	}
	
	
}

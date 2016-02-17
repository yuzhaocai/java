/**
 * 
 */
package com.lczy.media.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lczy.common.service.AbstractService;
import com.lczy.media.entity.Account;
import com.lczy.media.entity.Customer;
import com.lczy.media.entity.Invoice;
import com.lczy.media.entity.Media;
import com.lczy.media.entity.Order;
import com.lczy.media.entity.RefundLog;
import com.lczy.media.entity.Requirement;
import com.lczy.media.entity.Transaction;
import com.lczy.media.entity.User;
import com.lczy.media.repositories.AccountDao;
import com.lczy.media.repositories.FreezedLogDao;
import com.lczy.media.repositories.InvoiceDao;
import com.lczy.media.repositories.OrderDao;
import com.lczy.media.repositories.RefundLogDao;
import com.lczy.media.repositories.TransactionDao;
import com.lczy.media.util.Constants;
import com.lczy.media.util.Constants.FreezedStatus;
import com.lczy.media.util.Constants.InvoiceProvider;
import com.lczy.media.util.Constants.OrderStatus;
import com.lczy.media.util.Constants.TransactionType;
import com.lczy.media.util.CreditConfig;
import com.lczy.media.util.UserContext;

/**
 * @author wu
 *
 */
@Service
@Transactional(readOnly=true)
public class OrderService extends AbstractService<Order> {
	
	@Autowired
	private FreezedLogDao freezedLogDao;
	
	@Autowired
	private TransactionDao transactionDao;
	
	@Autowired
	private AccountDao accountDao;
	
	@Autowired
	private SiteMessageService siteMessageService;
	
	@Autowired
	private InvoiceDao invoiceDao;
	
	@Autowired
	private SysConfigService sysConfigService;
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private RefundLogDao refundLogDao;
	
	@Autowired
	private CustCreditService custCreditService;
	
	@Transactional(readOnly=false)
	public void deliverable(Order order) {
		sendSiteMessageDeliver(order);
		save(order);
	}
	
	private void sendSiteMessageDeliver(Order order) {
		Requirement req = order.getRequirement();
		Media media = order.getMedia();
		String content = MessageTemplate.get(MessageType.ADVERTISER_DELIVER.key(), media.getName(),
				order.getId());
		siteMessageService.send(siteMessageService.getSystemSender().getId(), 
				order.getReqOwner().getId(), MessageType.ADVERTISER_DELIVER, content);
	}
	/**
	 * 订单付款。
	 * @param id 订单ID
	 */
	@Transactional(readOnly=false)
	public void pay(Order order, Invoice invoice) {
		//检查订单状态
		checkStatusForPay(order);
		
		//更新订单状态为已完成
		order.setStatus(OrderStatus.FINISHED);
		order.setFinished(true);
		updateOrder(order);
		
		//更新冻结单状态
		updateFreezedLog(order);
		
		if( order.getInvoice() == 0 ) {
			//无需开具发票，则把订单中的金额转入媒体主账户
			transferAccount(order);
			
			//发送站内信
			sendMessageComplete(order);
			
		} else {
			//需要开具发票，则由后台管理程序，人工控制转账
			invoice.setOrder(order);
			invoice.setProvider(getProvider(order));
			invoice.setCreateTime(new Date());
			invoice.setStatus(Constants.InvoiceStatus.CREATED);
			invoiceDao.save(invoice);
		}
		
		//交易成功，增加双方信用值
//		updateCredit(order);
	}

	/**
	 * 根据订单信息获取发票提供方.
	 */
	private String getProvider(Order order) {
/*		Media media = order.getMedia();
		if( media.isProvideInvoice() ) {
			return InvoiceProvider.MEDIA;
		} else {
			return InvoiceProvider.SYSTEM;
		}*/
		return InvoiceProvider.SYSTEM;
	}
	
	/**
	 * 发送"订单完成"站内信
	 */
	private void sendMessageComplete(Order order) {
		String content = MessageTemplate.get(MessageType.ADVERTISER_ORDER_COMPLETE.key(), 
				order.getMedia().getName(), order.getId());
		
		siteMessageService.send(siteMessageService.getSystemSender().getId(), 
				order.getReqOwner().getId(), 
				MessageType.ADVERTISER_ORDER_COMPLETE, content);
		//给媒体发送
		String mediaContent = MessageTemplate.get(MessageType.MEDIA_ORDER_COMPLETE.key(), 
				order.getReqOwner().getName(), order.getId());
		siteMessageService.send(siteMessageService.getSystemSender().getId(), 
				order.getMediaOwner().getId(), 
				MessageType.MEDIA_ORDER_COMPLETE, mediaContent);
	}
	
	/**
	 * 增加订单双方交易成功后的信用值
	 */
	private void updateCredit(Order order) {
		Customer reqOwner = order.getReqOwner();
		Customer mediaOwner = order.getMediaOwner();
		String memo = "订单：%s, 交易成功！信用值%+d, 虚拟币%+d。";
		int credit = CreditConfig.ACT_TRANSACTION_SUCCESS[0];
		int vc     = CreditConfig.ACT_TRANSACTION_SUCCESS[1];
		custCreditService.change(reqOwner, credit, vc, String.format(memo, order.getId(), credit, vc));
		
		vc = 0;
		custCreditService.change(mediaOwner, credit, vc, String.format(memo, order.getId(), credit, vc));
	}

	/**
	 * 把订单中的金额转入媒体主账户
	 */
	private void transferAccount(Order order) {
		// 计算扣除佣金后给媒体主打款金额
		float commissionRate = sysConfigService.getCommissionRate();
		int amount = (int) (order.getAmountMedia() * (1 - commissionRate));
		Date now = new Date();
		
		// 生成交易记录
		Transaction t = new Transaction();
		t.setOrder(order);
		t.setAmount(amount);
		t.setCreateTime(now);
		t.setRemark("订单编号：" + order.getId());
		
		Account target = null;
		if (order.getPaymentType().equals(Constants.PaymentType.OFFLINE)) {
			target = order.getReceiver().getAccount();
			t.setType(TransactionType.REPAYMENT);
			t.setCustomer(order.getReceiver());
		} else {
			target = order.getMediaOwner().getAccount();
			t.setType(TransactionType.INCOME);
			t.setCustomer(order.getMediaOwner());
		}
		transactionDao.save(t);
		
		// 扣除税点和佣金后给媒体主打款
		accountDao.incBalance(target.getId(), amount, UserContext.getSystemUser().getId(), now);
	}

//	/**
//	 * 把订单中的金额转入媒体主账户
//	 */
//	private void transferAccount(Order order, boolean tax) {
//		if (tax) {
//			Date now = new Date();
//			Account target = order.getMediaOwner().getAccount();
//			
//			// 计算扣除税点和佣金后给媒体主打款金额
//			float taxRate = sysConfigService.getTaxRate();
//			float commissionRate = sysConfigService.getCommissionRate();
//			int amount = (int) (order.getAmountMedia() * (1 - taxRate - commissionRate));
//			
//			// 生成交易记录
//			Transaction t = new Transaction();
//			t.setOrder(order);
//			t.setCustomer(order.getMediaOwner());
//			t.setAmount(amount);
//			t.setCreateTime(now);
//			t.setType(TransactionType.INCOME);
//			t.setRemark("订单编号：" + order.getId());
//			transactionDao.save(t);
//			
//			// 扣除税点和佣金后给媒体主打款
//			accountDao.incBalance(target.getId(), amount, UserContext.getSystemUser().getId(), now);
//		} else {
//			transferAccount(order);
//		}
//	}

	/**
	 * 更新订单关联的“冻结单”状态为“支付”.
	 */
	private void updateFreezedLog(Order order) {
		freezedLogDao.setStatus(order.getId(), FreezedStatus.PAID);
	}

	/**
	 * 检查订单状态是否支持支付.
	 */
	private void checkStatusForPay(Order order) {
		if( !order.getStatus().equals(OrderStatus.ACCEPTANCE) )
			throw new IllegalStateException("支付失败。原因：订单状态（" + order.getStatus() + "）非法！");
	}

	/**
	 * @param order 更新的订单
	 */
	private void updateOrder(Order order) {
		order.setModifyTime(new Date());
		order.setModifyBy(UserContext.getCurrent().getId());
		getDao().save(order);
	}

	/**
	 * 订单付款，订单结束。
	 * 
	 * @param id 订单ID
	 */
	@Transactional(readOnly=false)
	public void finish(String id) {
		// 修改发票状态
		Invoice invoice = invoiceDao.findOne(id);
		invoice.setStatus(Constants.InvoiceStatus.FINISHED);
		invoice.setHandler(UserContext.getCurrent().getId());
		invoice.setHandleTime(new Date());
		invoiceDao.save(invoice);
			
		// 转账
		transferAccount(invoice.getOrder());
		
		//发送站内信
		sendMessageComplete(invoice.getOrder());
	}
	
	/**
	 * 拒付订单强制付款。
	 * 
	 * @param order
	 */
	@Transactional(readOnly=false)
	public void forcePay(Order order) {
		User user = UserContext.getSystemUser();
		
		order.setStatus(Constants.OrderStatus.FINISHED);
		order.setFinished(true);
		order.setModifyBy(user.getId());
		order.setModifyTime(new Date());
		save(order);
		
		// 处理冻结单
		freezedLogDao.setStatus(order.getId(), Constants.FreezedStatus.PAID);
		
		this.transferAccount(order);
	}
	
	/**
	 * 拒付订单退款。
	 * 
	 * @param order
	 */
	@Transactional(readOnly=false)
	public void refund(Order order) {
		Date now = new Date();
		User user = UserContext.getSystemUser();
		
		// 处理订单
		order.setStatus(Constants.OrderStatus.FINISHED);
		order.setFinished(true);
		order.setModifyBy(user.getId());
		order.setModifyTime(now);
		save(order);
		
		// 处理冻结单
		freezedLogDao.setStatus(order.getId(), Constants.FreezedStatus.REFUND);
		
		// 处理广告主账户
		Account account = order.getReqOwner().getAccount();
		accountDao.incBalance(account.getId(), order.getAmount(), user.getId(), now);
		
		// 处理退款日志
		RefundLog t = new RefundLog();
		t.setOrder(order);
		t.setCustomer(order.getReqOwner());
		t.setCreateTime(now);
		t.setModifyTime(now);
		t.setDeleted(false);
		refundLogDao.save(t);
	}
	
	/**
	 * @param mediaId
	 *            根据媒体id查找订单
	 */
	public List<Order> findByMediaId(String mediaId) {
		return orderDao.findByMediaId(mediaId);
	}
}

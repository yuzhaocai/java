package com.lczy.media.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lczy.common.exception.ServiceException;
import com.lczy.common.service.AbstractService;
import com.lczy.media.entity.Account;
import com.lczy.media.entity.Customer;
import com.lczy.media.entity.FreezedLog;
import com.lczy.media.entity.Media;
import com.lczy.media.entity.Order;
import com.lczy.media.entity.ReqMedia;
import com.lczy.media.entity.Requirement;
import com.lczy.media.entity.Transaction;
import com.lczy.media.exception.BalanceNotEnoughException;
import com.lczy.media.repositories.AccountDao;
import com.lczy.media.repositories.CustCreditLogDao;
import com.lczy.media.repositories.CustomerDao;
import com.lczy.media.repositories.FreezedLogDao;
import com.lczy.media.repositories.OrderDao;
import com.lczy.media.repositories.TransactionDao;
import com.lczy.media.util.Constants;
import com.lczy.media.util.CreditConfig;
import com.lczy.media.util.UserContext;
import com.lczy.media.util.Constants.InviteType;

@Service
@Transactional(readOnly=true)
public class ReqMediaService extends AbstractService<ReqMedia> {
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private TransactionDao transactionDao;
	
	@Autowired
	private FreezedLogDao freezedLogDao;
	
	@Autowired
	private AccountDao accountDao;
	
	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private CustCreditLogDao creditLogDao;
	
	@Autowired
	private SiteMessageService siteMessageService;
	
	@Autowired
	private CustCreditService custCreditService;
	
	public ReqMedia findOne(String reqMediaId) {
		return getDao().findOne(reqMediaId);
	}

	@Transactional(readOnly=false)
	public ReqMedia update(ReqMedia reqMedia) {
		return getDao().save(reqMedia);
	}
	
	/** 媒体接受应邀
	 * @param id 预约单ID
	 */
	@Transactional(readOnly=false)
	public void dealAccept(String id) {
		ReqMedia reqMedia = findOne(id);
		reqMedia.setFbStatus(Constants.MediaFeedback.ACCEPT);
		reqMedia.setFbTime(new Date());
		sendMessageDealAccept(reqMedia);
		getDao().save(reqMedia);
	}
	private void sendMessageDealAccept(ReqMedia reqMedia) {
		Requirement req = reqMedia.getRequirement();
		Media media = reqMedia.getMedia();
		String content = MessageTemplate.get(MessageType.ADVERTISER_INVIT.key(), media.getName(),
				req.getName());
		siteMessageService.send(siteMessageService.getSystemSender().getId(), 
				req.getCustomer().getId(), MessageType.ADVERTISER_INVIT, content);
	}
	/** 媒体拒绝应邀
	 * @param id 预约ID
	 * @param reason 拒绝理由
	 */
	@Transactional(readOnly=false)
	public void dealRefuse(String id, String reason) {
		Date now = new Date();
		ReqMedia reqMedia = findOne(id);
		reqMedia.setFbStatus(Constants.MediaFeedback.REFUSE);
		reqMedia.setFbTime(now);
		reqMedia.setRefuseReason(reason);
		sendMessageDealRefuse(reqMedia);
		update(reqMedia);
	}
	private void sendMessageDealRefuse(ReqMedia reqMedia) {
		Requirement req = reqMedia.getRequirement();
		Media media = reqMedia.getMedia();
		String content = MessageTemplate.get(MessageType.ADVERTISER_INVIT_REJECT.key(), media.getName(),
				req.getName());
		siteMessageService.send(siteMessageService.getSystemSender().getId(), 
				req.getCustomer().getId(), MessageType.ADVERTISER_INVIT_REJECT, content);
	}
	
	/**
	 * 接受媒体报价，生成订单.
	 * 
	 * @param id 预约单 ID.
	 */
	@Transactional(readOnly=false)
	public void accept(String id) {
		ReqMedia reqMedia = get(id);
		
		checkStatus(reqMedia);
		
		//更新预约单状态
		reqMedia.setCfStatus(Constants.AdverConfirm.ACCEPT);
		reqMedia.setCfTime(new Date());
		save(reqMedia);
		
		//生成订单
		Order order = saveOrder(reqMedia);
		//生成交易记录
		saveTransaction(order);
		
		//生成冻结单
		saveFreezedLog(order);
		
		//扣减账户余额
		reduceBalance(order);
		
		//发送通知
		sendMessageAccept(order, reqMedia);
	}
	
	private void sendMessageAccept(Order order, ReqMedia reqMedia) {
		Requirement req = order.getRequirement();
		Media media = order.getMedia();
		Customer reqOwner = order.getReqOwner();
		Customer mediaOwner = order.getMediaOwner();
		String content = MessageTemplate.get(MessageType.ADVERTISER_ORDER_CREATE.key(), 
				req.getName(), media.getName(), order.getId());
		siteMessageService.send(siteMessageService.getSystemSender().getId(), 
				reqOwner.getId(), MessageType.ADVERTISER_ORDER_CREATE, content);
		if (InviteType.PASSIVE.equals(reqMedia.getInviteType())) {
			String mediaContent = MessageTemplate.get(MessageType.MEDIA_INVIT.key(), 
					reqOwner.getName(), req.getName(), order.getId());
			siteMessageService.send(siteMessageService.getSystemSender().getId(), 
					mediaOwner.getId(), MessageType.MEDIA_INVIT, mediaContent);
		} else if (InviteType.ACTIVE.equals(reqMedia.getInviteType())) {
			String mediaContent = MessageTemplate.get(MessageType.MEDIA_INVIT_ACTIVE.key(), 
					reqOwner.getName(), req.getName(), order.getId());
			siteMessageService.send(siteMessageService.getSystemSender().getId(), 
					mediaOwner.getId(), MessageType.MEDIA_INVIT_ACTIVE, mediaContent);
		}
	}
	/**
	 * 移动端生成订单
	 * @param id
	 * @param status
	 * @return 订单
	 */
	@Transactional(readOnly=false)
	public Order accept(String id,String status) {
		
		ReqMedia reqMedia = get(id);
		
		checkStatus(reqMedia);
		
		//更新预约单状态
		reqMedia.setCfStatus(Constants.AdverConfirm.ACCEPT);
		reqMedia.setCfTime(new Date());
		save(reqMedia);
		
		//生成订单
		Order order = saveOrder(reqMedia);
		
		//生成交易记录
		saveTransaction(order);
		
		//生成冻结单
		saveFreezedLog(order);
		
		//扣减账户余额
		reduceBalance(order);
		
		//发送通知
		sendMessageAccept(order, reqMedia);
		
		return order;
	}
	
	
	
	

	private void checkStatus(ReqMedia reqMedia) {
		if( ! Constants.AdverConfirm.NULL.equals(reqMedia.getCfStatus()) ) {
			throw new ServiceException("预约单状态异常！当前 CF_STATUS = " + reqMedia.getCfStatus() );
		}
	}

	private void reduceBalance(Order order) {
		Account account = order.getReqOwner().getAccount();
		accountDao.refresh(account);
		
		if( account.getAvBalance() < order.getAmount() ) {
			String temp = "账户余额不足！当前余额：%d, 订单金额：%d。";
			throw new BalanceNotEnoughException(String.format(temp, account.getAvBalance(), order.getAmount()));
		}
		
		account.setAvBalance(account.getAvBalance() - order.getAmount());
		account.setModifyBy(UserContext.getCurrent().getId());
		account.setModifyTime(new Date());
		accountDao.save(account);
	}

	private void saveTransaction(Order order) {
		Transaction t = new Transaction();
		t.setOrder(order);
		t.setAmount(order.getAmount());
		t.setCustomer(order.getReqOwner());
		t.setCreateTime(new Date());
		t.setType(Constants.TransactionType.CONSUME);
		t.setRemark("订单编号：" + order.getId());
		
		transactionDao.save(t);
	}

	private void saveFreezedLog(Order order) {
		FreezedLog log = new FreezedLog();
		log.setAmount(order.getAmount());
		log.setCreateTime(new Date());
		log.setCustomer(order.getReqOwner());
		log.setOrder(order);
		log.setStatus(Constants.FreezedStatus.FREEZED);
		
		freezedLogDao.save(log);
	}

//	private void sendMessage(Order order) {
//		//# 成功接单-应邀
//		//site.message.order.receiving=恭喜, 您应邀的需求: "%s" 已成功生成订单, 广告主所支付资金已被系统托管。
//		String content = MessageTemplate.get(MessageType.MEDIA_INVIT.key(), order.getRequirement().getName());
//		String senderId = order.getReqOwner().getId();
//		String receiverId = order.getMediaOwner().getId();
//		
//		siteMessageService.send(senderId, receiverId, MessageType.MEDIA_INVIT, content);
//	}

	/**
	 * 根据预约单生成订单.
	 */
	private Order saveOrder(ReqMedia rm) {
		Order order = new Order();
		order.setMedia(rm.getMedia());
		order.setMediaOwner(rm.getMedia().getCustomer());
		order.setRequirement(rm.getRequirement());
		order.setReqOwner(rm.getRequirement().getCustomer());
		order.setAmount(rm.getPrice());
		order.setAmountMedia(rm.getPriceMedia());
		order.setTax(rm.getTax());
		order.setQuoteType(rm.getQuoteType());
		order.setCreateBy(UserContext.getCurrent().getId());
		order.setCreateTime(new Date());
		order.setModifyBy(order.getCreateBy());
		order.setModifyTime(order.getCreateTime());
		order.setStatus(Constants.OrderStatus.PROGRESS);
		order.setPaymentType(Constants.PaymentType.ONLINE);
		
		return orderDao.save(order);
	}

	/**
	 * 广告主“拒绝”媒体.
	 * @param id 预约单 ID.
	 */
	@Transactional(readOnly=false)
	public void refuse(String id) {
		ReqMedia reqMedia = get(id);
		
		checkStatus(reqMedia);
		
		//更新预约单状态
		reqMedia.setCfStatus(Constants.AdverConfirm.REFUSE);
		reqMedia.setCfTime(new Date());
		save(reqMedia);
		
		//扣减信用值
//		reduceCredit(reqMedia);
	}

	/**
	 * 如果媒体为广告主"邀请"且并"未改稿"，拒绝媒体时需要扣减广告主的信用值.
	 * @param reqMedia 预约单.
	 */
	private void reduceCredit(ReqMedia reqMedia) {
		if( Constants.InviteType.PASSIVE.equals(reqMedia.getInviteType())
				&& !reqMedia.isChanged() ) {
			
			int credit = CreditConfig.ACT_REJECT_NOT_CHANGE[0];
			int vc     = CreditConfig.ACT_REJECT_NOT_CHANGE[1];
			Customer c = reqMedia.getRequirement().getCustomer();
			String memo = "需求：%s, 拒绝未改稿的邀请媒体。信用值%+d，虚拟币%+d。";
			
			custCreditService.change(c, credit, vc, String.format(memo, reqMedia.getRequirement().getId(), credit, vc));
			
		}
	}
	
}

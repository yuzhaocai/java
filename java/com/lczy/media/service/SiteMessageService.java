/**
 * 
 */
package com.lczy.media.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lczy.common.service.AbstractService;
import com.lczy.media.entity.Customer;
import com.lczy.media.entity.SiteMessage;
import com.lczy.media.repositories.CustomerDao;
import com.lczy.media.repositories.SiteMessageDao;

/**
 * 提供站内信服务.
 * @author wu
 *
 */
@Service
@Transactional(readOnly=true)
public class SiteMessageService extends AbstractService<SiteMessage> {
	
	@Autowired
	private CustomerDao customerDao;

	/**
	 * 更新消息列表为已读状态.
	 * @param ids 以逗号分隔的id列表.
	 */
	@Transactional(readOnly=false)
	public void setReadFlag(String[] ids) {
		((SiteMessageDao)getDao()).setReadFlag(ids);
	}
	
	/**
	 * 发送站内信.
	 * @param senderId 发送者会员ID
	 * @param receiverId 接收者会员ID
	 * @param type 消息类型
	 * @param content 消息内容
	 * @return SiteMessage实例.
	 */
	@Transactional(readOnly=false)
	public SiteMessage send(String senderId, String receiverId, MessageType type, String content) {
		Customer sender = customerDao.findOne(senderId);
		Customer receiver = customerDao.findOne(receiverId);
		SiteMessage sm = new SiteMessage();
		sm.setSender(sender);
		sm.setReceiver(receiver);
		sm.setType(type.toString());
		sm.setContent(content);
		sm.setReadFlag(0);
		sm.setCreateTime(new Date());
		
		return getDao().save(sm);
	}

	/**
	 * @return 表示"系统消息"的会员.
	 */
	public Customer getSystemSender() {
		return customerDao.findOne("1");
	}
}

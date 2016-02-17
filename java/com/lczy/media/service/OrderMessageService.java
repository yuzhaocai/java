package com.lczy.media.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lczy.common.service.AbstractService;
import com.lczy.media.entity.Media;
import com.lczy.media.entity.OrderMessage;
import com.lczy.media.repositories.MediaDao;

@Service
@Transactional(readOnly=true)
public class OrderMessageService extends AbstractService<OrderMessage>{
	
	@Autowired
	private MediaDao mediaDao;
	
	/**
	 * @param orderId
	 *            根据订单id查找留言
	 */
	public List<OrderMessage> findByOrderId(String orderId) {
		Map<String, Object> searchMap = Maps.newLinkedHashMap();
		searchMap.put("EQ_order.id", orderId);
		searchMap.put("EQ_display", true);
		
		return find(searchMap, "ASC_createTime");
	}
	
	/**
	 * @param mediaId 媒体ID
	 * 根据订单内媒体ID查找媒体名称
	 */
	public Media findByMediaId(String mediaId){
		return mediaDao.findOne(mediaId);
	}
	
}

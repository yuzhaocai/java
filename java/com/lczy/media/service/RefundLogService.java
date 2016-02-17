package com.lczy.media.service;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lczy.common.service.AbstractService;
import com.lczy.media.entity.RefundLog;
import com.lczy.media.repositories.RefundLogDao;

@Service
@Transactional(readOnly=true)
public class RefundLogService extends AbstractService<RefundLog> {

	/**
	 * 删除退款记录.
	 * @param id
	 */
	@Transactional(readOnly=false)
	public void delete(String id) {
		((RefundLogDao)getDao()).setDelete(id, new Date());
	}

}

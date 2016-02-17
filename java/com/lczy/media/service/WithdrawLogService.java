package com.lczy.media.service;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lczy.common.service.AbstractService;
import com.lczy.media.entity.WithdrawLog;
import com.lczy.media.util.Constants;
import com.lczy.media.util.UserContext;

@Service
@Transactional(readOnly = true)
public class WithdrawLogService extends AbstractService<WithdrawLog> {
	
	/**
	 * 审核通过
	 * 
	 * @param id
	 */
	@Transactional(readOnly = false)
	public void audit(String id) {
		WithdrawLog log = this.get(id);
		log.setStatus(Constants.WithdrawStatus.FINISHED);
		log.setModifyBy(UserContext.getCurrent());
		log.setModifyTime(new Date());
		
		this.save(log);
	}

}

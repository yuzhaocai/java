package com.lczy.media.service;

import java.util.Date;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lczy.common.data.JPAUtil;
import com.lczy.media.entity.OpLogging;
import com.lczy.media.repositories.OpLoggingDao;
import com.lczy.media.util.UserContext;

@Service
@Transactional(readOnly = true)
public class OpLoggingService {

	@Autowired
	private OpLoggingDao opLoggingDao;
	
	/**
	 * 分页查找日志.
	 * 
	 * @throws Exception
	 */
	public Page<OpLogging> find(Map<String, Object> searchParams, int page, int size, String sort) throws Exception {
		
        Pageable pageable = JPAUtil.buildPageRequest(page, size, sort);
		
		Specification<OpLogging> spec = JPAUtil.buildSpecification(searchParams);
		
		return opLoggingDao.findAll(spec, pageable);
	}
	
	public OpLogging get(String logId) throws Exception {
		return opLoggingDao.findOne(logId);
	}
	
	/**
	 * 保存操作日志.
	 * @param log
	 * @throws Exception
	 */
	@Transactional(readOnly=false)
	public void save(OpLogging log) {
		opLoggingDao.save(log);
	}
	
	/**
	 * 助手方法，根据ServletRequest创建一个OpLogging实例.
	 * @return 返回的实例自动填充了ip/userId/createTime属性.
	 * 
	 */
	public static OpLogging newOpLogging(ServletRequest request) {
		OpLogging log = new OpLogging();
		log.setIp(request.getRemoteAddr());
		String userId = UserContext.getCurrent().getId();
		log.setUserId(userId);
		log.setCreateTime(new Date());
		return log;
	}
	
}

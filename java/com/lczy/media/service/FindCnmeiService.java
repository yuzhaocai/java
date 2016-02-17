package com.lczy.media.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lczy.common.service.AbstractService;
import com.lczy.media.entity.FindCnmei;
import com.lczy.media.repositories.FindCnmeiDao;

@Service
@Transactional(readOnly=true)
public class FindCnmeiService extends AbstractService<FindCnmei>{
	@Autowired
	private FindCnmeiDao findCnmeiDao;
	
	/**
	 * 清空发现采媒数据
	 */
	@Transactional(readOnly=false)
	public void deleteAll(){
		findCnmeiDao.deleteAll();
	}

}

package com.lczy.media.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lczy.common.exception.ServiceException;
import com.lczy.media.entity.Function;
import com.lczy.media.repositories.FunctionDao;
import com.lczy.media.security.ShiroCacheManager;
import com.lczy.media.service.common.FunctionProvider;

@Component
@Transactional(readOnly = true)
public class FunctionService {
	
	@Autowired
	private FunctionDao functionDao;

	
	@Autowired
	private ShiroCacheManager shiroCacheManager;
	
	@Autowired
	private FunctionProvider functionProvider;

	@Transactional(readOnly = false)
	public void saveFunction(Function f) {
		try {
			functionDao.save(f);
			resetApplicationCached();
			
		} catch (Exception e) {
			throw new ServiceException("保存or更新Function时发生异常", e);
		}
	}

	/**
	 * 重新加载应用中的缓存.
	 */
	private void resetApplicationCached() {
		functionProvider.setDirtyFlag();
		shiroCacheManager.clear();
	}
	
	@Transactional(readOnly = false)
	public void delFunction(String fid) {

		functionDao.delete(fid);
		
		resetApplicationCached();
	}
	

	public Function getFunction(String fid) {
		return functionDao.findOne(fid);
	}

	public List<Function> findByPId(String pid) {
		
		return functionDao.findByParentId(pid, new Sort("seqNum"));
	}
	
	public int countByPId(String pid) {
		
		return functionDao.countByParentId(pid);
	}

	public Function getByNameAndPId(String funcName, String pId) {
		
		return functionDao.findByFuncNameAndParentId(funcName, pId);
	}

	public List<Function> findAll() {
		
		return functionDao.findAll(null, new Sort("funcName"));
	}
	

}

package com.lczy.media.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lczy.media.entity.SysConfig;
import com.lczy.media.repositories.SysConfigDao;

@Service
@Transactional(readOnly=true)
public class SysConfigService {
	
	private static final Logger log = LoggerFactory.getLogger(SysConfigService.class);

	@Autowired
	private SysConfigDao sysConfigDao;
	
	/**
	 * 查询系统配置项.
	 * 
	 * @param name 配置项名称.
	 * @return 系统配置项的值.
	 */
	public SysConfig get(String name) {
		return sysConfigDao.findOne(name);
	}
	

	/**
	 * 设置系统配置项.
	 * 
	 * @param name 配置项名称.
	 * @param value 配置项的值.
	 */
	@Transactional(readOnly=false)
	public void set(String name, String value) {
		SysConfig config = get(name);
		if (config != null) {
			log.debug("更新系统配置项 '{}' 的值，原始值={}, 更新新值={}", name, config.getValue(), value);
			config.setValue(value);
		} else {
			log.debug("设置系统配置项： {} = {}", name, value);
			config = new SysConfig();
			config.setName(name);
			config.setValue(value);
		}
		sysConfigDao.save(config);
	}
	
	/**
	 * 查询税率
	 * 
	 * @return
	 */
	public float getTaxRate() {
		try {
			SysConfig config = sysConfigDao.findOne("INVOICE_TAX_RATE");
			return Float.parseFloat(config.getValue());
		} catch (Exception e) {
			return 0.0672f;
		}
	}

	/**
	 * 查询佣金率
	 * 
	 * @return
	 */
	public float getCommissionRate() {
		try {
			SysConfig config = sysConfigDao.findOne("COMMISSION_RATE");
			return Float.parseFloat(config.getValue());
		} catch (Exception e) {
			return 0;
		}
	}
	@Transactional(readOnly=false)
	public void save(SysConfig sysConfig){
		sysConfigDao.save(sysConfig);
	}
}

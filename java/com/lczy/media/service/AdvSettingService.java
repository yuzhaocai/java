package com.lczy.media.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lczy.common.service.AbstractService;
import com.lczy.media.entity.AdvSetting;
import com.lczy.media.repositories.AdvSettingDao;

@Service
@Transactional(readOnly=true)
public class AdvSettingService extends AbstractService<AdvSetting>{
	
	@Autowired
	private AdvSettingDao advSettingDao;
	
	/**删除广告墙
	 * @param id
	 */
	@Transactional(readOnly=false)
	public void deleteAdv(AdvSetting as){
		advSettingDao.delete(as);
	}
	
	/**查询首页广告墙
	 * @param id
	 */
	public List<AdvSetting> findAdvSetting(){
		return advSettingDao.findAdvSetting();
	}
}

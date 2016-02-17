package com.lczy.media.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lczy.media.entity.Area;
import com.lczy.media.repositories.AreaDao;
import com.lczy.media.service.common.AreaProvider;

/**
 * @author Wu.Yanhong
 *
 */
@Component
@Transactional(readOnly = true)
public class AreaService {
	
	public enum Level {COUNTRY, PROVINCE, CITY, COUNTY};
	
	@Autowired
	private AreaDao areaDao;
	
	public Collection<Area> findByParentId(String parentId) {

		return areaDao.findByParentId(parentId);
	}
	
	public Area get(String areaId) {
		return areaDao.findOne(areaId);
	}
	
	public Collection<Area> findByLevel(String level) {
		
		return areaDao.findByLevel(level);
	}
	
	/**
	 * 
	 * @param provinceId 表示省份的Area对象id.
	 * @return 表示省份的城市集合.
	 */
	public Collection<Area> getCities(String provinceId) {
		return findByParentId(provinceId);
	}
	
	
	@Autowired
	private AreaProvider areaProvider;
	
	public void save(Area area) {
		areaDao.save(area);
		//表的增删改必须调用此方法重新加载缓存
		areaProvider.reload();
	}
	
	public Area findByNameAndLevel(String name, String level) {
		return areaDao.findByNameAndLevel(name, level);
	}
	
	public Area findTopByName(String name) {
		return areaDao.findTopByName(name);
	}
	
	
	/**
	 * 查询所有的地区
	 * 
	 * @return
	 */
	public Collection<Area> findAll() {
		return areaDao.findAll((Specification<Area>)null);
	}
	
		
}

/**
 * 
 */
package com.lczy.media.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.lczy.common.data.Dao;
import com.lczy.media.entity.Area;

/**
 * @author wu
 *
 */
public interface AreaDao extends Dao<Area, Serializable> {
	String ORDER_BY_CLAUSE = "convert(name using gbk), seq_num";
	
	@Query(value = "select * from c_area where parent_id=?1 "
			+ ORDER_BY_CLAUSE, nativeQuery = true )
	List<Area> findByParentId(String parentId);

	@Query(value = "select * from c_area where level=?1 "
			+ ORDER_BY_CLAUSE, nativeQuery = true )
	List<Area> findByLevel(String level);
	
	Area findByNameAndLevel(String name, String level);
	
	Area findTopByName(String name);
	
}

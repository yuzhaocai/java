package com.lczy.media.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.Query;

import com.lczy.common.data.Dao;
import com.lczy.media.entity.ReqMedia;

public interface ReqMediaDao extends Dao<ReqMedia, Serializable> {
	
	/**
	 * @param rid 需求 id
	 * @param mid 媒体 id
	 */
	@Query(value = "select count(1) from ReqMedia t where t.requirement.id = ?1 and t.media.id = ?2 and t.quoteType = ?3")
	int countBy(String rid, String mid, String type);
	/**
	 * @param rid 需求 id
	 */
	@Query(value = "select count(1) from ReqMedia t where t.requirement.id = ?1")
	int countByReq(String rid);
	
	/**
	 * @param rid 需求 id
	 */
	@Query(value = "delete from ReqMedia t where t.requirement.id = ?1")
	void deleteMedia(String rid);
}

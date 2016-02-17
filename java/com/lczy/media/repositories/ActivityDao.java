package com.lczy.media.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.lczy.common.data.Dao;
import com.lczy.media.entity.Activity;

public interface ActivityDao extends Dao<Activity, Serializable> {

	/**
	 * @param id
	 * @param status
	 * @return
	 */
	@Modifying
	@Query(value = "update Activity l set l.status = ?2 where l.id = ?1")
	int disable(String id, String status);
}

package com.lczy.media.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.Query;

import com.lczy.common.data.Dao;
import com.lczy.media.entity.MediaActivity;

public interface MediaActivityDao extends Dao<MediaActivity, Serializable> {

	/**
	 * @param activityId
	 *            活动id
	 * @return
	 */
	@Query(value = "select count(1) from MediaActivity t where t.activity.id = ?1")
	int countBy(String activityId);
	
}

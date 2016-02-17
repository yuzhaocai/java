package com.lczy.media.repositories;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.lczy.common.data.Dao;
import com.lczy.media.entity.RefundLog;

public interface RefundLogDao extends Dao<RefundLog, Serializable> {

	@Modifying
	@Query("update RefundLog r set r.deleted = 1, modifyTime = ?2 where r.id = ?1")
	int setDelete(String id, Date time);

}

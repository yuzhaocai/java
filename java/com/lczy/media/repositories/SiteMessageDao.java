package com.lczy.media.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.lczy.common.data.Dao;
import com.lczy.media.entity.SiteMessage;

public interface SiteMessageDao extends Dao<SiteMessage, Serializable> {

	/**
	 * 更新消息列表为已读状态.
	 */
	@Modifying
	@Query(value="update SiteMessage set readFlag=1 where id in (?1)")
	void setReadFlag(String[] ids);

}

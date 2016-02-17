package com.lczy.media.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.Query;

import com.lczy.common.data.Dao;
import com.lczy.media.entity.OtherMedia;
import com.lczy.media.entity.OtherMediaTab;

public interface OtherMediaTabDao extends Dao<OtherMediaTab, Serializable> {

	@Query(value = "delete from OtherMediaTab t where t.media.id = ?1")
	void deleteTabs(String mediaId);

	void deleteByMedia(OtherMedia media);
}

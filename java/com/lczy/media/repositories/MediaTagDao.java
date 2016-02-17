package com.lczy.media.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.lczy.common.data.Dao;
import com.lczy.media.entity.MediaTag;

public interface MediaTagDao extends Dao<MediaTag, Serializable> {

	@Query(value = "select * from t_media_tag t "
			+ "where t.hot = 1 "
			+ "order by t.count", nativeQuery = true)
	List<MediaTag> findHotTags();

	@Query(value = "select * from t_media_tag t where t.rec = '1' order by  t.count desc ", nativeQuery = true)
	List<MediaTag> findRecTags();
	
	MediaTag findTopByName(String name);
	
	@Query(value = "select * from t_media_tag t where t.id not in (?1) and t.rec = '1' order by t.count desc", nativeQuery = true)
	List<MediaTag> findFitTags(String[] tagsName);

	@Modifying
	@Query(value = "update MediaTag t set t.count = t.count + 1 where t.id = ?1")
	int increase(String id);

	@Modifying
	@Query(value = "update MediaTag t set t.count = t.count - 1 where t.id = ?1")
	int decrease(String id);
	
	@Query(value = "select * from t_media_tag t where t.id  in (?1)", nativeQuery = true)
	List<MediaTag> findMediaTags(String[] tagsId);

}

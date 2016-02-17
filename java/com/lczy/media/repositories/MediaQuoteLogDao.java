package com.lczy.media.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.Query;

import com.lczy.common.data.Dao;
import com.lczy.media.entity.MediaQuoteLog;

public interface MediaQuoteLogDao extends Dao<MediaQuoteLog, Serializable> {

	@Query(value = "select * from t_media_quote_log where media_id=?1 and type=?2", nativeQuery = true)
	MediaQuoteLog findMediaQuoteLog(String mediaId,String type);

}

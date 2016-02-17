package com.lczy.media.repositories;

import java.io.Serializable;

import com.lczy.common.data.Dao;
import com.lczy.media.entity.Media;
import com.lczy.media.entity.MediaQuote;

public interface MediaQuoteDao extends Dao<MediaQuote, Serializable> {
	
	MediaQuote findTopByMediaAndType(Media media, String type);

}

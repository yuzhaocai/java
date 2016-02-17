package com.lczy.media.repositories;

import com.lczy.common.data.Dao;
import com.lczy.media.entity.MediaAdv;

public interface MediaAdvDao extends Dao<MediaAdv, String> {
	
	public MediaAdv findTopByType(String type);
}

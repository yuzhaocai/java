package com.lczy.media.repositories;

import java.io.Serializable;
import java.util.List;

import com.lczy.common.data.Dao;
import com.lczy.media.entity.Media;

public interface MediaDao extends Dao<Media, Serializable> {
	
	public List<Media> findByCustomerId(String customerId);
	
	public List<Media> findByOrganizationId(String organizationId);
	
	public Media findTopByNameAndMediaType(String name, String mediaType);
	
	public Media findTopByAccountAndMediaType(String account, String mediaType);
	
}

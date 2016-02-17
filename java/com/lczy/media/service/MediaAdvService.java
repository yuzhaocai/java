package com.lczy.media.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lczy.common.data.JPAUtil;
import com.lczy.media.entity.MediaAdv;
import com.lczy.media.repositories.MediaAdvDao;

@Service
public class MediaAdvService {
	
	@Autowired
	private MediaAdvDao mediaAdvDao;
	
	public List<MediaAdv> findAllByType(
			String type)
	{
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("EQ_type", type);
		Specification spec = JPAUtil.buildSpecification(searchParams);
		
		Sort sort = new Sort(Direction.ASC, "orders");
		return mediaAdvDao.findAll(spec, sort);
	}
	
	public MediaAdv findOneByType(
			String type)
	{
		return mediaAdvDao.findTopByType(type);
	}
}

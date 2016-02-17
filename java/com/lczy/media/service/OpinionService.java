package com.lczy.media.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lczy.common.service.AbstractService;
import com.lczy.media.entity.Opinion;
import com.lczy.media.repositories.OpinionDao;

@Service
@Transactional(readOnly=true)
public class OpinionService extends AbstractService<Opinion>{
	@Autowired
	private OpinionDao opinionDao;

}

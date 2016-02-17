package com.lczy.media.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lczy.common.service.AbstractService;
import com.lczy.media.entity.Blacklist;
import com.lczy.media.service.common.BlacklistProvider;


@Service
@Transactional(readOnly=true)
public class BlacklistService extends AbstractService<Blacklist> {
	
	@Autowired
	private BlacklistProvider blacklistProvider;
	
	@Transactional(readOnly=false)
	public Blacklist save(Blacklist blacklist) {
		blacklist = super.save(blacklist);
		blacklistProvider.reload();
		return blacklist;
	}

}

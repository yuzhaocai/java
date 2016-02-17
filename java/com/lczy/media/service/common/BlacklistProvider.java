package com.lczy.media.service.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.lczy.media.entity.Blacklist;
import com.lczy.media.repositories.BlacklistDao;

@Component
@Lazy
public class BlacklistProvider extends AbstractProvider {
	
	@Autowired
	private BlacklistDao blacklistDao;
	
	public BlacklistProvider() {
		Providers.add(this);
	}

	private Map<String, List<Blacklist>> cacheMap = Maps.newLinkedHashMap();

	@Override
	protected void load() {
		clear();
		Iterable<Blacklist> list = blacklistDao.findAll();
		for(Blacklist a : list) {
			if(cacheMap.get(a.getType()) == null) {
				cacheMap.put(a.getType(), new ArrayList<Blacklist>());
			}
			cacheMap.get(a.getType()).add(a);
		}
	}
	
	@Override
	public void clear() {
		cacheMap.clear();
	}
	
	public List<Blacklist> getBlacklist(String type) {
		tryLoad();
		return cacheMap.get(type);
	}
	
	/**
	 * 判断给出的条目是否在黑名单中.
	 * @param item
	 * @param type
	 */
	public boolean isBlocked(String item, String type) {
		tryLoad();
		
		List<Blacklist> list = getBlacklist(type);
		if( list != null ) {
			for( Blacklist b : list ) {
				if( b.getItem().equals(item) ) {
					return true;
				}
			}
		}
		return false;
	}
	
}

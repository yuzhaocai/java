/**
 * 
 */
package com.lczy.media.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lczy.common.service.AbstractService;
import com.lczy.media.entity.FavoritesMedia;

/**
 * @author wang.xiaoxiang
 *
 */
@Service
@Transactional(readOnly=true)
public class FavoritesMediaService extends AbstractService<FavoritesMedia> {
	
	
}

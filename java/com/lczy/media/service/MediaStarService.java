package com.lczy.media.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lczy.common.service.AbstractService;
import com.lczy.media.entity.Media;
import com.lczy.media.entity.MediaStar;

@Service
@Transactional(readOnly = true)
public class MediaStarService extends AbstractService<MediaStar> {

	@Autowired
	private MediaService mediaService;
	
	@Autowired
	private MediaQuoteLogService mediaQuoteLogService;
	
	/**
	 * 给媒体设置星级
	 * 
	 * @param id          星级id
	 * @param mediaId     媒体id
	 * @throws Exception
	 */
	@Transactional(readOnly = false)
	public void assign(String id, String mediaId) throws Exception {
		Media media = mediaService.get(mediaId);
		MediaStar mediaStar = this.get(id);
		
		// 如果星级和原来的星级相同，直接返回
		if (media.getStar().getId().equals(id)) {
			return;
		}
		
		media.setStar(mediaStar);
		mediaService.save(media);

		// 设置价格
		mediaQuoteLogService.updateStar(media, mediaStar);
	}

}

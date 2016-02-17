package com.lczy.media.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lczy.common.service.AbstractService;
import com.lczy.media.entity.Activity;
import com.lczy.media.entity.MediaActivity;
import com.lczy.media.repositories.MediaActivityDao;

@Service
@Transactional(readOnly = true)
public class MediaActivityService extends AbstractService<MediaActivity> {

	@Autowired
	public MediaActivityDao mediaActivityDao;

	@Autowired
	private MediaService mediaService;

	@Override
	public MediaActivityDao getDao() {
		return (MediaActivityDao) super.getDao();
	}
	
	@Transactional(readOnly = false)
	public List<MediaActivity> save(Activity activity, String[] mediaIds)
			throws Exception {
		List<MediaActivity> list = new ArrayList<MediaActivity>();
		for (String mediaId : mediaIds) {
			MediaActivity entity = new MediaActivity();
			entity.setActivity(activity);
			entity.setMedia(mediaService.get(mediaId));
			getDao().save(entity);
			list.add(entity);
		}
		return list;
	}

	public int getMediaNum(String activityId) {
		return getDao().countBy(activityId);
	}

}

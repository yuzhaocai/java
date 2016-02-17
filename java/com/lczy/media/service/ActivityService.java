package com.lczy.media.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lczy.common.service.AbstractService;
import com.lczy.media.entity.Activity;
import com.lczy.media.repositories.ActivityDao;
import com.lczy.media.util.Constants;
import com.lczy.media.vo.ActivityVO;

@Service
@Transactional(readOnly = true)
public class ActivityService extends AbstractService<Activity> {

	@Autowired
	private ActivityDao activityDao;

	@Override
	public ActivityDao getDao() {
		return (ActivityDao) super.getDao();
	}

	@Transactional(readOnly = false)
	public Activity save(ActivityVO vo, int mediaNum ,String activityName) throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Activity activity = new Activity();

		activity.setStartTime(df.parse(vo.getStartTime()));
		if (vo.getEndTime() != null && StringUtils.isNotBlank(vo.getEndTime())){
			int dayMis = 1000 * 60 * 60 * 24;// 一天的毫秒
			long curMillisecond = df.parse(vo.getEndTime()).getTime();// 当天的毫秒
			long resultMis = curMillisecond + (dayMis - 1); // 当天最后一秒
			 df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date resultDate = new Date(resultMis);
			activity.setEndTime(resultDate);
		}
		activity.setName(activityName);
		activity.setPercent(vo.getPercent()/100);
		activity.setCreateTime(new Date());
		activity.setStatus(Constants.ActivityStatus.ACTIVE);
		activity.setMediaNum(mediaNum);
		return getDao().save(activity);
	}

	@Transactional(readOnly = false)
	public int disable(String id, String status) throws Exception {
		return getDao().disable(id, status);
	}
}

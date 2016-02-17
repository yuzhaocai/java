package com.lczy.media.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lczy.common.service.AbstractService;
import com.lczy.media.entity.Intention;
import com.lczy.media.entity.OtherMedia;
import com.lczy.media.entity.OtherMediaTab;
import com.lczy.media.repositories.OtherMediaTabDao;
import com.lczy.media.util.UserContext;
import com.lczy.media.vo.OtherMediaVO;

/**
 * 其他类别媒体管理
 * 
 * @author wang.haibin
 *
 */
@Service
@Transactional(readOnly=true)
public class OtherMediaService extends AbstractService<OtherMedia> {
	
	@Autowired
	private OtherMediaTabDao otherMediaTabDao;
	
	@Autowired
	private IntentionService intentionService;
	
	@Transactional(readOnly=false)
	public void deleteValidate(Serializable id) throws Exception {
		Map<String, Object> searchParams = Maps.newHashMap();
 	   	searchParams.put("EQ_target.id", id);
 	   	List<Intention> intentions = intentionService.find(searchParams);
 	   	if (intentions.size() > 0) {
 	   		throw new Exception("该媒体已经被使用，不允许删除");
 	   	}
		super.remove(id);
	}
	
	/**
	 * 创建其他类型媒体：先删除已有页卡，再重建
	 * 
	 * @param media
	 * @throws Exception
	 */
	@Transactional(readOnly=false)
	public void createOtherMedia(OtherMedia media) throws Exception {
		Date now = new Date();
		media.setId(null);
		media.setCreateTime(now);
		media.setCreateBy(UserContext.getCurrent().getId());
		media.setModifyTime(now);
		media.setModifyBy(UserContext.getCurrent().getId());
		this.save(media);
		
		List<OtherMediaTab> tabs = media.getMediaTabs();
		if (tabs != null) {
			for (OtherMediaTab tab : tabs) {
				if (StringUtils.isNotBlank(tab.getTitle()) && StringUtils.isNotBlank(tab.getContent())) {
					tab.setMedia(media);
					otherMediaTabDao.save(tab);
				}
			}
		}
		
	}

	/**
	 * 更新其他类型媒体：先删除已有页卡，再重建
	 * 
	 * @param media
	 * @throws Exception
	 */
	@Transactional(readOnly=false)
	public void updateOtherMedia(OtherMediaVO media) throws Exception {
		OtherMedia old = this.get(media.getId());
		otherMediaTabDao.deleteByMedia(old);
		
		Date now = new Date();
		old.setModifyTime(now);
		old.setModifyBy(UserContext.getCurrent().getId());
		old.setName(media.getName());
		old.setCategory(media.getCategory());
		old.setIndustryType(media.getIndustryType());
		old.setRegion(media.getRegion());
		
		if (media.getShowPic() != null) {
			old.setShowPic(media.getShowPic());
		}
		if (media.getAttachment() != null) {
			old.setAttachment(media.getAttachment());
		}
		this.save(old);
		
		List<OtherMediaTab> tabs = media.getMediaTabs();
		if (tabs != null) {
			for (OtherMediaTab tab : tabs) {
				if (StringUtils.isNotBlank(tab.getTitle()) && StringUtils.isNotBlank(tab.getContent())) {
					tab.setMedia(old);
					otherMediaTabDao.save(tab);
				}
			}
		}
	}

}

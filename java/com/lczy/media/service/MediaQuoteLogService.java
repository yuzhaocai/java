package com.lczy.media.service;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lczy.common.exception.ServiceException;
import com.lczy.common.service.AbstractService;
import com.lczy.media.entity.Activity;
import com.lczy.media.entity.Media;
import com.lczy.media.entity.MediaQuote;
import com.lczy.media.entity.MediaQuoteLog;
import com.lczy.media.entity.MediaStar;
import com.lczy.media.entity.User;
import com.lczy.media.repositories.MediaQuoteLogDao;
import com.lczy.media.util.Constants;
import com.lczy.media.util.PriceAlgorithm;
import com.lczy.media.util.UserContext;

@Service
@Transactional(readOnly = true)
public class MediaQuoteLogService extends AbstractService<MediaQuoteLog>{
	
	@Autowired
	private MediaQuoteLogDao mediaQuoteLogDao;
	
	@Autowired
	private SysConfigService sysConfigService;
	
	@Autowired
	private MediaService mediaService;
	
	@Autowired
	private MediaQuoteService mediaQuoteService;
	
	@Autowired
	private UserService userService;
	
	public MediaQuoteLog findMediaQuoteLog(String mediaId,String type){
		return mediaQuoteLogDao.findMediaQuoteLog(mediaId, type);
	}
	
	/**
	 * 后台更新媒体报价，提交审核
	 * 
	 * @param mq
	 * @param price
	 * @throws Exception
	 */
	@Transactional(readOnly = false)
	public void updatePrice(MediaQuote mq, int price) throws Exception {
		Date now = new Date();
		
		MediaQuoteLog log = new MediaQuoteLog();
		log.setCreateTime(now);
		log.setModifyType(Constants.QuoteModifyType.ADMIN_QUOTE);
		String id = UserContext.getCurrent().getId();
		User user = userService.get(id);  
		log.setCreateBy(user.getLoginName());
		log.setPrice(PriceAlgorithm.regulatePrice(price));
		log.setPriceMedia(mq.getPriceMedia());
		log.setTax(mq.getTax());
		log.setStatus(Constants.QuoteLogStatus.AUDIT);
		log.setType(mq.getType());
		log.setMedia(mq.getMedia());
		
		
		this.save(log);
	}
	
	/**
	 * 更新媒体星级，无需审核
	 * 
	 * @param media
	 * @param mediaStar
	 * @throws Exception
	 */
	@Transactional(readOnly = false)
	public void updateStar(Media media, MediaStar mediaStar) throws Exception {
		if (media.isActivitying()) {
			throw new ServiceException("活动进行中，不能修改星级！");
		}
		
		Date now = new Date();
		MediaQuoteLog log = new MediaQuoteLog();
		log.setCreateTime(now);
		log.setCreateBy(UserContext.getCurrent().getId());
		log.setModifyType(Constants.QuoteModifyType.STAR);
		log.setStatus(Constants.QuoteLogStatus.PASS);
		log.setMedia(media);
		this.save(log);
		
		this.doUpdateStar(log);
	}
	
	/**
	 * 后台更新媒体报价，提交审核
	 * 
	 * @param mq
	 * @param price
	 * @throws Exception
	 */
	@Transactional(readOnly = false)
	public void updatePriceMedia(MediaQuote mq, int price,String modifyType) throws Exception {
		float taxRate = sysConfigService.getTaxRate();
		float percent = mq.getMedia().getStar().getPercent();
		Date now = new Date();
		
		MediaQuoteLog log = new MediaQuoteLog();
		log.setCreateTime(now);
		String id = UserContext.getCurrent().getId();
		User user = userService.get(id);  
		log.setCreateBy(user.getLoginName());
		log.setModifyType(modifyType);
		log.setPriceMedia(price);
		log.setTax(PriceAlgorithm.calcTax(price, mq.getMedia().isProvideInvoice(), taxRate));
		log.setPrice(PriceAlgorithm.calcPrice(price, mq.getMedia().isProvideInvoice(), percent, taxRate));
		log.setStatus(Constants.QuoteLogStatus.AUDIT);
		log.setType(mq.getType());
		log.setMedia(mq.getMedia());
		this.save(log);
	}
	
	/**
	 * 价格修改审核驳回
	 * 
	 * @param id
	 * @throws Exception
	 */
	@Transactional(readOnly = false)
	public void reject(String id) throws Exception {
		MediaQuoteLog log = this.get(id);
		Date now = new Date();
		String userid = UserContext.getCurrent().getId();
		User user = userService.get(userid);  
		log.setAuditBy(user.getLoginName());
		log.setAuditTime(now);
		log.setStatus(Constants.QuoteLogStatus.REJECT);
		this.save(log);
	}
	
	/**
	 * 价格修改审核驳回
	 * 
	 * @param id
	 * @throws Exception
	 */
	@Transactional(readOnly = false)
	public void pass(String id) throws Exception {
		MediaQuoteLog log = this.get(id);
		Date now = new Date();
		String userid = UserContext.getCurrent().getId();
		User user = userService.get(userid);  
		log.setAuditBy(user.getLoginName());
		log.setAuditTime(now);
		if (!log.getStatus().equals(Constants.QuoteLogStatus.AUDIT)) {
			throw new ServiceException("修改价格不能重复审核！");
		}
		
		Media media = log.getMedia();
		if (media.isActivitying()) {
			throw new ServiceException("活动进行中，不能修改价格！");
		}
		log.setStatus(Constants.QuoteLogStatus.PASS);
		this.save(log);
		
		// 审核通过后，修改价格
		if (log.getModifyType().equals(Constants.QuoteModifyType.INVOICE)
				|| log.getModifyType().equals(
						Constants.QuoteModifyType.ADMIN_INVOICE)) {
			// 修改是否能开发票
			doUpdateInvoice(log);
		} else {
			doUpdatePrice(log);
		}
			 
	}
	
	/**
	 * @param log
	 */
	private void doUpdatePrice(MediaQuoteLog log) {
		MediaQuote mq = mediaQuoteService.findByMediaAndType(log.getMedia(), log.getType());
		Date now = new Date();
		if (mq == null) {
			mq = new MediaQuote();
			mq.setCreateTime(now);
			mq.setCreateBy(log.getCreateBy());
		}
		mq.setModifyTime(now);
		mq.setModifyBy(log.getCreateBy());
		mq.setModifyType(log.getModifyType());
		mq.setMedia(log.getMedia());
		mq.setType(log.getType());
		mq.setPrice(log.getPrice());
		mq.setPriceMedia(log.getPriceMedia());
		mq.setPriceActivity(log.getPrice());
		mq.setTax(log.getTax());
		mediaQuoteService.save(mq);
	}
	
	/**
	 * 重新计算媒体报价
	 * 
	 * @param log
	 */
	private void doUpdateStar(MediaQuoteLog log) {
		Media media = log.getMedia();
		float taxRate = sysConfigService.getTaxRate();
		float percent = media.getStar().getPercent();
		Set<MediaQuote> list = media.getMediaQuotes();
		for (MediaQuote mq : list) {
			mq.setModifyBy(UserContext.getCurrent().getId());
			mq.setModifyTime(new Date());
			mq.setPrice(PriceAlgorithm.calcPrice(mq.getPriceMedia(), media.isProvideInvoice(), percent, taxRate));
			mq.setPriceActivity(0);
			mq.setModifyType(log.getModifyType());
			mediaQuoteService.save(mq);
		}
	}
	
	/**
	 * 媒体设置发票状态
	 * @param mediaId
	 * @param provideInvoice
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false)
	public void setInvoice(String mediaId, String provideInvoice) throws Exception {
		Media media = mediaService.get(mediaId);
		Date now = new Date();
		Map<String, Object> searchParams =Maps.newHashMap();
		searchParams.put("EQ_media.id", mediaId);
		searchParams.put("EQ_modifyType", Constants.QuoteModifyType.INVOICE);
		//最近一个月是否有修改
		Calendar calendar = Calendar.getInstance();//日历对象
		calendar.setTime(now);//设置当前日期
		calendar.add(Calendar.MONTH, -1);//月份减一
		searchParams.put("GTE_createTime", calendar.getTime());
		Collection<MediaQuoteLog> mqList= this.find(searchParams);
		if(mqList.size()==0){
			//距上次修改时间超过一个月
			//插入报价记录表等待审核
			MediaQuoteLog quoteLog = new MediaQuoteLog();
			quoteLog.setMedia(media);
			quoteLog.setCreateTime(now);
			String userid = UserContext.getCurrent().getId();
			User user = userService.get(userid);  
			quoteLog.setCreateBy(user.getLoginName());
			quoteLog.setStatus(Constants.QuoteLogStatus.AUDIT);
			quoteLog.setProvideInvoice(Boolean.valueOf(provideInvoice));
			quoteLog.setModifyType(Constants.QuoteModifyType.INVOICE);
			this.save(quoteLog);
		}else{
			throw new ServiceException("修改失败！距上次修改时间未满一个月");
		}
	}
	
	/**
	 * 管理员设置发票状态
	 * @param mediaId
	 * @param provideInvoice
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false)
	public void adminSetInvoice(String mediaId, String provideInvoice) throws Exception {
		Media media = mediaService.get(mediaId);
		Date now = new Date();
		MediaQuoteLog quoteLog = new MediaQuoteLog();
		String userid = UserContext.getCurrent().getId();
		User user = userService.get(userid);  
		quoteLog.setCreateBy(user.getLoginName());
		quoteLog.setMedia(media);
		quoteLog.setCreateTime(now);
		quoteLog.setStatus(Constants.QuoteLogStatus.AUDIT);
		quoteLog.setProvideInvoice(Boolean.valueOf(provideInvoice));
		quoteLog.setModifyType(Constants.QuoteModifyType.ADMIN_INVOICE);
		this.save(quoteLog);
	}
	
	/**
	 * 更新发票状态
	 * 
	 * @param log
	 */
	private void doUpdateInvoice(MediaQuoteLog log) {
		Media media = log.getMedia();
		media.setModifyBy(log.getCreateBy());
		media.setModifyTime(new Date());
		media.setProvideInvoice(log.isProvideInvoice());
		mediaService.save(media);
		
		float taxRate = sysConfigService.getTaxRate();
		float percent = media.getStar().getPercent();
		Set<MediaQuote> list = media.getMediaQuotes();
		for (MediaQuote mq : list) {
			mq.setModifyBy(UserContext.getCurrent().getId());
			mq.setModifyTime(new Date());
			int price = PriceAlgorithm.calcPrice(mq.getPriceMedia(), media.isProvideInvoice(), percent, taxRate);
			mq.setPrice(price);
			mq.setPriceActivity(price);
			mq.setModifyType(log.getModifyType());
			mediaQuoteService.save(mq);
		}
	}
	
	/**
	 * 参加活动
	 * 
	 * @param media
	 * @param activity
	 * @throws Exception
	 */
	@Transactional(readOnly = false)
	public void joinActivity(Media media, Activity activity) throws Exception {
		doJoinActivity(media, activity);
	}
	
	/**
	 * 参加活动，修改活动价
	 * 
	 * @param media
	 * @param activity
	 * @throws Exception
	 */
	private void doJoinActivity(Media media, Activity activity) throws Exception {
		if (media.isActivitying()) {
			throw new ServiceException("活动进行中！");
		}
		
		media.setActivity(activity);
		mediaService.save(media);
		
		float taxRate = sysConfigService.getTaxRate();
		float percent = media.getStar().getPercent();
		float discount = activity.getPercent();
		Set<MediaQuote> list = media.getMediaQuotes();
		for (MediaQuote mq : list) {
			mq.setModifyBy(UserContext.getCurrent().getId());
			mq.setModifyTime(new Date());
			int priceActivity = PriceAlgorithm.calcPriceActivity(mq.getPriceMedia(), media.isProvideInvoice(), percent, taxRate, discount);
			mq.setPriceActivity(priceActivity);
			mediaQuoteService.save(mq);
		}
	}
	
}

/**
 * 
 */
package com.lczy.media.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lczy.common.data.JPAUtil;
import com.lczy.common.service.AbstractService;
import com.lczy.media.entity.Customer;
import com.lczy.media.entity.Media;
import com.lczy.media.entity.ReqMedia;
import com.lczy.media.entity.Requirement;
import com.lczy.media.repositories.MediaDao;
import com.lczy.media.repositories.ReqMediaDao;
import com.lczy.media.solr.RequirementDoc;
import com.lczy.media.solr.RequirementSolrService;
import com.lczy.media.solr.SolrBean;
import com.lczy.media.solr.SolrService;
import com.lczy.media.util.Constants;
import com.lczy.media.util.JsonBean;
import com.lczy.media.util.UserContext;

/**
 * @author wu
 *
 */
@Service
@Transactional(readOnly=true)
public class RequirementService extends AbstractService<Requirement> {
	
	@Autowired
	private RequirementSolrService requirementSolrService;
	
	@Autowired
	private ReqMediaDao reqMediaDao;
	
	@Autowired
	private MediaDao mediaDao;
	@Autowired
	private SiteMessageService siteMessageService;
	
	/** 创建需求
	 * @param req
	 */
	@Transactional(readOnly=false)
	public void finish(Requirement req) {
		Requirement requirement = this.save(req);
		for (ReqMedia rm : requirement.getReqMedias()) {
			sendMessageFinish(rm);
		}
	}
	private void sendMessageFinish(ReqMedia rm) {
		Customer mediaOwner = rm.getMedia().getCustomer();
		Requirement req = rm.getRequirement();
		Customer owner = req.getCustomer();
		String content = MessageTemplate.get(MessageType.MEDIA_NEW_TASK.key(),
				owner.getName(), req.getName());
		siteMessageService.send(siteMessageService.getSystemSender().getId(), 
				mediaOwner.getId(), MessageType.MEDIA_NEW_TASK, content);
	}
	
	/**
	 * 需求审核 - 通过
	 * 
	 * @param id
	 */
	@Transactional(readOnly=false)
	public void auditPass(String id) {
		Requirement requirement = get(id);
		requirement.setStatus(Constants.ReqStatus.NORMAL);
		requirement.setModifyBy(UserContext.getCurrent().getId());
		requirement.setModifyTime(new Date());
		sendMessageAuditPass(requirement);
		this.save(requirement);
	}
	private void sendMessageAuditPass(Requirement requirement) {
		String content = MessageTemplate.get(MessageType.ADVERTISER_REQ_SUCCESS.key());
		siteMessageService.send(siteMessageService.getSystemSender().getId(), 
				requirement.getCustomer().getId(), MessageType.ADVERTISER_REQ_SUCCESS, content);
	}
	
	/**
	 * 需求审核 - 屏蔽,开放
	 * @param status = REQ_S_DISABLED 开放
	 * @param status = REQ_S_NORMAL 屏蔽
	 * 
	 * @param id
	 */
	@Transactional(readOnly=false)
	public void setStatus(String id,String status) {
		Requirement requirement = get(id);
		if(status.equals(Constants.ReqStatus.DISABLED)){
			requirement.setStatus(Constants.ReqStatus.AUDIT);
		}else{
			requirement.setStatus(Constants.ReqStatus.DISABLED);
		}
		requirement.setModifyBy(UserContext.getCurrent().getId());
		requirement.setModifyTime(new Date());
		sendMessageAuditReject(requirement);
		this.save(requirement);
	}
	
	private void sendMessageAuditReject(Requirement requirement) {
		String content = MessageTemplate.get(MessageType.ADVERTISER_REQ_FAILURE.key());
		siteMessageService.send(siteMessageService.getSystemSender().getId(), 
				requirement.getCustomer().getId(), MessageType.ADVERTISER_REQ_FAILURE, content);
	}
	
	/**
	 * 撰稿需求 - 处理
	 * 
	 * @param id
	 */
	@Transactional(readOnly=false)
	public void finish(String id) {
		Requirement requirement = get(id);
		requirement.setIsFinished(true);
		requirement.setModifyBy(UserContext.getCurrent().getId());
		requirement.setModifyTime(new Date());
		this.save(requirement);
	}
	
	/**
	 * 需求广场列表显示
	 * 
	 */
	public Page<Requirement> find(Map<String, Object> searchParams, int page,
			int size, String sort) {
		Pageable pageable = JPAUtil.buildPageRequest(page, size, sort);
        Specification<Requirement> spec = JPAUtil.buildSpecification(searchParams);
		return getDao().findAll(spec, pageable);
	}
	
	/**
	 * 通过reqId获取Requirement对象
	 * @param reqId
	 */
	public Requirement findOne(String reqId){
		return getDao().findOne(reqId);
	}

	@Override
	@Transactional(readOnly = false)
	public Requirement save(Requirement entity) {
		entity = super.save(entity);
		// 如果有稿且公开的需求，则保存到搜索引擎中
		if( entity.isHasArticle() && entity.getIsPublic() ) {
			saveToSolr(entity);
		}
		if (!entity.getIsPublic()) {
			deleteFromSolr(entity.getId());
		}
		return entity;
	}

	/**
	 * 保存需求到搜索引擎里.
	 * 
	 * @param entity 需求实体.
	 */
	private void saveToSolr(Requirement entity) {
		SolrBean doc = new RequirementDoc(entity);
		try {
			requirementSolrService.save(doc);
		} catch (SolrServerException e) {
			new RuntimeException(e);
		} catch (IOException e) {
			new RuntimeException(e);
		}
	}

	/**
	 * 删除需求.
	 * @param req 目标实体.
	 * @return 被删除的需求.
	 */
	@Transactional(readOnly=false)
	public Requirement delete(Requirement req) {
		req.setDeleted(true);
		//删除需求下应征应邀的媒体
		for( ReqMedia rm : req.getReqMedias() ) {
			reqMediaDao.delete(rm);
		}
		req.getReqMedias().clear();
		getDao().save(req);
		
		deleteFromSolr(req.getId());
		
		return req;
	}

	private void deleteFromSolr(String id) {
		try {
			requirementSolrService.delete(RequirementDoc.C_NAME, id);
		} catch (SolrServerException | IOException e) {
			throw new RuntimeException("删除索引发生异常", e);
		}
	}

	public com.lczy.media.solr.Page<RequirementDoc> find(
			Map<String, Object> fieldsMap, String sort, int pageNum,
			int pageSize) throws Exception {
		
		return requirementSolrService.find(RequirementDoc.class, fieldsMap, sort, pageNum, pageSize);
	}

	/**
	 * 主动应征.
	 * @param rid 目标需求id
	 * @param mid 媒体id
	 * @param type 报价类型（服务类型）
	 * @param price 报价
	 */
	@Transactional(readOnly = false)
	public JsonBean enlist(String rid, String mids, String types, String prices, String priceMedias) {
		JsonBean bean = null;
		String[] midsAr = mids.split(",");
		String[] typesAr = types.split(",");
		String[] pricesAr = prices.split(",");
		String[] priceMediasAr = priceMedias.split(",");
		
		for(int i = 0; i < midsAr.length; i++) {
			String mid = midsAr[i];
			String type = typesAr[i];
			if( isExists(rid, mid, type) ) {
				bean = new JsonBean("400", "您已经抢过此需求，请耐心等候广告主的反馈！");
				break;
			}
		}
		if( bean != null ) {
			return bean;
		}
		
		for(int i = 0; i < midsAr.length; i++) {
			String mid = midsAr[i];
			String type = typesAr[i];
			int price = Integer.parseInt(pricesAr[i]);
			int priceMedia = Integer.parseInt(priceMediasAr[i]);
			
			Requirement req = get(rid);
			Media media = mediaDao.findOne(mid);
			
			ReqMedia rm = new ReqMedia();
			rm.setRequirement(req);
			rm.setMedia(media);
			rm.setInviteType(Constants.InviteType.ACTIVE);
			rm.setQuoteType(type);
			rm.setPrice(price);
			rm.setPriceMedia(priceMedia);
			rm.setFbStatus(Constants.MediaFeedback.ACCEPT);
			rm.setFbTime(new Date());
			rm.setStatus(Constants.ReqMediaStatus.NORMAL);
			rm.setCfStatus(Constants.AdverConfirm.NULL);
			rm.setCreateTime(new Date());
			sendMessageDealAccept(rm);
			reqMediaDao.save(rm);
			
		}
		
		bean = new JsonBean("200", "抢单成功，请耐心等候广告主的反馈！");
		
		return bean;
	}
	private void sendMessageDealAccept(ReqMedia rm) {
		Requirement req = rm.getRequirement();
		Media media = rm.getMedia();
		String content = MessageTemplate.get(MessageType.ADVERTISER_INVIT_ACTIVE.key(), media.getName(),
				req.getName());
		siteMessageService.send(siteMessageService.getSystemSender().getId(), 
				req.getCustomer().getId(), MessageType.ADVERTISER_INVIT_ACTIVE, content);
	}
	
	
	public boolean isExists(String rid, String mid, String type) {
		
		return reqMediaDao.countBy(rid, mid, type) > 0;
	}
	
	
	/**
	 * 需求已选择媒体数
	 * 
	 * @param reqMediaId
	 */
	public int queryMediaNum(String reqMediaId){
		return reqMediaDao.countByReq(reqMediaId);
	}
	
	/**
	 * 删除订单内的媒体
	 * 
	 * @param reqId 
	 */
	@Transactional(readOnly=false)
	public void deleteMedia(ReqMedia rMedia){
		 reqMediaDao.delete(rMedia);
	}
	
	/**
	 * 移动端查询广告主订单
	 * 
	 * @param reqId 
	 */
	public List<Requirement> findByAdCustId(String custId){
			Map<String, Object> searchMap = Maps.newLinkedHashMap();
			
			searchMap.put("EQ_customer.id", custId);
			searchMap.put("EQ_hasArticle", true);
			searchMap.put("EQ_deleted", false);
			
			return find(searchMap, "DESC_createTime");
		}
}

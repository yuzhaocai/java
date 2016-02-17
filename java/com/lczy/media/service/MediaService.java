package com.lczy.media.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.lczy.common.data.JPAUtil;
import com.lczy.common.exception.IllegalFileTypeException;
import com.lczy.common.service.AbstractService;
import com.lczy.common.util.Files;
import com.lczy.common.util.MessageBean;
import com.lczy.media.entity.Customer;
import com.lczy.media.entity.Dic;
import com.lczy.media.entity.Media;
import com.lczy.media.entity.MediaStar;
import com.lczy.media.entity.MediaTag;
import com.lczy.media.entity.MediaTagMergeLog;
import com.lczy.media.entity.MediaWeibo;
import com.lczy.media.entity.MediaWeixin;
import com.lczy.media.repositories.MediaDao;
import com.lczy.media.repositories.MediaTagDao;
import com.lczy.media.repositories.MediaTagMergeLogDao;
import com.lczy.media.repositories.MediaWeiboDao;
import com.lczy.media.repositories.MediaWeixinDao;
import com.lczy.media.service.common.DicProvider;
import com.lczy.media.solr.MediaDoc;
import com.lczy.media.solr.MediaSolrService;
import com.lczy.media.solr.Page;
import com.lczy.media.util.Constants;
import com.lczy.media.util.FileServerUtils;
import com.lczy.media.util.UserContext;
import com.lczy.media.vo.MediaVO;

@Service
@Transactional(readOnly = true)
public class MediaService extends AbstractService<Media> {
	
	@Autowired
	private MediaWeiboDao mediaWeiboDao;
	
	@Autowired
	private MediaWeixinDao mediaWeixinDao;
	
	@Autowired
	private MediaSolrService mediaSolrService;
	
	@Autowired
	private MediaDao mediaDao;
	
	public Media getMedia(String mediaId) {
		return getDao().findOne(mediaId);
	}
	
	@Transactional(readOnly = false)
	public void delete(String mediaId) {
		getDao().delete(mediaId);
		deleteFromSolr(mediaId);
	}

	@Transactional(readOnly = false)
	public void deleteWeiXin(String mediaId){
		mediaWeixinDao.delete(mediaId);
		deleteFromSolr(mediaId);
	}
	
	@Transactional(readOnly = false)
	public void deleteWeiBo(String mediaId){
		mediaWeiboDao.delete(mediaId);
		deleteFromSolr(mediaId);
	}
	
	@Transactional(readOnly = false)
	public Media save(Media media) {
		if (media.getStar() == null){
			MediaStar star = new MediaStar();
			star.setId("6");
			media.setStar(star);
		}
		media = getDao().save(media);
		
		save2Solr(media);
		
		return media;
	}
	
	@Transactional(readOnly = false)
	public List<Media> save(List<Media> medias) {
		List<Media> list = (List<Media>)super.save(medias);
		save2Solr(list);
		
		return list;
	}

/*	@Transactional(readOnly = false)
	public MediaWeibo save(MediaWeibo weibo) throws Exception {
		MediaWeibo mediaWeibo = mediaWeiboDao.save(weibo);
		
		save2Solr(mediaWeibo);
		
		return mediaWeibo;
	}*/

	/**
	 * 保存到 solr.
	 */
	private void save2Solr(Media media) {
		MediaDoc doc = new MediaDoc(media);
		try {
			mediaSolrService.save(doc);
		} catch (SolrServerException | IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 保存到 solr.
	 */
	private void save2Solr(List<Media> medias) {
		List<MediaDoc> docs = Lists.newArrayList();
		try {
			for( Media m : medias ) {
				docs.add(new MediaDoc(m));
			}
			mediaSolrService.save(docs);
		} catch (SolrServerException | IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 从 solr 中删除索引.
	 */
	private void deleteFromSolr(String mediaId) {
		try {
			mediaSolrService.delete(MediaDoc.C_NAME, mediaId);
		} catch (SolrServerException | IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Page<MediaDoc> find(Map<String, Object> fieldsMap, String sort, 
			int pageNum, int size) throws Exception {
		return mediaSolrService.find(MediaDoc.class, fieldsMap, sort, pageNum, size);
	}
	
	@Transactional(readOnly = false)
	public MediaWeixin save(MediaWeixin weixin) 
	{
		if (weixin.getStar() == null){
			MediaStar star = new MediaStar();
			star.setId("6");
			weixin.setStar(star);
		}
		MediaWeixin mediaWeixin = mediaWeixinDao.save(weixin);
		save2Solr(mediaWeixin);
		
		return mediaWeixin;
	}
	
	
//	public MediaWeixin getWeixin(
//			String mediaId)
//	{
//		return mediaWeixinDao.findOne(mediaId);
//	}
	
	public Media findOneMedia(String mediaId){
		
		return getDao().findOne(mediaId);
	}
	
	
	/**
	 * 媒体审核 - 通过
	 * 
	 * @param id
	 */
	@Transactional(readOnly=false)
	public void auditPass(String id) {
		Media media = getDao().findOne(id);
		media.setStatus(Constants.MediaStatus.NORMAL);
		media.setModifyBy(UserContext.getCurrent().getId());
		media.setModifyTime(new Date());
		save(media);
	}
	
	/**
	 * 媒体审核
	 * 
	 * @param id
	 * @param status = MEDIA_S_DISABLED 开放
	 * @param status = MEDIA_S_NORMAL 屏蔽
	 * 
	 */
	@Transactional(readOnly=false)
	public void auditReject(String id,String status) {
		Media media = getDao().findOne(id);
		if(status.equals(Constants.MediaStatus.DISABLED)){
			media.setStatus(Constants.MediaStatus.AUDIT);
		}else{
			media.setStatus(Constants.MediaStatus.DISABLED);
		}
		media.setModifyBy(UserContext.getCurrent().getId());
		media.setModifyTime(new Date());
		save(media);
	}
	
	
	/**
	 * 设置媒体级别
	 * 
	 * @param id
	 * @param level
	 */
	@Transactional(readOnly=false)
	public void assignLevel(String id, String level) {
		Media media = getDao().findOne(id);
		media.setLevel(level);
		media.setModifyBy(UserContext.getCurrent().getId());
		media.setModifyTime(new Date());
		save(media);
	}
	
	/**
	 * 设置媒体星级
	 * 
	 * @param id
	 * @param star
	 */
	@Transactional(readOnly=false)
	public void assignStar(String id, MediaStar star) {
		Media media = getDao().findOne(id);
		media.setStar(star);
		media.setModifyBy(UserContext.getCurrent().getId());
		media.setModifyTime(new Date());
		save(media);
	}
	
	@Autowired
	private MediaTagDao mediaTagDao;
	
	@Autowired
	private MediaTagMergeLogDao mediaTagMergeLogDao;
	
	/**
	 * 媒体标签 - 合并:
	 * 1,增加主词引用次数
	 * 2,删除副词
	 * 3,媒体对副词的引用改为主词
	 * 4,插入合并日志
	 * 
	 * @param masterId
	 * @param slaveId
	 */
	@Transactional(readOnly = false)
	public void merge(String masterId, String slaveId) {
		MediaTag master = mediaTagDao.findOne(masterId);
		MediaTag slave = mediaTagDao.findOne(slaveId);

		String userId = UserContext.getCurrent().getId();
		Date now = new Date();
		
		// 添加合并日志
		MediaTagMergeLog log= new MediaTagMergeLog();
		log.setMasterName(master.getName());
		log.setMasterCount(master.getCount());
		log.setSlaveName(slave.getName());
		log.setSlaveCount(slave.getCount());
		log.setCreateBy(userId);
		log.setCreateTime(now);
		mediaTagMergeLogDao.save(log);
		
		// 增加主词引用次数
		master.setCount(master.getCount() + slave.getCount());
		master.setModifyBy(userId);
		master.setModifyTime(now);
		mediaTagDao.save(slave);
		
		// 删除副词
		mediaTagDao.delete(slave);
		
		removeTagFromMedia(slaveId, masterId);
	}
	
	/**
	 * 删除媒体中的标签.
	 * 
	 * @param medias 需要删除标签的媒体列表
	 * @param tagId 需要删除的标签ID
	 * @param masterId 需要替换的主词ID
	 */
	private void removeTagFromMedia(String tagId, String masterId) {
		// 查询引用标签的媒体
		Map<String, Object> params = new HashMap<>();
		params.put("LIKE_tags", tagId);
		Collection<Media> list = find(params);
		
		// 删除媒体引用的标签
		List<Media> medias = Lists.newArrayList();
		for (Media media : list) {
			String[] tags = media.getTags().split(",");
			tags = (String[]) ArrayUtils.removeElement(tags, tagId);

			if (masterId != null && !media.getTags().contains(masterId)) {
				tags = (String[]) ArrayUtils.add(tags, masterId);
			}
			media.setTags(StringUtils.join(tags, ","));
			medias.add(media);
		}

		if( !medias.isEmpty() ) {
			save(medias);//保存变更的媒体
		}
	}
	
	/**
	 * 媒体标签 - 创建
	 * 
	 * @param tag
	 */
	@Transactional(readOnly = false)
	public void saveMediaTag(MediaTag tag) {
		tag.setCreateBy(UserContext.getCurrent().getId());
		tag.setCreateTime(new Date());
		if(null==tag.getCount()){
			tag.setCount(0);
		}
		mediaTagDao.save(tag);
	}
	
	/**
	 * 媒体标签 - 删除
	 * 同时删除媒体对该标签的引用
	 * 
	 * @param tag
	 */
	@Transactional(readOnly = false)
	public void deleteMediaTag(String id) {
		//MediaTag tag = mediaTagDao.findOne(id);
		mediaTagDao.delete(id);
		
		removeTagFromMedia(id, null);
	}
	
	/**
	 * 媒体 - 删除媒体上的标签
	 * 1,删除媒体的标签
	 * 2,媒体标签的引用减一
	 * 
	 * @param media
	 * @param tag
	 */
	@Transactional(readOnly = false)
	public void deleteTag(Media media, MediaTag tag) {
		List<String> tags = Arrays.asList(media.getTags().split(","));
		tags.remove(tag.getId());
		media.setTags(StringUtils.join(tags, ","));
		
		save(media);
		
		tag.setCount(tag.getCount() - 1);
		mediaTagDao.save(tag);
	}
	
	
	
	@Autowired
	private CustomerService customerService;

	@Autowired
	private DicProvider dicProvider;

	@Autowired
	private MediaTagService mediaTagService;

	/**
	 * @param 页面显示标签
	 */
	public List<MediaTag> getNewTags(String[] tagsId) {
		List<MediaTag> tagList = new ArrayList<MediaTag>();
		String[] newTagsId = null;
		List<MediaTag> tList = mediaTagService.findFitTags(tagsId);
		String[] temp = null;
		int size = tList.size();
		temp = new String[size];
		for (int i = 0; i < size; i++)
			temp[i] = tList.get(i).getId();
		newTagsId = (String[]) ArrayUtils.addAll(tagsId, temp);

		for (String mediaTagId : newTagsId) {
			MediaTag tep = mediaTagService.get(mediaTagId);
			tagList.add(tep);
		}
		return tagList;
	}

	/**
	 * 新增媒体前准备页面元素
	 * 
	 * @param model
	 */
	public void setAttrsForModel(Model model) {
		String[] dics = new String[] { Constants.WeixinCategory.DIC_CODE,
				Constants.WeiboCategory.DIC_CODE,
				Constants.IndustryType.DIC_CODE, Constants.MediaArea.DIC_CODE,
				Constants.WeixinFans.DIC_CODE, Constants.WeiboFans.DIC_CODE,
				Constants.WeixinService.DIC_CODE,
				Constants.WeiboService.DIC_CODE,
				Constants.WeixinFitProduct.DIC_CODE };

		for (int i = 0; i < dics.length; i++) {
			Dic temp = dicProvider.getDicMap().get(dics[i]);
			AddAttribute(i, model, temp);
		}
		// 监管机构
//		model.addAttribute("orgs",
//				customerService.finaAllOrg(Constants.CustType.CUST_ORG));
		// 查找推荐标签
		model.addAttribute("tagList", mediaTagService.findRecTags());

	}

	private void AddAttribute(int i, Model model, Dic data) {
		// 类别
		if (i == 0)
			model.addAttribute("weixinCategory", data);
		if (i == 1)
			model.addAttribute("weiboCategory", data);
		// 行业类型
		if (i == 2)
			model.addAttribute("industryType", data);
		// 地区
		if (i == 3)
			model.addAttribute("mediaArea", data);
		// 粉丝方向
		if (i == 4)
			model.addAttribute("weixinFans", data);
		if (i == 5)
			model.addAttribute("weiboFans", data);
		// 服务类别报价
		if (i == 6)
			model.addAttribute("weixinService", data);
		if (i == 7)
			model.addAttribute("weiboService", data);
		// 适合产品
		if (i == 8)
			model.addAttribute("weixinFitProduct", data);
	}

	/**
	 * 为实体设置属性值
	 * 
	 * @param media
	 * @param mediaVO
	 * @param mediaType
	 * @param now
	 */
	public void setMediaValue(Media media, MediaVO mediaVO, Media mediaType,
			Date now) {

		Customer customer = UserContext.getCurrent().getCustomer();
		String userId = UserContext.getCurrent().getId();
		String showPicUrl = null;// 媒体展示图片
		MultipartFile img = mediaVO.getShowPicFile();
		if (img.getSize() > 0)
			showPicUrl = uploadFile(img);

		img = mediaVO.getQrCodeFile();
		String qrCode = null;// 二维码
		if (img.getSize() > 0)
			qrCode = uploadFile(img);

		mediaType.setDescription(media.getDescription());
		mediaType.setShowPic(showPicUrl);
		mediaType.setQrCode(qrCode);
		mediaType.setMediaType(media.getMediaType());
		mediaType.setCategory(media.getCategory());
		mediaType.setIndustryType(media.getIndustryType());
		mediaType.setRegion(media.getRegion());
		mediaType.setProducts(media.getProducts());
		mediaType.setTags(media.getTags());
		mediaType.setFans(media.getFans());
		mediaType.setFansDir(media.getFansDir());
		if (media.getId() != null && media.getId().trim().length() > 0) {
			mediaType.setModifyTime(now);
			mediaType.setModifyBy(userId);
			mediaType.setCreateTime(get(media.getId()).getCreateTime());
			mediaType.setCreateBy(get(media.getId()).getCreateBy());
		} else {
			mediaType.setCreateTime(now);
			mediaType.setCreateBy(userId);
			mediaType.setModifyTime(now);
			mediaType.setModifyBy(userId);
		}

		mediaType.setCustomer(customer);
	}

	/**
	 * 媒体新增或修改时对标签的处理
	 * 
	 * @param media
	 * @param userId
	 * @param now
	 * @param media2
	 */
	@Transactional(readOnly = false)
	public void dealTags(Media media, String userId, Date now, Media media2) {
		String[] tagNames = media.getTags().split(",");
		String[] tags = null;
		if (media != null && media.getId() != null
				&& media.getId().trim().length() > 0)
			tags = this.get(media.getId()).getTags().split(",");
		dealTags(tagNames, userId, now, media2, tags);
	}

	/**
	 * 标签处理
	 * 
	 * @param tagNames
	 *            前端传入标签
	 * @param userId
	 * @param now
	 * @param media
	 * @param tags
	 *            数据库媒体已有标签
	 */
	private void dealTags(String[] tagNames, String userId, Date now,
			Media media, String[] tags) {
		for (String tagName : tagNames) {
			if (mediaTagService.findByTagName(tagName) == null) {
				addMediaTag(tagName, userId, now);
			} else {
				if (media.getId() == null || media.getId().trim().length() < 1)
					updateMediaTag(tagName, userId, now);
				else {
					boolean contains = Arrays.asList(tags).contains(tagName);
					if (!contains)
						updateMediaTag(tagName, userId, now);
				}
			}
		}
	}

	/**
	 * 新增或修改媒体时，新标签入库
	 * 
	 * @param tagName
	 * @param userId
	 * @param now
	 */
	private void addMediaTag(String tagName, String userId, Date now) {
		MediaTag tag = new MediaTag();
		tag.setName(tagName);
		tag.setHot(false);
		tag.setCount(1);
		tag.setCreateBy(userId);
		tag.setCreateTime(now);
		tag.setModifyBy(userId);
		tag.setModifyTime(now);
		mediaTagService.save(tag);
	}

	/**
	 * 新增或修改媒体时，已有标签更改
	 * 
	 * @param tagName
	 * @param userId
	 * @param now
	 */
	private void updateMediaTag(String tagName, String userId, Date now) {
		MediaTag tag = mediaTagService.findByTagName(tagName);
		tag.setCount(tag.getCount() + 1);
		tag.setModifyBy(userId);
		tag.setModifyTime(now);
		mediaTagService.save(tag);
	}

	/**
	 * 把文件上传到文件服务器上.
	 */
	private String uploadFile(MultipartFile mpf) {
		if (mpf != null && !mpf.isEmpty()) {
			String filename = mpf.getOriginalFilename();
			String filetype = filename.substring(filename.lastIndexOf(".") + 1,
					filename.length());
			File temp = null;
			try {
				temp = File.createTempFile("req", "tmp");
				Files.write(temp, mpf.getInputStream());
				String fid = FileServerUtils.upload(null, filename, temp,
						false, filetype, false);

				return fid;
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				if (temp != null)
					temp.delete();
			}
		} else {
			return null;
		}
	}

	/**
	 * 检查当前用户是否有权限操作实体.
	 * 
	 * @param media
	 *            目标实体
	 * @return 有权限则返回 null，无权限则返回 MessageBean对象.
	 */
	public MessageBean checkPermission(Media media) {
		MessageBean bean = null;
		if (media == null) {
			bean = new MessageBean(0, "媒体不存在！");
		} else if (!hasPermission(media)) {
			bean = new MessageBean(0, "您无权操作此媒体：" + media.getId());
		}

		return bean;
	}

	/**
	 * @return 是否有权限操作此媒体.
	 */
	private boolean hasPermission(Media media) {

		return media.getCustomer().getId()
				.equals(UserContext.getCurrentCustomer().getId());
	}

	@Transactional(readOnly = false)
	public MediaWeibo save(MediaWeibo weibo, MediaVO vo) {
		//处理关联的标签
		dealTags(weibo, vo);
		setMediaValue(weibo, vo);
		return (MediaWeibo) save(weibo);
	}
	
	@Transactional(readOnly = false)
	public MediaWeixin save(MediaWeixin weixin, MediaVO vo) {
		//处理关联的标签
		dealTags(weixin, vo);
		setMediaValue(weixin, vo);
		return (MediaWeixin) save(weixin);
	}

	/**
	 * 处理媒体标签的变更情况.
	 * @param media 媒体实体对象.
	 * @param vo 前台传递的值对象.
	 */
	private void dealTags(Media media, MediaVO vo) {
		List<String> newTags = Arrays.asList(vo.getTags().split(",")); //前台页面选中的标签
		List<String> addTags = Lists.newArrayList(); //新选中的标签
		List<String> remTags = Lists.newArrayList(); //去除选中的标签
		
		if( StringUtils.isNotEmpty(media.getTags()) ) { //编辑媒体
			List<String> oldTags = Arrays.asList(media.getTags().split(","));
			for( String tid : newTags ) {
				if( !oldTags.contains(tid) ) {
					addTags.add(tid);
				}
			}
			for( String tid : oldTags ) {
				if( !newTags.contains(tid) ) {
					remTags.add(tid);
				}
			}
		} else { //新增媒体
			addTags = newTags;
		}
		
		for( String tid : addTags ) {
			mediaTagService.increase(tid);
		}
		
		for( String tid : remTags ) {
			mediaTagService.decrease(tid);
		}
	}
	
	/**
	 * 设置媒体的公共属性值
	 * 
	 * @param media
	 * @param mediaVO
	 */
	public void setMediaValue(Media media, MediaVO vo) {
		
		Date now = new Date();
		String userId = UserContext.getCurrent().getId();
		
		media.setName(vo.getName());
		media.setDescription(vo.getDescription());
		media.setMediaType(vo.getMediaType());
		media.setCategory(vo.getCategory());
		media.setIndustryType(vo.getIndustryType());
		media.setRegion(vo.getRegion());
		media.setProducts(vo.getProducts());
		media.setTags(vo.getTags());
		media.setFans(vo.getFans());
		media.setFansDir(vo.getFansDir());
		media.setAccount(vo.getAccount());
		
		if ( StringUtils.isNotBlank(media.getId() ) ) { //编辑
			media.setModifyTime(now);
			media.setModifyBy(userId);
		} else { //新增
			media.setCustomer(UserContext.getCurrentCustomer());
			media.setStatus(Constants.MediaStatus.AUDIT);
			media.setLevel(Constants.MediaLevel.UNLEVELED);
			media.setCreateTime(now);
			media.setCreateBy(userId);
			media.setModifyTime(now);
			media.setModifyBy(userId);
		}
	}
	
	/**
	 * 媒体信息显示标签
	 * 
	 * @param tagsId  标签数组
	 * @return  tags 媒体便签.
	 */
	public String getMediaTags(String[] tagsId) {
		String tags = "";
		List<MediaTag>mediaTags =  mediaTagService.findMediaTags(tagsId);
		for (MediaTag tag : mediaTags) {
			tags+=tag.getName()+"、";
		}
		return tags.substring(0, tags.length()-1);
	}
	
	/**
	 * 检查微博昵称是否重复
	 * 
	 * @param mediaType
	 * @param name
	 * @param category
	 * @return
	 */
	public int weiboCountBy(String mediaType, String name) {
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("EQ_mediaType", mediaType);
		searchParams.put("EQ_name", name);
		Specification<Media> spec = JPAUtil.buildSpecification(searchParams);
		return (int)getDao().count(spec);
	}
	
	/**
	 * 检查微信号是否重复
	 * 
	 * @param mediaType
	 * @param account
	 * @return
	 */
	public int countBy(String mediaType, String account) {
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("EQ_mediaType", mediaType);
		searchParams.put("EQ_account", account);
		Specification<Media> spec = JPAUtil.buildSpecification(searchParams);
		return (int)getDao().count(spec);
	}
	
	/**根据机构ID查询下属媒体
	 * @param organizationId
	 * @return
	 */
	public List<Media> subMedia(String organizationId){
		return mediaDao.findByOrganizationId(organizationId);
	}
	
	/**
	 * 允许上传LOGO,二维码,粉丝数截图类型.
	 */
	public static final List<String> ALLOWED_FILE_TYPES = Lists.newArrayList("jpg", "png", "bmp");
	
	
	/**
	 * 保存LOGO
	 * @throws IOException 
	 * @throws IllegalFileTypeException
	 */
	public void saveLogoImg(MediaVO vo, Media media) throws IllegalFileTypeException, IOException {
		String fid = uploadFile(vo.getShowPicFile(), media.getShowPic(), ALLOWED_FILE_TYPES);
		if( fid != null ) {
			media.setShowPic(fid);
		}
	}
	
	/**
	 * 保存二维码
	 * @throws IOException 
	 * @throws IllegalFileTypeException
	 */
	public void saveQrCodeImg(MediaVO vo, Media media) throws IllegalFileTypeException, IOException {
		String fid = uploadFile(vo.getQrCodeFile(), media.getQrCode(), ALLOWED_FILE_TYPES);
		if( fid != null ) {
			media.setQrCode(fid);
		}
	}
	
	
	/**
	 * 保存粉丝数截图
	 * @throws IOException 
	 * @throws IllegalFileTypeException
	 */
	public void saveFansNumImg(MediaVO vo, Media media) throws IllegalFileTypeException, IOException {
		String fid = uploadFile(vo.getFansNumFile(), media.getFansNumPic(), ALLOWED_FILE_TYPES);
		if( fid != null ) {
			media.setFansNumPic(fid);
		}
	}
	
	
	/**
	 * 把文件上传到文件服务器上.
	 * @throws IllegalFileTypeException 
	 * @throws IOException 
	 */
	private String uploadFile(MultipartFile mpf, String fid, List<String> allowedList) throws IllegalFileTypeException, IOException {
		if( mpf != null && !mpf.isEmpty() ) {
			File file = null;
			try {
				String filename = mpf.getOriginalFilename();
				file = File.createTempFile("media", Files.getExtension(filename));
				Files.write(file, mpf.getInputStream());
				
				String fileType = Files.getExtension(file);
				if( fileType == null || !isAllowedFileType(fileType, allowedList) ) {
					throw new IllegalFileTypeException(fileType, allowedFileTypes(allowedList));
				}
				
				fid = FileServerUtils.upload(fid, filename, file, false, fileType, false);
				return fid;
			} finally {
				if( file != null )
					file.delete();
			}
		} else {
			return null;
		}
	}

	private String allowedFileTypes(List<String> allowedList) {
		return StringUtils.join(allowedList, ", ");
	}

	private boolean isAllowedFileType(String fileType, List<String> allowedList) {
		return allowedList.contains(fileType.toLowerCase());
	}
	
}

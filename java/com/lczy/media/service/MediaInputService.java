package com.lczy.media.service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lczy.common.service.AbstractService;
import com.lczy.media.entity.Media;
import com.lczy.media.entity.MediaCase;
import com.lczy.media.entity.MediaInput;
import com.lczy.media.entity.MediaQuote;
import com.lczy.media.entity.MediaWeibo;
import com.lczy.media.entity.MediaWeixin;
import com.lczy.media.util.Constants;
import com.lczy.media.util.UserContext;
import com.lczy.media.vo.CustomerVO;

/**
 * 媒体录入
 * 
 * @author wang.haibin
 *
 */
@Service
@Transactional(readOnly=true)
public class MediaInputService extends AbstractService<MediaInput> {
	
	@Autowired
	private CustomerService customerService;

	// 默认密码
	private String DEFAULT_PASSWORD = "12345678";
	
	/**
	 * 审核媒体
	 * 
	 * @param id
	 * @throws Exception
	 */
	@Transactional(readOnly=false)
	public void audit(String id) throws Exception {
		MediaInput input = this.get(id);
		Date now = new Date();
		
		CustomerVO customerVO = new CustomerVO();
		customerVO.setName(input.getName());
		customerVO.setMobPhone(StringUtils.isBlank(input.getMobPhone()) ? this.getMobPhone() : input.getMobPhone());
		customerVO.setLoginName(customerVO.getMobPhone());
		customerVO.setPassword(StringUtils.isBlank(input.getPassword()) ? DEFAULT_PASSWORD : input.getPassword());		

		Media media = this.getMedia(input.getMediaType());
		if (media instanceof MediaWeixin) {
			media.setAccount(input.getAccount());
		}
		media.setName(input.getName());
		media.setRegion(input.getRegion());
		media.setCategory(input.getCategory());
		media.setIndustryType(input.getIndustryType());
		media.setDescription(input.getDescription());
		media.setFans(input.getFans());
		media.setFansDir(input.getFansDir());
		media.setProducts(input.getProducts());
		media.setShowPic(input.getShowPic());
		
		setMediaQuotes(input, now, media);
		setMediaCases(input, now, media);
		
		customerService.importMedia(media, customerVO);
		
		// 删除该记录
		this.remove(id);
	}

	/**
	 * 创建案例实体列表
	 * 
	 * @param input
	 * @param createTime
	 * @param media
	 */
	private void setMediaCases(MediaInput input, Date createTime, Media media) {
		Set<MediaCase> cases = new HashSet<MediaCase>();
		
		String title = input.getCaseTitle();
		String light = input.getCaseLight();
		String content = input.getCaseContent();
		if (StringUtils.isNotBlank(title)) {
			MediaCase mc = buildQuoteCase(title, light, content, createTime);
			mc.setMedia(media);
			cases.add(mc);
		}

		title = input.getCaseTitle1();
		light = input.getCaseLight1();
		content = input.getCaseContent1();
		if (StringUtils.isNotBlank(title)) {
			MediaCase mc = buildQuoteCase(title, light, content, createTime);
			mc.setMedia(media);
			cases.add(mc);
		}

		media.setMediaCases(cases);
	}

	/**
	 * 创建案例实体
	 * 
	 * @param title
	 * @param light
	 * @param content
	 * @param createTime
	 * @return
	 */
	private MediaCase buildQuoteCase(String title, String light, String content, Date createTime) {
		MediaCase mc = new MediaCase();
		mc.setTitle(title);
		mc.setLight(light);
		mc.setContent(content);
		mc.setCreateBy(UserContext.getCurrent().getId());
		mc.setModifyBy(UserContext.getCurrent().getId());
		mc.setCreateTime(createTime);
		mc.setModifyTime(createTime);
		return mc;
	}

	/**
	 * 创建报价实体列表
	 * 
	 * @param input
	 * @param createTime
	 * @param media
	 */
	private void setMediaQuotes(MediaInput input, Date createTime, Media media) {
		Set<MediaQuote> qs = new HashSet<>();
		if (StringUtils.isNotBlank(input.getQuoteType())) {
			MediaQuote mq = buildMediaQuote(input.getQuoteType(), input.getQuotePrice(), createTime);
			mq.setMedia(media);
			qs.add(mq);
		}
		
		if (StringUtils.isNotBlank(input.getQuoteType1())) {
			MediaQuote mq = buildMediaQuote(input.getQuoteType1(), input.getQuotePrice1(), createTime);
			mq.setMedia(media);
			qs.add(mq);
		}
	
		if (StringUtils.isNotBlank(input.getQuoteType2())) {
			MediaQuote mq = buildMediaQuote(input.getQuoteType2(), input.getQuotePrice2(), createTime);
			mq.setMedia(media);
			qs.add(mq);
		}
	
		if (StringUtils.isNotBlank(input.getQuoteType3())) {
			MediaQuote mq = buildMediaQuote(input.getQuoteType3(), input.getQuotePrice3(), createTime);
			mq.setMedia(media);
			qs.add(mq);
		}
	
		if (StringUtils.isNotBlank(input.getQuoteType4())) {
			MediaQuote mq = buildMediaQuote(input.getQuoteType4(), input.getQuotePrice4(), createTime);
			mq.setMedia(media);
			qs.add(mq);
		}
	
		media.setMediaQuotes(qs);
	}

	/**
	 * 创建报价对象
	 * 
	 * @param type
	 * @param price
	 * @param createTime
	 * @return
	 */
	private MediaQuote buildMediaQuote(String type, Integer price, Date createTime) {
		MediaQuote mq = new MediaQuote();
		mq.setType(type);
		mq.setPrice(price);
		mq.setCreateTime(createTime);
		mq.setModifyTime(createTime);
		mq.setCreateBy(UserContext.getCurrent().getId());
		mq.setModifyBy(UserContext.getCurrent().getId());
		return mq;
	}
	
	
	/**
	 * 根据媒体大类创建媒体实体
	 * 
	 * @param type
	 * @return
	 */
	private Media getMedia(String type) {
		if (type.contains(Constants.MediaType.WEIBO)) {
			MediaWeibo media = new MediaWeibo();
			media.setMediaType(Constants.MediaType.WEIBO);
			media.setWeiboPlatform("SINA");
			return media;
		} else {
			MediaWeixin media = new MediaWeixin();
			media.setMediaType(Constants.MediaType.WEIXIN);
			media.setWeixinCode("TENCENT");
			return media;
		}
	}

	/**
	 * 返回生成11数字当做电话号码
	 * 
	 * @return
	 */
	private String getMobPhone() {
		return String.valueOf(System.currentTimeMillis()/100);
	}
}

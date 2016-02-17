/**
 * 
 */
package com.lczy.media.controller.member;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.lczy.common.exception.FileUploadException;
import com.lczy.common.exception.IllegalFileTypeException;
import com.lczy.common.util.BeanMapper;
import com.lczy.common.web.Token;
import com.lczy.common.web.Token.Type;
import com.lczy.common.web.TokenInterceptor;
import com.lczy.media.entity.Area;
import com.lczy.media.entity.DicItem;
import com.lczy.media.entity.Media;
import com.lczy.media.entity.MediaQuote;
import com.lczy.media.entity.ReqMedia;
import com.lczy.media.entity.Requirement;
import com.lczy.media.service.MediaQuoteService;
import com.lczy.media.service.MediaService;
import com.lczy.media.service.common.AreaProvider;
import com.lczy.media.util.Constants;
import com.lczy.media.util.Constants.AdverConfirm;
import com.lczy.media.util.Constants.IndustryType;
import com.lczy.media.util.Constants.InviteType;
import com.lczy.media.util.Constants.MediaFeedback;
import com.lczy.media.util.Constants.MediaType;
import com.lczy.media.util.Constants.ReqStatus;
import com.lczy.media.util.UserContext;
import com.lczy.media.vo.MediaQuoteVo;
import com.lczy.media.vo.RequirementVO;

/**
 * 广告主创建需求控制器.
 * @author wu
 *
 */
@Controller
@RequestMapping("/member/req/advertiser/create")
public class ReqCreateController extends ReqAdvertiserController {
	
	private static final Logger log = LoggerFactory.getLogger(ReqCreateController.class);
	
	protected static final String REQ_SESSION_KEY = "com.lczy.RequirementVO";

	private static final String MEDIAS_ARRAY = "mediasArray"; 
	@Autowired
	private MediaService mediaService;
	
	@Autowired
	private MediaQuoteService mediaQuoteService;
	
	public static class NaviBar {
		private static Map<String, String> mediaTypeNaviNameMap = Maps.newHashMap();
		static {
			mediaTypeNaviNameMap.put(Constants.MediaType.WEIXIN, "选择微信媒体");
			mediaTypeNaviNameMap.put(Constants.MediaType.WEIBO,  "选择微博媒体");
		}
		private String name;
		private String code;
		private String status;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public NaviBar(String code, String name) {
			this(code, name, null);
		}
		public NaviBar(String code, String name, String status) {
			this.code = code;
			this.status = status;
			if( name == null ) {
				String m = mediaTypeNaviNameMap.get(code);
				this.name = (m == null) ? code : m;
			} else {
				this.name = name;
			}
		}
	}
	
	/**
	 * 创建需求：填写预算
	 */
	@RequestMapping(value="start")
	public String start(Model model, HttpServletRequest request) {
		
		setAttribute4Create(model);
		
		return "member/req/advertiser/create/start";
	}
	@RequestMapping(value="detailCreate")
	public String detailCreate(Model model, RequirementVO vo, HttpServletRequest request, RedirectAttributes redirectAttrs) {
		String mediaId = (String) request.getParameter("medias");
		String mediaQuoteId = (String) request.getParameter("mediaQuote");
		Media media =  mediaService.get(mediaId);
		setVo(media, vo, mediaQuoteId);
		redirectAttrs.addFlashAttribute("req", vo);
		return "redirect:/member/req/advertiser/create/fillInReq";
	}
	private void setVo(Media media, RequirementVO vo, String mediaQuoteId){
		vo.setMediaTypes(new String[]{media.getMediaType()});
		String inviteNum = dicProvider.getDic(Constants.InviteNum.DIC_CODE).getDicItems().get(0).getItemCode();
		vo.setInviteNum(inviteNum);
		vo.setRegions(new String[]{media.getRegion()});
		vo.setIndustryTypes(media.getIndustryType().split(","));
		MediaQuote mediaQuote = null;
		for (MediaQuote quote : media.getMediaQuotes()) {
			if (mediaQuoteId.equals(quote.getId())) {
				mediaQuote = quote;
				break;
			}
		}
		if (mediaQuote != null) {
			vo.setBudget(mediaQuote.getPrice());
			vo.getQuotes().add(mediaQuote);
			vo.setServiceTypes(new String[]{mediaQuote.getType()});
		}
	}
	
	@RequestMapping(value="next")
	public String next(Model model, RequirementVO vo, RedirectAttributes redirectAttrs) {
		if( vo.isHasArticle() ) {
			if( StringUtils.isNotBlank(vo.getCurrentMediaType()) ) {
				String mediaType = vo.getCurrentMediaType();
				vo.getDoneMediaTypes().add(mediaType);
				if( vo.getDoneMediaTypes().size() == vo.getMediaTypes().length ) {
					//所有的媒体类型都已经选择完媒体，跳到填写需求详情页面
					vo.setServiceTypes(getServiceTypes(vo).split(SEPARATOR));//当前需求的服务类别
					//查出所选媒体信息用于页面查看弹出框
					for(MediaQuote mq: vo.getQuotes()){
						 Media media = mediaService.get(mq.getMedia().getId());
						 mq.setMedia(media);
						 mq.getPrice();
					}
					redirectAttrs.addFlashAttribute("req", vo);
					//inviteMedias  用于查看当前需求选择的媒体信息
					return "redirect:/member/req/advertiser/create/fillInReq";
				} else {
					//计算下一个要选取的媒体类型
					String nextMediaType = nextMediaType(vo);
					vo.setCurrentMediaType(nextMediaType);
				}
				
				return nextAction(model, vo);
				
			} else { //第一次进入此方法
				String mediaType = vo.getMediaTypes()[0];
				vo.setCurrentMediaType(mediaType);
				
				return nextAction(model, vo);
			}
		} else {
			redirectAttrs.addFlashAttribute("req", vo);
			return "redirect:/member/req/advertiser/create/noArticle";
		}
	}
	
	
	/**
	 * 计算下一个要选取的媒体类型
	 */
	private String nextMediaType(RequirementVO vo) {
		String nextMediaType = null;
		for( String type : vo.getMediaTypes() ) {
			if( !vo.getDoneMediaTypes().contains(type) ) {
				nextMediaType = type;
				break;
			}
		}
		return nextMediaType;
	}
	
	/**
	 * 下一步动作.
	 */
	private String nextAction(Model model, RequirementVO vo) {
		String mediaType = vo.getCurrentMediaType();
		if( Constants.MediaType.WEIXIN.equals(mediaType) )
			return recommendWeixinMedia(model, vo);
		else if( Constants.MediaType.WEIBO.equals(mediaType))
			return recommendWeiboMedia(model, vo);
		else {
			throw new RuntimeException("无法识别的媒体类型：" + mediaType);
		}
	}

	private List<NaviBar> createNaviBar(RequirementVO vo) {
		List<NaviBar> list = Lists.newArrayList();
		String[] types = vo.getMediaTypes();
		for(String type : types) {
			NaviBar naviBar = new NaviBar(type, null, "disabled");
			if( vo.getCurrentMediaType().equals(type) ) {
				naviBar.setStatus("current");
			} else if( vo.getDoneMediaTypes().contains(type) ) {
				naviBar.setStatus("done");
			}
			list.add(naviBar);
		}
		return list;
	}

	/**
	 * 推荐微信媒体.
	 */
	@RequestMapping(value="recommendWeixinMedia")
	public String recommendWeixinMedia(Model model, RequirementVO vo) {
		setAttribute4Create(model);
		model.addAttribute("req", vo);
		model.addAttribute("serviceTypes", dicProvider.getDic(Constants.WeixinService.DIC_CODE).getDicItems());
		List<NaviBar> naviBars = createNaviBar(vo);
		model.addAttribute("naviBars", naviBars);
		return "member/req/advertiser/create/recommendWeixinMedia";
	}
	
	/**
	 * 自选微信媒体.
	 */
	@RequestMapping(value="selectWeixinMedia")
	public String selectWeixinMedia(Model model, RequirementVO vo) {
		setAttribute4Create(model);
		model.addAttribute("req", vo);
		model.addAttribute("serviceTypes", dicProvider.getDic(Constants.WeixinService.DIC_CODE).getDicItems());
		model.addAttribute("categories", dicProvider.getDic(Constants.WeixinCategory.DIC_CODE).getDicItems());
		
		List<NaviBar> naviBars = createNaviBar(vo);
		model.addAttribute("naviBars", naviBars);
		
		return "member/req/advertiser/create/selectWeixinMedia";
	}
	
	/**
	 * 推荐微博媒体.
	 */
	@RequestMapping(value="recommendWeiboMedia")
	public String recommendWeiboMedia(Model model, RequirementVO vo) {
		setAttribute4Create(model);
		model.addAttribute("req", vo);
		model.addAttribute("serviceTypes", dicProvider.getDic(Constants.WeiboService.DIC_CODE).getDicItems());
		List<NaviBar> naviBars = createNaviBar(vo);
		model.addAttribute("naviBars", naviBars);
		return "member/req/advertiser/create/recommendWeiboMedia";
	}
	
	/**
	 * 自选微博媒体.
	 */
	@RequestMapping(value="selectWeiboMedia")
	public String selectWeiboMedia(Model model, RequirementVO vo) {
		setAttribute4Create(model);
		model.addAttribute("req", vo);
		model.addAttribute("serviceTypes", dicProvider.getDic(Constants.WeiboService.DIC_CODE).getDicItems());
		model.addAttribute("categories", dicProvider.getDic(Constants.WeiboCategory.DIC_CODE).getDicItems());
		
		List<NaviBar> naviBars = createNaviBar(vo);
		model.addAttribute("naviBars", naviBars);
		
		return "member/req/advertiser/create/selectWeiboMedia";
	}
	
	/**
	 * 创建需求：无稿需求.
	 * 
	 * 显示"无稿"需求的表单.
	 */
	@RequestMapping(value="noArticle")
	@Token
	public String noArticle(Model model, HttpServletRequest request) {
		setAttribute4Create(model);
		setServiceTypes4NoArticle(model, request);
		return "member/req/advertiser/create/noArticle";
	}
	
	/**
	 * 创建需求：填写需求说明.
	 * 
	 */
	@RequestMapping(value="fillInReq")
	@Token
	public String fillInReq(Model model, HttpServletRequest request) {
		
		return "member/req/advertiser/create/fillInReq";
	}
	
	
	
	/**
	 * 提交采媒车中媒体数据到发布需求页面进行邀约
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="createReq")
	public String createReq(RequirementVO vo, HttpServletRequest request,  RedirectAttributes redirectAttrs) {
		String quoteIds = request.getParameter("quoteIds");
		String[] mediaQuoteIds = quoteIds.split(",");
		for (String quoteId : mediaQuoteIds) {
			MediaQuote quote = mediaQuoteService.get(quoteId);
			quote.getMedia().getName();
			quote.getMedia().getMediaType();
			quote.getMedia().getCategory();
			quote.getMedia().getFans();
			quote.getPrice();
			vo.getQuotes().add(quote);
		}
		redirectAttrs.addFlashAttribute("req", vo);
		redirectAttrs.addFlashAttribute("mediaTypes", dicProvider.getDic(MediaType.DIC_CODE).getDicItems());
		redirectAttrs.addFlashAttribute("inviteNums", dicProvider.getDic(Constants.InviteNum.DIC_CODE).getDicItems());
		redirectAttrs.addFlashAttribute("industryTypes", dicProvider.getDic(IndustryType.DIC_CODE).getDicItems());
		redirectAttrs.addFlashAttribute("regions", getRegions());
		return "redirect:/member/req/advertiser/create/cartReq";
	} 
	@RequestMapping(value="cartReq")
	@Token
	public String cartReq(Model model, HttpServletRequest request) {
		return "member/req/advertiser/create/cartReq";
	}
	
	@RequestMapping(value="finishCart")
	@Token(Type.REMOVE)
	public String finishCart(Model model, @ModelAttribute("req") RequirementVO vo, BindingResult result, HttpSession session,
			HttpServletRequest request, RedirectAttributes redirectAttrs) {
		try {
			List<MediaQuote> quotes = vo.getQuotes();
			Map<String, Object> quoteMap = Maps.newHashMap();
			List<MediaQuoteVo> list = (List<MediaQuoteVo>) session.getAttribute(MEDIAS_ARRAY);//从session中取出购物车中媒体list
			for (MediaQuote quote : quotes) {
				String type = quote.getType();
				List<MediaQuote> items = (List<MediaQuote>) quoteMap.get(type);
				if (items == null) {
					items = new ArrayList<MediaQuote>();
					quoteMap.put(type, items);
				}
				items.add(quote);
				for(int i=0;i<list.size();i++){
					if(quote.getId().trim().equals(list.get(i).getId().trim())){//根据id找出要删除的媒体对象
						list.remove(list.get(i));
					}
				}
			}
			int i = 1;
			for (String type : quoteMap.keySet()) {
				Requirement requitement = BeanMapper.map(vo, Requirement.class);
				if (quoteMap.size() > 1) {
					requitement.setName(requitement.getName() + "-" + i);
				}
				i++;
				requitement.setRegions(StringUtils.join(vo.getRegions(), SEPARATOR));
				requitement.setIndustryTypes(StringUtils.join(vo.getIndustryTypes(), SEPARATOR));
				try {
					saveArticle(vo, requitement);
				} catch (IllegalFileTypeException e) {
					throw new FileUploadException("articleFile", "撰稿材料：" + e.getMessage(), e);
				} catch (IOException e) {
					throw new FileUploadException("articleFile", "撰稿材料文件上传失败，请稍后重试！", e);
				}
				requitement.setStatus(ReqStatus.AUDIT);
				try {
					saveCertImg(vo, requitement);
				} catch (IllegalFileTypeException e) {
					throw new FileUploadException("certImgFile", "资质文件：" + e.getMessage(), e);
				} catch (IOException e) {
					throw new FileUploadException("certImgFile", "资质文件上传失败，请稍后重试！", e);
				}
				requitement.setCreateTime(new Date());
				requitement.setCreateBy(UserContext.getCurrent().getId());
				requitement.setModifyBy(requitement.getCreateBy());
				requitement.setModifyTime(requitement.getCreateTime());
				requitement.setCustomer(UserContext.getCurrent().getCustomer());
				List<MediaQuote> items = (List<MediaQuote>) quoteMap.get(type);
				requitement.setServiceTypes(type);
				Media media = items.get(0).getMedia();
				requitement.setMediaTypes(media.getMediaType());
				Date createTime = new Date();
				int budget = 0;
				for(MediaQuote mq : items) {
					MediaQuote mediaQuote = mediaQuoteService.get(mq.getId());
					String id = mediaQuote.getMedia().getId();
					int price = mediaQuote.getPrice();
					int priceMedia = mediaQuote.getPriceMedia();
					budget = budget + price;
					ReqMedia rm = new ReqMedia();
					Media m = mediaService.get(id);
					rm.setMedia(m);
					rm.setCreateTime(createTime);
					rm.setInviteType(InviteType.PASSIVE);
					rm.setQuoteType(type);
					rm.setPrice(price);
					rm.setTax(mediaQuote.getTax());
					rm.setPriceMedia(priceMedia);
					rm.setFbStatus(MediaFeedback.NULL);
					rm.setCfStatus(AdverConfirm.NULL);
					rm.setRequirement(requitement);
					requitement.getReqMedias().add(rm);
					requitement.setMediaTypes(m.getMediaType());
				}
				requitement.setBudget(budget);
				String inviteNum = null;
				if (items.size() < 10) {
					inviteNum = dicProvider.getDic(Constants.InviteNum.DIC_CODE).getDicItems().get(0).getItemCode();
				} else if (items.size() < 20) {
					inviteNum = dicProvider.getDic(Constants.InviteNum.DIC_CODE).getDicItems().get(1).getItemCode();
				} else if (items.size() < 50) {
					inviteNum = dicProvider.getDic(Constants.InviteNum.DIC_CODE).getDicItems().get(2).getItemCode();
				} else {
					inviteNum = dicProvider.getDic(Constants.InviteNum.DIC_CODE).getDicItems().get(3).getItemCode();
				}
				requitement.setInviteNum(inviteNum);
				requirementService.finish(requitement);
			}
			redirectAttrs.addFlashAttribute("message", "成功创建需求");
		} catch (FileUploadException e) {
			log.warn("文件上传失败", e);
			if(e.getMessage().contains("?")&&e.getField().equals("articleFile")){
				result.addError(new FieldError("req", e.getField(), "撰稿材料：文件类型["+vo.getArticleFile().getOriginalFilename().split("\\.")[1]+"]不允许上传，允许上传的文件类型为：doc, docx, pdf"));
			}else if(e.getMessage().contains("?")&&e.getField().equals("certImgFile")){
				result.addError(new FieldError("req", e.getField(), "资质文件：文件类型["+vo.getCertImgFile().getOriginalFilename().split("\\.")[1]+"]不允许上传，允许上传的文件类型为：doc, docx, pdf"));
			}else{
				result.addError(new FieldError("req", e.getField(), e.getMessage()));
			}
			
		}
		return "redirect:/member/req/advertiser";
	}
	
	/**
	 * 最后保存需求.
	 */
	@RequestMapping(value="finish")
	@Token(Type.REMOVE)
	public String finish(Model model, @ModelAttribute("req") RequirementVO vo, BindingResult result, HttpServletRequest request, RedirectAttributes redirectAttrs) {
		Requirement req = BeanMapper.map(vo, Requirement.class);
		try {
			req.setMediaTypes(StringUtils.join(vo.getMediaTypes(), SEPARATOR));
			req.setRegions(StringUtils.join(vo.getRegions(), SEPARATOR));
			req.setIndustryTypes(StringUtils.join(vo.getIndustryTypes(), SEPARATOR));
		
			if( req.isHasArticle() ) {
				try {
					req.setServiceTypes(getServiceTypes(vo));
					saveArticle(vo, req);
				} catch (IllegalFileTypeException e) {
					throw new FileUploadException("articleFile", "撰稿材料：" + e.getMessage(), e);
				} catch (IOException e) {
					throw new FileUploadException("articleFile", "撰稿材料文件上传失败，请稍后重试！", e);
				}
				saveReqMedia(vo, req);
				req.setStatus(ReqStatus.AUDIT);//有稿需求的初始状态为"待审核"
			} else {
				try {
					req.setServiceTypes(getServiceTypesNoArticle(vo));
					saveArticleMatter(vo, req);
				} catch (IllegalFileTypeException e) {
					throw new FileUploadException("articleMatterFile", "撰稿材料：" + e.getMessage(), e);
				} catch (IOException e) {
					throw new FileUploadException("articleMatterFile", "撰稿材料文件上传失败，请稍后重试！", e);
				}
				req.setStatus(ReqStatus.PENDING);//撰稿需求的初始状态为"待处理"
			}
			
			try {
				saveCertImg(vo, req);
			} catch (IllegalFileTypeException e) {
				throw new FileUploadException("certImgFile", "资质文件：" + e.getMessage(), e);
			} catch (IOException e) {
				throw new FileUploadException("certImgFile", "资质文件上传失败，请稍后重试！", e);
			}
			req.setCreateTime(new Date());
			req.setCreateBy(UserContext.getCurrent().getId());
			req.setModifyBy(req.getCreateBy());
			req.setModifyTime(req.getCreateTime());
			req.setCustomer(UserContext.getCurrent().getCustomer());
			
			requirementService.finish(req);
			
			redirectAttrs.addFlashAttribute("message", "成功创建需求");

		} catch (FileUploadException e) {
			log.warn("文件上传失败", e);
			if(e.getMessage().contains("?")&&e.getField().equals("articleFile")){
				result.addError(new FieldError("req", e.getField(), "撰稿材料：文件类型["+vo.getArticleFile().getOriginalFilename().split("\\.")[1]+"]不允许上传，允许上传的文件类型为：doc, docx, pdf"));
			}else if(e.getMessage().contains("?")&&e.getField().equals("certImgFile")){
				result.addError(new FieldError("req", e.getField(), "资质文件：文件类型["+vo.getCertImgFile().getOriginalFilename().split("\\.")[1]+"]不允许上传，允许上传的文件类型为：doc, docx, pdf"));
			}else{
				result.addError(new FieldError("req", e.getField(), e.getMessage()));
			}
			
		} 
		
		if( result.hasErrors() ) {
			TokenInterceptor.newToken(request);
			if(req.isHasArticle()) {
				return "member/req/advertiser/create/fillInReq";
			} else {
				setAttribute4Create(model);
				setServiceTypes4NoArticle(model, vo);
				return "member/req/advertiser/create/noArticle";
			}
		} else {
			if (req.isHasArticle()) {
				return "redirect:/member/req/advertiser";
			} else {
				return "redirect:/member/req/advertiser/notHasArticle";
			}
		}
	}
	
	private String getServiceTypes(RequirementVO vo) {
		Set<String> types = Sets.newLinkedHashSet();
		for(MediaQuote mq : vo.getQuotes()) {
			types.add(mq.getType());
		}
		return StringUtils.join(types, SEPARATOR);
	}
	
	private String getServiceTypesNoArticle(RequirementVO vo) {
		Set<String> types = Sets.newLinkedHashSet();
		if (vo.getServiceTypes() != null) {
			for (String type : vo.getServiceTypes()) {
				types.add(type);
			}
		}
		return StringUtils.join(types, SEPARATOR);
	}

	protected void setAttribute4Create(Model model) {
		model.addAttribute("mediaTypes", dicProvider.getDic(MediaType.DIC_CODE).getDicItems());
		model.addAttribute("inviteNums", dicProvider.getDic(Constants.InviteNum.DIC_CODE).getDicItems());
		model.addAttribute("industryTypes", dicProvider.getDic(IndustryType.DIC_CODE).getDicItems());
		model.addAttribute("regions", getRegions());
	}
	
	private List<Area> getRegions() {
		return areaProvider.getProvinceMap().get(AreaProvider.HOT_CITIES_KEY);
	}
	
	/**
	 * 媒体类型-服务类型映射.
	 */
	protected static Map<String, String> mediaTypeMap = Maps.newHashMap();
	static {
		mediaTypeMap.put(Constants.MediaType.WEIBO,  Constants.WeiboService.DIC_CODE);
		mediaTypeMap.put(Constants.MediaType.WEIXIN, Constants.WeixinService.DIC_CODE);
	}
	
	/**
	 * 媒体类型-服务类型前缀映射.
	 */
	protected static Map<String, String> mediaPrefixMap = Maps.newHashMap();
	static {
		mediaPrefixMap.put(Constants.MediaType.WEIBO,  "微博 - ");
		mediaPrefixMap.put(Constants.MediaType.WEIXIN, "微信 - ");
	}

	private void setServiceTypes4NoArticle(Model model, RequirementVO vo) {
		if ( vo != null ) {
			String[] types = vo.getMediaTypes();
			List<DicItem> items = Lists.newArrayList();
			for(String type : types) {
				List<DicItem> list = dicProvider.getDic(mediaTypeMap.get(type)).getDicItems();
				list = setNamePrefix(list, mediaPrefixMap.get(type));
				items.addAll(list);
			}
			model.addAttribute("serviceTypes", items);
		}
	}
	private void setServiceTypes4NoArticle(Model model, HttpServletRequest request) {
		RequirementVO vo = (RequirementVO) RequestContextUtils.getInputFlashMap(request).get("req");
		if ( vo != null ) {
			String[] types = vo.getMediaTypes();
			List<DicItem> items = Lists.newArrayList();
			for(String type : types) {
				List<DicItem> list = dicProvider.getDic(mediaTypeMap.get(type)).getDicItems();
				list = setNamePrefix(list, mediaPrefixMap.get(type));
				items.addAll(list);
			}
			model.addAttribute("serviceTypes", items);
		}
	}
	
	private List<DicItem> setNamePrefix(List<DicItem> list, String prefix) {
		List<DicItem> nlist = Lists.newArrayList();
		for(DicItem item : list) {
			nlist.add(item.clone());
		}
		
		for(DicItem item : nlist) {
			item.setItemName(prefix + item.getItemName());
		}
		return nlist;
	}
}

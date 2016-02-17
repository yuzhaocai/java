package com.lczy.media.controller.member;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lczy.common.util.MessageBean;
import com.lczy.common.web.Token;
import com.lczy.common.web.Token.Type;
import com.lczy.common.web.WebHelper;
import com.lczy.media.controller.common.BaseController;
import com.lczy.media.entity.Area;
import com.lczy.media.entity.Dic;
import com.lczy.media.entity.Media;
import com.lczy.media.entity.MediaTag;
import com.lczy.media.entity.Order;
import com.lczy.media.service.CustomerService;
import com.lczy.media.service.MediaCaseService;
import com.lczy.media.service.MediaQuoteService;
import com.lczy.media.service.MediaService;
import com.lczy.media.service.MediaTagService;
import com.lczy.media.service.OrderService;
import com.lczy.media.service.common.AreaProvider;
import com.lczy.media.service.common.DicProvider;
import com.lczy.media.util.Constants;
import com.lczy.media.util.Constants.IndustryType;
import com.lczy.media.util.Constants.WeiboFans;
import com.lczy.media.util.Constants.WeixinFans;
import com.lczy.media.util.Constants.WeixinFitProduct;
import com.lczy.media.util.UserContext;
import com.lczy.media.vo.MediaVO;

/**
 * 我的媒体控制器.
 * 
 * @author wu
 *
 */
@Controller
@RequestMapping("/member/media")
public class MyMediaController extends BaseController{
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private DicProvider dicProvider;
	
	@Autowired
	private MediaService mediaService;

	@Autowired
	private MediaQuoteService mediaQuoteService;
	
	@Autowired
	private MediaCaseService mediaCaseService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private MediaTagService mediaTagService;
	
	@Autowired
	private AreaProvider areaProvider;
	

	@RequestMapping({ "", "/list" })
	public String mediaList(Model model,HttpServletRequest request) throws Exception {
		
		int page = WebHelper.getPage(request);
		int size = WebHelper.getPageSize(request);
		String sort = getSort(request);
		Map<String, Object> searchParams = getSearchParams(request);
		searchParams.put("EQ_customer.id", UserContext.getCurrent()
				.getCustomer().getId());
		Page<Media> aPage = mediaService.find(searchParams, page, size, sort);

		setModalAttrsForPaging(model, searchParams, aPage, sort);
		return "member/media/list";
	}
	
	/**
	 * 媒体主选择类别
	 * 
	 * @param model
	 */
	@RequestMapping(value = "/createStep1", method = RequestMethod.GET)
	public String createStep1(Model model) throws Exception {
		Dic mediaType = dicProvider.getDicMap().get(
				Constants.MediaType.DIC_CODE);
		
		model.addAttribute("mediaType", mediaType);
		return "member/media/createStep1";
	}
	
	
	protected void setAttribute2Create(Model model, MediaVO vo) {
		mediaService.setAttrsForModel(model);
		model.addAttribute("regions", getRegionOptions());
		if(vo!=null)
			model.addAttribute("mediaType", vo.getMediaType());
		model.addAttribute("industryTypes", dicProvider.getDic(IndustryType.DIC_CODE).getDicItems());
		model.addAttribute("weixinFitProduct", dicProvider.getDic(WeixinFitProduct.DIC_CODE).getDicItems());
		model.addAttribute("weixinFans", dicProvider.getDic(WeixinFans.DIC_CODE).getDicItems());
		model.addAttribute("weiboFans", dicProvider.getDic(WeiboFans.DIC_CODE).getDicItems());
	}
	
	
	protected void setAttributeEdit(Model model, Media media) {
		mediaService.setAttrsForModel(model);
		// 防止导入的媒体的标签为空
		if (media.getTags() != null) {
			String[] tagsId = media.getTags().split(",");
			model.addAttribute("tags", mediaService.getNewTags(tagsId));
		}
		model.addAttribute("media", media);
		setAttribute2Create(model, null);
	}
	
	/**
	 * 选择类别后进入对应的创建页面
	 * 
	 * @param model
	 * @param vo
	 */
	@RequestMapping(value = "/createStep2", method = RequestMethod.GET)
	@Token(Type.NEW)
	public String createStep2(Model model, MediaVO vo) throws Exception {
		
		setAttribute2Create(model, vo);
		
		return "member/media/"+vo.getMediaType().substring(8).toLowerCase()+"/create";
	}
	
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	@Token(Type.NEW)
	public String mediaEdit(String mediaId,Model model,RedirectAttributes redirectAttributes) throws Exception {
		Media media = mediaService.findOneMedia(mediaId);
		MessageBean bean = mediaService.checkPermission(media);
		if(bean == null){
			setAttributeEdit(model, media);
			return "member/media/"+media.getMediaType().substring(8).toLowerCase()+"/edit";
		}else{
			redirectAttributes.addFlashAttribute("message", bean.toJSON());
			return "redirect:/member/userInfo";
		}
	}

	@RequestMapping({ "/del" })
	public String delMedia(String mediaId, String mediaType,
			RedirectAttributes redirectAttributes,Model model) {
		Media media = mediaService.findOneMedia(mediaId);
		List<Order> orderList = orderService.findByMediaId(mediaId);
		MessageBean bean = mediaService.checkPermission(media);
		if(bean ==  null){
			// 检查媒体是否可以删除
			if (media.getReqMedias() != null && media.getReqMedias().size() > 0
					|| orderList != null && orderList.size() > 0) {
				redirectAttributes.addFlashAttribute("notDel", true);
			} else {
				if (Constants.MediaType.WEIXIN.equals(mediaType)){
					mediaService.deleteWeiXin(mediaId);
				}else{
					mediaService.deleteWeiBo(mediaId);
				}
				bean = new MessageBean(1, "删除成功！");
				redirectAttributes.addFlashAttribute("message", bean.toJSON());
			}
			return "redirect:/member/media/list";
		}else{
			redirectAttributes.addFlashAttribute("message", bean.toJSON());
			return "redirect:/member/userInfo";
		}
		
	}
	
	
	/**
	 * 查看媒体详情
	 * @param id
	 */
	@RequestMapping(value = "view")
	public String view(String id, Model model) {
		Media media = mediaService.get(id);
		media.setRegion(areaProvider.getAreaNames(Arrays.asList(media
				.getRegion().split(","))));
		
		if(StringUtils.isNotBlank(media.getTags())){
			String[] mediaTagIds = media.getTags().split(",");
			String tagNames = "";
			for (String mediaTagId : mediaTagIds) {
				MediaTag temp = mediaTagService.get(mediaTagId);
				tagNames += temp.getName() + " ";
			}
			media.setTags(tagNames);
		}
		
		model.addAttribute("media", media);
		return "/member/media/view";
	}
	
	

	private List<Area> getRegionOptions() {
		List<Area> list = Lists.newArrayList();
		
		Map<String, List<Area>> provinceMap = Maps.newLinkedHashMap(areaProvider.getProvinceMap());
		provinceMap.remove(AreaProvider.HOT_CITIES_KEY);
		for(List<Area> province: provinceMap.values()) {
			for(Area a : province) {
				list.add(a);
			}
		}
		return list;
	}
	
	/**
	 * 新增媒体标签.
	 * @param name 标签名称
	 * @return 如果存在同名标签，则返回存在的标签；否则，创建新的标签并返回。
	 */
	@RequestMapping(value = "/addTag")
	@ResponseBody
	public MediaTag addTag(String name) {
		MediaTag tag = mediaTagService.findByTagName(name);
		if( tag == null) {
			tag = new MediaTag();
			tag.setName(name);
			tag.setCount(0);
			tag.setCreateBy(UserContext.getCurrent().getId());
			tag.setCreateTime(new Date());
			tag.setModifyBy(tag.getCreateBy());
			tag.setModifyTime(tag.getCreateTime());
			
			tag = mediaTagService.save(tag);
		}
		
		return tag;
	}

	/**
	 * 检查微博名称是否可创建.
	 */
	@RequestMapping(value = "checkWeiBoName")
	@ResponseBody
	public boolean checkMediaName(String mediaType, String name) {
		if (StringUtils.isBlank(mediaType))
			return false;
		if (StringUtils.isBlank(name))
			return true;
		return mediaService.weiboCountBy(mediaType, name) == 0;
	}

	/**
	 * 检查微博名称是否可修改.
	 */
	@RequestMapping(value = "checkWeiBoNameEdit")
	@ResponseBody
	public boolean checkEditMediaName(String mediaType, String name,
			String oldName) {
		if (StringUtils.isBlank(mediaType))
			return false;
		if (name.equals(oldName))
			return true;
		if (StringUtils.isBlank(name))
			return true;
		else
			return mediaService.weiboCountBy(mediaType, name) == 0;
	}
	
	
	/**
	 * 检查微信号是否可创建.
	 */
	@RequestMapping(value = "checkMediaAccount")
	@ResponseBody
	public boolean checkMediaAccount(String mediaType, String account) {
		if (StringUtils.isBlank(mediaType))
			return false;
		if(StringUtils.isBlank(account))
			return true;
		return mediaService.countBy(mediaType, account) == 0;
	}
	
	/**
	 * 检查微信号是否可修改.
	 */
	@RequestMapping(value = "checkMediaAccountEdit")
	@ResponseBody
	public boolean checkMediaAccountEdit(String mediaType, String account,
			String oldAccount) {
		if (StringUtils.isBlank(mediaType))
			return false;
		if (account.equals(oldAccount))
			return true;
		if(StringUtils.isBlank(account))
			return true;
		else
			return mediaService.countBy(mediaType, account) == 0;
	}
	
}

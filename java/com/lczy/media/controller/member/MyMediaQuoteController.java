package com.lczy.media.controller.member;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;

import com.google.common.collect.Maps;
import com.lczy.common.util.MessageBean;
import com.lczy.common.web.Token;
import com.lczy.common.web.Token.Type;
import com.lczy.common.web.WebHelper;
import com.lczy.media.controller.common.BaseController;
import com.lczy.media.entity.Dic;
import com.lczy.media.entity.Media;
import com.lczy.media.entity.MediaQuote;
import com.lczy.media.entity.MediaQuoteLog;
import com.lczy.media.service.MediaQuoteLogService;
import com.lczy.media.service.MediaQuoteService;
import com.lczy.media.service.MediaService;
import com.lczy.media.service.common.DicProvider;
import com.lczy.media.util.Constants;
import com.lczy.media.util.UserContext;

/**
 * @Description 媒体报价控制器
 * @Author Zhang.CJ
 * @Date 2015年8月18日 下午5:11:46
 */
@Controller
@RequestMapping("/member/media/quote")
public class MyMediaQuoteController extends BaseController {

	@Autowired
	private DicProvider dicProvider;

	@Autowired
	private MediaService mediaService;

	@Autowired
	private MediaQuoteService mediaQuoteService;
	
	@Autowired
	private MediaQuoteLogService mediaQuoteLogService;

	@RequestMapping({ "", "/list" })
	@Token(Type.NEW)
	public String list(Model model, String mediaId,
			RedirectAttributes redirectAttributes) throws Exception {
		Media media = mediaService.get(mediaId);
		MessageBean bean = checkPermission(media);
		if (bean == null) {
			Set<MediaQuote> quotes = media.getMediaQuotes();
			model.addAttribute("quotes", quotes);

			String[] dics = new String[] { Constants.WeixinService.DIC_CODE,
					Constants.WeiboService.DIC_CODE };
			for (int i = 0; i < dics.length; i++) {
				Dic data = dicProvider.getDicMap().get(dics[i]);
				if (i == 0)
					model.addAttribute("weixinService", data);
				if (i == 1)
					model.addAttribute("weiboService", data);
			}
			//日志中是否有未审核的发票变更记录	
			Map<String, Object> searchParams =Maps.newHashMap();
			searchParams.put("EQ_media.id", mediaId);
			searchParams.put("EQ_modifyType", Constants.QuoteModifyType.INVOICE);
			searchParams.put("EQ_status", Constants.QuoteLogStatus.AUDIT);
			Collection<MediaQuoteLog> mqList= mediaQuoteLogService.find(searchParams);
			if(mqList.size()>0){
				model.addAttribute("auditInvoice", true);
			}
			
			model.addAttribute("media", media);
			return "member/media/quote/list";
		}else{
			redirectAttributes.addFlashAttribute("message", bean.toJSON());
			return "redirect:/member/userInfo";
		}

	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@Token(Type.REMOVE)
	public String save(MediaQuote quote, String mediaId,RedirectAttributes redirectAttrs) throws Exception {
		Media media = mediaService.get(mediaId);
		quote.setMedia(media);
		mediaQuoteLogService.updatePriceMedia(quote, quote.getPrice(),Constants.QuoteModifyType.MEDIA);
		MessageBean bean = new MessageBean(1, "报价成功，请等待审批！");
		redirectAttrs.addFlashAttribute("message", bean.toJSON());	
		return "redirect:list?mediaId=" + mediaId;
	}

	@RequestMapping({ "/delete" })
	public String delete(String mediaId, String quoteId,RedirectAttributes redirectAttrs) {
		mediaQuoteService.remove(quoteId);
		MessageBean bean = new MessageBean(1, "删除成功！");
		redirectAttrs.addFlashAttribute("message", bean.toJSON());	
		return "redirect:list?mediaId=" + mediaId;
	}
	
	/**
	 * 检查当前用户是否有权限操作实体.
	 * @param media 目标实体
	 * @return 有权限则返回 null，无权限则返回 MessageBean 对象.
	 */
	private MessageBean checkPermission(Media media) {
		MessageBean bean = null;
		if( media == null) {
			bean = new MessageBean(0, "媒体不存在！");
		} else if( !hasPermission(media) ) {
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
	
	/**
	 * 修改发票状态
	 * @param mediaId
	 * @param provideInvoice
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/setInvoice", method = RequestMethod.POST)
	@ResponseBody
	public MessageBean setInvoice(String mediaId,String provideInvoice){
		MessageBean bean = null;
		try{
			mediaQuoteLogService.setInvoice(mediaId, provideInvoice);
			bean = new MessageBean(1, "修改发票状态成功！");
		}catch(Exception e){
			bean = new MessageBean(0, e.getMessage());
		}
		return bean;
	}
	
	
	
	/**
	 * 审核记录
	 * @param mediaId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/auditLog/{mediaId}"})
	public String auditLog(Model model,@PathVariable String mediaId, ServletRequest request) throws Exception {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		int page = WebHelper.getPage(request);
		int size = WebHelper.getPageSize(request);
		
		Media media = mediaService.get(mediaId);
		searchParams.put("EQ_media", media);
		searchParams.put("EQ_modifyType",Constants.QuoteModifyType.MEDIA );

		String startTime = (String)searchParams.get("GTE_createTime");
		String endTime = (String)searchParams.get("LTE_createTime");
		if (StringUtils.isNotBlank(startTime)) {
			DateTime start = DateTime.parse(startTime + "T00:00:00");
			searchParams.put("GTE_createTime", start.toDate());
		}
		if (StringUtils.isNotBlank(endTime)) {
			DateTime end = DateTime.parse(endTime + "T23:59:59");
			searchParams.put("LTE_createTime", end.toDate());
		}
		
		Page<MediaQuoteLog> data = mediaQuoteLogService.find(searchParams, page, size, null);
		model.addAttribute("data", data);
		model.addAttribute("media", media);
		return "member/media/quote/auditLog";
	}
	
	/**
	 * 撤销记录
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/revoke"})
	public String revoke(Model model,String logId,String mediaId,RedirectAttributes redirectAttrs) throws Exception {
		mediaQuoteLogService.remove(logId);
		redirectAttrs.addFlashAttribute("message", "撤销成功！");	
		return "redirect:/member/media/quote/auditLog/"+mediaId;
	}
	
}

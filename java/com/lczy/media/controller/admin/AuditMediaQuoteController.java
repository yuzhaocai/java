package com.lczy.media.controller.admin;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Maps;
import com.lczy.common.util.MessageBean;
import com.lczy.common.web.Token;
import com.lczy.common.web.Token.Type;
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

/**
 * 案例 controller
 * @author zhang.cj
 *
 */
@Controller
@RequestMapping(value = "/admin/audit/media/quote")
public class AuditMediaQuoteController extends BaseController{

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
			model.addAttribute("media", media);
			//日志中是否有未审核的发票变更记录	
			Map<String, Object> searchParams =Maps.newHashMap();
			searchParams.put("EQ_media.id", mediaId);
			searchParams.put("EQ_modifyType", Constants.QuoteModifyType.ADMIN_INVOICE);
			searchParams.put("EQ_status", Constants.QuoteLogStatus.AUDIT);
			Collection<MediaQuoteLog> mqList= mediaQuoteLogService.find(searchParams);
			if(mqList.size()>0){
				model.addAttribute("auditInvoice", true);
			}
			return "admin/media/quote/list";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@Token(Type.REMOVE)
	public String save(MediaQuote quote, String mediaId,RedirectAttributes redirectAttrs) throws Exception {
		Media media = mediaService.get(mediaId);
		quote.setMedia(media);
		mediaQuoteLogService.updatePriceMedia(quote, quote.getPrice(),Constants.QuoteModifyType.ADMIN_MEDIAQUOTE);
		MessageBean bean = new MessageBean(1, "报价成功，请等待审批！");
		redirectAttrs.addFlashAttribute("message", bean.toJSON());	
		return "redirect:list?mediaId=" + mediaId;
	}

	@RequestMapping({ "/delete" })
	public String delete(String mediaId, String quoteId) {
		mediaQuoteService.remove(quoteId);
		
		return "redirect:list?mediaId=" + mediaId;
	}
	
}

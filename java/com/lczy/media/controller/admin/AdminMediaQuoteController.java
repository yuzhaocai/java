package com.lczy.media.controller.admin;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.lczy.common.util.MessageBean;
import com.lczy.common.web.WebHelper;
import com.lczy.media.controller.common.BaseController;
import com.lczy.media.entity.DicItem;
import com.lczy.media.entity.Media;
import com.lczy.media.entity.MediaQuote;
import com.lczy.media.entity.MediaQuoteLog;
import com.lczy.media.service.DicService;
import com.lczy.media.service.MediaQuoteLogService;
import com.lczy.media.service.MediaQuoteService;
import com.lczy.media.service.SysConfigService;
import com.lczy.media.util.Constants;
import com.lczy.media.util.PriceAlgorithm;
import com.mchange.v1.lang.BooleanUtils;

/**
 * 报价管理 controller
 * @author wang.haibin
 *
 */
@Controller
public class AdminMediaQuoteController extends BaseController{

	private static Logger log = LoggerFactory.getLogger(AdminMediaQuoteController.class);
	
	@Autowired
	private MediaQuoteService mediaQuoteService;
	
	@Autowired
	private MediaQuoteLogService mediaQuoteLogService;
	
	@Autowired
	private SysConfigService sysConfigService;
	
	@Autowired
	private DicService dicService;
	
	
	/**
	 * 报价修改首页
	 * 
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/admin/mediaQuote")
	public String index(Model model, ServletRequest request) throws Exception {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		int page = WebHelper.getPage(request);
		int size = WebHelper.getPageSize(request);
		
		String startTime = (String)searchParams.get("GTE_media.createTime");
		String endTime = (String)searchParams.get("LTE_media.createTime");
		if (StringUtils.isNotBlank(startTime)) {
			DateTime start = DateTime.parse(startTime + "T00:00:00");
			searchParams.put("GTE_media.createTime", start.toDate());
		}
		if (StringUtils.isNotBlank(endTime)) {
			DateTime end = DateTime.parse(endTime + "T23:59:59");
			searchParams.put("LTE_media.createTime", end.toDate());
		}
		Page<MediaQuote> data = mediaQuoteService.find(searchParams, page, size, null);
		model.addAttribute("data", data);
		
		List<DicItem> mediaTypes = dicService.findItemByCodeType(Constants.MediaType.DIC_CODE);
		model.addAttribute("mediaTypes", mediaTypes);
		List<DicItem> mediaLevels = dicService.findItemByCodeType(Constants.MediaLevel.DIC_CODE);
		model.addAttribute("mediaLevels", mediaLevels);
		List<DicItem> mediaStatus = dicService.findItemByCodeType(Constants.MediaStatus.DIC_CODE);
		//页面查询条件去掉 “未审核”状态
		mediaStatus.remove(0);
		model.addAttribute("mediaStatus", mediaStatus);
		
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "admin/mediaQuote/index";
	}

	/**
	 * 后台修改报价
	 * 
	 * @param id
	 * @param price
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/admin/mediaQuote/update")
	public String update(String id, int price, RedirectAttributes redirectAttributes) throws Exception {
		MediaQuote mediaQuote = mediaQuoteService.get(id);
		try {
			mediaQuoteLogService.updatePrice(mediaQuote, price);
			log.info("修改价格，提交审核成功！");
			redirectAttributes.addFlashAttribute("message", "修改价格，提交审核成功！");
		} catch (Exception e) {
			log.error("修改价格，提交审核失败：{}", e.getMessage());
			redirectAttributes.addFlashAttribute("message", "修改价格，提交审核失败：" + e.getMessage());
		}
		
		return "redirect:/admin/mediaQuote";
	}
	
	@RequestMapping("/admin/mediaQuote/audit")
	public String audit(Model model, ServletRequest request) throws Exception {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		int page = WebHelper.getPage(request);
		int size = WebHelper.getPageSize(request);
		
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
		
		String certified = (String)searchParams.get("EQ_media.customer.certified");
		if (StringUtils.isNotBlank(certified)) {
			searchParams.put("EQ_media.customer.certified", BooleanUtils.parseBoolean(certified));
		}

		Page<MediaQuoteLog> data = mediaQuoteLogService.find(searchParams, page, size, "DESC_createTime");
		model.addAttribute("data", data);
		List<DicItem> mediaTypes = dicService.findItemByCodeType(Constants.MediaType.DIC_CODE);
		model.addAttribute("mediaTypes", mediaTypes);
		List<DicItem> mediaLevels = dicService.findItemByCodeType(Constants.MediaLevel.DIC_CODE);
		model.addAttribute("mediaLevels", mediaLevels);
		List<DicItem> mediaStatus = dicService.findItemByCodeType(Constants.MediaStatus.DIC_CODE);
		mediaStatus.remove(0);
		model.addAttribute("mediaStatus", mediaStatus);
		List<DicItem> quoteAuditStatus = dicService.findItemByCodeType(Constants.QuoteLogStatus.DIC_CODE);
		model.addAttribute("quoteAuditStatus", quoteAuditStatus);
		
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "/admin/mediaQuote/audit";
	}
	
	/**
	 * 价格修改记录
	 * 
	 * @param id
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/admin/mediaQuote/history/{id}")
	public String history(@PathVariable String id, Model model, ServletRequest request) throws Exception {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		int page = WebHelper.getPage(request);
		int size = WebHelper.getPageSize(request);
		
		MediaQuoteLog log = mediaQuoteLogService.get(id);
		searchParams.put("EQ_media", log.getMedia());

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
		return "/admin/mediaQuote/history";
	}
	
	/**
	 * 审核拒绝
	 * 
	 * @param id
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/mediaQuote/audit/reject", method = RequestMethod.POST)
	public String reject(String id, RedirectAttributes redirectAttributes) throws Exception {
		try {
			mediaQuoteLogService.reject(id);
			log.info("审核拒绝成功！");
			redirectAttributes.addFlashAttribute("message", "审核拒绝成功！");
		} catch (Exception e) {
			log.error("审核拒绝失败：{}", e.getMessage());
			redirectAttributes.addFlashAttribute("message", "审核拒绝失败：" + e.getMessage());			
		}
		return "redirect:/admin/mediaQuote/audit";
	}
	
	/**
	 * 审核通过
	 * 
	 * @param id
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/mediaQuote/audit/pass", method = RequestMethod.POST)
	public String pass(String id, RedirectAttributes redirectAttributes) throws Exception {
		try {
			mediaQuoteLogService.pass(id);
			log.info("审核通过成功！");
			redirectAttributes.addFlashAttribute("message", "审核通过成功！");
		} catch (Exception e) {
			log.error("审核通过失败：{}", e.getMessage());
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("message", "审核通过失败：" + e.getMessage());			
		}
		return "redirect:/admin/mediaQuote/audit";
	}
	
	/**
	 * 多个媒体报价 - 通过
	 * @param ids
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/admin/mediaQuote/audit/passes", method = RequestMethod.POST)
	public String pass(String[] ids, RedirectAttributes redirectAttributes) {
		try {
			if (ids != null) {
				for (String id : ids) {
					mediaQuoteLogService.pass(id);
					log.info(id + "批量审核通过成功！");
				}
				redirectAttributes.addFlashAttribute("message", "批量审核通过成功！");
			}
		} catch (Exception e) {
			log.error("批量审核通过失败：{}", e.getMessage());
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("message",
					"批量审核通过失败：" + e.getMessage());
		}
		return "redirect:/admin/mediaQuote/audit";
	}
	
	
	/**
	 * 多个媒体报价 - 拒绝
	 * 
	 * @param ids
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/admin/mediaQuote/audit/rejectes", method = RequestMethod.POST)
	public String reject(String[] ids, RedirectAttributes redirectAttributes) {
		try {
			if (ids != null) {
				for (String id : ids) {
					mediaQuoteLogService.reject(id);
					log.info(id + "批量审核拒绝成功！");
				}
				redirectAttributes.addFlashAttribute("message", "批量审核拒绝成功！");
			}
		} catch (Exception e) {
			log.error("批量审核拒绝成功：{}", e.getMessage());
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("message",
					"批量审核拒绝成功：" + e.getMessage());
		}
		return "redirect:/admin/mediaQuote/audit";
	}
	
	
	/**
	 * 修改发票状态的详细影响
	 * 
	 * @param id
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/mediaQuote/audit/detail")
	public String detail(String id, Model model) throws Exception {
		MediaQuoteLog mediaQuoteLog = mediaQuoteLogService.get(id);
		Media media = mediaQuoteLog.getMedia();
		Set<MediaQuote> set = media.getMediaQuotes();
		boolean provideInvoice = mediaQuoteLog.isProvideInvoice();
		float percent = media.getStar().getPercent();
		float taxRate = sysConfigService.getTaxRate();
		for (MediaQuote mediaQuote : set) {
			int priceMedia = mediaQuote.getPriceMedia();
			int price = PriceAlgorithm.calcPrice(priceMedia, provideInvoice, percent, taxRate);
			int tax = PriceAlgorithm.calcTax(priceMedia, provideInvoice, taxRate);
			mediaQuote.setPrice(price);
			mediaQuote.setPriceActivity(price);
			mediaQuote.setTax(tax);
		}
		model.addAttribute("mediaQuotes", set);
		return "/admin/mediaQuote/detail";
	}
	
	
	/**
	 * 管理员修改发票状态
	 * @param mediaId
	 * @param provideInvoice
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/mediaQuote/adminSetInvoice", method = RequestMethod.POST)
	@ResponseBody
	public MessageBean adminSetInvoice(String mediaId,String provideInvoice){
		MessageBean bean = null;
		try{
			mediaQuoteLogService.adminSetInvoice(mediaId, provideInvoice);
			bean = new MessageBean(1, "修改发票状态成功！");
		}catch(Exception e){
			bean = new MessageBean(0, e.getMessage());
		}
		return bean;
	}
}

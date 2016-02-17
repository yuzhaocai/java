package com.lczy.media.controller;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lczy.common.util.Strings;
import com.lczy.common.web.WebHelper;
import com.lczy.media.entity.Customer;
import com.lczy.media.entity.FavoritesReq;
import com.lczy.media.entity.Media;
import com.lczy.media.entity.MediaQuote;
import com.lczy.media.entity.MediaTag;
import com.lczy.media.entity.ReqMedia;
import com.lczy.media.entity.Requirement;
import com.lczy.media.service.CustomerService;
import com.lczy.media.service.FavoritesReqService;
import com.lczy.media.service.MediaService;
import com.lczy.media.service.MediaTagService;
import com.lczy.media.service.ReqMediaService;
import com.lczy.media.service.RequirementService;
import com.lczy.media.service.common.DicProvider;
import com.lczy.media.solr.Page;
import com.lczy.media.solr.RequirementDoc;
import com.lczy.media.util.Constants;
import com.lczy.media.util.JsonBean;
import com.lczy.media.util.UserContext;
import com.lczy.media.vo.RequirementSearchVo;

/**
 * 
 * 需求广场控制器.
 * 
 * @author wu
 *
 */
@Controller
@RequestMapping("/plaza")
public class ReqPlazaController {
	
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(ReqPlazaController.class);
	
	@Autowired
	private RequirementService requirementService;
	
	@Autowired
	private ReqMediaService reqMediaService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private MediaService mediaService;
	
	@Autowired
	protected MediaTagService mediaTagService;
	
	@Autowired
	private DicProvider dicProvider;
	
	@Autowired
	private FavoritesReqService favoritesReqService;
	
	@RequestMapping({"", "list"})
	public String list(Model model,HttpServletRequest request)  {

		// 页面高亮显示“需求广场”导航菜单
		model.addAttribute("activeSearch", "req");
		model.addAttribute("active", "plaza");
		
		setModelAttribute(model);
		
		return "plaza/list";
	}
	
	/**
	 * 设置公共的模型属性.
	 */
	protected void setModelAttribute(Model model) {
		model.addAttribute("industryTypes", dicProvider.getDic(Constants.IndustryType.DIC_CODE).getDicItems());
		model.addAttribute("weixinCategories", dicProvider.getDic(Constants.WeixinCategory.DIC_CODE).getDicItems());
		model.addAttribute("weiboCategories", dicProvider.getDic(Constants.WeiboCategory.DIC_CODE).getDicItems());
		model.addAttribute("mediaTypes", dicProvider.getDic(Constants.MediaType.DIC_CODE).getDicItems());
		model.addAttribute("recTags", getRecTags());
		model.addAttribute("otherMediaCategories", dicProvider.getDic(Constants.OtherMediaCategory.DIC_CODE).getDicItems());
	}
	
	/**
	 * 从搜索引擎查找需求.
	 * @param vo
	 * @return json
	 */
	@RequestMapping("load")
	@ResponseBody
	public String load(RequirementSearchVo vo) {
		Map<String, Object> fieldsMap = getFieldsMap(vo);
		
		JsonBean result = new JsonBean();
		try {
			Page<RequirementDoc> page = requirementService.find(fieldsMap, vo.getSort(), vo.getPageNum(), vo.getPageSize());
			result.put("result", true);
			result.put("page", toJsonBean(page));
		} catch (Exception e) {
			log.error("solr查询失败", e);
			result.put("result", false);
		}
		
		return result.toJson();
	}
	
	protected JsonBean toJsonBean(Page<RequirementDoc> page) {
		JsonBean bean = new JsonBean();
		bean.put("total", page.getTotal());
		bean.put("totalPage", page.getTotalPage());
		bean.put("pageNum", page.getPageNum());
		bean.put("pageSize", page.getPageSize());
		bean.put("next", page.isNext());
		bean.put("previous", page.isPrevious());
		List<JsonBean> data = Lists.newArrayList();
		Customer cust = customerService.get(UserContext.getCurrentCustomer().getId());
		String custType = cust.getCustType();
		List<String> ids = Lists.newArrayList();
		boolean needFavorites = false;
		if (Constants.CustType.CUST_PRO.equals(custType)){
			needFavorites = true;
			Map<String, Object> searchParams = new TreeMap<String, Object>();
			searchParams.put("EQ_customer.id", cust.getId());
			List<FavoritesReq> reqs =  favoritesReqService.find(searchParams);
			for (FavoritesReq req : reqs) {
				ids.add(req.getReq().getId());
			}
		}
		for( RequirementDoc doc : page.getData() ) {
			data.add(toJsonBean(doc, needFavorites, ids));
		}
		bean.put("data", data);
		
		return bean;
	}

	protected JsonBean toJsonBean(RequirementDoc doc, boolean needFavorites, List<String> ids) {
		JsonBean bean = new JsonBean();
		bean.put("id", doc.getId());
		bean.put("name", doc.getName());
		bean.put("summary", Strings.ellipsis(doc.getSummary(), 150));
		bean.put("budget", doc.getBudget());
		bean.put("mediaTypes", dicProvider.getItemNames(doc.getMediaTypes()));
		bean.put("serviceTypes", dicProvider.getItemNames(doc.getServiceTypes()));
		bean.put("industryTypes", dicProvider.getItemNames(doc.getIndustryTypes()));
		bean.put("startTime", new DateTime(doc.getStartTime()).toString("yyyy-MM-dd"));
		bean.put("endTime", new DateTime(doc.getEndTime()).toString("yyyy-MM-dd"));
		bean.put("deadline", new DateTime(doc.getDeadline()).toString("yyyy-MM-dd"));
		bean.put("allowChange", doc.isAllowChange() ? "允许" : "不允许");
		if (needFavorites) {
			if (ids.contains(doc.getId())) {
				bean.put("favorites", true);
			}
		}
		return bean;
	}
	
	private Map<String, Object> getFieldsMap(RequirementSearchVo vo) {
		Map<String, Object> fieldsMap = Maps.newLinkedHashMap();
		
		//只查正常状态
		fieldsMap.put("status", Constants.ReqStatus.NORMAL);
		fieldsMap.put("isPublic", true);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		fieldsMap.put("deadline", "[" + calendar.getTime().getTime() + " TO *]");
		
		//媒体类型
		if( StringUtils.isNotBlank(vo.getMediaTypes()) ) {
			fieldsMap.put("mediaTypes", vo.getMediaTypes());
		}
		//媒体类型
		if( StringUtils.isNotBlank(vo.getName()) ) {
			fieldsMap.put("name", "*" + vo.getName() + "*");
		}
		
		//行业类型
		if( StringUtils.isNotBlank(vo.getIndustryTypes()) && !"101601".equals(vo.getIndustryTypes()) ) {
			fieldsMap.put("industryTypes", vo.getIndustryTypes());
		}
		
		//预算
		if( StringUtils.isNotBlank(vo.getBudget()) ) {
			String[] range = vo.getBudget().split(",");
			if( range.length == 1 ) {
				fieldsMap.put("budget", "[" + Integer.parseInt(range[0]) + " TO *]");
			} else {
				fieldsMap.put("budget", "[" + Integer.parseInt(range[0]) 
						+ " TO " + Integer.parseInt(range[1]) + "]");
			}
		}
		
		return fieldsMap;
	}

	/**
	 * 查看详情.
	 */
	@RequestMapping("detail/{id}")
	public String detail(@PathVariable String id, Model model)  {
		Requirement req = requirementService.get(id);
		if( req != null && !req.getIsPublic() ) {
			req = null;
		}
		model.addAttribute("req", req);
		
		return "plaza/detail";
	}
	
	/**
	 * 主动应征.
	 */
	@RequestMapping("enlist/{id}")
	public String enlist(@PathVariable String id, Model model) {
		
		Requirement req = requirementService.get(id);
		if( req != null && !req.getIsPublic() ) { //必须是公开的需求才能应征
			req = null;
		} else {
			model.addAttribute("quotes", getMediaQuotes(req,model));
		}
		
		model.addAttribute("req", req);
		
		return "plaza/enlist";
	}
	
	/**
	 * 获取符合条件的报价列表.
	 */
	private List<MediaQuote> getMediaQuotes(Requirement req,Model model) {
		//媒体用户对应需求的报价类型
		List<MediaQuote> typeQuotes = Lists.newArrayList();
		//媒体用户其他报价类型
		List<MediaQuote> otherQuotes = Lists.newArrayList();
		Customer cust = customerService.get(UserContext.getCurrentCustomer().getId());
		for( Media m : cust.getMedias() ) {
			if (req.getMediaTypes().contains(m.getMediaType())) {
				//查找当前媒体中对应的报价类型
				for(MediaQuote mq:m.getMediaQuotes()){
					if(req.getServiceTypes().contains(mq.getType())){
						typeQuotes.add(mq);
					}
				}
				//当前媒体没有对应类型报价
				if(typeQuotes.size()==0){
					otherQuotes.addAll(m.getMediaQuotes());
				}
			}
		}
		if(typeQuotes.size()>0){
			//媒体用户中包含报价类型
			markEnlisted(req.getId(), cust.getId(), typeQuotes);
			//判断媒体用户下的媒体是否全部抢单
			for(MediaQuote mq:typeQuotes){
				if(!mq.getMedia().isEnlisted()){
					model.addAttribute("unAllCheck", true);
				}
			}
			return typeQuotes;
		}else{
			//媒体用户中不包含报价类型
			markEnlisted(req.getId(), cust.getId(), otherQuotes);
			//判断媒体用户下的媒体是否全部抢单
			for(MediaQuote mq:otherQuotes){
				if(!mq.getMedia().isEnlisted()){
					model.addAttribute("unAllCheck", true);
				}
			}
			return otherQuotes;
		}

	}

	/**
	 * 标记已应征.
	 */
	private void markEnlisted(String rid, String custId, List<MediaQuote> quotes) {
		Map<String, Object> searchParams = Maps.newLinkedHashMap();
		searchParams.put("EQ_requirement.id", rid);
		searchParams.put("EQ_media.customer.id", custId);
		
		Collection<ReqMedia> rmList = reqMediaService.find(searchParams);
		for(ReqMedia rm : rmList) {
			for(MediaQuote quote : quotes) {
				if( quote.getMedia().equals(rm.getMedia())
						&& quote.getType().equals(rm.getQuoteType())) {
					quote.setEnlisted(true);
					//一个媒体只能抢一次单
					quote.getMedia().setEnlisted(true);
				}
			}
		}
	}

	/**
	 * 确定应征.
	 */
	@RequestMapping(value="enlist", method=RequestMethod.POST)
	@ResponseBody
	public String doEnlist(String rid, String mid, String type, String price, String priceMedia, HttpServletRequest request) {
		JsonBean bean = null;
		
		if( SecurityUtils.getSubject().isAuthenticated() ) {
			Requirement req = requirementService.get(rid);
			if( req == null || !req.getIsPublic() ) {
				bean = new JsonBean("400", "非法操作！");
			} else {
				try {
					bean = requirementService.enlist(rid, mid, type, price, priceMedia);
				} catch(Exception e) {
					log.error("抢单失败", e);
					bean = new JsonBean("500", "抢单失败！");
				}
			}
		} else {
			bean = new JsonBean("302"); //从定向到 登录页面
			bean.put("url", WebHelper.getLoginURL(request));
		}
		
		return bean.toJson();
	}
	
	
	/**
	 * 查看是否能被媒体主动应征.
	 */
	@RequestMapping(value = "checkEnlist/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String checkEnlist(@PathVariable String id,
			HttpServletRequest request) {
		JsonBean bean = new JsonBean();
		Requirement req = requirementService.get(id);
		String inviteN = req.getInviteNum().substring(
				req.getInviteNum().indexOf("_") + 1);
		int inviteNum = 0;
		if (inviteN.contains("-"))
			inviteNum = Integer.parseInt(inviteN.split("-")[1]);
		else
			inviteNum = Integer.parseInt(inviteN);

		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("EQ_id", id);
		searchParams.put("NEQ_cfStatus", Constants.AdverConfirm.REFUSE);
		int enlistNum = reqMediaService.countBy(searchParams);
		if (enlistNum < 2 * inviteNum) {
			bean.put("success", true);
		} else {
			bean.put("fail", false);
		}
		return bean.toJson();
	}
	
	private List<MediaTag> getRecTags() {
		return mediaTagService.findRecTags();
	}
}


/**
 * 
 */
package com.lczy.media.controller.member;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lczy.common.exception.IllegalFileTypeException;
import com.lczy.common.util.Files;
import com.lczy.common.util.MessageBean;
import com.lczy.common.web.Token;
import com.lczy.common.web.Token.Type;
import com.lczy.common.web.TokenInterceptor;
import com.lczy.common.web.WebHelper;
import com.lczy.media.controller.MediaController;
import com.lczy.media.entity.Area;
import com.lczy.media.entity.DicItem;
import com.lczy.media.entity.Media;
import com.lczy.media.entity.MediaQuote;
import com.lczy.media.entity.ReqMedia;
import com.lczy.media.entity.Requirement;
import com.lczy.media.exception.BalanceNotEnoughException;
import com.lczy.media.service.MediaQuoteService;
import com.lczy.media.service.MediaService;
import com.lczy.media.service.ReqMediaService;
import com.lczy.media.service.RequirementService;
import com.lczy.media.service.common.AreaProvider;
import com.lczy.media.service.common.DicProvider;
import com.lczy.media.solr.MediaDoc;
import com.lczy.media.solr.SolrService;
import com.lczy.media.util.Constants;
import com.lczy.media.util.Constants.AdverConfirm;
import com.lczy.media.util.Constants.IndustryType;
import com.lczy.media.util.Constants.InviteType;
import com.lczy.media.util.Constants.MediaFeedback;
import com.lczy.media.util.Constants.MediaType;
import com.lczy.media.util.Constants.ReqStatus;
import com.lczy.media.util.FileServerUtils;
import com.lczy.media.util.JsonBean;
import com.lczy.media.util.UserContext;
import com.lczy.media.vo.RequirementVO;

/**
 * 广告主需求控制器.
 * 
 * @author wu
 *
 */
@Controller
@RequestMapping("/member/req/advertiser")
public class ReqAdvertiserController extends MediaController {
	
	private Logger log = LoggerFactory.getLogger(ReqAdvertiserController.class);
	
	@Autowired
	protected RequirementService requirementService;
	
	@Autowired
	protected DicProvider dicProvider;
	
	@Autowired
	private MediaService mediaService;
	
	@Autowired
	private MediaQuoteService mediaQuoteService;
	
	@Autowired
	private ReqMediaService reqMediaService;
	
	@Autowired
	private SolrService solrService;
	
	
	/**
	 * 允许上传的稿件类型.
	 */
	public static final List<String> ALLOWED_ARTICLE_FILE_TYPES = Lists.newArrayList("jpg", "png", "bmp", "doc", "docx", "pdf");
	
	/**
	 * 允许上传的资质文件类型.
	 */
	public static final List<String> ALLOWED_CERT_FILE_TYPES = Lists.newArrayList("jpg", "png", "bmp");
	
	/**
	 * 允许上传的撰稿材料文件类型.
	 */
	public static final List<String> ALLOWED_MATTER_FILE_TYPES = Lists.newArrayList("jpg", "png", "bmp", "doc", "docx", "pdf");

	/**
	 * 我的需求列表.
	 */
	@RequestMapping(value={"", "list"})
	public String list(Model model, HttpServletRequest request) {
		int page = WebHelper.getPage(request);
		int size = WebHelper.getPageSize(request);
		String sort = getSort(request);
		Map<String, Object> searchParams = getSearchParams(request);
		//只能查看自己创建的需求
		searchParams.put("EQ_createBy", UserContext.getCurrent().getId());
		searchParams.put("EQ_hasArticle", true); //只列出有稿需求
		searchParams.put("EQ_deleted", false); //未删除的
		Page<Requirement> aPage= requirementService.find(searchParams, page, size, sort);
		
		setModalAttrsForPaging(model, searchParams, aPage, sort);
		
		return "member/req/advertiser/list";
	}
	/**
	 * 我的需求列表.
	 */
	@RequestMapping(value="notHasArticle")
	public String notHasArticle(Model model, HttpServletRequest request) {
		int page = WebHelper.getPage(request);
		int size = WebHelper.getPageSize(request);
		String sort = getSort(request);
		Map<String, Object> searchParams = getSearchParams(request);
		//只能查看自己创建的需求
		searchParams.put("EQ_createBy", UserContext.getCurrent().getId());
		searchParams.put("EQ_hasArticle", false); //只列出有稿需求
		searchParams.put("EQ_deleted", false); //未删除的
		Page<Requirement> aPage= requirementService.find(searchParams, page, size, sort);
		
		setModalAttrsForPaging(model, searchParams, aPage, sort);
		
		return "member/req/advertiser/notHasArticle";
	}

	/**
	 * 设置创建需求页面需要的属性.
	 */
	protected void setAttribute4Create(Model model, HttpServletRequest request) {
		model.addAttribute("mediaTypes", dicProvider.getDic(MediaType.DIC_CODE).getDicItems());
		model.addAttribute("serviceTypes", getServiceTypes(request.getParameter("mediaTypes")));
		model.addAttribute("regions", getRegionOptions());
		model.addAttribute("industryTypes", dicProvider.getDic(IndustryType.DIC_CODE).getDicItems());
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

	protected List<DicItem> getServiceTypes(String mediaTypes) {
		List<DicItem> items;
		if( Constants.MediaType.WEIXIN.equals(mediaTypes) ) {
			items = dicProvider.getDic(Constants.WeixinService.DIC_CODE).getDicItems();
		} else if( Constants.MediaType.WEIBO.equals(mediaTypes) ) {
			items = dicProvider.getDic(Constants.WeiboService.DIC_CODE).getDicItems();
		} else {
			items = Lists.newArrayList();
		}
		return items;
	}
	
	/**
	 * 查询符合条件的推荐媒体.
	 */
	@RequestMapping(value="loadMedias")
	@ResponseBody
	public JsonBean loadMedias(RequirementVO vo, HttpServletRequest request) {
		
		int pageNum = Integer.parseInt(request.getParameter("pageNum")); //WebHelper.getInt(request, "pageNum");
		//pageNum = pageNum == 0 ? 1 : pageNum;
		
		int pageSize = getPageSize(vo.getInviteNum());
		//int pageSize = 2;
		
		JsonBean result = new JsonBean();
		try {
			
			Map<String, Object> fieldsMap = getFieldsMap(vo, request, true);
			
			com.lczy.media.solr.Page<MediaDoc> page = solrService.find(MediaDoc.class, fieldsMap, null, pageNum, pageSize);
			
			for(int i = 0; i < 2 && pageSize > page.getTotalPage(); i++ ) {
				if( i == 0) {		
					fieldsMap.remove("industryTypes"); //删除行业类型过滤条件
				} else if ( i == 1) {
					fieldsMap.remove("regions"); //删除地区过滤条件
				}
				page = solrService.find(MediaDoc.class, fieldsMap, request.getParameter("sort"), pageNum, pageSize);
			}
			
			result = toJsonBean(page);
			result.put("result", true);
			result.put(FIELDS_MAP_PARAM, serialize(fieldsMap));
		} catch (Exception e) {
			log.error("solr 查询异常", e);
			result.put("result", false);
		}
		
		return result;
	}
	
	private static final String FIELD_SEPARATOR = "::";
	
	private static final String FIELDS_MAP_PARAM = "fieldsMap";
	
	/**
	 * 把fieldsMap转换成字符串.
	 */
	private String serialize(Map<String, Object> fieldsMap) {
		StringBuilder sb = new StringBuilder(300);
		for(Map.Entry<String, Object> entry : fieldsMap.entrySet()) {
			if(sb.length() > 0) {
				sb.append(FIELD_SEPARATOR);
			}
			sb.append(entry.getKey()).append("=").append(serialize(entry.getValue()));
		}
		return sb.toString();
	}
	
	/**
	 * 把Object序列化成字符串.
	 */
	private String serialize(Object value) {
		if( value instanceof Iterable<?> ) {
			StringBuilder sb = new StringBuilder(100);
			Iterable<?> it = (Iterable<?>)value;
			for(Object o : it) {
				if( sb.length() > 0 ) {
					sb.append(",");
				}
				sb.append(o.toString());
			}
			return sb.toString();
		} else {
			return value.toString();
		}
	}

	/**
	 * 把fieldsMap字符串转换成Map.
	 */
	private Map<String, Object> deserialize(String source) {
		Map<String, Object> fieldsMap = new HashMap<String, Object>();
		String[] fields = source.split(FIELD_SEPARATOR);
		
		for(String field : fields) {
			log.debug("===>> field = {}", field);
			String[] a = field.split("=");
			Assert.isTrue(a.length == 2);
			String fieldName = a[0];
			Object fieldValue = parseFieldValue(a[1]);
			fieldsMap.put(fieldName, fieldValue);
		}
		return fieldsMap;
	}

	private Object parseFieldValue(String value) {
		String[] values = value.split(",");
		if( values.length > 1 ) {
			return Arrays.asList(values);
		}
		return value;
	}

	/**
	 * 根据拟邀媒体数计算分页大小.
	 * @param inviteNum 形如 "INVITE_0-10" 的拟邀媒体数量.
	 */
	private int getPageSize(String inviteNum) {
		Assert.notNull(inviteNum);
		
		String[] range = inviteNum.substring(7).split("-");
		if( range.length == 2 ) {
			return Integer.parseInt(range[1].trim()) / 3; //目前只有三类媒体，所以除以3
		} else {
			return Integer.parseInt(range[0].trim()) / 3; 
		}
	}

	/**
	 * 替换一行媒体.
	 */
	@RequestMapping(value="loadOneMedia")
	@ResponseBody
	public JsonBean loadOneMedia(RequirementVO vo, HttpServletRequest request) {
		Map<String, Object> fieldsMap = getFieldsMap(vo, request, true);
		if( StringUtils.isNotBlank(vo.getExcludes()) ) {
			String[] excludes = vo.getExcludes().split(SEPARATOR);
			fieldsMap.put("-id", Arrays.asList(excludes));
		}
		int pageNum = Integer.parseInt(request.getParameter("pageNum"));
		int pageSize = 1/*getPageSize(vo.getInviteNum())*/;
		JsonBean result = new JsonBean();
		try {
			com.lczy.media.solr.Page<MediaDoc> page = solrService.find(MediaDoc.class, fieldsMap, request.getParameter("sort"), pageNum, pageSize);
			result = toJsonBean(page);
			result.put("result", true);
		} catch (Exception e) {
			log.error("solr 查询异常", e);
			result.put("result", false);
		}
		
		return result;
	}

	/**
	 * 添加"媒体级别"筛选条件.
	 */
	private void setMediaLevelField(HttpServletRequest request,
			Map<String, Object> fieldsMap) {
		int level = WebHelper.getInt(request, "level");
		
		switch (level) {
		case 2:
			List<String> levels = Lists.newArrayList();
			levels.add(Constants.MediaLevel.LEVEL2_P);
			levels.add(Constants.MediaLevel.LEVEL2_S);
			fieldsMap.put("level", levels);
			break;
		case 3:
			levels = Lists.newArrayList();
			levels.add(Constants.MediaLevel.LEVEL3_P);
			levels.add(Constants.MediaLevel.LEVEL3_S);
			fieldsMap.put("level",  levels);
			break;
		default:
			fieldsMap.put("level",  Constants.MediaLevel.LEVEL1);
		}
	}

	/**
	 * 构造查询媒体的过滤条件.
	 * @param request 
	 */
	private Map<String, Object> getFieldsMap(RequirementVO vo, HttpServletRequest request, boolean needLevel) {
		
		String serializedFieldsMap = WebHelper.getString(request, FIELDS_MAP_PARAM);
		if( StringUtils.isNotBlank(serializedFieldsMap) ) {
			return deserialize(serializedFieldsMap);
		}
		
		Map<String, Object> fieldsMap = Maps.newLinkedHashMap();
		fieldsMap.put("status", Constants.MediaStatus.NORMAL);
		
		if( StringUtils.isNotBlank(vo.getCurrentMediaType()) ) {
			fieldsMap.put("mediaType", vo.getCurrentMediaType());
		}
		
		if( vo.getServiceTypes() != null && vo.getServiceTypes().length > 0 ) {
			if( vo.getServiceTypes().length > 1 )
				fieldsMap.put("serviceTypes", Arrays.asList(vo.getServiceTypes()));
			else
				fieldsMap.put("serviceTypes", vo.getServiceTypes()[0]);
		}
		
		if( vo.getRegions() != null && vo.getRegions().length > 0 ) {
			List<String> regions = Lists.newArrayList();
			for( String region : vo.getRegions() ) {
				if( ! "ALL".equalsIgnoreCase(region) ) {
					regions.add(region + '*');//模糊查询
				}
			}
			if( regions.size() > 0 ) {
				fieldsMap.put("regions", regions);						
			}
		}
		
		if( vo.getIndustryTypes() != null && vo.getIndustryTypes().length > 0 ) {
			List<String> types = Lists.newArrayList();
			for( String type : vo.getIndustryTypes() ) {
				if( ! "ALL".equalsIgnoreCase(type) ) {
					types.add(type);
				}
			}
			if( types.size() > 0 ) {
				fieldsMap.put("industryTypes", types);						
			}
		}
		
		if( StringUtils.isNotBlank(vo.getCategory()) ) {
			fieldsMap.put("category", vo.getCategory());
		}
		
		if( StringUtils.isNotBlank(vo.getFans()) ) {
			String[] range = vo.getFans().split(",");
			if( range.length == 1 ) {
				fieldsMap.put("fans", "[" + Integer.parseInt(range[0]) + " TO *]");
			} else {
				fieldsMap.put("fans", "[" + Integer.parseInt(range[0]) 
						+ " TO " + Integer.parseInt(range[1]) + "]");
			}
		}
		if (needLevel) {
			setMediaLevelField(request, fieldsMap);
		}
		
		return fieldsMap;
	}
	
	@Override
	protected JsonBean toJsonBean(MediaDoc doc) {
		JsonBean bean = super.toJsonBean(doc);
		//TODO
		return bean;
	}
	

	/**
	 * 用户自定义查询媒体.
	 */
	@RequestMapping(value="queryMedias")
	@ResponseBody
	public String queryMedias(RequirementVO vo, HttpServletRequest request) {
		
		Map<String, Object> fieldsMap = getFieldsMap(vo, request, false);
		
		int pageNum = WebHelper.getInt(request, "pageNum");
		request.getParameter("pageNum");
		pageNum = Math.max(1, pageNum);
		int pageSize = WebHelper.getInt(request, "pageSize");
		pageSize = Math.max(1, pageSize);
		
		JsonBean result = new JsonBean();
		try {
			com.lczy.media.solr.Page<MediaDoc> page = solrService.find(MediaDoc.class, fieldsMap, null, pageNum, pageSize);
			result = toJsonBean(page);
			result.put("result", true);
		} catch (Exception e) {
			log.error("solr 查询异常", e);
			result.put("result", false);
		}
		
		return result.toJson();
	}
	
	
	/**
	 * 分隔符.
	 */
	protected final static String SEPARATOR = ",";

	/**
	 * 保存撰稿材料.
	 * @throws IOException 
	 * @throws IllegalFileTypeException 
	 */
	protected void saveArticleMatter(RequirementVO vo, Requirement req) throws IllegalFileTypeException, IOException {
		String fid = uploadFile(vo.getArticleMatterFile(), req.getArticleMatter(), ALLOWED_MATTER_FILE_TYPES);
		if( fid != null ) {
			req.setArticleMatter(fid);
		}
	}

	/**
	 * 保存资质文件
	 * @throws IOException 
	 * @throws IllegalFileTypeException
	 */
	protected void saveCertImg(RequirementVO vo, Requirement req) throws IllegalFileTypeException, IOException {
		String fid = uploadFile(vo. getCertImgFile(), req.getCertImg(), ALLOWED_CERT_FILE_TYPES);
		if( fid != null ) {
			req.setCertImg(fid);
		}
	}

	/**
	 * 保存稿件.
	 * @throws IOException 
	 * @throws IllegalFileTypeException 
	 */
	protected void saveArticle(RequirementVO vo, Requirement req) throws IllegalFileTypeException, IOException {
		String fid = uploadFile(vo.getArticleFile(), req.getArticle(), ALLOWED_ARTICLE_FILE_TYPES);
		if( fid != null ) {
			req.setArticle(fid);
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
				file = File.createTempFile("req", Files.getExtension(filename));
				Files.write(file, mpf.getInputStream());
				
				String fileType = Files.getExtension(file);
				if( fileType == null || !isAllowedFileType(fileType, allowedList) ) {
					throw new IllegalFileTypeException(fileType, allowedFileTypes(allowedList));
				}
				
				fid = FileServerUtils.upload(fid, filename, file, false, fileType, true);
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

	/**
	 * 保存预约单.
	 */
	protected void saveReqMedia(RequirementVO vo, Requirement req) {
		
		Date createTime = new Date();
		for( MediaQuote mq : vo.getQuotes() ) {
			String id = mq.getMedia().getId();
			String type = mq.getType();
			Media media = mediaService.get(id);
			MediaQuote quote = mediaQuoteService.findByMediaAndType(media, type);
			ReqMedia rm = new ReqMedia();
			rm.setMedia(media);
			rm.setCreateTime(createTime);
			rm.setInviteType(InviteType.PASSIVE);
			rm.setQuoteType(type);
			rm.setPrice(quote.getPrice());
			rm.setPriceMedia(quote.getPriceMedia());
			rm.setTax(quote.getTax());
			rm.setFbStatus(MediaFeedback.NULL);
			rm.setCfStatus(AdverConfirm.NULL);
			
			rm.setRequirement(req);
			req.getReqMedias().add(rm);
		}
	}
	
	/**
	 * 加载需求的可选媒体列表.
	 * @param id 需求 ID.
	 */
	@RequestMapping("selectMedia")
	public String selectMedia(String id, Model model) {
		
		Requirement req = requirementService.get(id);
		List<ReqMedia> rms = Lists.newArrayList();
		for( ReqMedia rm : req.getReqMedias() ) {
			System.out.println("id = "+rm.getId());
			if( MediaFeedback.ACCEPT.equals(rm.getFbStatus()) 
					&& AdverConfirm.NULL.equals(rm.getCfStatus()) ) {
				rms.add(rm);
			}
		}
		
		Collections.sort(rms, new Comparator<ReqMedia>() {
			public int compare(ReqMedia o1, ReqMedia o2) {
				return o1.getInviteType().compareTo(o2.getInviteType());
			}
		});
		System.out.println("rms.size = "+rms.size());
		model.addAttribute("reqMedias", rms);
		model.addAttribute("req", req);
		
		return "member/req/advertiser/selectMedia";
	}
	
	/**
	 * 查看改稿申请内容.
	 * @param id 预约单(ReqMedia)ID.
	 */
	@RequestMapping("viewChangedArticle")
	public String viewChangedArticle(String id, Model model) {
		ReqMedia reqMedia = reqMediaService.get(id);
		model.addAttribute("reqMedia", reqMedia);

		return "member/req/advertiser/viewChangedArticle";
	}
	
	/**
	 * 生成订单.
	 * @param id 预约单(ReqMedia)ID.
	 */
	@RequestMapping(value="accept", method = RequestMethod.POST)
	@ResponseBody
	public String accept(String id,String endTime) {
		MessageBean msg = null;
//		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
//            Date reqEndDate = df.parse(endTime);
//            if(df.parse(df.format(new Date())).getTime()<=reqEndDate.getTime()){
            	reqMediaService.accept(id);
    			msg = new MessageBean(1, "生成订单并扣款成功");
//            }else{
//            	msg = new MessageBean(0, "需求已过期！");
//            }
	            
		} catch (BalanceNotEnoughException e) {
			msg = new MessageBean(0, e.getMessage());
		} catch (Exception e) {
			msg = new MessageBean(0, "操作失败！");
			log.error("生成订单发生异常.", e);
		}
		return msg.toJSON();
	}
	
	/**
	 * 拒绝媒体.
	 * @param id 预约单(ReqMedia)ID.
	 */
	@RequestMapping(value="refuse/{id}", method = RequestMethod.POST)
	@ResponseBody
	public String refuse(@PathVariable String id) {
		MessageBean msg = null;
		try {
			reqMediaService.refuse(id);
			msg = new MessageBean(1, "操作成功！");
		} catch (Exception e) {
			msg = new MessageBean(0, "操作失败！");
			log.error("生成订单发生异常.", e);
		}
		return msg.toJSON();
	}
	
	/**
	 * 编辑需求-保存.
	 * @param id 需求ID.
	 */
	@RequestMapping(value="save")
	@Token
	public String save(@Valid @ModelAttribute("req") RequirementVO req, BindingResult result,
			Model model, HttpServletRequest request, 
			RedirectAttributes redirectAttrs) {
		Requirement entity = requirementService.get(req.getId());
		
		MessageBean bean = check4Edit(entity);
		
		if( bean == null ) {
			try {
				//修改需求状态为待审核
				entity.setStatus(ReqStatus.AUDIT);
				entity.setName(req.getName());
				entity.setSummary(req.getSummary());
				entity.setIsPublic(req.getIsPublic());
				entity.setAllowChange(req.isAllowChange());
				entity.setStartTime(LocalDate.parse(req.getStartTime()).toDate());
				entity.setEndTime(LocalDate.parse(req.getEndTime()).toDate());
				entity.setDeadline(LocalDate.parse(req.getDeadline()).toDate());
				//注释   项目预算、拟邀媒体数、 地区、 行业类型、 媒体类别 、服务类别
//				entity.setBudget(req.getBudget());
//				entity.setInviteNum(req.getInviteNum());
//				entity.setRegions(StringUtils.join(req.getRegions(), ","));
//				entity.setIndustryTypes(StringUtils.join(req.getIndustryTypes(), ","));
//				entity.setMediaTypes(StringUtils.join(req.getMediaTypes(), ","));
//				entity.setServiceTypes(StringUtils.join(req.getServiceTypes(), ","));
				
				entity.setModifyTime(new Date());
				entity.setModifyBy(UserContext.getCurrent().getId());
				
				try {
					saveArticle(req, entity);
				} catch (IllegalFileTypeException e) {
					result.addError(new FieldError("req", "articleFile", "稿件：" + e.getMessage()));
				}
				
				try {
					saveCertImg(req, entity);
				} catch (IllegalFileTypeException e) {
					result.addError(new FieldError("req", "certImgFile", "资质：" + e.getMessage()));
				}
				
				if( result.hasErrors() ) {
					//已邀请媒体数
					model.addAttribute("reqChooseMediaNum", entity.getReqMedias().size());
					//返回错误页面时，将数据赋值到返回值
					req.setBudget(entity.getBudget());
					req.setInviteNum(entity.getInviteNum());
					req.setRegions(StringUtils.split(entity.getRegions()));
					req.setIndustryTypes(StringUtils.split(entity.getIndustryTypes()));
					req.setMediaTypes(StringUtils.split(entity.getMediaTypes()));
					req.setServiceTypes(StringUtils.split(entity.getServiceTypes()));
					TokenInterceptor.newToken(request);
					model.addAttribute("req", req);
					setAttribute4Create(model, request);
					return "member/req/advertiser/create/reqEdit";
				}
				
				requirementService.save(entity);
				
				bean = new MessageBean(1, "操作成功！");
				
			} catch (Exception e) {
				log.error("编辑需求时发生异常", e);
				bean = new MessageBean(0, "操作失败");
			}
		}
		redirectAttrs.addFlashAttribute("message", bean.toJSON());
		return "redirect:/member/req/advertiser";
	}
	/**
	 * 编辑需求.
	 * @param id 需求ID.
	 */
	@RequestMapping(value="edit/{id}")
	@Token
	public String edit(@PathVariable String id, Model model, RedirectAttributes redirectAttrs, HttpServletRequest request) {
		Requirement req = requirementService.get(id);
		MessageBean bean = checkPermission(req);
		bean = (bean == null) ? check4Edit(req) : bean;
		if( bean == null ) {
			
			model.addAttribute("req", new RequirementVO(req));
			setAttribute4Create(model, request);
			model.addAttribute("serviceTypes", getServiceTypes(req.getMediaTypes()));
			//已邀请媒体数
			model.addAttribute("reqChooseMediaNum", req.getReqMedias().size());
			return "member/req/advertiser/edit";
		} else {
			redirectAttrs.addFlashAttribute("message", bean.toJSON());
			return "redirect:/member/req/advertiser";
		}
		
	}
	
	/**
	 * 检查需求是否可修改.
	 * @param req 目标实体
	 * @return 如果可修改则返回 null，否则返回 MessageBean 说明原因。
	 */
	protected MessageBean check4Edit(Requirement req) {
		MessageBean bean = checkPermission(req);
		if( bean == null ) {
			if( req.getPassiveNum() > 0 || req.getActiveNum() > 0 ) {
				bean = new MessageBean(0, "此需求不可修改！");
			}
		}
		return bean;
	}

	/**
	 * 编辑需求-保存.
	 * @param id 需求ID.
	 */
	@RequestMapping(value="edit", method = RequestMethod.POST)
	@Token(Type.REMOVE)
	public String doEdit(@Valid @ModelAttribute("req") RequirementVO req, BindingResult result,
			Model model, HttpServletRequest request, 
			RedirectAttributes redirectAttrs) {
		Requirement entity = requirementService.get(req.getId());
		
		MessageBean bean = check4Edit(entity);
		
		if( bean == null ) {
			try {
				entity.setName(req.getName());
				entity.setSummary(req.getSummary());
				entity.setBudget(req.getBudget());
				entity.setInviteNum(req.getInviteNum());
				entity.setIsPublic(req.getIsPublic());
				entity.setAllowChange(req.isAllowChange());
				entity.setStartTime(LocalDate.parse(req.getStartTime()).toDate());
				entity.setEndTime(LocalDate.parse(req.getEndTime()).toDate());
				entity.setDeadline(LocalDate.parse(req.getDeadline()).toDate());
				entity.setRegions(StringUtils.join(req.getRegions(), ","));
				entity.setIndustryTypes(StringUtils.join(req.getIndustryTypes(), ","));
				entity.setMediaTypes(StringUtils.join(req.getMediaTypes(), ","));
				entity.setServiceTypes(StringUtils.join(req.getServiceTypes(), ","));
				
				entity.setModifyTime(new Date());
				entity.setModifyBy(UserContext.getCurrent().getId());
				
				try {
					saveArticle(req, entity);
				} catch (IllegalFileTypeException e) {
					result.addError(new FieldError("req", "articleFile", "稿件：" + e.getMessage()));
				}
				
				try {
					saveCertImg(req, entity);
				} catch (IllegalFileTypeException e) {
					result.addError(new FieldError("req", "certImgFile", "资质：" + e.getMessage()));
				}
				
				if( result.hasErrors() ) {
					TokenInterceptor.newToken(request);
					model.addAttribute("req", req);
					setAttribute4Create(model, request);
					return "member/req/advertiser/edit";
				}
				
				requirementService.save(entity);
				
				bean = new MessageBean(1, "操作成功！");
				
			} catch (Exception e) {
				log.error("编辑需求时发生异常", e);
				bean = new MessageBean(0, "操作失败");
			}
		}
		redirectAttrs.addFlashAttribute("message", bean.toJSON());
		return "redirect:/member/req/advertiser";
	}
	
	/**
	 * 查看需求.
	 * @param id 需求ID.
	 */
	@RequestMapping(value="view/{id}")
	@Token
	public String view(@PathVariable String id, Model model, RedirectAttributes redirectAttrs) {
		Requirement req = requirementService.get(id);
		MessageBean bean = checkPermission(req);
//		bean = (bean == null) ? check4Edit(req) : bean;
		if( bean == null ) {
			req.setRegions(areaProvider.getAreaNames(Arrays.asList(req.getRegions().split(","))));
			req.setIndustryTypes(dicProvider.getItemNames(Arrays.asList(req.getIndustryTypes().split(","))));
			//已邀请媒体数
			model.addAttribute("reqChooseMediaNum", req.getReqMedias().size());
	
			model.addAttribute("req", req);
			if (req.isHasArticle()){
				return "member/req/advertiser/view";
			} else {
				return "member/req/advertiser/viewNoArticle";
			}
		} else {
			redirectAttrs.addFlashAttribute("message", bean.toJSON());
			return "redirect:/member/req/advertiser";
		}
	}
	
	/**
	 * 删除需求.
	 * @param id 需求ID.
	 */
	@RequestMapping(value="delete/{id}")
	public String delete(@PathVariable String id, RedirectAttributes redirectAttr) {
		Requirement req = requirementService.get(id);
		MessageBean bean = check4Delete(req);
		if( bean == null) {
			try {
				requirementService.delete(req);
				bean = new MessageBean(1, "操作成功！");
			} catch(Exception e) {
				log.error("删除需求时发生异常", e);
				bean = new MessageBean(0, "操作失败！");
			}
		}
		
		redirectAttr.addFlashAttribute("message", bean.toJSON());
		return "redirect:/member/req/advertiser";
	}
	
	/**
	 * 检查需求是否可删除.
	 * @param req 目标实体
	 * @return 如果可删除则返回 null，否则返回 MessageBean 说明理由.
	 */
	private MessageBean check4Delete(Requirement req) {
		MessageBean bean = checkPermission(req);
		if( bean == null ) {
			if( req.getPassiveNum() > 0 ) {
				bean = new MessageBean(0, "您不能删除已有‘应邀’媒体的需求");
			}
		}
		return bean;
	}

	/**
	 * 检查当前用户是否有权限操作实体.
	 * @param req 目标实体
	 * @return 有权限则返回 null，无权限则返回 MessageBean 对象.
	 */
	protected MessageBean checkPermission(Requirement req) {
		MessageBean bean = null;
		if( req == null) {
			bean = new MessageBean(0, "需求不存在！");
		} else if( !hasPermission(req) ) {
			bean = new MessageBean(0, "您无权操作此需求：" + req.getId());
		}
		
		return bean;
	}

	/**
	 * @return 是否有权限操作此需求.
	 */
	protected boolean hasPermission(Requirement req) {
		
		return req.getCreateBy().equals(UserContext.getCurrent().getId());
	}
	
	
	
	/**
	 * 加载需求的媒体列表.
	 * @param id 需求 ID.
	 */
	@RequestMapping("selectAllMedia")
	@Token
	public String selectAllMedia(String id, Model model, RedirectAttributes redirectAttrs) {
		
		Requirement req = requirementService.get(id);
		MessageBean bean = checkPermission(req);
//		bean = (bean == null) ? check4Edit(req) : bean;
		if( bean == null ) {
			List<ReqMedia> rms = Lists.newArrayList();
			rms.addAll(req.getReqMedias());
			Collections.sort(rms, new Comparator<ReqMedia>() {
				public int compare(ReqMedia o1, ReqMedia o2) {
					return o1.getInviteType().compareTo(o2.getInviteType());
				}
			});
			model.addAttribute("reqMedias", rms);
			model.addAttribute("req", req);
			
			return "member/req/advertiser/selectAllMedia";
		} else {
			redirectAttrs.addFlashAttribute("message", bean.toJSON());
			return "redirect:/member/req/advertiser";
		}
	}
	
	
	/**
	 * 删除订单内选中的媒体.
	 * @param id 媒体ID.
	 */
	@RequestMapping(value="deleteMedia/{id}", method = RequestMethod.POST)
	@ResponseBody
	public String deleteMedia(@PathVariable String id) {
		MessageBean msg = null;
		ReqMedia rMedia = reqMediaService.get(id);
		MessageBean bean = checkPermission(rMedia.getRequirement());
		bean = (bean == null) ? check4Delete(rMedia,rMedia.getRequirement()) : bean;
		if( bean == null ) {
			try {
				requirementService.deleteMedia(rMedia);
				msg = new MessageBean(1, "操作成功！");
			} catch (BalanceNotEnoughException e) {
				msg = new MessageBean(0, e.getMessage());
			} catch (Exception e) {
				msg = new MessageBean(0, "操作失败！");
				log.error("删除媒体发生异常.", e);
			}
		} else{
			msg = bean;
		}
		return msg.toJSON();
		
	}
	
	
	/**
	 * 检查需求中媒体是否可删除.
	 * @param req 需求实体   media当前媒体
	 * @return 如果可删除则返回 null，否则返回 MessageBean 说明理由.
	 */
	private MessageBean check4Delete(ReqMedia media,Requirement req) {
		MessageBean bean = checkPermission(req);
		if( bean == null ) {
			if( !media.getFbStatus().equals("MEDIA_FB_NULL") ) {
				bean = new MessageBean(0, "您不能删除已反馈的媒体");
			}
		}
		return bean;
	}
}

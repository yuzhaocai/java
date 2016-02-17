package com.lczy.media.controller;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lczy.media.entity.AdvSetting;
import com.lczy.media.entity.MediaCase;
import com.lczy.media.entity.MediaTag;
import com.lczy.media.service.AdvSettingService;
import com.lczy.media.service.MediaCaseService;
import com.lczy.media.service.MediaService;
import com.lczy.media.service.MediaTagService;
import com.lczy.media.service.OtherMediaService;
import com.lczy.media.service.SysConfigService;
import com.lczy.media.service.common.DicProvider;
import com.lczy.media.solr.MediaDoc;
import com.lczy.media.solr.Page;
import com.lczy.media.util.Constants;
import com.lczy.media.util.FileServerUtils;
import com.lczy.media.util.JsonBean;
import com.lczy.media.vo.MediaSearchVo;

@Controller
@RequestMapping("/")
public class IndexController extends MediaController {
	
	@Autowired
	protected DicProvider dicProvider;
	
	@Autowired
	private MediaService mediaService;
	
	@Autowired
	private OtherMediaService otherMediaService;
	
	@Autowired
	private MediaCaseService mediaCaseService;
	
	@Autowired
	private AdvSettingService advSettingService;
	
	@Autowired
	protected MediaTagService mediaTagService;
	
	@Autowired
	private SysConfigService sysConfigService;
	
	//首页显示多条广告类型
	private static Map<String,String> showMore = Maps.newHashMap();
	static {
		showMore.put("优媒推荐","mediaRec");
		showMore.put("名企客户","enterpriseCsut");
		showMore.put("微信频道-推荐媒体","weixinRec");
		showMore.put("微博频道-推荐媒体","weiboRec");
		showMore.put("合作媒体","partnerMedia");
		showMore.put("友情链接","friendlyLink");
		showMore.put("营销经典图文头条","marketingHead");
		showMore.put("小编推荐","editorRecommend");
	}
	
	@RequestMapping({"","/index" })
	public String index(Model model) throws Exception {
		setAdSetting(model);
		model.addAttribute("weixinCategories", dicProvider.getDic(Constants.WeixinCategory.DIC_CODE).getDicItems());
		model.addAttribute("weiboCategories", dicProvider.getDic(Constants.WeiboCategory.DIC_CODE).getDicItems());
		model.addAttribute("mediaTypes", dicProvider.getDic(Constants.MediaType.DIC_CODE).getDicItems());
		model.addAttribute("recTags", getTags());
		model.addAttribute("otherMediaCategories", dicProvider.getDic(Constants.OtherMediaCategory.DIC_CODE).getDicItems());
		
		model.addAttribute("mediaCount", getMediaCount());
		//成交数
		model.addAttribute("orderCount", sysConfigService.get("TOTAL_VOLUME").getValue());
		//悬赏金额
		model.addAttribute("offerSums", format(sysConfigService.get("OFFER_SUMS").getValue()));
		
		return "index";
	}
	
	private String format(String str){
		// 将传进数字反转
		String reverseStr = new StringBuilder(str).reverse().toString();
		
		String strTemp = "";
		for (int i=0; i<reverseStr.length(); i++) {
			if (i*3+3 > reverseStr.length()) {
				strTemp += reverseStr.substring(i*3, reverseStr.length());
				break;
			}
			strTemp += reverseStr.substring(i*3, i*3+3)+",";
		}
		// 将 最后一个,去除
		if (strTemp.endsWith(",")) {
			strTemp = strTemp.substring(0, strTemp.length()-1);
		}
		// 将数字重新反转
		String resultStr = new StringBuilder(strTemp).reverse().toString();
		return resultStr;
	}
	
	/**
	 * 获得广告位数据
	 * @param model
	 */
	protected void setAdSetting(Model model){
		List<AdvSetting> asList = advSettingService.findAdvSetting();
		Map<String,List<AdvSetting>> asMap = Maps.newHashMap();
		for(AdvSetting as:asList){
			String key = as.getType();
			if (asMap.containsKey(key)){
				asMap.get(key).add(as);
			} else {
				List<AdvSetting> list = Lists.newArrayList(); 
				list.add(as);
				asMap.put(key, list);
			}
		}
		for(String key:asMap.keySet()){
			String mkey = returnKey(key);
			model.addAttribute(mkey, asMap.get(key));
		}
	}
	
	
	private int getMediaCount() {
		int  num = mediaService.countBy(null) + otherMediaService.countBy(null);
		return num * 2;
	}
	
	protected String returnKey(String key){
		if(showMore.containsKey(key)){
			return showMore.get(key);
		}
		return key;
	}

	@RequestMapping("load")
	@ResponseBody
	public JsonBean load(MediaSearchVo vo) {
		
		Map<String, Object> fieldsMap = getFieldsMap(vo);
		
		JsonBean result = new JsonBean();
		try {
			Page<MediaDoc> page = mediaService.find(fieldsMap, vo.getSort(), vo.getPageNum(), vo.getPageSize());
			result.put("result", true);
			result.put("page", toJsonBean(page));
		} catch (Exception e) {
			log.error("solr查询失败", e);
			result.put("result", false);
		}
		
		return result;
	}
	
	
	@RequestMapping("loadMarketing")
	@ResponseBody
	public JsonBean loadMarketing(HttpServletRequest request) {
		JsonBean result = new JsonBean();
		try {
	        List<JsonBean> data = Lists.newArrayList();
	        getUrlMarketingToData(data, "http://www.cnmei.com/cms/category/yingxiao");
	        result.put("data", data);
	        result.put("result", true);
		} catch (Exception e) {
			log.error("获取营销案例失败", e);
			result.put("result", false);
		}
		return result;
	}
	
	
	
	
	private void getUrlMarketingToData(List<JsonBean> data, String link) throws Exception {
		URL url = new URL(link);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		InputStream inStream =  conn.getInputStream();
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        byte[]  buffer = new byte[1204];  
        int len = 0;  
        while ((len = inStream.read(buffer)) != -1){  
            outStream.write(buffer,0,len);  
        }  
        inStream.close();
        String htmlSource = new String(outStream.toByteArray());
        org.jsoup.nodes.Document doc = Jsoup.parse(htmlSource);
        Elements articles = doc.getElementsByTag("article");
        if (articles != null && articles.size() > 0) {
        	for (Element e : articles) {
        		Element a = e.getElementsByTag("a").get(0);
        		JsonBean bean = new JsonBean();
        		String href = a.attr("href");
        		String title = a.childNodes().get(0).toString();
        		bean.put("url", href);
        		bean.put("titleAll", title);
        		bean.put("title", title.length()>19 ?title.substring(0,19):title);
        		data.add(bean);
        	}
        }
        Elements nextPages = doc.getElementsContainingText("下一页");
        if (nextPages != null && nextPages.size() > 0) {
        	Element next = nextPages.last();
        	String nextUrl = next.attr("href");
        	if (nextUrl != null) {
        		getUrlMarketingToData(data, nextUrl);
        	}
        }
	}
	
	
	
	@RequestMapping("loadAd")
	@ResponseBody
	public JsonBean loadAd(HttpServletRequest request) {
		String type = request.getParameter("adType");
		JsonBean result = new JsonBean();
		try {
			Properties prop = new Properties();
			prop.load(getClass().getClassLoader().getResourceAsStream("properties/ad.properties"));
			List<JsonBean> data = Lists.newArrayList();
			SortedMap<String, Object> sortedMap = new TreeMap(prop);
			for (String keyValue : sortedMap.keySet()) {
				if (keyValue.startsWith(type)) {
					String value = prop.getProperty(keyValue);
					String[] param = value.split(",");
					if (param.length == 4) {
						JsonBean bean = new JsonBean();
						bean.put("id", param[0]);
						bean.put("type", param[1]);
						String fileUrl = FileServerUtils.getFileUrl(param[2]);
						bean.put("showPic", fileUrl);
						bean.put("name", param[3]);
						data.add(bean);
					} else if (param.length == 5){
						JsonBean bean = new JsonBean();
						bean.put("id", param[0]);
						bean.put("type", param[1]);
						String fileUrl = FileServerUtils.getFileUrl(param[2]);
						bean.put("showPic", fileUrl);
						bean.put("name", param[3]);
						bean.put("cooperate", true);
						data.add(bean);
						
					}
				}
			}
			result.put("data", data);
			result.put("result", true);
		} catch (Exception e) {
			log.error("获取广告位失败，请检查配置文件", e);
			result.put("result", false);
		}
		
		return result;
	}
	@RequestMapping("loadCase")
	@ResponseBody
	public JsonBean loadCase(HttpServletRequest request) {
		Map<String, Object> searchParams = new LinkedHashMap<String, Object>();
		if (request.getParameter("media_mediaType") != null){
			searchParams.put("EQ_media.mediaType", request.getParameter("media_mediaType"));
		}
		searchParams.put("EQ_media.status", Constants.MediaStatus.NORMAL);
		JsonBean result = new JsonBean();
		try {
			org.springframework.data.domain.Page<MediaCase> page = mediaCaseService.find(searchParams, 1, 100, "DESC_createTime");
			result.put("result", true);
			result.put("page", caseToJsonBean(page.getContent()));
		} catch (Exception e) {
			log.error("solr查询失败", e);
			result.put("result", false);
		}
		return result;
	}
	protected JsonBean caseToJsonBean(List<MediaCase> page) {
		JsonBean bean = new JsonBean();
		List<JsonBean> data = Lists.newArrayList();
		for( MediaCase doc : page) {
			data.add(caseToJsonBean(doc));
		}
		bean.put("data", data);
		return bean;
	}
	
	
	protected JsonBean caseToJsonBean(MediaCase page) {
		JsonBean bean = new JsonBean();
		bean.put("id", page.getId());
		bean.put("title", page.getTitle().length()>19?page.getTitle().substring(0,19):page.getTitle());
		bean.put("titleAll", page.getTitle());
		bean.put("light", page.getLight());
		bean.put("content", page.getContent());
		bean.put("createBy", page.getCreateBy());
		bean.put("createTime", page.getCreateTime());
		bean.put("showPic", page.getShowPic());
		bean.put("modifyBy", page.getModifyBy());
		bean.put("modifyTime", page.getModifyTime());
		bean.put("mediaId", page.getMedia().getId());
		if( Constants.MediaType.WEIXIN.equals(page.getMedia().getMediaType()) ) {
			bean.put("mediaType", "weixin");
		} else if( Constants.MediaType.WEIBO.equals(page.getMedia().getMediaType()) ) {
			bean.put("mediaType", "weibo");
		}
		return bean;
	}
	
	protected List<MediaTag> getTags() {
		return mediaTagService.findRecTags();
	}
	
	@RequestMapping("guidepage")
	public String guidepage(Model model) throws Exception {
		model.addAttribute("active", "guidepage");
		setModelAttribute(model);
		return "guidepage";
	}
	
	@RequestMapping("updateLog")
	public String updateLog(Model model) throws Exception {
		model.addAttribute("active", "updateLog");
		setModelAttribute(model);
		return "updateLog";
	}
	
	/**
	 * 获取cms底部数据
	 * @return
	 */
	@RequestMapping("cmsFooter")
	@ResponseBody
	public JsonBean cmsFooter(){
		JsonBean result = new JsonBean();
		URL url;
		try {
			url = new URL("http://www.cnmei.com/cms/");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			InputStream inStream =  conn.getInputStream();
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
	        byte[]  buffer = new byte[1204];  
	        int len = 0;  
	        while ((len = inStream.read(buffer)) != -1){  
	            outStream.write(buffer,0,len);  
	        }  
	        inStream.close();
	        String htmlSource = new String(outStream.toByteArray());
	        org.jsoup.nodes.Document doc = Jsoup.parse(htmlSource);
	        Element footer = doc.getElementsByTag("footer").get(0);
	        result.put("result", true);
	        result.put("data", footer.children().toString());
		} catch (Exception e) {
			log.error("获取cms底部失败", e);
			result.put("result", false);
		}
		
		return result;
	}
	
}

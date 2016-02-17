package com.lczy.media.jobs;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.lczy.media.entity.FindCnmei;
import com.lczy.media.service.FindCnmeiService;

/**
 * 更新发现采媒数据
 *
 */
@Service
public class UpdateFindCnmei { 
	@Autowired
	private FindCnmeiService findCnmeiService;
	
	private static Logger logger = LoggerFactory.getLogger(UpdateFindCnmei.class);
	
	
	/**
	 * 访问cms地址
	 * http://www.cnmei.com/cms/category/yingxiao(营销经典)
	 * http://www.cnmei.com/cms/category/cehuazhuanchang(策划专场)
	 * http://www.cnmei.com/cms/category/wenanfenxi(文案分析)
	 * http://www.cnmei.com/cms/category/fangjianfengchuan(坊间疯传)
	 * http://www.cnmei.com/cms/category/guanggaochaoliu(广告潮流)
	 * http://www.cnmei.com/cms/category/guanggaocelue(广告策略)
	 */
	private static List<String> cmsLink = Lists.newArrayList();
	static {
		cmsLink.add("http://www.cnmei.com/cms/category/yingxiao");
		cmsLink.add("http://www.cnmei.com/cms/category/cehuazhuanchang");
		cmsLink.add("http://www.cnmei.com/cms/category/wenanfenxi");
		cmsLink.add("http://www.cnmei.com/cms/category/fangjianfengchuan");
		cmsLink.add("http://www.cnmei.com/cms/category/guanggaochaoliu");
		cmsLink.add("http://www.cnmei.com/cms/category/guanggaocelue");
	}
	
	/**
	 * 更新数据
	 */
    public void update() { 
		logger.info("获取cms数据任务开始...");
		long start = System.currentTimeMillis();
        List<FindCnmei> data = Lists.newArrayList();
        try {
        	for(String link:cmsLink){
        		getUrlMarketingToData(data, link);
        	}
        	if(data.size()>0){
        		//清空原数据
    			findCnmeiService.deleteAll();
        		//保存新数据
        		findCnmeiService.save(data);
        	}
        	logger.info("**********获取cms数据任务结束，耗时：{} 毫秒", System.currentTimeMillis() - start);
		} catch (Exception e) {
			logger.error("访问cms失败！", e);
		}
    }  
	
	
	/**
	 * 获得链接内容
	 * @param data
	 * @param link
	 * @throws Exception
	 */
	protected void getUrlMarketingToData(List<FindCnmei> data, String link) throws Exception {
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
        		FindCnmei findCnmei = new FindCnmei();
        		String href = a.attr("href");
        		String title = a.childNodes().get(0).toString();
        		findCnmei.setUrl(href);
        		findCnmei.setTitle(title);
        		getUrlFirstImgAndTime(findCnmei,href);
        		data.add(findCnmei);
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
	
	/**
	 * 获得文章中的第一个图片
	 * @param link
	 * @return
	 * @throws Exception
	 */
	protected void getUrlFirstImgAndTime(FindCnmei findCnmei,String link) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Element img = null;
		Element time = null;
		String outLine = null;
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
        Element article = doc.getElementsByTag("article").get(0);
        if (null!=article) {
        	 if(null!=article.getElementsByTag("img")&&article.getElementsByTag("img").size()>0){
        		 img = article.getElementsByTag("img").get(0);
        		 findCnmei.setImg(img.attr("src"));
        	 }else{
        		 findCnmei.setImg("http://www.cnmei.com/cms/wp-content/uploads/2016/01/1453449099.jpg");
        	 }
        	 time = article.getElementsByTag("p").get(0);
        	 Date date=sdf.parse(time.text());
        	 findCnmei.setCreateTime(date);
        	 //文本从第二十位开始截掉隐藏的时间字段
        	 if(article.text().replace(" ", "").length()>19){
        		 outLine = article.text().substring(19);
        	 }else{
        		 outLine = findCnmei.getTitle();
        	 }
        	 findCnmei.setOutLine(outLine.length()>30 ?outLine.substring(0,30)+"..." :outLine);
        }
	} 
}  

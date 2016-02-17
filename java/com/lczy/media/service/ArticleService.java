package com.lczy.media.service;


import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lczy.common.data.JPAUtil;
import com.lczy.common.util.PropertyUtils;
import com.lczy.media.entity.Article;
import com.lczy.media.repositories.ArticleDao;


@Service
@Transactional(readOnly=true)
public class ArticleService {

	@Autowired
	private ArticleDao articleDao;


	public Article get(String id) {
		
		return articleDao.findOne(id);
	}
	
	@Transactional(readOnly=false)
	public Article save(Article article) {
		article.setContent(clean(article.getContent()));
		return articleDao.save(article);
	}
	
	private Cleaner cleaner;
	
	public ArticleService() {
		Whitelist wl = Whitelist.relaxed();
		wl.addAttributes(":all", "class");
		wl.addAttributes(":all", "style");
		cleaner = new Cleaner(wl);
	}

	/**
	 * 清理不合法html的标签.
	 * @param content 待处理的 html 文件内容.
	 * @return 干净的内容.
	 */
	private String clean(String content) {
		Document doc = Jsoup.parse(content, PropertyUtils.getProperty("jsoup.baseUri"));
		doc = cleaner.clean(doc);
		//为图片增加响应式类
		doc.select("img").addClass("img-responsive");
		
		return doc.body().html();
	}

	public Page<Article> find(Map<String, Object> searchParams, int page,
			int size, String sort) {
		Pageable pageable = JPAUtil.buildPageRequest(page, size, sort);
        Specification<Article> spec = JPAUtil.buildSpecification(searchParams);
                
        return articleDao.findAll(spec, pageable);
	}

}

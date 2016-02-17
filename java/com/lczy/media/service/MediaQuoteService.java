package com.lczy.media.service;

import java.io.IOException;
import java.io.Serializable;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lczy.common.service.AbstractService;
import com.lczy.media.entity.Media;
import com.lczy.media.entity.MediaQuote;
import com.lczy.media.repositories.MediaQuoteDao;
import com.lczy.media.solr.MediaDoc;
import com.lczy.media.solr.SolrService;

@Service
@Transactional(readOnly = true)
public class MediaQuoteService extends AbstractService<MediaQuote> {
	
	@Autowired
	private SolrService solrService;
	
	@Autowired
	private MediaQuoteDao mediaQuoteDao;
	
	@Transactional(readOnly = false)
	public MediaQuote save(MediaQuote quote) {
		MediaQuote mq = super.save(quote);
		save2Solr(mq);
		return mq;
	}
	
	private void save2Solr(MediaQuote mq) {
		Media m = mq.getMedia();
		MediaDoc doc = new MediaDoc(m);
		try {
			solrService.save(doc);
		} catch (SolrServerException | IOException e) {
			throw new RuntimeException("保存到solr发生异常", e);
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void remove(Serializable id) {
		MediaQuote mq = get(id);
		remove(mq);
		mq.getMedia().getMediaQuotes().remove(mq);
		
		save2Solr(mq);
	}
	
	/**
	 * 按照媒体和类型查找价格
	 * 
	 * @param media
	 * @param type
	 * @return
	 */
	public MediaQuote findByMediaAndType(Media media, String type) {
		return this.mediaQuoteDao.findTopByMediaAndType(media, type);
	}

}

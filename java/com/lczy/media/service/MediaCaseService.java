package com.lczy.media.service;

import java.io.IOException;
import java.io.Serializable;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lczy.common.service.AbstractService;
import com.lczy.media.entity.Media;
import com.lczy.media.entity.MediaCase;
import com.lczy.media.repositories.MediaCaseDao;
import com.lczy.media.solr.MediaDoc;
import com.lczy.media.solr.SolrService;

@Service
public class MediaCaseService extends AbstractService<MediaCase> {
	@Autowired
	private MediaCaseDao mediaCaseDao;
	
	@Autowired
	private SolrService solrService;
	
	@Transactional(readOnly = false)
	public MediaCase save(MediaCase quote) {
		MediaCase mc = super.save(quote);
		save2Solr(mc);
		return mc;
	}
	
	private void save2Solr(MediaCase mc) {
		Media m = mc.getMedia();
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
		MediaCase mc = get(id);
		remove(mc);
		mc.getMedia().getMediaCases().remove(mc);
		save2Solr(mc);
	}

}

package com.lczy.media.solr;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lczy.media.entity.Media;
import com.lczy.media.entity.MediaQuote;
import com.lczy.media.service.MediaQuoteService;
import com.lczy.media.service.MediaService;
import com.lczy.media.util.Constants;

@Service
public class IndexBuilder4Media {
	
	private Logger log = LoggerFactory.getLogger(IndexBuilder4Media.class);
	
	@Autowired
	private MediaQuoteService mediaQuoteService;
	
	@Autowired
	private MediaService mediaService;
	
	@Value("#{application['solr.baseUrl']}")
	private String baseURL;

	private void createIndexDataFromDb() throws Exception {
		Map<String, Object> searchParams = Maps.newLinkedHashMap();
		searchParams.put("EQ_status", Constants.MediaStatus.NORMAL);
		Collection<Media> medias = mediaService.find(searchParams);
		
		List<MediaDoc> docs = Lists.newArrayList();
		
		searchParams = Maps.newLinkedHashMap();
		for( Media m : medias ) {
			searchParams.put("EQ_media.id", m.getId());
			List<MediaQuote> quotes = mediaQuoteService.find(searchParams);
			m.setMediaQuotes(new HashSet<>(quotes));
			docs.add(new MediaDoc(m));
		}
		
		ConcurrentUpdateSolrClient client = new ConcurrentUpdateSolrClient(baseURL + MediaDoc.C_NAME, 500, 10);
		
		client.addBeans(docs);
		
		UpdateResponse resp = client.commit();
		
		log.debug(resp.toString());
		if( resp.getStatus() == 0 )
			log.info("成功创建 {} 个媒体的索引", docs.size());
				
		client.close();
	}
	
	private void cleanMediaData() throws Exception {
		SolrClient client = new HttpSolrClient(baseURL + MediaDoc.C_NAME);
		//SolrQuery query = new SolrQuery();
		client.deleteByQuery("*:*");
		client.commit();
		client.close();
	}
	
	public void rebuild() throws Exception {
		cleanMediaData();
		createIndexDataFromDb();
	}

}

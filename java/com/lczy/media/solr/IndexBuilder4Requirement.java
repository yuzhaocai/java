package com.lczy.media.solr;

import java.util.Collection;
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
import com.lczy.media.entity.Requirement;
import com.lczy.media.service.RequirementService;
import com.lczy.media.util.Constants;

@Service
public class IndexBuilder4Requirement {
	private Logger log = LoggerFactory.getLogger(IndexBuilder4Requirement.class);
	
	@Autowired
	private RequirementService requirementService;
	
	@Value("#{application['solr.baseUrl']}")
	private String baseURL;
	
	private void createIndexDataFromDb() throws Exception {
		Map<String, Object> searchParams = Maps.newLinkedHashMap();
		searchParams.put("EQ_status", Constants.ReqStatus.NORMAL);
		searchParams.put("EQ_isPublic", true);
		//searchParams.put("LIKE_name", "测试需求T");
		Collection<Requirement> dataList = requirementService.find(searchParams);
		
		List<RequirementDoc> docs = Lists.newArrayList();
		
		for( Requirement r : dataList ) {
			docs.add(new RequirementDoc(r));
		}
		
		ConcurrentUpdateSolrClient client = new ConcurrentUpdateSolrClient(baseURL + RequirementDoc.C_NAME, 500, 10);
		
		client.addBeans(docs);
		
		UpdateResponse resp = client.commit();
		
		log.debug(resp.toString());
		if( resp.getStatus() == 0 )
			log.info("成功创建 {} 个需求的索引", docs.size());
		
		client.close();
	}
	
	private void cleanIndexData() throws Exception {
		SolrClient client = new HttpSolrClient(baseURL + RequirementDoc.C_NAME);
		//SolrQuery query = new SolrQuery();
		client.deleteByQuery("*:*");
		client.commit();
		client.close();
	}
	
	public void rebuild() throws Exception {
		cleanIndexData();
		createIndexDataFromDb();
	}

}

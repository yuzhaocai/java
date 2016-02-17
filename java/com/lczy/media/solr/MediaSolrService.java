package com.lczy.media.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.stereotype.Service;

@Service
public class MediaSolrService extends SolrService {
	
	@Override
	protected void setBoosting(SolrQuery query) {
		query.set("qf", "name^1.9 tagNames^1.7 categoryName^1.5 industryTypeNames");
		query.set("bf", "sum(log(max(fans,1)),recip(sub(ms(),modifyTime),3.16e-11,1,1))^0.5");
	}

}

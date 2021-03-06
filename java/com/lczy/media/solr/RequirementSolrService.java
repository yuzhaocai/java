/**
 * 
 */
package com.lczy.media.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.stereotype.Service;

/**
 * @author wu
 *
 */
@Service
public class RequirementSolrService extends SolrService {

	@Override
	protected void setBoosting(SolrQuery query) {
		query.set("qf", "name^1.9 summary^1.5 industryTypeNames^1.3");
		query.set("bf", "sum(log(max(budget,1)),recip(sub(ms(),modifyTime),3.16e-11,1,1))^0.5");
	}
	
}

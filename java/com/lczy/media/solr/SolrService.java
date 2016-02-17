package com.lczy.media.solr;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
public class SolrService {
	
	public static final String Q_KEY = "name";
	
	protected Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("#{application['solr.baseUrl']}")
	private String baseUrl;
	
	public <T extends SolrBean> void save(T t) throws SolrServerException, IOException {
		SolrClient client = getSolrClient(t.getCName());
		try {
			UpdateResponse response = client.addBean(t);
			log.debug(response.toString());
			response = client.commit();
			log.debug(response.toString());
		} finally {
			client.close();
		}
	}
	
	public <T extends SolrBean> void save(List<T> beans) throws SolrServerException, IOException {
		if( beans == null || beans.isEmpty() )
			return;
		
		SolrClient client = getUpdateSolrClient(getCName(beans));
		try {
			client.addBeans(beans);
			client.commit();
		} finally {
			client.close();
		}
	}

	private <T extends SolrBean> String getCName(List<T> beans) {
		if( beans == null || beans.isEmpty() )
			throw new RuntimeException("集合不能为空");
		
		return beans.get(0).getCName();
	}
	
	public <T extends SolrBean> void delete(T t) throws SolrServerException, IOException {
		SolrClient client = getSolrClient(t.getCName());
		try {
			client.deleteById(t.getId());
			client.commit();
		} finally {
			client.close();
		}
	}
	
	/**
	 * @param cname core 名称
	 * @param id 文档 UUID
	 */
	public void delete(String cname, String id) throws SolrServerException, IOException {
		SolrClient client = getSolrClient(cname);
		try {
			client.deleteById(id);
			client.commit();
		} finally {
			client.close();
		}
	}

	protected SolrClient getSolrClient(String cname) {
		SolrClient client = new HttpSolrClient(baseUrl + cname);
		return client;
	}
	
	protected SolrClient getUpdateSolrClient(String cname) {
		SolrClient client = new ConcurrentUpdateSolrClient(baseUrl + cname, 5000, 10);
		return client;
	}
	
	public <T extends SolrBean> Page<T> find(Class<T> clazz, 
			Map<String, Object> fieldsMap,
			String sort,
			int pageNum, int size) throws Exception {
		
		SolrClient client = getSolrClient(clazz);
		SolrQuery query = new SolrQuery();
		
		if( fieldsMap.get(Q_KEY ) != null ) {
			String val = fieldsMap.get(Q_KEY).toString();
			if( StringUtils.isNotBlank(val) ) {
				query.set("defType","dismax");
				query.set("q", val.toString());
			} else {
				query.set("q", "*:*");
			}
			fieldsMap.remove(Q_KEY);
		} else {
			query.set("q", "*:*");
		}
		
		parseFieldsMap(fieldsMap, query);
		
		setBoosting(query);
		
		if( StringUtils.isNotBlank(sort) ) {
			query.set("sort", sort);
		}
		
		query.setStart((pageNum-1) * size);
		query.setRows(size);
		
		log.debug("===>> query: " + query);
		
		try {
			QueryResponse rsp = client.query(query);
			int total = (int) rsp.getResults().getNumFound();
			List<T> data = rsp.getBeans(clazz);
			Page<T> page = new Page<T>(data, total, pageNum, size);
			
			return page;
			
		} finally {
			client.close();
		}
	}

	/**
	 * @param fieldsMap
	 * @param query
	 */
	protected void parseFieldsMap(Map<String, Object> fieldsMap, SolrQuery query) {
		if( fieldsMap != null ) {
			List<String> fq = Lists.newArrayList();
			for( String field : fieldsMap.keySet() ) {
				Object value = fieldsMap.get(field);
				if( value instanceof Iterable<?> ) {
					Iterable<?> it = (Iterable<?>) value;
					StringBuilder sb = new StringBuilder(300);
					for( Object obj : it) {
						if(sb.length() > 0) {
							if( field.startsWith("-") )
								sb.append(" AND ");
							else
								sb.append(" OR ");
						}
						sb.append(field).append(":").append(obj);
					}
					fq.add(sb.toString());
				} else {
					fq.add(field + ":" + value);
				}
			}
			
			query.addFilterQuery(fq.toArray(new String[0]));
		}
	}

	protected <T extends SolrBean> SolrClient getSolrClient(Class<T> clazz) throws Exception {
		T t = clazz.newInstance();
		SolrClient client = new HttpSolrClient(baseUrl + t.getCName());
		return client;
	}
	
	/**
	 * 由子类实现，用于设置查询的字段权重.
	 * 
	 * @param query
	 */
	protected void setBoosting(SolrQuery query) {
		//nothing
	}
}

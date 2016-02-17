package com.lczy.media.solr;

public interface SolrBean {

	/**
	 * @return core（Standalone 模式） 或者 collection（SolrCloud 模式） 的名称。
	 */
	String getCName();
	
	/**
	 * @return 返回文档的 ID.
	 */
	String getId();
}

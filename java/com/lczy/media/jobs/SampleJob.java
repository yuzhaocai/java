/**
 * 
 */
package com.lczy.media.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class SampleJob {
	
	private static Logger log = LoggerFactory.getLogger(SampleJob.class);
	
	//@Autowired
	//private AccountService accountService; //注入你需要的服务
	
	@Autowired
	private UpdateFindCnmei updateFindCnmei; 
	
	@Autowired
	private HomeData homeData;
	
	public void work() {
		long start = System.currentTimeMillis();
		
		log.info("********** XX任务开始...{}");
		
		//TODO do youself
		
		log.info("********** XX任务结束，耗时：{} 毫秒", System.currentTimeMillis() - start);
	}
	
	/**
	 * 更新发现采媒
	 */
	public void findCnmeiWork(){
		updateFindCnmei.update();
	}
	
	/**
	 * 更新首页悬赏金额与成交数
	 */
	public void homeDataWork(){
		homeData.update();
	}
	
}

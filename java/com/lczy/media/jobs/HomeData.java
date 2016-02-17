package com.lczy.media.jobs;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lczy.media.entity.SysConfig;
import com.lczy.media.service.SysConfigService;

/**
 * 更新系统配置表中悬赏金额与成交数
 *
 */
@Service
public class HomeData {  
	
	private static Logger logger = LoggerFactory.getLogger(HomeData.class);
	
	@Autowired
	private SysConfigService sysConfigService;
	
	private static int[] array = {5000,6000,7000,8000,9000,10000};
	
	/**
	 * 更新数据
	 */
    public void update() { 
    	System.out.println("任务开始");
		logger.info("更新悬赏金额与成交数任务开始...");
		long start = System.currentTimeMillis();
		Random random=new Random(); 
		//随机1-5
		int num=random.nextInt(5)+1; 
		//随机0-5
		int index=random.nextInt(6); 
		//更新悬赏金额
		SysConfig offerSums = sysConfigService.get("OFFER_SUMS");
		offerSums.setValue(String.valueOf(Integer.parseInt(offerSums.getValue())+array[index]));
		sysConfigService.save(offerSums);
		//更新成交数
		SysConfig totalVolume = sysConfigService.get("TOTAL_VOLUME");
		totalVolume.setValue(String.valueOf(Integer.parseInt(totalVolume.getValue())+num));
		sysConfigService.save(totalVolume);
		logger.info("**********更新悬赏金额与成交数任务任务结束，耗时：{} 毫秒", System.currentTimeMillis() - start);
    }  
}  

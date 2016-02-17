package com.lczy.media.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lczy.common.util.DateUtil;
import com.lczy.media.entity.LotteryPrize;
import com.lczy.media.entity.LotteryTime;
import com.lczy.media.entity.SysConfig;
import com.lczy.media.service.LotteryService;
import com.lczy.media.service.SysConfigService;
import com.lczy.media.util.JsonBean;
import com.lczy.media.util.UserContext;

@Controller
@RequestMapping("/lottery")
public class LotteryController extends MediaController {
	
	@Autowired
	private LotteryService lotteryService;
	
	@Autowired
	private SysConfigService sysConfigService;
	
	
	/**
	 * 抽奖首页
	 * 
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"","/index" })
	public String index(Model model, HttpServletRequest request) throws Exception {
		String now = DateUtil.nowDateTimeString();
		if (now.compareTo(getStartDate()) < 0) {
			model.addAttribute("wait", true);
			model.addAttribute("start_time", getStartDate());
		}
		
		if (now.compareTo(getEndDate()) >= 0) {
			model.addAttribute("end", true);
		}
		
		if (UserContext.getCurrentCustomer() != null) {
			if (UserContext.isAdvertiser()) {
				LotteryTime times = lotteryService.getLotteryTime();
				model.addAttribute("times", times);
			}
		}
		
		List<LotteryPrize> data = lotteryService.findLotteryPrizes();
		model.addAttribute("data", data);
		
		return "lottery/index";
	}
	
	/**
	 * 抽奖，返回结果：
	 * {
	 * 		result: true-成功，false-失败
	 * 		message: 失败原因
	 * 		times：剩余抽奖次数
	 * 		prize：奖品，null-没有抽中
	 * 		{
	 * 			name：奖品名称
	 * 			category：奖品类型
	 * 		}
	 * }
	 * 
	 * @return
	 */
	@RequestMapping("start")
	@ResponseBody
	public JsonBean start() {
		JsonBean result = new JsonBean();
		
		try {
			if (!UserContext.isAdvertiser()) {
				throw new Exception("对不起，只有企业用户可以参与抽奖哦！");
			}
			
			String now = DateUtil.nowDateTimeString();
			if (now.compareTo(getStartDate()) < 0) {
				throw new Exception("抽奖时间还未开始，请稍候！");
			}
			
			if (now.compareTo(getEndDate()) >= 0) {
				throw new Exception("对不起，抽奖活动已经结束，请下次再来吧！");
			}
			
			LotteryPrize prize = lotteryService.startLottery();
			LotteryTime times = lotteryService.getLotteryTime();
			result.put("result", true);
			result.put("times", times.getTimes());
			if (prize != null) {
				JsonBean bean = new JsonBean();
				bean.put("name", prize.getName());
				bean.put("category", prize.getCategory());
				result.put("prize", bean);
			}
		} catch (ObjectOptimisticLockingFailureException e) {
			LotteryTime times = lotteryService.getLotteryTime();
			result.put("result", true);
			result.put("times", times.getTimes());
		} catch (Exception e) {
			e.printStackTrace();
			result.put("result", false);
			result.put("message", e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 查询抽奖开始日期
	 * 
	 * @return
	 */
	private String getStartDate() {
		SysConfig config = sysConfigService.get("LOTTERY_DATE");
		if (config == null) {
			return "2015-12-16 10:00:00";
		}
		return config.getValue();

	}
	
	/**
	 * 抽奖结束时间
	 * 
	 * @return
	 */
	private String getEndDate() {
		return "2015-12-17 00:00:00";
	}
}

/**
 * 
 */
package com.lczy.media.util;

/**
 * 信用奖励配置类.
 * 格式：[信用值, 虚拟币]
 * 
 * @author wu
 *
 */
public interface CreditConfig {
	
	/**
	 * 注册
	 */
	int[] ACT_REGISTER = new int[]{10, 2};
	
	/**
	 * 完善资料
	 */
	int[] ACT_PERFECT_INFO = new int[]{100, 0};
	
	/**
	 * 每日登录
	 */
	int[] ACT_LOGIN = new int[]{10, 0};
	
	/**
	 * 成功创建媒体
	 */
	int[] ACT_CREATE_MEDIA_SUCCESS = new int[]{100, 0};
	
	/**
	 * 成功发布需求
	 */
	int[] ACT_PUB_REQ_SUCCESS = new int[]{50, -2};
	
	/**
	 * 交易成功
	 */
	int[] ACT_TRANSACTION_SUCCESS = new int[]{50, 2};
	
	/**
	 * 媒体被屏蔽
	 */
	int[] ACT_DISABLED_MEDIA = new int[]{-50, 0};
	
	/**
	 * 需求被屏蔽
	 */
	int[] ACT_DISABLED_REQ = new int[]{-50, 0};
	
	/**
	 * 首次充值
	 */
	int[] ACT_FIRST_CHARGE = new int[]{100, 1};
	
	/**
	 * 推荐注册会员+1
	 */
	int[] ACT_RECOMMEND_MEMBER = new int[]{100, 2};
	
	/**
	 * 拒绝未改稿应邀媒体.
	 */
	int[] ACT_REJECT_NOT_CHANGE = new int[]{-50, 0};

}

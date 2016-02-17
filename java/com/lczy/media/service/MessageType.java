/**
 * 
 */
package com.lczy.media.service;

/**
 * 站内消息枚举类型.
 * 
 * @author wu
 *
 */
public enum MessageType {
	/**
	 * 广告主消息
	 */
	ADVERTISER_REQ_SUCCESS("需求发布成功", "site.message.advertiser.requirement.success"),
	ADVERTISER_REQ_FAILURE("需求发布失败", "site.message.advertiser.requirement.failure"),
	ADVERTISER_INVIT("邀约媒体应邀", "site.message.advertiser.invit"),
	ADVERTISER_INVIT_ACTIVE("抢单", "site.message.advertiser.invit.active"),
	ADVERTISER_ORDER_CREATE("交易订单生成", "site.message.advertiser.order.create"),
	ADVERTISER_DELIVER("媒体交付任务", "site.message.advertiser.deliver"),
	ADVERTISER_REMINDER_PAY("媒体催款通知", "site.message.advertiser.reminder.pay"),
	ADVERTISER_INVIT_REJECT("媒体拒绝邀约", "site.message.advertiser.invit.rejection"),
	ADVERTISER_ORDER_COMPLETE("订单完成", "site.message.advertiser.order.complete"),
	ADVERTISER_WITHDROW_SUCCESS("提现成功", "site.message.advertiser.withdraw.success"),
	ADVERTISER_CHARGE_SUCCESS("充值成功", "site.message.advertiser.charge.success"),
	ADVERTISER_REFUND_SUCCESS("退款成功", "site.message.advertiser.refund.success"),
	ADVERTISER_REMIND_FEEDBACK("收到催单回复", "site.message.advertiser.reminder.feedback"),
	
	
	/**
	 * 媒体主消息
	 */
	MEDIA_NEW_TASK("收到任务邀请", "site.message.media.new.task"),
	MEDIA_INVIT("应邀订单成功", "site.message.media.invit"),
	MEDIA_INVIT_ACTIVE("抢单成功", "site.message.media.invit.active"),
	MEDIA_REMINDER_FEEDBACK("收到催款回复", "site.message.media.reminder.feedback"),
	MEDIA_REMINDER("收到催单消息", "site.message.media.reminder"),
	MEDIA_REJECTION("拒付通知", "site.message.media.rejection"),
	MEDIA_ORDER_COMPLETE("订单完成", "site.message.media.order.complete"),
	MEDIA_CHARGE_SUCCESS("充值成功", "site.message.media.charge.success"),
	MEDIA_WITHDRAW_SUCCESS("提现成功", "site.message.media.withdrow.success"),
	MEDIA_ORDER_CANCEL("订单取消通知", "site.message.media.order.cancel"),
	
	/**
	 * 主动应征
	 */
	REMINDER_PAY("催款消息", "site.message.reminder.pay"),
	PASSWORD_CHANGE("密码修改成功", "site.message.password.change"),
	REFUND_SUCCESS("退款成功", "site.message.refund.success")
	;
	
	private String name;
	private String key;
	
	private MessageType(String name, String key) {
		this.name = name;
		this.key = key;
	}
	
	public String key() {
		return this.key;
	}
	
	public String toString() {
		return this.name;
	}
	
}

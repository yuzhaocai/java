/**
 * 
 */
package com.lczy.media.service;

import com.lczy.common.util.PropertyUtils;

/**
 * 站内信消息模板.
 * 
 * @author wu
 *
 */
public abstract class MessageTemplate {
	
	/**
	 * 获取系统配置的站内信模板信息.
	 * 
	 * @param messageKey 模板key
	 * @param args 消息的格式化参数
	 * @return 格式化后的模板消息.
	 */
	public static String get(String messageKey, Object... args) {
		String template = PropertyUtils.getProperty(messageKey);
		if( template == null)
			return "找不到 " + messageKey + " 对应的消息模板";
		
		return String.format(template, args);
	}

}

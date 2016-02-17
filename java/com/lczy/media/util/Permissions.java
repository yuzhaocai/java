package com.lczy.media.util;

import com.lczy.media.entity.ReqMedia;
import com.lczy.media.entity.Requirement;

/**
 * 数据权限工具类.
 * 
 * @author wu
 *
 */
public abstract class Permissions {
	
	/**
	 * 判断当前用户是否有权限操作参数对象.
	 * @param req
	 * @return 有权限则返回 true，否则返回 false.
	 */
	public static boolean isAllow(Requirement req) {
		return req.getCustomer().getId()
				.equals(UserContext.getCurrent().getCustomer().getId());
	}
	
	
	/**
	 * 判断当前用户是否有权限操作参数对象.
	 * @param item
	 * @return 有权限则返回 true，否则返回 false.
	 */
	public static boolean isAllow(ReqMedia item) {
		return item.getRequirement().getCustomer().getId()
				.equals(UserContext.getCurrent().getCustomer().getId());
	}
	
	/**
	 * 判断当前用户是否有权限操作参数对象.
	 * @param item
	 * @return 有权限则返回 true，否则返回 false.
	 */
	public static boolean isAllowDeal(ReqMedia item) {
		return item.getMedia().getCustomer().getId()
				.equals(UserContext.getCurrent().getCustomer().getId());
	}

}

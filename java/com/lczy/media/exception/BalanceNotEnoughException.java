/**
 * 
 */
package com.lczy.media.exception;

import com.lczy.common.exception.ServiceException;

/**
 * 
 * 
 * @author wu
 *
 */
public class BalanceNotEnoughException extends ServiceException {


	private static final long serialVersionUID = 2895862922054981694L;
	
	public BalanceNotEnoughException(String message) {
		super(message);
	}

}

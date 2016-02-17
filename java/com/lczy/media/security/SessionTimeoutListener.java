package com.lczy.media.security;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionTimeoutListener extends SessionListenerAdapter {
	
	private static final Logger log = LoggerFactory.getLogger(SessionTimeoutListener.class);
	
	@Override
	public void onExpiration(Session session) {
		log.debug("=====>> 会话超时: sessionId = {}", session.getId());
		invalidateToken(session);
	}


	@Override
	public void onStop(Session session) {
		log.debug("=====>> 会话停止: sessionId = {}", session.getId());
		invalidateToken(session);
	}
		
	private void invalidateToken(Session session) {
//		String token = getToken(session);
//		if (StringUtils.isNotBlank(token)) {
//			try {
//				session.removeAttribute(Constants.Security.SSO_TOKEN_KEY);
//				bossSSOService.logout(token);
//			} catch (IOException e) {
//				log.error("=====>> 注销token发生错误", e);
//			}
//		}
	}


}

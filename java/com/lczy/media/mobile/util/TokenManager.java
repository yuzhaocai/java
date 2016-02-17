/**
 * 
 */
package com.lczy.media.mobile.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.springframework.stereotype.Component;

/**
 * 客户端token管理器.
 * 
 * @author wu
 *
 */
@Component
public class TokenManager {

	private static Map<String, Token> cachedTokens;
	
	/**
	 * key=loginName, value=sessionId
	 */
	private static Map<String, String> cachedLoginNames;
	
	public TokenManager() {
		cachedTokens = new ConcurrentHashMap<>();
		cachedLoginNames = new ConcurrentHashMap<>();
	}
	
	public void save(Token token) {
		String loginName = token.getLoginName();
		if( isExists(loginName) ) {
			String sessionId = cachedLoginNames.get(loginName);
			cachedTokens.remove(sessionId);
			new Subject.Builder().sessionId(sessionId).buildSubject().logout();
		}
		
		String sessionId = token.getSessionId();
		cachedTokens.put(sessionId, token);
		cachedLoginNames.put(loginName, sessionId);
	}
	
	private boolean isExists(String loginName) {
		return cachedLoginNames.containsKey(loginName);
	}

	public Token getToken(String sessionId) {
		return cachedTokens.get(sessionId);
	}

	public void invalidate(String sessionId) {
		Token token = getToken(sessionId);
		cachedLoginNames.remove(token.getLoginName());
		cachedTokens.remove(sessionId);
		new Subject.Builder().sessionId(sessionId).buildSubject().logout();
		ThreadContext.unbindSubject();
	}
	
}

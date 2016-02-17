/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.lczy.media.security;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.Hibernates;
import org.springside.modules.utils.Encodes;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.lczy.common.util.Encrypts;
import com.lczy.media.entity.Function;
import com.lczy.media.entity.Role;
import com.lczy.media.entity.User;
import com.lczy.media.repositories.FunctionDao;
import com.lczy.media.repositories.RoleDao;
import com.lczy.media.repositories.UserDao;
import com.lczy.media.util.Constants.UserStatus;

@Component
public class ShiroDbRealm extends AuthorizingRealm {
	
	private static final Logger log = LoggerFactory.getLogger(ShiroDbRealm.class);

	/**
	 * 认证回调函数,登录时调用.
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		
		User user = null;
		try {
			
            user = getUserByLoginName(token.getUsername());
			
		} catch (Exception e) {
			log.error("Can't find by loginName: " + token.getUsername(), e);
		}
		
		if (user != null) {
			if ( !UserStatus.ENABLED.equals(user.getStatus()) ) {
				log.info("=====>> 账号：{}, 已经被禁用.", token.getUsername());
				throw new DisabledAccountException();
			}
			
			byte[] salt = Encodes.decodeHex(user.getSalt());
			return new SimpleAuthenticationInfo(new ShiroUser(user), user.getPassword(),
					ByteSource.Util.bytes(salt), getName());
		} else {
			return null;
		}
	}
	
	
	@Autowired
	private UserDao userDao;
	
	@Transactional(readOnly=true)
	private User getUserByLoginName(String loginName) throws Exception {
		
		User user = userDao.findByLoginName(loginName);
		if (user != null) {
			Hibernates.initLazyProperty(user.getRole().getFunctions());
		}
		return user;
		
	}
	

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
		User user = (User)shiroUser.getUser();
	
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		
        //基于Role的权限信息
        info.addRole(user.getRole().getRoleCode());
        //基于Permission的权限信息
        info.addStringPermissions(getPermissions(user.getRole()));
		
		return info;
	}

	/**
	 * 设定Password校验的Hash算法与迭代次数.
	 */
	@PostConstruct
	public void initCredentialsMatcher() {
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(Encrypts.HASH_ALGORITHM);
		matcher.setHashIterations(Encrypts.HASH_ITERATIONS);

		setCredentialsMatcher(matcher);
	}
	
	
	/**
	 * 检查用户名密码是否正确的回调方法.
	 */
	protected void assertCredentialsMatch(AuthenticationToken authToken, AuthenticationInfo info) throws AuthenticationException {
//		UsernamePasswordToken token = (UsernamePasswordToken)authToken;
//		String username = token.getUsername();
//		String password = new String(token.getPassword());

        super.assertCredentialsMatch(authToken, info);
	}


	/**
	 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
	 */
	public static class ShiroUser implements Serializable {
		private static final long serialVersionUID = -1373760761780840081L;
		
		private User user;

		public ShiroUser(User user) {
			this.user = user;
		}
		
		public User getUser() {
			return user;
		}

		/**
		 * 本函数输出将作为默认的<shiro:principal/>输出.
		 */
		public String toString() {
			return user.getNickname();
		}

		public int hashCode() {
			return user.hashCode();
		}
		
		public String getLoginName() {
			return user.getLoginName();
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			
			ShiroUser that = (ShiroUser) obj;
			
			return Objects.equal(this.getLoginName(), that.getLoginName());
		}
	}
	
	@Autowired
	private FunctionDao functionDao;
	
	@Autowired
	private RoleDao roleDao;
	
	/**
	 * 从数据库中查询角色拥有的权限.
	 * @param roleId 角色ID.
	 * @return 权限集合.
	 */
	private Collection<String> getPermissions(Role role) {

		List<String> perms = Lists.newArrayList();
		
		for(Function f : role.getFunctions()) {
			String perm = f.getPermission();
			if (StringUtils.isNotBlank(perm))
				perms.add(perm);
		}
		
		return perms;
	}	
}

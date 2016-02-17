/**
 * 
 */
package com.lczy.media.service;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lczy.common.exception.ServiceException;
import com.lczy.common.service.AbstractService;
import com.lczy.common.util.Encrypts;
import com.lczy.media.entity.Customer;
import com.lczy.media.entity.User;
import com.lczy.media.repositories.CustomerDao;
import com.lczy.media.repositories.UserDao;
import com.lczy.media.util.Constants.UserStatus;
import com.lczy.media.util.UserContext;

/**
 * @author wu
 *
 */
@Service
@Transactional(readOnly = true)
public class UserService extends AbstractService<User> {
	
	@Autowired
	private SysConfigService sysConfigService;
	
	@Autowired
	private CustomerDao customerDao;
	
	@Override
	public UserDao getDao() {
		return (UserDao) super.getDao();
	}

	@Transactional(readOnly = false)
	public User save(User user) {
		if (StringUtils.isBlank(user.getId())) {
			//创建用户时需要加密明文密码，修改用户信息时不修改密码
			entryptPassword(user);
		}
		return super.save(user);
	}
	
	/**
	 * 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
	 */
	private void entryptPassword(User user) {
		if (user.getPassword() == null)
			return;
		
		String[] hash = Encrypts.hashPassword(user.getPassword());
		user.setPassword(hash[0]);
		user.setSalt(hash[1]);
	}

	/**
	 * 更新用户的最近登录信息.
	 * 
	 * @param userId
	 * @param ip
	 * @return 更新的记录数。
	 */
	@Transactional(readOnly = false)
	public int updateUserLoginInfo(String userId, String ip) {
		return getDao().updateLoginInfo(userId, ip, new Date());
	}

	@Transactional(readOnly = false)
	public User changePassword(String id, String password) throws Exception {
		User user = get(id);
		String[] hash = Encrypts.hashPassword(password);
		
		user.setPassword(hash[0]);
		user.setSalt(hash[1]);
		
		getDao().save(user);
		
		if (user.getLoginName().equals("root")) {
			String value = Encrypts.encryptAES(password, UserContext.ENCODE_KEY);
			sysConfigService.set(UserContext.SYSTEM_PASSWORD_KEY, value);
		}
		
		return user;
	}
	
	/**
	 * 修改密码
	 * 
	 * @param id
	 * @param newpwd
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false)
	public User changePassword(String id, String oldpwd, String newpwd) throws Exception {
		User user = get(id);
		oldpwd = Encrypts.hashPassword(oldpwd, user.getSalt());
		if (!user.getPassword().equals(oldpwd)) {
			throw new ServiceException("旧密码不正确!");
		}
		
		String[] hash = Encrypts.hashPassword(newpwd);
		
		user.setPassword(hash[0]);
		user.setSalt(hash[1]);
		
		getDao().save(user);
		
		if (user.getLoginName().equals("root")) {
			String value = Encrypts.encryptAES(newpwd, UserContext.ENCODE_KEY);
			sysConfigService.set(UserContext.SYSTEM_PASSWORD_KEY, value);
		}
		
		return user;
	}
	
	/**
	 * 修改手机号码
	 * 
	 * @param id
	 * @param mobPhone
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false)
	public User changeMobPhone(String id, String mobPhone) throws Exception {
		if (this.getByLoginName(mobPhone) != null) {
			throw new ServiceException("手机号码已注册!");
		}
		// 修改user,包括loginName和secMobile
		User user = get(id);
		user.setLoginName(mobPhone);
		user.setSecMobile(mobPhone);
		this.save(user);
		
		// 修改customer,包括mobPhone
		Customer customer = user.getCustomer();
		customer.setMobPhone(mobPhone);
		customerDao.save(customer);
		return user;
	}


	public User getByLoginName(String loginName) {
		
		return getDao().findByLoginName(loginName);
	}

	public User getByNickname(String nickname) {
		
		return getDao().findTopByNickname(nickname);
	}

	@Transactional(readOnly = false)
	public void delete(String id) {
		getDao().delete(id);
	}
	

	/**
	 * 重置用户的密码为登录名称.
	 * 
	 * @param id
	 */
	@Transactional(readOnly = false)
	public void resetPassword(String id) {
		User user = get(id);
		String[] hash = Encrypts.hashPassword(user.getLoginName());
		String password = hash[0];
		String salt = hash[1];
		
		getDao().updatePassword(id, password, salt);
	}

	public User getByCustomerId(String id) {
		
		return getDao().findByCustomerId(id);
	}
	
	/**
	 * 设置用户状态为： 离职
	 * 
	 * @param id
	 */
	@Transactional(readOnly = false)
	public void dimission(String id) {
		User user = get(id);
		user.setStatus(UserStatus.DIMISSION);
		getDao().save(user);
	}
	
	/**
	 * 冻结用户
	 * 
	 * @param userId
	 * @param userStatus
	 * @return
	 */
	@Transactional(readOnly = false)
	public int freeze(String userId, String userStatus) {
		return getDao().freeze(userId, userStatus);
	}
	
	/**
	 * 解冻用户
	 * 
	 * @param userId
	 * @param userStatus
	 * @return
	 */
	@Transactional(readOnly = false)
	public int unfreeze(String userId, String userStatus) {
		return getDao().freeze(userId, userStatus);
	}
	
	/**
	 * 禁言用户
	 * 
	 * @param userId
	 * @param userSpeakStatus
	 * @return
	 */
	@Transactional(readOnly = false)
	public int banned(String userId, boolean userSpeakStatus) {
		return getDao().banned(userId, userSpeakStatus);
	}
	
	/**
	 * 解禁用户 
	 * 
	 * @param userId
	 * @param userSpeakStatus
	 * @return
	 */
	@Transactional(readOnly = false)
	public int unbanned(String userId, boolean userSpeakStatus) {
		return getDao().unbanned(userId, userSpeakStatus);
	}
}

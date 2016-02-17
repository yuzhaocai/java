/**
 * 
 */
package com.lczy.media.repositories;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.lczy.common.data.Dao;
import com.lczy.media.entity.User;

/**
 * @author wu
 *
 */
public interface UserDao extends Dao<User, Serializable>
{

	/**
	 *  按登录名查找用户.
	 * @param loginName
	 * @return 符合查询条件的第一个记录.
	 */
	@QueryHints({@QueryHint(name="org.hibernate.cacheable", value="true")})
	User findByLoginName(String loginName);

	/**
	 * 更新用户最近登录时间信息.
	 * 
	 * @param userId
	 * @param ip
	 * @param date
	 * @return 更新的记录数.
	 */
	@Modifying
	@Query("update User u set u.lastIp=u.latestIp, u.lastTime=u.latestTime, " +
			"u.latestIp=?2, u.latestTime=?3 where u.id=?1")
	int updateLoginInfo(String userId, String ip, Date date);
	
	
	/**
	 *  按昵称查找用户.
	 * @param loginName
	 * @return 符合查询条件的第一个记录.
	 */
	User findTopByNickname(String nickname);

	@Modifying
	@Query("update User u set u.password = ?2, u.salt = ?3 where u.id = ?1")
	int updatePassword(String id, String password, String salt);

	User findByCustomerId(String id);

	void deleteByCustomerId(String id);

	@Modifying
	@Query("update User u set u.loginName = ?1 where u.loginName = ?2")
	int updateLoginName(String newValue, String oldValue);
	
	@Modifying
	@Query("update User u set u.status = ?2 where u.id = ?1")
	int unfreeze(String userId, String userStatus);

	@Modifying
	@Query("update User u set u.status = ?2 where u.id = ?1")
	int freeze(String userId, String userStatus);
	
	@Modifying
	@Query("update User u set u.shutup = ?2 where u.id = ?1")
	int banned(String userId, boolean userSpeakStatus);
	
	@Modifying
	@Query("update User u set u.shutup = ?2 where u.id = ?1")
	int unbanned(String userId, boolean userSpeakStatus);
	

}

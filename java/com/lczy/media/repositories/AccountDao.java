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
import com.lczy.media.entity.Account;

/**
 * @author wu
 *
 */
public interface AccountDao extends	Dao<Account, Serializable>
{

	@QueryHints({@QueryHint(name="org.hibernate.cacheable", value="true")})
	Account findByCustomerId(String id);

	/**
	 * 更改账户余额
	 * 
	 * @param accountId 账户id
	 * @param amount    变化金额
	 * @param modifyBy  
	 * @param modifyTime
	 * @return
	 */
	@Modifying
	@Query(value="update Account l set l.avBalance = l.avBalance + (?2), l.modifyBy = ?3, l.modifyTime = ?4 where l.id = ?1")
	int incBalance(String accountId, int amount, String modifyBy, Date modifyTime);
}

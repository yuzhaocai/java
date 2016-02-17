/**
 * 
 */
package com.lczy.media.repositories;

import java.io.Serializable;
import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.lczy.common.data.Dao;
import com.lczy.media.entity.Customer;

/**
 * @author wu
 *
 */
public interface CustomerDao extends Dao<Customer, Serializable>
{

	Slice<Customer> findByMobPhone(String mobPhone, Pageable pageable);

	Customer findByName(String name);

	@QueryHints({@QueryHint(name="org.hibernate.cacheable", value="true")})
	List<Customer> findByCustTypeOrderByName(String custOrg);

	/**
	 * 设置会员的信息值.
	 * @param id 改变信用值的会员ID
	 * @param credit 增加的信用值
	 * @return 更新的记录数
	 */
	@Modifying
	@Query("update Customer c set c.credit = c.credit + ?2 where c.id = ?1")
	int setCredit(String id, int credit);
	
	/**
	 * 设置会员的实名认证通过
	 * 
	 * @param customerId 会员ID
	 * @param certified 是否认证
	 * @param certStatus 认证状态
	 */
	@Modifying
	@Query("update Customer c set c.certStatus = ?2 where c.id = ?1")
	int customerPass(String customerId, String certStatus);
	
	/**
	 * 设置会员的实名认证不通过
	 * 
	 * @param customerId 会员ID
	 * @param certStatus 认证状态
	 */
	@Modifying
	@Query("update Customer c set c.certStatus = ?2 where c.id = ?1")
	int customerUnPass(String customerId, String certStatus);
}

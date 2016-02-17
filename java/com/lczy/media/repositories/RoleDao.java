/**
 * 
 */
package com.lczy.media.repositories;

import java.io.Serializable;

import com.lczy.common.data.Dao;
import com.lczy.media.entity.Role;

/**
 * @author wu
 *
 */
public interface RoleDao extends Dao<Role, Serializable>
{

	Role findTopByRoleCode(String code);

	Role findTopByRoleName(String name);
	
}

/**
 * 
 */
package com.lczy.media.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lczy.common.service.AbstractService;
import com.lczy.media.entity.Function;
import com.lczy.media.entity.Role;
import com.lczy.media.repositories.FunctionDao;
import com.lczy.media.repositories.RoleDao;
import com.lczy.media.security.ShiroCacheManager;

/**
 * @author wu
 *
 */
@Service
@Transactional(readOnly = true)
public class RoleService extends AbstractService<Role> {
	
	@Autowired
	private FunctionDao functionDao;
	
	@Autowired
	private ShiroCacheManager shiroCacheManager;
	
	@Transactional(readOnly = false)
	public Role save(Role role) {
		getDao().save(role);
		
		shiroCacheManager.clear();
		
		return role;
	}

	public List<Role> findAll(Sort sort) {
		return getDao().findAll(null, sort);
	}

	public Role getByRoleCode(String code) {
		return getDao().findTopByRoleCode(code);
	}
	
	public Role getRole(String id) {
		return get(id);
	}

	@Transactional(readOnly=false)
	public void saveRole(Role role) {
		save(role);
	}

	public Role findByRoleName(String name) {
		
		return getDao().findTopByRoleName(name);
	}
	
	/**
	 * 根据角色编码获取角色对象.
	 * 
	 * @param code
	 * @return 角色.
	 */
	public Role findByRoleCode(String code) {
		
		return getDao().findTopByRoleCode(code);
	}


	/**
	 * 删除角色.
	 * @param roleId
	 * @throws Exception 
	 */
	@Transactional(readOnly = false)
	public void delete(String roleId) throws Exception {
		Role role = get(roleId);
		role.setUsers(null);
		role.getFunctions().clear();
		
		getDao().delete(roleId);
		
/*		//1、删除用户角色关系
		UserRoleCriteria userRoleCriteria = new UserRoleCriteria();
		UserRoleCriteria.Criteria urc = userRoleCriteria.createCriteria();
		urc.andRoleIdEqualTo(roleId);
		userRoleMapper.deleteByExample(userRoleCriteria);
		
		//2、删除角色功能关系
		RoleFuncCriteria roleFuncCriteria = new RoleFuncCriteria();
		RoleFuncCriteria.Criteria rfc = roleFuncCriteria.createCriteria();
		rfc.andRoleIdEqualTo(roleId);
		roleFuncMapper.deleteByExample(roleFuncCriteria);
		
		//3、最后删除角色
		deleteByPrimaryKey(Role.class, roleId);*/
		
		
		shiroCacheManager.clear();
		
	}

	/**
	 * 批量删除角色.
	 * @param ids 角色ID数组.
	 * @throws Exception 
	 */
	@Transactional(readOnly = false)
	public void delete(String[] ids) throws Exception {
		for(String id : ids) {
			delete(id);
		}
		
		shiroCacheManager.clear();
	}

	
	/**
	 * 保存角色功能关系.
	 * 
	 * @param id
	 * @param funcIds
	 */
	@Transactional(readOnly = false)
	public void saveRoleFunc(String id, List<String> funcIds) {
		Role role = getRole(id);
		role.getFunctions().clear();
		Iterable<Function> funcs = functionDao.findAll(funcIds);
		for (Function f : funcs) {
			role.getFunctions().add(f);
		}
		
		save(role);
	}

	@Override
	public RoleDao getDao() {
		
		return (RoleDao)super.getDao();
	}

	
}

/**
 * 
 */
package com.lczy.media.repositories;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.QueryHints;

import com.lczy.common.data.Dao;
import com.lczy.media.entity.Function;

/**
 * @author wu
 *
 */
public interface FunctionDao extends Dao<Function, String>
{

	@QueryHints({@QueryHint(name="org.hibernate.cacheable", value="true")})
	List<Function> findByParentId(String pid, Sort sort);

	int countByParentId(String pid);

	@QueryHints({@QueryHint(name="org.hibernate.cacheable", value="true")})
	Function findByFuncNameAndParentId(String funcName, String pId);

}

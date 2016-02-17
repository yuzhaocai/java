/**
 * 
 */
package com.lczy.media.repositories;

import java.util.List;

import com.lczy.common.data.Dao;
import com.lczy.media.entity.Dic;

/**
 * @author wu
 *
 */
public interface DicDao extends	Dao<Dic, String>
{

	Dic findTopByDicName(String dicName);

	Dic findTopByDicCode(String dicCode);

	List<Dic> findByParentId(String pId);

}

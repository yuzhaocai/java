/**
 * 
 */
package com.lczy.media.repositories;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

import com.lczy.common.data.Dao;
import com.lczy.media.entity.DicItem;

/**
 * @author wu
 *
 */
public interface DicItemDao extends
		Dao<DicItem, String>
{

	List<DicItem> findByDicId(String dicId, Sort sort);

	
	@Query("select max(i.seqNum) from DicItem i where i.dic.id=?1")
	Integer getDicItemMaxSeqNum(String dicId);


	DicItem findTopByItemNameAndDicId(String itemName, String dicId);


	DicItem findTopByItemCode(String itemCode);

	DicItem findTopByItemName(String itemName);

}

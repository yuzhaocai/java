package com.lczy.media.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lczy.media.entity.Dic;
import com.lczy.media.entity.DicItem;
import com.lczy.media.repositories.DicDao;
import com.lczy.media.repositories.DicItemDao;
import com.lczy.media.service.common.DicProvider;

@Component
@Transactional(readOnly = true)
public class DicService {
	
	@Autowired
	private DicDao dicDao;
	
	@Autowired
	private DicItemDao dicItemDao;
	
	@Autowired
	private DicProvider dicProvider;
	
	@Transactional(readOnly = false)
	public void saveDic(Dic dic) throws Exception {
		dicDao.save(dic);
		dicProvider.reload();
	}

	public Dic getDic(String dicId) throws Exception {
		return dicDao.findOne(dicId);
	}
	
	public DicItem getDicItem(String itemId) throws Exception {
		return dicItemDao.findOne(itemId);
	}

	public Dic findDicByName(String dicName) {
		
		return dicDao.findTopByDicName(dicName);
	}

	public Dic findDicByCode(String dicCode) {
		
		return dicDao.findTopByDicCode(dicCode);
	}

	@Transactional(readOnly = false)
	public void deleteDic(String dicId) throws Exception {
		dicDao.delete(dicId);
		
		dicProvider.reload();
	}

	public List<Dic> findDicByPId(String pId) {
		
		return dicDao.findByParentId(pId);
	}
	
	/**
	 * 根据codeType查询DICITEM
	 * @ClassName:DicService.java
	 * @Author:Li.Xiaochao
	 * @Description:
	 * @CreateDate:2014年10月30日
	 */
	public List<DicItem> findItemByCodeType(String codeType) {
		String dicId=findDicByCode(codeType).getId();
		return findItemByDicId(dicId);
	}
	
	public List<DicItem> findItemByDicId(String dicId) {
		
		return dicItemDao.findByDicId(dicId, new Sort("seqNum"));
	}
	
	
	public List<DicItem> findAllItem() {
		return dicItemDao.findAll(null, new Sort("seqNum"));
	}
	

	@Transactional(readOnly = false)
	public void saveDicItem(DicItem item) throws Exception {
		boolean exist = true;
		if (StringUtils.isBlank(item.getId())) {
			item.setId(null);
			exist = false;
			if (null == item.getSeqNum()) {
				//给字典项保存时排序字段设置值
				Integer maxSeqNum=dicItemDao.getDicItemMaxSeqNum(item.getDic().getId());
				if(null==maxSeqNum){
					item.setSeqNum(10);
				}else{
					item.setSeqNum((maxSeqNum+10));
				}
			}
		} 
		
		Dic dic = getDic(item.getDic().getId());
		item.setDic(dic);
		item = dicItemDao.save(item);
		if (!exist) {
			dic.getDicItems().add(item);
		}
		
		dicProvider.reload();
	}

	@Transactional(readOnly = false)
	public void deleteDicItem(String itemId) throws Exception {
		DicItem item = getDicItem(itemId);
		item.getDic().getDicItems().remove(item);//从集合缓存中删除
		dicItemDao.delete(item);
		
		dicProvider.reload();
	}

	public DicItem findItemByNameAndDicId(String itemName, String dicId) {
		
		return dicItemDao.findTopByItemNameAndDicId(itemName, dicId);
	}

	public DicItem findItemByCode(String itemCode) {
		
		return dicItemDao.findTopByItemCode(itemCode);
	}
	
	public DicItem findByItemName(String itemName) {
		
		return dicItemDao.findTopByItemName(itemName);
	}
	

}

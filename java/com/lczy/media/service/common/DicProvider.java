package com.lczy.media.service.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.lczy.media.entity.Dic;
import com.lczy.media.entity.DicItem;
import com.lczy.media.repositories.DicDao;

@Component
@Lazy
public class DicProvider extends AbstractProvider {
	
	@Autowired
	private DicDao dicDao;
	
	public DicProvider() {
		Providers.add(this);
	}
	
	
	/**
	 * key = itemCode.
	 */
	private Map<String, DicItem> itemMap = null;
	
	/**
	 * key = dicCode.
	 */
	private Map<String, Dic> dicMap = null;
	

	/**
	 * key = itemCode.
	 */
	public Map<String, DicItem> getItemMap() {
		tryLoad();
		return itemMap;
	}

	public Map<String, Dic> getDicMap() {
		tryLoad();
		return dicMap;
	}


	
	protected void load() {
		
		clear();
		
		Iterable<Dic> dics = dicDao.findAll();
		for (Dic dic : dics) {
			dicMap.put(dic.getDicCode(), dic);
			for(DicItem item : dic.getDicItems()) {
				addItem(item);
			}
		}
	}

	public void clear() {
		dicMap = new HashMap<>();
		itemMap = new HashMap<>();
	}

	private void addItem(DicItem item) {
		itemMap.put(item.getItemCode(), item);
	}

	
	/**
	 * 从缓存查询字典
	 * @param dicCode 字典编码
	 * @return 字典名称
	 */
	public String getDicName(String dicCode) {
		Dic dic = getDicMap().get(dicCode);
		
		return dic != null ? dic.getDicName() : dicCode;
	}
	
	/**
	 * 从缓存查询字典项.
	 * @param itemCode 字典项编码
	 * @return 字典项名称
	 */
	public String getDicItemName(String itemCode) {
		DicItem item = getItemMap().get(itemCode);
		String name = itemCode;
		if( item == null ) {
			if( "ALL".equalsIgnoreCase(itemCode) )
				name = "全部";
		} else {
			name = item.getItemName();
		}
		return name;
	}
	
	/**
	 * 从缓存查询字典。
	 * @param dicCode 字典编码
	 */
	public Dic getDic(String dicCode) {
		return getDicMap().get(dicCode);
	}
	
	/**
	 * 从缓存查询字典项.
	 * @param itemCode 字典项编码
	 */
	public DicItem getDicItem(String itemCode) {
		return getItemMap().get(itemCode);
	}
	
	public String getItemNames(List<String> codes) {
		if( codes == null || codes.size() == 0 )
			return "";
		
		StringBuilder sb = new StringBuilder(100);
		for( String code : codes ) {
			if( sb.length() > 0 ) {
				sb.append(",");
			}
			sb.append(getDicItemName(code));
		}
		return sb.toString();
	}
}

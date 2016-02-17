package com.lczy.media.entity;

// Generated 2015-3-27 18:53:05 by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * DicItem generated by hbm2java
 */
@Entity
@Table(name = "c_dic_item")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class DicItem implements Cloneable {
	
	private String id;
	private Dic dic;
	private String itemCode;
	private String itemName;
	private Integer seqNum;

	public DicItem() {
	}
	
	public DicItem(String itemCode, String itemName) {
		this(null, null, itemCode, itemName);
	}

	public DicItem(String id, Dic dic, String itemCode, String itemName) {
		this(null, null, itemCode, itemName, 0);
	}

	public DicItem(String id, Dic dic, String itemCode, String itemName, Integer seqNum) {
		this.id = id;
		this.dic = dic;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.seqNum = seqNum;
	}

	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DIC_ID", nullable = false)
	public Dic getDic() {
		return this.dic;
	}

	public void setDic(Dic dic) {
		this.dic = dic;
	}

	@Column(name = "ITEM_CODE", nullable = false, length = 20)
	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	@Column(name = "ITEM_NAME", nullable = false, length = 128)
	public String getItemName() {
		return this.itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	@Column(name = "SEQ_NUM", precision = 6, scale = 0)
	public Integer getSeqNum() {
		return this.seqNum;
	}

	public void setSeqNum(Integer seqNum) {
		this.seqNum = seqNum;
	}

	@Override
	public DicItem clone() {
		DicItem clone = null; 
        try{ 
            clone = (DicItem) super.clone();
        }catch(CloneNotSupportedException e){ 
            throw new RuntimeException(e); // won't happen 
        }
          
        return clone; 
	}

	@Override
	public String toString() {
		return "DicItem{itemCode=" + itemCode + ", itemName=" + itemName + "}";
	}

	
}

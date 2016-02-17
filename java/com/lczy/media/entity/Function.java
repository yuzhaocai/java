package com.lczy.media.entity;

// Generated 2015-3-27 18:53:05 by Hibernate Tools 4.3.1

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * Function generated by hbm2java
 */
@Entity
@Table(name = "c_function")
@Cacheable
public class Function {

	private String id;
	private String funcName;
	private Function parent;
	private String action;
	private String permission;
	private Boolean isMenu;
	private Integer seqNum;
	private Set<Role> roles = new HashSet<Role>(0);
	private Collection<Function> children = new ArrayList<>(0);

	public Function() {
	}

	public Function(String id, String funcName, String action) {
		this.id = id;
		this.funcName = funcName;
		this.action = action;
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

	@Column(name = "FUNC_NAME", nullable = false, length = 128)
	public String getFuncName() {
		return this.funcName;
	}

	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_ID")
	public Function getParent() {
		return this.parent;
	}

	public void setParent(Function parent) {
		this.parent = parent;
	}

	@Column(name = "ACTION", nullable = false, length = 4000)
	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Column(name = "PERMISSION", length = 128)
	public String getPermission() {
		return this.permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	@Column(name = "IS_MENU", precision = 1, scale = 0)
	public Boolean getIsMenu() {
		return this.isMenu;
	}

	public void setIsMenu(Boolean isMenu) {
		this.isMenu = isMenu;
	}

	@Column(name = "SEQ_NUM", precision = 6, scale = 0)
	public Integer getSeqNum() {
		return this.seqNum;
	}

	public void setSeqNum(Integer seqNum) {
		this.seqNum = seqNum;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "c_role_func", 
		joinColumns = { @JoinColumn(name = "FUNC_ID") }, 
		inverseJoinColumns = { @JoinColumn(name = "ROLE_ID") })
	public Set<Role> getRoles() {
		return this.roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
	public Collection<Function> getChildren() {
		return children;
	}

	public void setChildren(Collection<Function> children) {
		this.children = children;
	}

}

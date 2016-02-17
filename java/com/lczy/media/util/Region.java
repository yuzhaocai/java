/**
 * 
 */
package com.lczy.media.util;

import java.util.List;

import com.google.common.collect.Lists;
import com.lczy.media.entity.Area;

/**
 * 表示地区，如华南、华北...。
 * 
 * @author wu
 *
 */
public class Region {
	
	public static class Province {
		private Area a;
		private List<Area> cities;
		
		public Province(Area a) {
			this.a = a;
			this.cities = Lists.newArrayList();
		}

		public List<Area> getCities() {
			return cities;
		}

		public void setCities(List<Area> cities) {
			this.cities = cities;
		}
		
		public void add(Area a) {
			this.cities.add(a);
		}

		public String getId() {
			return a.getId();
		}

		public String getName() {
			return a.getName();
		}

		public String getLevel() {
			return a.getLevel();
		}

		public String getParentId() {
			return a.getParentId();
		}

		public String getRegionName() {
			return a.getRegionName();
		}
		
		
	}
	
	private String name;
	
	private List<Province> provinces;
	
	public Region(String name) {
		this.name = name;
		this.provinces = Lists.newArrayList();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Province> getProvinces() {
		return provinces;
	}

	public void setProvinces(List<Province> provinces) {
		this.provinces = provinces;
	}
	
	public void add(Province p) {
		this.provinces.add(p);
	}

}

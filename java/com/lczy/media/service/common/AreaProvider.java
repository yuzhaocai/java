package com.lczy.media.service.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lczy.media.entity.Area;
import com.lczy.media.repositories.AreaDao;
import com.lczy.media.service.AreaService.Level;
import com.lczy.media.util.Region;
import com.lczy.media.util.Region.Province;

@Component
@Lazy
public class AreaProvider extends AbstractProvider {
	
	@Autowired
	private AreaDao areaDao;
	
	public AreaProvider() {
		Providers.add(this);
	}
	
	/**
	 * 缓存的Area对象, 以id为key.
	 */
	private Map<String, Area> areaMap = Maps.newLinkedHashMap();
	
	/**
	 * 以省份名称为 key.
	 */
	private Map<String, List<Area>> provinceMap = Maps.newLinkedHashMap();
	
	/**
	 * @return 以id为key的map.
	 */
	public Map<String, Area> getAreaMap() {
		
		tryLoad();
		
		return this.areaMap;
	}
	
	private List<Region> regions = Lists.newArrayList();
	
	private List<Area> hotCities = Lists.newArrayList();

	@Override
	protected void load() {
		clear();
		Iterable<Area> list = areaDao.findAll();
		for(Area a : list) {
			areaMap.put(a.getId(), a);
		}
		
		populate();
		
		populateRegions();
	}

	private void populate() {
		Collection<Area> areas = areaMap.values();
		for( Area a : areas ) {
			int level = Integer.parseInt(a.getLevel());
			if( level == Level.CITY.ordinal() ) {
				Area p = areaMap.get(a.getParentId());
				String provinceName = p.getName();
				if( provinceMap.get(provinceName) == null) {
					provinceMap.put(provinceName, new ArrayList<Area>());
				}
				provinceMap.get(provinceName).add(a);
				if( a.isHot() ) {
					provinceMap.get(HOT_CITIES_KEY).add(a);
					hotCities.add(a);
				}
			} else if( level == Level.PROVINCE.ordinal() ) {
				String provinceName = a.getName();
				if( provinceMap.get(provinceName) == null) {
					provinceMap.put(provinceName, new ArrayList<Area>());
				}
				
				if( !provinceName.equals("直辖市") )
					provinceMap.get(provinceName).add(a);
			}
		}
	}
	
	@Override
	public void clear() {
		areaMap.clear();
		provinceMap.clear();
		provinceMap.put(HOT_CITIES_KEY, new ArrayList<Area>());
		regions.clear();
	}
	
	public static final String HOT_CITIES_KEY = "热门城市";

	public Map<String, List<Area>> getProvinceMap() {
		tryLoad();
		return provinceMap;
	}
	
	public String getAreaNames(List<String> ids) {
		if( ids == null || ids.size() == 0 ) {
			return "全国";
		}
		StringBuilder sb = new StringBuilder(100);
		for( String id : ids ) {
			if( sb.length() > 0 ) {
				sb.append(",");
			}
			if( "ALL".equalsIgnoreCase(id) ) {
				sb.append("全国");
			} else {
				Area a = getAreaMap().get(id);
				if( a != null )
					sb.append(a.getName());
			}
		}
		return sb.toString();
	}
	
	private void populateRegions() {
		Collection<Area> areas = areaMap.values();
		Map<String, Province> cachedProvince = Maps.newHashMap();
		Map<String, Region> cachedRegion = Maps.newHashMap();
		
		for( Area a : areas ) {
			int level = Integer.parseInt(a.getLevel());
			if( level == Level.CITY.ordinal() ) { //城市
				Province p = getProvince(a.getParentId(), cachedProvince);
				if (p != null ) {
					p.add(a);
				}
			} else if( level == Level.PROVINCE.ordinal() ) { //省份
				if ( StringUtils.isNotBlank(a.getRegionName()) ) {
					Region region = getRegion(a.getRegionName(), cachedRegion);
					Province p = getProvince(a.getId(), cachedProvince);
					region.add(p);
				}
			}
		}
	}
	
	private Province getProvince(String pid, Map<String, Province> cachedProvince) {
		Province p = cachedProvince.get(pid);
		if( p == null ) {
			Area area = areaMap.get(pid);
			p = new Province(area);
			cachedProvince.put(p.getId(), p);
		}
		return p;
	}
	
	private Region getRegion(String regionName, Map<String, Region> cachedRegion) {
		Region region = cachedRegion.get(regionName);
		if (region == null) {
			region = new Region(regionName);
			cachedRegion.put(regionName, region);
			regions.add(region);
		}
		return region;
	}

	public List<Region> getRegions() {
		tryLoad();
		return regions;
	}
	
	public List<Area> getHotCities() {
		tryLoad();
		return hotCities;
	}
	
}

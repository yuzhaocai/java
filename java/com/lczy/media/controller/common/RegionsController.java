/**
 * 
 */
package com.lczy.media.controller.common;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.mapper.JsonMapper;

import com.google.common.collect.Lists;
import com.lczy.media.entity.Area;
import com.lczy.media.service.common.AreaProvider;
import com.lczy.media.util.JsonBean;

/**
 * 地区公共 controller.
 * 
 * @author wu
 *
 */
@Controller
public class RegionsController {
	
	@Autowired
	private AreaProvider areaProvider;
	
	@RequestMapping("/common/regions")
	@ResponseBody
	public String regions() {
		List<JsonBean> regions = Lists.newArrayList();
		
		Set<String> provinces = areaProvider.getProvinceMap().keySet();
		for( String name : provinces ) {
			JsonBean region = new JsonBean();
			region.put("group", name);
			List<JsonBean> cities = Lists.newArrayList();
			for( Area a : areaProvider.getProvinceMap().get(name) ) {
				JsonBean city = new JsonBean();
				city.put("id", a.getId());
				city.put("name", a.getName());
				cities.add(city);
			}
			region.put("cities", cities);
			regions.add(region);
		}
		
		return JsonMapper.nonEmptyMapper().toJson(regions);
	}
	
	@RequestMapping("/common/selectRegionsModal")
	public String selectRegionsModal(Model model) {
		model.addAttribute("hotCities", areaProvider.getHotCities());
		model.addAttribute("regions", areaProvider.getRegions());
		
		return "common/selectRegionsModal";
	}
	@RequestMapping("/common/selectRegionModal")
	public String selectRegionModal(Model model) {
		model.addAttribute("regions", areaProvider.getRegions());
		return "common/selectRegionModal";
	}
}

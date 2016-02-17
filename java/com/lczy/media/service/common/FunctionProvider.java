package com.lczy.media.service.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.lczy.media.entity.Function;
import com.lczy.media.repositories.FunctionDao;

@Component
@Lazy
public class FunctionProvider extends AbstractProvider {

	@Autowired
	private FunctionDao functionDao;
	
	private Map<String, String> actionPermissionMap;
	
	private List<String> actions;
	
	public FunctionProvider() {
		Providers.add(this);
	}
	
	
	protected void load() {

		clear();
		
		Set<String> set = new HashSet<>();
		
		Iterable<Function> funcs = functionDao.findAll();
		for (Function f : funcs) {
			actionPermissionMap.put(f.getAction(), f.getPermission());
			set.add(f.getAction());
		}
		
		actions.addAll(set);
		Collections.sort(actions);
		Collections.reverse(actions);

	}
	
	public void clear() {
		actionPermissionMap = new HashMap<>();
		actions = new ArrayList<>();
	}

	public List<String> getActions() {
		tryLoad();
		return actions;
	}

	public Map<String, String> getActionPermissionMap() {
		tryLoad();
		return actionPermissionMap;
	}

}

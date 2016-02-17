package com.lczy.media.util;

import org.springside.modules.mapper.JsonMapper;

public class JSONUtil {
	
	public static String toJson(Object obj) {
		return JsonMapper.nonEmptyMapper().toJson(obj);
	}
}

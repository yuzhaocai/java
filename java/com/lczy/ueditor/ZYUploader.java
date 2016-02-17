package com.lczy.ueditor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.baidu.ueditor.define.State;

public class ZYUploader {
	private HttpServletRequest request = null;
	private Map<String, Object> conf = null;

	public ZYUploader(HttpServletRequest request, Map<String, Object> conf) {
		this.request = request;
		this.conf = conf;
	}

	public final State doExec() {
		String filedName = (String) this.conf.get("fieldName");
		State state = null;

		if ("true".equals(this.conf.get("isBase64"))) {
			state = ZYBase64Uploader.save(this.request.getParameter(filedName),
					this.conf);
		} else {
			state = ZYBinaryUploader.save(this.request, this.conf);
		}

		return state;
	}
}

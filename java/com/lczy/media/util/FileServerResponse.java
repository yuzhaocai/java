package com.lczy.media.util;

import java.util.HashMap;
import java.util.Map;

public class FileServerResponse {
	private boolean success;
	
	private Map<String, String> infoMap = new HashMap<>();
	
	private Entity entity = new Entity();
	
	public static class Entity {
		private String id;
		private int size;
		private String reason;
		
		/**
		 * @return 文件唯一标志.
		 */
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		
		/**
		 * @return 文件大小.
		 */
		public int getSize() {
			return size;
		}
		public void setSize(int size) {
			this.size = size;
		}
		
		/**
		 * @return 上传失败原因.
		 */
		public String getReason() {
			return reason;
		}
		public void setReason(String reason) {
			this.reason = reason;
		}
		
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	
	public String getInfo(String key) {
		return infoMap.get(key);
	}
	
	public void setInfo(String key, String value) {
		infoMap.put(key, value);
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}
}

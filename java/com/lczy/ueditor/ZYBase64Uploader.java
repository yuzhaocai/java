package com.lczy.ueditor;

import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.fileupload.FileUploadException;

import com.baidu.ueditor.define.AppInfo;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.FileType;
import com.baidu.ueditor.define.State;
import com.lczy.media.util.FileServerResponse;
import com.lczy.media.util.FileServerUtils;

public class ZYBase64Uploader {
	
	public static State save(String content, Map<String, Object> conf) {
		
		byte[] data = decode(content);

		long maxSize = ((Long) conf.get("maxSize")).longValue();

		if (!validSize(data, maxSize)) {
			return new BaseState(false, AppInfo.MAX_SIZE);
		}

		String suffix = FileType.getSuffix("JPG");

		//String savePath = PathFormat.parse((String) conf.get("savePath"),(String) conf.get("filename"));
		
		State state = null;
		try {
			FileServerResponse resp = FileServerUtils.uploadImage(data);
			if (resp.isSuccess()) {
				state = new BaseState(true);
				state.putInfo("url", resp.getInfo("url"));
				state.putInfo("type", suffix);
				state.putInfo("original", "");
			} else {
				throw new FileUploadException();
			}
		} catch (Exception e) {
			state = new BaseState(false, AppInfo.IO_ERROR);
		}

		return state;
	}

	private static byte[] decode(String content) {
		return Base64.decodeBase64(content);
	}

	private static boolean validSize(byte[] data, long length) {
		return data.length <= length;
	}
	
}

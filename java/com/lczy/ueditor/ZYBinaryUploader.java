package com.lczy.ueditor;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.baidu.ueditor.define.AppInfo;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.FileType;
import com.baidu.ueditor.define.State;
import com.lczy.media.util.FileServerResponse;
import com.lczy.media.util.FileServerUtils;

public class ZYBinaryUploader {
	public static final State save(HttpServletRequest request, Map<String, Object> conf) {
		
		if (!ServletFileUpload.isMultipartContent(request)) {
			return new BaseState(false, AppInfo.NOT_MULTIPART_CONTENT);
		}

		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			String filedName = (String) conf.get("fieldName");
			MultipartFile multipartFile = multipartRequest.getFile(filedName);

			if (multipartFile == null) {
				return new BaseState(false, AppInfo.NOTFOUND_UPLOAD_DATA);
			}

			String originFileName = multipartFile.getOriginalFilename();
			String suffix = FileType.getSuffixByFilename(originFileName);
			
			//long maxSize = ((Long) conf.get("maxSize")).longValue();

			if (!validType(suffix, (String[]) conf.get("allowFiles"))) {
				return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
			}

			
			InputStream is = multipartFile.getInputStream();
			
			FileServerResponse resp = FileServerUtils.uploadImage(is, originFileName);
			State state = new BaseState(true);
			if (resp.isSuccess()) {
				state.putInfo("url", resp.getInfo("url"));
				state.putInfo("type", suffix);
				state.putInfo("original", originFileName);
			} else {
				throw new FileUploadException();
			}
			
			is.close();
			
			return state;
		} catch (FileUploadException e) {
			return new BaseState(false, AppInfo.PARSE_REQUEST_ERROR);
		} catch (Exception e) {
		}
		
		return new BaseState(false, AppInfo.IO_ERROR);
	}

	private static boolean validType(String type, String[] allowTypes) {
		List<String> list = Arrays.asList(allowTypes);

		return list.contains(type);
	}
}

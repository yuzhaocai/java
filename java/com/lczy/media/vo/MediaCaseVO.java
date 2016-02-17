package com.lczy.media.vo;

import org.springframework.web.multipart.MultipartFile;

public class MediaCaseVO {
	private MultipartFile showCasePicFile;

	public MultipartFile getShowCasePicFile() {
		return showCasePicFile;
	}

	public void setShowCasePicFile(MultipartFile showCasePicFile) {
		this.showCasePicFile = showCasePicFile;
	}

}

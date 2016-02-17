package com.lczy.media.vo;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import com.lczy.media.entity.Media;

public class MedioCaseVO {
	public MedioCaseVO() {
	}

	private MultipartFile showPicFile;
	private String id;
	private Media media;
	private String title;
	private String light;
	private String content;
	private String showPic;
	private String createBy;
	private Date createTime;
	private String modifyBy;
	private Date modifyTime;

	public MultipartFile getShowPicFile() {
		return showPicFile;
	}

	public void setShowPicFile(MultipartFile showPicFile) {
		this.showPicFile = showPicFile;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLight() {
		return light;
	}

	public void setLight(String light) {
		this.light = light;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getShowPic() {
		return showPic;
	}

	public void setShowPic(String showPic) {
		this.showPic = showPic;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getModifyBy() {
		return modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

}

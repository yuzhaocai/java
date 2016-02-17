package com.lczy.media.vo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.lczy.media.entity.Activity;

public class ActivityVO {

	private String id;
	private String name;
	private float percent;
	private String startTime;
	private String endTime;

	public ActivityVO() {
	}

	public ActivityVO(Activity entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.percent = entity.getPercent();

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		this.startTime = df.format(entity.getStartTime());
		this.endTime = df.format(entity.getEndTime());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPercent() {
		return percent;
	}

	public void setPercent(float percent) {
		this.percent = percent;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

}

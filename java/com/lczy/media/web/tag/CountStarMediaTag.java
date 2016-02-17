package com.lczy.media.web.tag;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import com.lczy.common.util.SpringUtils;
import com.lczy.media.service.MediaService;

public class CountStarMediaTag extends TagSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -232163184449816656L;
	
	private String id;

	private String mediaType;

	@Override
	public int doStartTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		try {
			MediaService mediaService = this.getMediaService();
			Map<String, Object> searchParams = new HashMap<>();
			searchParams.put("EQ_star.id", id);
			if (StringUtils.isNotBlank(mediaType)) {
				searchParams.put("EQ_mediaType", mediaType);
			}
			int count = mediaService.countBy(searchParams);
			out.write(String.valueOf(count));
		} catch (IOException e) {
			throw new JspException(e);
		}
		return SKIP_BODY;
	}
	
	private MediaService getMediaService() {
		return SpringUtils.getBean(MediaService.class);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

}

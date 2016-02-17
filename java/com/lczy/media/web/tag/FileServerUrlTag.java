package com.lczy.media.web.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.lczy.media.util.FileServerUtils;

@SuppressWarnings("serial")
public class FileServerUrlTag extends TagSupport {
	
	private String value;

	public void setValue(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}

	@Override
	public int doStartTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		try {
			out.write(FileServerUtils.getFileUrl(value));
		} catch (IOException e) {
			throw new JspException(e);
		}
		return SKIP_BODY;
	}
	
}

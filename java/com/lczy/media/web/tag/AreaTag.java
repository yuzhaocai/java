package com.lczy.media.web.tag;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.DynamicAttributes;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.lczy.common.util.SpringUtils;
import com.lczy.media.entity.Area;
import com.lczy.media.service.common.AreaProvider;

@SuppressWarnings("serial")
public class AreaTag  extends BodyTagSupport implements DynamicAttributes {
	
	private String id;
	
	private boolean english;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isEnglish() {
		return english;
	}

	public void setEnglish(boolean english) {
		this.english = english;
	}

	@Override
	public void setDynamicAttribute(String uri, String localName, Object value)
			throws JspException {
		
	}
	
	private AreaProvider getProvicer() {
		return SpringUtils.getBean(AreaProvider.class);
	}
	
	private String getName(Area a) {
		if (isEnglish()) {
			return a.getEnglish();
		} else {
			return a.getName();
		}
	}
	
	@Override
	public int doStartTag() throws JspException {
		String name = id;
		Area a = getProvicer().getAreaMap().get(id);
		if (a != null) {
			name = getName(a);
		} else if( "ALL".equalsIgnoreCase(id) ) {
			name = "全国";
		} else {
			String[] ids = id.split(",");
			List<String> names = Lists.newArrayList();
			for(String id : ids) {
				a = getProvicer().getAreaMap().get(id.trim());
				if ( a != null ) {
					names.add(getName(a));
				} else {
					names.add(id);
				}
			}
			name = StringUtils.join(names, ", ");
		}
		JspWriter out = this.pageContext.getOut();
		try {
			out.write(name);
		} catch (IOException e) {
			throw new JspException(e);
		}
		return SKIP_BODY;
	}

}

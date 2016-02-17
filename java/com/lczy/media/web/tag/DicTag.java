package com.lczy.media.web.tag;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.google.common.collect.Lists;
import com.lczy.common.util.ConfigFile;
import com.lczy.common.util.SpringUtils;
import com.lczy.media.service.common.DicProvider;

@SuppressWarnings("serial")
public class DicTag extends TagSupport {
	
	private ConfigFile styles = new ConfigFile("classpath:/properties/styles.properties");
	
	private Object value;

	private boolean style;
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	public Object getValue() {
		return value;
	}

	public void setStyle(boolean style) {
		this.style = style;
	}
	
	public boolean getStyle() {
		return style;
	}

	@Override
	public int doStartTag() throws JspException {
		String name = getDicItemNames();
		if (style && styles.getProperty(value.toString()) != null) {
			name = "<span style='color:" + styles.getProperty(value.toString()) + "'>" + name + "</span>";
		}
		JspWriter out = this.pageContext.getOut();
		try {
			out.write(name);
		} catch (IOException e) {
			throw new JspException(e);
		}
		return SKIP_BODY;
	}
	
	private DicProvider getDicProvider() {
		return SpringUtils.getBean(DicProvider.class);
	}

	@SuppressWarnings("rawtypes")
	private String getDicItemNames() {
		if( value == null)
			return "";
		
		List<String> keys = null;
		if ( value instanceof String ) {
			keys = Arrays.asList(value.toString().split(","));
		} else if (value instanceof String[]) {
			keys = Arrays.asList((String[])value);
		} else if (value instanceof List<?> ) {
			keys = Lists.newArrayList();
			for( Object o : (List) value) {
				keys.add(o.toString());
			}
		}
		if( keys != null ) {
			return getDicProvider().getItemNames(keys);
		} else {
			return value.toString();
		}
	}





	
}

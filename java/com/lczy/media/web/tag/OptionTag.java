package com.lczy.media.web.tag;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

@SuppressWarnings("serial")
public class OptionTag extends TagSupport {
	
	private String value;
	
	private String label;
	
	private Object property;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Object getProperty() {
		return property;
	}

	public void setProperty(Object property) {
		this.property = property;
	}

	private static String FLAG = "selected";
	
	@Override
	public int doStartTag() throws JspException {
		
		JspWriter out = this.pageContext.getOut();
		try {
			out.write(String.format("<option value='%s' %s >%s</option>", value, getFlag(), label));
		} catch (IOException e) {
			throw new JspException(e);
		}
	
		return SKIP_BODY;
	}

	@SuppressWarnings("rawtypes")
	private String getFlag() {
		String flag = "";
		if( property != null ) {
			if( property instanceof List ) {
				List l = (List) property;
				flag = l.contains(value) ? FLAG : "";
			} else if( property instanceof String[] ) {
				List<String> l = Arrays.asList( (String[])property );
				flag = l.contains(value) ? FLAG : "";
			} else {
				flag = value.equals(property) ? FLAG : "";
			}
		}
		
		return flag;
	}
	
	

}

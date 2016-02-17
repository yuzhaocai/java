package com.lczy.media.web.tag;

import javax.servlet.jsp.JspException;

import org.apache.taglibs.standard.tag.common.core.OutSupport;


public class OutTag extends OutSupport {

	private static final long serialVersionUID = 6861342122612761914L;
	
	private int len = 50;
	
	private boolean ellipsis = true;
	
	/**
	 * 是否转换 \n 为 &lt;br> 标签.
	 */
	private boolean br = false;
	
    /**
	 * @return the ellipsis
	 */
	public boolean isEllipsis() {
		return ellipsis;
	}

	/**
	 * @param ellipsis the ellipsis to set
	 */
	public void setEllipsis(boolean ellipsis) {
		this.ellipsis = ellipsis;
	}

	/**
	 * @return the len
	 */
	public int getLen() {
		return len;
	}

	/**
	 * @param len the len to set
	 */
	public void setLen(int len) {
		this.len = len;
	}

	// for tag attribute
    public void setValue(Object value) {
        this.value = value;
    }
      
    // for tag attribute
    public void setDefault(String def) {
        this.def = def;
    }
        
    // for tag attribute
    public void setEscapeXml(boolean escapeXml) {
        this.escapeXml = escapeXml;
    }

    
    
	@Override
	public int doStartTag() throws JspException {
		if (value != null && ellipsis && value.toString().length() > len) {
			String src = value.toString();
			StringBuilder sb = new StringBuilder();
			sb.append(src.substring(0, len));
			sb.append("...");
			value = sb.toString();
		} else if (value != null && isBr()) {
			value = value.toString().replace("<", "&lt;").replace(">", "&gt;").replaceAll("\n", "<br>");
		}
		return super.doStartTag();
	}

	public boolean isBr() {
		return br;
	}

	public void setBr(boolean br) {
		this.br = br;
	}
    
    
}

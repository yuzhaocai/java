package com.lczy.media.web.tag;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.beanutils.PropertyUtils;

@SuppressWarnings("serial")
public class OptionsTag extends TagSupport {

	private String itemValue;
	
	private String itemLabel;
	
	@SuppressWarnings("rawtypes")
	private Iterable items;
	
	@SuppressWarnings("rawtypes")
	private Collection selecteds;

	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}

	public void setItemLabel(String itemLabel) {
		this.itemLabel = itemLabel;
	}

	@SuppressWarnings("rawtypes")
	public void setItems(Object items) {
		if( items instanceof Iterable ) {
			this.items = (Iterable)items;
		} else if( items instanceof Object[]) {
			this.items = Arrays.asList(items);
		} else {
			throw new RuntimeException("items 属性不支持此类型的对象：" + items.getClass());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setSelecteds(Object value) {
		selecteds = new ArrayList<Object>();
		if( value == null ) {
			return;
		}
		
		if( value instanceof Iterable ) {
			Iterable<Object> it = (Iterable) value;
			for(Object o : it) {
				selecteds.add(o);
			}
		} else if( value instanceof Object[]) {
			selecteds = Arrays.asList((Object[])value);
		} else if( value instanceof String) {
			selecteds = Arrays.asList(value.toString().split(","));
		} else {
			selecteds = Arrays.asList(value.toString());
		}
	}

	private static final String SELECTED = "selected";
	
	@Override
	public int doStartTag() throws JspException {
		
		JspWriter out = this.pageContext.getOut();
		StringBuilder sb = new StringBuilder(300);
		try {
			for(Object obj : items) {
				sb.append(String.format("<option value='%s' %s >%s</option>", getValue(obj), getFlag(obj), getLabel(obj)));
			}
			out.write(sb.toString());
		} catch (IOException e) {
			throw new JspException(e);
		}
	
		return SKIP_BODY;
	}

	private Object getValue(Object bean) {
		if( itemValue == null ) {
			return bean;
		}
		try {
			return PropertyUtils.getProperty(bean, itemValue);
		} catch (IllegalAccessException | InvocationTargetException
				| NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	private Object getLabel(Object bean) {
		if( itemLabel == null ) {
			return getValue(bean);
		}
		
		try {
			return PropertyUtils.getProperty(bean, itemLabel);
		} catch (IllegalAccessException | InvocationTargetException
				| NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	private String getFlag(Object bean) {
		String flag = "";
		if( selecteds != null ) {
			if( selecteds.contains(getValue(bean)))
				flag = SELECTED;
		}
		return flag;
	}
	
}

package com.lczy.media.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
* SysConfig
* table:sys_config
* 
* @author Wu.Yanhong
* @version v1.0
* @date 2015-01-14 17:23:24
*/
@Entity
@Table(name = "SYS_CONFIG")
public class SysConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 配置项名称
     */
    private String name;

    /**
     * 配置项的值
     */
    private String value;

    /**
     * @return 配置项名称
     */
    @Id
    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    /**
     * @param name 
	 *            配置项名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return 配置项的值
     */
    @Column(name = "VALUE")
    public String getValue() {
        return value;
    }

    /**
     * @param value 
	 *            配置项的值
     */
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        SysConfig other = (SysConfig) that;
        return (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getValue() == null ? other.getValue() == null : this.getValue().equals(other.getValue()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getValue() == null) ? 0 : getValue().hashCode());
        return result;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
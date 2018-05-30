/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.config;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author bfredrich
 */
@Entity
@Table(name = "varwatch_config_filter")
public class FilterConfig implements Serializable {

    @Id
    @Column(name = "filter_name")
    private String key;
    @Column(name = "filter_value")
    private String value;
    @Column(name = "filter_type", nullable = true)
    private String type;//variant of variant_effect
    @Column(name = "enabled")
    private boolean enabled;

    FilterConfig(String key, String value, String type, boolean enabled) {
        this.enabled = enabled;
        this.key = key;
        this.value = value;
        this.type = type;
    }

    public FilterConfig() {
    }

    public String getName() {
        return key;
    }

    public void setName(String name) {
        this.key = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFilterType() {
        return type;
    }

    public void setFilterType(String filterType) {
        this.type = filterType;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}

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
@Table(name = "varwatch_config")
public class VarWatchConfig implements Serializable {

    @Id
    @Column(name = "config_key")
    private String key;
    @Column(name = "config_value")
    private String value;

    public VarWatchConfig(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public VarWatchConfig() {

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}

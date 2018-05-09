/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.variant;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Broder
 */
@Entity
@Table(name = "variant_meta_data")
public class VariantMetaData implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "key")
    private String dataKey;
    @Column(name = "value")
    private String dataValue;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", nullable = false)
    private Variant variant;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Variant getVariant() {
        return variant;
    }

    public void setVariant(Variant variant) {
        this.variant = variant;
    }

    public String getKey() {
        return dataKey;
    }

    public void setKey(String key) {
        this.dataKey = key;
    }

    public String getValue() {
        return dataValue;
    }

    public void setValue(String value) {
        this.dataValue = value;
    }

}

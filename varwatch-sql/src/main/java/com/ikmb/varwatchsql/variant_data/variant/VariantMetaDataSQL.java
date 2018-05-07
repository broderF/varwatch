/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.variant_data.variant;

import com.ikmb.varwatchsql.entities.DatasetVWSQL;
import com.ikmb.varwatchsql.matching.MatchSQL;
import com.ikmb.varwatchsql.entities.VariantEffectSQL;
import com.ikmb.varwatchsql.entities.VariantStatusSQL;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Broder
 */
@Entity
@Table(name = "variant_meta_data")
public class VariantMetaDataSQL implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "key")
    private String dataKey;
    @Column(name = "value")
    private String dataValue;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", nullable = false)
    private VariantSQL variant;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VariantSQL getVariant() {
        return variant;
    }

    public void setVariant(VariantSQL variant) {
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

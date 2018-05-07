/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.data.hpo;

import com.ikmb.varwatchsql.variant_data.variant.VariantSQL;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Broder
 */
@Entity
@Table(name = "hpo")
public class HPOTermSQL implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "identifier")
    private String identifier;
    @Column(name = "description")
    private String description;
    @Column(name = "subontology")
    private String subontology;
    @Column(name = "is_root")
    private boolean is_root;
    @Column(name = "is_obsolete")
    private boolean is_obsolete;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "hpo")
    private Set<HPOAlternativeTermSQL> alternativeHpos = new HashSet<HPOAlternativeTermSQL>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getSubontology() {
        return subontology;
    }

    public void setSubontology(String subontology) {
        this.subontology = subontology;
    }

    public boolean isRoot() {
        return is_root;
    }

    public void setIsRoot(boolean is_root) {
        this.is_root = is_root;
    }

    public boolean isObsolete() {
        return is_obsolete;
    }

    public void setIsObsolete(boolean is_obsolete) {
        this.is_obsolete = is_obsolete;
    }

    public Set<HPOAlternativeTermSQL> getAlternativeHpos() {
        return alternativeHpos;
    }

    public void setAlternativeHpos(Set<HPOAlternativeTermSQL> alternativeHpos) {
        this.alternativeHpos = alternativeHpos;
    }

}

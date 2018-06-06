/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.hpo;

import com.google.gson.annotations.Expose;
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
public class HPOTerm implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "identifier")
    @Expose
    private String identifier;
    @Column(name = "description", nullable = true)
    private String description;
    @Column(name = "subontology", nullable = true)
    private String subontology;
    @Column(name = "is_root", nullable = true)
    private Boolean is_root;
    @Column(name = "is_obsolete", nullable = true)
    private Boolean is_obsolete;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "hpo")
    private Set<HPOAlternativeTerm> alternativeHpos = new HashSet<>();

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

    public Boolean isRoot() {
        return is_root;
    }

    public void setIsRoot(Boolean is_root) {
        this.is_root = is_root;
    }

    public Boolean isObsolete() {
        return is_obsolete;
    }

    public void setIsObsolete(Boolean is_obsolete) {
        this.is_obsolete = is_obsolete;
    }

    public Set<HPOAlternativeTerm> getAlternativeHpos() {
        return alternativeHpos;
    }

    public void setAlternativeHpos(Set<HPOAlternativeTerm> alternativeHpos) {
        this.alternativeHpos = alternativeHpos;
    }
}

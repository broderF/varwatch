/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.update.hpo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author bfredrich
 */
public class HpoPathTerm {

    private String hpoTerm;
    private String description;
    private List<Long> parentHPOTerms = new ArrayList<>();
    private Set<String> alternativeTerms = new HashSet<>();
    private final Long id;

    public HpoPathTerm(Long id, String hpoTerm, String description) {
        this.hpoTerm = hpoTerm;
        this.id = id;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void addParent(Long id) {
        parentHPOTerms.add(id);
    }

    public String getHpoTerm() {
        return hpoTerm;
    }

    public List<Long> getParentHPOTerms() {
        return parentHPOTerms;
    }

    public Long getId() {
        return id;
    }

    public void addAlternativeHpos(Set<String> alternativeTerms) {
        this.alternativeTerms.addAll(alternativeTerms);
    }

    public Set<String> getAlternativeTerms() {
        return alternativeTerms;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.varwatchcommons.entities;

/**
 *
 * @author broder
 */
public class HPOTerm {

    private String identifier;
    private String ageOfOnset;
    private Boolean observed;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getAgeOfOnset() {
        return ageOfOnset;
    }

    public void setAgeOfOnset(String ageOfOnset) {
        this.ageOfOnset = ageOfOnset;
    }

    public Boolean getObserved() {
        return observed;
    }

    public void setObserved(Boolean observed) {
        this.observed = observed;
    }
    
    

}

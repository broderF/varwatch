/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.data.hpo;

import com.ikmb.varwatchcommons.entities.Feature;
import com.ikmb.varwatchcommons.entities.HPOTerm;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author broder
 */
public class HPOTermBuilder {

    private List<HPOTerm> hpoTermsBasic = new ArrayList<>();
    private List<String> hpoTerms = new ArrayList<>();

    public HPOTermBuilder addFeatures(List<Feature> vwFeatures) {
        hpoTerms = new ArrayList<>();
        hpoTermsBasic = new ArrayList<>();
        for (Feature vwFeature : vwFeatures) {
            hpoTerms.add(vwFeature.getId());
        }
        return this;
    }

    public HPOTermBuilder addFeaturesNew(List<HPOTerm> vwFeatures) {
        hpoTerms = new ArrayList<>();
        hpoTermsBasic = new ArrayList<>();
        for (HPOTerm vwFeature : vwFeatures) {
            hpoTerms.add(vwFeature.getIdentifier());
        }
        return this;
    }

    public HPOTermBuilder addFeatures(Set<PhenotypeSQL> vwFeatures) {
        hpoTerms = new ArrayList<>();
        hpoTermsBasic = new ArrayList<>();
        for (PhenotypeSQL vwFeature : vwFeatures) {
            hpoTerms.add(vwFeature.getPhenotype().getIdentifier());
            HPOTerm term = new HPOTerm();
            term.setIdentifier(vwFeature.getPhenotype().getIdentifier());
            if (vwFeature.getAgeOfOnset() != null) {
                term.setAgeOfOnset(vwFeature.getAgeOfOnset().getIdentifier());
            }
            term.setObserved(vwFeature.getObserved());
            hpoTermsBasic.add(term);

        }
        return this;
    }

    public List<String> buildStringList() {
        return hpoTerms;
    }

    public Set<String> buildStringSet() {
        return new HashSet<String>(hpoTerms);
    }

    public List<HPOTerm> buildList() {
        return hpoTermsBasic;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.varwatchcommons.entities;

import com.ikmb.core.data.gene.Gene;
import com.ikmb.core.data.hpo.HPOTerm;
import com.ikmb.core.data.variant.Variant;
import java.util.List;

/**
 *
 * @author broder
 */
public class InternalMatchInformation extends MatchInformation {

    private Long queryVariantId;

    private boolean identicalMatch;
    private Variant matchedVariant;
    private List<HPOTerm> hpoTerms;
    private Double hpoDist;
    private List<Gene> genes;
    private Contact contact;
    private Gene gene;

    public Long getQueryVariantId() {
        return queryVariantId;
    }

    public void setQueryVariantId(Long queryVariantId) {
        this.queryVariantId = queryVariantId;
    }

    public Gene getGene() {
        return gene;
    }

    public void setGene(Gene gene) {
        this.gene = gene;
    }

    public boolean isIdenticalMatch() {
        return identicalMatch;
    }

    public void setIdenticalMatch(boolean identicalMatch) {
        this.identicalMatch = identicalMatch;
    }

    public Variant getMatchedVariant() {
        return matchedVariant;
    }

    public void setMatchedVariant(Variant matchedVariant) {
        this.matchedVariant = matchedVariant;
    }

    public List<HPOTerm> getHpoTerms() {
        return hpoTerms;
    }

    public void setHpoTerms(List<HPOTerm> hpoTerms) {
        this.hpoTerms = hpoTerms;
    }

    public List<Gene> getGenes() {
        return genes;
    }

    public void setGenes(List<Gene> genes) {
        this.genes = genes;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Double getHpoDist() {
        return hpoDist;
    }

    public void setHpoDist(Double hpoDist) {
        this.hpoDist = hpoDist;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.gene;


/**
 *
 * @author broder
 */
public class GeneBuilder {

    private String identifier;

    public GeneBuilder withGeneSql(Gene gene) {
        identifier = gene.getName();
        return this;
    }

    public com.ikmb.core.varwatchcommons.entities.Gene build() {
        com.ikmb.core.varwatchcommons.entities.Gene gene = new com.ikmb.core.varwatchcommons.entities.Gene();
        gene.setIdentifier(identifier);
        return gene;
    }
}

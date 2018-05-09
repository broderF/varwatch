/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.varwatchcommons.entities;


/**
 *
 * @author bfredrich
 */
public class GenomicFeature {

//    private MMEGene gene;
    private VWVariant variant;
    private Integer zygosity;
//    private MMEGenomicFeaturesType type;

//    public MMEGene getGene() {
//        return gene;
//    }
//
//    public void setGene(MMEGene gene) {
//        this.gene = gene;
//    }

    public VWVariant getVariant() {
        return variant;
    }

    public void setVariant(VWVariant variant) {
        this.variant = variant;
    }

    public Integer getZygosity() {
        return zygosity;
    }

    public void setZygosity(Integer zygosity) {
        this.zygosity = zygosity;
    }

//    public MMEGenomicFeaturesType getType() {
//        return type;
//    }
//
//    public void setType(MMEGenomicFeaturesType type) {
//        this.type = type;
//    }

}

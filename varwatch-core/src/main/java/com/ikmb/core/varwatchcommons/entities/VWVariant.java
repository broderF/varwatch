/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.varwatchcommons.entities;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bfredrich
 */
public class VWVariant {

//    private String assembly;
    private String referenceName;
    private Integer start;
    private Integer end;
    private String referenceBases;
    private String alternateBases;
    private String hgvs;
    private List<VWStatus> status;
    private String vepIdentifier;
//    private List<VWVariantDetails> variantDetails;
    private List<MetaData> metaData = new ArrayList<>();

    public List<MetaData> getMetaData() {
        return metaData;
    }

    public void setMetaData(List<MetaData> metaData) {
        this.metaData = metaData;
    }

//    public String getAssembly() {
//        return assembly;
//    }
//
//    public void setAssembly(String assembly) {
//        this.assembly = assembly;
//    }
    public String getReferenceName() {
        return referenceName;
    }

    public void setReferenceName(String referenceName) {
        this.referenceName = referenceName;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public String getReferenceBases() {
        return referenceBases;
    }

    public void setReferenceBases(String referenceBases) {
        this.referenceBases = referenceBases;
    }

    public String getAlternateBases() {
        return alternateBases;
    }

    public void setAlternateBases(String alternateBases) {
        this.alternateBases = alternateBases;
    }

    public List<VWStatus> getStatus() {
        return status;
    }

    public void setStatus(List<VWStatus> status) {
        this.status = status;
    }

    public String getHgvs() {
        return hgvs;
    }

    public void setHgvs(String hgvs) {
        this.hgvs = hgvs;
    }

    public String getVepIdentifier() {
        return vepIdentifier;
    }

    public void setVepIdentifier(String vepIdentifier) {
        this.vepIdentifier = vepIdentifier;
    }

    @Override
    public String toString() {
        String variantString = referenceName + "_" + start + "_" + referenceBases + "_" + alternateBases;
        return variantString;
    }

    public String toCompactString() {
        String variantString = referenceName + "_" + start + "_" + referenceBases.substring(0, Math.min(referenceBases.length(), 3)) + "_" + alternateBases.substring(0, Math.min(alternateBases.length(), 3));
        return variantString;
    }

    public Variant toVariant() {
        Variant variant = new Variant();
        variant.setChromosomeName(referenceName);
        variant.setReferenceBase(referenceBases);
        variant.setAlternateBase(alternateBases);
        variant.setPosition(start);
        return variant;
    }

}

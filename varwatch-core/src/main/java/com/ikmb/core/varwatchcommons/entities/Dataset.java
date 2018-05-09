/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.varwatchcommons.entities;

import java.util.List;

/**
 *
 * @author broder
 */
public class Dataset {

    private Long id;
    private String description;
    private List<Variant> variants;
    private String assembly;
    private List<HPOTerm> hpoTerms;
    private byte[] vcfFile;
    private String ageOfOnset;
    private String modeOfInheritance;

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

    public String getAssembly() {
        return assembly;
    }

    public void setAssembly(String assembly) {
        this.assembly = assembly;
    }

    public List<Variant> getVariants() {
        return variants;
    }

    public void setVariants(List<Variant> variants) {
        this.variants = variants;
    }

    public List<HPOTerm> getHpoTerms() {
        return hpoTerms;
    }

    public void setHpoTerms(List<HPOTerm> hpoTerms) {
        this.hpoTerms = hpoTerms;
    }

    public byte[] getVcfFile() {
        return vcfFile;
    }

    public void setVcfFile(byte[] vcfFile) {
        this.vcfFile = vcfFile;
    }

    public String getAgeOfOnset() {
        return ageOfOnset;
    }

    public void setAgeOfOnset(String ageOfOnset) {
        this.ageOfOnset = ageOfOnset;
    }

    public String getModeOfInheritance() {
        return modeOfInheritance;
    }

    public void setModeOfInheritance(String modeOfInheritance) {
        this.modeOfInheritance = modeOfInheritance;
    }
    
    

}

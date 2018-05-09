/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.varwatchcommons.entities;

import java.util.List;

/**
 *
 * @author bfredrich
 */
public class Patient {

    private String description;
    private byte[] vcfFile;
    private String assembly;
    private String id;
    private String label;
    private Contact contact;
    private String species;
    private String sex;
    private String ageOfOnset;
    private String inheritanceMode;
//    private List<VWDisorder> disorders;
    private List<Feature> features;
    private List<GenomicFeature> genomicFeatures;
    private VWStatus status;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getVcfFile() {
        return vcfFile;
    }

    public void setVcfFile(byte[] vcfFile) {
        this.vcfFile = vcfFile;
    }

    public String getAssembly() {
        return assembly;
    }

    public void setAssembly(String vcfFileAssembly) {
        this.assembly = vcfFileAssembly;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAgeOfOnset() {
        return ageOfOnset;
    }

    public void setAgeOfOnset(String ageOfOnset) {
        this.ageOfOnset = ageOfOnset;
    }

    public String getInheritanceMode() {
        return inheritanceMode;
    }

    public void setInheritanceMode(String inheritanceMode) {
        this.inheritanceMode = inheritanceMode;
    }

//    public List<VWDisorder> getDisorders() {
//        return disorders;
//    }
//
//    public void setDisorders(List<VWDisorder> disorders) {
//        this.disorders = disorders;
//    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    public List<GenomicFeature> getGenomicFeatures() {
        return genomicFeatures;
    }

    public void setGenomicFeatures(List<GenomicFeature> genomicFeatures) {
        this.genomicFeatures = genomicFeatures;
    }

    public VWStatus getStatus() {
        return status;
    }

    public void setStatus(VWStatus status) {
        this.status = status;
    }
    
}

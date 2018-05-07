/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.importer.hgmd;

import java.util.List;

/**
 *
 * @author broder
 */
public class HGMDData {

    private String hgmd_acc;
    private String chromosome;
    private Integer pos;
    private String ref;
    private String alt;
    private List<String> hpos;
    private List<String> ulms;

    public String getHgmd_acc() {
        return hgmd_acc;
    }

    public void setHgmd_acc(String hgmd_acc) {
        this.hgmd_acc = hgmd_acc;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public Integer getPos() {
        return pos;
    }

    public void setPos(Integer pos) {
        this.pos = pos;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public List<String> getHpos() {
        return hpos;
    }

    public void setHpos(List<String> hpos) {
        this.hpos = hpos;
    }

    public List<String> getUlms() {
        return ulms;
    }

    public void setUlms(List<String> ulms) {
        this.ulms = ulms;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.config;

/**
 *
 * @author broder
 */
public class FilterOptions {

    boolean canonical;
    double maf;
    String impact;

    public boolean isCanonical() {
        return canonical;
    }

    public void setCanonical(boolean canonical) {
        this.canonical = canonical;
    }

    public double getMaf() {
        return maf;
    }

    public void setMaf(double maf) {
        this.maf = maf;
    }

    public String getImpact() {
        return impact;
    }

    public void setImpact(String impact) {
        this.impact = impact;
    }

}

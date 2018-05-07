/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.entities;

import com.ikmb.varwatchsql.data.transcript.TranscriptSQL;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author bfredrich
 */
@Entity
@Table(name = "dataset_hgmd")
public class DatasetHGMDSQL extends DatasetSQL implements Serializable {

    @Column(name = "hgmd_acc")
    private String hgmdAcc;
    @Column(name = "chr_name")
    private String chrName;
    @Column(name = "chr_pos")
    private Integer chrPos;
    @Column(name = "ref_base")
    private String refBase;
    @Column(name = "alt_base")
    private String altBase;
    @Column(name = "polyphen")
    private Double polyphen;
    @Column(name = "sift")
    private Double sift;
    @Column(name = "protein_start")
    private Integer protein_start;
    @Column(name = "protein_end")
    private Integer protein_end;
    @Column(name = "loftee")
    private String loftee;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "transcript_id", nullable = false)
    private TranscriptSQL transcript;
    @Transient
    private VariantEffectSQL variantEffect;

    public String getHgmdAcc() {
        return hgmdAcc;
    }

    public void setHgmdAcc(String hgmdAcc) {
        this.hgmdAcc = hgmdAcc;
    }

    public Integer getProtein_start() {
        return protein_start;
    }

    public void setProtein_start(Integer protein_start) {
        this.protein_start = protein_start;
    }

    public Integer getProtein_end() {
        return protein_end;
    }

    public void setProtein_end(Integer protein_end) {
        this.protein_end = protein_end;
    }

    public String getLoftee() {
        return loftee;
    }

    public void setLoftee(String loftee) {
        this.loftee = loftee;
    }
    
    public String getChrName() {
        return chrName;
    }

    public void setChrName(String chrName) {
        this.chrName = chrName;
    }

    public Integer getChrPos() {
        return chrPos;
    }

    public void setChrPos(Integer chrPos) {
        this.chrPos = chrPos;
    }

    public String getRefBase() {
        return refBase;
    }

    public void setRefBase(String refBase) {
        this.refBase = refBase;
    }

    public String getAltBase() {
        return altBase;
    }

    public void setAltBase(String altBase) {
        this.altBase = altBase;
    }

    public Double getPolyphen() {
        return polyphen;
    }

    public void setPolyphen(Double polyphen) {
        this.polyphen = polyphen;
    }

    public Double getSift() {
        return sift;
    }

    public void setSift(Double sift) {
        this.sift = sift;
    }

    public TranscriptSQL getTranscript() {
        return transcript;
    }

    public void setTranscript(TranscriptSQL transcript) {
        this.transcript = transcript;
    }

    public VariantEffectSQL getVariantEffect() {
        return variantEffect;
    }

    public void setVariantEffect(VariantEffectSQL variantEffect) {
        this.variantEffect = variantEffect;
    }

}

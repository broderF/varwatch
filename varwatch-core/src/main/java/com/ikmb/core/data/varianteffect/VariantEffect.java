/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.varianteffect;

import com.ikmb.core.data.transcript.Transcript;
import com.ikmb.core.data.variant.Variant;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author fredrich
 */
@Entity
@Table(name = "variant_effect")
public class VariantEffect implements Serializable {

    public static VariantEffect get(VariantEffect curEffect) {
        VariantEffect effectSql = new VariantEffect();
        effectSql.setUploaded_variation(curEffect.getUploaded_variation());
        effectSql.setImpactFactor(curEffect.getImpactFactor());
        effectSql.setHgvs_c(curEffect.getHgvs_c());
        effectSql.setFeatureName(curEffect.getTranscriptName());
        effectSql.setConsequence(curEffect.getConsequence());
        effectSql.setCds_start(curEffect.getCds_start());
        effectSql.setCds_end(curEffect.getCds_end());
        effectSql.setCanonicalTranscript(curEffect.getCanonicalTranscript());
        return effectSql;
    }

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "uploaded_variation")
    private String uploaded_variation;
    @Column(name = "consequence")
    private String consequence;
    @Column(name = "hgvs_c")
    private String hgvs_c;
    @Column(name = "sift")
    private Double sift;
    @Column(name = "polyphen")
    private Double polyphen;
    @Column(name = "cds_start")
    private Integer cds_start;
    @Column(name = "cds_end")
    private Integer cds_end;
    @Column(name = "protein_start")
    private Integer protein_start;
    @Column(name = "protein_end")
    private Integer protein_end;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "variant_id", nullable = false)
    private Variant variant;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "transcript_id", nullable = false)
    private Transcript transcript;
    @Transient
    private String featureName;
    @Transient
    private Boolean canonicalTranscript;
    @Column(name = "loftee")
    private String loftee;
    @Column(name = "impact")
    private String impactFactor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoftee() {
        return loftee;
    }

    public void setLoftee(String loftee) {
        this.loftee = loftee;
    }
    
    public Integer getCds_start() {
        return cds_start;
    }

    public void setCds_start(Integer cds_start) {
        this.cds_start = cds_start;
    }

    public Integer getCds_end() {
        return cds_end;
    }

    public void setCds_end(Integer cds_end) {
        this.cds_end = cds_end;
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

    public String getUploaded_variation() {
        return uploaded_variation;
    }

    public void setUploaded_variation(String uploaded_variation) {
        this.uploaded_variation = uploaded_variation.substring(0, Math.min(uploaded_variation.length(), 50));
    }

    public String getConsequence() {
        return consequence;
    }

    public void setConsequence(String consequence) {
        this.consequence = consequence;
    }

    public String getHgvs_c() {
        return hgvs_c;
    }

    public void setHgvs_c(String hgvs_c) {
        this.hgvs_c = hgvs_c;
    }

    public Variant getVariant() {
        return variant;
    }

    public void setVariant(Variant variant) {
        this.variant = variant;
    }

    public Transcript getTranscript() {
        return transcript;
    }

    public void setTranscript(Transcript transcript) {
        this.transcript = transcript;
    }

    public String getTranscriptName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public Double getSift() {
        return sift;
    }

    public void setSift(Double sift) {
        this.sift = sift;
    }

    public Double getPolyphen() {
        return polyphen;
    }

    public void setPolyphen(Double polyphen) {
        this.polyphen = polyphen;
    }

    public Boolean getCanonicalTranscript() {
        return canonicalTranscript;
    }

    public void setCanonicalTranscript(Boolean canonicalTranscript) {
        this.canonicalTranscript = canonicalTranscript;
    }

    public String getImpactFactor() {
        return impactFactor;
    }

    public void setImpactFactor(String impactFactor) {
        this.impactFactor = impactFactor;
    }

}

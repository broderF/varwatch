/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.variant;

import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.data.varianteffect.VariantEffect;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author Broder
 */
@Entity
@Table(name = "variant")
public class Variant implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "chr_name")
    private String chromosomeName;
    @Column(name = "vep_identifier")
    private String vepIdentifier;
    @Column(name = "chr_pos")
    private Integer chromosomePos;
    @Column(name = "ref_base")
    private String referenceBase;
    @Column(name = "alt_Base")
    private String alternateBase;
    @Column(name = "quality")
    private Integer quality;
    @Column(name = "filter")
    private String filter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataset_id", nullable = false)
    private DatasetVW dataset_vw;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "variant")
    private Set<VariantEffect> variantEffects = new HashSet<VariantEffect>();
//    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinTable(name = "match2variant", joinColumns = {
//        @JoinColumn(name = "variant_id", nullable = false, updatable = false)},
//            inverseJoinColumns = {
//                @JoinColumn(name = "match_id",
//                        nullable = false, updatable = false)})
//    protected Set<MatchSQL> matches = new HashSet<MatchSQL>();
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "variant")
    private Set<VariantStatus> variantStatus = new HashSet<VariantStatus>();
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "variant")
    private Set<VariantMetaData> variantMetaData = new HashSet<VariantMetaData>();
    @Transient
    private String uploaded_variant;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChromosomeName() {
        return chromosomeName;
    }

    public void setChromosomeName(String chromosomeName) {
        this.chromosomeName = chromosomeName;
    }

    public Integer getChromosomePos() {
        return chromosomePos;
    }

    public void setChromosomePos(Integer chromosomePos) {
        this.chromosomePos = chromosomePos;
    }

    public String getReferenceBase() {
        return referenceBase;
    }

    public void setReferenceBase(String referenceBase) {
        this.referenceBase = referenceBase;
    }

    public String getAlternateBase() {
        return alternateBase;
    }

    public void setAlternateBase(String alternateBase) {
        this.alternateBase = alternateBase;
    }

    public Integer getQuality() {
        return quality;
    }

    public void setQuality(Integer quality) {
        this.quality = quality;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public DatasetVW getDataset() {
        return dataset_vw;
    }

    public void setDataset(DatasetVW dataset) {
        this.dataset_vw = dataset;
    }

    public Set<VariantEffect> getVariantEffects() {
        return variantEffects;
    }

    public void setVariantEffects(Set<VariantEffect> variantEffects) {
        this.variantEffects = variantEffects;
    }

    public String getVEPIdentifier() {
        return this.vepIdentifier;
    }

    public void setVEPIdentifier(String identifier) {
        this.vepIdentifier = identifier.substring(0, Math.min(identifier.length(), 50));
    }

//    public Set<MatchSQL> getMatches() {
////        return matches;
//        return null;
//    }
//
//    public void setMatches(Set<MatchSQL> matches) {
////        this.matches = matches;
//    }
    public Set<VariantStatus> getVariantStatus() {
        return variantStatus;
    }

    public void setVariantStatus(Set<VariantStatus> variantStatus) {
        this.variantStatus = variantStatus;
    }

    public Set<VariantMetaData> getVariantMetaData() {
        return variantMetaData;
    }

    public void setVariantMetaData(Set<VariantMetaData> variantMetaData) {
        this.variantMetaData = variantMetaData;
    }

    public String toString() {
        return chromosomeName + "," + chromosomePos + "," + referenceBase + "," + alternateBase;
    }

    public String toDbString() {
        String dbid = "vw_" + String.format("%06d", id);
        return dbid + "," + chromosomeName + "," + chromosomePos + "," + referenceBase + "," + alternateBase;
    }

    public void setUploadedVariantion(String uploaded_variation) {
        this.uploaded_variant = uploaded_variation;
    }

    public String getUploadedVariant() {
        return this.uploaded_variant;
    }
}

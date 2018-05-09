/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.variant;

import com.google.common.collect.Lists;
import com.ikmb.core.varwatchcommons.entities.MetaData;
import com.ikmb.core.varwatchcommons.entities.VWVariant;

/**
 *
 * @author broder
 */
public class VariantBuilder {

    String altBase;
    String chromName;
    Integer chromPos;
    String vepIdent;
    String refBase;
    private long id;
    private String datasetId;

    public VariantBuilder withChromosome(String chromosome) {
        this.chromName = chromosome;
        return this;
    }

    public VariantBuilder withPosition(Integer position) {
        this.chromPos = position;
        return this;
    }

    public VariantBuilder withRefBase(String refBase) {
        this.refBase = refBase;
        return this;
    }

    public VariantBuilder withAltBase(String altBase) {
        this.altBase = altBase;
        return this;
    }

    public VariantBuilder withVWVariant(VWVariant variant) {
        altBase = variant.getAlternateBases();
        chromName = variant.getReferenceName();
        chromPos = variant.getStart();
        vepIdent = variant.getVepIdentifier();
        refBase = variant.getReferenceBases();
        return this;
    }

    public Variant buildSql() {
        Variant variantSQL = new Variant();
        variantSQL.setAlternateBase(altBase);
        variantSQL.setChromosomeName(chromName);
        variantSQL.setChromosomePos(chromPos);
        if (vepIdent != null) {
            variantSQL.setVEPIdentifier(vepIdent);
        }
        variantSQL.setReferenceBase(refBase);
        return variantSQL;
    }

    public VWVariant buildVW() {
        VWVariant variantVW = new VWVariant();
        variantVW.setAlternateBases(altBase);
        variantVW.setReferenceName(chromName);
        variantVW.setStart(chromPos);
        variantVW.setVepIdentifier(vepIdent);
        variantVW.setReferenceBases(refBase);
        return variantVW;
    }

    public VariantBuilder withVariant(Variant variant) {
        altBase = variant.getAlternateBase();
        chromName = variant.getChromosomeName();
        chromPos = variant.getChromosomePos();
        vepIdent = variant.getVEPIdentifier();
        refBase = variant.getReferenceBase();
        id = variant.getId();
        if (variant.getDataset() != null) {
            datasetId = String.valueOf(variant.getDataset().getId());
        }
        return this;
    }

    public com.ikmb.core.varwatchcommons.entities.Variant build() {
        com.ikmb.core.varwatchcommons.entities.Variant variant = new com.ikmb.core.varwatchcommons.entities.Variant();
        variant.setAlternateBase(altBase);
        variant.setChromosomeName(chromName);
        variant.setId(String.valueOf(id));
        variant.setPosition(chromPos);
        variant.setReferenceBase(refBase);
        variant.setDatasetId(datasetId);
        if (vepIdent != null) {
            MetaData meta = new MetaData();
            meta.setDataKey("raw_variant");
            meta.setDataValue(vepIdent);
            variant.setMetaData(Lists.newArrayList(meta));
        }
        return variant;

    }
}

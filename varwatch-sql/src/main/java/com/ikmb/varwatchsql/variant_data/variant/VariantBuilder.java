/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.variant_data.variant;

import com.google.common.collect.Lists;
import com.ikmb.varwatchcommons.entities.MetaData;
import com.ikmb.varwatchcommons.entities.VWVariant;
import com.ikmb.varwatchcommons.entities.Variant;

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
    private String id;
    private String datasetId;

    public VariantBuilder withVWVariant(VWVariant variant) {
        altBase = variant.getAlternateBases();
        chromName = variant.getReferenceName();
        chromPos = variant.getStart();
        vepIdent = variant.getVepIdentifier();
        refBase = variant.getReferenceBases();
        return this;
    }

    public VariantSQL buildSql() {
        VariantSQL variantSQL = new VariantSQL();
        variantSQL.setAlternateBase(altBase);
        variantSQL.setChromosomeName(chromName);
        variantSQL.setChromosomePos(chromPos);
        variantSQL.setVEPIdentifier(vepIdent);
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

    public VariantBuilder withSQLVariant(VariantSQL variant) {
        altBase = variant.getAlternateBase();
        chromName = variant.getChromosomeName();
        chromPos = variant.getChromosomePos();
        vepIdent = variant.getVEPIdentifier();
        refBase = variant.getReferenceBase();
        id = String.valueOf(variant.getId());
        if (variant.getDataset() != null) {
            datasetId = String.valueOf(variant.getDataset().getId());
        }
        return this;
    }

    public Variant build() {
        Variant variant = new Variant();
        variant.setAlternateBase(altBase);
        variant.setChromosomeName(chromName);
        variant.setId(id);
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

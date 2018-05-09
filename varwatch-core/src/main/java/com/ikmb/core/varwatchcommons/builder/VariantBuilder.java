///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.ikmb.core.varwatchcommons.builder;
//
//import com.google.common.collect.Lists;
//import com.ikmb.core.varwatchcommons.entities.MetaData;
//import com.ikmb.core.varwatchcommons.entities.VWVariant;
//import com.ikmb.core.varwatchcommons.entities.Variant;
//
//public class VariantBuilder {
//
//    private String altBase;
//    private String chromName;
//    private Integer chromPos;
//    private String vepIdent;
//    private String refBase;
//    private String id;
//    private String datasetId;
//
//    public VariantBuilder withChromosome(String chromosome) {
//        this.chromName = chromosome;
//        return this;
//    }
//
//    public VariantBuilder withPosition(Integer position) {
//        this.chromPos = position;
//        return this;
//    }
//
//    public VariantBuilder withRefBase(String refBase) {
//        this.refBase = refBase;
//        return this;
//    }
//
//    public VariantBuilder withAltBase(String altBase) {
//        this.altBase = altBase;
//        return this;
//    }
//
//    public VariantBuilder withVWVariant(VWVariant variant) {
//        altBase = variant.getAlternateBases();
//        chromName = variant.getReferenceName();
//        chromPos = variant.getStart();
//        vepIdent = variant.getVepIdentifier();
//        refBase = variant.getReferenceBases();
//        return this;
//    }
//
//    public VariantBuilder withVariant(Variant variant) {
//        altBase = variant.getAlternateBase();
//        chromName = variant.getChromosomeName();
//        chromPos = variant.getPosition();
//        vepIdent = null;
//        refBase = variant.getReferenceBase();
//        return this;
//    }
//
//    public VWVariant buildVW() {
//        VWVariant variantVW = new VWVariant();
//        variantVW.setAlternateBases(altBase);
//        variantVW.setReferenceName(chromName);
//        variantVW.setStart(chromPos);
//        variantVW.setVepIdentifier(vepIdent);
//        variantVW.setReferenceBases(refBase);
//        return variantVW;
//    }
//
//    public Variant build() {
//        Variant variant = new Variant();
//        variant.setAlternateBase(altBase);
//        variant.setChromosomeName(chromName);
//        variant.setId(id);
//        variant.setPosition(chromPos);
//        variant.setReferenceBase(refBase);
//        variant.setDatasetId(datasetId);
//        if (vepIdent != null) {
//            MetaData meta = new MetaData();
//            meta.setDataKey("raw_variant");
//            meta.setDataValue(vepIdent);
//            variant.setMetaData(Lists.newArrayList(meta));
//        }
//        return variant;
//    }
//}

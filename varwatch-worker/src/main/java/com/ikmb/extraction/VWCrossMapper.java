package com.ikmb.extraction;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.ikmb.core.data.variant.Variant;
import com.ikmb.core.data.variant.VariantBuilder;
import com.ikmb.utils.VWConfiguration;
import com.ikmb.core.tools.CrossMap;
import com.ikmb.core.varwatchcommons.entities.GenomicFeature;
import com.ikmb.core.varwatchcommons.entities.VWVariant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author broder
 */
public class VWCrossMapper {

    private List<VWVariant> parsedVariants = new ArrayList<>();
    private List<VWVariant> errorVariants = new ArrayList<>();
    private ArrayList<VWVariant> variantList = new ArrayList<>();
    private String assembly;
    private CrossMap crossMap = new CrossMap();
    private VariantBuilder vbuilder = new VariantBuilder();

    public void setGenomicFeatures(List<GenomicFeature> genomicFeatures, String assembly) {
        this.assembly = assembly;
        for (GenomicFeature feature : genomicFeatures) {
            variantList.add(feature.getVariant());
        }
    }

    public void crossmap() {
        for (VWVariant variant : variantList) {
            VariantBuilder build = new VariantBuilder();
            Variant curVar = build.withVWVariant(variant).buildSql();
            Optional<Variant> resultVariant = crossMap.run(curVar, assembly, VWConfiguration.STANDARD_COORDS);
            parsedVariants.add(vbuilder.withVariant(resultVariant.get()).buildVW());
        }
    }

    public List<VWVariant> getErrorVariants() {
        return errorVariants;
    }

    public List<VWVariant> getMappedVariants() {
        return parsedVariants;
    }

    public List<GenomicFeature> getMappedFeatures() {
        List<GenomicFeature> features = new ArrayList<>();
        for (VWVariant variant : parsedVariants) {
            GenomicFeature feature = new GenomicFeature();
            feature.setVariant(variant);
            features.add(feature);
        }
        return features;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.extraction;

import com.ikmb.core.varwatchcommons.entities.GenomicFeature;
import com.ikmb.core.varwatchcommons.entities.VWVariant;
import com.ikmb.utils.VWUtils;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author broder
 */
public class TestAssemblyCrossMapper {

    public void shouldBeConvertedFrom37To38Correctly() {
        AssemblyCrossMapper crossMapper = new AssemblyCrossMapper();
        crossMapper.setTarget("GRCh38");

        List<GenomicFeature> features = new ArrayList<>();
        GenomicFeature feature1 = new GenomicFeature();
        feature1.setVariant(getVariant("X", 120438443, "C", "T", "GRCh37"));
        features.add(feature1);
        GenomicFeature feature2 = new GenomicFeature();
        feature2.setVariant(getVariant("1", 120438085, "C", "T", "GRCh37"));
        features.add(feature2);
        GenomicFeature feature3 = new GenomicFeature();
        feature3.setVariant(getVariant("1", 120438445, "C", "A", "GRCh37"));
        features.add(feature3);

        crossMapper.setGenomicFeatures(features, "GRCh37");
        crossMapper.setVcfParser(new VCFParser());
        crossMapper.setVwUtils(new VWUtils());
        crossMapper.crossmap();
        List<GenomicFeature> mappedFeatures = crossMapper.getMappedFeatures();
        System.out.println("finish");
    }

//    17_35545362_C/T_HP:0001249_HP:0000252_HP:0012758_HP:0001263
//12_102517717_-/A_HP:0001249_HP:0000252_HP:0012758_HP:0001263
//17_34871826_G/A_HP:0000252_HP:0012758_HP:0001249_HP:0001263
    private VWVariant getVariant(String chrom, int pos, String ref, String alt, String assembly) {
        VWVariant variant = new VWVariant();
        variant.setAlternateBases(alt);
//        variant.setAssembly(assembly);
        variant.setReferenceBases(ref);
        variant.setReferenceName(chrom);
        variant.setStart(pos);
        return variant;
    }
}

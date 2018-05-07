/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.extraction;

import com.ikmb.varwatchcommons.entities.GenomicFeature;
import com.ikmb.varwatchcommons.entities.VWVariant;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author broder
 */
public class TestHgvsConverter {
    
    //Todo not convertable hbgs strings in a parameter list
    //Todo check the result
    //Todo different assemblies

    public void shouldBeConvertable() {
        HgvsConverter converter = new HgvsConverter();
        converter.setVcfParser(new VCFParser());
        converter.setEnsemblVersion("EnsEMBL/83");
        List<GenomicFeature> features = new ArrayList<>();
        GenomicFeature feature1 = new GenomicFeature();
        feature1.setVariant(getVariantFromHGVS("1:g.120438443C>T", "GRCh37"));
        features.add(feature1);
        GenomicFeature feature2 = new GenomicFeature();
        feature2.setVariant(getVariantFromHGVS("1:g.120438085C>T", "GRCh37"));
        features.add(feature2);
        GenomicFeature feature3 = new GenomicFeature();
        feature3.setVariant(getVariantFromHGVS("1:g.120438445C>A", "GRCh37"));
        features.add(feature3);
        List<GenomicFeature> mapVariants = converter.mapVariants(features,"GRCh37");
        Assert.assertEquals(3, mapVariants.size());

        List<String> errorVariants = converter.getErrorVariants();
        Assert.assertEquals(0, errorVariants.size());
    }

    public void shouldNotBeConvertable() {
        HgvsConverter converter = new HgvsConverter();
        converter.setVcfParser(new VCFParser());
        converter.setEnsemblVersion("EnsEMBL/83");
        List<GenomicFeature> features = new ArrayList<>();
        GenomicFeature feature1 = new GenomicFeature();
        feature1.setVariant(getVariantFromHGVS("8:g.12952468_12952470delGinsAGTGT", "GRCh37"));
        features.add(feature1);
        GenomicFeature feature2 = new GenomicFeature();
        feature2.setVariant(getVariantFromHGVS("17:g.10347921_10357202cnv", "GRCh37"));
        features.add(feature2);
        GenomicFeature feature3 = new GenomicFeature();
        feature3.setVariant(getVariantFromHGVS("19:g.44039551_44039575delGACTGCCCCAAGTCCTTCTGCTAinsCACTGCCCCCAGGCCTTTTTCTTCCCCTCCCAGCTGGCGGGCCACCGCC", "GRCh37"));
        features.add(feature3);
        List<GenomicFeature> mapVariants = converter.mapVariants(features,"GRCh37");
        Assert.assertEquals(0, mapVariants.size());

        List<String> errorVariants = converter.getErrorVariants();
        Assert.assertEquals(3, errorVariants.size());
    }

    private VWVariant getVariantFromHGVS(String hgvs, String assembly) {
        VWVariant variant = new VWVariant();
        variant.setHgvs(hgvs);
//        variant.setAssembly(assembly);
        return variant;
    }
}

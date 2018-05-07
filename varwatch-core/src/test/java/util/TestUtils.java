package util;

import com.ikmb.varwatchcommons.entities.GenomicFeature;
import com.ikmb.varwatchcommons.entities.VWVariant;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author bfredrich
 */
public class TestUtils {

    public static List<GenomicFeature> setupNormalGenomicFeatures() {
        List<GenomicFeature> features = new ArrayList<GenomicFeature>();

        GenomicFeature feature1 = new GenomicFeature();
        VWVariant variant1 = new VWVariant();
        variant1.setAlternateBases("A");
        variant1.setStart(1000);
        variant1.setReferenceBases("G");
        variant1.setReferenceName("2");
        feature1.setVariant(variant1);
        features.add(feature1);

        GenomicFeature feature2 = new GenomicFeature();
        VWVariant variant2 = new VWVariant();
        variant2.setAlternateBases("G");
        variant2.setStart(2000);
        variant2.setReferenceBases("C");
        variant2.setReferenceName("3");
        feature2.setVariant(variant2);
        features.add(feature2);

        GenomicFeature feature3 = new GenomicFeature();
        VWVariant variant3 = new VWVariant();
        variant3.setAlternateBases("T");
        variant3.setStart(50);
        variant3.setReferenceBases("C");
        variant3.setReferenceName("2");
        feature3.setVariant(variant3);
        features.add(feature3);

        return features;
    }

    public static List<GenomicFeature> setupNormalGenomicFeatures37() {
        List<GenomicFeature> features = new ArrayList<GenomicFeature>();

        GenomicFeature feature1 = new GenomicFeature();
        VWVariant variant1 = new VWVariant();
        variant1.setReferenceName("1");
        variant1.setStart(1167659);
        variant1.setReferenceBases("A");
        variant1.setAlternateBases("G");
//        variant1.setAssembly("GRCh37");
        feature1.setVariant(variant1);
        features.add(feature1);

        GenomicFeature feature2 = new GenomicFeature();
        VWVariant variant2 = new VWVariant();
        variant2.setReferenceName("1");
        variant2.setStart(11855218);
        variant2.setReferenceBases("A");
        variant2.setAlternateBases("G");
//        variant2.setAssembly("GRCh37");
        feature2.setVariant(variant2);
        features.add(feature2);

        GenomicFeature feature3 = new GenomicFeature();
        VWVariant variant3 = new VWVariant();
        variant3.setReferenceName("1");
        variant3.setStart(11861356);
        variant3.setReferenceBases("C");
        variant3.setAlternateBases("A,T");
//        variant3.setAssembly("GRCh37");
        feature3.setVariant(variant3);
        features.add(feature3);

        return features;
    }

    public static List<GenomicFeature> setupNormalGenomicFeatures38() {
        List<GenomicFeature> features = new ArrayList<GenomicFeature>();

        GenomicFeature feature1 = new GenomicFeature();
        VWVariant variant1 = new VWVariant();
        variant1.setReferenceName("1");
        variant1.setStart(1232279);
        variant1.setReferenceBases("A");
        variant1.setAlternateBases("G");
//        variant1.setAssembly("GRCh38");
        feature1.setVariant(variant1);
        features.add(feature1);

        GenomicFeature feature2 = new GenomicFeature();
        VWVariant variant2 = new VWVariant();
        variant2.setReferenceName("1");
        variant2.setStart(11795161);
        variant2.setReferenceBases("A");
        variant2.setAlternateBases("G");
//        variant2.setAssembly("GRCh38");
        feature2.setVariant(variant2);
        features.add(feature2);

        GenomicFeature feature3 = new GenomicFeature();
        VWVariant variant3 = new VWVariant();
        variant3.setReferenceName("1");
        variant3.setStart(11801299);
        variant3.setReferenceBases("C");
        variant3.setAlternateBases("A,T");
//        variant3.setAssembly("GRCh38");
        feature3.setVariant(variant3);
        features.add(feature3);

        return features;
    }

    public static List<GenomicFeature> setupHGVSGenomicFeatures() {
        List<GenomicFeature> features = new ArrayList<GenomicFeature>();

        GenomicFeature feature1 = new GenomicFeature();
        VWVariant variant1 = new VWVariant();
        variant1.setHgvs("NG_005895.1:g.43894_43897delTGAG");
        feature1.setVariant(variant1);
        features.add(feature1);

        GenomicFeature feature2 = new GenomicFeature();
        VWVariant variant2 = new VWVariant();
        variant2.setHgvs("NG_029422.2:g.5738_5739insT");
        feature2.setVariant(variant2);
        features.add(feature2);

        GenomicFeature feature3 = new GenomicFeature();
        VWVariant variant3 = new VWVariant();
        variant3.setHgvs("NM_182763.2:c.688+403C>T");
        feature3.setVariant(variant3);
        features.add(feature3);

        return features;
    }
}

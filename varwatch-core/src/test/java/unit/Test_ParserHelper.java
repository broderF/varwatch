//package unit;
//
//import com.ikmb.varwatchcommons.entities.VWGenomicFeature;
//import com.ikmb.varwatchcommons.entities.VWVariant;
//import util.TestUtils;
//import com.ikmb.utils.ParserHelper;
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//import org.junit.Assert;
//import org.junit.Test;
//
///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
///**
// *
// * @author bfredrich
// */
//public class Test_ParserHelper {
//
//    @Test
//    public void test_getVariantInfoFromObject() {
//        List<VWGenomicFeature> features = TestUtils.setupNormalGenomicFeatures();
//        String responseNominal = "[{\"gene\":null,\"variant\":{\"assembly\":null,\"referenceName\":\"2\",\"start\":1000,\"end\":null,\"referenceBases\":\"G\",\"alternateBases\":\"A\",\"hgvs\":null,\"status\":null,\"vepIdentifier\":null},\"zygosity\":null,\"type\":null},{\"gene\":null,\"variant\":{\"assembly\":null,\"referenceName\":\"3\",\"start\":2000,\"end\":null,\"referenceBases\":\"C\",\"alternateBases\":\"G\",\"hgvs\":null,\"status\":null,\"vepIdentifier\":null},\"zygosity\":null,\"type\":null},{\"gene\":null,\"variant\":{\"assembly\":null,\"referenceName\":\"2\",\"start\":50,\"end\":null,\"referenceBases\":\"C\",\"alternateBases\":\"T\",\"hgvs\":null,\"status\":null,\"vepIdentifier\":null},\"zygosity\":null,\"type\":null}]";
//        byte[] bytes = ParserHelper.getVariantInfoFromObject(features);
//
//        String responseActual = new String(bytes, StandardCharsets.UTF_8);
//
//        // assert statements
//        Assert.assertEquals("Nominal and Actual should be the same", responseNominal, responseActual);
//    }
//
//    @Test
//    public void test_getVariantInfoFromByteArray() {
//        List<VWGenomicFeature> featuresNorminal = TestUtils.setupNormalGenomicFeatures();
//
//        String input = "[{\"gene\":null,\"variant\":{\"assembly\":null,\"referenceName\":\"2\",\"start\":1000,\"end\":null,\"referenceBases\":\"G\",\"alternateBases\":\"A\",\"status\":null},\"zygosity\":null,\"type\":null},{\"gene\":null,\"variant\":{\"assembly\":null,\"referenceName\":\"3\",\"start\":2000,\"end\":null,\"referenceBases\":\"C\",\"alternateBases\":\"G\",\"status\":null},\"zygosity\":null,\"type\":null},{\"gene\":null,\"variant\":{\"assembly\":null,\"referenceName\":\"2\",\"start\":50,\"end\":null,\"referenceBases\":\"C\",\"alternateBases\":\"T\",\"status\":null},\"zygosity\":null,\"type\":null}]";
//        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
//
//        List<VWGenomicFeature> featuresActual = ParserHelper.getVariantInfoFromByteArray(bytes);
//
//        // assert statements
//        Assert.assertEquals("Length should be the same", featuresNorminal.size(), featuresActual.size());
//
//        for (int i = 0; i < featuresNorminal.size(); i++) {
//            VWVariant variantNorminal = featuresNorminal.get(i).getVariant();
//            VWVariant variantActual = featuresActual.get(i).getVariant();
//            Assert.assertEquals("chromosome", variantNorminal.getReferenceName(), variantActual.getReferenceName());
//            Assert.assertEquals("position", variantNorminal.getStart(), variantActual.getStart());
//            Assert.assertEquals("refBase", variantNorminal.getReferenceBases(), variantActual.getReferenceBases());
//            Assert.assertEquals("altBase", variantNorminal.getAlternateBases(), variantActual.getAlternateBases());
//        }
//    }
//
//    @Test
//    public void test_json2vcf() {
//        List<VWGenomicFeature> features = TestUtils.setupNormalGenomicFeatures();
//        String vcfNorminal = "#CHROM	POS	ID	REF	ALT	QUAL	FILTER	INFO\n"
//                + "2	1000	1	G	A	.	.	.\n"
//                + "3	2000	1	C	G	.	.	.\n"
//                + "2	50	1	C	T	.	.	.\n";
//
//        String vcfActual = ParserHelper.json2vcf(features);
//        Assert.assertEquals(vcfNorminal, vcfActual);
//    }
//
//    @Test
//    public void test_json2hgvs() {
//        List<VWGenomicFeature> features = TestUtils.setupHGVSGenomicFeatures();
//        String hgvsNorminal = "NG_005895.1:g.43894_43897delTGAG\n"
//                + "NG_029422.2:g.5738_5739insT\n"
//                + "NM_182763.2:c.688+403C>T\n";
//
//        String hgvsActual = ParserHelper.json2hgvs(features);
//        Assert.assertEquals(hgvsNorminal, hgvsActual);
//    }
//
//}

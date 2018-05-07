///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package unit;
//
//import com.ikmb.varwatchcommons.entities.VWGenomicFeature;
//import com.ikmb.varwatchcommons.entities.VWVariant;
//import com.ikmb.extraction.CrossmapHelper;
//import com.ikmb.varwatchcommons.utils.VWConfiguration;
//import java.util.List;
//import org.junit.Assert;
//import org.junit.Test;
//import util.TestUtils;
//
///**
// *
// * @author bfredrich
// */
//public class Test_CrossmapHelper {
//
//    @Test
//    public void test_mapGenomicFeatures() {
//        List<VWGenomicFeature> inputVariants = TestUtils.setupNormalGenomicFeatures37();
//
//        List<VWGenomicFeature> norminalOutput = TestUtils.setupNormalGenomicFeatures38();
//
////        List<VWGenomicFeature> actualOutput = CrossmapHelper.mapGenomicFeatures(inputVariants, VWConfiguration.STANDARD_COORDS);
//
////        Assert.assertEquals(norminalOutput.size(), actualOutput.size());
////
////        for (int i = 0; i < norminalOutput.size(); i++) {
////            VWVariant norminalVariant = norminalOutput.get(i).getVariant();
////            VWVariant actualVariant = actualOutput.get(i).getVariant();
////            Assert.assertEquals(norminalVariant.getReferenceName(), actualVariant.getReferenceName());
////            Assert.assertEquals(norminalVariant.getStart(), actualVariant.getStart());
////            Assert.assertEquals(norminalVariant.getReferenceBases(), actualVariant.getReferenceBases());
////            Assert.assertEquals(norminalVariant.getAlternateBases(), actualVariant.getAlternateBases());
////            Assert.assertEquals(norminalVariant.getAssembly(), actualVariant.getAssembly());
////        }
//    }
//}

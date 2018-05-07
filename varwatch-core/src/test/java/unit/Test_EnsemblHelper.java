///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package unit;
//
//import com.ikmb.EnsemblHelper;
//import org.junit.Assert;
//import org.junit.Test;
//
///**
// *
// * @author bfredrich
// */
//public class Test_EnsemblHelper {
//
//    @Test
//    public void test_hgvs2vcf() {
//        String hgvs = "NM_182763.2:c.688+403C>T\n"
//                + "NP_000228.1:p.Asn318Ser\n"
//                + "NM_000237.2:c.953A>G\n";
//        String ensemblName = "EnsEMBL/82";
//
//        String vcfNorminal = "##fileformat=VCFv4.0\n"
//                + "#CHROM	POS	ID	REF	ALT	QUAL	FILTER	INFO\n"
//                + "1	150578440	NM_182763.2:c.688+403C>T	G	A	.	.	.\n"
//                + "8	19956018	NP_000228.1:p.Asn318Ser	A	G	.	.	.\n"
//                + "8	19956018	NM_000237.2:c.953A>G	A	G	.	.	.\n";
//
//        String vcfActual = EnsemblHelper.hgvs2vcf(hgvs, ensemblName);
//        Assert.assertEquals(vcfNorminal, vcfActual);
//    }
//}

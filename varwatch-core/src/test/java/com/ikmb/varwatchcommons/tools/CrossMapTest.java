/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchcommons.tools;

import com.ikmb.core.data.variant.Variant;
import com.ikmb.core.data.variant.VariantBuilder;
import com.ikmb.core.varwatchcommons.tools.CrossMap;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author broder
 */
public class CrossMapTest {

    //22:46615880 T / C
    Variant grch37Variant = new VariantBuilder().withChromosome("22").withPosition(46615880).withRefBase("T").withAltBase("C").buildSql();
    Variant grch38Variant = new VariantBuilder().withChromosome("22").withPosition(46219983).buildSql();
    CrossMap crossMap = new CrossMap();
    String assemblyFrom = "GRCh37";
    String assemblyTo = "GRCh38";
    String validResponse = "{\"mappings\":[{\"original\":{\"seq_region_name\":\"22\",\"strand\":1,\"coord_system\":\"chromosome\",\"end\":46615880,\"start\":46615880,\"assembly\":\"GRCh37\"},\"mapped\":{\"seq_region_name\":\"22\",\"strand\":1,\"coord_system\":\"chromosome\",\"end\":46219983,\"start\":46219983,\"assembly\":\"GRCh38\"}}]}";
    String invalidResponse = "{\"mappings\":[]}";

//    @Test
//    public void shouldRunCorrectCrossMap() {
//        CrossMap crossMapSpy = spy(CrossMap.class);
//        crossMapSpy.run(grch37Variant, assemblyFrom, assemblyTo);
//        verify(crossMapSpy).("https://rest.ensembl.org/map/human/GRCh37/22:46615880..46615880:1/GRCh38?content-type=application/json");
//
//    }

    @Test
    public void shouldparseCorrectVariantToRestCall() {
        String expectedRestCall = "/map/human/GRCh37/22:46615880..46615880:1/GRCh38";
        Assert.assertEquals(expectedRestCall, crossMap.buildRestVariant("22", 46615880, "GRCh37", "GRCh38"));
    }

//    @Test
//    public void shouldCrossMapCorrectVariant() {
//        String restCall = "https://rest.ensembl.org/map/human/GRCh37/22:46615880..46615880:1/GRCh38?content-type=application/json";
//        String realOutput = crossMap.crossMapVariant(restCall);
//        Assert.assertEquals(validResponse, realOutput);
//    }

    @Test
    public void shouldParseCorrectResponse() {
        Variant outputVariant = crossMap.run(grch37Variant, "GRCh37", "GRCh38").get();
        Assert.assertEquals("22", outputVariant.getChromosomeName());
        Assert.assertEquals(new Integer(46219983), outputVariant.getChromosomePos());
        Assert.assertEquals("T", outputVariant.getReferenceBase());
        Assert.assertEquals("C", outputVariant.getAlternateBase());
    }

    @Test
    public void shouldReturnNullForInvalidResponse() {
        Assert.assertNull(crossMap.getVariantFromResponse(invalidResponse));
    }
}

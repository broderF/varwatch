/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.tools.variant_parser;

import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.data.variant.Variant;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import util.JsonFileLoader;

/**
 *
 * @author broder
 */
public class VCFVariantParserTest {

    VCFVariantParser vcfParser = new VCFVariantParser();

    @Test
    public void shouldReturnCorrectResult() {
        DatasetVW dataset = mock(DatasetVW.class);
        when(dataset.getRawData()).thenReturn(JsonFileLoader.getStringFromFile("src/test/resources/vcf_variants.vcf").getBytes());
        List<Variant> variants = vcfParser.getVariants(dataset);
        Assert.assertEquals(22, variants.size());

        for (Variant variant : variants) {
            Assert.assertEquals("pass", variant.getFilter());
        }
    }

    int minVal;
}

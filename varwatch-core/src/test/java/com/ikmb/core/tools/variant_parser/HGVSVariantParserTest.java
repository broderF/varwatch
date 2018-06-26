/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.tools.variant_parser;

import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.data.variant.Variant;
import com.ikmb.core.tools.EnsemblHttpRequestHandler;
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
public class HGVSVariantParserTest {

    HGVSVariantParser hgvsParser = new HGVSVariantParser();

    @Test
    public void shouldGetCorrectAnswer() {
        DatasetVW dataset = mock(DatasetVW.class);
        when(dataset.getRawDataAssembly()).thenReturn("GRCh38");
        when(dataset.getRawData()).thenReturn(JsonFileLoader.getTrimmedStringFromFile("src/test/resources/hgvs_variants.json").getBytes());
        hgvsParser.setEnsemblRequestHandler(new EnsemblHttpRequestHandler());
        List<Variant> variants = hgvsParser.getVariants(dataset);
        Assert.assertEquals(3, variants.size());

        for (Variant variant : variants) {
            Assert.assertEquals("pass", variant.getFilter());
        }
    }

    @Test
    public void shouldNotBeAbleToConvertVariants() {
        DatasetVW dataset = mock(DatasetVW.class);
        when(dataset.getRawDataAssembly()).thenReturn("GRCh37");
        when(dataset.getRawData()).thenReturn(JsonFileLoader.getTrimmedStringFromFile("src/test/resources/hgvs_variants.json").getBytes());
        hgvsParser.setEnsemblRequestHandler(new EnsemblHttpRequestHandler());
        List<Variant> variants = hgvsParser.getVariants(dataset);
        Assert.assertEquals(3, variants.size());

        Variant failedVariant = variants.get(1);
        Assert.assertEquals("not pass:cant convert hgvs", failedVariant.getFilter());
    }
}

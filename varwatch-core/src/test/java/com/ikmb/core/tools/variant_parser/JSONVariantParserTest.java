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
public class JSONVariantParserTest {

    JSONVariantParser jsonParser = new JSONVariantParser();

    @Test
    public void shouldReturnCorrectResult() {
        DatasetVW dataset = mock(DatasetVW.class);
        when(dataset.getRawData()).thenReturn(JsonFileLoader.getTrimmedStringFromFile("src/test/resources/direct_variants.json").getBytes());
        List<Variant> variants = jsonParser.getVariants(dataset);
        Assert.assertEquals(1, variants.size());
        Variant curVariant = variants.get(0);
        Assert.assertEquals("1", curVariant.getChromosomeName());
        Assert.assertEquals(new Integer(123123), curVariant.getChromosomePos());
        Assert.assertEquals("T", curVariant.getReferenceBase());
        Assert.assertEquals("C", curVariant.getAlternateBase());

        for (Variant variant : variants) {
            Assert.assertEquals("pass", variant.getFilter());
        }
    }
}

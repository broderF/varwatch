/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.tools;

import com.ikmb.core.data.variant.Variant;
import com.ikmb.core.data.varianteffect.VariantEffect;
import com.ikmb.core.tools.VariantEffectPredictor;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author broder
 */
public class VariantEffectPredictorTest {

    Variant variant = new Variant();
    VariantEffectPredictor vep = new VariantEffectPredictor();

    @Test
    public void shouldPredictVariantEffect() {
        variant.setChromosomePos(6524705);
        variant.setReferenceBase("G");
        variant.setAlternateBase("T");
        variant.setChromosomeName("1");
        List<VariantEffect> variantEffects = vep.run(variant);
        Assert.assertEquals("get all variant effects", 3, variantEffects.size());
        
        VariantEffect variantEffect = variantEffects.get(0);
        Assert.assertEquals("MODIFIER",variantEffect.getImpactFactor());
        Assert.assertEquals("ENST00000377705",variantEffect.getTranscriptName());
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.filter;

import com.ikmb.core.data.config.FilterConfig;
import com.ikmb.core.data.variant.Variant;
import com.ikmb.core.data.varianteffect.VariantEffect;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import util.JsonFileLoader;

/**
 *
 * @author broder
 */
public class VariantFilterManagerTest {

    VariantFilterManager variantFilterManager;

    @Test
    public void shouldFilterVariantsWithMaf() throws JSONException {
        VariantFilter mafVariantFilter = createVariantFilterSetup("variant", "maf", "0.001", true, new AllelFrequencyFilter());
        variantFilterManager = new VariantFilterManager(Arrays.asList(mafVariantFilter), new ArrayList<>());

        Variant variant = createVariantSetup();
        boolean valid = variantFilterManager.isValid(variant, Arrays.asList(new VariantEffect()));
        Assert.assertTrue(valid);
    }

    @Test
    public void shouldNotFilterVariantsWithMaf() throws JSONException {
        VariantFilter mafVariantFilter = createVariantFilterSetup("variant", "maf", "0.0001", true, new AllelFrequencyFilter());
        variantFilterManager = new VariantFilterManager(Arrays.asList(mafVariantFilter), new ArrayList<>());

        Variant variant = createVariantSetup();
        boolean valid = variantFilterManager.isValid(variant, Arrays.asList(new VariantEffect()));
        Assert.assertFalse(valid);
    }

    @Test
    public void shouldFilterVariantEffectsWithCanonical() throws JSONException {
        VariantEffectFilter canonicalVariantEffectFilter = createVariantEffectFilterSetup("variant_effect", "canonical", null, true, new CanonicalFilter());
        variantFilterManager = new VariantFilterManager(new ArrayList<>(), Arrays.asList(canonicalVariantEffectFilter));

        Variant variant = createVariantSetup();
        VariantEffect variantEffect = new VariantEffect();
        variantEffect.setCanonicalTranscript(Boolean.TRUE);
        VariantEffect variantEffectNonCanonical = new VariantEffect();
        variantEffectNonCanonical.setCanonicalTranscript(Boolean.FALSE);
        boolean valid = variantFilterManager.isValid(variant, Arrays.asList(variantEffect, variantEffectNonCanonical));
        Assert.assertTrue(valid);
        List<VariantEffect> validVariantEffects = variantFilterManager.getValidVariantEffects();
        Assert.assertEquals(1, validVariantEffects.size());
    }

    @Test
    public void shouldNotFilterVariantEffects() throws JSONException {
        variantFilterManager = new VariantFilterManager(new ArrayList<>(), new ArrayList<>());

        Variant variant = createVariantSetup();
        VariantEffect canonicalVariantEffect = new VariantEffect();
        canonicalVariantEffect.setCanonicalTranscript(Boolean.TRUE);
        VariantEffect variantEffect = new VariantEffect();
        variantEffect.setCanonicalTranscript(Boolean.FALSE);
        boolean valid = variantFilterManager.isValid(variant, Arrays.asList(canonicalVariantEffect, variantEffect));
        Assert.assertTrue(valid);
        List<VariantEffect> validVariantEffects = variantFilterManager.getValidVariantEffects();
        Assert.assertEquals(2, validVariantEffects.size());
    }

    @Test
    public void shouldFilterVariantEffectsWithImpact() throws JSONException {
        VariantEffectFilter impactFactorVariantEffectFilter = createVariantEffectFilterSetup("variant_effect", "impact", "MODERATE", true, new ImpactFactorFilter());
        variantFilterManager = new VariantFilterManager(new ArrayList<>(), Arrays.asList(impactFactorVariantEffectFilter));

        Variant variant = createVariantSetup();
        VariantEffect impactfullVariantEffect = new VariantEffect();
        impactfullVariantEffect.setImpactFactor("HIGH");
        VariantEffect variantEffect = new VariantEffect();
        variantEffect.setImpactFactor("MODIFIER");
        boolean valid = variantFilterManager.isValid(variant, Arrays.asList(impactfullVariantEffect, variantEffect));
        Assert.assertTrue(valid);
        List<VariantEffect> validVariantEffects = variantFilterManager.getValidVariantEffects();
        Assert.assertEquals(1, validVariantEffects.size());
    }

    private String getVepResponseFromFile() {
        return JsonFileLoader.getStringFromFile("src/test/resources/vep_response.json");
    }

    private VariantEffectFilter createVariantEffectFilterSetup(String filterType, String filterName, String filterValue, boolean enabled, VariantEffectFilter variantFilter) {
        FilterConfig config = new FilterConfig();
        config.setEnabled(enabled);
        config.setValue(filterValue);
        config.setFilterType(filterType);
        config.setName(filterName);
        variantFilter.setFilterConfig(config);
        return variantFilter;
    }

    private VariantFilter createVariantFilterSetup(String filterType, String filterName, String filterValue, boolean enabled, VariantFilter variantFilter) {
        FilterConfig config = new FilterConfig();
        config.setEnabled(enabled);
        config.setValue(filterValue);
        config.setFilterType(filterType);
        config.setName(filterName);
        variantFilter.setFilterConfig(config);
        return variantFilter;
    }

    private Variant createVariantSetup() throws JSONException {
        Variant variant = new Variant();
        String vepResponseFromFile = getVepResponseFromFile();
        JSONObject jsonArray = new JSONObject(vepResponseFromFile);
        variant.setVepOutput(jsonArray);
        return variant;
    }
}

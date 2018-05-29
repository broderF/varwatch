/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.filter;

import com.ikmb.core.data.config.FilterConfig;
import com.ikmb.core.data.varianteffect.VariantEffect;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author broder
 */
public class ImpactFactorFilter implements VariantEffectFilter {

    private final static List<String> IMPACT_FACTORS_SORTED = new ArrayList<>();
    private String minImpactFactor;

    public ImpactFactorFilter() {
    }

    ImpactFactorFilter(FilterConfig variantFilter) {
       minImpactFactor = variantFilter.getValue();
    }

    @Override
    public boolean isValid(VariantEffect variantEffect) {
        String impactFactor = variantEffect.getImpactFactor();
        return impactFactor != null && IMPACT_FACTORS_SORTED.indexOf(impactFactor) <= IMPACT_FACTORS_SORTED.indexOf(minImpactFactor);
    }

    @Override
    public void setFilterConfig(FilterConfig config) {
        minImpactFactor = config.getValue();
    }

    static {
        IMPACT_FACTORS_SORTED.add("HIGH");
        IMPACT_FACTORS_SORTED.add("MODERATE");
        IMPACT_FACTORS_SORTED.add("LOW");
        IMPACT_FACTORS_SORTED.add("MODIFIER");
    }
}

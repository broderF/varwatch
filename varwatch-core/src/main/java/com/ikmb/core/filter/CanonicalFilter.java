/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.filter;

import com.ikmb.core.data.config.FilterConfig;
import com.ikmb.core.data.varianteffect.VariantEffect;

/**
 *
 * @author broder
 */
public class CanonicalFilter implements VariantEffectFilter {

    private String filterType;

    public CanonicalFilter() {
    }

    CanonicalFilter(FilterConfig variantFilter) {
    }

    @Override
    public boolean isValid(VariantEffect variantEffect) {
        return variantEffect.getCanonicalTranscript() != null && variantEffect.getCanonicalTranscript();
    }

    @Override
    public void setFilterConfig(FilterConfig config) {
    }

}

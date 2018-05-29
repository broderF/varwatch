/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.filter;

import com.google.inject.Inject;
import com.ikmb.core.data.config.ConfigurationManager;
import com.ikmb.core.data.config.FilterConfig;
import com.ikmb.core.data.variant.Variant;
import com.ikmb.core.data.varianteffect.VariantEffect;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author broder
 */
public class VariantFilterManager {

    private List<VariantFilter> variantFilter = new ArrayList<>();
    private List<VariantEffectFilter> variantEffectFilter = new ArrayList<>();
    private List<VariantEffect> validVariantEffects = new ArrayList<>();

    @Inject
    public VariantFilterManager(ConfigurationManager configManager, FilterFactory filterFactory) {
        List<FilterConfig> filterOptions = configManager.getFilterOptions();
        variantFilter = filterFactory.getVariantFilter(filterOptions);
        variantEffectFilter = filterFactory.getVariantEffectFilter(filterOptions);
    }

    public VariantFilterManager(List<VariantFilter> variantFilter, List<VariantEffectFilter> variantEffectFilter) {
        this.variantFilter = variantFilter;
        this.variantEffectFilter = variantEffectFilter;
    }

    public boolean isValid(Variant variant, List<VariantEffect> variantEffects) {
        return isValid(variant) && isValid(variantEffects);
    }

    public List<VariantEffect> getValidVariantEffects() {
        return validVariantEffects;
    }

    private boolean isValid(Variant variant) {
        return variantFilter.stream().noneMatch((curFilter) -> (!curFilter.isValid(variant)));
    }

    private boolean isValid(List<VariantEffect> variantEffects) {
        validVariantEffects = new ArrayList<>();
        for (VariantEffect variantEffect : variantEffects) {
            boolean isValid = true;
            for (VariantEffectFilter curFilter : variantEffectFilter) {
                if (!curFilter.isValid(variantEffect)) {
                    isValid = false;
                    break;
                }
            }
            if (isValid) {
                validVariantEffects.add(variantEffect);
            }
        }
        return !validVariantEffects.isEmpty();
    }
}

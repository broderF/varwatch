/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.filter;

import com.ikmb.core.data.config.FilterConfig;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author broder
 */
public class FilterFactory {

    VariantFilter getVariantFilter(FilterConfig variantFilter) {
        switch (variantFilter.getName()) {
            case "maf":
                return new AllelFrequencyFilter(variantFilter);
            default:
                return null;
        }
    }
    
    
    VariantEffectFilter getVariantEffectFilter(FilterConfig variantFilter) {
        switch (variantFilter.getName()) {
            case "canonical":
                return new CanonicalFilter(variantFilter);
            case "impact":
                return new ImpactFactorFilter(variantFilter);
            default:
                return null;
        }
    }

    List<VariantFilter> getVariantFilter(List<FilterConfig> filterOptions) {
        List<VariantFilter> variantFilters = new ArrayList<>();
        for (FilterConfig filterConfig : filterOptions) {
            if (filterConfig.getFilterType().equals("variant") && filterConfig.isEnabled()) {
                variantFilters.add(getVariantFilter(filterConfig));
            }
        }
        return variantFilters;
    }

    List<VariantEffectFilter> getVariantEffectFilter(List<FilterConfig> filterOptions) {
        List<VariantEffectFilter> variantFilters = new ArrayList<>();
        for (FilterConfig filterConfig : filterOptions) {
            if (filterConfig.getFilterType().equals("variant_effect") && filterConfig.isEnabled()) {
                variantFilters.add(getVariantEffectFilter(filterConfig));
            }
        }
        return variantFilters;
    }
}

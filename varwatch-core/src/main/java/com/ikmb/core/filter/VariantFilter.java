/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.filter;

import com.ikmb.core.data.config.FilterConfig;
import com.ikmb.core.data.variant.Variant;

/**
 *
 * @author broder
 */
public interface VariantFilter {

    boolean isValid(Variant variant);
    
    public void setFilterConfig(FilterConfig filterConfig);
}

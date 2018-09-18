/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.filter;

import com.ikmb.core.data.config.FilterConfig;
import com.ikmb.core.data.variant.Variant;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author broder
 */
public class AllelFrequencyFilter implements VariantFilter {

    private FilterConfig config;
    private double maf;

    public AllelFrequencyFilter() {
    }

    AllelFrequencyFilter(FilterConfig variantFilter) {
        this.maf = Double.parseDouble(variantFilter.getValue());
    }

    @Override
    public boolean isValid(Variant variant) {
        try {
            JSONObject vepOutput = variant.getVepOutput();
            if (vepOutput.has("colocated_variants")) {
                JSONArray colocatedVariants = vepOutput.getJSONArray("colocated_variants");
                if (colocatedVariants.length() > 0) {
                    JSONObject colocatedVariant = colocatedVariants.getJSONObject(0);
                    if (colocatedVariant.has("gnomad_maf")) {
                        double gnomadmaf = colocatedVariant.getDouble("gnomad_maf");
                        if (gnomadmaf <= maf) {
                            return true;
                        }else{
                            return false;
                        }
                    }
                }
            }
        } catch (JSONException ex) {
            Logger.getLogger(AllelFrequencyFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    @Override
    public void setFilterConfig(FilterConfig filterConfig) {
        this.config = filterConfig;
        this.maf = Double.parseDouble(filterConfig.getValue());
    }

}

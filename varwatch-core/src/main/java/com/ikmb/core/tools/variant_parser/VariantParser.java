/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.tools.variant_parser;

import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.data.variant.Variant;
import java.util.List;

/**
 *
 * @author broder
 */
public interface VariantParser {

    public List<Variant> getVariants(DatasetVW dataset);
}

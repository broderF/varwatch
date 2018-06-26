/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.tools.variant_parser;

import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.data.variant.Variant;
import com.ikmb.core.varwatchcommons.entities.GenomicFeature;
import com.ikmb.core.varwatchcommons.entities.VWVariant;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author broder
 */
public class JSONVariantParser implements VariantParser {

    @Override
    public List<Variant> getVariants(DatasetVW dataset) {
        List<GenomicFeature> variantInfoFromByteArray = com.ikmb.core.utils.VariantParser.getVariantInfoFromByteArray(dataset.getRawData());
        List<Variant> variants = variantInfoFromByteArray.stream().map((curFeature) -> {
            return getVariantFromVWVariant(curFeature.getVariant());
        }).collect(Collectors.toList());
        return variants;
    }

    private Variant getVariantFromVWVariant(VWVariant vwvariant) {
        Variant variant = new Variant();
        variant.setChromosomeName(vwvariant.getReferenceName());
        variant.setChromosomePos(vwvariant.getStart());
        variant.setReferenceBase(vwvariant.getReferenceBases());
        variant.setAlternateBase(vwvariant.getAlternateBases());
        variant.setVEPIdentifier(vwvariant.toCompactString());
        variant.setFilter("pass");
        return variant;
    }

}

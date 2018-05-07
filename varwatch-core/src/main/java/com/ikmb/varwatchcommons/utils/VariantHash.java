/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchcommons.utils;

import com.ikmb.varwatchcommons.entities.VWVariant;
import com.ikmb.varwatchcommons.entities.Variant;
import java.util.Set;

/**
 *
 * @author broder
 */
public class VariantHash {

    public static Integer MAX_BASE_LENGTH = 30;
    public static Integer MAX_HPO_NUM = 3;

    public String getVariantHash(VWVariant mmeVariant, Set<String> features) {
        String ref = mmeVariant.getReferenceBases().substring(0, Math.min(MAX_BASE_LENGTH, mmeVariant.getReferenceBases().length()));
        String alt = mmeVariant.getAlternateBases().substring(0, Math.min(MAX_BASE_LENGTH, mmeVariant.getAlternateBases().length()));
        String hash = mmeVariant.getReferenceName() + "_" + String.valueOf(mmeVariant.getStart()) + "_" + ref + "/" + alt;
        int i = 0;
        for (String hpo : features) {
            hash += "_" + hpo;
            i++;
            if (i > 3) {
                break;
            }
        }
        return hash;
    }

    public static String getVariantHash(Variant variant) {
        String ref = variant.getReferenceBase().substring(0, Math.min(MAX_BASE_LENGTH, variant.getReferenceBase().length()));
        String alt = variant.getAlternateBase().substring(0, Math.min(MAX_BASE_LENGTH, variant.getAlternateBase().length()));
        String hash = variant.getChromosomeName() + "_" + String.valueOf(variant.getPosition()) + "_" + ref + "/" + alt;
        return hash;
    }

    public static String getVariantHash(String chrom, Integer pos, String refBase, String altBase) {
        String ref = refBase.substring(0, Math.min(MAX_BASE_LENGTH, refBase.length()));
        String alt = altBase.substring(0, Math.min(MAX_BASE_LENGTH, altBase.length()));
        String hash = chrom + "_" + String.valueOf(pos) + "_" + ref + "/" + alt;
//        for (String hpo : features) {
//            hash += "_" + hpo;
//        }
        return hash;
    }
}

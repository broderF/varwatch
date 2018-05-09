/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.family;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.ikmb.core.data.gene.Gene;
import com.ikmb.core.data.variant.Variant;
import com.ikmb.core.data.variant.VariantDao;
import com.ikmb.core.data.varianteffect.VariantEffect;
//import com.ikmb.core.data.variant.VariantSQL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author broder
 */
public class FamilyDataManager {

    @Inject
    private VariantDao variantDao;

//    @Inject
//    private FamilyBuilder familyBuilder;

    @Transactional
    public List<GeneFamily> getFamilies(Long variantId) {
        Variant variant = variantDao.get(variantId);
        List<GeneFamily> familiesSql = new ArrayList<>();
        for (VariantEffect variantEffect : variant.getVariantEffects()) {
            Gene gene = variantEffect.getTranscript().getGene();
            Set<GeneFamily> currentFamilies = gene.getFamilies();
            familiesSql.addAll(currentFamilies);
        }
//        List<GeneFamily> families = new ArrayList<>();
//        for (GeneFamily currentPath : familiesSql) {
//            GeneFamily pathway = familyBuilder.withFamilySql(currentPath).build();
//            families.add(pathway);
//        }

        return familiesSql;
    }

}

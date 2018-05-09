/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.pathway;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.ikmb.core.data.gene.Gene;
import com.ikmb.core.data.variant.Variant;
import com.ikmb.core.data.variant.VariantDao;
import com.ikmb.core.data.varianteffect.VariantEffect;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author broder
 */
public class PathwayDataManager {

    @Inject
    private VariantDao variantDao;

//    @Inject
//    private PathwayBuilder pathwayBuilder;
    @Transactional
    public List<Pathway> getPathways(Long variantId) {
        Variant variant = variantDao.get(variantId);
        Set<Pathway> pathwaysSql = new HashSet<>();
        for (VariantEffect variantEffect : variant.getVariantEffects()) {
            Gene gene = variantEffect.getTranscript().getGene();
            Set<Pathway> currentPathways = gene.getPathways();
            pathwaysSql.addAll(currentPathways);
        }
//        List<Pathway> pathways = new ArrayList<>();
//        for(Pathway currentPath: pathwaysSql){
//            Pathway pathway = pathwayBuilder.withPathwaySql(currentPath).build();
//            pathways.add(pathway);
//        }

        return new ArrayList<>(pathwaysSql);
    }

}

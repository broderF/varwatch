/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.gene;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
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
public class GeneDataManager {

    @Inject
    private VariantDao variantDao;
    
//    @Inject
//    private GeneBuilder geneBuilder;

    @Transactional
    public List<Gene> getGenes(Long variantId) {
        Variant variant = variantDao.get(variantId);
        Set<Gene> genesSql = new HashSet<>();
        for (VariantEffect variantEffect : variant.getVariantEffects()) {
            Gene gene = variantEffect.getTranscript().getGene();
            genesSql.add(gene);
        }
//        List<Gene> genes = new ArrayList<>();
//        for(Gene currentGene: genesSql){
//            Gene pathway = geneBuilder.withGeneSql(currentGene).build();
//            genes.add(pathway);
//        }
//        
        return new ArrayList<>(genesSql);
    }

}

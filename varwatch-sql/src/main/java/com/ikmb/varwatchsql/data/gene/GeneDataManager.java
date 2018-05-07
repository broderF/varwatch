/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.data.gene;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.ikmb.varwatchcommons.entities.Gene;
import com.ikmb.varwatchsql.entities.VariantEffectSQL;
import com.ikmb.varwatchsql.variant_data.variant.VariantDao;
import com.ikmb.varwatchsql.variant_data.variant.VariantSQL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.PersistenceException;

/**
 *
 * @author broder
 */
public class GeneDataManager {

    @Inject
    private VariantDao variantDao;
    
    @Inject
    private GeneBuilder geneBuilder;

    @Transactional
    public List<Gene> getGenes(Long variantId) {
        VariantSQL variant = variantDao.get(variantId);
        Set<GeneSQL> genesSql = new HashSet<>();
        for (VariantEffectSQL variantEffect : variant.getVariantEffects()) {
            GeneSQL gene = variantEffect.getTranscript().getGene();
            genesSql.add(gene);
        }
        List<Gene> genes = new ArrayList<>();
        for(GeneSQL currentGene: genesSql){
            Gene pathway = geneBuilder.withGeneSql(currentGene).build();
            genes.add(pathway);
        }
        
        return genes;
    }

}

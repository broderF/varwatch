/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.data.pathway;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.ikmb.varwatchcommons.entities.Pathway;
import com.ikmb.varwatchsql.data.gene.GeneSQL;
import com.ikmb.varwatchsql.entities.PathwaySQL;
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
public class PathwayDataManager {

    @Inject
    private VariantDao variantDao;
    
    @Inject
    private PathwayBuilder pathwayBuilder;

    @Transactional
    public List<Pathway> getPathways(Long variantId) {
        VariantSQL variant = variantDao.get(variantId);
        Set<PathwaySQL> pathwaysSql = new HashSet<>();
        for (VariantEffectSQL variantEffect : variant.getVariantEffects()) {
            GeneSQL gene = variantEffect.getTranscript().getGene();
            Set<PathwaySQL> currentPathways = gene.getPathways();
            pathwaysSql.addAll(currentPathways);
        }
        List<Pathway> pathways = new ArrayList<>();
        for(PathwaySQL currentPath: pathwaysSql){
            Pathway pathway = pathwayBuilder.withPathwaySql(currentPath).build();
            pathways.add(pathway);
        }
        
        return pathways;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.data.family;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.ikmb.varwatchcommons.entities.Family;
import com.ikmb.varwatchsql.entities.FamilySQL;
import com.ikmb.varwatchsql.data.gene.GeneSQL;
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
public class FamilyDataManager {

    @Inject
    private VariantDao variantDao;
    
    @Inject
    private FamilyBuilder familyBuilder;

    @Transactional
    public List<Family> getFamilies(Long variantId) {
        VariantSQL variant = variantDao.get(variantId);
        Set<FamilySQL> familiesSql = new HashSet<>();
        for (VariantEffectSQL variantEffect : variant.getVariantEffects()) {
            GeneSQL gene = variantEffect.getTranscript().getGene();
            Set<FamilySQL> currentFamilies = gene.getFamilies();
            familiesSql.addAll(currentFamilies);
        }
        List<Family> families = new ArrayList<>();
        for(FamilySQL currentPath: familiesSql){
            Family pathway = familyBuilder.withFamilySql(currentPath).build();
            families.add(pathway);
        }
        
        return families;
    }

}

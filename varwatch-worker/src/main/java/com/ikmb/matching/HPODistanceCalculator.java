/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.matching;

import com.google.inject.Inject;
import com.ikmb.core.data.hpo.Phenotype;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author broder
 */
public class HPODistanceCalculator {

    private final Map<String, HPOPath> pathList;

    @Inject
    public HPODistanceCalculator(HPODistanceLoader hpoDistanceLoader) {
        pathList = hpoDistanceLoader.loadHPODistances();
    }

    public Double getHPOSetDistance(Set<Phenotype> queryHpoTerms, Set<Phenotype> targetHpoTerms) {
//        Set<Phenotype> qhpos = variant.getDataset().getPhenotypes();
//        Set<Phenotype> thpos = fixVariant.getDataset().getPhenotypes();

        Double maxSim = 0d;
        for (Phenotype qhpo : queryHpoTerms) {
            Double currentMaxSimilarity = 0d;
            for (Phenotype thpo : targetHpoTerms) {
                String hpoTerm1 = qhpo.getPhenotype().getIdentifier();
                String hpoTerm2 = thpo.getPhenotype().getIdentifier();
                Double currentSimilartiy = getHPODistance(hpoTerm1, hpoTerm2);
                if (currentSimilartiy > currentMaxSimilarity) {
                    currentMaxSimilarity = currentSimilartiy;
                }
            }
            maxSim += currentMaxSimilarity;
        }

        for (Phenotype thpo : targetHpoTerms) {
            Double currentMaxSimilarity = 0d;
            for (Phenotype qhpo : queryHpoTerms) {
                String hpoTerm1 = qhpo.getPhenotype().getIdentifier();
                String hpoTerm2 = thpo.getPhenotype().getIdentifier();
                Double currentSimilartiy = getHPODistance(hpoTerm2, hpoTerm1);
                if (currentSimilartiy > currentMaxSimilarity) {
                    currentMaxSimilarity = currentSimilartiy;
                }
            }
            maxSim += currentMaxSimilarity;
        }

        return maxSim / ((double) (queryHpoTerms.size() + targetHpoTerms.size()));
    }

    private Double getHPODistance(String hpoTerm1, String hpoTerm2) {
        HPOPath firstHPO = pathList.get(hpoTerm1);
        HPOPath secondHPO = pathList.get(hpoTerm2);
        if (firstHPO == null || secondHPO == null) {
            return 0.5;
        }
        return firstHPO.getSimilarity(secondHPO);
    }
}

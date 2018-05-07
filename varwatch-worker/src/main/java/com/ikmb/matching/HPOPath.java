/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.matching;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author broder
 */
public class HPOPath {

    public String mainHPO;
    public List<Set<String>> pathsToRoot = new ArrayList<>();

    public HPOPath(String mainHPO) {
        this.mainHPO = mainHPO;
    }

    public void addPath(Set<String> path) {
        pathsToRoot.add(path);
    }

    public String getMainHPO() {
        return mainHPO;
    }

    public List<Set<String>> getPathsToRoot() {
        return pathsToRoot;
    }

    public Double getSimilarity(HPOPath compPathHPO) {
        Double maxSimilarity = 0d;
        if (compPathHPO==null){
            return 0.5;
        }

        for (Set path : pathsToRoot) {
            for (Set pathComp : compPathHPO.getPathsToRoot()) {
//                if (path.size() < 2 || pathComp.size() < 2) {
//                    continue; //only containing the main hpo term
//                }
                Set union = new HashSet<>(path);
                union.addAll(pathComp);

                Set intersection = new HashSet<>(path);
                intersection.retainAll(pathComp);

                Double alpha = 1.0;
                Double similarity = intersection.size() / ((double) intersection.size() + alpha);

                if (similarity > maxSimilarity) {
                    maxSimilarity = similarity;
                }
            }
        }
        System.out.println("Single hpodist: " + maxSimilarity);
        return maxSimilarity;
    }

}

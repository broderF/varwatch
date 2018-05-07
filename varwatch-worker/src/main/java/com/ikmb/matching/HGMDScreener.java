///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.ikmb.matching;
//
//import com.ikmb.varwatchsql.databasehelper.MatchingDatabaseHelper;
//import com.ikmb.varwatchsql.entities.DatasetHGMDSQL;
//import com.ikmb.varwatchsql.entities.DatasetVWSQL;
//import com.ikmb.varwatchsql.entities.ExternalVariantSQL;
//import com.ikmb.varwatchsql.entities.HPOTermDistSQL;
//import com.ikmb.varwatchsql.entities.HPOTermSQL;
//import com.ikmb.varwatchsql.entities.MatchGroupSQL;
//import com.ikmb.varwatchsql.entities.MatchSQL;
//import com.ikmb.varwatchsql.int_data.reference_db.RefDatabaseSQL;
//import com.ikmb.varwatchsql.ext_data.variant.VariantSQL;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
///**
// *
// * @author bfredrich
// */
//public class HGMDScreener implements DatabaseScreener {
//
//    private Map<Long, MatchGroupSQL> _matches = new HashMap<Long, MatchGroupSQL>();
//    private DatasetVWSQL _dataset;
//    private RefDatabaseSQL _database;
//    private int MAX_MATCH = 5;
//
//    @Override
//    public void run() {
//        for (VariantSQL variant : _dataset.getVariants()) {
//
//            Set<MatchSQL> matches = new HashSet<MatchSQL>();
//            MatchGroupSQL matchgrp = new MatchGroupSQL();
//            matchgrp.setDatabase(_database);
//
//            //equal gene,pathway,family 
//            if (matches.size() < MAX_MATCH) {
//                List<DatasetHGMDSQL> currentsimvariants = MatchingDatabaseHelper.getSimilarHGMDVariants(variant, true, true, true);
//                System.out.println(currentsimvariants.size());
//                addToMatches(matches, variant, currentsimvariants, matchgrp, true, true, true);
//            }
//
//            //equal pathway,family; not equal gene  
//            if (matches.size() < MAX_MATCH) {
//                List<DatasetHGMDSQL> currentsimvariants = MatchingDatabaseHelper.getSimilarHGMDVariants(variant, false, true, true);
//                System.out.println(currentsimvariants.size());
//                addToMatches(matches, variant, currentsimvariants, matchgrp, false, true, true);
//            }
//            //equal pathway; not equal gene,family
//            if (matches.size() < MAX_MATCH) {
//                List<DatasetHGMDSQL> currentsimvariants = MatchingDatabaseHelper.getSimilarHGMDVariants(variant, false, true, false);
//                System.out.println(currentsimvariants.size());
//                addToMatches(matches, variant, currentsimvariants, matchgrp, false, true, false);
//            }
//            //equal family; not equal gene,pathway  
//            if (matches.size() < MAX_MATCH) {
//                List<DatasetHGMDSQL> currentsimvariants = MatchingDatabaseHelper.getSimilarHGMDVariants(variant, false, false, true);
//                System.out.println(currentsimvariants.size());
//                addToMatches(matches, variant, currentsimvariants, matchgrp, false, false, true);
//            }
//            if (!matches.isEmpty()) {
//                matchgrp.setMatches(matches);
//
//                _matches.put(variant.getId(), matchgrp);
//            }
//
//        }
//        System.out.println(_matches.size());
//    }
//
//    private Double getHPODistance(VariantSQL variant, DatasetHGMDSQL fixVariant) {
//        Set<HPOTermSQL> qhpos = variant.getDataset().getPhenotypes();
//
//        Set<HPOTermSQL> thpos = fixVariant.getPhenotypes();
//
//        Map<HPOTermSQL, HPOTermDistSQL> minGenes = new HashMap<HPOTermSQL, HPOTermDistSQL>();
//        for (HPOTermSQL qhpo : qhpos) {
//            HPOTermDistSQL minPairwiseDistances = MatchingDatabaseHelper.getMinPairwiseHPODistances(qhpo, qhpos);
//            minGenes.put(qhpo, minPairwiseDistances);
//        }
//
//        for (HPOTermSQL thpo : thpos) {
//            HPOTermDistSQL minPairwiseDistances = MatchingDatabaseHelper.getMinPairwiseHPODistances(thpo, thpos);
//            minGenes.put(thpo, minPairwiseDistances);
//        }
//
//        Double dist = 1d;
//        for (HPOTermDistSQL geneDistance : minGenes.values()) {
//            dist += geneDistance.getDistQ();
//        }
//        return dist / (double) minGenes.values().size();
//    }
//
//    @Override
//    public List<MatchGroupSQL> getMatches() {
//        return new ArrayList<MatchGroupSQL>(_matches.values());
//    }
//
//    @Override
//    public void initialize(RefDatabaseSQL database, DatasetVWSQL dataset) {
//        _database = database;
//        _dataset = dataset;
//    }
//
//    @Override
//    public DatasetVWSQL getDataset() {
//        return _dataset;
//    }
//
//    @Override
//    public RefDatabaseSQL getDatabase() {
//        return _database;
//    }
//
//    private void addToMatches(Set<MatchSQL> matches, VariantSQL variant, List<DatasetHGMDSQL> currentsimvariants, MatchGroupSQL matchGrp, boolean equalGene, boolean equalPath, boolean equalFam) {
//        for (DatasetHGMDSQL currentVar : currentsimvariants) {
//            MatchSQL match = new MatchSQL();
//            match.setEqualGene(equalGene);
//            match.setEqualPath(equalPath);
//            match.setEqualFam(equalPath);
//            Set<VariantSQL> variants = new HashSet<VariantSQL>();
//            variants.add(variant);
//            match.setVariants(variants);
//            Set<ExternalVariantSQL> externalVariants = new HashSet<ExternalVariantSQL>();
//            ExternalVariantSQL externalVariant = new ExternalVariantSQL();
//            externalVariant.setAccIdent(currentVar.getHgmdAcc());
//            externalVariant.setAccNr(currentVar.getId());
//            externalVariants.add(externalVariant);
//            match.setExternalVariants(externalVariants);
//            match.setMatchGroup(matchGrp);
//            Double hpoDist = getHPODistance(variant, currentVar);
//            match.setHpoDist(hpoDist);
//            if (currentVar.getChrName().equals(variant.getChromosomeName()) && currentVar.getChrPos().equals(variant.getChromosomePos())) {
//                match.setIdentical(true);
//            } else {
//                match.setIdentical(false);
//            }
//            matches.add(match);
//        }
//    }
//}

package com.ikmb.matching;

///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.ikmb.externaldb;
//
//import com.ikmb.entities.DatasetVWSQL;
//import com.ikmb.entities.MatchGroupSQL;
//import com.ikmb.entities.MatchSQL;
//import com.ikmb.entities.RefDatabaseSQL;
//import com.ikmb.entities.VariantSQL;
//import com.ikmb.matching.IdenticalVariationDist;
//import com.ikmb.databasehelper.VariantDatabaseHelper;
//import com.ikmb.entities.DatasetSQL;
//import com.ikmb.matching.VariantMatcher;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import org.ejml.simple.SimpleMatrix;
//import org.joda.time.DateTime;
//
///**
// *
// * @author bfredrich
// */
//public class VarWatchScreener implements DatabaseScreener {
//
//    private Map<Long, MatchGroupSQL> _matches = new HashMap<Long, MatchGroupSQL>();
//    private DatasetVWSQL _dataset;
//    private RefDatabaseSQL _database;
//    private VariantMatcher _variantMatcher;
//
//    @Override
//    public void run() {
//        List<Long> availableDatasets = VariantDatabaseHelper.getIDsOfAvailableDatasets();
//        for (Long id : availableDatasets) {
//            if (_dataset.getId().equals(id)) {
//                continue;
//            }
//            DatasetVWSQL targetDataset = VariantDatabaseHelper.getDatasetByID(id);
//            for (VariantSQL qVariant : _dataset.getVariants()) {
////                Set<MatchSQL> matches = new HashSet<MatchSQL>();
//                for (VariantSQL tVariant : targetDataset.getVariants()) {
//                    Double calcMahalanobisDistance = _variantMatcher.calcMahalanobisDistance(qVariant, tVariant);
//                    if (calcMahalanobisDistance < 0.5) {
//                        MatchSQL match = new MatchSQL();
//                        match.setVariant(qVariant);
//                        match.setAccessionNr(tVariant.getId().toString());
//                        match.setDistance(calcMahalanobisDistance);
//                        match.setCreationTimestamp(new DateTime());
////                        matches.add(match);
//
//                        MatchSQL reverseMatch = new MatchSQL();
//                        reverseMatch.setVariant(tVariant);
//                        reverseMatch.setAccessionNr(qVariant.getId().toString());
//                        reverseMatch.setDistance(calcMahalanobisDistance);
//                        reverseMatch.setCreationTimestamp(new DateTime());
////                        matches.add(match);
//
//                        if (_matches.containsKey(qVariant.getId())) {
//                            MatchGroupSQL matchgrp = _matches.get(qVariant.getId());
//                            matchgrp.addMatches(match);
//                            matchgrp.addMatches(reverseMatch);
//
//                        } else {
//                            MatchGroupSQL matchgrp = new MatchGroupSQL();
//                            matchgrp.setDatabase(_database);
//                            matchgrp.addMatches(match);
//                            matchgrp.addMatches(reverseMatch);
//                            _matches.put(qVariant.getId(), matchgrp);
//
//                        }
//                    }
//
//                }
//            }
//        }
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
//
//        _variantMatcher = new VariantMatcher();
////        _variantMatcher.addDimention(new ChromosomeDist());
////        _variantMatcher.addDimention(new PositionDist());
////        _variantMatcher.addDimention(new GeneDist());
////        _variantMatcher.addDimention(new PolyphenDist());
////        _variantMatcher.addDimention(new SiftDist());
////        _variantMatcher.addDimention(new PhänotypeDist());
////        _variantMatcher.addDimention(new FamilyDist());
////        _variantMatcher.addDimention(new PathwayDist());
//        _variantMatcher.addDimention(new IdenticalVariationDist());
//
//        SimpleMatrix weight = SimpleMatrix.identity(_variantMatcher.getDimensions().size());
//        _variantMatcher.setWeightMatrix(weight);
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
//}

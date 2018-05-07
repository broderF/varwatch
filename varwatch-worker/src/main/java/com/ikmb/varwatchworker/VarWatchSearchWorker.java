///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.ikmb.varwatchworker;
//
//import com.ikmb.beans.ComparedDatasetSQL;
//import com.ikmb.beans.DatasetSQL;
//import com.ikmb.beans.SimilarVariantSQL;
//import com.ikmb.matcher.VariantMatcher;
//import com.ikmb.varwatchsql.DatabaseJobHelper;
//import com.ikmb.varwatchsql.DatabaseVariantHelper;
//import java.util.List;
//
///**
// *
// * @author Broder
// */
//public class VarWatchSearchWorker extends AbstractWorker {
//
//    private DatasetSQL _dataset;
//    private DatasetSQL _referenceDataset;
//    private List<SimilarVariantSQL> _similarVariants;
//
//    @Override
//    public void getInput() {
//        _dataset = DatabaseVariantHelper.getDatasetByID(Integer.parseInt(_inputParameters.get("dataset_id")));
//        _referenceDataset = DatabaseVariantHelper.getDatasetByID(Integer.parseInt(_inputParameters.get("reference_dataset_id")));
//    }
//
//    @Override
//    public void work() {
//        VariantMatcher varMatcher = new VariantMatcher();
//        varMatcher.init();
//        varMatcher.searchVariants(_dataset, _referenceDataset);
//        _similarVariants = varMatcher.getSimilarVariants();
//    }
//
//    @Override
//    public void writeOutput() {
//        ComparedDatasetSQL compDataset = DatabaseVariantHelper.persistComparedDataset(_dataset, _referenceDataset);
//        if (!_similarVariants.isEmpty()) {
//            _similarVariants = DatabaseVariantHelper.persistSimilarVariants(_similarVariants, compDataset);
//        }
//    }
//
//    @Override
//    public void createFollowingJobs() {
////        for (SimilarVariantSQL variant : _similarVariants) {
////            DatabaseJobHelper.createFollowJob(_analysisJobSQL, _dataset.getId().toString() + "," + variant.getId() + ",varwatch_similar_search", "notification");
////        }
//    }
//
//    @Override
//    public void revert() {
//    }
//
//    @Override
//    public Boolean checkPreconditions() {
//        return true;
//    }
//
//}

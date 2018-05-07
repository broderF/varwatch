///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.ikmb.varwatchworker;
//
//import com.ikmb.beans.DatasetSQL;
//import com.ikmb.beans.ReferenceDBSQL;
//import com.ikmb.beans.ReferenceDBVariantSQL;
//import com.ikmb.beans.VariantSQL;
//import com.ikmb.varwatchsql.DatabaseJobHelper;
//import com.ikmb.varwatchsql.DatabaseVariantHelper;
//import java.util.List;
//
///**
// *
// * @author Broder
// */
//public class ScreenReferenceDBWorker extends AbstractWorker {
//
//    private DatasetSQL _dataset;
//    private ReferenceDBSQL _referenceDBSQL;
//    private Integer _externalDBID;
////    private List<VariantSQL> _foundVariants;
//    private List<ReferenceDBVariantSQL> _persistVariantsInReferenceDBs;
//
//    @Override
//    public void getInput() {
//        _dataset = DatabaseVariantHelper.getDatasetByID(Integer.parseInt(_inputParameters.get("dataset_id")));
//        _externalDBID = Integer.parseInt(_inputParameters.get("external_db"));
//        _referenceDBSQL = DatabaseVariantHelper.getReferenceDBByID(_externalDBID);
//    }
//
//    @Override
//    public void work() {
//        ScreeningLauncher externalDBSearcher = new ScreeningLauncher();
//        externalDBSearcher.searchVariants(_dataset, _referenceDBSQL.getName(), _referenceDBSQL.getSqlPath());
//        _persistVariantsInReferenceDBs = externalDBSearcher.getFoundVariants();
//    }
//
//    @Override
//    public void writeOutput() {
//        _persistVariantsInReferenceDBs = DatabaseVariantHelper.persistVariantsInReferenceDBs(_dataset, _persistVariantsInReferenceDBs, _referenceDBSQL);
//    }
//
//    @Override
//    public void createFollowingJobs() {
////        for (ReferenceDBVariantSQL variant : _persistVariantsInReferenceDBs) {
////            DatabaseJobHelper.createFollowJob(_analysisJobSQL, _dataset.getId().toString() + "," + variant.getId() + ",external_db_search", "notification");
////        }
//    }
//
//    @Override
//    public void revert() {
//        DatabaseVariantHelper.removeOldReferenceVariants(_dataset, _referenceDBSQL);
//    }
//
//    @Override
//    public Boolean checkPreconditions() {
//        return true;
//    }
//}

///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.ikmb.varwatchworker;
//
//import com.ikmb.submit.job.AnalysisJobSQL;
//import com.ikmb.entities.DatasetVWSQL;
//import com.ikmb.entities.MatchGroupSQL;
//import com.ikmb.databasehelper.WorkflowDatabaseHelper;
//import com.ikmb.databasehelper.StatusDatabaseHelper;
//import com.ikmb.databasehelper.VariantDatabaseHelper;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// *
// * @author bfredrich
// */
//public class CollectScreeningResultWorker extends AbstractWorker {
//
//    private DatasetVWSQL _dataset;
//    private List<MatchGroupSQL> _notificationMatchGroups = new ArrayList<MatchGroupSQL>();
////    private List<VariantSQL> _notifiedVariants = new ArrayList<VariantSQL>();
//
//    @Override
//    public void getInput() {
//        _dataset = VariantDatabaseHelper.getDatasetByID(Long.parseLong(_inputParameters.get("dataset_id")));
//    }
//
//    @Override
//    public void work() {
////        _notificationMatchGroups = VariantDatabaseHelper.getMatchGroupsToNotifiedByDataset(_dataset);
////        _notifiedVariants = DatabaseVariantHelper.getNotifiedVariants(_dataset);
//
//    }
//
//    @Override
//    public void writeOutput() {
////        StatusDatabaseHelper.setMatchedStatus(_notificationMatchGroups);
//        StatusDatabaseHelper.setNotMatchedStatus(_dataset);
//    }
//
//    @Override
//    public void createFollowingJobs() {
////        for (MatchGroupSQL matchGroup : _notificationMatchGroups) {
////            WorkflowDatabaseHelper.createFollowJob(_analysisJobSQL, _dataset, matchGroup.getId().toString(), "notification");
////        }
//    }
//
//    @Override
//    public Boolean checkPreconditions() {
//        List<AnalysisJobSQL> referenceDBJobs = WorkflowDatabaseHelper.getScreeningJobs(_dataset);
//        for (AnalysisJobSQL analysisJob : referenceDBJobs) {
//            if (!analysisJob.getStatus().equals("DONE")) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    @Override
//    public void revert() {
//    }
//
//}

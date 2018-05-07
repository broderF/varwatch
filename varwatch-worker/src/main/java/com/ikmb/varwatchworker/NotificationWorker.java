///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.ikmb.varwatchworker;
//
//import com.ikmb.entities.DatasetSQL;
//import com.ikmb.entities.VariantSQL;
//import com.ikmb.notification.NotificationInfoParser;
//import com.ikmb.notification.NotificationService;
//import com.ikmb.databasehelper.VariantDatabaseHelper;
//
///**
// *
// * @author Broder
// */
//public class NotificationWorker extends AbstractWorker {
//
//    private DatasetSQL _dataset;
//    private VariantSQL _variant;
////    private List<SimilarVariantSQL> _notifiedVariants = new ArrayList<SimilarVariantSQL>();
//
//    @Override
//    public void getInput() {
//        _dataset = VariantDatabaseHelper.getDatasetByID(Long.parseLong(_inputParameters.get("dataset_id")));
//        _variant = VariantDatabaseHelper.getVariantByID(Integer.parseInt(_inputParameters.get("variant_id")));
//    }
//
//    @Override
//    public void work() {
//        NotificationService.sendEmail(new NotificationInfoParser(_variant));
//    }
//
//    @Override
//    public void writeOutput() {
//        VariantDatabaseHelper.notifiedVariantMatchGroups(_variant);
//    }
//
//    @Override
//    public void createFollowingJobs() {
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

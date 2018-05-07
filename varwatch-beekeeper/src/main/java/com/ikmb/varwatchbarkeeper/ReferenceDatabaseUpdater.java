///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.ikmb.varwatchbarkeeper;
//
//import com.ikmb.databasehelper.VariantDatabaseHelper;
//import com.ikmb.databasehelper.WorkflowDatabaseHelper;
//import com.ikmb.entities.RefDatabaseSQL;
//import java.util.List;
//import java.util.TimerTask;
//
//
///**
// *
// * @author bfredrich
// */
//public class ReferenceDatabaseUpdater extends TimerTask {
//
//    @Override
//    public void run() {
//        List<Integer> iDsOfAvailableDatasets = VariantDatabaseHelper.getIDsOfAvailableDatasets();
//        List<RefDatabaseSQL> newDatabases = VariantDatabaseHelper.getUpdatableDatabases();
//        for (RefDatabaseSQL refDB : newDatabases) {
//            for (Integer id : iDsOfAvailableDatasets) {
////                DatasetSQL datasetByID = DatabaseVariantHelper.getDatasetByID(id);
////            List<ReferenceDBSQL> newDatabases = DatabaseVariantHelper.getUpdatableDatabases(datasetByID);
//                String module = "external_db_search";
//                String inputParameter = id.toString() + "," + refDB.getId();
//                WorkflowDatabaseHelper.createJob(inputParameter, module, true);
//            }
//        }
//    }
//
//}

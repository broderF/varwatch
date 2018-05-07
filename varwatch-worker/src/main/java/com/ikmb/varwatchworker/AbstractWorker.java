///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.ikmb.varwatchworker;
//
//import com.ikmb.submit.job.AnalysisJobSQL;
//import com.ikmb.entities.AnalysisSQL;
//import com.ikmb.entities.AnalysisWorkerSQL;
//import com.ikmb.databasehelper.WorkflowDatabaseHelper;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// *
// * @author Broder
// */
//public abstract class AbstractWorker implements Worker {
//
//    protected AnalysisWorkerSQL _workerSQL;
//    protected AnalysisSQL _analysisSQL;
//    protected AnalysisJobSQL _analysisJobSQL;
//    protected Map<String, String> _inputParameters = new HashMap<String, String>();
//
//    @Override
//    public void loadInputParameters() {
//        String[] parameters = new String[0];
//        if (_analysisSQL.getAdditionalParameters() != null) {
//            parameters = _analysisSQL.getAdditionalParameters().split(",");
//        }
//
//        String[] inputParameters = new String[0];
//        if (_analysisJobSQL.getAdditionalParameters() != null) {
//            inputParameters = _analysisJobSQL.getAdditionalParameters().split(",");
//        }
//
//        _inputParameters.put("dataset_id", _analysisJobSQL.getDataset().getId().toString());
//        for (int i = 0; i < parameters.length; i++) {
//            _inputParameters.put(parameters[i], inputParameters[i]);
//        }
//    }
//
//    @Override
//    public void finishJob(Long runtime) {
//        WorkflowDatabaseHelper.finishJob(_analysisJobSQL, runtime);
//    }
//
//    @Override
//    public void setWorkerSQL(AnalysisWorkerSQL workerSQL) {
//        _workerSQL = workerSQL;
//    }
//
//    @Override
//    public void setAnalysisSQL(AnalysisSQL analysisSQL) {
//        _analysisSQL = analysisSQL;
//    }
//
//    @Override
//    public void setAnalysisJobSQL(AnalysisJobSQL analysisJobSQL) {
//        _analysisJobSQL = analysisJobSQL;
//    }
//
//}

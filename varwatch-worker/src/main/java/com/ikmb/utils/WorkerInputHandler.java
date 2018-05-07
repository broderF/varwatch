/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.utils;

import com.ikmb.varwatchsql.entities.AnalysisSQL;
import com.ikmb.varwatchsql.workflow.job.AnalysisJobSQL;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author broder
 */
public class WorkerInputHandler {

    private static String DATASET_ID = "dataset_id";

    private AnalysisSQL _analysisSQL;
    private AnalysisJobSQL _analysisJobSQL;
    private Map<String, String> _inputParameters = new HashMap<>();

    public void setAnalysis(AnalysisSQL analysisSQL) {
        _analysisSQL = analysisSQL;
    }

    public void setJob(AnalysisJobSQL analysisJobSQL) {
        _analysisJobSQL = analysisJobSQL;
    }

    public void parseInputData() {
        String[] parameters = new String[0];
        if (_analysisSQL.getAdditionalParameters() != null) {
            parameters = _analysisSQL.getAdditionalParameters().split(",");
        }

        String[] inputParameters = new String[0];
        if (_analysisJobSQL.getAdditionalParameters() != null) {
            inputParameters = _analysisJobSQL.getAdditionalParameters().split(",");
        }

        if (_analysisJobSQL.getDataset() != null) {
            _inputParameters.put(DATASET_ID, _analysisJobSQL.getDataset().getId().toString());
        }
        for (int i = 0; i < parameters.length; i++) {
            _inputParameters.put(parameters[i], inputParameters[i]);
        }
    }

    public Long getDatasetID() {
        return Long.parseLong(_inputParameters.get(DATASET_ID));
    }

    public Long getLong(String type) {
        return Long.parseLong(_inputParameters.get(type));
    }

    public Integer getInteger(String type) {
        return Integer.parseInt(_inputParameters.get(type));
    }

}

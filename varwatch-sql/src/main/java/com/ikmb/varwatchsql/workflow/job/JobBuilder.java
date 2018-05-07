/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.workflow.job;

import com.ikmb.varwatchsql.entities.AnalysisSQL;
import com.ikmb.varwatchsql.entities.DatasetVWSQL;
import org.joda.time.DateTime;

/**
 *
 * @author broder
 */
public class JobBuilder {

    private AnalysisSQL analysis;
    private DatasetVWSQL dataset;
    private String additionalParam;
    private String action;

    public JobBuilder withAnalysis(AnalysisSQL analysis) {
        this.analysis = analysis;
        return this;
    }

    public JobBuilder withDataset(DatasetVWSQL dataset) {
        this.dataset = dataset;
        return this;
    }

    public AnalysisJobSQL buildNewJob() {
        AnalysisJobSQL analysisJob = new AnalysisJobSQL();
        analysisJob.setAnalysis(analysis);
        analysisJob.setDataset(dataset);
        analysisJob.setStatus(JobStatus.READY.toString());
        analysisJob.setSubmitted(new DateTime());
        analysisJob.setRetryCount(0);
        analysisJob.setAdditionalParameters(additionalParam);
        analysisJob.setAction(action);
        return analysisJob;
    }

    public JobBuilder withAdditionalParam(String additionalParameters) {
        this.additionalParam = additionalParameters;
        return this;
    }

    public JobBuilder withActionProp(String action) {
        this.action = action;
        return this;
    }

    public enum JobStatus {
        READY;
    }
}

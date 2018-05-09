/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.workflow.job;

import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.workflow.analysis.Analysis;
import org.joda.time.DateTime;

/**
 *
 * @author broder
 */
public class JobBuilder {

    private Analysis analysis;
    private DatasetVW dataset;
    private String additionalParam;
    private String action;

    public JobBuilder withAnalysis(Analysis analysis) {
        this.analysis = analysis;
        return this;
    }

    public JobBuilder withDataset(DatasetVW dataset) {
        this.dataset = dataset;
        return this;
    }

    public AnalysisJob buildNewJob() {
        AnalysisJob analysisJob = new AnalysisJob();
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

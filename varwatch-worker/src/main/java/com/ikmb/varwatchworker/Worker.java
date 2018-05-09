/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchworker;

import com.ikmb.WorkFlowManager.JobProcessStatus;
import com.ikmb.core.workflow.analysis.Analysis;
import com.ikmb.core.workflow.job.AnalysisJob;
import com.ikmb.core.workflow.worker.AnalysisWorker;

/**
 *
 * @author Broder
 */
public interface Worker {

//    public void getInput();
//
//    public void work();
//
//    public void writeOutput();
//
//    public void createFollowingJobs();
//
//    public Boolean checkPreconditions();
//
//    public void loadInputParameters();

//    public void postprocessing(Long runtime);

    public void setWorker(AnalysisWorker workerSQL);

    public void setAnalysis(Analysis analysisSQL);

    public void setAnalysisJob(AnalysisJob analysisJobSQL);

//    public void revert();
//
//    public void finishJob(Long elapsedJobTime);
//
//    public void run();

    public void runJob();

    public void undo();

    public JobProcessStatus getJobProcessStatus();
}

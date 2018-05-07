/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchworker;

import com.ikmb.WorkFlowManager.JobProcessStatus;
import com.ikmb.varwatchsql.workflow.job.AnalysisJobSQL;
import com.ikmb.varwatchsql.entities.AnalysisSQL;
import com.ikmb.varwatchsql.entities.AnalysisWorkerSQL;

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

    public void setWorkerSQL(AnalysisWorkerSQL workerSQL);

    public void setAnalysisSQL(AnalysisSQL analysisSQL);

    public void setAnalysisJobSQL(AnalysisJobSQL analysisJobSQL);

//    public void revert();
//
//    public void finishJob(Long elapsedJobTime);
//
//    public void run();

    public void runJob();

    public void undo();

    public JobProcessStatus getJobProcessStatus();
}

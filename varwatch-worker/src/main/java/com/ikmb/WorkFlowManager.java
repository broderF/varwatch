/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb;

import com.google.inject.Inject;
import com.ikmb.varwatchsql.entities.AnalysisWorkerSQL;
import com.ikmb.varwatchsql.workflow.job.AnalysisJobSQL;
import com.ikmb.varwatchsql.workflow.WorkerManager;
import com.ikmb.varwatchsql.workflow.job.JobManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;
import org.joda.time.DateTime;

/**
 *
 * @author broder
 */
public class WorkFlowManager {

    @Inject
    private WorkerManager workerManager;

    @Inject
    private JobManager jobManager;

    public AnalysisWorkerSQL createWorker() {
        return workerManager.createWorker();
    }

    public AnalysisJobSQL getJobForWorker(AnalysisWorkerSQL worker) {
        AnalysisJobSQL job = null;
        try {
            job = jobManager.getJobForWorker(worker.getId());
        } catch (RollbackException rbe) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, rbe.getMessage(), rbe);
        } catch (EntityNotFoundException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return job;
    }

    void setWorkerState(WorkerState workerState) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void finishWorker(AnalysisWorkerSQL workerSQL) {
        workerSQL.setStatus("DONE");
        workerSQL.setDied(new DateTime());
        workerSQL.setCauseOfDeath("Timeout");
        workerManager.update(workerSQL);
    }

    public void setJobReleased(AnalysisJobSQL jobSQL) {
        jobSQL.setRetryCount(0);
        jobSQL.setStatus("READY");
        jobSQL.setWorker(null);
        jobSQL.setRuntime(null);
        DateTime submittedDate = jobSQL.getSubmitted();
        submittedDate.plusMinutes(3);
        jobSQL.setSubmitted(submittedDate);
        jobManager.updateJob(jobSQL);
    }

    public void setWorkerReleased(AnalysisWorkerSQL workerSQL) {
//        workerManager.release();
        workerManager.setWorkerReleased(workerSQL);
    }

    public void setJobFailed(AnalysisJobSQL jobSQL) {
        if (jobSQL == null) {
            return;
        }
        jobSQL.setRetryCount(jobSQL.getRetryCount() + 1);
        jobSQL.setWorker(null);
        jobSQL.setRuntime(null);
        DateTime submittedDate = jobSQL.getSubmitted();
        submittedDate.plusMinutes(3);
        jobSQL.setSubmitted(submittedDate);
        jobSQL.setStatus("FAILED");
        try {
            jobManager.updateJob(jobSQL);
        } catch (PersistenceException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void finishJob(JobProcessStatus status, AnalysisWorkerSQL workerSQL, AnalysisJobSQL jobSQL, Long runtime) {
        jobSQL.setStatus(status.toString());
        jobSQL.setRuntime(runtime);
        jobManager.updateJob(jobSQL);

        workerSQL.setStatus("READY");
        workerSQL.setWorkDone(workerSQL.getWorkDone() + 1);
        workerManager.update(workerSQL);
    }

    public enum WorkerState {

        PROCESS;
    }

    public enum JobProcessStatus {

        PRECONDITION_FAILED, SUCCESSFUL, FAILED, UNKNOWN, NO_EXECUTION_NECESSARY;
    }
}

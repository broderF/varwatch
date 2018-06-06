/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb;

import com.google.inject.Inject;
import com.ikmb.core.data.workflow.analysis.Analysis;
import com.ikmb.core.data.workflow.job.AnalysisJob;
import com.ikmb.core.data.workflow.worker.AnalysisWorker;
import com.ikmb.utils.WorkerTimer;
import com.ikmb.core.data.workflow.worker.WorkerManager;
import com.ikmb.varwatchworker.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author broder
 */
public class WorkFlowHandler {

    @Inject
    private WorkerTimer workerTimer;

    @Inject
    private WorkFlowManager workFlowManager;

    @Inject
    private WorkerManager workerManager;

    private final Logger logger = LoggerFactory.getLogger(WorkFlowHandler.class);

    public void run(Long workerID) {

        AnalysisWorker workerSQL = workerManager.getWorker(workerID);
//        Long workerID = workerSQL.getId();
//        long startWorkerTime = System.currentTimeMillis();
        workerTimer.setRunningTime(300000);
//        workerTimer.setRunningTime(30000);
        workerTimer.start();
//        String deadReason = null;
        AnalysisJob jobSQL = null;
        Worker worker = null;

        while (!workerTimer.isFinish()) {

//            long currentWorkerTime = System.currentTimeMillis();
//            long dif = currentWorkerTime - startWorkerTime;
//            if (dif > 300000) { //5 min erstmal
//                deadReason = "timeout";
//                break;
//            }
            //check if there is a job for this worker
//            jobSQL = WorkflowDatabaseHelper.startWorkerJob(workerID);
            try {
                jobSQL = workFlowManager.getJobForWorker(workerSQL);

                if (jobSQL == null) {
                    logger.info("No available job found");
                    try {
                        Thread.sleep(20000);                //1000 milliseconds is one second.
                        continue;
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }

                logger.info("Start new job with id {}", jobSQL.getId());
                Analysis analysis = jobSQL.getAnalysis();
                worker = WorkerFactory.getWorker(workerSQL, analysis, jobSQL);
                workerTimer.startJob();

                worker.runJob();

                switch (worker.getJobProcessStatus()) {
                    case PRECONDITION_FAILED:
                        logger.info("precondition for job failed, release job with id {}", jobSQL.getId());
                        workFlowManager.setJobReleased(jobSQL);
                        workFlowManager.setWorkerReleased(workerSQL);
                        break;
                    case FAILED:
                        logger.info("job failed with id {}", jobSQL.getId());
                        worker.undo();
                        workFlowManager.setJobFailed(jobSQL);
                        workFlowManager.setWorkerReleased(workerSQL);
                        break;
                    default:
                        logger.info("job execution successfull with id {}", jobSQL.getId());
                        workFlowManager.finishJob(worker.getJobProcessStatus(),workerSQL, jobSQL, workerTimer.stopJob());
                }

            } catch (Exception ex) {
                logger.error(null, ex);

                workFlowManager.setJobFailed(jobSQL);
                workFlowManager.setWorkerReleased(workerSQL);

                if (worker != null) {
                    worker.undo();
                }
            }

        }
        logger.info("------ Finish worker -----");
        workFlowManager.finishWorker(workerSQL);
    }
}

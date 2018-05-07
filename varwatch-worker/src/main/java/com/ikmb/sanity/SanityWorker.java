/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.sanity;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.ikmb.WorkFlowManager;
import com.ikmb.WorkFlowManager.JobProcessStatus;
import com.ikmb.utils.WorkerInputHandler;
import com.ikmb.varwatchcommons.notification.NotificationSubmitter;
import com.ikmb.varwatchsql.guice.VarWatchInjector;
import com.ikmb.varwatchsql.guice.VarWatchPersist;
import com.ikmb.varwatchsql.entities.AnalysisSQL;
import com.ikmb.varwatchsql.entities.AnalysisWorkerSQL;
import com.ikmb.varwatchsql.workflow.WorkerManager;
import com.ikmb.varwatchsql.workflow.job.AnalysisJobSQL;
import com.ikmb.varwatchsql.workflow.job.JobManager;
import com.ikmb.varwatchworker.Worker;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author bfredrich
 */
public class SanityWorker implements Worker {

    private final Logger logger = LoggerFactory.getLogger(SanityWorker.class);
    protected AnalysisWorkerSQL _workerSQL;
    protected AnalysisSQL _analysisSQL;
    protected AnalysisJobSQL _analysisJobSQL;

    private WorkFlowManager.JobProcessStatus jobProcessStatus = WorkFlowManager.JobProcessStatus.FAILED;
    @Inject
    private Provider<EntityManager> emProvider;
    @Inject
    private WorkerInputHandler workerInputHandler;
    @Inject
    private WorkerManager workerManager;
    @Inject
    private JobManager jobManager;

    public static void main(String[] args) {
        Injector inj = Guice.createInjector(new VarWatchInjector(), new JpaPersistModule("varwatch_dev"));
        VarWatchPersist init = inj.getInstance(VarWatchPersist.class);
        SanityWorker vdm = inj.getInstance(SanityWorker.class);
        vdm.runJob();
        System.out.println("finish");
    }

    @Override
    public void setWorkerSQL(AnalysisWorkerSQL workerSQL) {
        _workerSQL = workerSQL;
    }

    @Override
    public void setAnalysisSQL(AnalysisSQL analysisSQL) {
        _analysisSQL = analysisSQL;
    }

    @Override
    public void setAnalysisJobSQL(AnalysisJobSQL analysisJobSQL) {
        _analysisJobSQL = analysisJobSQL;
    }

    @Override
    public void runJob() {

        workerInputHandler.setJob(_analysisJobSQL);
        workerInputHandler.setAnalysis(_analysisSQL);

        List<SanityResponse> sanityCheckResponses = new ArrayList<>();

        sanityCheckResponses.add(checkForFailedWorkers());
        sanityCheckResponses.add(checkForClaimedWorkers());
        sanityCheckResponses.add(checkForFailedJobs());
        sanityCheckResponses.add(checkForLongGoingJobs());

        if (checkForFailedChecks(sanityCheckResponses)) {
            String collectResponses = StringUtils.join(sanityCheckResponses.toArray(), "\n");
            sendMail(collectResponses);
        }

        jobProcessStatus = JobProcessStatus.SUCCESSFUL;
    }

    @Override
    public void undo() {
        sendMail("Undefiend error for sanity check");
    }

    @Override
    public WorkFlowManager.JobProcessStatus getJobProcessStatus() {
        return jobProcessStatus;
    }

    private SanityResponse checkForFailedWorkers() {
        List<AnalysisWorkerSQL> failedWorkers = workerManager.getWorkerByStatus("FAILED");

        SanityResponse sanityResponse = new SanityResponse("Check for failed workers in the system");
        if (failedWorkers.isEmpty()) {
            sanityResponse.setResponseType("OK");
        } else {
            sanityResponse.setResponseType("ERROR");
        }
        return sanityResponse;
    }

    private SanityResponse checkForClaimedWorkers() {
        List<AnalysisWorkerSQL> claimedWorkers = workerManager.getWorkerByStatus("CLAIMED");
        List<AnalysisWorkerSQL> claimedLongGoingWorkers = new ArrayList<>();
        for (AnalysisWorkerSQL currentWorker : claimedWorkers) {
            DateTime bornTime = currentWorker.getBorn();
            DateTime currentTime = new DateTime();
            Duration duration = new Duration(bornTime, currentTime);
            if (duration.toPeriod().getMinutes() > 10) {
                claimedLongGoingWorkers.add(currentWorker);
            }
        }

        SanityResponse sanityResponse = new SanityResponse("Check for long going claimed workers in the system");
        if (claimedLongGoingWorkers.isEmpty()) {
            sanityResponse.setResponseType("OK");
        } else {
            sanityResponse.setResponseType("ERROR");
        }
        return sanityResponse;
    }

    private void sendMail(String content) {
        String puName = emProvider.get().getEntityManagerFactory().getProperties().get("hibernate.ejb.persistenceUnitName").toString();
        NotificationSubmitter.sendMail("broderfredrich@gmail.com", content, "varwatch error: " + puName.toString());
    }

    private boolean checkForFailedChecks(List<SanityResponse> sanityCheckResponses) {
        for (SanityResponse currentResponse : sanityCheckResponses) {
            if (currentResponse.getResponseType().equals("ERROR")) {
                return true;
            }
        }
        return false;
    }

    private SanityResponse checkForFailedJobs() {
        List<AnalysisJobSQL> failedJobs = jobManager.getJobByStatus("FAILED");

        SanityResponse sanityResponse = new SanityResponse("Check for failed jobs in the system");
        if (failedJobs.isEmpty()) {
            sanityResponse.setResponseType("OK");
        } else {
            sanityResponse.setResponseType("ERROR");
        }
        return sanityResponse;
    }

    private SanityResponse checkForLongGoingJobs() {
        List<AnalysisJobSQL> longGoingJobs = new ArrayList<>();

        List<AnalysisJobSQL> claimedJobs = jobManager.getJobByStatus("CLAIMED");
        for (AnalysisJobSQL currentJob : claimedJobs) {
            DateTime bornTime = currentJob.getLastCheckIn();
            DateTime currentTime = new DateTime();
            Duration duration = new Duration(bornTime, currentTime);
            if (duration.toPeriod().getMinutes() > 10) {
                longGoingJobs.add(currentJob);
            }
        }

        List<AnalysisJobSQL> readyJobs = jobManager.getJobByStatus("READY");
        for (AnalysisJobSQL currentJob : readyJobs) {
            DateTime bornTime = currentJob.getLastCheckIn();
            DateTime currentTime = new DateTime();
            Duration duration = new Duration(bornTime, currentTime);
            if (duration.toPeriod().getMinutes() > 10) {
                longGoingJobs.add(currentJob);
            }
        }

        SanityResponse sanityResponse = new SanityResponse("Check for long going ready/claimed jobs in the system");
        if (longGoingJobs.isEmpty()) {
            sanityResponse.setResponseType("OK");
        } else {
            sanityResponse.setResponseType("ERROR");
        }
        return sanityResponse;
    }

}

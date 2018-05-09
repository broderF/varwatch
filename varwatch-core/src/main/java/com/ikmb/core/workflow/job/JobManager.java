/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.workflow.job;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.ikmb.core.data.dataset.DatasetDao;
import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.workflow.analysis.Analysis;
import com.ikmb.core.workflow.analysis.AnalysisDao;
import com.ikmb.core.workflow.worker.AnalysisWorker;
import com.ikmb.varwatchsql.workflow.analysis.AnalysisBuilder.ModuleName;
import com.ikmb.core.workflow.worker.WorkerDao;
import java.util.List;
import java.util.Random;
import org.joda.time.DateTime;

/**
 *
 * @author broder
 */
public class JobManager {

    @Inject
    private DatasetDao datasetDao;

    @Inject
    private AnalysisDao analysisDao;

    @Inject
    private JobDao jobDao;

    @Inject
    private WorkerDao workerDao;

    @Inject
    private JobBuilder jobBuilder;

    @Transactional
    public void createJob(Long datasetID, ModuleName analysisName, String additionalParameters, String action) {
        Analysis analysisSQL = analysisDao.getAnalysisByModuleName(analysisName);
        DatasetVW dataset = datasetDao.getDataset(datasetID);
        AnalysisJob job = jobBuilder.withAnalysis(analysisSQL).withDataset(dataset).withAdditionalParam(additionalParameters).withActionProp(action).buildNewJob();
        jobDao.save(job);
    }

    @Transactional
    public void createJob(ModuleName analysisName, String additionalParameters, String action) {
        Analysis analysisSQL = analysisDao.getAnalysisByModuleName(analysisName);
        AnalysisJob job = jobBuilder.withAnalysis(analysisSQL).withAdditionalParam(additionalParameters).withActionProp(action).buildNewJob();
        jobDao.save(job);
    }

    @Transactional
    public AnalysisJob getJobForWorker(Long workerID) {
        List<Long> availableJobs = jobDao.getIDsOfAvailableJobs();
        if (availableJobs.isEmpty()) {
            return null;
        }

        AnalysisWorker worker = workerDao.getWorker(workerID);
        worker.setStatus("CLAIMED");
        worker.setLastCheckIn(new DateTime());
        workerDao.update(worker);

        //pick random job
        Random random = new Random();
        int number = random.nextInt(availableJobs.size());
        Long id = availableJobs.get(number);
        AnalysisJob job = jobDao.getJobByID(id);
        job.setWorker(worker);
        Integer retryCount = job.getRetryCount() + 1;
        job.setRetryCount(retryCount);
        job.setLastCheckIn(new DateTime());
        job.setStatus("CLAIMED");
        jobDao.update(job);
        return job;
    }

    @Transactional
    public void updateJob(AnalysisJob jobSQL) {
        AnalysisJob dbJob = jobDao.getJobByID(jobSQL.getId());
        if (dbJob != null) {
            dbJob.setRuntime(jobSQL.getRuntime());
            dbJob.setStatus(jobSQL.getStatus());
            jobDao.update(dbJob);
        }
    }

    @Transactional
    public List<AnalysisJob> getScreeningJobs(Long id) {
        DatasetVW dataset = datasetDao.getDataset(id);
        Analysis analysisSQL = analysisDao.getAnalysisByModuleName(ModuleName.SCREENING);
        return jobDao.getJobByModuleAndDataset(dataset, analysisSQL);
    }

    @Transactional
    public int getNofAvailableJobs() {
        return jobDao.getNofAvailableJobs();
    }

    @Transactional
    public List<AnalysisJob> getBeaconScreeningJobs(DatasetVW dataset) {
        Analysis analysisSQL = analysisDao.getAnalysisByModuleName(ModuleName.SCREENING_BEACON);
        return jobDao.getJobByModuleAndDataset(dataset, analysisSQL);
    }

    public boolean isVarWatchJobProcessed(DatasetVW dataset) {
        Analysis analysisSQL = analysisDao.getAnalysisByModuleName(ModuleName.SCREENING_VARWATCH);
        List<AnalysisJob> curJob = jobDao.getJobByModuleAndStatus(dataset, "CLAIMED", analysisSQL);
        if (curJob.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    @Transactional
    public List<AnalysisJob> getJobByStatus(String status) {
        return jobDao.getJobByStatus(status);
    }
}

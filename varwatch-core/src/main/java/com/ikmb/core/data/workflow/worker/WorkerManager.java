/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.workflow.worker;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.ikmb.core.data.workflow.job.AnalysisJob;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author broder
 */
public class WorkerManager {

    @Inject
    private WorkerDao workerDao;

    @Inject
    private WorkerBuilder workerBuilder;

    @Transactional
    public List<AnalysisWorker> getWorkerByStatus(String status) {
        return workerDao.getWorkerByStatus(status);
    }

    @Transactional
    public AnalysisWorker createWorker() {
        AnalysisWorker worker = workerBuilder.buildNewWorker();
        workerDao.save(worker);
        return worker;
    }

    @Transactional
    public void update(AnalysisWorker workerSQL) {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "id:" + workerSQL.getId());
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, workerSQL.getStatus());
        AnalysisWorker dbWorker = workerDao.getWorker(workerSQL.getId());
        dbWorker.setStatus(workerSQL.getStatus());
        dbWorker.setWorkDone(workerSQL.getWorkDone() + 1);
        dbWorker.setCauseOfDeath(workerSQL.getCauseOfDeath());
        dbWorker.setDied(workerSQL.getDied());
        dbWorker.setLastCheckIn(workerSQL.getLastCheckIn());
//        dbWorker.setWorkerJobs(new HashSet<AnalysisJobSQL>());
        workerDao.update(dbWorker);
    }

    @Transactional
    public List<AnalysisWorker> getAvailableWorker() {
        return workerDao.getAvailableWorker();
    }

    @Transactional
    public AnalysisWorker getWorker(Long id) {
        return workerDao.getWorker(id);
    }

//    public void release() {
//        workerDao.clearEM();
//    }
    @Transactional
    public void setWorkerReleased(AnalysisWorker workerSQL) {
        AnalysisWorker dbWorker = workerDao.getWorker(workerSQL.getId());
        dbWorker.setStatus("READY");
        dbWorker.setWorkerJobs(new HashSet<>());
        workerDao.update(dbWorker);
    }

}

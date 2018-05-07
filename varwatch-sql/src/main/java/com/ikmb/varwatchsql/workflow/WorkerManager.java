/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.workflow;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.ikmb.varwatchsql.entities.AnalysisWorkerSQL;
import com.ikmb.varwatchsql.workflow.job.AnalysisJobSQL;
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
    public List<AnalysisWorkerSQL> getWorkerByStatus(String status) {
        return workerDao.getWorkerByStatus(status);
    }

    @Transactional
    public AnalysisWorkerSQL createWorker() {
        AnalysisWorkerSQL worker = workerBuilder.buildNewWorker();
        workerDao.save(worker);
        return worker;
    }

    @Transactional
    public void update(AnalysisWorkerSQL workerSQL) {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "id:" + workerSQL.getId());
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, workerSQL.getStatus());
        AnalysisWorkerSQL dbWorker = workerDao.getWorker(workerSQL.getId());
        dbWorker.setStatus(workerSQL.getStatus());
        dbWorker.setWorkDone(workerSQL.getWorkDone() + 1);
        dbWorker.setCauseOfDeath(workerSQL.getCauseOfDeath());
        dbWorker.setDied(workerSQL.getDied());
        dbWorker.setLastCheckIn(workerSQL.getLastCheckIn());
//        dbWorker.setWorkerJobs(new HashSet<AnalysisJobSQL>());
        workerDao.update(dbWorker);
    }

    @Transactional
    public List<AnalysisWorkerSQL> getAvailableWorker() {
        return workerDao.getAvailableWorker();
    }

    @Transactional
    public AnalysisWorkerSQL getWorker(Long id) {
        return workerDao.getWorker(id);
    }

//    public void release() {
//        workerDao.clearEM();
//    }
    @Transactional
    public void setWorkerReleased(AnalysisWorkerSQL workerSQL) {
        AnalysisWorkerSQL dbWorker = workerDao.getWorker(workerSQL.getId());
        dbWorker.setStatus("READY");
        dbWorker.setWorkerJobs(new HashSet<AnalysisJobSQL>());
        workerDao.update(dbWorker);
    }

}

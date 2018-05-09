/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.workflow;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.ikmb.core.workflow.worker.AnalysisWorker;
import com.ikmb.core.workflow.worker.WorkerDao;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author broder
 */
public class WorkerDaoSQL implements WorkerDao {

    @Inject
    private Provider<EntityManager> emProvider;

    public void refresh(AnalysisWorker worker) {
        emProvider.get().merge(worker);
        emProvider.get().refresh(worker);
    }

    public void save(AnalysisWorker worker) {
        emProvider.get().persist(worker);
    }

    public void update(AnalysisWorker worker) {
        emProvider.get().merge(worker);
    }

    public List<AnalysisWorker> getAvailableWorker() {
        TypedQuery<AnalysisWorker> query = emProvider.get().createQuery("SELECT s FROM AnalysisWorker s WHERE s.status = :statusReady or s.status = :statusClaimed", AnalysisWorker.class);
        query.setParameter("statusReady", "READY");
        query.setParameter("statusClaimed", "CLAIMED");
        List<AnalysisWorker> worker = query.getResultList();
        return worker;
    }

    public AnalysisWorker getWorker(Long id) {
        return emProvider.get().find(AnalysisWorker.class, id);
    }

    public List<AnalysisWorker> getWorkerByStatus(String status) {
        TypedQuery<AnalysisWorker> query = emProvider.get().createQuery("SELECT s FROM AnalysisWorker s WHERE s.status = :status", AnalysisWorker.class);
        query.setParameter("status", status);
        List<AnalysisWorker> worker = query.getResultList();
        return worker;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.workflow;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.ikmb.varwatchsql.entities.AnalysisWorkerSQL;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author broder
 */
public class WorkerDao {

    @Inject
    private Provider<EntityManager> emProvider;

    public void refresh(AnalysisWorkerSQL worker) {
        emProvider.get().merge(worker);
        emProvider.get().refresh(worker);
    }

    public void save(AnalysisWorkerSQL worker) {
        emProvider.get().persist(worker);
    }

    public void update(AnalysisWorkerSQL worker) {
        emProvider.get().merge(worker);
    }

    public List<AnalysisWorkerSQL> getAvailableWorker() {
        TypedQuery<AnalysisWorkerSQL> query = emProvider.get().createQuery("SELECT s FROM AnalysisWorkerSQL s WHERE s.status = :statusReady or s.status = :statusClaimed", AnalysisWorkerSQL.class);
        query.setParameter("statusReady", "READY");
        query.setParameter("statusClaimed", "CLAIMED");
        List<AnalysisWorkerSQL> worker = query.getResultList();
        return worker;
    }

    public AnalysisWorkerSQL getWorker(Long id) {
        return emProvider.get().find(AnalysisWorkerSQL.class, id);
    }

    public List<AnalysisWorkerSQL> getWorkerByStatus(String status) {
        TypedQuery<AnalysisWorkerSQL> query = emProvider.get().createQuery("SELECT s FROM AnalysisWorkerSQL s WHERE s.status = :status", AnalysisWorkerSQL.class);
        query.setParameter("status", status);
        List<AnalysisWorkerSQL> worker = query.getResultList();
        return worker;
    }

}

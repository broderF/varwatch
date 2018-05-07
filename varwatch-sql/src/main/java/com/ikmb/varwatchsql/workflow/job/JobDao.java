/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.workflow.job;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.ikmb.varwatchsql.entities.AnalysisSQL;
import com.ikmb.varwatchsql.entities.DatasetVWSQL;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author broder
 */
public class JobDao {

    @Inject
//    private EntityManager em;
    private Provider<EntityManager> emProvider;

    public void save(AnalysisJobSQL job) {
        emProvider.get().persist(job);
    }

    public List<Long> getIDsOfAvailableJobs() {
        TypedQuery<Long> query = emProvider.get().createQuery("SELECT s.id FROM AnalysisJobSQL s join s.analysis a WHERE (s.status = :status  or s.status = :statusFailed) AND s.retryCount < 5 AND s.submitted < CURRENT_TIMESTAMP", Long.class);
        query.setParameter("status", "READY");
        query.setParameter("statusFailed", "FAILED");
        List<Long> jobIDs = query.getResultList();
        return jobIDs;
    }

    public AnalysisJobSQL getJobByID(Long id) {
        return emProvider.get().find(AnalysisJobSQL.class, id);
    }

    public void update(AnalysisJobSQL job) {
        emProvider.get().merge(job);
    }

    public List<AnalysisJobSQL> getJobByModuleAndDataset(DatasetVWSQL dataset, AnalysisSQL analysisSQL) {
        TypedQuery<AnalysisJobSQL> jobQuery = emProvider.get().createQuery("SELECT s FROM AnalysisJobSQL s WHERE s.dataset = :dataset AND s.analysis = :analysis", AnalysisJobSQL.class);
        jobQuery.setParameter("dataset", dataset).setParameter("analysis", analysisSQL);
        return jobQuery.getResultList();
    }

    public int getNofAvailableJobs() {
        TypedQuery<Long> query = emProvider.get().createQuery("SELECT s.id FROM AnalysisJobSQL s WHERE s.status = :statusReady or (s.status = :statusFailed and s.retryCount < 5)", Long.class);
        query.setParameter("statusReady", "READY");
        query.setParameter("statusFailed", "FAILED");
        List<Long> worker = query.getResultList();
        return worker.size();
    }

    List<AnalysisJobSQL> getJobByModuleAndStatus(DatasetVWSQL dataset, String claimed, AnalysisSQL analysisSQL) {
        TypedQuery<AnalysisJobSQL> jobQuery = emProvider.get().createQuery("SELECT s FROM AnalysisJobSQL s WHERE s.status = :status AND s.analysis = :analysis AND s.dataset != :dataset", AnalysisJobSQL.class);
        jobQuery.setParameter("status", claimed).setParameter("dataset", dataset).setParameter("analysis", analysisSQL);
        return jobQuery.getResultList();
    }

    List<AnalysisJobSQL> getJobByStatus(String status) {
        TypedQuery<AnalysisJobSQL> jobQuery = emProvider.get().createQuery("SELECT s FROM AnalysisJobSQL s WHERE s.status = :status", AnalysisJobSQL.class);
        jobQuery.setParameter("status", status);
        return jobQuery.getResultList();
    }
}

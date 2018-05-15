/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.sql.workflow;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.data.workflow.analysis.Analysis;
import com.ikmb.core.data.workflow.job.AnalysisJob;
import com.ikmb.core.data.workflow.job.JobDao;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author broder
 */
public class JobDaoSQL implements JobDao {

    @Inject
//    private EntityManager em;
    private Provider<EntityManager> emProvider;

    public void save(AnalysisJob job) {
        emProvider.get().persist(job);
    }

    public List<Long> getIDsOfAvailableJobs() {
        TypedQuery<Long> query = emProvider.get().createQuery("SELECT s.id FROM AnalysisJob s join s.analysis a WHERE (s.status = :status  or s.status = :statusFailed) AND s.retryCount < 5 AND s.submitted < CURRENT_TIMESTAMP", Long.class);
        query.setParameter("status", "READY");
        query.setParameter("statusFailed", "FAILED");
        List<Long> jobIDs = query.getResultList();
        return jobIDs;
    }

    public AnalysisJob getJobByID(Long id) {
        return emProvider.get().find(AnalysisJob.class, id);
    }

    public void update(AnalysisJob job) {
        emProvider.get().merge(job);
    }

    public List<AnalysisJob> getJobByModuleAndDataset(DatasetVW dataset, Analysis analysisSQL) {
        TypedQuery<AnalysisJob> jobQuery = emProvider.get().createQuery("SELECT s FROM AnalysisJob s WHERE s.dataset = :dataset AND s.analysis = :analysis", AnalysisJob.class);
        jobQuery.setParameter("dataset", dataset).setParameter("analysis", analysisSQL);
        return jobQuery.getResultList();
    }

    public int getNofAvailableJobs() {
        TypedQuery<Long> query = emProvider.get().createQuery("SELECT s.id FROM AnalysisJob s WHERE s.status = :statusReady or (s.status = :statusFailed and s.retryCount < 5)", Long.class);
        query.setParameter("statusReady", "READY");
        query.setParameter("statusFailed", "FAILED");
        List<Long> worker = query.getResultList();
        return worker.size();
    }

   public  List<AnalysisJob> getJobByModuleAndStatus(DatasetVW dataset, String claimed, Analysis analysisSQL) {
        TypedQuery<AnalysisJob> jobQuery = emProvider.get().createQuery("SELECT s FROM AnalysisJob s WHERE s.status = :status AND s.analysis = :analysis AND s.dataset != :dataset", AnalysisJob.class);
        jobQuery.setParameter("status", claimed).setParameter("dataset", dataset).setParameter("analysis", analysisSQL);
        return jobQuery.getResultList();
    }

   public List<AnalysisJob> getJobByStatus(String status) {
        TypedQuery<AnalysisJob> jobQuery = emProvider.get().createQuery("SELECT s FROM AnalysisJob s WHERE s.status = :status", AnalysisJob.class);
        jobQuery.setParameter("status", status);
        return jobQuery.getResultList();
    }
}

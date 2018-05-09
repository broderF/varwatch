/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.data.dataset;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.ikmb.core.auth.user.User;
import com.ikmb.core.data.dataset.DatasetDao;
import com.ikmb.core.data.dataset.DatasetHGMD;
import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.data.hpo.Phenotype;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author broder
 */
public class DatasetDaoSQL implements DatasetDao {

    @Inject
    private Provider<EntityManager> emProvider;

    public void save(DatasetVW datasetSQL) {
        emProvider.get().persist(datasetSQL);
        for (Phenotype phenotype : datasetSQL.getPhenotypes()) {
            phenotype.setDataset(datasetSQL);
            emProvider.get().persist(phenotype);
        }
    }

    public DatasetVW getDataset(Long datasetID) {
        DatasetVW ds = emProvider.get().find(DatasetVW.class, datasetID);
        if (ds == null) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "ds is null with id: " + datasetID);
        }
        emProvider.get().refresh(ds);
        return ds;
    }

    public void update(DatasetVW dataset) {
        emProvider.get().merge(dataset);
    }

    public void refresh(DatasetVW dataset) {
        emProvider.get().refresh(dataset);
    }

    public List<DatasetVW> getDatasetsByUser(User user) {
        TypedQuery<DatasetVW> query = emProvider.get().createQuery("SELECT s FROM DatasetVW s WHERE s.user = :user", DatasetVW.class
        );
        List<DatasetVW> datasets = query.setParameter("user", user).getResultList();
        return new ArrayList<DatasetVW>(datasets);
    }

    public List<Long> getDatasetIDsByUser(User user) {
        TypedQuery<Long> query = emProvider.get().createQuery("SELECT s.id FROM DatasetVW s WHERE s.user = :user", Long.class
        );
        List<Long> datasetIDs = query.setParameter("user", user).getResultList();
        return datasetIDs;
    }

    public DatasetHGMD getHGMDDataset(long datasetID) {
        DatasetHGMD ds = emProvider.get().find(DatasetHGMD.class, datasetID);
        if (ds == null) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "ds is null with id: " + datasetID);
        }
        emProvider.get().refresh(ds);
        return ds;
    }

    public List<Long> getAllDatasetIds() {
        TypedQuery<Long> query = emProvider.get().createQuery("SELECT s.id FROM DatasetVW s ", Long.class
        );
        List<Long> datasetIDs = query.getResultList();
        return datasetIDs;
    }

}

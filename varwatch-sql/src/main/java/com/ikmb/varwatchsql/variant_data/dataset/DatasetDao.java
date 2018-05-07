/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.variant_data.dataset;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.ikmb.varwatchsql.auth.user.UserSQL;
import com.ikmb.varwatchsql.entities.DatasetHGMDSQL;
import com.ikmb.varwatchsql.entities.DatasetVWSQL;
import com.ikmb.varwatchsql.data.hpo.PhenotypeSQL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author broder
 */
public class DatasetDao {

    @Inject
//    private EntityManager em;
    private Provider<EntityManager> emProvider;

    public void save(DatasetVWSQL datasetSQL) {
        emProvider.get().persist(datasetSQL);
        for (PhenotypeSQL phenotype : datasetSQL.getPhenotypes()) {
            phenotype.setDataset(datasetSQL);
            emProvider.get().persist(phenotype);
        }
    }

    public DatasetVWSQL getDataset(Long datasetID) {
        DatasetVWSQL ds = emProvider.get().find(DatasetVWSQL.class, datasetID);
        if (ds == null) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "ds is null with id: " + datasetID);
        }
        emProvider.get().refresh(ds);
        return ds;
    }

    void update(DatasetVWSQL dataset) {
        emProvider.get().merge(dataset);
    }

    public void refresh(DatasetVWSQL dataset) {
        emProvider.get().refresh(dataset);
    }

    public List<DatasetVWSQL> getDatasetsByUser(UserSQL user) {
        TypedQuery<DatasetVWSQL> query = emProvider.get().createQuery("SELECT s FROM DatasetVWSQL s WHERE s.user = :user", DatasetVWSQL.class
        );
        List<DatasetVWSQL> datasets = query.setParameter("user", user).getResultList();
        return datasets;
    }

    public List<Long> getDatasetIDsByUser(UserSQL user) {
        TypedQuery<Long> query = emProvider.get().createQuery("SELECT s.id FROM DatasetVWSQL s WHERE s.user = :user", Long.class
        );
        List<Long> datasetIDs = query.setParameter("user", user).getResultList();
        return datasetIDs;
    }

    public DatasetHGMDSQL getHGMDDataset(long datasetID) {
        DatasetHGMDSQL ds = emProvider.get().find(DatasetHGMDSQL.class, datasetID);
        if (ds == null) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "ds is null with id: " + datasetID);
        }
        emProvider.get().refresh(ds);
        return ds;
    }

    public List<Long> getAllDatasetIds() {
        TypedQuery<Long> query = emProvider.get().createQuery("SELECT s.id FROM DatasetVWSQL s ", Long.class
        );
        List<Long> datasetIDs = query.getResultList();
        return datasetIDs;
    }

}

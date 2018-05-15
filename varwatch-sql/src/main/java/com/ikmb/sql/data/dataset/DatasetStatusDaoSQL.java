/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.sql.data.dataset;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.ikmb.core.data.dataset.Dataset;
import com.ikmb.core.data.dataset.DatasetManager;
import com.ikmb.core.data.dataset.DatasetStatus;
import com.ikmb.core.data.dataset.DatasetStatusDao;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author broder
 */
public class DatasetStatusDaoSQL implements DatasetStatusDao {

    @Inject
//    private EntityManager em;
    private Provider<EntityManager> emProvider;

    @Inject
    private DatasetManager dsManager;

    public void save(DatasetStatus dsStatussql) {
        emProvider.get().persist(dsStatussql);
    }

    public List<DatasetStatus> getStatus(Long id) {
        Dataset ds = dsManager.getDatasetByID(id);

        TypedQuery<DatasetStatus> query = emProvider.get().createQuery("SELECT s FROM DatasetStatus s WHERE s.dataset= :ds", DatasetStatus.class);
        List<DatasetStatus> status = query.setParameter("ds", ds).getResultList();
        return status;
    }

}

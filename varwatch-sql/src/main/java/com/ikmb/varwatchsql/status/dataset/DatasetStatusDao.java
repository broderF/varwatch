/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.status.dataset;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.ikmb.varwatchsql.entities.DatasetSQL;
import com.ikmb.varwatchsql.variant_data.dataset.DatasetManager;
import com.ikmb.varwatchsql.variant_data.dataset.DatasetStatusSQL;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author broder
 */
public class DatasetStatusDao {

    @Inject
//    private EntityManager em;
    private Provider<EntityManager> emProvider;

    @Inject
    private DatasetManager dsManager;

    public void save(DatasetStatusSQL dsStatussql) {
        emProvider.get().persist(dsStatussql);
    }

    public List<DatasetStatusSQL> getStatus(Long id) {
        DatasetSQL ds = dsManager.getDatasetByID(id);

        TypedQuery<DatasetStatusSQL> query = emProvider.get().createQuery("SELECT s FROM DatasetStatusSQL s WHERE s.dataset= :ds", DatasetStatusSQL.class);
        List<DatasetStatusSQL> status = query.setParameter("ds", ds).getResultList();
        return status;
    }

}

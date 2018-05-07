/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.status.variant;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.ikmb.varwatchsql.entities.DatasetSQL;
import com.ikmb.varwatchsql.entities.DatasetVWSQL;
import com.ikmb.varwatchsql.entities.VariantStatusSQL;
import com.ikmb.varwatchsql.variant_data.dataset.DatasetStatusSQL;
import com.ikmb.varwatchsql.variant_data.variant.VariantDao;
import com.ikmb.varwatchsql.variant_data.variant.VariantSQL;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author broder
 */
public class VariantStatusDao {

    @Inject
//    private EntityManager em;
    private Provider<EntityManager> emProvider;

    @Inject
    private VariantDao variantDao;

    public void save(VariantStatusSQL variantStatus) {
        emProvider.get().persist(variantStatus);
    }

    public List<VariantStatusSQL> get(VariantSQL variant) {
        TypedQuery<VariantStatusSQL> query = emProvider.get().createQuery("SELECT s FROM VariantStatusSQL s WHERE s.variant= :variant", VariantStatusSQL.class);
        List<VariantStatusSQL> status = query.setParameter("variant", variant).getResultList();
        return status;
    }

    List<VariantStatusSQL> getStatusFromDataset(DatasetVWSQL dataset) {
        TypedQuery<VariantStatusSQL> query = emProvider.get().createQuery("SELECT s FROM VariantStatusSQL s WHERE s.dataset_vw= :dataset", VariantStatusSQL.class);
        List<VariantStatusSQL> status = query.setParameter("dataset", dataset).getResultList();
        return status;
    }

    public List<VariantStatusSQL> getStatus(Long id) {
        VariantSQL variant = variantDao.get(id);

        TypedQuery<VariantStatusSQL> query = emProvider.get().createQuery("SELECT s FROM VariantStatusSQL s WHERE s.variant =:var", VariantStatusSQL.class);
        List<VariantStatusSQL> status = query.setParameter("var", variant).getResultList();
        return status;
    }

    VariantStatusSQL getStatusByID(Long statusID) {
        return emProvider.get().find(VariantStatusSQL.class, statusID);
    }

    List<VariantStatusSQL> getMatchedStatus(Long id) {
        VariantSQL variant = variantDao.get(id);
        TypedQuery<VariantStatusSQL> query = emProvider.get().createQuery("SELECT s FROM VariantStatusSQL s WHERE s.variant =:var and s.status = :status", VariantStatusSQL.class);
        List<VariantStatusSQL> status = query.setParameter("var", variant).setParameter("status", "MATCHED").getResultList();
        return status;
    }

    List<VariantStatusSQL> getVwMatchedStatus(Long id) {
        VariantSQL variant = variantDao.get(id);
        TypedQuery<VariantStatusSQL> query = emProvider.get().createQuery("SELECT s FROM VariantStatusSQL s WHERE s.variant =:var and s.status = :status and s.statusValue = :description", VariantStatusSQL.class);
        List<VariantStatusSQL> status = query.setParameter("var", variant).setParameter("status", "MATCHED").setParameter("description", "Variant matched to VarWatch").getResultList();
        return status;
    }

    List<VariantStatusSQL> getHgmdMatchedStatus(Long id) {
        VariantSQL variant = variantDao.get(id);
        TypedQuery<VariantStatusSQL> query = emProvider.get().createQuery("SELECT s FROM VariantStatusSQL s WHERE s.variant =:var and s.status = :status and s.statusValue = :description", VariantStatusSQL.class);
        List<VariantStatusSQL> status = query.setParameter("var", variant).setParameter("status", "MATCHED").setParameter("description", "Variant matched to HGMD").getResultList();
        return status;
    }

    List<VariantStatusSQL> getNonMatchedStatus(Long id) {
        VariantSQL variant = variantDao.get(id);
        TypedQuery<VariantStatusSQL> query = emProvider.get().createQuery("SELECT s FROM VariantStatusSQL s WHERE s.variant =:var and s.status != :status", VariantStatusSQL.class);
        List<VariantStatusSQL> status = query.setParameter("var", variant).setParameter("status", "MATCHED").getResultList();
        return status;
    }

    List<VariantStatusSQL> getBeaconMatchedStatus(Long id) {
        VariantSQL variant = variantDao.get(id);
        TypedQuery<VariantStatusSQL> query = emProvider.get().createQuery("SELECT s FROM VariantStatusSQL s WHERE s.variant =:var and s.status = :status and s.statusValue != :matchVw and s.statusValue != :matchHgmd", VariantStatusSQL.class);
        List<VariantStatusSQL> status = query.setParameter("var", variant).setParameter("status", "MATCHED").setParameter("matchHgmd", "Variant matched to HGMD").setParameter("matchVw", "Variant matched to VarWatch").getResultList();
        return status;
    }
}

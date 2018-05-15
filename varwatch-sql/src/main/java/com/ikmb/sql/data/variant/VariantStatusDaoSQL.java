/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.sql.data.variant;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.data.variant.Variant;
import com.ikmb.core.data.variant.VariantDao;
import com.ikmb.core.data.variant.VariantStatus;
import com.ikmb.core.data.variant.VariantStatusDao;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author broder
 */
public class VariantStatusDaoSQL implements VariantStatusDao {

    @Inject
    private Provider<EntityManager> emProvider;

    @Inject
    private VariantDao variantDao;

    public void save(VariantStatus variantStatus) {
        emProvider.get().persist(variantStatus);
    }

    public List<VariantStatus> get(Variant variant) {
        TypedQuery<VariantStatus> query = emProvider.get().createQuery("SELECT s FROM VariantStatus s WHERE s.variant= :variant", VariantStatus.class);
        List<VariantStatus> status = query.setParameter("variant", variant).getResultList();
        return new ArrayList<VariantStatus>(status);
    }

    public List<VariantStatus> getStatusFromDataset(DatasetVW dataset) {
        TypedQuery<VariantStatus> query = emProvider.get().createQuery("SELECT s FROM VariantStatus s WHERE s.dataset_vw= :dataset", VariantStatus.class);
        List<VariantStatus> status = query.setParameter("dataset", dataset).getResultList();
        return new ArrayList<VariantStatus>(status);
    }

    public List<VariantStatus> getStatus(Long id) {
        Variant variant = variantDao.get(id);

        TypedQuery<VariantStatus> query = emProvider.get().createQuery("SELECT s FROM VariantStatus s WHERE s.variant =:var", VariantStatus.class);
        List<VariantStatus> status = query.setParameter("var", variant).getResultList();
        return new ArrayList<VariantStatus>(status);
    }

    public VariantStatus getStatusByID(Long statusID) {
        return emProvider.get().find(VariantStatus.class, statusID);
    }

    public List<VariantStatus> getMatchedStatus(Long id) {
        Variant variant = variantDao.get(id);
        TypedQuery<VariantStatus> query = emProvider.get().createQuery("SELECT s FROM VariantStatus s WHERE s.variant =:var and s.status = :status", VariantStatus.class);
        List<VariantStatus> status = query.setParameter("var", variant).setParameter("status", "MATCHED").getResultList();
        return status;
    }

    public List<VariantStatus> getVwMatchedStatus(Long id) {
        Variant variant = variantDao.get(id);
        TypedQuery<VariantStatus> query = emProvider.get().createQuery("SELECT s FROM VariantStatus s WHERE s.variant =:var and s.status = :status and s.statusValue = :description", VariantStatus.class);
        List<VariantStatus> status = query.setParameter("var", variant).setParameter("status", "MATCHED").setParameter("description", "Variant matched to VarWatch").getResultList();
        return status;
    }

    public List<VariantStatus> getHgmdMatchedStatus(Long id) {
        Variant variant = variantDao.get(id);
        TypedQuery<VariantStatus> query = emProvider.get().createQuery("SELECT s FROM VariantStatus s WHERE s.variant =:var and s.status = :status and s.statusValue = :description", VariantStatus.class);
        List<VariantStatus> status = query.setParameter("var", variant).setParameter("status", "MATCHED").setParameter("description", "Variant matched to HGMD").getResultList();
        return status;
    }

    public List<VariantStatus> getNonMatchedStatus(Long id) {
        Variant variant = variantDao.get(id);
        TypedQuery<VariantStatus> query = emProvider.get().createQuery("SELECT s FROM VariantStatus s WHERE s.variant =:var and s.status != :status", VariantStatus.class);
        List<VariantStatus> status = query.setParameter("var", variant).setParameter("status", "MATCHED").getResultList();
        return status;
    }

    public List<VariantStatus> getBeaconMatchedStatus(Long id) {
        Variant variant = variantDao.get(id);
        TypedQuery<VariantStatus> query = emProvider.get().createQuery("SELECT s FROM VariantStatus s WHERE s.variant =:var and s.status = :status and s.statusValue != :matchVw and s.statusValue != :matchHgmd", VariantStatus.class);
        List<VariantStatus> status = query.setParameter("var", variant).setParameter("status", "MATCHED").setParameter("matchHgmd", "Variant matched to HGMD").setParameter("matchVw", "Variant matched to VarWatch").getResultList();
        return status;
    }
}

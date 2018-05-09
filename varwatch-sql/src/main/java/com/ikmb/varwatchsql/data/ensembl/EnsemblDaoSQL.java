/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.data.ensembl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.ikmb.core.data.ensembl.Ensembl;
import com.ikmb.core.data.ensembl.EnsemblDao;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author broder
 */
public class EnsemblDaoSQL implements EnsemblDao {

    @Inject
//    private EntityManager em;
    private Provider<EntityManager> emProvider;

    public Ensembl getActiveEnsembl(Boolean needFile) {
        TypedQuery<Ensembl> query = emProvider.get().createQuery("SELECT s FROM Ensembl s WHERE s.active = :active", Ensembl.class);
        Ensembl ensembl = query.setParameter("active", true).getSingleResult();
        if (needFile) {
            ensembl.getGenFile();
        }
        return ensembl;
    }
}

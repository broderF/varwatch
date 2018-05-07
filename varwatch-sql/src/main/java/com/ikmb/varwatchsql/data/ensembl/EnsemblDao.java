/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.data.ensembl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.ikmb.varwatchsql.entities.EnsemblSQL;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author broder
 */
public class EnsemblDao {

    @Inject
//    private EntityManager em;
    private Provider<EntityManager> emProvider;

    public EnsemblSQL getActiveEnsembl(Boolean needFile) {
        TypedQuery<EnsemblSQL> query = emProvider.get().createQuery("SELECT s FROM EnsemblSQL s WHERE s.active = :active", EnsemblSQL.class);
        EnsemblSQL ensembl = query.setParameter("active", true).getSingleResult();
        if (needFile) {
            ensembl.getGenFile();
        }
        return ensembl;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.data.reference_db;

import com.google.inject.Inject;
import com.google.inject.Provider;
//import com.ikmb.varwatchsql.PersistenceManager;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author broder
 */
public class ReferenceDBDao {

    @Inject
//    private EntityManager em;
    private Provider<EntityManager> emProvider;

    public RefDatabaseSQL get(Long id) {
        return emProvider.get().find(RefDatabaseSQL.class, id);
    }

    public List<RefDatabaseSQL> getActiveDatabases() {
        TypedQuery<RefDatabaseSQL> query = emProvider.get().createQuery("SELECT s FROM RefDatabaseSQL s WHERE s.isActive = :isActive", RefDatabaseSQL.class);
        List<RefDatabaseSQL> referenceDBs = query.setParameter("isActive", true).getResultList();
        return referenceDBs;
    }

    public RefDatabaseSQL getVarWatchDatabase() {
        TypedQuery<RefDatabaseSQL> query = emProvider.get().createQuery("SELECT s FROM RefDatabaseSQL s WHERE s.isActive = :isActive and s.name = :name", RefDatabaseSQL.class);
        RefDatabaseSQL varwatch = query.setParameter("isActive", true).setParameter("name", "VarWatch").getSingleResult();
        return varwatch;
    }

    public void save(RefDatabaseSQL refDbSql) {
        emProvider.get().persist(refDbSql);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.sql.data.reference_db;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.ikmb.core.data.reference_db.RefDatabase;
import com.ikmb.core.data.reference_db.ReferenceDBDao;
import java.util.ArrayList;
//import com.ikmb.varwatchsql.PersistenceManager;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author broder
 */
public class ReferenceDBDaoSQL implements ReferenceDBDao {

    @Inject
//    private EntityManager em;
    private Provider<EntityManager> emProvider;

    public RefDatabase get(Long id) {
            RefDatabase find = emProvider.get().find(RefDatabase.class, id);
        return find;
    }

    public List<RefDatabase> getActiveDatabases() {
        TypedQuery<RefDatabase> query = emProvider.get().createQuery("SELECT s FROM RefDatabase s WHERE s.isActive = :isActive", RefDatabase.class);
        List<RefDatabase> referenceDBs = query.setParameter("isActive", true).getResultList();
        return new ArrayList<RefDatabase>(referenceDBs);
    }

    public RefDatabase getVarWatchDatabase() {
        TypedQuery<RefDatabase> query = emProvider.get().createQuery("SELECT s FROM RefDatabase s WHERE s.isActive = :isActive and s.name = :name", RefDatabase.class);
        RefDatabase varwatch = query.setParameter("isActive", true).setParameter("name", "VarWatch").getSingleResult();
        return varwatch;
    }

    public void save(RefDatabase refDbSql) {
        emProvider.get().persist(refDbSql);
    }
}

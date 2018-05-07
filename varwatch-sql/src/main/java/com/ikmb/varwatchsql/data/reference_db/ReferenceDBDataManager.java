/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.data.reference_db;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import java.util.List;
import javax.persistence.PersistenceException;

/**
 *
 * @author broder
 */
public class ReferenceDBDataManager {

    @Inject
    private ReferenceDBDao refDBDao;

    @Transactional
    public RefDatabaseSQL getReferenceDBById(Long id) {
        return refDBDao.get(id);
    }

    @Transactional
    public List<RefDatabaseSQL> getActiveDatabases() {
        return refDBDao.getActiveDatabases();
    }

    @Transactional
    public RefDatabaseSQL getVarWatchDatabase() {
        return refDBDao.getVarWatchDatabase();
    }

    @Transactional
    public void saveReferenceDatabase(RefDatabaseSQL refDbSql) {
        refDBDao.save(refDbSql);
    }
}

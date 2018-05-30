/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.reference_db;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author broder
 */
public class ReferenceDBDataManager {

    @Inject
    private ReferenceDBDao refDBDao;

    @Transactional
    public RefDatabase getReferenceDBById(Long id) {
        return refDBDao.get(id);
    }

    @Transactional
    public List<RefDatabase> getActiveDatabases() {
        return refDBDao.getActiveDatabases();
    }

    @Transactional
    public RefDatabase getVarWatchDatabase() {
        return refDBDao.getVarWatchDatabase();
    }

    @Transactional
    public void saveReferenceDatabase(RefDatabase refDbSql) {
        refDBDao.save(refDbSql);
    }

    public List<RefDatabase> getActiveBeacons() {
        List<RefDatabase> activeDatabases = refDBDao.getActiveDatabases();
        List<RefDatabase> activeBeacons = new ArrayList<>();
        for (RefDatabase curDatabase : activeDatabases) {
            if (curDatabase.getImplementation().equals("global_beacon")) {
                activeBeacons.add(curDatabase);
            }
        }
        return activeBeacons;
    }
}

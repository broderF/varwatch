/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.reference_db;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;

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

    @Transactional
    public List<RefDatabase> getActiveBeacons() {
        List<RefDatabase> activeDatabases = refDBDao.getActiveDatabases();
        List<RefDatabase> activeBeacons = new ArrayList<>();
        for (RefDatabase curDatabase : activeDatabases) {
            if (curDatabase.getImplementation().equals("global_beacon")) {
                try {
                    if (curDatabase.getImagePath() != null) {
                        byte[] image = Files.readAllBytes(Paths.get(curDatabase.getImagePath()));
                        curDatabase.setImage(image);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ReferenceDBDataManager.class.getName()).log(Level.SEVERE, null, ex);
                }
                activeBeacons.add(curDatabase);
            }
        }
        return activeBeacons;
    }

    @Transactional
    public RefDatabase saveBeacon(String name, String path, String assembly, String image, Boolean enabled) {
        RefDatabase refDatabase = refDBDao.getRefDatabaseByName(name);
        if (refDatabase == null) {
            RefDatabase newRefDatabase = new RefDatabase();
            newRefDatabase.setAssembly(assembly);
            newRefDatabase.setImagePath(image);
            newRefDatabase.setImplementation("global_beacon");
            if (enabled != null) {
                newRefDatabase.setIsActive(enabled);
            }

            newRefDatabase.setName(name);
            newRefDatabase.setPath(path);
            newRefDatabase.setUpdated(Boolean.FALSE);
            newRefDatabase.setLastUpdate(new DateTime());
            refDBDao.save(newRefDatabase);
            return newRefDatabase;
        } else {
            if (assembly != null && !assembly.isEmpty()) {
                refDatabase.setAssembly(assembly);
            }
            if (image != null) {
                refDatabase.setImagePath(image);
            }
            if (enabled != null) {
                refDatabase.setIsActive(enabled);
            }
            if (name != null && !name.isEmpty()) {
                refDatabase.setName(name);
            }
            if (path != null && !path.isEmpty()) {
                refDatabase.setPath(path);
            }
            refDBDao.update(refDatabase);
            return refDatabase;
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.update.databasehelper;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author fredrich
 */
public enum PersistenceManager {

    INSTANCE;

    private EntityManagerFactory emFactory;
    private String databaseName = "varwatch_dev";

    private PersistenceManager() {
//        System.out.println("Working Directory = "
//                + System.getProperty("user.dir"));
//        DBConfig dbconfig = new DBConfig("db.config");
//        databaseName = dbconfig.getDb();
        emFactory = Persistence.createEntityManagerFactory(databaseName);
    }

    public EntityManager getEntityManager() {
        return emFactory.createEntityManager();
    }

    public void close() {
        emFactory.close();
    }
}

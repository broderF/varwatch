/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.guice;

import com.google.inject.AbstractModule;
import com.ikmb.core.workflow.worker.WorkerDao;
import com.ikmb.varwatchsql.workflow.WorkerDaoSQL;

public class SQLModule extends AbstractModule {


    public SQLModule() {
    }

    @Override
    protected void configure() {
        bind(WorkerDao.class).to(WorkerDaoSQL.class);
    }

}


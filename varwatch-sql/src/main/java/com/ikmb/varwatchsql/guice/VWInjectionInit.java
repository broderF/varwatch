/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.ikmb.core.varwatchcommons.VWConfiguration;

/**
 *
 * @author broder
 */
public class VWInjectionInit {

    private Injector vwInjector;
    private static VWInjectionInit instance;
    private final VWConfiguration configuration;

    private VWInjectionInit(String configPath) {
        configuration = new VWConfiguration(configPath);
        String persistanceUnitName = configuration.getDatabase();
        vwInjector = Guice.createInjector(new VarWatchInjector(), new JpaPersistModule(persistanceUnitName));
        VarWatchPersist init = vwInjector.getInstance(VarWatchPersist.class);
    }

    public static synchronized VWInjectionInit getInstance() {
        return instance;
    }

    public static synchronized void init(String path) {
        instance = new VWInjectionInit(path);
    }

    public Injector getVWInjector() {
        return vwInjector;
    }

    public VWConfiguration getConfiguration() {
        return configuration;
    }

    public void close() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

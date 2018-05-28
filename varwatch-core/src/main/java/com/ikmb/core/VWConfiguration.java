/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 *
 * @author bfredrich
 */
public class VWConfiguration {

    private String database;
    private String pathToJob;
    private String worker;
    private String pathToLog;
    private int nrWorker;
    private String pathToHPO;
    private String pathToTmp;
    private String hgvs2genomic;
    private String crossMapPath;
    private String pluginDir;
    private String fastaPath;

    public VWConfiguration(String path) {
        try {
            Configurations configs = new Configurations();

            Configuration config = configs.properties(path);
            database = config.getString("db");
            pathToJob = config.getString("job");
            worker = config.getString("work");
            pathToLog = config.getString("log");
            pathToHPO = config.getString("hpo");
            pathToTmp = config.getString("tmp");
            hgvs2genomic = config.getString("hgvs");
            crossMapPath = config.getString("crossmap");
            fastaPath = config.getString("fasta");
            pluginDir = config.getString("pluginDir");
            String nrWorkerString = config.getString("nrworker");
            if (nrWorkerString != null) {
                nrWorker = Integer.parseInt(nrWorkerString);
            }

        } catch (ConfigurationException ex) {
            Logger.getLogger(VWConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getDatabase() {
        return database;
    }

    public String getPathToJobscript() {
        return pathToJob;
    }

    public String getPathToWorker() {
        return worker;
    }

    public String getPathToLog() {
        return pathToLog;
    }

    public int getNrWorker() {
        return nrWorker;
    }

    public String getPathToHPO() {
        return pathToHPO;
    }

    public String getPathToTmp() {
        return pathToTmp;
    }

    public String getHgvs2GenomicPath() {
        return hgvs2genomic;
    }

    public String getCrossmapPath() {
        return crossMapPath;
    }

    public String getPathToFastaFile() {
        return fastaPath;
    }

    public String getPathToPluginDir() {
        return pluginDir;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb;

import com.google.inject.Injector;
import com.ikmb.varwatchsql.guice.VWInjectionInit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 *
 * @author Broder
 */
public class WorkerLauncher {

//    public static Injector injector;
    public static VWInjectionInit injector;

    public static void main(String[] args) {
        WorkerLauncher launcher = new WorkerLauncher();
        // create Options object
        Options options = new Options();

        options.addOption("cp", "config-path", true, "the config path");
        options.addOption("wid", "worker-id", true, "id of the created worker");
        CommandLineParser parser = new DefaultParser();
        String pathToConfig = null;
        Long workerID = null;
        try {
            CommandLine cmd = parser.parse(options, args);
            pathToConfig = cmd.getOptionValue("cp");
            workerID = Long.parseLong(cmd.getOptionValue("wid"));
        } catch (ParseException ex) {
            Logger.getLogger(WorkerLauncher.class.getName()).log(Level.SEVERE, null, ex);
        }
        launcher.run(pathToConfig, workerID);
    }

    public void run(String pathToConfig, Long workerID) {
        VWInjectionInit.init(pathToConfig);
        injector = VWInjectionInit.getInstance();
//        injector = VWInjectionInit.getInstance().getVWInjector();

        WorkFlowHandler workflowHandler = injector.getVWInjector().getInstance(WorkFlowHandler.class);
        workflowHandler.run(workerID);
    }
}

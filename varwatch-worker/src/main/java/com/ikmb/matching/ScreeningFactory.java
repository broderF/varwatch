/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.matching;

import com.google.inject.Injector;
import com.ikmb.WorkFlowHandler;
import com.ikmb.WorkerLauncher;
import static com.ikmb.WorkerLauncher.injector;
import com.ikmb.varwatchsql.data.reference_db.RefDatabaseSQL;

/**
 *
 * @author Broder
 */
public class ScreeningFactory {

    public static DatabaseScreener getScreeningDatabase(RefDatabaseSQL database) {
        DatabaseScreener matcher = null;
        String implementation = database.getImplementation();
        Injector injector = WorkerLauncher.injector.getVWInjector();

        if (implementation.equals("varwatch")) {
            matcher = injector.getInstance(VarWatchScreenerNew.class);
        } else if (implementation.equals("global_beacon")) {
            matcher = injector.getInstance(GlobalBeaconScreener.class);
        } else if (implementation.equals("hgmd_match")) {
            matcher = injector.getInstance(HGMDScreenerNew.class);
        }
        return matcher;
    }
}

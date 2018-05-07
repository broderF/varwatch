/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchbarkeeper;

import com.google.inject.Injector;
import com.ikmb.varwatchsql.variant_data.dataset.DatasetManager;
import com.ikmb.varwatchsql.data.reference_db.RefDatabaseSQL;
import com.ikmb.varwatchsql.data.reference_db.ReferenceDBDataManager;
import com.ikmb.varwatchsql.workflow.analysis.AnalysisBuilder;
import com.ikmb.varwatchsql.workflow.job.AnalysisJobSQL;
import com.ikmb.varwatchsql.workflow.job.JobManager;
import java.util.List;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author bfredrich
 */
public class BeaconWeeklyJob implements Job {

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        System.out.println("create beacon job");
        Injector injector = (Injector) jec.getMergedJobDataMap().get("injector");

        ReferenceDBDataManager refDBDataManager = injector.getInstance(ReferenceDBDataManager.class);
        JobManager jobManager = injector.getInstance(JobManager.class);
        DatasetManager dsManager = injector.getInstance(DatasetManager.class);

        List<Long> datasetIds = dsManager.getAllDatasetIds();

        List<RefDatabaseSQL> referenceDBs = refDBDataManager.getActiveDatabases();
        for (Long id : datasetIds) {
            for (RefDatabaseSQL referenceDB : referenceDBs) {
                if (referenceDB.getImplementation().equals("global_beacon")) {
                    jobManager.createJob(id, AnalysisBuilder.ModuleName.SCREENING_BEACON, referenceDB.getId().toString(), AnalysisJobSQL.JobAction.UPDATE.toString());
                }
            }
            jobManager.createJob(id, AnalysisBuilder.ModuleName.SCREEN_BEACON_RESULT_COLLECT, null, AnalysisJobSQL.JobAction.UPDATE.toString());
        }
        System.out.println("finish create job");
    }

}

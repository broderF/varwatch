/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchbarkeeper;

import com.google.inject.Injector;
import com.ikmb.core.workflow.job.JobManager;
import com.ikmb.varwatchsql.workflow.analysis.AnalysisBuilder;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author bfredrich
 */
public class SanityCheckJob implements Job {

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        System.out.println("create sanity check job");
        Injector injector = (Injector) jec.getMergedJobDataMap().get("injector");

        JobManager jobManager = injector.getInstance(JobManager.class);
        jobManager.createJob(AnalysisBuilder.ModuleName.SANITY_CHECK, null, "NEW");
        System.out.println("finish sanity check job");
    }
}

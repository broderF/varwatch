/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchbarkeeper;

import com.google.inject.Injector;
import com.ikmb.varwatchsql.auth.user.UserManager;
import com.ikmb.varwatchsql.auth.user.UserSQL;
import com.ikmb.varwatchsql.workflow.analysis.AnalysisBuilder;
import com.ikmb.varwatchsql.workflow.job.JobManager;
import java.util.List;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author bfredrich
 */
public class ReportScheduleJob implements Job {

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        System.out.println("create report job");
        Injector injector = (Injector) jec.getMergedJobDataMap().get("injector");

        UserManager userManager = injector.getInstance(UserManager.class);
        List<UserSQL> userList = userManager.getAllUser();
        JobManager jobManager = injector.getInstance(JobManager.class);
        for(UserSQL user: userList){
            jobManager.createJob(AnalysisBuilder.ModuleName.REPORT, String.valueOf(user.getId()), "NEW");
        }
        System.out.println("finish create job");
    }

}

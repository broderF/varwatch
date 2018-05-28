/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchbarkeeper;

import com.google.inject.Injector;
import com.ikmb.core.VWConfiguration;
import com.ikmb.core.data.workflow.job.JobManager;
import com.ikmb.core.data.workflow.worker.AnalysisWorker;
import com.ikmb.core.data.workflow.worker.WorkerManager;
import com.ikmb.sql.guice.VWInjectionInit;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import static org.quartz.JobBuilder.newJob;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import org.quartz.Trigger;
import static org.quartz.TriggerBuilder.newTrigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author Broder
 */
public class VarWatchBarkeeper {

//    private static final Integer MAX_WORKER = 3;
    public static void main(String[] args) throws SchedulerException {
        // create Options object
        Options options = new Options();

        options.addOption("cp", "config-path", true, "the config path");
        CommandLineParser parser = new DefaultParser();
        String pathToConfig = null;
        try {
            CommandLine cmd = parser.parse(options, args);
            if (!cmd.hasOption("cp")) {
                System.out.println("Noo Config Path");
                System.exit(0);
            } else {
                pathToConfig = cmd.getOptionValue("cp");
            }
        } catch (ParseException ex) {
            Logger.getLogger(VarWatchBarkeeper.class.getName()).log(Level.SEVERE, null, ex);
        }

        VarWatchBarkeeper barkeeper = new VarWatchBarkeeper();
//        updater.run();
//        barkeeper.initUpdater();
        barkeeper.run(pathToConfig);
    }

    private void initUpdater() {
        Timer timer = new Timer();
        Calendar instance = Calendar.getInstance();
        instance.set(2015, 6, 14, 17, 7, 0);
//        System.out.println(System.currentTimeMillis());
//        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
//        System.out.println(df.format(instance.getTime()));
        //        ReferenceDatabaseUpdater updater = new ReferenceDatabaseUpdater();
        //Setting the date from when you want to activate scheduling
        //        date.set(2015, 7, 13, 14, 53);

//        System.out.println(date);
        //execute every 3 seconds
//        timer.schedule(new ReferenceDatabaseUpdater(), instance.getTime(), 86400000);
    }

    private void run(String pathToConfig) throws SchedulerException {
        VWInjectionInit.init(pathToConfig);
        Injector injector = VWInjectionInit.getInstance().getVWInjector();
        WorkerManager workerManager = injector.getInstance(WorkerManager.class);
        JobManager jobManager = injector.getInstance(JobManager.class);
//        initReportScheduler(injector);

        Integer max_worker = VWInjectionInit.getInstance().getConfiguration().getNrWorker();
        while (true) {

            Integer numberOfAvailableWorker = getNumberOfAvailableWorker(workerManager);
            Integer numberOfAvailableJobs = getNumberOfAvailableJobs(jobManager);
            for (int i = 0; i < Math.min(max_worker - numberOfAvailableWorker, (numberOfAvailableJobs - numberOfAvailableWorker + 1) / 2); i++) {
                createNewWorker(workerManager, pathToConfig);
            }
            List<AnalysisWorker> worker = getAvailableWorker(workerManager);
            for (AnalysisWorker curWorker : worker) {
                if (new DateTime().minusMinutes(30).isAfter(curWorker.getBorn())) {
                    curWorker.setStatus("FAILED");
                    workerManager.update(curWorker);
                }
            }
            try {
                System.out.println("no worker needed " + new DateTime().toString(DateTimeFormat.forPattern("yyyy.MM.dd HH.mm.ss")));
                Thread.sleep(20000);                 //20 sec
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private List<AnalysisWorker> getAvailableWorker(WorkerManager workerManager) {
        return workerManager.getAvailableWorker();
    }

    private Integer getNumberOfAvailableWorker(WorkerManager workerManager) {
        List<AnalysisWorker> worker = workerManager.getAvailableWorker();
        return worker.size();
    }

    private Integer getNumberOfAvailableJobs(JobManager jobManager) {
        return jobManager.getNofAvailableJobs();
    }

    private void createNewWorker(WorkerManager workerManager, String pathToConfig) {
        VWConfiguration configuration = VWInjectionInit.getInstance().getConfiguration();
        String pathToWorkerScript = configuration.getPathToJobscript();
        String pathToWorker = configuration.getPathToWorker();
        String pathToLog = configuration.getPathToLog();

        try {
            AnalysisWorker workerSQL = workerManager.createWorker();
            Long id = workerSQL.getId();

            String command = "sbatch -o " + pathToLog + id + "_output.txt " + pathToWorkerScript + " " + pathToWorker + " " + pathToConfig + " " + id;
            System.out.println("create new worker: " + command);
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec(command);
//            ProcessBuilder pb = new ProcessBuilder(command);
//            pb.redirectOutput(Redirect.INHERIT);
//            pb.redirectError(Redirect.INHERIT);
//            Process p = pb.start();
        } catch (IOException ex) {
            Logger.getLogger(VarWatchBarkeeper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initReportScheduler(Injector injector) throws SchedulerException {
        //Create instance of factory
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();

        //Get schedular
        Scheduler scheduler = schedulerFactory.getScheduler();

        //Create JobDetail object specifying which Job you want to execute
        JobDataMap data = new JobDataMap();
        data.put("injector", injector);
        JobDetail job = newJob(ReportScheduleJob.class).setJobData(data)
                .withIdentity("report_job", "report_group")
                .build();

        // Trigger the job to run now, and then repeat every 40 seconds
        DateTime currentTime = new DateTime();
        DateTime reportDateTime = currentTime.plusDays(1).withHourOfDay(3).withMinuteOfHour(0).withSecondOfMinute(0);
        Trigger trigger = newTrigger()
                .withIdentity("report_trigger", "report_group")
                .startAt(reportDateTime.toDate())
                .withSchedule(simpleSchedule()
                        .withIntervalInHours(24)
                        .repeatForever())
                .build();
        scheduler.scheduleJob(job, trigger);

        //Trigger Beacon Job
        DateTime beaconDateTime = currentTime.plusDays(1).withHourOfDay(5).withMinuteOfHour(0).withSecondOfMinute(0);
        JobDataMap beaconData = new JobDataMap();
        beaconData.put("injector", injector);
        JobDetail beaconJob = newJob(BeaconWeeklyJob.class).setJobData(beaconData)
                .withIdentity("beacon_job", "beacon_group")
                .build();
        Trigger beaconTrigger = newTrigger()
                .withIdentity("beacon_job", "beacon_group")
                .startAt(beaconDateTime.toDate())
                .withSchedule(simpleSchedule()
                        .withIntervalInHours(48)
                        .repeatForever())
                .build();
        scheduler.scheduleJob(beaconJob, beaconTrigger);

        //Trigger Sanity Check Job
        DateTime sanityCheckDateTime = currentTime.plusDays(0).plusMinutes(1);
        JobDataMap sanityCheckData = new JobDataMap();
        sanityCheckData.put("injector", injector);
        JobDetail sanityJob = newJob(SanityCheckJob.class)
                .withIdentity("sanity_job", "sanity_group").setJobData(sanityCheckData)
                .build();
        Trigger sanityTrigger = newTrigger()
                .withIdentity("sanity_job", "sanity_group")
                .startAt(sanityCheckDateTime.toDate())
                .withSchedule(simpleSchedule()
                        .withIntervalInMinutes(30)
                        .repeatForever())
                .build();
        scheduler.scheduleJob(sanityJob, sanityTrigger);

        //Pass JobDetail and trigger dependencies to schedular
        //Start schedular
        scheduler.start();
    }
}

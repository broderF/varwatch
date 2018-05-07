/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchworker;

import com.google.inject.Injector;
import com.ikmb.WorkerLauncher;
import com.ikmb.annotation.AnnotationWorker;
import com.ikmb.extraction.ExtractVariantsWorker;
import com.ikmb.matching.BeaconCollectorWorker;
import com.ikmb.matching.HGMDScreeningWorker;
import com.ikmb.matching.ScreeningWorker;
import com.ikmb.matching.VarWatchScreeningWorker;
import com.ikmb.report.ReportWorker;
import com.ikmb.sanity.SanityWorker;
import com.ikmb.varwatchsql.entities.AnalysisSQL;
import com.ikmb.varwatchsql.entities.AnalysisWorkerSQL;
import com.ikmb.varwatchsql.workflow.job.AnalysisJobSQL;

/**
 *
 * @author Broder
 */
public class WorkerFactory {

//    public static Worker getWorker(String module) {
//        Worker worker = null;
//        switch (module) {
//            case AnalysisSQL.ANALYSIS_ANNOTATION:
////                worker = new AnnotationWorker();
//                break;
//            case AnalysisSQL.ANALYSIS_EXTRACTION:
//                worker = new ExtractVariantsWorker();
//                break;
//            case AnalysisSQL.ANALYSIS_COLLECTING:
////                worker = new CollectScreeningResultWorker();
//                break;
//            case AnalysisSQL.ANALYSIS_NOTIFICATION:
////                worker = new NotificationWorker();
//                break;
//            case AnalysisSQL.ANALYSIS_SCREENING:
////                worker = new ScreeningWorker();
//                break;
//        }
//        return worker;
//    }
    public static Worker getWorker(AnalysisWorkerSQL workerSQL, AnalysisSQL analysis, AnalysisJobSQL jobSQL) {
        Worker worker = null;
        Injector injector = WorkerLauncher.injector.getVWInjector();
        switch (analysis.getModule()) {
            case AnalysisSQL.ANALYSIS_EXTRACTION:
                worker = injector.getInstance(ExtractVariantsWorker.class);
                break;
            case AnalysisSQL.ANALYSIS_ANNOTATION:
                worker = injector.getInstance(AnnotationWorker.class);
                break;
            case AnalysisSQL.ANALYSIS_SCREENING_VARWATCH:
                worker = injector.getInstance(VarWatchScreeningWorker.class);
                break;
            case AnalysisSQL.ANALYSIS_SCREENING_HGMD:
                worker = injector.getInstance(HGMDScreeningWorker.class);
                break;
            case AnalysisSQL.ANALYSIS_COLLECTING:
                worker = injector.getInstance(CollectScreeningResultWorkerNew.class);
                break;
            case AnalysisSQL.ANALYSIS_SCREENING_BEACON:
                worker = injector.getInstance(ScreeningWorker.class);
                break;
            case AnalysisSQL.ANALYSIS_COLLECTING_BEACON:
                worker = injector.getInstance(BeaconCollectorWorker.class);
                break;
            case AnalysisSQL.ANALYSIS_REPORT:
                worker = injector.getInstance(ReportWorker.class);
                break;
            case AnalysisSQL.ANALYSIS_SANITY_CHECK:
                worker = injector.getInstance(SanityWorker.class);
                break;
        }

        worker.setAnalysisJobSQL(jobSQL);
        worker.setAnalysisSQL(analysis);
        worker.setWorkerSQL(workerSQL);
        return worker;
    }
}

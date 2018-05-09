/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchworker;

import com.google.inject.Injector;
import com.ikmb.WorkerLauncher;
import com.ikmb.annotation.AnnotationWorker;
import com.ikmb.core.workflow.analysis.Analysis;
import com.ikmb.core.workflow.job.AnalysisJob;
import com.ikmb.core.workflow.worker.AnalysisWorker;
import com.ikmb.extraction.ExtractVariantsWorker;
import com.ikmb.matching.BeaconCollectorWorker;
import com.ikmb.matching.HGMDScreeningWorker;
import com.ikmb.matching.ScreeningWorker;
import com.ikmb.matching.VarWatchScreeningWorker;
import com.ikmb.report.ReportWorker;
import com.ikmb.sanity.SanityWorker;

/**
 *
 * @author Broder
 */
public class WorkerFactory {

//    public static Worker getWorker(String module) {
//        Worker worker = null;
//        switch (module) {
//            case Analysis.ANALYSIS_ANNOTATION:
////                worker = new AnnotationWorker();
//                break;
//            case Analysis.ANALYSIS_EXTRACTION:
//                worker = new ExtractVariantsWorker();
//                break;
//            case Analysis.ANALYSIS_COLLECTING:
////                worker = new CollectScreeningResultWorker();
//                break;
//            case Analysis.ANALYSIS_NOTIFICATION:
////                worker = new NotificationWorker();
//                break;
//            case Analysis.ANALYSIS_SCREENING:
////                worker = new ScreeningWorker();
//                break;
//        }
//        return worker;
//    }
    public static Worker getWorker(AnalysisWorker workerSQL, Analysis analysis, AnalysisJob jobSQL) {
        Worker worker = null;
        Injector injector = WorkerLauncher.injector.getVWInjector();
        switch (analysis.getModule()) {
            case Analysis.ANALYSIS_EXTRACTION:
                worker = injector.getInstance(ExtractVariantsWorker.class);
                break;
            case Analysis.ANALYSIS_ANNOTATION:
                worker = injector.getInstance(AnnotationWorker.class);
                break;
            case Analysis.ANALYSIS_SCREENING_VARWATCH:
                worker = injector.getInstance(VarWatchScreeningWorker.class);
                break;
            case Analysis.ANALYSIS_SCREENING_HGMD:
                worker = injector.getInstance(HGMDScreeningWorker.class);
                break;
            case Analysis.ANALYSIS_COLLECTING:
                worker = injector.getInstance(CollectScreeningResultWorkerNew.class);
                break;
            case Analysis.ANALYSIS_SCREENING_BEACON:
                worker = injector.getInstance(ScreeningWorker.class);
                break;
            case Analysis.ANALYSIS_COLLECTING_BEACON:
                worker = injector.getInstance(BeaconCollectorWorker.class);
                break;
            case Analysis.ANALYSIS_REPORT:
                worker = injector.getInstance(ReportWorker.class);
                break;
            case Analysis.ANALYSIS_SANITY_CHECK:
                worker = injector.getInstance(SanityWorker.class);
                break;
        }

        worker.setAnalysisJob(jobSQL);
        worker.setAnalysis(analysis);
        worker.setWorker(workerSQL);
        return worker;
    }
}

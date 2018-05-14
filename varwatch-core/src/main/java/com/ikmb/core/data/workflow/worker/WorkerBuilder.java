/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.workflow.worker;

import org.joda.time.DateTime;

/**
 *
 * @author broder
 */
public class WorkerBuilder {

    public AnalysisWorker buildNewWorker() {
        AnalysisWorker worker = new AnalysisWorker();
        worker.setStatus("READY");
        worker.setBorn(new DateTime());
        worker.setWorkDone(0);
        return worker;
    }
}

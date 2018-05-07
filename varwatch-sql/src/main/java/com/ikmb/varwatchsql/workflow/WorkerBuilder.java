/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.workflow;

import com.ikmb.varwatchsql.entities.AnalysisWorkerSQL;
import org.joda.time.DateTime;

/**
 *
 * @author broder
 */
public class WorkerBuilder {

    public AnalysisWorkerSQL buildNewWorker() {
        AnalysisWorkerSQL worker = new AnalysisWorkerSQL();
        worker.setStatus("READY");
        worker.setBorn(new DateTime());
        worker.setWorkDone(0);
        return worker;
    }
}

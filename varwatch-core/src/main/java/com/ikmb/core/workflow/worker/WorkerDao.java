/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.workflow.worker;

import java.util.List;

/**
 *
 * @author broder
 */
public interface WorkerDao {


    public void refresh(AnalysisWorker worker);

    public void save(AnalysisWorker worker) ;

    public void update(AnalysisWorker worker);

    public List<AnalysisWorker> getAvailableWorker();

    public AnalysisWorker getWorker(Long id);

    public List<AnalysisWorker> getWorkerByStatus(String status) ;

}

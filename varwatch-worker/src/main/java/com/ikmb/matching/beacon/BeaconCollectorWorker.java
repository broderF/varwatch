/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.matching.beacon;

import com.google.inject.Inject;
import com.ikmb.WorkFlowManager;
import com.ikmb.utils.WorkerInputHandler;
import com.ikmb.core.data.dataset.DatasetManager;
import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.data.matching.MatchVariantDataManager;
import com.ikmb.core.data.reference_db.RefDatabase;
import com.ikmb.core.data.reference_db.ReferenceDBDataManager;
import com.ikmb.core.data.workflow.analysis.Analysis;
import com.ikmb.core.data.workflow.job.AnalysisJob;
import com.ikmb.core.data.workflow.job.JobManager;
import com.ikmb.core.data.workflow.worker.AnalysisWorker;
import com.ikmb.core.data.workflow.analysis.AnalysisBuilder;
import com.ikmb.varwatchworker.Worker;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author bfredrich
 */
public class BeaconCollectorWorker implements Worker {

    private final Logger logger = LoggerFactory.getLogger(BeaconCollectorWorker.class);
    protected AnalysisWorker _workerSQL;
    protected Analysis _analysisSQL;
    protected AnalysisJob _analysisJobSQL;

    private DatasetVW _dataset;

    @Inject
    private WorkerInputHandler workerInputHandler;

    @Inject
    private JobManager jobManager;

    @Inject
    private DatasetManager dsDataManager;

    @Inject
    private ReferenceDBDataManager refDBDataManager;

    @Inject
    private MatchVariantDataManager matchVariantDm;

    private boolean procondFullfilled = true;

    private WorkFlowManager.JobProcessStatus jobProcessStatus = WorkFlowManager.JobProcessStatus.FAILED;

    @Override
    public void runJob() {
        logger.info("-----start beacon collection job-----");

        parseInput();

        //check precond
        List<AnalysisJob> referenceDBJobs = jobManager.getBeaconScreeningJobs(_dataset);
        for (AnalysisJob analysisJob : referenceDBJobs) {
            if (!analysisJob.getStatus().equals("SUCCESSFUL")) {
                jobProcessStatus = WorkFlowManager.JobProcessStatus.PRECONDITION_FAILED;
                return;
            }
        }

//        for (VariantSQL variant : _dataset.getVariants()) {
//            List<MatchSQL> curMatches = matchVariantDm.getMatchesByVariant(variant);
//            if (!curMatches.isEmpty()) {
//                logger.info("beacon match found for variant with id {}", variant.getId());
//            }
//            for (MatchSQL match : curMatches) {
//                logger.info("Match found with id", match.getId());
//            }
//        }
//
//        matchVariantDM.deleteBeaconMatchedVariants(_dataset);
        if (_analysisJobSQL.getAction().equals("NEW")) {
            List<RefDatabase> referenceDBs = refDBDataManager.getActiveDatabases();
            for (RefDatabase referenceDB : referenceDBs) {
//            if (referenceDB.getImplementation().equals("varwatch")) {
//                jobManager.createJob(_dataset.getId(), AnalysisBuilder.ModuleName.SCREENING, referenceDB.getId().toString(), false);
//            } else
                if (referenceDB.getImplementation().equals("hgmd_match")) {
                    jobManager.createJob(_dataset.getId(), AnalysisBuilder.ModuleName.SCREENING_HGMD, referenceDB.getId().toString(), AnalysisJob.JobAction.NEW.toString());
                }
            }
        }

//        jobManager.createJob(_dataset.getId(), AnalysisBuilder.ModuleName.SCREEN_RESULT_COLLECT, null, false);
        jobProcessStatus = WorkFlowManager.JobProcessStatus.SUCCESSFUL;
    }

    @Override
    public void setWorker(AnalysisWorker workerSQL) {
        _workerSQL = workerSQL;
    }

    @Override
    public void setAnalysis(Analysis analysisSQL) {
        _analysisSQL = analysisSQL;
    }

    @Override
    public void setAnalysisJob(AnalysisJob analysisJobSQL) {
        _analysisJobSQL = analysisJobSQL;
    }

    private void parseInput() {
        workerInputHandler.setJob(_analysisJobSQL);
        workerInputHandler.setAnalysis(_analysisSQL);
        workerInputHandler.parseInputData();

        Long datasetID = workerInputHandler.getDatasetID();

        _dataset = dsDataManager.getDatasetWithVariantsByID(datasetID);
    }

    @Override
    public void undo() {
    }

    @Override
    public WorkFlowManager.JobProcessStatus getJobProcessStatus() {
        return jobProcessStatus;
    }

}

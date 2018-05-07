/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.matching;

import com.google.inject.Inject;
import com.ikmb.WorkFlowManager;
import com.ikmb.utils.WorkerInputHandler;
import com.ikmb.varwatchsql.entities.AnalysisSQL;
import com.ikmb.varwatchsql.entities.AnalysisWorkerSQL;
import com.ikmb.varwatchsql.entities.DatasetVWSQL;
import com.ikmb.varwatchsql.variant_data.dataset.DatasetManager;
import com.ikmb.varwatchsql.variant_data.variant.VariantSQL;
import com.ikmb.varwatchsql.data.reference_db.RefDatabaseSQL;
import com.ikmb.varwatchsql.data.reference_db.ReferenceDBDataManager;
import com.ikmb.varwatchsql.matching.MatchSQL;
import com.ikmb.varwatchsql.matching.MatchVariantDataManager;
import com.ikmb.varwatchsql.workflow.analysis.AnalysisBuilder;
import com.ikmb.varwatchsql.workflow.job.AnalysisJobSQL;
import com.ikmb.varwatchsql.workflow.job.JobManager;
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
    protected AnalysisWorkerSQL _workerSQL;
    protected AnalysisSQL _analysisSQL;
    protected AnalysisJobSQL _analysisJobSQL;

    private DatasetVWSQL _dataset;

//    private byte[] _rawData;
//    private String _rawDataType;
//    private byte[] _vcfFile;
    @Inject
    private WorkerInputHandler workerInputHandler;

    @Inject
    private JobManager jobManager;

    @Inject
    private DatasetManager dsDataManager;

    @Inject
    private MatchVariantDataManager matchVariantDM;

//    @Inject
//    private VariantStatusBuilder variantStatusBuilder;
//    @Inject
//    private VariantBuilder variantBuilder;
//
//    @Inject
//    private VariantStatusManager variantStatusManager;
//
//    @Inject
//    private HPOTermBuilder hpoTermBuilder;
//
//    @Inject
//    private VariantHash variantHasher;
//    @Inject
//    private VariantDataManager variantDM;
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
        List<AnalysisJobSQL> referenceDBJobs = jobManager.getBeaconScreeningJobs(_dataset);
        for (AnalysisJobSQL analysisJob : referenceDBJobs) {
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
            List<RefDatabaseSQL> referenceDBs = refDBDataManager.getActiveDatabases();
            for (RefDatabaseSQL referenceDB : referenceDBs) {
//            if (referenceDB.getImplementation().equals("varwatch")) {
//                jobManager.createJob(_dataset.getId(), AnalysisBuilder.ModuleName.SCREENING, referenceDB.getId().toString(), false);
//            } else
                if (referenceDB.getImplementation().equals("hgmd_match")) {
                    jobManager.createJob(_dataset.getId(), AnalysisBuilder.ModuleName.SCREENING_HGMD, referenceDB.getId().toString(), AnalysisJobSQL.JobAction.NEW.toString());
                }
            }
        }

//        jobManager.createJob(_dataset.getId(), AnalysisBuilder.ModuleName.SCREEN_RESULT_COLLECT, null, false);
        jobProcessStatus = WorkFlowManager.JobProcessStatus.SUCCESSFUL;
    }

    @Override
    public void setWorkerSQL(AnalysisWorkerSQL workerSQL) {
        _workerSQL = workerSQL;
    }

    @Override
    public void setAnalysisSQL(AnalysisSQL analysisSQL) {
        _analysisSQL = analysisSQL;
    }

    @Override
    public void setAnalysisJobSQL(AnalysisJobSQL analysisJobSQL) {
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

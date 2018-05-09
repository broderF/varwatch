/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.matching;

import com.google.inject.Inject;
import com.ikmb.WorkFlowManager;
import com.ikmb.core.data.dataset.DatasetManager;
import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.data.matching.Match;
import com.ikmb.core.data.matching.MatchVariantDataManager;
import com.ikmb.core.data.reference_db.RefDatabase;
import com.ikmb.core.data.reference_db.ReferenceDBDataManager;
import com.ikmb.core.data.variant.VariantStatusManager;
import com.ikmb.core.workflow.analysis.Analysis;
import com.ikmb.core.workflow.job.AnalysisJob;
import com.ikmb.core.workflow.job.JobManager;
import com.ikmb.core.workflow.worker.AnalysisWorker;
import com.ikmb.utils.WorkerInputHandler;
import com.ikmb.varwatchsql.workflow.analysis.AnalysisBuilder;
import com.ikmb.varwatchworker.Worker;
import java.util.List;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Broder
 */
public class VarWatchScreeningWorker implements Worker {

    protected AnalysisWorker _workerSQL;
    protected Analysis _analysisSQL;
    protected AnalysisJob _analysisJobSQL;
    private DatasetVW _dataset;
    private RefDatabase _referenceDB;
    private WorkFlowManager.JobProcessStatus jobProcessStatus = WorkFlowManager.JobProcessStatus.FAILED;
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(VarWatchScreeningWorker.class);

    @Inject
    private MatchVariantDataManager matchDataManager;

    @Inject
    private VariantStatusManager variantStatusManager;

    @Inject
    private DatasetManager dsManager;

    @Inject
    private WorkerInputHandler workerInputHandler;

    @Inject
    private ReferenceDBDataManager referenceDBDataManager;

    @Inject
    private JobManager jobManager;

//    @Inject
//    private DatasetManager dsDataManager;
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

        _dataset = dsManager.getDatasetWithVariantsByID(datasetID);
        Long refDbI = workerInputHandler.getLong("database_id");
        _referenceDB = referenceDBDataManager.getReferenceDBById(refDbI);
    }

    @Override
    public void runJob() {

        parseInput();

        //check precond
        if (jobManager.isVarWatchJobProcessed(_dataset)) {
            jobProcessStatus = WorkFlowManager.JobProcessStatus.PRECONDITION_FAILED;
            return;
        }

        logger.info("----- Start Screening Database " + _referenceDB.getName() + "-----");
        DatabaseScreener screener = ScreeningFactory.getScreeningDatabase(_referenceDB);
        screener.initialize(_referenceDB, _dataset);
        RefDatabase varwatchDB = referenceDBDataManager.getVarWatchDatabase();
        screener.setVWDatabase(varwatchDB);
        screener.run();

        List<Match> matches = screener.getMatches();
        logger.info("Found {} matches", matches.size());
        matches = matchDataManager.persistMatches(matches);
        for (Match matchSQL : matches) {
            logger.info("match with id {} saved in database", matchSQL.getId());
        }
        variantStatusManager.persistMatchStatus(matches, _referenceDB.getName());
        jobManager.createJob(_dataset.getId(), AnalysisBuilder.ModuleName.SCREEN_RESULT_COLLECT, null, AnalysisJob.JobAction.NEW.toString());
        _dataset.setCompleted(true);
        dsManager.updateDataset(_dataset);
        jobProcessStatus = WorkFlowManager.JobProcessStatus.SUCCESSFUL;
    }

    @Override
    public void undo() {
        //delete matches
    }

    @Override
    public WorkFlowManager.JobProcessStatus getJobProcessStatus() {
        return jobProcessStatus;
    }

}

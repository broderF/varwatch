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
import com.ikmb.core.data.workflow.analysis.Analysis;
import com.ikmb.core.data.workflow.job.AnalysisJob;
import com.ikmb.core.data.workflow.worker.AnalysisWorker;
import com.ikmb.utils.WorkerInputHandler;
import com.ikmb.varwatchworker.Worker;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Broder
 */
public class ScreeningWorker implements Worker {

    protected AnalysisWorker _workerSQL;
    protected Analysis _analysisSQL;
    protected AnalysisJob _analysisJobSQL;
    private DatasetVW _dataset;
    private RefDatabase _referenceDB;
    private WorkFlowManager.JobProcessStatus jobProcessStatus = WorkFlowManager.JobProcessStatus.FAILED;
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(ScreeningWorker.class);

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

        logger.info("----- Start Screening Database " + _referenceDB.getName() + "-----");
        DatabaseScreener screener = ScreeningFactory.getScreeningDatabase(_referenceDB);
        screener.initialize(_referenceDB, _dataset);
        RefDatabase varwatchDB = referenceDBDataManager.getVarWatchDatabase();
        screener.setVWDatabase(varwatchDB);
        screener.run();

        List<Match> matches = screener.getMatches();
        logger.info("Found {} matches", matches.size());
        List<Match> filteredMatches = filterForDuplicates(matches);
        logger.info("Found {} duplicates", matches.size() - filteredMatches.size());
        filteredMatches = matchDataManager.persistMatches(filteredMatches);
        for (Match matchSQL : filteredMatches) {
            logger.info("match with id {} saved in database", matchSQL.getId());
        }
        variantStatusManager.persistMatchStatus(filteredMatches, _referenceDB.getName());
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

    private List<Match> filterForDuplicates(List<Match> matches) {
        List<Match> filteredMatches = new ArrayList<>();
        for (Match curMatch : matches) {
            if (!matchDataManager.isDuplicatedMatch(curMatch)) {
                filteredMatches.add(curMatch);
            }
        }
        return filteredMatches;
    }

}

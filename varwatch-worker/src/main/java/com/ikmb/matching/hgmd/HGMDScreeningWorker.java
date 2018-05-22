/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.matching.hgmd;

import com.google.inject.Inject;
import com.ikmb.WorkFlowManager;
import com.ikmb.core.data.dataset.DatasetManager;
import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.data.matching.Match;
import com.ikmb.core.data.matching.MatchVariant;
import com.ikmb.core.data.matching.MatchVariantDataManager;
import com.ikmb.core.data.reference_db.RefDatabase;
import com.ikmb.core.data.reference_db.ReferenceDBDataManager;
import com.ikmb.core.data.variant.Variant;
import com.ikmb.core.data.variant.VariantDataManager;
import com.ikmb.core.data.variant.VariantStatusManager;
import com.ikmb.core.data.workflow.analysis.Analysis;
import com.ikmb.core.data.workflow.job.AnalysisJob;
import com.ikmb.core.data.workflow.job.JobManager;
import com.ikmb.core.data.workflow.worker.AnalysisWorker;
import com.ikmb.utils.WorkerInputHandler;
import com.ikmb.core.data.workflow.analysis.AnalysisBuilder;
//import com.ikmb.matching.ScreeningFactory;
import com.ikmb.varwatchworker.Worker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Broder
 */
public class HGMDScreeningWorker implements Worker {

    protected AnalysisWorker _workerSQL;
    protected Analysis _analysisSQL;
    protected AnalysisJob _analysisJobSQL;
    private DatasetVW _dataset;
    private RefDatabase _referenceDB;
    private WorkFlowManager.JobProcessStatus jobProcessStatus = WorkFlowManager.JobProcessStatus.FAILED;
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(HGMDScreeningWorker.class);

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

    @Inject
    private VariantDataManager variantDataManager;
    @Inject
    private HGMDScreener screener;

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
//        DatabaseScreener screener = ScreeningFactory.getScreeningDatabase(_referenceDB);
        screener.initialize(_referenceDB, _dataset);
        RefDatabase varwatchDB = referenceDBDataManager.getVarWatchDatabase();
        screener.setVWDatabase(varwatchDB);
        screener.run();

        List<Match> matches = screener.getMatches();
        logger.info("Found {} matches", matches.size());

        //filter matches
        Map<Long, List<MatchVariant>> variantToMatches = new HashMap<>();
        Set<Long> variandIds = new HashSet<>();
        for (Match curMatch : matches) {
            Set<MatchVariant> matchedVariants = curMatch.getVariants();
            for (MatchVariant curMatchedVariant : matchedVariants) {
                if (curMatchedVariant.getDatabase().getName().equals("VarWatch")) {
                    Long variantId = curMatchedVariant.getVariantId();
                    if (variantToMatches.containsKey(variantId)) {
                        List<MatchVariant> tmpMatches = variantToMatches.get(variantId);
                        tmpMatches.add(curMatchedVariant);
                        variantToMatches.put(variantId, tmpMatches);
                    } else {
                        List<MatchVariant> tmpMatches = new ArrayList<>();
                        tmpMatches.add(curMatchedVariant);
                        variantToMatches.put(variantId, tmpMatches);
                    }
                }
            }
        }

        List<Match> filteredMatches = new ArrayList<>();
        for (Entry<Long, List<MatchVariant>> curVarId : variantToMatches.entrySet()) {
            Variant curVariant = variantDataManager.get(curVarId.getKey());
            List<MatchVariant> filteredCurMatches = matchDataManager.getFilteredList(curVarId.getValue(), curVariant);
            for (MatchVariant curMatchedVariant : filteredCurMatches) {
                filteredMatches.add(curMatchedVariant.getMatch());
            }
        }

        matches = matchDataManager.persistMatches(filteredMatches);

        for (Match matchSQL : matches) {
            logger.info("match with id {} saved in database", matchSQL.getId());
        }
        variantStatusManager.persistMatchStatus(matches, _referenceDB.getName());
        jobProcessStatus = WorkFlowManager.JobProcessStatus.SUCCESSFUL;

        List<RefDatabase> referenceDBs = referenceDBDataManager.getActiveDatabases();
        for (RefDatabase referenceDB : referenceDBs) {
            if (referenceDB.getImplementation().equals("varwatch")) {
                jobManager.createJob(_dataset.getId(), AnalysisBuilder.ModuleName.SCREENING_VARWATCH, referenceDB.getId().toString(), AnalysisJob.JobAction.NEW.toString());
            }
        }
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

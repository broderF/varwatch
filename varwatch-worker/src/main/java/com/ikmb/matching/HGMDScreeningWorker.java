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
import com.ikmb.varwatchsql.variant_data.variant.VariantDataManager;
import com.ikmb.varwatchsql.variant_data.variant.VariantSQL;
import com.ikmb.varwatchsql.data.reference_db.RefDatabaseSQL;
import com.ikmb.varwatchsql.data.reference_db.ReferenceDBDataManager;
import com.ikmb.varwatchsql.matching.MatchSQL;
import com.ikmb.varwatchsql.matching.MatchVariantDataManager;
import com.ikmb.varwatchsql.matching.MatchVariantSQL;
import com.ikmb.varwatchsql.status.variant.VariantStatusManager;
import com.ikmb.varwatchsql.workflow.analysis.AnalysisBuilder;
import com.ikmb.varwatchsql.workflow.job.AnalysisJobSQL;
import com.ikmb.varwatchsql.workflow.job.JobManager;
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

    protected AnalysisWorkerSQL _workerSQL;
    protected AnalysisSQL _analysisSQL;
    protected AnalysisJobSQL _analysisJobSQL;
    private DatasetVWSQL _dataset;
    private RefDatabaseSQL _referenceDB;
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

//    @Inject
//    private DatasetManager dsDataManager;
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
        RefDatabaseSQL varwatchDB = referenceDBDataManager.getVarWatchDatabase();
        screener.setVWDatabase(varwatchDB);
        screener.run();

        List<MatchSQL> matches = screener.getMatches();
        logger.info("Found {} matches", matches.size());

        //filter matches
        Map<Long, List<MatchVariantSQL>> variantToMatches = new HashMap<>();
        Set<Long> variandIds = new HashSet<>();
        for (MatchSQL curMatch : matches) {
            Set<MatchVariantSQL> matchedVariants = curMatch.getVariants();
            for (MatchVariantSQL curMatchedVariant : matchedVariants) {
                if (curMatchedVariant.getDatabase().getName().equals("VarWatch")) {
                    Long variantId = curMatchedVariant.getVariantId();
                    if (variantToMatches.containsKey(variantId)) {
                        List<MatchVariantSQL> tmpMatches = variantToMatches.get(variantId);
                        tmpMatches.add(curMatchedVariant);
                        variantToMatches.put(variantId, tmpMatches);
                    } else {
                        List<MatchVariantSQL> tmpMatches = new ArrayList<>();
                        tmpMatches.add(curMatchedVariant);
                        variantToMatches.put(variantId, tmpMatches);
                    }
                }
            }
        }
        
        List<MatchSQL> filteredMatches = new ArrayList<>();
        for (Entry<Long,List<MatchVariantSQL>> curVarId : variantToMatches.entrySet()) {
            VariantSQL curVariant = variantDataManager.get(curVarId.getKey());
            List<MatchVariantSQL> filteredCurMatches = matchDataManager.getFilteredList(curVarId.getValue(), curVariant);
            for(MatchVariantSQL curMatchedVariant: filteredCurMatches){
                filteredMatches.add(curMatchedVariant.getMatch());
            }
        }

        matches = matchDataManager.persistMatches(filteredMatches);

        for (MatchSQL matchSQL : matches) {
            logger.info("match with id {} saved in database", matchSQL.getId());
        }
        variantStatusManager.persistMatchStatus(matches, _referenceDB.getName());
        jobProcessStatus = WorkFlowManager.JobProcessStatus.SUCCESSFUL;

        List<RefDatabaseSQL> referenceDBs = referenceDBDataManager.getActiveDatabases();
        for (RefDatabaseSQL referenceDB : referenceDBs) {
            if (referenceDB.getImplementation().equals("varwatch")) {
                jobManager.createJob(_dataset.getId(), AnalysisBuilder.ModuleName.SCREENING_VARWATCH, referenceDB.getId().toString(), AnalysisJobSQL.JobAction.NEW.toString());
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

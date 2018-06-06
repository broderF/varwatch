/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.update;

import com.google.inject.Inject;
import com.ikmb.WorkFlowManager;
import com.ikmb.core.data.dataset.DatasetManager;
import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.data.hpo.HPOOboFileParser;
import com.ikmb.core.data.hpo.HPOTerm;
import com.ikmb.core.data.hpo.HPOUpdateManager;
import com.ikmb.core.data.hpo.Phenotype;
import com.ikmb.core.data.matching.Match;
import com.ikmb.core.data.matching.MatchVariant;
import com.ikmb.core.data.matching.MatchVariantDataManager;
import com.ikmb.core.data.variant.Variant;
import com.ikmb.core.data.workflow.analysis.Analysis;
import com.ikmb.core.data.workflow.job.AnalysisJob;
import com.ikmb.core.data.workflow.worker.AnalysisWorker;
import com.ikmb.matching.HPODistanceCalculator;
import com.ikmb.varwatchworker.Worker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.LoggerFactory;

/**
 *
 * @author broder
 */
public class HPOUpdateWorker implements Worker {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(HPOUpdateWorker.class);
    private WorkFlowManager.JobProcessStatus jobProcessStatus = WorkFlowManager.JobProcessStatus.FAILED;
    @Inject
    private HPOOboFileParser oboFileParser;
    @Inject
    private DatasetManager dsManager;
    @Inject
    private HPOUpdateManager hpoUpdateManager;
    @Inject
    private MatchVariantDataManager matchVariantDataManager;

    @Override
    public void runJob() {

        oboFileParser.parseHpoFromConfig();
        Set<String> termFrames = oboFileParser.gerPrimaryIds();
        Map<String, String> altIdToId = oboFileParser.getAltIdToIdMapping();
        Map<String, List<Long>> hpoToDatasetId = new HashMap<>();
        for (Long dsId : dsManager.getAllDatasetIds()) {
            DatasetVW datasetByID = dsManager.getDatasetByID(dsId);
            Set<Phenotype> phenotypes = datasetByID.getPhenotypes();
            for (Phenotype curPheno : phenotypes) {
                String identifier = curPheno.getPhenotype().getIdentifier();
                List<Long> datasetIds = hpoToDatasetId.getOrDefault(identifier, new ArrayList<Long>());
                datasetIds.add(dsId);
                hpoToDatasetId.put(identifier, datasetIds);
            }
        }

        //check if an id isnt a primary id anymore
        List<String> invalidIds = new ArrayList<>();
        for (String curId : hpoToDatasetId.keySet()) {
            if (!termFrames.contains(curId)) {
                invalidIds.add(curId);
            }
        }

        logger.info("nr of invalid hpo terms {}", invalidIds.size());

        //check if this id is in an alt_id, if yes -> add new hpo term to the database
        for (String curInvalidId : invalidIds) {
            logger.info("invalid hpo term {}: ", curInvalidId);
            String primaryId = altIdToId.get(curInvalidId);
            logger.info("primary hpo term {}: ", primaryId);
            HPOTerm curHpoTerm = hpoUpdateManager.getHpoTermOrUpdate(primaryId);
            List<Long> dsWithInvalidHpoId = hpoToDatasetId.get(curInvalidId);
            logger.info("nr of invalid ds terms {}", dsWithInvalidHpoId.size());
            for (Long curdsId : dsWithInvalidHpoId) {
                logger.info("invalid ds term {}: ", curdsId);
                DatasetVW datasetByID = dsManager.getDatasetByID(curdsId);
                Set<Phenotype> phenotypes = datasetByID.getPhenotypes();
                for (Phenotype curPheno : phenotypes) {
                    logger.info("try");
                    if (curPheno.getPhenotype().getIdentifier().equals(curInvalidId)) {
                        logger.info("found");
                        curPheno.setPhenotype(curHpoTerm);
                        hpoUpdateManager.updatePhenotype(curPheno);
                    }
                }
            }
        }

        //TODO recalculate all hpo matches for all varwatch matches!
//        List<Long> matchIds = matchVariantDataManager.getMatchesByVariant(null);
//        Match get = matchesByVariant.get(0);
//        Set<MatchVariant> variants = get.getVariants();
//        MatchVariant curVar = (MatchVariant) variants.toArray()[0];
//
//        Long variantId = curVar.getVariantId();
//        Variant tmp = null;
//        Set<Phenotype> phenotypes = tmp.getDataset().getPhenotypes();
//        HPODistanceCalculator blubb = null;
//        Double hpoSetDistance = blubb.getHPOSetDistance(phenotypes, phenotypes);
        jobProcessStatus = WorkFlowManager.JobProcessStatus.SUCCESSFUL;
    }

    @Override
    public void setWorker(AnalysisWorker workerSQL) {
    }

    @Override
    public void setAnalysis(Analysis analysisSQL) {
    }

    @Override
    public void setAnalysisJob(AnalysisJob analysisJobSQL) {
    }

    @Override
    public void undo() {
        //delete following jobs
        //delete this annotation job
        //redo extraction job (maybe variants are deleted) -> maybe delete variants in the collection job?
    }

    @Override
    public WorkFlowManager.JobProcessStatus getJobProcessStatus() {
        return jobProcessStatus;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.annotation;

import com.google.inject.Inject;
import com.ikmb.WorkFlowManager;
import com.ikmb.utils.WorkerInputHandler;
import com.ikmb.varwatchcommons.entities.GenomicFeature;
import com.ikmb.varwatchcommons.entities.VariantEffect;
import com.ikmb.varwatchcommons.tools.VariantEffectPredictor;
import com.ikmb.varwatchcommons.utils.ParserHelper;
import com.ikmb.varwatchsql.entities.AnalysisSQL;
import com.ikmb.varwatchsql.entities.AnalysisWorkerSQL;
import com.ikmb.varwatchsql.entities.DatasetVWSQL;
import com.ikmb.varwatchsql.entities.EnsemblSQL;
import com.ikmb.varwatchsql.entities.VariantEffectSQL;
import com.ikmb.varwatchsql.variant_data.dataset.DatasetManager;
import com.ikmb.varwatchsql.variant_data.varianteffect.VariantEffectDataManager;
import com.ikmb.varwatchsql.data.ensembl.EnsemblDataManager;
import com.ikmb.varwatchsql.data.reference_db.RefDatabaseSQL;
import com.ikmb.varwatchsql.data.reference_db.ReferenceDBDataManager;
import com.ikmb.varwatchsql.variant_data.variant.VariantSQL;
import com.ikmb.varwatchsql.workflow.analysis.AnalysisBuilder;
import com.ikmb.varwatchsql.workflow.job.AnalysisJobSQL;
import com.ikmb.varwatchsql.workflow.job.JobManager;
import com.ikmb.varwatchworker.Worker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Broder
 */
public class AnnotationWorker implements Worker {

    private final Logger logger = LoggerFactory.getLogger(AnnotationWorker.class);

    protected AnalysisWorkerSQL _workerSQL;
    protected AnalysisSQL _analysisSQL;
    protected AnalysisJobSQL _analysisJobSQL;
    private Map<String, String> _featureImpact;
    private DatasetVWSQL _dataset;
    private EnsemblSQL _predictorSQL;

    @Inject
    private WorkerInputHandler workerInputHandler;

    @Inject
    private AnnotationDataManager annotationDataManager;

    @Inject
    private EnsemblDataManager ensemblDataManager;

    @Inject
//    private VEPAnnotater vepAnnotator;
    private VariantEffectPredictor vep;

    @Inject
    private VariantEffectDataManager variantEffDataManager;

    @Inject
    private DatasetManager datasetManager;

    @Inject
    private ReferenceDBDataManager refDBDataManager;

    @Inject
    private JobManager jobManager;

    @Inject
    private SubmissionNotification submissionNoti;

    private WorkFlowManager.JobProcessStatus jobProcessStatus = WorkFlowManager.JobProcessStatus.FAILED;

    @Override
    public void runJob() {
        logger.info("-----Start annotation-----");
        parseInput();

        //work
//        vep.run(variant)
//        vep.runOfflineWithFasta(_dataset.getVcfFile(), _predictorSQL.getName(), String.valueOf(_dataset.getId()));
//        List<VariantEffectSQ> variantEffects = vepAnnotator.getVariantEffects();
        Set<VariantSQL> variants = _dataset.getVariants();
//        List<GenomicFeature> variantInfoFromByteArray = ParserHelper.getVariantInfoFromByteArray(vcfFile);
        List<VariantEffectSQL> filteredvariantEffects = new ArrayList<>();
        for (VariantSQL variant : variants) {
            List<VariantEffect> variantEffects = vep.run(variant.toVariant());
            for (VariantEffect curEffect : variantEffects) {
                if (curEffect.getTranscriptName().startsWith("ENST") && isImpactfull(curEffect.getConsequence())) {
                    filteredvariantEffects.add(VariantEffectSQL.get(curEffect));
                    variant.setUploadedVariantion(curEffect.getUploaded_variation());
                }
            }
        }
//        variantEffects = filteredvariantEffects;
        logger.info("{} canonical variants with high or moderate impact factor", filteredvariantEffects.size());
//        byte[] vepFile = vepAnnotator.getVEPFile();

        variantEffDataManager.persistSQLVariantEffects(filteredvariantEffects, _dataset);

        datasetManager.refresh(_dataset);
//        _dataset.setVepFile(vepFile);
//        _dataset.setCompleted(true);
        datasetManager.updateDataset(_dataset);

        submissionNoti.notifySubmission(_dataset);
        List<RefDatabaseSQL> referenceDBs = refDBDataManager.getActiveDatabases();
        for (RefDatabaseSQL referenceDB : referenceDBs) {
            if (referenceDB.getImplementation().equals("global_beacon")) {
                jobManager.createJob(_dataset.getId(), AnalysisBuilder.ModuleName.SCREENING_BEACON, referenceDB.getId().toString(), AnalysisJobSQL.JobAction.NEW.toString());
            }
        }

        jobManager.createJob(_dataset.getId(), AnalysisBuilder.ModuleName.SCREEN_BEACON_RESULT_COLLECT, null, AnalysisJobSQL.JobAction.NEW.toString());
        jobProcessStatus = WorkFlowManager.JobProcessStatus.SUCCESSFUL;
        logger.info("finish annotation job");
    }

    private Map<String, String> initFeatureMap() {
        Map<String, String> featureMap = new HashMap<String, String>();
        featureMap.put("splice_acceptor_variant", "high");
        featureMap.put("splice_donor_variant", "high");
        featureMap.put("stop_gained", "high");
        featureMap.put("frameshift_variant", "high");
        featureMap.put("stop_lost", "high");
        featureMap.put("start_lost", "high");
        featureMap.put("transcript_amplification", "high");
        featureMap.put("inframe_insertion", "moderate");
        featureMap.put("inframe_deletion", "moderate");
        featureMap.put("missense_variant", "moderate");
        featureMap.put("protein_altering_variant", "moderate");
        return featureMap;
    }

    private boolean isImpactfull(String consequence) {
        boolean impactfull = false;
        for (String impact : consequence.split(",")) {
            if (_featureImpact.containsKey(impact)) {
                impactfull = true;
                break;
            }
        }
        return impactfull;
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

        _dataset = annotationDataManager.getDatasetByID(datasetID);
        _predictorSQL = ensemblDataManager.getActiveEnsembl(false);
        _featureImpact = initFeatureMap();
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

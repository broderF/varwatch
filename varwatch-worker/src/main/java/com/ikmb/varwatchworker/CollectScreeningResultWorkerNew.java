/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchworker;

import com.google.inject.Inject;
import com.ikmb.WorkFlowManager;
import com.ikmb.matching.VarWatchScreenerNew.MatchType;
import com.ikmb.utils.WorkerInputHandler;
import com.ikmb.varwatchcommons.notification.NotificationSubmitter;
import com.ikmb.varwatchcommons.utils.VariantHash;
import com.ikmb.varwatchsql.entities.AnalysisSQL;
import com.ikmb.varwatchsql.entities.AnalysisWorkerSQL;
import com.ikmb.varwatchsql.entities.DatasetVWSQL;
import com.ikmb.varwatchsql.variant_data.dataset.DatasetManager;
import com.ikmb.varwatchsql.variant_data.variant.VariantBuilder;
import com.ikmb.varwatchsql.variant_data.variant.VariantDataManager;
import com.ikmb.varwatchsql.variant_data.variant.VariantSQL;
import com.ikmb.varwatchsql.data.hpo.HPOTermBuilder;
import com.ikmb.varwatchsql.matching.MatchSQL;
import com.ikmb.varwatchsql.matching.MatchVariantDataManager;
import com.ikmb.varwatchsql.matching.MatchVariantSQL;
import com.ikmb.varwatchsql.status.dataset.DatasetStatusBuilder;
import com.ikmb.varwatchsql.status.dataset.DatasetStatusManager;
import com.ikmb.varwatchsql.status.variant.VariantStatusBuilder;
import com.ikmb.varwatchsql.status.variant.VariantStatusManager;
import com.ikmb.varwatchsql.workflow.job.AnalysisJobSQL;
import com.ikmb.varwatchsql.workflow.job.JobManager;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author bfredrich
 */
public class CollectScreeningResultWorkerNew implements Worker {

    private final Logger logger = LoggerFactory.getLogger(CollectScreeningResultWorkerNew.class);

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
    private DatasetStatusManager statusManager;

    @Inject
    private VariantStatusManager variantStatusManager;

    @Inject
    private VariantStatusBuilder variantStatusBuilder;

    @Inject
    private VariantBuilder variantBuilder;

    @Inject
    private VariantDataManager variantDM;

    @Inject
    private HPOTermBuilder hpoTermBuilder;

    @Inject
    private VariantHash variantHasher;

    @Inject
    private MatchVariantDataManager matchDM;

    private boolean procondFullfilled = true;

    private WorkFlowManager.JobProcessStatus jobProcessStatus = WorkFlowManager.JobProcessStatus.FAILED;
    private Object datasetManager;

    @Override
    public void runJob() {
        logger.info("----- start result collection job ----");

        parseInput();

        //check precond
        List<AnalysisJobSQL> referenceDBJobs = jobManager.getScreeningJobs(_dataset.getId());
        for (AnalysisJobSQL analysisJob : referenceDBJobs) {
            if (!analysisJob.getStatus().equals("DONE")) {
                jobProcessStatus = WorkFlowManager.JobProcessStatus.PRECONDITION_FAILED;
                logger.info("precondition failed");
                return;
            }
        }

        List<VariantSQL> variants = variantDM.getVariantsByDatasetWithMatches(_dataset.getId());
        for (VariantSQL variant : variants) {
            List<MatchSQL> curMatches = matchDM.getMatchesByVariant(variant);
            for (MatchSQL match : curMatches) {
                if (match.getDatabase().getName().equals("VarWatch") && (match.getMatch_type().equals(MatchType.perfect.name()) || match.getMatch_type().equals(MatchType.nucleotide.name()) || match.getMatch_type().equals(MatchType.codon.name()))) {
                    Set<String> emails = new HashSet<>();
                    for (MatchVariantSQL matchedVar : match.getVariants()) {
                        Long variantId = matchedVar.getVariantId(); //all Variants should be VarWatch Variants
                        VariantSQL curVar = variantDM.get(variantId);
                        String mail = curVar.getDataset().getUser().getMail();
                        emails.add(mail);
                    }
                    //bingo match
                    String mailText = getMailTextFromVariant(variant);
                    NotificationSubmitter.sendMail(emails, mailText, "VarWatch: Identical variant found");
                }
            }
        }

//        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Size of matched variants to a beacon:{0}", beaconMatchedVariants.size());
//        for (VariantSQL curVar : beaconMatchedVariants.keySet()) {
//            //delete Variant, set Variantstatus, remove Matches
//            VWStatus status = variantStatusBuilder.withStatus(VariantStatusBuilder.VariantStatus.REJECTED).withMessage(VariantStatusBuilder.VariantStatusMessage.MATCHED_TO_BEACON).buildVWStatus();
//            VWVariant buildVW = variantBuilder.withSQLVariant(curVar).buildVW();
//            String variantHash = variantHasher.getVariantHash(buildVW, hpoTermBuilder.addFeatures(_dataset.getPhenotypes()).buildStringSet());
//            variantStatusManager.save(_dataset, null, variantHash, status);
//            List<MatchSQL> matches = beaconMatchedVariants.get(curVar);
//            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Nr of Matches:{0}", matches.size());
//
//            variantDM.deleteVariantCompletly(curVar);
//        }
        statusManager.createNewStatus(_dataset.getId(), DatasetStatusBuilder.DatasetStatusType.OBSERVED);
//        _dataset.setCompleted(true);
        dsDataManager.updateDataset(_dataset);
        //match handling, remove variants cause of identical matches?
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

    private String getMailTextFromVariant(VariantSQL variant) {
        String mailText = "Dear user,\n\nVarWatch has found an identical entry to one of your variants. For details, please follow this link:\n";
        String datasetId = variant.getDataset().getId().toString();
        String variantId = variant.getId().toString();
        String link = "https://varwatch.de/datasets/" + datasetId + "?details=[" + variantId + "]";
        String linkDoesNotWork = "\n\nIf the link does not work in your mail client, please copy/paste it into your browser.";
        String vwTeam = "\n\nYour VarWatch Team, www.varwatch.de";
        return mailText + link + linkDoesNotWork + vwTeam;
    }
}

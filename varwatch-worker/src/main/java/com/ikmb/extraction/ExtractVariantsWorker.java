/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.extraction;

import com.google.inject.Inject;
import com.ikmb.WorkFlowManager;
import com.ikmb.core.data.dataset.DatasetStatusBuilder;
import com.ikmb.core.data.dataset.DatasetStatusManager;
import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.data.variant.Variant;
import com.ikmb.core.data.variant.VariantStatusBuilder;
import com.ikmb.core.data.wipe.WipeDataManager;
import com.ikmb.core.varwatchcommons.entities.GenomicFeature;
import com.ikmb.core.varwatchcommons.entities.VWStatus;
import com.ikmb.core.varwatchcommons.entities.VWVariant;
import com.ikmb.core.varwatchcommons.utils.ParserHelper;
import com.ikmb.core.data.workflow.analysis.Analysis;
import com.ikmb.core.data.workflow.job.AnalysisJob;
import com.ikmb.core.data.workflow.job.JobManager;
import com.ikmb.core.data.workflow.worker.AnalysisWorker;
import com.ikmb.utils.VWConfiguration;
import com.ikmb.utils.WorkerInputHandler;
import com.ikmb.core.data.workflow.analysis.AnalysisBuilder;
import com.ikmb.varwatchworker.Worker;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Broder
 */
public class ExtractVariantsWorker implements Worker {

    protected AnalysisWorker _workerSQL;
    protected Analysis _analysisSQL;
    protected AnalysisJob _analysisJobSQL;

    private final Logger logger = LoggerFactory.getLogger(ExtractVariantsWorker.class);

    private DatasetVW _dataset;

    @Inject
    private WorkerInputHandler workerInputHandler;

    @Inject
    private UniformFormatConverter uniformFormatConverter;

    @Inject
    private ExtractionDataManager extractionDataManager;

    @Inject
    private AssemblyCrossMapper crossMapper;

    @Inject
    private VWCrossMapper vwCrossMapper;

    @Inject
    private VariantStatusBuilder variantStatusBuilder;

    @Inject
    private VariantIndelChecker variantChecker;

    @Inject
    private JobManager jobManager;

    @Inject
    private DatasetStatusManager statusManager;

    @Inject
    private WipeDataManager wipeDataManager;

    private WorkFlowManager.JobProcessStatus jobProcessStatus = WorkFlowManager.JobProcessStatus.FAILED;

    @Override
    public void runJob() {
        logger.info("-----Start extraction job-----");

        parseInput();
        logger.info("-----Id of the current dataset {}-----", _dataset.getId());
        statusManager.createNewStatus(_dataset.getId(), DatasetStatusBuilder.DatasetStatusType.PROCESSED);

        //extract the variants to an uniform format
        uniformFormatConverter.setDataset(_dataset);
        uniformFormatConverter.extractVariants();
        List<GenomicFeature> genomicFeatures = uniformFormatConverter.getGenomicFeatures();
        logger.info("{} variants extracted", genomicFeatures.size());
        if (genomicFeatures.size() > 100) {
            //delete ds if size > 100
            logger.info("variant size exceeded, dataset will be rejected");
            statusManager.createNewStatus(_dataset.getId(), DatasetStatusBuilder.DatasetStatusType.REFECTED_MAX_NUMBER);
            jobProcessStatus = WorkFlowManager.JobProcessStatus.SUCCESSFUL;
            return;
        }

        Map<String, VWStatus> preProcessErrorVariants = uniformFormatConverter.getPreProcessErrorVariants();
        logger.info("{} variants couldnt be extracted", preProcessErrorVariants.size());
        extractionDataManager.setRawVariantStatus(_dataset, preProcessErrorVariants);

        //crossmap the variants based on the given assembly
        List<GenomicFeature> mappedVariants = genomicFeatures;
        List<VWVariant> notMapableVariants = new ArrayList<>();
        if (!_dataset.getRawDataAssembly().equals(VWConfiguration.STANDARD_COORDS)) {
            vwCrossMapper.setGenomicFeatures(genomicFeatures, _dataset.getRawDataAssembly());
            vwCrossMapper.crossmap();
            mappedVariants = vwCrossMapper.getMappedFeatures();
            notMapableVariants = vwCrossMapper.getErrorVariants();
        }
        logger.info("{} variants mapped", mappedVariants.size());
        logger.info("{} variants couldnt be mapped", notMapableVariants.size());
        VWStatus status = variantStatusBuilder.withStatus(VariantStatusBuilder.VariantStatusTerm.REJECTED).withMessage(VariantStatusBuilder.VariantStatusMessage.ASSEMBLY_NOT_MAPABLE.getMessage()).buildVWStatus();
        extractionDataManager.setVariantStatus(_dataset, notMapableVariants, status);

        String vcfString = ParserHelper.json2vcf(mappedVariants);
        byte[] vcfFile = vcfString.getBytes();

        VCFParser vcfParser = new VCFParser();
        vcfParser.run(vcfFile);
        List<VWVariant> variants = vcfParser.getVariants();

        variantChecker.filterVariants(variants);
        Map<VWVariant, VWStatus> maxIndelExceededVariants = variantChecker.getErrorVariants();
        logger.info("{} variants with too large indels", maxIndelExceededVariants.size());
        List<VWVariant> correctVariants = variantChecker.getCorrectVariants();
        logger.info("{} correct variants", correctVariants.size());
        extractionDataManager.setVariantStatus(_dataset, maxIndelExceededVariants);
        List<Variant> persistVariants = extractionDataManager.persistVariants(correctVariants, _dataset);

//        VariantDatabaseHelper.persistVariants(_variants, _dataset);
//        DatasetVW dataset = VariantDatabaseHelper.getDatasetByID(_dataset.getId());
//        Set<Variant> variants = dataset.getVariants();
        String vcf = json2vcf(persistVariants);
        extractionDataManager.persistVCFFile(vcf.getBytes(), _dataset);

        jobManager.createJob(_dataset.getId(), AnalysisBuilder.ModuleName.ANNOTATION, null, AnalysisJob.JobAction.NEW.toString());
        jobProcessStatus = WorkFlowManager.JobProcessStatus.SUCCESSFUL;
    }

    //before rawDataType on of the three, _rawdata != null
    //after genomic features with content, variants with content, assembly is set
    private String json2vcf(List<Variant> variants) {
        StringBuilder vcfString = new StringBuilder();
        vcfString.append("#CHROM\tPOS\tID\tREF\tALT\tQUAL\tFILTER\tINFO\n");
        for (Variant variant : variants) {
            String id = ".";
            if (variant.getVEPIdentifier() != null) {
                id = variant.getVEPIdentifier();
            }
            vcfString.append(variant.getChromosomeName()).append("\t").append(variant.getChromosomePos()).append("\t").append(id).append("\t").append(variant.getReferenceBase()).append("\t").append(variant.getAlternateBase()).append("\t.\t.\t.\n");
        }
        return vcfString.toString();
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

        _dataset = extractionDataManager.getDatasetByID(datasetID);
    }

    @Override
    public void undo() {
        wipeDataManager.wipeVariantsFromDataset(_dataset.getId());
        statusManager.createNewStatus(_dataset.getId(), DatasetStatusBuilder.DatasetStatusType.REFECTED_DATA);
        //delete annotation job
    }

    @Override
    public WorkFlowManager.JobProcessStatus getJobProcessStatus() {
        return jobProcessStatus;
    }
}

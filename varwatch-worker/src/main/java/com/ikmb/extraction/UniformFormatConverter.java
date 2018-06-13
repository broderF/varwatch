/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.extraction;

import com.google.inject.Inject;
import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.data.ensembl.Ensembl;
import com.ikmb.core.varwatchcommons.entities.GenomicFeature;
import com.ikmb.core.varwatchcommons.entities.VWVariant;
import com.ikmb.core.varwatchcommons.entities.VWStatus;
import com.ikmb.core.utils.VariantParser;
import com.ikmb.core.data.ensembl.EnsemblDataManager;
import com.ikmb.core.data.variant.Variant;
import com.ikmb.core.data.variant.VariantStatusBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author broder
 */
public class UniformFormatConverter {

    @Inject
    private VariantStatusBuilder variantStatusBuilder;

    @Inject
    private HgvsConverter hgvsConverter;

    @Inject
    private EnsemblDataManager ensemblDataManager;

    private DatasetVW dataset;
    private List<GenomicFeature> genomicFeatures;
    private Map<String, VWStatus> errorVariants;

    public void setDataset(DatasetVW dataset) {
        this.dataset = dataset;
    }

    public void extractVariants() {
        genomicFeatures = new ArrayList<>();
        errorVariants = new HashMap<>();
        VariantFormat variantFormat = VariantFormat.getVariantFormat(dataset.getRawDataType());
        switch (variantFormat) {
            case NORMAL:
                genomicFeatures = VariantParser.getVariantInfoFromByteArray(dataset.getRawData());
                break;
            case VCF:
                VCFParser parser = new VCFParser();
                parser.run(dataset.getRawData());
                List<VWVariant> variants = parser.getVariants();
                for (VWVariant variant : variants) {
                    GenomicFeature genomicfeature = new GenomicFeature();
                    genomicfeature.setVariant(variant);
                    genomicFeatures.add(genomicfeature);
                }
                break;
            case HGVS:
                //with the hgvs string inside of the object
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Start hgvs");
                List<GenomicFeature> tmpfeatures = VariantParser.getVariantInfoFromByteArray(dataset.getRawData());

                Ensembl _predictorSQL = ensemblDataManager.getActiveEnsembl(false);
                hgvsConverter.setEnsemblVersion(_predictorSQL.getName());
                genomicFeatures = hgvsConverter.mapVariants(tmpfeatures, dataset.getRawDataAssembly());
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Nof genomic features:" + genomicFeatures.size());
                for (String variant : hgvsConverter.getErrorVariants()) {
                    errorVariants.put(variant, variantStatusBuilder.withStatus(VariantStatusBuilder.VariantStatusTerm.REJECTED).withMessage(VariantStatusBuilder.VariantStatusMessage.HGVS_NOT_CONVERTABLE.getMessage()).buildVWStatus());
                }
                break;
        }
    }

    public List<Variant> getVariants(DatasetVW dataset) {
        List<Variant> variants = new ArrayList<>();
        VariantFormat variantFormat = VariantFormat.getVariantFormat(dataset.getRawDataType());
        switch (variantFormat) {
            case NORMAL:
                genomicFeatures = VariantParser.getVariantInfoFromByteArray(dataset.getRawData());
                break;
            case VCF:
                VCFParser parser = new VCFParser();
                variants = parser.getVariants(dataset.getRawData());
                break;
            case HGVS:
                //with the hgvs string inside of the object
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Start hgvs");
                List<GenomicFeature> tmpfeatures = VariantParser.getVariantInfoFromByteArray(dataset.getRawData());

                Ensembl _predictorSQL = ensemblDataManager.getActiveEnsembl(false);
                hgvsConverter.setEnsemblVersion(_predictorSQL.getName());
                genomicFeatures = hgvsConverter.mapVariants(tmpfeatures, dataset.getRawDataAssembly());
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Nof genomic features:" + genomicFeatures.size());
                for (String variant : hgvsConverter.getErrorVariants()) {
                    errorVariants.put(variant, variantStatusBuilder.withStatus(VariantStatusBuilder.VariantStatusTerm.REJECTED).withMessage(VariantStatusBuilder.VariantStatusMessage.HGVS_NOT_CONVERTABLE.getMessage()).buildVWStatus());
                }
                break;
        }
        return variants;
    }

    public List<GenomicFeature> getGenomicFeatures() {
        return genomicFeatures;
    }

    public Map<String, VWStatus> getPreProcessErrorVariants() {
        return errorVariants;

    }

    public enum VariantFormat {

        NORMAL, HGVS, VCF;

        public static VariantFormat getVariantFormat(String value) {
            for (VariantFormat v : values()) {
                if (v.toString().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }
}

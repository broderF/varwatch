/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.extraction;

import com.ikmb.core.varwatchcommons.utils.VariantHash;
import com.google.inject.Inject;
import com.ikmb.core.varwatchcommons.entities.VWStatus;
import com.ikmb.core.varwatchcommons.entities.VWVariant;
import com.ikmb.core.data.dataset.DatasetManager;
import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.data.variant.VariantDataManager;
import com.ikmb.core.data.hpo.HPOTermBuilder;
import com.ikmb.core.data.variant.Variant;
import com.ikmb.core.data.variant.VariantStatusManager;
import java.util.List;
import java.util.Map;

/**
 *
 * @author broder
 */
public class ExtractionDataManager {

    @Inject
    private VariantStatusManager variantStatusManager;

    @Inject
    private VariantDataManager variantDataManager;

    @Inject
    private DatasetManager datasetManager;

    @Inject
    private VariantHash variantHasher;

    @Inject
    private HPOTermBuilder hpoTermBuilder;

    public void setRawVariantStatus(DatasetVW dataset, Map<String, VWStatus> preProcessErrorVariants) {
        for (Map.Entry<String, VWStatus> variantStatus : preProcessErrorVariants.entrySet()) {
            String variant = variantStatus.getKey();
            VWStatus status = variantStatus.getValue();

//            String varString = variant.toCompactString();
            variantStatusManager.save(dataset, variant, status);
        }
    }

    public void setVariantStatus(DatasetVW dataset, List<VWVariant> notMapableVariants, VWStatus status) {
        for (VWVariant variant : notMapableVariants) {
//            String varString = variant.toCompactString();
            String variantHash = variantHasher.getVariantHash(variant, hpoTermBuilder.addFeatures(dataset.getPhenotypes()).buildStringSet());
            variantStatusManager.save(dataset, variant.toCompactString(), variantHash, status);
        }
    }

    public void setVariantStatus(DatasetVW dataset, Map<VWVariant, VWStatus> errorVariants) {
        for (Map.Entry<VWVariant, VWStatus> variantStatus : errorVariants.entrySet()) {
            VWVariant variant = variantStatus.getKey();
            VWStatus status = variantStatus.getValue();

            String variantHash = variantHasher.getVariantHash(variant, hpoTermBuilder.addFeatures(dataset.getPhenotypes()).buildStringSet());
            variantStatusManager.save(dataset, variant.toCompactString(), variantHash, status);
        }
    }

    public DatasetVW getDatasetByID(Long datasetID) {
        return datasetManager.getDatasetWithVariantsByID(datasetID);
    }

    public List<Variant> persistVariants(List<VWVariant> variants, DatasetVW dataset) {

        return variantDataManager.save(variants, dataset);
    }

    public void persistVCFFile(byte[] bytes, DatasetVW dataset) {
        dataset.setVcfFile(bytes);
        datasetManager.updateDataset(dataset);
    }
}

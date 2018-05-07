/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.extraction;

import com.ikmb.varwatchcommons.utils.VariantHash;
import com.google.inject.Inject;
import com.ikmb.varwatchcommons.entities.VWStatus;
import com.ikmb.varwatchcommons.entities.VWVariant;
import com.ikmb.varwatchsql.entities.DatasetVWSQL;
import com.ikmb.varwatchsql.variant_data.variant.VariantSQL;
import com.ikmb.varwatchsql.variant_data.dataset.DatasetManager;
import com.ikmb.varwatchsql.variant_data.variant.VariantDataManager;
import com.ikmb.varwatchsql.data.hpo.HPOTermBuilder;
import com.ikmb.varwatchsql.status.variant.VariantStatusManager;
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

    public void setRawVariantStatus(DatasetVWSQL dataset, Map<String, VWStatus> preProcessErrorVariants) {
        for (Map.Entry<String, VWStatus> variantStatus : preProcessErrorVariants.entrySet()) {
            String variant = variantStatus.getKey();
            VWStatus status = variantStatus.getValue();

//            String varString = variant.toCompactString();
            variantStatusManager.save(dataset, variant, status);
        }
    }

    public void setVariantStatus(DatasetVWSQL dataset, List<VWVariant> notMapableVariants, VWStatus status) {
        for (VWVariant variant : notMapableVariants) {
//            String varString = variant.toCompactString();
            String variantHash = variantHasher.getVariantHash(variant, hpoTermBuilder.addFeatures(dataset.getPhenotypes()).buildStringSet());
            variantStatusManager.save(dataset, variant.toCompactString(), variantHash, status);
        }
    }

    public void setVariantStatus(DatasetVWSQL dataset, Map<VWVariant, VWStatus> errorVariants) {
        for (Map.Entry<VWVariant, VWStatus> variantStatus : errorVariants.entrySet()) {
            VWVariant variant = variantStatus.getKey();
            VWStatus status = variantStatus.getValue();

            String variantHash = variantHasher.getVariantHash(variant, hpoTermBuilder.addFeatures(dataset.getPhenotypes()).buildStringSet());
            variantStatusManager.save(dataset, variant.toCompactString(), variantHash, status);
        }
    }

    public DatasetVWSQL getDatasetByID(Long datasetID) {
        return datasetManager.getDatasetWithVariantsByID(datasetID);
    }

    public List<VariantSQL> persistVariants(List<VWVariant> variants, DatasetVWSQL dataset) {

        return variantDataManager.save(variants, dataset);
    }

    public void persistVCFFile(byte[] bytes, DatasetVWSQL dataset) {
        dataset.setVcfFile(bytes);
        datasetManager.updateDataset(dataset);
    }
}

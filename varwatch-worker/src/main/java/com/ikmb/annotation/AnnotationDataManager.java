/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.annotation;

import com.google.inject.Inject;
import com.ikmb.varwatchsql.entities.DatasetVWSQL;
import com.ikmb.varwatchsql.variant_data.dataset.DatasetManager;

/**
 *
 * @author broder
 */
public class AnnotationDataManager {

    @Inject
    private DatasetManager datasetManager;

    public DatasetVWSQL getDatasetByID(Long datasetID) {
        return datasetManager.getDatasetWithVariantsByID(datasetID);
    }
}

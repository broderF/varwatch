/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.hpo;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import java.util.List;

/**
 *
 * @author broder
 */
public class PhenotypeDataManager {

    @Inject
    private PhenotypeDao phenotypeDao;
    @Inject
    private HPOTermBuilder hpoTermBuilder;

    @Transactional
    public HPOTerm getHPOTermByName(String hpoName) {
        HPOTerm hpoTermByName = phenotypeDao.getHPOTermByName(hpoName, "C");
        return hpoTermByName;
    }

    @Transactional
    public List<HPOTerm> getPhenotypes(Long datasetId) {
//        DatasetVWSQL dataset = datasetDao.getDataset(datasetId);
//        Set<PhenotypeSQL> hpoTermsSql = dataset.getPhenotypes();
//        List<com.ikmb.varwatchcommons.entities.HPOTerm> hpoTerms = hpoTermBuilder.addFeatures(hpoTermsSql).buildList();
        return null;
    }
}
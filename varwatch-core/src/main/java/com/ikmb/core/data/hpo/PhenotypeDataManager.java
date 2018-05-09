/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.hpo;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.ikmb.core.data.dataset.DatasetDao;
import com.ikmb.core.data.dataset.DatasetVW;
import java.util.List;
import java.util.Set;

/**
 *
 * @author broder
 */
public class PhenotypeDataManager {

    @Inject
    private PhenotypeDao phenotypeDao;
    @Inject
    private HPOTermBuilder hpoTermBuilder;
    @Inject
    private DatasetDao datasetDao;

    @Transactional
    public HPOTerm getHPOTermByName(String hpoName) {
        HPOTerm hpoTermByName = phenotypeDao.getHPOTermByName(hpoName, "C");
        return hpoTermByName;
    }

    @Transactional
    public List<HPOTerm> getPhenotypes(Long datasetId) {
        DatasetVW dataset = datasetDao.getDataset(datasetId);
        Set<Phenotype> hpoTermsSql = dataset.getPhenotypes();
        List<HPOTerm> hpoTerms = hpoTermBuilder.addFeatures(hpoTermsSql).buildList();
        return hpoTerms;
    }
}

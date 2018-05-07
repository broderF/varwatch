/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.data.hpo;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.ikmb.varwatchcommons.entities.HPOTerm;
import com.ikmb.varwatchsql.entities.DatasetVWSQL;
import com.ikmb.varwatchsql.variant_data.dataset.DatasetDao;
import java.util.List;
import java.util.Set;
import javax.persistence.PersistenceException;

/**
 *
 * @author broder
 */
public class PhenotypeDataManager {

    @Inject
    private PhenotypeDao phenotypeDao;

    @Inject
    private DatasetDao datasetDao;

    @Inject
    private HPOTermBuilder hpoTermBuilder;

    @Transactional
    public List<HPOTerm> getPhenotypes(Long datasetId) {
        DatasetVWSQL dataset = datasetDao.getDataset(datasetId);
        Set<PhenotypeSQL> hpoTermsSql = dataset.getPhenotypes();
        List<HPOTerm> hpoTerms = hpoTermBuilder.addFeatures(hpoTermsSql).buildList();
        return hpoTerms;
    }
}

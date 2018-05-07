/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.data.ensembl;

import com.google.inject.Inject;
import com.ikmb.varwatchsql.entities.EnsemblSQL;

/**
 *
 * @author broder
 */
public class EnsemblDataManager {
    
    @Inject
    private EnsemblDao ensemblDao;

    public EnsemblSQL getActiveEnsembl(Boolean needFile) {
        return ensemblDao.getActiveEnsembl(needFile);
    }
}

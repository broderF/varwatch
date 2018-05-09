/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.workflow;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.ikmb.core.workflow.analysis.Analysis;
import com.ikmb.core.workflow.analysis.AnalysisDao;
import com.ikmb.varwatchsql.workflow.analysis.AnalysisBuilder.ModuleName;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author broder
 */
public class AnalysisDaoSQL implements AnalysisDao{

    @Inject
//    private EntityManager em;
    private Provider<EntityManager> emProvider;

    public Analysis getAnalysisByModuleName(ModuleName moduleName) {
        TypedQuery<Analysis> query = emProvider.get().createQuery("SELECT s FROM Analysis s WHERE s.moduleName = :module", Analysis.class);
        Analysis analysisSQL = query.setParameter("module", moduleName.toString()).getSingleResult();
        return analysisSQL;
    }

}

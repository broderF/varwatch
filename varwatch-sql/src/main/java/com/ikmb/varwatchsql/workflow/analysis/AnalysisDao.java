/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.workflow.analysis;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.ikmb.varwatchsql.entities.AnalysisSQL;
import com.ikmb.varwatchsql.workflow.analysis.AnalysisBuilder.ModuleName;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author broder
 */
public class AnalysisDao {

    @Inject
//    private EntityManager em;
    private Provider<EntityManager> emProvider;

    public AnalysisSQL getAnalysisByModuleName(ModuleName moduleName) {
        TypedQuery<AnalysisSQL> query = emProvider.get().createQuery("SELECT s FROM AnalysisSQL s WHERE s.moduleName = :module", AnalysisSQL.class);
        AnalysisSQL analysisSQL = query.setParameter("module", moduleName.toString()).getSingleResult();
        return analysisSQL;
    }

}

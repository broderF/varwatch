/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.workflow.analysis;

import com.ikmb.varwatchsql.workflow.analysis.AnalysisBuilder.ModuleName;

/**
 *
 * @author broder
 */
public interface AnalysisDao {

    public Analysis getAnalysisByModuleName(ModuleName moduleName);

}

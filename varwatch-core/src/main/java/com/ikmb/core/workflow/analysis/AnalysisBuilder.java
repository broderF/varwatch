/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.workflow.analysis;

/**
 *
 * @author broder
 */
public class AnalysisBuilder {

    public enum ModuleName {

        EXTRACT_VARIANTS,
        ANNOTATION,
        SCREENING,
        SCREEN_RESULT_COLLECT,
        SCREEN_BEACON_RESULT_COLLECT,
        SCREENING_BEACON,
        SCREENING_VARWATCH,
        SCREENING_HGMD,
        REPORT,
        SANITY_CHECK
        ;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }
}

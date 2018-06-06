/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.workflow.analysis;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author Broder
 */
@Entity
@Table(name = "analysis")
public class Analysis implements Serializable {

    @Transient
    public final static String ANALYSIS_EXTRACTION = "extract_variants";
    @Transient
    public final static String ANALYSIS_ANNOTATION = "annotation";
    @Transient
    public final static String ANALYSIS_SCREENING = "screening";
    @Transient
    public final static String ANALYSIS_SCREENING_BEACON = "screening_beacon";
    @Transient
    public final static String ANALYSIS_SCREENING_HGMD = "screening_hgmd";
    @Transient
    public final static String ANALYSIS_SCREENING_VARWATCH = "screening_varwatch";
    @Transient
    public final static String ANALYSIS_COLLECTING_BEACON = "screen_beacon_result_collect";
    @Transient
    public final static String ANALYSIS_COLLECTING = "screen_result_collect";
    @Transient
    public final static String ANALYSIS_NOTIFICATION = "notification";
    @Transient
    public final static String ANALYSIS_REPORT = "report";
    @Transient
    public final static String ANALYSIS_SANITY_CHECK = "sanity_check";
    @Transient
    public final static String ANALYSIS_HPO_UPDATE = "hpo_update";

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "module_name")
    private String moduleName;
    @Column(name = "additional_parameters")
    private String parameters;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModule() {
        return moduleName;
    }

    public void setModule(String module) {
        this.moduleName = module;
    }

    public String getAdditionalParameters() {
        return parameters;
    }

    public void setAdditionalParameters(String parameters) {
        this.parameters = parameters;
    }

}

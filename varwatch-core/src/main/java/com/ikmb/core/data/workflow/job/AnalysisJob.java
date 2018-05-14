/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.workflow.job;

import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.data.workflow.analysis.Analysis;
import com.ikmb.core.data.workflow.worker.AnalysisWorker;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 *
 * @author Broder
 */
@Entity
@Table(name = "analysis_job")
public class AnalysisJob implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @Version
    private long version;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "analysis_id", nullable = false)
    private Analysis analysis;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "worker_id", nullable = true)
    private AnalysisWorker worker;
    @Column(name = "status")
    private String status;
    @Column(name = "retry_count")
    private Integer retryCount;
    @Column(name = "runtime")
    private Long runtime;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dataset_id", nullable = true)
    private DatasetVW dataset;
    @Column(name = "additional_parameters")
    private String additionalParameters;
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "submitted")
    private DateTime submitted;
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "last_try")
    private DateTime lastTry;
    @Column(name = "action")
    private String action;
    @Column(name = "process")
    private String process;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public Analysis getAnalysis() {
        return analysis;
    }

    public void setAnalysis(Analysis analysis) {
        this.analysis = analysis;
    }

    public AnalysisWorker getWorker() {
        return worker;
    }

    public void setWorker(AnalysisWorker worker) {
        this.worker = worker;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Long getRuntime() {
        return runtime;
    }

    public void setRuntime(Long runtime) {
        this.runtime = runtime;
    }

    public DatasetVW getDataset() {
        return dataset;
    }

    public void setDataset(DatasetVW dataset) {
        this.dataset = dataset;
    }

    public String getAdditionalParameters() {
        return additionalParameters;
    }

    public void setAdditionalParameters(String inputParameters) {
        this.additionalParameters = inputParameters;
    }

    public DateTime getSubmitted() {
        return submitted;
    }

    public void setSubmitted(DateTime submitted) {
        this.submitted = submitted;
    }

    public DateTime getLastCheckIn() {
        return lastTry;
    }

    public void setLastCheckIn(DateTime lastCheckIn) {
        this.lastTry = lastCheckIn;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public enum JobAction{
        NEW,UPDATE,REDO,UNDO;
    }
    
    public enum JobProcess{
        SINGLE,WORKFLOW;
    }
    
}

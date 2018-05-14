/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.workflow.worker;

import com.ikmb.core.data.workflow.job.AnalysisJob;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 *
 * @author Broder
 */
@Entity
@Table(name = "analysis_worker")
public class AnalysisWorker implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "status")
    private String status;
    @Column(name = "work_done")
    private Integer workDone;
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "last_check_in")
    private DateTime lastCheckIn;
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "born")
    private DateTime born;
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "died")
    private DateTime died;
    @Column(name = "cause_of_death")
    private String causeOfDeath;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "worker")
    private Set<AnalysisJob> workerJobs = new HashSet<AnalysisJob>();

    public Long getId() {
        return id;
    }

    public void Long(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getWorkDone() {
        return workDone;
    }

    public void setWorkDone(Integer workDone) {
        this.workDone = workDone;
    }

    public DateTime getLastCheckIn() {
        return lastCheckIn;
    }

    public void setLastCheckIn(DateTime lastCheckIn) {
        this.lastCheckIn = lastCheckIn;
    }

    public DateTime getBorn() {
        return born;
    }

    public void setBorn(DateTime born) {
        this.born = born;
    }

    public DateTime getDied() {
        return died;
    }

    public void setDied(DateTime died) {
        this.died = died;
    }

    public String getCauseOfDeath() {
        return causeOfDeath;
    }

    public void setCauseOfDeath(String causeOfDeath) {
        this.causeOfDeath = causeOfDeath;
    }

    public Set<AnalysisJob> getWorkerJobs() {
        return workerJobs;
    }

    public void setWorkerJobs(Set<AnalysisJob> workerJobs) {
        this.workerJobs = workerJobs;
    }
}

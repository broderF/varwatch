/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.data.hpo;

import com.ikmb.varwatchsql.entities.DatasetSQL;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Broder
 */
@Entity
@Table(name = "dataset2hpo")
public class PhenotypeSQL implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dataset_id", nullable = false)
    private DatasetSQL dataset;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "phenotype_id", nullable = false)
    private HPOTermSQL phenotype;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "age_of_onset_id", nullable = true)
    private HPOTermSQL ageOfOnset;
    @Column(name = "observed")
    private Boolean observed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DatasetSQL getDataset() {
        return dataset;
    }

    public void setDataset(DatasetSQL dataset) {
        this.dataset = dataset;
    }

    public HPOTermSQL getPhenotype() {
        return phenotype;
    }

    public void setPhenotype(HPOTermSQL phenotype) {
        this.phenotype = phenotype;
    }

    public HPOTermSQL getAgeOfOnset() {
        return ageOfOnset;
    }

    public void setAgeOfOnset(HPOTermSQL ageOfOnset) {
        this.ageOfOnset = ageOfOnset;
    }

    public Boolean getObserved() {
        return observed;
    }

    public void setObserved(Boolean observed) {
        this.observed = observed;
    }

}

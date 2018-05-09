/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.hpo;

import com.ikmb.core.data.dataset.Dataset;
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
public class Phenotype implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dataset_id", nullable = false)
    private Dataset dataset;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "phenotype_id", nullable = false)
    private HPOTerm phenotype;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "age_of_onset_id", nullable = true)
    private HPOTerm ageOfOnset;
    @Column(name = "observed")
    private Boolean observed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Dataset getDataset() {
        return dataset;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    public HPOTerm getPhenotype() {
        return phenotype;
    }

    public void setPhenotype(HPOTerm phenotype) {
        this.phenotype = phenotype;
    }

    public HPOTerm getAgeOfOnset() {
        return ageOfOnset;
    }

    public void setAgeOfOnset(HPOTerm ageOfOnset) {
        this.ageOfOnset = ageOfOnset;
    }

    public Boolean getObserved() {
        return observed;
    }

    public void setObserved(Boolean observed) {
        this.observed = observed;
    }

}

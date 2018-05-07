/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.entities;

import com.ikmb.varwatchsql.data.hpo.HPOTermSQL;
import com.ikmb.varwatchsql.data.hpo.PhenotypeSQL;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author bfredrich
 */
//@MappedSuperclass
//@Table(name = "dataset")
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "dataset")
public abstract class DatasetSQL implements Serializable {

    @Id
    @GeneratedValue
    protected Long id;
//    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinTable(name = "dataset2hpo", joinColumns = {
//        @JoinColumn(name = "dataset_id", nullable = false, updatable = false)},
//            inverseJoinColumns = {
//                @JoinColumn(name = "hpo_id",
//                        nullable = false, updatable = false)})
//    protected Set<HPOTermSQL> phenotypes = new HashSet<HPOTermSQL>();
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "dataset")
    private Set<PhenotypeSQL> phenotypes = new HashSet<PhenotypeSQL>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public Set<HPOTermSQL> getPhenotypes() {
//        return phenotypes;
//    }
//
//    public void setPhenotypes(Set<HPOTermSQL> phenotypes) {
//        this.phenotypes = phenotypes;
//    }

    public Set<PhenotypeSQL> getPhenotypes() {
        return phenotypes;
    }

    public void setPhenotypes(Set<PhenotypeSQL> phenotypes) {
        this.phenotypes = phenotypes;
    }
}

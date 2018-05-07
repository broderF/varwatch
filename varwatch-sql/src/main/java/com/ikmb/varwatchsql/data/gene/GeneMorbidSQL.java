/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.data.gene;

import com.ikmb.varwatchsql.entities.EnsemblSQL;
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
 * @author bfredrich
 */
@Entity
@Table(name = "gene_morbid")
public class GeneMorbidSQL implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "omim_morbid")
    private String omimMorbid;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gene_id")
    private GeneSQL gene;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOmimMorbid() {
        return omimMorbid;
    }

    public void setOmimMorbid(String omimMorbid) {
        this.omimMorbid = omimMorbid;
    }

    public GeneSQL getGene() {
        return gene;
    }

    public void setGene(GeneSQL gene) {
        this.gene = gene;
    }

}

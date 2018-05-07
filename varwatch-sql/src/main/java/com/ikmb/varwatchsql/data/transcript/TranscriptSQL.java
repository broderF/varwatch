/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.data.transcript;

import com.ikmb.varwatchsql.data.gene.GeneSQL;
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
@Table(name = "transcript")
public class TranscriptSQL implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "bio_type")
    private String bioType;
    @Column(name = "protein")
    private String protein;
    @Column(name = "canonical")
    private Boolean canonical;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gene_id")
    private GeneSQL gene;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBioType() {
        return bioType;
    }

    public void setBioType(String featureType) {
        this.bioType = featureType;
    }

    public Boolean getCanonical() {
        return canonical;
    }

    public void setCanonical(Boolean canonical) {
        this.canonical = canonical;
    }

    public GeneSQL getGene() {
        return gene;
    }

    public void setGene(GeneSQL gene) {
        this.gene = gene;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

}

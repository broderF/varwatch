/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.data.hpo;

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
@Table(name = "hpo_alternative")
public class HPOAlternativeTermSQL implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "alternative_identifier")
    private String identifier;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_id")
    private HPOTermSQL hpo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public HPOTermSQL getHpo() {
        return hpo;
    }

    public void setHpo(HPOTermSQL hpo) {
        this.hpo = hpo;
    }

}

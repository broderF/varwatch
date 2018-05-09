/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.hpo;

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
public class HPOAlternativeTerm implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "alternative_identifier")
    private String identifier;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_id")
    private HPOTerm hpo;

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

    public HPOTerm getHpo() {
        return hpo;
    }

    public void setHpo(HPOTerm hpo) {
        this.hpo = hpo;
    }

}
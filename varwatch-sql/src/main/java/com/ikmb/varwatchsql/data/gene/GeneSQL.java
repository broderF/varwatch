/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.data.gene;

import com.ikmb.varwatchsql.entities.EnsemblSQL;
import com.ikmb.varwatchsql.entities.FamilySQL;
import com.ikmb.varwatchsql.entities.PathwaySQL;
import com.ikmb.varwatchsql.data.transcript.TranscriptSQL;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author bfredrich
 */
@Entity
@Table(name = "gene")
public class GeneSQL implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "symbol")
    private String symbol;
    @Column(name = "symbol_source")
    private String symbolSource;
    @Column(name = "accession_id")
    private String accessionID;
    @Column(name = "omim_gene")
    private String omim_gene;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ensembl_id", nullable = false)
    private EnsemblSQL ensemblVersion;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "gene2pathway", joinColumns = {
        @JoinColumn(name = "gene_id", nullable = false, updatable = false)},
            inverseJoinColumns = {
                @JoinColumn(name = "pathway_id",
                        nullable = false, updatable = false)})
    private Set<PathwaySQL> pathways = new HashSet<PathwaySQL>();
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "gene2family", joinColumns = {
        @JoinColumn(name = "gene_id", nullable = false, updatable = false)},
            inverseJoinColumns = {
                @JoinColumn(name = "family_id",
                        nullable = false, updatable = false)})
    private Set<FamilySQL> families = new HashSet<FamilySQL>();
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "gene")
    private Set<TranscriptSQL> transcripts = new HashSet<TranscriptSQL>();
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "gene")
    private Set<GeneMorbidSQL> morbids = new HashSet<GeneMorbidSQL>();

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

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbolSource() {
        return symbolSource;
    }

    public void setSymbolSource(String symbolSource) {
        this.symbolSource = symbolSource;
    }

    public String getOmimGene() {
        return omim_gene;
    }

    public void setOmimGene(String omim_gene) {
        this.omim_gene = omim_gene;
    }

    public String getAccessionID() {
        return accessionID;
    }

    public void setAccessionID(String hgnc) {
        this.accessionID = hgnc;
    }

    public EnsemblSQL getEnsemblVersion() {
        return ensemblVersion;
    }

    public void setEnsemblVersion(EnsemblSQL ensemblVersion) {
        this.ensemblVersion = ensemblVersion;
    }

    public Set<PathwaySQL> getPathways() {
        return pathways;
    }

    public void setPathways(Set<PathwaySQL> pathways) {
        this.pathways = pathways;
    }

    public Set<FamilySQL> getFamilies() {
        return families;
    }

    public void setFamilies(Set<FamilySQL> families) {
        this.families = families;
    }

    public Set<TranscriptSQL> getTranscripts() {
        return transcripts;
    }

    public void setTranscripts(Set<TranscriptSQL> transcripts) {
        this.transcripts = transcripts;
    }

    public Set<GeneMorbidSQL> getMorbids() {
        return morbids;
    }

    public void setMorbids(Set<GeneMorbidSQL> morbids) {
        this.morbids = morbids;
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.matching;

import com.ikmb.varwatchsql.data.reference_db.RefDatabaseSQL;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 *
 * @author bfredrich
 */
@Entity
@Table(name = "match_pair")
public class MatchSQL implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "identical")
    private Boolean identical;
    @Column(name = "hpo_dist")
    private Double hpoDist;
    @Column(name = "equal_gene")
    private Boolean equalGene;
    @Column(name = "equal_path")
    private Boolean equalPath;
    @Column(name = "equal_fam")
    private Boolean equalFam;
    @Column(name = "notified")
    private Boolean notified;
    @Column(name = "match_type")
    private String match_type;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "database_id", nullable = false)
    private RefDatabaseSQL database;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "match")
    private Set<MatchVariantSQL> variants = new HashSet<MatchVariantSQL>();
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "creation_timestamp")
    private DateTime creationTimestamp;
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "deletion_timestamp")
    private DateTime deletionTimestamp;
//    @OneToOne(fetch = FetchType.LAZY, mappedBy = "match")
//    private VariantStatusSQL variantStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatch_type() {
        return match_type;
    }

    public void setMatch_type(String match_type) {
        this.match_type = match_type;
    }

    public Boolean getNotified() {
        return notified;
    }

    public void setNotified(Boolean notified) {
        this.notified = notified;
    }

    public Boolean getIdentical() {
        return identical;
    }

    public RefDatabaseSQL getDatabase() {
        return database;
    }

    public void setDatabase(RefDatabaseSQL database) {
        this.database = database;
    }

    public void setIdentical(Boolean identical) {
        this.identical = identical;
    }

    public Double getHpoDist() {
        return hpoDist;
    }

    public void setHpoDist(Double hpoDist) {
        this.hpoDist = hpoDist;
    }

    public Boolean getEqualGene() {
        return equalGene;
    }

    public void setEqualGene(Boolean equalGene) {
        this.equalGene = equalGene;
    }

    public Boolean getEqualPath() {
        return equalPath;
    }

    public void setEqualPath(Boolean equalPath) {
        this.equalPath = equalPath;
    }

    public Boolean getEqualFam() {
        return equalFam;
    }

    public void setEqualFam(Boolean equalFam) {
        this.equalFam = equalFam;
    }

    public Set<MatchVariantSQL> getVariants() {
        return variants;
    }

    public void setVariants(Set<MatchVariantSQL> variants) {
        this.variants = variants;
    }

    public DateTime getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(DateTime creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public DateTime getDeletionTimestamp() {
        return deletionTimestamp;
    }

    public void setDeletionTimestamp(DateTime deletionTimestamp) {
        this.deletionTimestamp = deletionTimestamp;
    }

//    public VariantStatusSQL getVariantStatus() {
//        return variantStatus;
//    }
//
//    public void setVariantStatus(VariantStatusSQL variantStatus) {
//        this.variantStatus = variantStatus;
//    }
}

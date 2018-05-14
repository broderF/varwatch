/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.variant;

import com.ikmb.core.data.auth.user.User;
import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.data.matching.MatchVariant;
import com.ikmb.core.data.variant.Variant;
import com.ikmb.core.data.variant.VariantStatus;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 *
 * @author bfredrich
 */
@Entity
@Table(name = "variant_status")
public class VariantStatus implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "variant_hash")
    private String variantHash;
    @Column(name = "variant_raw")
    private String variantRaw;
    @Column(name = "status")
    private String status;
    @Column(name = "status_value")
    private String statusValue;
    @Column(name = "timestamp")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timestamp;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "variant_id")
    private Variant variant;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dataset_id", nullable = false)
    private DatasetVW dataset_vw;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "match_variant_id", nullable = true)
    private MatchVariant matchvariant;
    
//    private MatchVariant matchvariant;

//    public Long getTestId() {
//        return testId;
//    }
//
//    public void setTestId(Long testId) {
//        this.testId = testId;
//    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVariantHash() {
        return variantHash;
    }

    public void setVariantHash(String variantHash) {
        this.variantHash = variantHash;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Variant getVariant() {
        return variant;
    }

    public void setVariant(Variant variant) {
        this.variant = variant;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatusValue() {
        return statusValue;
    }

    public void setStatusValue(String statusValue) {
        this.statusValue = statusValue;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }

    public String getVariantRaw() {
        return variantRaw;
    }

    public void setVariantRaw(String variantRaw) {
        this.variantRaw = variantRaw;
    }

    public void setTimestamp(DateTime timestamp) {
        this.timestamp = timestamp;
    }

    public MatchVariant getMatchVariant() {
        return matchvariant;
//        return null;
    }

    public void setMatchVariant(MatchVariant matchvariant) {
        this.matchvariant = matchvariant;
    }

//    public MatchSQL getMatch() {
//        return match;
////        return null;
//    }
//
//    public void setMatch(MatchSQL match) {
//        this.match = match;
//    }

    public DatasetVW getDataset() {
        return dataset_vw;
    }

    public void setDataset(DatasetVW dataset) {
        this.dataset_vw = dataset;
    }
}

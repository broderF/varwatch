/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.matching;

import com.ikmb.core.data.auth.user.User;
import com.ikmb.core.data.reference_db.RefDatabase;
import com.ikmb.core.data.variant.VariantStatus;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 *
 * @author bfredrich
 */
@Entity
@Table(name = "match2variant")
public class MatchVariant implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "database_id", nullable = false)
    private RefDatabase database;
    @Column(name = "variant_id", nullable = true)
    private Long variantId;
//    @Column(name = "variant_id", nullable = true)
//    private VariantSQL variant;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;
    @Column(name = "acknowledged")
    private Boolean acknowledged;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;
    @Column(name = "timestamp")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timestamp = new DateTime();
    @OneToOne(fetch = FetchType.EAGER, mappedBy = "matchvariant")
    private VariantStatus variantStatus;
//        @OneToMany(fetch = FetchType.LAZY, mappedBy = "matchvariant")
//    private Set<VariantStatus> variantStatus = new HashSet<VariantStatus>();

    public DateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(DateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Long getVariantId() {
        return variantId;
    }

    public void setVariantId(Long variantId) {
        this.variantId = variantId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RefDatabase getDatabase() {
        return database;
    }

    public void setDatabase(RefDatabase database) {
        this.database = database;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getNotified() {
        return acknowledged;
    }

    public void setNotified(Boolean notified) {
        this.acknowledged = notified;
    }

    public VariantStatus getVariantStatus() {
        return variantStatus;
//        return null;
    }

    public void setVariantStatus(VariantStatus variantStatus) {
        this.variantStatus = variantStatus;
    }

}

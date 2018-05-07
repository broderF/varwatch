/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.matching;

import com.ikmb.varwatchsql.auth.user.UserSQL;
import com.ikmb.varwatchsql.entities.VariantStatusSQL;
import com.ikmb.varwatchsql.data.reference_db.RefDatabaseSQL;
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
public class MatchVariantSQL implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "database_id", nullable = false)
    private RefDatabaseSQL database;
    @Column(name = "variant_id", nullable = true)
    private Long variantId;
//    @Column(name = "variant_id", nullable = true)
//    private VariantSQL variant;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "match_id", nullable = false)
    private MatchSQL match;
    @Column(name = "acknowledged")
    private Boolean acknowledged;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = true)
    private UserSQL user;
    @Column(name = "timestamp")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timestamp = new DateTime();
    @OneToOne(fetch = FetchType.EAGER, mappedBy = "matchvariant")
    private VariantStatusSQL variantStatus;
//        @OneToMany(fetch = FetchType.LAZY, mappedBy = "matchvariant")
//    private Set<VariantStatusSQL> variantStatus = new HashSet<VariantStatusSQL>();

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

    public RefDatabaseSQL getDatabase() {
        return database;
    }

    public void setDatabase(RefDatabaseSQL database) {
        this.database = database;
    }

    public MatchSQL getMatch() {
        return match;
    }

    public void setMatch(MatchSQL match) {
        this.match = match;
    }

    public UserSQL getUser() {
        return user;
    }

    public void setUser(UserSQL user) {
        this.user = user;
    }

    public Boolean getNotified() {
        return acknowledged;
    }

    public void setNotified(Boolean notified) {
        this.acknowledged = notified;
    }

    public VariantStatusSQL getVariantStatus() {
        return variantStatus;
//        return null;
    }

    public void setVariantStatus(VariantStatusSQL variantStatus) {
        this.variantStatus = variantStatus;
    }
}

///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.ikmb.varwatchsql.entities;
//
//import com.ikmb.varwatchsql.int_data.reference_db.RefDatabaseSQL;
//import java.io.Serializable;
//import java.util.HashSet;
//import java.util.Set;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.OneToMany;
//import javax.persistence.Table;
//import org.hibernate.annotations.Type;
//import org.joda.time.DateTime;
//
///**
// *
// * @author bfredrich
// */
//@Entity
//@Table(name = "var_match_grp")
//public class MatchGroupSQL implements Serializable {
//
//    @Id
//    @GeneratedValue
//    private Long id;
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "database_id", nullable = false)
//    private RefDatabaseSQL database;
//    @Column(name = "notified")
//    private Boolean notified;
//    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
//    @Column(name = "timestamp")
//    private DateTime timestamp;
//    @OneToMany(fetch = FetchType.EAGER, mappedBy = "matchGroup")
//    private Set<MatchSQL> matches = new HashSet<MatchSQL>();
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public RefDatabaseSQL getDatabase() {
//        return database;
//    }
//
//    public void setDatabase(RefDatabaseSQL database) {
//        this.database = database;
//    }
//
//    public Boolean getNotified() {
//        return notified;
//    }
//
//    public void setNotified(Boolean notified) {
//        this.notified = notified;
//    }
//
//    public DateTime getTimestamp() {
//        return timestamp;
//    }
//
//    public void setTimestamp(DateTime timestamp) {
//        this.timestamp = timestamp;
//    }
//
//    public Set<MatchSQL> getMatches() {
//        return matches;
//    }
//
//    public void setMatches(Set<MatchSQL> matches) {
//        this.matches = matches;
//    }
//
//    public void addMatches(MatchSQL match) {
//        this.matches.add(match);
//    }
//
//}

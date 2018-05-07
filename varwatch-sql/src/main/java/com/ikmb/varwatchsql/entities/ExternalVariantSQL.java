///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.ikmb.varwatchsql.entities;
//
//import com.ikmb.varwatchsql.matching.MatchSQL;
//import java.io.Serializable;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.Table;
//
///**
// *
// * @author bfredrich
// */
//@Entity
//@Table(name = "var_match2external_var")
//public class ExternalVariantSQL implements Serializable {
//
//    @Id
//    @GeneratedValue
//    private Long id;
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "varmatch_id", nullable = false)
//    private MatchSQL match;
//    @Column(name = "acc_nr")
//    private Long accNr;
//    @Column(name = "acc_ident")
//    private String accIdent;
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public MatchSQL getMatch() {
//        return match;
//    }
//
//    public void setMatch(MatchSQL match) {
//        this.match = match;
//    }
//
//    public Long getAccNr() {
//        return accNr;
//    }
//
//    public void setAccNr(Long accNr) {
//        this.accNr = accNr;
//    }
//
//    public String getAccIdent() {
//        return accIdent;
//    }
//
//    public void setAccIdent(String accIdent) {
//        this.accIdent = accIdent;
//    }
//    
//    
//}

package com.ikmb.matching;

///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.ikmb.externaldb;
//
//import com.ikmb.entities.DatasetSQL;
//import com.ikmb.entities.DatasetVWSQL;
//import com.ikmb.entities.MatchGroupSQL;
//import com.ikmb.entities.MatchSQL;
//import com.ikmb.entities.RefDatabaseSQL;
//import com.ikmb.entities.VariantSQL;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.List;
//
///**
// *
// * @author Broder
// */
//public class IdenticalGenericScreener implements DatabaseScreener {
//
//    private Connection _conn = null;
//    private List<MatchGroupSQL> _matches = new ArrayList<MatchGroupSQL>();
//    private DatasetVWSQL _dataset;
//    private RefDatabaseSQL _database;
//
//    @Override
//    public void initialize(RefDatabaseSQL database, DatasetVWSQL dataset) {
//        _database = database;
//        _dataset = dataset;
//        try {
//            _conn = DriverManager.getConnection("jdbc:mysql://localhost/" + database.getPath() + "?"
//                    + "user=root&password=yuBr3Pef");
//
//        } catch (SQLException ex) {
//            // handle any errors
//            System.out.println("SQLException: " + ex.getMessage());
//            System.out.println("SQLState: " + ex.getSQLState());
//            System.out.println("VendorError: " + ex.getErrorCode());
//        }
//    }
//
//    @Override
//    public DatasetVWSQL getDataset() {
//        return _dataset;
//    }
//
//    @Override
//    public RefDatabaseSQL getDatabase() {
//        return _database;
//    }
//
//    @Override
//    public void run() {
////        connect(sqlPath);
//
//        for (VariantSQL variant : _dataset.getVariants()) {
//            Statement stmt = null;
//            ResultSet rs = null;
//
//            try {
//                stmt = _conn.createStatement();
//                String select = "SELECT variant_id,seq_name,pos,ref,alt FROM variant where seq_name=" + variant.getChromosomeName() + " and pos=" + variant.getChromosomePos() + " and ref=\"" + variant.getReferenceBase() + "\" and alt=\"" + variant.getAlternateBase() + "\"";
//                System.out.println(select);
//                rs = stmt.executeQuery(select);
//                //while so lange wie gefundene varianten
//                while (rs.next()) {
//                    MatchSQL match = new MatchSQL();
//                    String accesionNumber = rs.getString("variant_id");
//                    match.setVariant(variant);
//                    match.setAccessionNr(accesionNumber);
//                    match.setAllelFrequency(0.0); //get this value!
//                    match.setDistance(0.0);
//
//                    MatchGroupSQL matchgrp = new MatchGroupSQL();
//                    matchgrp.setDatabase(_database);
//                    matchgrp.setMatches(new HashSet<MatchSQL>(Arrays.asList(match)));
//                    _matches.add(matchgrp);
//                }
//            } catch (SQLException ex) {
//                System.out.println("1SQLException: " + ex.getMessage());
//                System.out.println("1SQLState: " + ex.getSQLState());
//                System.out.println("1VendorError: " + ex.getErrorCode());
//            } finally {
//                if (rs != null) {
//                    try {
//                        rs.close();
//                    } catch (SQLException sqlEx) {
//                    } // ignore
//                    rs = null;
//                }
//                if (stmt != null) {
//                    try {
//                        stmt.close();
//                    } catch (SQLException sqlEx) {
//                    } // ignore
//                    stmt = null;
//                }
//            }
//        }
//    }
//
//    @Override
//    public List<MatchGroupSQL> getMatches() {
//        return _matches;
//    }
//}

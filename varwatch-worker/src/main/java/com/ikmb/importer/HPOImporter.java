///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.ikmb.importer;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.persistence.EntityManager;
//import javax.persistence.NoResultException;
//import javax.persistence.TypedQuery;
//
///**
// *
// * @author bfredrich
// */
//public class HPOImporter {
//
//    public static void main(String[] args) {
//        String fileName = "/home/ojunge/hpo_dist.txt";
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(fileName));
//            String line = br.readLine();
//
//            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/varwatch?"
//                    + "user=root&password=yuBr3Pef");
//            connection.setAutoCommit(false);
//            Statement statement = connection.createStatement();
//            Long id = 1l;
//            Map<String, Long> hpoIDMap = getHPOIDs();
//            long currentcount = 0;
//            while (line != null) {
//                String hpo1 = line.split(",")[0];
//                String hpo2 = line.split(",")[1];
//                Double dist = Double.parseDouble(line.split(",")[2]);
//
//                Long hpo1ID = null;
//                if (hpoIDMap.containsKey(hpo1)) {
//                    hpo1ID = hpoIDMap.get(hpo1);
//                }
//                Long hpo2ID = null;
//                if (hpoIDMap.containsKey(hpo2)) {
//                    hpo2ID = hpoIDMap.get(hpo2);
//                }
//
//                line = br.readLine();
//
//                if (hpo1ID == null || hpo2ID == null) {
//                    continue;
//                }
//
//                String insertdist = "INSERT INTO hpodist (id,distance) VALUES(" + id + "," + dist + ");";
//                String inserthpo1 = "INSERT INTO hpodist2hpo (hpodist_id,hpo_id) VALUES(" + id + "," + hpo1ID + ");";
//
//                statement.addBatch(insertdist);
//                statement.addBatch(inserthpo1);
//
//                if (!hpo1.equals(hpo2)) {
//                    String inserthpo2 = "INSERT INTO hpodist2hpo (hpodist_id,hpo_id) VALUES(" + id + "," + hpo2ID + ");";
//                    statement.addBatch(inserthpo2);
//                }
//
//                if (id % 100000 == 0) {
//                    int[] rows = statement.executeBatch();
//                    connection.commit();
//                    currentcount = currentcount + 100000;
//                    System.out.println(currentcount);
//                }
//                id++;
//            }
//            br.close();
//        } catch (IOException ex) {
//            Logger.getLogger(HPOImporter.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (SQLException ex) {
//            Logger.getLogger(HPOImporter.class.getName()).log(Level.SEVERE, null, ex);
//        } finally {
//        }
//    }
//
//    private static Long getHPOID(String hpo2) {
//        Long id = null;
//        EntityManager _em = PersistenceManager.INSTANCE.getEntityManager();
//        try {
//            _em.getTransaction().begin();
//            TypedQuery<Long> query = _em.createQuery("SELECT h.id FROM HPOTermSQL h WHERE h.identifier = :identifier", Long.class);
//            id = query.setParameter("identifier", hpo2).getSingleResult();
//        } catch (NoResultException nre) {
//            nre.printStackTrace();
//        } finally {
//            _em.getTransaction().commit();
//            _em.close();
//
//        }
//        return id;
//    }
//
//    private static Map<String, Long> getHPOIDs() {
//        Map<String, Long> hpoIDMap = new HashMap<String, Long>();
//        EntityManager _em = PersistenceManager.INSTANCE.getEntityManager();
//        _em.getTransaction().begin();
//        TypedQuery<HPOTermSQL> query = _em.createQuery("SELECT h FROM HPOTermSQL h", HPOTermSQL.class);
//        List<HPOTermSQL> hpoTerms = query.getResultList();
//        for (HPOTermSQL hpoTerm : hpoTerms) {
//            hpoIDMap.put(hpoTerm.getIdentifier(), hpoTerm.getId());
//        }
//        _em.getTransaction().commit();
//        _em.close();
//
//        return hpoIDMap;
//    }
//}

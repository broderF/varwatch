///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.ikmb.update.databasehelper;
//
//import com.ikmb.core.data.family.GeneFamily;
//import com.ikmb.core.data.gene.Gene;
//import com.ikmb.core.data.pathway.Pathway;
//import com.ikmb.varwatchsql.data.ensembl.EnsemblSQL;
//import com.ikmb.varwatchsql.data.family.FamilySQL;
//import com.ikmb.varwatchsql.data.gene.GeneMorbidSQL;
//import com.ikmb.varwatchsql.data.gene.GeneSQL;
//import com.ikmb.varwatchsql.data.transcript.TranscriptSQL;
//import com.ikmb.varwatchsql.entities.PathwaySQL;
//import java.io.BufferedReader;
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.persistence.EntityManager;
//import javax.persistence.TypedQuery;
//
///**
// *
// * @author bfredrich
// */
//public class EnsemblDatabaseHelper {
//    
//    public static void main(String[] args) {
//        EnsemblDatabaseHelper help = new EnsemblDatabaseHelper();
//        help.loadGeneSet();
//    }
//    
//    public void loadGeneSet() {
//        EnsemblSQL ensembl = getActiveEnsembl(Boolean.TRUE);
//        importGenesets(ensembl.getGenFile(), ensembl);
//    }
//    
//    public static EnsemblSQL getActiveEnsembl(Boolean needFile) {
//        EntityManager _em = PersistenceManager.INSTANCE.getEntityManager();
//        _em.getTransaction().begin();
//        TypedQuery<EnsemblSQL> query = _em.createQuery("SELECT s FROM EnsemblSQL s WHERE s.active = :active", EnsemblSQL.class);
//        EnsemblSQL ensembl = query.setParameter("active", true).getSingleResult();
//        if (needFile) {
//            ensembl.getGenFile();
//        }
//        _em.getTransaction().commit();
//        _em.close();
//        return ensembl;
//    }
//    
//    public static EnsemblSQL setFile(byte[] genFile, EnsemblSQL ensembl) {
//        EntityManager _em = PersistenceManager.INSTANCE.getEntityManager();
//        _em.getTransaction().begin();
//        TypedQuery<EnsemblSQL> query = _em.createQuery("SELECT s FROM EnsemblSQL s WHERE s.id = :id", EnsemblSQL.class);
//        ensembl = query.setParameter("id", ensembl.getId()).getSingleResult();
//        ensembl.setGenFile(genFile);
//        _em.getTransaction().commit();
//        _em.close();
//        return ensembl;
//    }
//    
//    public static EnsemblSQL getLatestEnsemblVersion() {
//        EntityManager _em = PersistenceManager.INSTANCE.getEntityManager();
//        _em.getTransaction().begin();
//        String select = "SELECT e FROM EnsemblSQL e WHERE e.timestamp = (SELECT MAX(ee.timestamp) FROM EnsemblSQL ee WHERE ee.name = e.name)";
//        TypedQuery<EnsemblSQL> query = _em.createQuery(select, EnsemblSQL.class);
//        EnsemblSQL ensemblSQL = query.getSingleResult();
//        _em.getTransaction().commit();
//        _em.close();
//        return ensemblSQL;
//    }
//    
//    public static void importGenesets(byte[] genFile, EnsemblSQL ensembl) {
//        InputStream is = null;
//        BufferedReader bfReader = null;
//        try {
//            EntityManager _em = PersistenceManager.INSTANCE.getEntityManager();
//            _em.getTransaction().begin();
//            is = new ByteArrayInputStream(genFile);
//            bfReader = new BufferedReader(new InputStreamReader(is));
//            String temp = null;
//            int i = 0;
//            
//            TypedQuery<EnsemblSQL> ensemblquery = _em.createQuery("SELECT s FROM EnsemblSQL s WHERE s.id = :id", EnsemblSQL.class);
//            ensembl = ensemblquery.setParameter("id", ensembl.getId()).getSingleResult();
//            
//            Gene gene = null;
//            Set<Pathway> pathways = new HashSet<>();
//            Set<GeneFamily> families = new HashSet<>();
//            
//            while ((temp = bfReader.readLine()) != null) {
//                String ident = temp.split(":")[0].trim();
////                String[] split = temp.split(":", 2);
//                String attributes = temp.split(":", 2)[1];
//                if (ident.equals("Gene")) {
//                    //this is the old gene
//                    if (gene != null) {
//                        gene.setPathways(pathways);
//                        gene.setFamilies(families);
//                        _em.persist(gene);
//                        pathways = new HashSet<>();
//                        families = new HashSet<>();
////                        if (i > 300) {
////                            break;
////                        }
////                        i++;
//                    }
//                    
//                    gene = parseGene(attributes);
//                    gene.setEnsemblVersion(ensembl);
//                    _em.persist(gene);
//                    _em.getTransaction().commit();
//                    _em.getTransaction().begin();
//                } else if (ident.equals("Transcript")) {
//                    TranscriptSQL trans = parseTranscript(attributes);
//                    trans.setGene(gene);
//                    _em.persist(trans);
//                } else if (ident.equals("Family")) {
//                    FamilySQL family = parseFamily(attributes);
//                    FamilySQL familyTmp = null;
//                    try {
//                        TypedQuery<FamilySQL> query = _em.createQuery("SELECT s FROM FamilySQL s WHERE s.name = :name", FamilySQL.class);
//                        familyTmp = query.setParameter("name", family.getName()).getSingleResult();
//                    } catch (Exception ex) {
//                        
//                    }
//                    if (familyTmp != null) {
//                        families.add(familyTmp);
//                        
//                    } else {
//                        families.add(family);
//                        _em.persist(family);
//                    }
//                    
//                } else if (ident.equals("Pathway")) {
//                    PathwaySQL pathway = parsePathway(attributes);
//                    PathwaySQL pathwayTmp = null;
//                    try {
//                        TypedQuery<PathwaySQL> query = _em.createQuery("SELECT s FROM PathwaySQL s WHERE s.name = :name", PathwaySQL.class);
//                        pathwayTmp = query.setParameter("name", pathway.getName()).getSingleResult();
//                    } catch (Exception ex) {
//                        
//                    }
//                    if (pathwayTmp != null) {
//                        pathways.add(pathwayTmp);
//                        
//                    } else {
//                        pathways.add(pathway);
//                        _em.persist(pathway);
//                    }
//                } else if (ident.equals("OMIM_GENE")) {
//                    System.out.println(attributes);
//                    String[] attributeArray = attributes.split(";");
//                    String omimGene = attributeArray[attributeArray.length - 1];
//                    gene.setOmimGene(omimGene.trim());
//                    _em.persist(gene);
//                } else if (ident.equals("OMIM_MORBID")) {
//                    String omimMorbid = attributes;
//                    GeneMorbidSQL geneMorb = new GeneMorbidSQL();
//                    geneMorb.setOmimMorbid(omimMorbid.trim());
//                    geneMorb.setGene(gene);
//                    _em.persist(geneMorb);
//                    gene.getMorbids().add(geneMorb);
//                    _em.persist(gene);
//                }
//            }
//            
//            _em.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (is != null) {
//                    is.close();
//                }
//            } catch (Exception ex) {
//                
//            }
//        }
//        
//    }
//    
//    private static GeneSQL parseGene(String attributes) {
//        
//        String[] split = attributes.split(",");
//        //ENSG00000198888,MT-ND1,HGNC,HGNC:7455
//        String name = split[0];
//        String symbol = split[1];
//        String symbolSource = split[2];
//        String accession = split[3];
//        
//        GeneSQL gene = new GeneSQL();
//        gene.setName(name);
//        gene.setSymbol(symbol);
//        gene.setSymbolSource(symbolSource);
//        gene.setAccessionID(accession);
//        
//        return gene;
//    }
//    
//    private static TranscriptSQL parseTranscript(String attributes) {
//        
//        String[] split = attributes.split(",");
//        //Transcript:ENST00000626153,protein_coding,1
//        String name = split[0];
//        String bioType = split[1];
//        String canonicalString = split[2];
//        String prot = split[3];
//        Boolean canonical = false;
//        if (canonicalString.equals("1")) {
//            canonical = true;
//        }
//        
//        TranscriptSQL transcript = new TranscriptSQL();
//        transcript.setName(name);
//        transcript.setBioType(bioType);
//        transcript.setCanonical(canonical);
//        transcript.setProtein(prot);
//        
//        return transcript;
//    }
//    
//    private static FamilySQL parseFamily(String attributes) {
//        
//        String[] split = attributes.split(",");
//        //3259,ORNITHINE CARBAMOYLTRANSFERASE MITOCHONDRIAL PRECURSOR EC_2.1.3.3 ORNITHINE TRANSCARBAMYLASE OTCASE
//        String name = split[0];
//        String description = split[1].substring(0,Math.min(split[1].length(), 1000));
//        
//        FamilySQL family = new FamilySQL();
//        family.setName(name);
//        family.setDescription(description);
//        
//        return family;
//    }
//    
//    private static PathwaySQL parsePathway(String attributes) {
//        String[] split = attributes.split(",");
//        //path:hsa05134
//        String name = split[0];
////        String description = split[1];
//
//        PathwaySQL pathway = new PathwaySQL();
//        pathway.setName(name);
//        
//        return pathway;
//    }
//    
//    private void loadFile() {
//        try {
//            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/varwatch?"
//                    + "user=root&password=yuBr3Pef");
//            String INSERT_PICTURE = "Update ensembl_version set gen_file = ? WHERE id=2";
//            
//            FileInputStream fis = null;
//            PreparedStatement ps = null;
//            try {
//                connection.setAutoCommit(false);
//                File file = new File("/home/bfredrich/E85.gene_set_with_pathways.txt");
//                fis = new FileInputStream(file);
//                ps = connection.prepareStatement(INSERT_PICTURE);
//                ps.setBinaryStream(1, fis, (int) file.length());
//                ps.executeUpdate();
//                connection.commit();
//            } catch (FileNotFoundException ex) {
//                Logger.getLogger(EnsemblDatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
//            } finally {
//                ps.close();
//                try {
//                    fis.close();
//                } catch (IOException ex) {
//                    Logger.getLogger(EnsemblDatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//            
//        } catch (SQLException ex) {
//            Logger.getLogger(EnsemblDatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//    }
//    
//}

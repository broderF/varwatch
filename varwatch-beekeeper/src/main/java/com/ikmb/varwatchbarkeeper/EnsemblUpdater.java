///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.ikmb.varwatchbarkeeper;
//
//import com.ikmb.beans.EnsemblSQL;
//import com.ikmb.databasehelper.EnsemblDatabaseHelper;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
///**
// *
// * @author bfredrich
// */
//public class EnsemblUpdater {
//
//    public static void main(String[] args) {
//        //erstmal nur von der aktiven Version die Datei holen und parsen und in die DB schieben. Später dnan noch automatisch checken,
//        //ob es eine neue Version gibt und alles updaten usw
//
//        EnsemblUpdater updater = new EnsemblUpdater();
////        updater.importFile();
//        updater.run();
//    }
//
//    private void run() {
//        EnsemblSQL activeEnsembl = EnsemblDatabaseHelper.getActiveEnsembl(true);
//        byte[] genFile = activeEnsembl.getGenFile();
//        EnsemblDatabaseHelper.importGenesets(genFile, activeEnsembl);
//    }
//
//    private void importFile() {
//        try {
//            Path path = Paths.get("/data/varwatch/data/gene_set/E82/E82.gene_set_with_pathways.txt");
//            byte[] data = Files.readAllBytes(path);
//            EnsemblSQL activeEnsembl = EnsemblDatabaseHelper.getActiveEnsembl(true);
//            EnsemblDatabaseHelper.setFile(data, activeEnsembl);
//        } catch (IOException ex) {
//            Logger.getLogger(EnsemblUpdater.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//}

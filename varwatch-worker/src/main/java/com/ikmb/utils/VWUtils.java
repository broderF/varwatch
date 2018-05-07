/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.utils;

import com.ikmb.EnsemblHelper;
import com.ikmb.varwatchcommons.entities.VWVariant;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bfredrich
 */
public class VWUtils {

    public void saveFile(byte[] vcfFile, String filePath) {
        File file = new File(filePath);
        file.setWritable(true, false);
        file.setReadable(true, false);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, false);
            fos.write(vcfFile);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EnsemblHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EnsemblHelper.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
                Logger.getLogger(EnsemblHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void saveVariantsToFile(List<VWVariant> variants, String filePath) {
        PrintWriter filePrinter = null;
        try {
            filePrinter = new PrintWriter(filePath);
            filePrinter.println("#CHROM\tPOS\tID\tREF\tALT\tQUAL\tFILTER\tINFO");
            for (VWVariant variant : variants) {
                String id = ".";
                if (variant.getVepIdentifier() != null) {
                    id = variant.getVepIdentifier();
                }
                filePrinter.println(variant.getReferenceName() + "\t" + variant.getStart() + "\t" + id + "\t" + variant.getReferenceBases() + "\t" + (variant.getAlternateBases() + "\t.\t.\t."));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VWUtils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            filePrinter.close();
        }
    }

    public void saveVariantsToFileWithBuffer(List<VWVariant> variants, String filePath) {
        BufferedWriter output = null;
        try {
            output = new BufferedWriter(new FileWriter(filePath, true));
//            output.append("#CHROM\tPOS\tID\tREF\tALT\tQUAL\tFILTER\tINFO");
            for (VWVariant variant : variants) {
                String id = ".";
                if (variant.getVepIdentifier() != null) {
                    id = variant.getVepIdentifier();
                }
                output.append(variant.getReferenceName() + "\t" + variant.getStart() + "\t" + id + "\t" + variant.getReferenceBases() + "\t" + (variant.getAlternateBases() + "\t.\t.\t.\n"));
            }   //Close Streams
            output.close();
        } catch (IOException ex) {
            Logger.getLogger(VWUtils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                output.close();
            } catch (IOException ex) {
                Logger.getLogger(VWUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

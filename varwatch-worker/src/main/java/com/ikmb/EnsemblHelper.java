/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb;

import com.ikmb.core.varwatchcommons.utils.ParserHelper;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 *
 * @author bfredrich
 */
public class EnsemblHelper {

    public static final String CONVERT_HGVS2VCF = "-database -refseq -convert vcf";

    public static String hgvs2vcf(String hgvs, String ensemblName) {
        EnsemblHelper helper = new EnsemblHelper();
        String folder = WorkerLauncher.injector.getConfiguration().getPathToTmp();
        String filePathInput = folder + "input_" + RandomStringUtils.randomAlphanumeric(10) + "_" + new DateTime().toString(DateTimeFormat.forPattern("ddMMyyyyHHmmss")) + ".hgvs";
        String vcf = null;
        try {
            PrintWriter writer = new PrintWriter(filePathInput);
            writer.print(hgvs);
            writer.close();
            String filePathOutput = folder + "output_" + RandomStringUtils.randomAlphanumeric(10) + "_" + new DateTime().toString(DateTimeFormat.forPattern("ddMMyyyyHHmmss")) + ".vcf";
            String command = CONVERT_HGVS2VCF;
            InputStream inputSream = helper.runEnsembl(filePathInput, filePathOutput, ensemblName, command);
            vcf = IOUtils.toString(inputSream, StandardCharsets.UTF_8);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ParserHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ParserHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vcf;
    }


    public InputStream runEnsembl(String input, String output, String ensemblName, String command) {
        //maybe more than one file, so there should be an unique id
        //path should be in a config file or the database
        //maybe change '--refseq' auch durch '--merged'
        String loadModule = "module load " + ensemblName;
//        String annotateFile = "variant_effect_predictor.pl -i " + pathToVCF + " -o " + pathToVEP + " --species human --merged --assembly GRCh38 --format vcf  --cache --dir /old.varwatch/tools/variant_effect/vep/cache/ --fork 2 --everything --host localhost --user ensembl_admin -password ensembladmin --force_overwrite --fields \"Uploaded_variation\",Gene,Feature,\"Feature_type\",Consequence,SYMBOL,SYMBOL_SOURCE,HGVSc,HGNC_ID,SIFT,PolyPhen";
        String annotateFile = "variant_effect_predictor.pl -i " + input + " -o " + output + " " + command;
        System.out.println(annotateFile);

//        EnsemblHelper ensemblHelper = new EnsemblHelper();
        System.out.println(executeCommand(loadModule + " && " + annotateFile));
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(output);
//            Files.deleteIfExists(Paths.get(input));
//            Files.deleteIfExists(Paths.get(output));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EnsemblHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EnsemblHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new BufferedInputStream(fileInputStream);
    }

    private Boolean executeCommand(String command) {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process p = runtime.exec(new String[]{"bash", "-l", "-c", command});
            String output = loadStream(p.getInputStream());
            String error = loadStream(p.getErrorStream());
            int rc = p.waitFor();
            Logger.getLogger(EnsemblHelper.class.getName()).log(Level.SEVERE, "Process ended with rc=" + rc);
            Logger.getLogger(EnsemblHelper.class.getName()).log(Level.SEVERE, "Standard Output:\n");
            Logger.getLogger(EnsemblHelper.class.getName()).log(Level.SEVERE, output);
            Logger.getLogger(EnsemblHelper.class.getName()).log(Level.SEVERE, "Standard Error:\n");
            Logger.getLogger(EnsemblHelper.class.getName()).log(Level.SEVERE, error);
        } catch (Exception ex) {
            Logger.getLogger(EnsemblHelper.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    private String loadStream(InputStream s) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(s));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

}

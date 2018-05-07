/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.extraction;

import com.google.inject.Inject;
import com.ikmb.varwatchcommons.entities.GenomicFeature;
import com.ikmb.varwatchcommons.entities.VWVariant;
import com.ikmb.EnsemblHelper;
import com.ikmb.WorkerLauncher;
import com.ikmb.varwatchcommons.utils.ParserHelper;
import com.ikmb.utils.VWConfiguration;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.RandomStringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 *
 * @author broder
 */
public class HgvsConverter {

    @Inject
    private VCFParser vcfParser;

    private List<GenomicFeature> genomicfeatures;
    private String moduleName;
    private List<String> errorVariants = new ArrayList<>();
    private List<VWVariant> mappedVariants;
    private String assembly;

    public List<GenomicFeature> mapVariants(List<GenomicFeature> genomicfeatures, String assembly) {
        this.genomicfeatures = genomicfeatures;
        this.assembly = assembly;
        this.run();
        return this.getMappedGenomicFeatures();
    }

    private void run() {
        Map<String, String> hgvsAssemblyMap = ParserHelper.json2hgvsMap(genomicfeatures,assembly);
        String folder = WorkerLauncher.injector.getConfiguration().getPathToTmp();
        for (String assembly : hgvsAssemblyMap.keySet()) {
            String filePathInput = folder + "input_" + RandomStringUtils.randomAlphanumeric(10) + "_" + new DateTime().toString(DateTimeFormat.forPattern("ddMMyyyyHHmmss")) + ".hgvs";
            String filePathError = folder + "error_" + RandomStringUtils.randomAlphanumeric(10) + "_" + new DateTime().toString(DateTimeFormat.forPattern("ddMMyyyyHHmmss")) + ".hgvs";
            try {
                //write the whole string into the file
                PrintWriter writer = new PrintWriter(filePathInput);
                writer.print(hgvsAssemblyMap.get(assembly));
                writer.close();
                String filePathOutput = folder + "output_hgvs_" + RandomStringUtils.randomAlphanumeric(10) + "_" + new DateTime().toString(DateTimeFormat.forPattern("ddMMyyyyHHmmss")) + ".vcf";

                //run the hgvs convertion script
                //TODO define global one config file
                String module = "module load " + moduleName;
                String modulPath = WorkerLauncher.injector.getConfiguration().getHgvs2GenomicPath();
                String command = modulPath + " --hgvs " + filePathInput + " --assembly " + assembly + " --output " + filePathOutput + " --error " + filePathError;
                System.out.println(command);
                executeCommand(module + " && " + command);

                //read outputfile, parse to genomic feature, delete all files
                mappedVariants = vcfParser.getVariantsFromFile(filePathOutput);

                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Nr of correct mapped Variants: " + mappedVariants.size());
//                for (VWVariant variant : mappedVariants) {
//                    variant.setAssembly(assembly);
//                }

                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Nr of error Variants while mapping: " + vcfParser.getErrorVariants());

                for (VWVariant variant : vcfParser.getErrorVariants()) {
                    errorVariants.add(variant.toCompactString());
                }

                for (String curVar : Files.readAllLines(Paths.get(filePathError), StandardCharsets.UTF_8)) {
                    errorVariants.add(curVar);
                }
//                Files.deleteIfExists(Paths.get(filePathInput));
//                Files.deleteIfExists(Paths.get(filePathOutput));
//                Files.deleteIfExists(Paths.get(filePathError));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ParserHelper.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(EnsemblHelper.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
            }

        }
    }

    public List<String> getErrorVariants() {
        return errorVariants;
    }

    public List<VWVariant> getMappedVariants() {
        return mappedVariants;
    }

    public List<GenomicFeature> getMappedGenomicFeatures() {
        List<GenomicFeature> mappedFeatures = new ArrayList<>();
        for (VWVariant variant : mappedVariants) {
            GenomicFeature feature = new GenomicFeature();
            feature.setVariant(variant);
            mappedFeatures.add(feature);
        }
        return mappedFeatures;
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

    public void setEnsemblVersion(String ensemblVersion) {
        this.moduleName = ensemblVersion;
    }

    public void setVcfParser(VCFParser vcfParser) {
        this.vcfParser = vcfParser;
    }
}

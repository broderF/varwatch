/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.extraction;

import com.google.inject.Inject;
import com.ikmb.WorkerLauncher;
import com.ikmb.varwatchcommons.entities.GenomicFeature;
import com.ikmb.varwatchcommons.entities.VWVariant;
import com.ikmb.utils.VWUtils;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.RandomStringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 *
 * @author bfredrich
 */
public class AssemblyCrossMapper {

    private String target;
    private Map<String, List<VWVariant>> rawVariants = new HashMap<>();

    @Inject
    private VCFParser vcfParser;

    @Inject
    private VWUtils vwUtils;
    private List<VWVariant> parsedVariants = new ArrayList<>();
    private List<VWVariant> errorVariants = new ArrayList<>();

    public void setTarget(String target) {
        this.target = target;
    }

    public void setVariants(List<VWVariant> variants, String assembly) {
        for (VWVariant variant : variants) {
            addVariant(variant, assembly);
        }
    }

    public void setGenomicFeatures(List<GenomicFeature> genomicFeatures, String assembly) {
        for (GenomicFeature feature : genomicFeatures) {
            addVariant(feature.getVariant(), assembly);
        }
    }

    private void addVariant(VWVariant variant, String assembly) {
        List<VWVariant> variantList = new ArrayList<VWVariant>();
        if (rawVariants.containsKey(assembly)) {
            variantList.addAll(rawVariants.get(assembly));
        }
        variantList.add(variant);
        rawVariants.put(assembly, variantList);
    }

    public void crossmap() {
        for (Map.Entry<String, List<VWVariant>> entry : rawVariants.entrySet()) {
            String source = entry.getKey();
            List<VWVariant> variants = entry.getValue();
            if (source.equals(target)) {
                parsedVariants.addAll(variants);
                continue;
            }

            try {
                String fileName = RandomStringUtils.randomAlphanumeric(10) + "_" + new DateTime().toString(DateTimeFormat.forPattern("ddMMyyyyHHmmss"));
                String varInputName = WorkerLauncher.injector.getConfiguration().getPathToTmp() + "input_crosmap_" + fileName + ".vcf";
                String varOutputUnmapName = WorkerLauncher.injector.getConfiguration().getPathToTmp() + "output_crossmap_" + fileName + ".vcf.unmap";
                String varOutputName = WorkerLauncher.injector.getConfiguration().getPathToTmp() + "output_crossmap_" + fileName + ".vcf";

//                String vcfFile = vcfParser.getVCFString(variants);
//                vwUtils.saveFile(vcfFile.getBytes(), varInputName);
                vwUtils.saveVariantsToFileWithBuffer(variants, varInputName);

                String loadModule = "module load Crossmap";
                String modulPath = WorkerLauncher.injector.getConfiguration().getCrossmapPath();
                String command = "ruby " + modulPath + " --source " + source + " --target " + target + " --infile " + varInputName + " --outfile " + varOutputName;
                System.out.println(command);
                Runtime runtime = Runtime.getRuntime();
                Process p = runtime.exec(new String[]{"bash", "-l", "-c", loadModule + " && " + command});
                int rc = p.waitFor();
                parsedVariants.addAll(vcfParser.getVariantsFromFile(varOutputName));

                errorVariants.addAll(vcfParser.getVariantsFromFile(varOutputUnmapName));

//                Files.deleteIfExists(Paths.get(varInputName));
//                Files.deleteIfExists(Paths.get(varOutputName));
//                Files.deleteIfExists(Paths.get(varOutputUnmapName));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(AssemblyCrossMapper.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(AssemblyCrossMapper.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(AssemblyCrossMapper.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(AssemblyCrossMapper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

//    public List<VWGenomicFeature> mapGenomicFeatures(List<VWGenomicFeature> genomicFeatures, String target, List<VWVariant> errorVariants) {
//        List<VWGenomicFeature> mappedVariants = new ArrayList<VWGenomicFeature>();
//        for (VWGenomicFeature curFeature : genomicFeatures) {
//            VWVariant curVariant = curFeature.getVariant();
//            if (curVariant.getAssembly().equals(VWConfiguration.STANDARD_COORDS)) {
//                mappedVariants.add(curFeature);
//            } else {
//                List<VWVariant> tmpvariants = new ArrayList<VWVariant>();
//                tmpvariants.add(curVariant);
//                List<VWVariant> responsevariants = mapVariantsFromAssembly(tmpvariants, curVariant.getAssembly(), target);
//                if (responsevariants.size() == 1) {
//                    curVariant = responsevariants.get(0);
//                    curVariant.setAssembly(VWConfiguration.STANDARD_COORDS);
//                    curFeature.setVariant(curVariant);
//                    mappedVariants.add(curFeature);
//                } else {
//                    errorVariants.add(curVariant);
//                }
//            }
//        }
//        return mappedVariants;
//    }
    public List<VWVariant> getErrorVariants() {
        return errorVariants;
    }

    public void setVcfParser(VCFParser vcfParser) {
        this.vcfParser = vcfParser;
    }

    public void setVwUtils(VWUtils vwUtils) {
        this.vwUtils = vwUtils;
    }

    public List<VWVariant> getMappedVariants() {
        return parsedVariants;
    }

    public List<GenomicFeature> getMappedFeatures() {
        List<GenomicFeature> features = new ArrayList<>();
        for (VWVariant variant : parsedVariants) {
            GenomicFeature feature = new GenomicFeature();
            feature.setVariant(variant);
            features.add(feature);
        }
        return features;
    }

    public static void main(String[] args) throws Exception {
        String server = "https://rest.ensembl.org";
        String ext = "/map/human/GRCh37/X:1000000..1000100:1/GRCh38?";
        URL url = new URL(server + ext);

        URLConnection connection = url.openConnection();
        HttpURLConnection httpConnection = (HttpURLConnection) connection;

        httpConnection.setRequestProperty("Content-Type", "application/json");

        InputStream response = connection.getInputStream();
        int responseCode = httpConnection.getResponseCode();

        if (responseCode != 200) {
            throw new RuntimeException("Response code was not 200. Detected response was " + responseCode);
        }

        String output;
        Reader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(response, "UTF-8"));
            StringBuilder builder = new StringBuilder();
            char[] buffer = new char[8192];
            int read;
            while ((read = reader.read(buffer, 0, buffer.length)) > 0) {
                builder.append(buffer, 0, read);
            }
            output = builder.toString();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException logOrIgnore) {
                    logOrIgnore.printStackTrace();
                }
            }
        }

        System.out.println(output);
    }

}

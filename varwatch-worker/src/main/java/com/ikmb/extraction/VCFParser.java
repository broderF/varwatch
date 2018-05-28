/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.extraction;

import com.google.inject.Inject;
import com.ikmb.core.varwatchcommons.entities.VWVariant;
import static com.ikmb.core.utils.VariantHash.MAX_BASE_LENGTH;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author fredrich
 */
public class VCFParser {

    private List<String> _header = new ArrayList<String>();
    private List<VWVariant> _variants = new ArrayList<VWVariant>();
    @Inject
    private VariantIndelChecker variantIndelChecker;
    private List<VWVariant> _variantsWithMaxIndelExeeded = new ArrayList<VWVariant>();
    private List<VWVariant> _errorVariants = new ArrayList<VWVariant>();

    public List<VWVariant> getErrorVariants() {
        return _errorVariants;
    }

    public List<VWVariant> getVariantsFromFile(String fileName) {
        InputStream fileInputStream = null;
        List<VWVariant> variants = new ArrayList<>();
        try {
            fileInputStream = new FileInputStream(fileName);
            this.run(IOUtils.toByteArray(fileInputStream));
            variants = this.getVariants();
            fileInputStream.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VCFParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(VCFParser.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fileInputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(VCFParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return variants;
    }

    public void run(byte[] vcfFile) {
        _header = new ArrayList<String>();
        _variants = new ArrayList<VWVariant>();
        InputStream vcfInput = new ByteArrayInputStream(vcfFile);
        try {
            parse(vcfInput);
        } catch (IOException ex) {
            Logger.getLogger(VCFParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void parse(InputStream vcfInput) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(vcfInput));
        String line = null;

//        StringBuilder responseData = new StringBuilder();
        while ((line = in.readLine()) != null) {
            //ALt could be more cases, save them in seperate vpis?
            //get the genotype, phenotype
            if (!line.startsWith("#")) {
                Map<String, String> variantInformation = new HashMap<String, String>();
                StringTokenizer tokenizer = new StringTokenizer(line);
                variantInformation.put("CHROM", tokenizer.nextToken());
                variantInformation.put("POS", tokenizer.nextToken());
                variantInformation.put("ID", tokenizer.nextToken());
                variantInformation.put("REF", tokenizer.nextToken());
                variantInformation.put("ALT", tokenizer.nextToken());
//                variantInformation.put("QUAL", tokenizer.nextToken());
//                variantInformation.put("FILTER", tokenizer.nextToken());
//                variantInformation.put("INFO", tokenizer.nextToken());

                VWVariant variant = new VWVariant();
                if (line.length() > 10000) {
                    variant.setReferenceName(getChromosomeName(variantInformation));
                    variant.setStart(getChromosomePos(variantInformation));
                    String referenceBase = getReferenceBase(variantInformation).substring(0, Math.min(MAX_BASE_LENGTH, getReferenceBase(variantInformation).length()));
                    variant.setReferenceBases(referenceBase);
                    String alternateBase = getAlternateBase(variantInformation).substring(0, Math.min(MAX_BASE_LENGTH, getAlternateBase(variantInformation).length()));
                    variant.setAlternateBases(alternateBase);
                    String identifier = getIdentifier(variantInformation);
                    if (identifier.equals(".")) {
                        identifier = variant.getReferenceName() + "_" + variant.getStart() + "_" + variant.getReferenceBases() + "/" + variant.getAlternateBases();
                    }
                    variant.setVepIdentifier(identifier);
                    _errorVariants.add(variant);
                } else {
                    variant.setReferenceName(getChromosomeName(variantInformation));
                    variant.setStart(getChromosomePos(variantInformation));
                    variant.setReferenceBases(getReferenceBase(variantInformation));
                    variant.setAlternateBases(getAlternateBase(variantInformation));
                    String identifier = getIdentifier(variantInformation);
                    if (identifier.equals(".")) {
                        identifier = variant.getReferenceName() + "_" + variant.getStart() + "_" + variant.getReferenceBases() + "/" + variant.getAlternateBases();
                    }
                    variant.setVepIdentifier(identifier);
                    _variants.add(variant);
                }
//                variant.s(getFilter(variantInformation));
//                variant.setQuality(getQuality(variantInformation));
            }
            //verschiedene Info Parser, Format Parser usw??
//            if (line.startsWith("#CHROM")) {
//                StringTokenizer tokenizer = new StringTokenizer(line);
//                while (tokenizer.hasMoreTokens()) {
//                    _header.add(tokenizer.nextToken());
//                }
//                parsedHeader = true;
//            }
        }
    }

    public String getVCFString(List<VWVariant> variants) {
        StringBuilder vcfString = new StringBuilder();
        vcfString.append("#CHROM\tPOS\tID\tREF\tALT\tQUAL\tFILTER\tINFO\n");
        for (VWVariant variant : variants) {
            String id = ".";
            if (variant.getVepIdentifier() != null) {
                id = variant.getVepIdentifier();
            }
            vcfString.append(variant.getReferenceName()).append("\t").append(variant.getStart()).append("\t").append(id).append("\t").append(variant.getReferenceBases()).append("\t").append(variant.getAlternateBases()).append("\t.\t.\t.\n");
        }
        return vcfString.toString();
    }

    private String getChromosomeName(Map<String, String> variantInformation) {
        return variantInformation.get("CHROM");
    }

    private String getIdentifier(Map<String, String> variantInformation) {
        return variantInformation.get("ID");
    }

    private Integer getChromosomePos(Map<String, String> variantInformation) {
        return Integer.parseInt(variantInformation.get("POS"));
    }

    private String getReferenceBase(Map<String, String> variantInformation) {
        return variantInformation.get("REF");
    }

    private String getAlternateBase(Map<String, String> variantInformation) {
        return variantInformation.get("ALT");
    }

    private String getFilter(Map<String, String> variantInformation) {
        return variantInformation.get("FILTER");
    }

    private Integer getQuality(Map<String, String> variantInformation) {
        Integer quality = null;
        try {
            quality = Integer.parseInt(variantInformation.get("QUAL"));
        } catch (NumberFormatException ex) {
        }
        return quality;
    }

    public List<VWVariant> getVariants() {
        return _variants;
    }

}

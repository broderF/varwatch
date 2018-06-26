/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.tools.variant_parser;

import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.data.variant.Variant;
import static com.ikmb.core.utils.VariantHash.MAX_BASE_LENGTH;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
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

/**
 *
 * @author broder
 */
public class VCFVariantParser implements VariantParser {

    @Override
    public List<Variant> getVariants(DatasetVW dataset) {
        InputStream vcfInput = new ByteArrayInputStream(dataset.getRawData());
        return parseVariants(vcfInput);
    }

    private List<Variant> parseVariants(InputStream vcfInput) {
        BufferedReader in = new BufferedReader(new InputStreamReader(vcfInput));
        String line;
        List<Variant> variants = new ArrayList<>();

        try {
            while ((line = in.readLine()) != null) {
                if (!line.startsWith("#")) {
                    Map<String, String> variantInformation = new HashMap<>();
                    StringTokenizer tokenizer = new StringTokenizer(line);
                    variantInformation.put("CHROM", tokenizer.nextToken());
                    variantInformation.put("POS", tokenizer.nextToken());
                    variantInformation.put("ID", tokenizer.nextToken());
                    variantInformation.put("REF", tokenizer.nextToken());
                    variantInformation.put("ALT", tokenizer.nextToken());

                    Variant variant = new Variant();
                    variant.setChromosomeName(getChromosomeName(variantInformation));
                    variant.setChromosomePos(getChromosomePos(variantInformation));
                    String referenceBase = getReferenceBase(variantInformation).substring(0, Math.min(MAX_BASE_LENGTH, getReferenceBase(variantInformation).length()));
                    variant.setReferenceBase(referenceBase);
                    String alternateBase = getAlternateBase(variantInformation).substring(0, Math.min(MAX_BASE_LENGTH, getAlternateBase(variantInformation).length()));
                    variant.setAlternateBase(alternateBase);
                    String identifier = getIdentifier(variantInformation);
                    if (identifier.equals(".")) {
                        identifier = variant.getChromosomeName() + "_" + variant.getChromosomePos() + "_" + variant.getReferenceBase() + "/" + variant.getAlternateBase();
                    }
                    variant.setVEPIdentifier(identifier);
                    if (line.length() > 10000) {
                        variant.setFilter("not pass: too long");
                    } else {
                        variant.setFilter("pass");
                    }
                    variants.add(variant);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(VCFVariantParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return variants;
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
}

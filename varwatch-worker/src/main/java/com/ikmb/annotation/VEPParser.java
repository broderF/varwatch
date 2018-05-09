/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.annotation;

import com.ikmb.core.data.varianteffect.VariantEffect;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
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

/**
 *
 * @author Broder
 */
public class VEPParser {

    List<String> _header = new ArrayList<String>();
    List<VariantEffect> _variantEffects = new ArrayList<VariantEffect>();
    private int MAX_CHAR = 50;
    private Map<String, List<VariantEffect>> _variantEffectMap = new HashMap<>();

    public void parse(InputStream annotatedFile) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(annotatedFile));
        String line;
        boolean parsedHeader = false;
        while ((line = in.readLine()) != null) {
            //ALt could be more cases, save them in seperate vpis?
            //get the genotype, phenotype
            if (parsedHeader) {
                Map<String, String> variantInformation = new HashMap<String, String>();
                StringTokenizer tokenizer = new StringTokenizer(line);
                for (String header : _header) {
                    variantInformation.put(header, tokenizer.nextToken());
                }
                Boolean canonical = getCanonical(variantInformation);
                if (!canonical) {
                    continue;
                }
                VariantEffect variantEffect = new VariantEffect();
                String uploadedVar = getUploaded_variation(variantInformation);
                variantEffect.setUploaded_variation(uploadedVar);
                variantEffect.setConsequence(getConsequence(variantInformation));
                variantEffect.setImpactFactor(getImpactFactor(variantInformation));
                variantEffect.setFeatureName(getFeature(variantInformation));

                variantEffect.setPolyphen(getPolyphen(variantInformation));
                variantEffect.setSift(getSift(variantInformation));

                variantEffect.setCanonicalTranscript(getCanonicalTranscript(variantInformation));
                variantEffect.setHgvs_c(getHgvs_c(variantInformation));
                variantEffect.setLoftee(getLoftee(variantInformation));

                String protein = getProteinPosition(variantInformation);
                if (protein.contains("-")) {
                    String[] splittedPos = protein.split("-");
                    if (splittedPos.length == 0) {
                        variantEffect.setProtein_start(null);
                        variantEffect.setProtein_end(null);
                    } else {
                        variantEffect.setProtein_start(Integer.parseInt(splittedPos[0]));
                        variantEffect.setProtein_end(Integer.parseInt(splittedPos[1]));
                    }
                } else {
                    variantEffect.setProtein_start(Integer.parseInt(protein));
                    variantEffect.setProtein_end(Integer.parseInt(protein));
                }

                String cds = getCDSPosition(variantInformation);
                if (cds.contains("-")) {
                    String[] splittedPos = cds.split("-");
                    if (splittedPos.length == 0) {
                        variantEffect.setCds_start(null);
                        variantEffect.setCds_end(null);
                    } else {
                        variantEffect.setCds_start(Integer.parseInt(splittedPos[0]));
                        variantEffect.setCds_end(Integer.parseInt(splittedPos[1]));
                    }
                } else {
                    variantEffect.setCds_start(Integer.parseInt(cds));
                    variantEffect.setCds_end(Integer.parseInt(cds));
                }

                if (_variantEffectMap.containsKey(uploadedVar)) {
                    _variantEffectMap.get(uploadedVar).add(variantEffect);
                } else {
                    List<VariantEffect> effectList = new ArrayList<>();
                    effectList.add(variantEffect);
                    _variantEffectMap.put(uploadedVar, effectList);
                }
                _variantEffects.add(variantEffect);
            }
            //verschiedene Info Parser, Format Parser usw??
            if (line.startsWith("#Uploaded_variation")) {
                StringTokenizer tokenizer = new StringTokenizer(line);
                while (tokenizer.hasMoreTokens()) {
                    _header.add(tokenizer.nextToken());
                }
                parsedHeader = true;
            }
        }
    }

    public List<VariantEffect> getVariantEffects() {
        return _variantEffects;
    }

    public Map<String, List<VariantEffect>> getVariantEffectMap() {
        return _variantEffectMap;
    }

    private String getUploaded_variation(Map<String, String> variantInformation) {
        return variantInformation.get("#Uploaded_variation");
    }

    private String getConsequence(Map<String, String> variantInformation) {
        return variantInformation.get("Consequence");
    }

    private String getCDSPosition(Map<String, String> variantInformation) {
        String cds = variantInformation.get("CDS_position");
        return cds;
    }

    private String getLoftee(Map<String, String> variantInformation) {
        String cds = variantInformation.get("LoF");
        if(cds.equals("-")){
            return null;
        }
        return cds;
    }

    private String getProteinPosition(Map<String, String> variantInformation) {
        String protein_pos = variantInformation.get("Protein_position");
        return protein_pos;
    }

    private String getFeature(Map<String, String> variantInformation) {
        return variantInformation.get("Feature");
    }

    private Double getSift(Map<String, String> variantInformation) {
        Double val = null;
        String sift = variantInformation.get("SIFT");
        try {
            int start = sift.indexOf("(");
            int end = sift.indexOf(")");
            String score = sift.substring(start + 1, end);
            val = Double.parseDouble(score);
        } catch (Exception ex) {

        }
        return val;
    }

    private Double getPolyphen(Map<String, String> variantInformation) {
        Double val = null;

        String polyphen = variantInformation.get("PolyPhen");
        try {
            int start = polyphen.indexOf("(");
            int end = polyphen.indexOf(")");
            String score = polyphen.substring(start + 1, end);

            val = Double.parseDouble(score);
        } catch (Exception ex) {

        }
        return val;
    }

    private Boolean getCanonical(Map<String, String> variantInformation) {
        Boolean val = false;

        String canonical = variantInformation.get("CANONICAL");
        try {
            if (canonical.equals("YES")) {
                val = true;
            }
        } catch (Exception ex) {

        }
        return val;
    }

    private String getFeature_type(Map<String, String> variantInformation) {
        return variantInformation.get("Feature_type");
    }

    private String getGene(Map<String, String> variantInformation) {
        return variantInformation.get("Gene");
    }

    private String getHgnc_id(Map<String, String> variantInformation) {
        return variantInformation.get("HGNC_ID");
    }

    private String getHgvs_c(Map<String, String> variantInformation) {
        return variantInformation.get("HGVSc");
    }

    private String getSymbol(Map<String, String> variantInformation) {
        return variantInformation.get("SYMBOL");
    }

    private String getSymbol_source(Map<String, String> variantInformation) {
        return variantInformation.get("SYMBOL_SOURCE");
    }

    public static void main(String[] args) {
        VEPParser parser = new VEPParser();
        FileInputStream fileInputStream = null;
        String pathToVEP = "C:\\Users\\Broder\\Downloads\\annotateRefseq.vep";
        try {
            fileInputStream = new FileInputStream(pathToVEP);
            parser.parse(new BufferedInputStream(fileInputStream));
            List<VariantEffect> variantEffects = parser.getVariantEffects();
            for (VariantEffect ve : variantEffects) {
                System.out.println(ve.getSift());
                System.out.println(ve.getPolyphen());
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VEPAnnotater.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(VEPParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Boolean getCanonicalTranscript(Map<String, String> variantInformation) {
        return variantInformation.get("CANONICAL").equals("YES");
    }

    private String getImpactFactor(Map<String, String> variantInformation) {
        return variantInformation.get("IMPACT");
    }
}
